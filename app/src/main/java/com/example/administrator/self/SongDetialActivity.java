package com.example.administrator.self;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.self.ImageLoader.DoubleCache;
import com.example.administrator.self.ImageLoader.ImageLoader;
import com.example.administrator.self.Util.HttpCallbackListener;
import com.example.administrator.self.Util.HttpUtil;
import com.example.administrator.self.model.Song;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SongDetialActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView back; //返回主界面
    ImageView stop; // 暂停播放/继续播放
    ImageView last; //上一首歌曲
    ImageView next; //下一首歌曲
    SeekBar progress; //进度条
    TextView playingTime; //已播放时长
    TextView totalTime; //总时长
    ImageView collect; //收藏
    MyService myService;
    Song song; //主界面传递的song的信息
    private TextView songName;
    private TextView singer;
    private ImageView face;
    private int index = 0;  //当前播放歌曲在歌单中的位次
    ImageLoader imageLoader = new ImageLoader();
    DoubleCache mDoubleCache = new DoubleCache();


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = ((MyService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private List<Song> songList = new ArrayList<>();

    private void bindServiceConnection() {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        bindService(intent, connection, this.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detial);
        Intent intent = getIntent();
        songList = (ArrayList<Song>) intent.getExtras().getSerializable("detial");
        index = intent.getIntExtra("index", 0);
        song = songList.get(index);
        init();
        imageLoader.setmImageCache(mDoubleCache);
        bindServiceConnection();
    }

    private void init() {
        back = findViewById(R.id.detial_back);
        back.setOnClickListener(this);
        stop = findViewById(R.id.detial_stop);
        stop.setOnClickListener(this);
        last = findViewById(R.id.detial_lastsong);  //上一首歌曲
        last.setOnClickListener(this);
        next = findViewById(R.id.detial_nextsong); //下一首歌曲
        next.setOnClickListener(this);
        collect = findViewById(R.id.detial_collect); //收藏
        collect.setOnClickListener(this);
        progress = findViewById(R.id.detial_seekBar);
        progress.setProgress(MyService.mediaPlayer.getCurrentPosition());
        progress.setMax(MyService.mediaPlayer.getDuration());
        totalTime = findViewById(R.id.detial_totalTime);
        playingTime = findViewById(R.id.detial_playingTime);
        songName = findViewById(R.id.detial_songname);
        singer = findViewById(R.id.detial_author);
        face = findViewById(R.id.detial_face);
        handler.post(runnable);
        if (!MyService.mediaPlayer.isPlaying()) {
            stop.setImageResource(R.drawable.ic_play_pause);
        }
    }

    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(MyService.isCollect){
                collect.setImageResource(R.drawable.ic_star_on_blue);
            }else {
                collect.setImageResource(R.drawable.ic_star_off);
            }
            if (MyService.mediaPlayer.isPlaying()) {
                songName.setText(song.getSongName());
                singer.setText(song.getSinger());
                imageLoader.displayImage(song.getPicaddress(), face);
                playingTime.setText(time.format(MyService.mediaPlayer.getCurrentPosition()));
                totalTime.setText(time.format(MyService.mediaPlayer.getDuration()));
                progress.setProgress(MyService.mediaPlayer.getCurrentPosition());
                progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            myService.mediaPlayer.seekTo(seekBar.getProgress());

                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                handler.postDelayed(runnable, 100);
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detial_back:
                Intent intent = new Intent();
                intent.putExtra("index",index);
                intent.putParcelableArrayListExtra("detial", (ArrayList<? extends Parcelable>) songList);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.detial_stop:
                if (MyService.mediaPlayer.isPlaying()) {
                    MyService.mediaPlayer.pause();
                    stop.setImageResource(R.drawable.ic_play_pause);
                } else {
                    MyService.mediaPlayer.start();
                    stop.setImageResource(R.drawable.ic_play_running);
                }
                break;
            case R.id.detial_lastsong:
                if (index == 0) {
                    index = songList.size();
                }
                try {
                    index = index - 1;
                    song = songList.get(index);
                    myService.setMediaPlayer(Constant.address4 + song.getSongId());
                    ;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.detial_nextsong:
                if (index == songList.size()) {
                    index = -1;
                }
                try {
                    index = index + 1;
                    song = songList.get(index);
                    myService.setMediaPlayer(Constant.address4 + song.getSongId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.detial_collect:
               MyService.isCollect = !MyService.isCollect;
                String address = Constant.address6 + song.getSongId() + "&songName=" + song.getSongName()+
                        "&singer=" + song.getSinger() + "&picaddress=" + song.getPicaddress();
                Log.d("xxxxxx",address);
                HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        if(response.equals("success")){
                            Looper.prepare();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    collect.setImageResource(R.drawable.ic_star_on_blue);
                                }
                            });
                            Toast.makeText(SongDetialActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }else {
                            Looper.prepare();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    collect.setImageResource(R.drawable.ic_star_off);
                                }
                            });
                            Toast.makeText(SongDetialActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                    @Override
                    public void onError(Exception e) {
                        Looper.prepare();
                        Toast.makeText(SongDetialActivity.this,"请检查自己的网络",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });
                break;
            default:
                break;
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("index",index);
        intent.putParcelableArrayListExtra("detial", (ArrayList<? extends Parcelable>) songList);
        setResult(RESULT_OK,intent);
        finish();
    }
}
