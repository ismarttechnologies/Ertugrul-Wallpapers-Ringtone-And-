package com.wallpapers.ertugrul.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wallpapers.ertugrul.R;
import com.wallpapers.ertugrul.activities.WallpaperDetail;
import com.wallpapers.ertugrul.adapters.WallpaperAdapter;
import com.wallpapers.ertugrul.model.PageViewModel;
import com.wallpapers.ertugrul.model.Ringtone;
import com.wallpapers.ertugrul.model.Wallpaper;
import com.wallpapers.ertugrul.utilities.OkhttpUtilities;
import com.wallpapers.ertugrul.utilities.SuccessFailureInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WallpaperFragment extends BaseFragment implements MultiplePermissionsListener {
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

        wallpapers_data = new ArrayList<Wallpaper>();

//        getWallpapers();
        for (int i=0; i<10; i++) {
            wallpapers_data.add(new Wallpaper("1", "Wallpaper 1", "https://image.winudf.com/v2/image1/Y29tLmVydHVncnVsLnR1cmNfc2NyZWVuXzNfMTU1Mjg1OTM1M18wNTY/screen-3.jpg?fakeurl=1&type=.jpg"));
        }

        setAdapter();

        return root;
    }


    public void setAdapter(){

        mAdapter = new WallpaperAdapter(getActivity(), wallpapers_data);
        wallpapers_list.setAdapter(mAdapter);


        wallpapers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intent = new Intent(getContext(), WallpaperDetail.class);
                intent.putExtra("wallpaper", wallpapers_data.get(position));
                startActivity(intent);


            }
        });

        isPermissionGranted();




    }



    private void getWallpapers() {
        if (!checkConnection(getContext())){
            showNoInternetDialog(getActivity(), "Please connect to your internet.", "OK");

            return;
        }

        showProgressDialog(getResources().getString(R.string.loading));
        RequestBody formBody = new FormEncodingBuilder()
                .build();

        //OkHttpClient okHttpClient = OkhttpUtilities.getTrustedHttpClient(new OkHttpClient());
        final OkHttpClient okHttpClient= OkhttpUtilities.getTrustedHttpClient(new OkHttpClient());
        final Request request = new Request.Builder()
                .url("http://111.88.246.218/api/media/getimage")
                .get()
                .build();

        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
//                Log.e("TAG", e.toString() + "");
                Log.e("TAG",e.toString()+"");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideprogressDialog();

                        String message;
                        if (e.toString().contains("failed to connect"))
                            message = "Server is not responding";
                        else
                            message = e.getMessage();

                        showNoInternetDialog(getActivity(), message, "OK");

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String requestResult = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("onResponse", "response: " + requestResult);
                        try {
                            hideprogressDialog();

                            JSONObject obj=new JSONObject(requestResult);
                            JSONArray data_array=obj.optJSONArray("Result");

                            if (data_array!=null && data_array.length()>0){
                                for (int i=0; i<data_array.length(); i++) {
                                    wallpapers_data.add(new Wallpaper(data_array.getJSONObject(i).optString("Id"), "Wallpaper"+data_array.getJSONObject(i).optString("Id"), data_array.getJSONObject(i).optString("Url")));
                                }
                            }

                            setAdapter();
                        } catch (Exception e) {
                            hideprogressDialog();
                            e.printStackTrace();
                            return;
                        }
                    }
                });
            }
        });
    }




    public void isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    getActivity().checkSelfPermission(Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED ) {
                Log.v("TAG", "Permission is granted");

            } else {
                Log.v("TAG", "Permission is revoked");
                // CHECK IF USER CHECKED THE NEVER ASK AGAIN THEN IMPLEMENT THE CUSTOM FUNCTIONALITY
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_SETTINGS)) {

                    Log.d("TAG", "SOME PERMISSION IS MISSING");


                    genaricDialog(getContext(), "Permission Required", "Storage permission required", "Settings", "Cancel", new SuccessFailureInterface() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, 1000);
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS}, 1);
                }
            }
        }
    }


    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        if (report.getGrantedPermissionResponses().size() == 3) {

        } else {

            genaricDialog(getContext(), "Permission Required", "Storage permission required", "Settings", "Cancel", new SuccessFailureInterface() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 1000);
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
        genaricDialog(getContext(), "Permission Required", "Storage permission required", "Settings", "Cancel", new SuccessFailureInterface() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 1000);
            }

            @Override
            public void onFailure() {

            }
        });
    }
}