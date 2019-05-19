package com.example.administrator.self;

import android.content.Intent;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.self.Util.HttpCallbackListener;
import com.example.administrator.self.Util.HttpUtil;
import com.example.administrator.self.Util.Utility;
import com.example.administrator.self.model.Song;

import java.util.ArrayList;
import java.util.List;

public class startActivity extends AppCompatActivity {

    private String songListId; //歌单id
    private List<Song> songList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_start);
        sendforSongListId(Constant.address1 + "EXCITING");
        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    sleep(5000);//使程序休眠五秒
                    Intent it=new Intent(getApplicationContext(),MainActivity.class);//启动MainActivity
                    it.putParcelableArrayListExtra("detial", (ArrayList<? extends Parcelable>) songList);
                    startActivity(it);
                    finish();//关闭当前活动
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
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
                Toast.makeText(startActivity.this, "没有网络，请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

    public void sendforSongId() {
        HttpUtil.sendHttpRequest(Constant.address3 + songListId, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                songList = Utility.sendSongId(response);

            }

            @Override
            public void onError(Exception e) {
                Looper.prepare();
                Toast.makeText(startActivity.this, "没有网络，请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }
}

