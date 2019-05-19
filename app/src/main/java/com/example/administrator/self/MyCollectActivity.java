package com.example.administrator.self;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.self.adapter.CollectAdapter;
import com.example.administrator.self.adapter.DailyAdapter;
import com.example.administrator.self.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyCollectActivity extends AppCompatActivity {
    private ListView collectList;
    private List<Song> songList = new ArrayList<>();
    private CollectAdapter adapter;
    private ImageView back;
    MyService myService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = ((MyService.MyBinder) service).getService();
//            try {
//                myService.setMediaPlayer(address);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
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
        setContentView(R.layout.activity_my_collect);
        Intent intent = getIntent();
        songList = (ArrayList<Song>) intent.getExtras().getSerializable("collect");
        init();
        bindServiceConnection();
    }

    private void init() {
        back = findViewById(R.id.collect_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collectList = findViewById(R.id.collect_list);
        adapter = new CollectAdapter(this,R.layout.collect_list_item,songList);
        collectList.setAdapter(adapter);
        collectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    myService.setMediaPlayer(Constant.address4 + songList.get(position).getSongId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MyCollectActivity.this,SongDetialActivity.class);
                intent.putParcelableArrayListExtra("detial", (ArrayList<? extends Parcelable>) songList);
                intent.putExtra("index",position);
                startActivity(intent);
                finish();
            }
        });
    }
}
