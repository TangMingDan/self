package com.example.administrator.self.Util;

import android.util.Log;

import com.example.administrator.self.model.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utility {
    public static String sendSongListId(String response) {
        String id = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equals("200")) {
                JSONObject object = jsonObject.getJSONObject("data");
                id = object.getString("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static List<Song> sendSongId(String response) {
        List<Song> songList = null;
        int index;
        try {
            songList = new ArrayList<>();
            JSONObject object = new JSONObject(response);
            if (object.getInt("code") == 200) {
                JSONObject object1 = object.getJSONObject("playlist");
                JSONArray array = object1.getJSONArray("tracks");
                for (int i = 0; i < 35; i++) {
                    index = (int) (Math.random() * array.length());  //随机获取一首歌曲
                    JSONObject object2 = array.getJSONObject(index);
                    Song song = new Song();
                    if (i == 0) {
                        song.setDescription(object1.getString("description"));
                        Log.d("123456", song.getDescription());
                        song.setPlayListName(object1.getString("name"));
                        Log.d("123456", song.getPlayListName());
                        song.setCoverImg(object1.getString("coverImgUrl"));
                        Log.d("123456", song.getCoverImg());
                    }
                    song.setSongId(object2.getInt("id"));  //设置歌曲id
                    song.setSongName(object2.getString("name")); //设置歌曲名字
                    JSONArray array1 = object2.getJSONArray("ar");
                    for (int j = 0; j < array1.length(); j++) {
                        JSONObject object3 = array1.getJSONObject(j);
                        song.setSinger(object3.getString("name")); //设置歌手名字
                    }
                    JSONObject object4 = object2.getJSONObject("al");
                    song.setAlbumName(object4.getString("name")); //设置专辑名字
                    song.setPicaddress(object4.getString("picUrl")); //设置专辑图片
                    songList.add(song);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songList;
    }

    public static String handlyrics(String response) {
        String lyrics = null;
        try {
            JSONObject object = new JSONObject(response);
            if (object.getInt("code") == 200) {
                JSONObject object1 = object.getJSONObject("tlyric");
                lyrics = object1.getString("lyric");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lyrics;
    }

    public static List<Song> handCollect(String response) {
        List<Song> songList = new ArrayList<>();
        if (response != null) {
            String[] s1 = response.split(";");
            for (int i = 0; i < s1.length; i++) {
                String[] s2 = s1[i].split(",");
                Song song = new Song();
                song.setSongId(Integer.parseInt(s2[0]));
                song.setSongName(s2[1]);
                song.setSinger(s2[2]);
                song.setPicaddress(s2[3]);
                songList.add(song);
            }
        }
        return songList;
    }
}
