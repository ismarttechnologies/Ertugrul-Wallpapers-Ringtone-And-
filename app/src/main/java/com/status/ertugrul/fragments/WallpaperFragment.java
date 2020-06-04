package com.status.ertugrul.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.status.ertugrul.R;
import com.status.ertugrul.activities.WallpaperDetail;
import com.status.ertugrul.adapters.WallpaperAdapter;
import com.status.ertugrul.model.PageViewModel;
import com.status.ertugrul.model.Wallpaper;
import com.status.ertugrul.utilities.OkhttpUtilities;
import com.status.ertugrul.utilities.SuccessFailureInterface;

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

        getWallpapers();

//        wallpapers_data.add(new Wallpaper("1", "Wallpaper 1", "https://image.winudf.com/v2/image1/Y29tLmVydHVncnVsLnR1cmNfc2NyZWVuXzNfMTU1Mjg1OTM1M18wNTY/screen-3.jpg?fakeurl=1&type=.jpg"));
//        wallpapers_data.add(new Wallpaper("2", "Wallpaper 2", "https://fsa.zobj.net/crop.php?r=q1xrzSZ9mxU6Tx8hDBMu8owC93SRXUolC7Kk6f2jiVseArHj5yh-snaGxYg7JKS2gxVbknHqhXLcBUp8JIVeAZv4xOIqzIiN2zZHYNqbxjuWUDq7eqn-mrQ949lUHbqis_Ov28Xfm-5whycO"));
//        wallpapers_data.add(new Wallpaper("2", "Wallpaper 2", "https://fsb.zobj.net/crop.php?r=0J22kxVdaYr9DclLNn-y5zCaXmMnD3zA1T3ee0Cmgt6EQPsv2mAyauv6jRHFJ7mKKDams8v5kj08zpeTwyXCzoRs1n-dPzRP4K6PcyLznkTzHxb-1NrKP3cYkazh-5o-H5DTEjvK9UXUC5PX"));
//        wallpapers_data.add(new Wallpaper("2", "Wallpaper 2", "https://fsa.zobj.net/crop.php?r=F4uxqWPwEnzmaJslCyQ5_LdaQA5Yf0_t5N_4muoYLSl8gwSHOF6e5v3ozg1R-pFhXja3cOh0V0WxzryWQqESIb7c_kW0lP6OemR9YExBCjWRCx-Vo8CSko8veZ2X_-rwo1kbmCY61Fnkk00u"));
//        wallpapers_data.add(new Wallpaper("2", "Wallpaper 2", "https://fsb.zobj.net/crop.php?r=NuJveZEC8UJmWqxm5iEbtPuRrY4b9yIPLl9i3bLvcWc-BDJekyPp6aP1L4odkxqV52tQsTz5XL6NKxcIDe7PIO4_rN15NNd2THkn0l9fzM_vLvFcccnAylli8NGrjn3dK5mvugl_g19okedS"));
//        wallpapers_data.add(new Wallpaper("2", "Wallpaper 2", "https://m.media-amazon.com/images/M/MV5BYzg0NzIzMWMtMTdlNS00MDA3LWFkOWMtZWE3YjZhZWQxMWJlXkEyXkFqcGdeQXVyNDg4MjkzNDk@._V1_.jpg"));
//
//        wallpapers_data.add(new Wallpaper("2", "Wallpaper 2", "https://fsb.zobj.net/crop.php?r=aRtq97K3LQsX_Bxeay9FLn33NDwbV9AEJJx5pBBi5ieKmsdx-XzwuBRlomrPDSz2mHIP3w9wSb5U3bIAJR_XedfXZt7Yui02C1tGG91TiC4xkuiuR7QZJXJ6ah0Jmw4OPN8JmNkWslWIzFfe"));
//        wallpapers_data.add(new Wallpaper("2", "Wallpaper 2", "https://fsb.zobj.net/crop.php?r=aRtq97K3LQsX_Bxeay9FLn33NDwbV9AEJJx5pBBi5ieKmsdx-XzwuBRlomrPDSz2mHIP3w9wSb5U3bIAJR_XedfXZt7Yui02C1tGG91TiC4xkuiuR7QZJXJ6ah0Jmw4OPN8JmNkWslWIzFfe"));
//        wallpapers_data.add(new Wallpaper("2", "Wallpaper 2", "https://fsb.zobj.net/crop.php?r=MMC8SvmUjU8KWZroMDssm60nhvIJmAywEO4rmVUI_rINT3qcv0bpBSBpxW50Fk5PcXQwxY1Ol4yzSLZZVT6cA_5MaZt1o1b-c4tRllrymFAXRuuV93LZ0lsbCyQ9veWPnWQHl5KQOy36S7JC"));
//        wallpapers_data.add(new Wallpaper("2", "Wallpaper 2", "https://fsb.zobj.net/crop.php?r=6TDMdeXblclGsbyWNAUKt_uE_fI-xSvD36ozH4y34W6xy8iNNmD3Gan_q9fgulxRH_xdiQDhud3_pgCdLFdMmB2oDNmNcqfbfRBnBMyR2qInk1pgkHeEMXcAJyHJ_TNg6GzrVnQpLDjY-TKG"));
//        wallpapers_data.add(new Wallpaper("2", "Wallpaper 2", "https://fsb.zobj.net/crop.php?r=8bDvu0BBxc4sPUcruaBP6eWIjB-i8Zvp-Okd0wXE2LL7sN6-s8Zwno4q9ajEiFPgSN18uzXqBbkhoprFttO3kk8dV_CaMv6G5gxVuGmpI_f_gWKjBYzDZ_laW1r-3KOw0Rt89Cz2p94h9eRx"));
//
//        setAdapter();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getContext())) {

                //Write code to feature for eg. set brightness or vibrate device
           /* ContentResolver cResolver = context.getContentResolver();
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS,brightness);*/
            } else {
                showRingtonePermissionDialog(getContext());
            }
        }
        return root;
    }


    private  void showRingtonePermissionDialog(final Context context) {


        genaricDialog(context, "Permission Required", "Please give the permission to change Ringtone.", "OK", "", new SuccessFailureInterface() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure() {

            }
        });


//        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
//        builder.setCancelable(true);
//        final AlertDialog alert = builder.create();
//        builder.setMessage("Please give the permission to change Ringtone. ")
//                .setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                        intent.setData(Uri.parse("package:" + context.getPackageName()));
//                        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        alert.dismiss();
//                    }
//                });
//        alert.show();
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
                .url("http://111.88.244.77/api/media/getimage")
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

                            String name[];
                            if (data_array!=null && data_array.length()>0){
                                for (int i=0; i<data_array.length(); i++) {
                                    name = data_array.getJSONObject(i).optString("FileName").split("\\.");
                                    wallpapers_data.add(new Wallpaper(data_array.getJSONObject(i).optString("Id"), name[0], data_array.getJSONObject(i).optString("Url")));
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