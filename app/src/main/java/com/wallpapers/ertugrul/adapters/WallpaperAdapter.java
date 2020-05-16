package com.wallpapers.ertugrul.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.wallpapers.ertugrul.R;
import com.wallpapers.ertugrul.model.Wallpaper;

import java.util.ArrayList;

public class WallpaperAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Wallpaper> wallpapers_data;

    public WallpaperAdapter(Context context, ArrayList<Wallpaper> wallpapers_data) {
        this.context = context;
        this.wallpapers_data = wallpapers_data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.item_wallpaper, null);

//            // set value into textview
//            TextView textView = (TextView) gridView
//                    .findViewById(R.id.grid_item_label);
//            textView.setText(mobileValues[position]);

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.image);

            String mobile = wallpapers_data.get(position).getName();

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return wallpapers_data.size();
    }

    @Override
    public Wallpaper getItem(int position) {
        return wallpapers_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}