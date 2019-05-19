package com.example.administrator.self;

import android.content.Intent;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.self.ImageLoader.DoubleCache;
import com.example.administrator.self.ImageLoader.ImageLoader;
import com.example.administrator.self.model.Song;

import java.util.ArrayList;
import java.util.List;

public class DailyActivity extends AppCompatActivity implements View.OnClickListener {
    private ConstraintLayout dailySongList; // 点击跳转到推荐歌单页面
    private ImageView dailyFace;
    private TextView  dailyTitle;
    private TextView dailyContent;
    private ImageView dailyFirstimg;
    private TextView dailyFirstSongName;
    private TextView dailyFirstSinger;
    ImageView back;
    private List<Song> songList = new ArrayList<>();

    ImageLoader imageLoader = new ImageLoader();
    DoubleCache mDoubleCache = new DoubleCache();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        imageLoader.setmImageCache(mDoubleCache);
        init();
    }

    private void init() {
        dailySongList = findViewById(R.id.constraintLayout);
        Intent intent = getIntent();
        songList = (ArrayList<Song>)intent.getExtras().getSerializable("songs");
        dailySongList.setOnClickListener(this);
        dailyFace = findViewById(R.id.dialy_face);
        imageLoader.displayImage(songList.get(0).getCoverImg(), dailyFace);
        dailyTitle = findViewById(R.id.daily_title);
        dailyTitle.setText(songList.get(0).getPlayListName());
        dailyContent = findViewById(R.id.daily_content);
        dailyContent.setText(songList.get(0).getDescription());
        dailyFirstimg = findViewById(R.id.daily_firstimg);
        imageLoader.displayImage(songList.get(0).getPicaddress(), dailyFirstimg);
        dailyFirstSongName = findViewById(R.id.daily_firstSongName);
        dailyFirstSongName.setText(songList.get(0).getSongName());
        dailyFirstSinger = findViewById(R.id.daily_firstSinger);
        dailyFirstSinger.setText(songList.get(0).getSinger());
        back = findViewById(R.id.daily_back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.constraintLayout:
                Intent intent = new Intent(DailyActivity.this,DailyListActivity.class);
                intent.putParcelableArrayListExtra("songlist", (ArrayList<? extends Parcelable>) songList);
                startActivity(intent);
                finish();
                break;
            case R.id.daily_back:
                finish();
        }
    }
}
