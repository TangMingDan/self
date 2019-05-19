package com.example.administrator.self.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.self.ImageLoader.DoubleCache;
import com.example.administrator.self.ImageLoader.ImageLoader;
import com.example.administrator.self.R;
import com.example.administrator.self.model.Song;

import java.util.List;

public class CollectAdapter extends ArrayAdapter<Song> {
    ImageLoader imageLoader = new ImageLoader();
    DoubleCache mDoubleCache = new DoubleCache();
    private int resourece;

    public CollectAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
        this.resourece = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song song = getItem(position);
        imageLoader.setmImageCache(mDoubleCache);
        View view = LayoutInflater.from(getContext()).inflate(resourece, parent, false);
        ImageView imageView = view.findViewById(R.id.item_collect_img);
        TextView textView = view.findViewById(R.id.item_collect_songName);
        TextView textView1 = view.findViewById(R.id.item_collect_singer);
        imageLoader.displayImage(song.getPicaddress(),imageView);
        textView.setText(song.getSongName());
        textView1.setText(song.getSinger());
        return view;
    }

}
