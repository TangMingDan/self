package com.example.administrator.self.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private int songId;
    private String songName;
    private String singer;
    private String albumName; //专辑名字
    private String picaddress;
    private String description;
    private String playListName;
    private String coverImg; //歌单封面

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    public Song() {
    }

    public Song(int songId, String songName, String singer, String albumName,
                String picaddress, String description, String playListName, String coverImg) {
        this.songId = songId;
        this.songName = songName;
        this.singer = singer;
        this.albumName = albumName;
        this.picaddress = picaddress;
        this.description = description;
        this.playListName = playListName;
        this.coverImg = coverImg;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getPicaddress() {
        return picaddress;
    }

    public void setPicaddress(String picaddress) {
        this.picaddress = picaddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(songId);
        dest.writeString(songName);
        dest.writeString(singer);
        dest.writeString(albumName);
        dest.writeString(picaddress);
        dest.writeString(description);
        dest.writeString(playListName);
        dest.writeString(coverImg);
    }
//    protected Song(Parcel in) {
//        songId = in.readInt();
//        songName = in.readString();
//        singer = in.readString();
//        albumName = in.readString();
//        picaddress = in.readString();
//    }

    public static final Parcelable.Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in.readInt(),in.readString(),in.readString(),
                    in.readString(),in.readString(),in.readString(),in.readString(),in.readString());
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
