package com.example.administrator.self;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.self.ImageLoader.DoubleCache;
import com.example.administrator.self.ImageLoader.ImageLoader;
import com.example.administrator.self.Util.HttpCallbackListener;
import com.example.administrator.self.Util.HttpUtil;
import com.example.administrator.self.Util.Utility;
import com.example.administrator.self.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private ImageView setting; //展开drawerLayout
    private ImageView songDetial; //展开详细歌曲页面
    private TextView daily; //展开日推
    private TextView collect; //展开收藏
    private TextView ground; //展开广场
    private int[] mRes = {R.id.img_exciting, R.id.img_unhappy, R.id.happy, R.id.clam};
    private List<ImageView> mImageViews = new ArrayList<>();
    private boolean mFlag = true;
    private String songListId; //歌单id
    private String songId; //歌曲的Id
    private String songListMood = "EXCITING"; //心情歌单
    private List<Song> songList; //获取详细歌单信息
    private List<Song> songCollect = new ArrayList<>(); //获取收藏的歌单
    private Song song; //当前歌曲
    private String address; //歌曲地址

    private ImageView face; //主页面歌曲图片
    private TextView songName; //歌曲名字
    private TextView singer; //歌手名字

    private String lyric; //歌词
    //得到歌词存储位置
    String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    ImageLoader imageLoader = new ImageLoader();
    DoubleCache mDoubleCache = new DoubleCache();
    private int index = 0;

    MyService myService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = ((MyService.MyBinder) service).getService();
            try {
                myService.setMediaPlayer(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };

    private void bindServiceConnection() {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        bindService(intent, connection, this.BIND_AUTO_CREATE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();//初始化View
        Intent intent = getIntent();
        songList = (ArrayList<Song>) intent.getExtras().getSerializable("detial");
        address = Constant.address4 + songList.get(0).getSongId() + ".mp3";
        song = songList.get(0);
        bindServiceConnection();
        setView();
//        sendforSongListId(Constant.address1 + songListMood);//发送网络请求
        sendforcollect();
        imageLoader.setmImageCache(mDoubleCache);
    }

    private void sendforSongListId(String address) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                songListId = Utility.sendSongListId(response);
                sendforSongId();
            }

            @Override
            public void onError(Exception e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "没有网络，请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

    public void sendforSongId() {
        HttpUtil.sendHttpRequest(Constant.address3 + songListId, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                songList = Utility.sendSongId(response);
                address = Constant.address4 + songList.get(0).getSongId() + ".mp3";
                bindServiceConnection();
                Log.d("123456", address);
                setView();
                sendforlyric();
            }

            @Override
            public void onError(Exception e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "没有网络，请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

    private void sendforlyric() {
        HttpUtil.sendHttpRequest(Constant.address5 + songList.get(0).getSongId(), new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                lyric = Utility.handlyrics(response);
            }

            @Override
            public void onError(Exception e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "没有网络，请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

    private void setView() {
        song = songList.get(index);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageLoader.displayImage(song.getPicaddress(), face);
                songName.setText(song.getSongName());
                singer.setText(song.getSinger());
                startAnim2();
            }
        });
    }

    private void init() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        setting = findViewById(R.id.setting); //展开drawerLayout
        songDetial = findViewById(R.id.main_song_detial);
        songDetial.setOnClickListener(this);
        face = findViewById(R.id.main_img_face);
        songName = findViewById(R.id.main_songname);
        singer = findViewById(R.id.main_author);
        setting.setOnClickListener(this);
        collect = findViewById(R.id.txt_collect);
        collect.setOnClickListener(this);
        daily = findViewById(R.id.txt_daily);
        daily.setOnClickListener(this);
        ground = findViewById(R.id.txt_ground);
        ground.setOnClickListener(this);
        for (int i = 0; i < mRes.length; i++) {
            ImageView imageView = findViewById(mRes[i]);
            imageView.setOnClickListener(this);
            mImageViews.add(imageView);
        }
        face = findViewById(R.id.main_img_face);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_exciting:
                if (mFlag) {
                    startAnim();
                } else {
                    stopAnim();
                    songListMood = "EXCITING";
                    setMeadio();
                }
                break;
            case R.id.img_unhappy:
                songListMood = "UNHAPPY";
                setMeadio();
                break;
            case R.id.happy:
                songListMood = "HAPPY";
                setMeadio();
                break;
            case R.id.clam:
                setMeadio();
                break;
            case R.id.setting:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.main_song_detial:
                if (songList != null) {
                    Intent intent = new Intent(MainActivity.this, SongDetialActivity.class);
                    intent.putParcelableArrayListExtra("detial", (ArrayList<? extends Parcelable>) songList);
                    intent.putExtra("index", index);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.txt_ground:
                Log.d("1234567", "ground");
                break;
            case R.id.txt_collect:
                sendforcollect();
                Intent intent2 = new Intent(MainActivity.this, MyCollectActivity.class);
                intent2.putParcelableArrayListExtra("collect", (ArrayList<? extends Parcelable>) songCollect);
                startActivity(intent2);
                break;
            case R.id.txt_daily:
                sendforSongListId(Constant.address2);
                if (songList != null) {
                    Intent intent1 = new Intent(MainActivity.this, DailyActivity.class);
                    intent1.putParcelableArrayListExtra("songs", (ArrayList<? extends Parcelable>) songList);
                    startActivity(intent1);
                }
                break;
        }
    }

    private void sendforcollect() {
        HttpUtil.sendHttpRequest(Constant.address7, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                songCollect = Utility.handCollect(response);
                Log.d("234567", String.valueOf(songCollect.size()));
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void stopAnim() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mImageViews.get(1),
                "translationY", 300F, 0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mImageViews.get(2),
                "translationY", 600F, 0);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mImageViews.get(3),
                "translationY", 900F, 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.setInterpolator(new BounceInterpolator());
        set.playTogether(animator1, animator2, animator3);
        set.start();
        for (int i = 1; i < mRes.length; i++) {
            mImageViews.get(i).setVisibility(View.GONE);
        }
        mFlag = true;
    }

    private void startAnim() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mImageViews.get(1), "translationY", -180F);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mImageViews.get(2), "translationY", -360F);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mImageViews.get(3), "translationY", -540F);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.setInterpolator(new BounceInterpolator());
        set.playTogether(animator1, animator2, animator3);
        set.start();
        for (int i = 1; i < mRes.length; i++) {
            mImageViews.get(i).setVisibility(View.VISIBLE);
        }
        mFlag = false;
    }

    //旋转动画
    private void startAnim2() {
        if (MyService.mediaPlayer.isPlaying()) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(face, "rotation", 360);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(20000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            set.setInterpolator(new LinearInterpolator());
            set.playTogether(animator);
            set.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    songList = (ArrayList<Song>) data.getExtras().getSerializable("detial");
                    index = data.getIntExtra("index", 10);
                    song = songList.get(index);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setView();
                        }
                    });
                }
                break;
        }
    }

    public void setMeadio() {
        sendforSongListId(Constant.address1 + songListMood);
        if (songList != null) {
            try {
                myService.setMediaPlayer(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
