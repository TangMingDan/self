package com.example.administrator.self.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.self.R;
import com.example.administrator.self.model.Song;

import java.util.List;

public class DailyAdapter extends ArrayAdapter<Song> {
    private int resourece;

    public DailyAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
        this.resourece = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song song = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourece, parent, false);
        TextView text1 = view.findViewById(R.id.item_rank);
        TextView text2 = view.findViewById(R.id.item_songname);
        TextView text3 = view.findViewById(R.id.item_singer);
        if (position < 9) {
            text1.setText("0" + (position + 1));
            if(position <3){
                text1.setTextColor(Color.RED);
            }
        }else {
            text1.setText("" + (position + 1));
        }
        text2.setText(song.getSongName());
        text3.setText(song.getSinger());
        return view;
    }
    
}
