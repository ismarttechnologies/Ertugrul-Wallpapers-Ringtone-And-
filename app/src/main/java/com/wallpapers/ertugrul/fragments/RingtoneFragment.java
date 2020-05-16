package com.wallpapers.ertugrul.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wallpapers.ertugrul.R;
import com.wallpapers.ertugrul.adapters.RingtoneAdapter;
import com.wallpapers.ertugrul.model.PageViewModel;
import com.wallpapers.ertugrul.model.Status;

import java.util.ArrayList;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;

public class RingtoneFragment  extends Fragment implements RingtoneAdapter.OnItemListner {
    private static final String TAG = "SpeedDial";
    private PageViewModel pageViewModel;

    private RecyclerView status_listing;
    RingtoneAdapter adapter;
    ArrayList<Status> dataList;
    public static final int PERMISSION_WRITE = 0;

    CircleProgressView progress_bar;


    public RingtoneFragment() {
        // Required empty public constructor
    }
    /**
     * @return A new instance of fragment SpeedDialFragment.
     */
    public static RingtoneFragment newInstance() {
        return new RingtoneFragment();
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
        View root = inflater.inflate(R.layout.status_layout, container, false);
//        final TextView textView = root.findViewById(R.id.section_label);
//        pageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        status_listing = root.findViewById(R.id.status_listing);
        progress_bar = (CircleProgressView) root.findViewById(R.id.donut_progress);
        progress_bar.setBarColor(getResources().getColor(R.color.colorAccent));
        progress_bar.setRimColor(getResources().getColor(R.color.colorPrimary));
        progress_bar.setFillCircleColor(getResources().getColor(R.color.white));
        progress_bar.setShowTextWhileSpinning(true); // Show/hide text in spinning mode
        progress_bar.setTextSize(25);


        dataList = new ArrayList<Status>();
        setAdapter();


        return root;
    }



    private void setAdapter() {

        dataList.add(new Status("video4", "https://mmnews.tv/wp-content/uploads/2020/04/turkish.jpg", "https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3"));

        adapter = new RingtoneAdapter(getActivity(), dataList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        status_listing.setLayoutManager(mLayoutManager);
        status_listing.setItemAnimator(new DefaultItemAnimator());
        status_listing.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermissions())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                //requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }

    }


    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }

    @Override
    public void setRingtone(Status status) {

    }
}