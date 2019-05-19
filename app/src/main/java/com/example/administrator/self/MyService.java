package com.example.administrator.self;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.io.IOException;

public class MyService extends Service {
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public final IBinder binder = new MyBinder();
    public static ObjectAnimator animator;
    public static boolean isCollect = false;
    public static int isReturnTo = 0;

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    public MyService() {

    }

    public void setMediaPlayer(String address) throws IOException {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this, Uri.parse(address));
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this, Uri.parse(address));
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void AnimatorAction(View view) {
        if (mediaPlayer.isPlaying()) {
            animator = ObjectAnimator.ofFloat(view,"rotation",5000);
            //匀速旋转
            animator.setInterpolator(new LinearInterpolator());
            //无限循环
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.start();
        }
    }
//    private int flag = 0;
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void playOrPause() {
//        flag++;
//        if (flag >= 1000) flag = 2;
//        if(mediaPlayer.isPlaying()){
//            mediaPlayer.pause();
//            animator.pause();
//        } else {
//            mediaPlayer.start();
//            if ((flag == 1) || (isReturnTo == 1)) {
//                animator.setDuration(5000);
//                animator.setInterpolator(new LinearInterpolator()); // 均速旋转
//                animator.setRepeatCount(ValueAnimator.INFINITE); // 无限循环
//                animator.start();
//            } else {
//                animator.resume();
//            }
//        }
//    }
}
