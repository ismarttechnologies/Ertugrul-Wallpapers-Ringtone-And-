package com.wallpapers.ertugrul.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wallpapers.ertugrul.R;
import com.wallpapers.ertugrul.activities.WallpaperDetail;
import com.wallpapers.ertugrul.adapters.WallpaperAdapter;
import com.wallpapers.ertugrul.model.PageViewModel;
import com.wallpapers.ertugrul.model.Wallpaper;

import java.util.ArrayList;

public class WallpaperFragment extends Fragment {
    private static final String TAG = "SpeedDial";
    private PageViewModel pageViewModel;

    GridView wallpapers_list;
    WallpaperAdapter mAdapter;
    ArrayList<Wallpaper> wallpapers_data;


    public WallpaperFragment() {
        // Required empty public constructor
    }
    /**
     * @return A new instance of fragment SpeedDialFragment.
     */
    public static WallpaperFragment newInstance() {
        return new WallpaperFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.setIndex(TAG);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.wallpaper_layout, container, false);

        wallpapers_list = (GridView) root.findViewById(R.id.wallpapers_list);

        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });

        populateData();


        return root;
    }


    public void populateData(){

        wallpapers_data = new ArrayList<Wallpaper>();
        wallpapers_data.add(new Wallpaper("1", "Wallpaper 1", ""));
        wallpapers_data.add(new Wallpaper("2", "Wallpaper 2", ""));
        wallpapers_data.add(new Wallpaper("3", "Wallpaper 3", ""));
        wallpapers_data.add(new Wallpaper("4", "Wallpaper 4", ""));
        wallpapers_data.add(new Wallpaper("5", "Wallpaper 5", ""));
        wallpapers_data.add(new Wallpaper("6", "Wallpaper 6", ""));
        wallpapers_data.add(new Wallpaper("7", "Wallpaper 7", ""));


        mAdapter = new WallpaperAdapter(getActivity(), wallpapers_data);
        wallpapers_list.setAdapter(mAdapter);


        wallpapers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                Toast.makeText(getActivity(), ((TextView) v.findViewById(R.id.grid_item_label)) .getText(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), wallpapers_data.get(position).getName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), WallpaperDetail.class);
                intent.putExtra("NAME", wallpapers_data.get(position).getName());
                intent.putExtra("URL", wallpapers_data.get(position).getImage());
                startActivity(intent);


            }
        });
    }
}