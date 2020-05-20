package com.wallpapers.ertugrul.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Func;
import com.wallpapers.ertugrul.R;
import com.wallpapers.ertugrul.adapters.RingtoneAdapter;
import com.wallpapers.ertugrul.model.PageViewModel;
import com.wallpapers.ertugrul.model.Ringtone;
import com.wallpapers.ertugrul.utilities.OkhttpUtilities;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import at.grabner.circleprogress.CircleProgressView;

public class RingtoneFragment  extends BaseFragment implements RingtoneAdapter.OnItemListner {   // MultiplePermissionsListener
    private static final String TAG = "SpeedDial";
    private PageViewModel pageViewModel;

    private RecyclerView ringtone_listing;
    RingtoneAdapter adapter;
    ArrayList<Ringtone> dataList;
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
        View root = inflater.inflate(R.layout.ringtone_layout, container, false);
//        final TextView textView = root.findViewById(R.id.section_label);
//        pageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        ringtone_listing = root.findViewById(R.id.ringtone_listing);
        progress_bar = (CircleProgressView) root.findViewById(R.id.donut_progress);
        progress_bar.setBarColor(getResources().getColor(R.color.colorAccent));
        progress_bar.setRimColor(getResources().getColor(R.color.colorPrimary));
        progress_bar.setFillCircleColor(getResources().getColor(R.color.white));
        progress_bar.setShowTextWhileSpinning(true); // Show/hide text in spinning mode
        progress_bar.setTextSize(25);


        dataList = new ArrayList<Ringtone>();
//        getRingtones();


        for (int i=0; i<10; i++) {
//            dataList.add(new Ringtone(""+i, "ringtone_"+i, "https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3", "10"));
            dataList.add(new Ringtone(""+i, "ringtone_"+i, "https://naghma.me/files/1472.mp3", "10"));
        }

        setAdapter();

        return root;
    }



//    public void isPermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (getActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                    getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                    getActivity().checkSelfPermission(Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED ) {
//                Log.v("TAG", "Permission is granted");
//
////                PromptPicture promptPicture = new PromptPicture(getActivity());
////                promptPicture.setCanceledOnTouchOutside(true);
////                promptPicture.show();
////                promptPicture.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//            } else {
//                Log.v("TAG", "Permission is revoked");
//                // CHECK IF USER CHECKED THE NEVER ASK AGAIN THEN IMPLEMENT THE CUSTOM FUNCTIONALITY
//                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_SETTINGS)) {
//
//
//                    Log.d("TAG", "SOME PERMISSION IS MISSING");
////                    dialo("Camera and Storage Permission required, click YES to grant the permission from settings and No to cancel.", GenaricConstants.BUTTONS_YES_NO, new OnSuccessFailureCallBack() {
////                        @Override
////                        public void confirm() {
////                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
////                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
////                            intent.setData(uri);
////                            startActivityForResult(intent, 1000);
////                        }
////
////                        @Override
////                        public void cancel() {
////
////                        }
////                    });
//
//                } else {
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS}, 1);
//                }
//            }
//        }
//    }


    private void setAdapter() {

        adapter = new RingtoneAdapter(getActivity(), dataList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        ringtone_listing.setLayoutManager(mLayoutManager);
        ringtone_listing.setItemAnimator(new DefaultItemAnimator());
        ringtone_listing.setAdapter(adapter);

//        isPermissionGranted();

    }


    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS
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
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 110);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 110) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }

    @Override
    public void setRingtone(Ringtone ringtone) {
        onDownloadRingtone(ringtone);
    }

    @Override
    public void likeRingtone(Ringtone ringtone) {
        saveLikeRingtone(ringtone);
    }


    private Fetch fetch;
    String fileUri;

    public void onDownloadRingtone(Ringtone ringtone) {
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(getContext())
                .setDownloadConcurrentLimit(1)
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);

        File mydir = new File(Environment.getExternalStorageDirectory() + "/Ertugrul Wallpapers And Status");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }

        fileUri = mydir.getAbsolutePath() + File.separator + ringtone.getName() + ".mp3";

        if (new File(fileUri).exists()){
            setAsRingtone(fileUri);
            return;
        }

        final Request request = new Request(ringtone.getUrl(), fileUri);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);
        request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");


        FetchListener fetchListener = new FetchListener() {
            @Override
            public void onError(@NotNull Download download, @NotNull Error error, @org.jetbrains.annotations.Nullable Throwable throwable) {

            }

            @Override
            public void onWaitingNetwork(@NotNull Download download) {

            }

            @Override
            public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {

            }


            @Override
            public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

            }

            @Override
            public void onAdded(@NotNull Download download) {

            }

            @Override
            public void onQueued(@NotNull Download download, boolean waitingOnNetwork) {
                if (request.getId() == download.getId()) {
//                    showDownloadInList(download);
                }
            }

            @Override
            public void onCompleted(@NotNull Download download) {
                Log.d("DOWNLOAD Completed", " "+download.getId());

//                progress_bar.stopSpinning();
//                Toast.makeText(getApplicationContext(), "Download Completed", Toast.LENGTH_SHORT).show();
//                shareVideoWhatsApp(new File(fileUri));

            }


            @Override
            public void onProgress(@NotNull Download download, long etaInMilliSeconds, long downloadedBytesPerSecond) {
                if (request.getId() == download.getId()) {
//                    updateDownload(download, etaInMilliSeconds);
                }
                int progress = download.getProgress();
                Log.d("DOWNLOAD PREGRESS", " "+progress);
                progress_bar.setVisibility(View.VISIBLE);
                progress_bar.setValueAnimated(progress);
                progress_bar.setText(""+progress);



//                progress_bar.setSpinSpeed(progress);


                if (progress==100) {
                    fetch.close();

                    progress_bar.stopSpinning();
                    progress_bar.setVisibility(View.GONE);
                    setAsRingtone(fileUri);
                }
            }

            @Override
            public void onPaused(@NotNull Download download) {

            }

            @Override
            public void onResumed(@NotNull Download download) {

            }

            @Override
            public void onCancelled(@NotNull Download download) {

            }

            @Override
            public void onRemoved(@NotNull Download download) {

            }

            @Override
            public void onDeleted(@NotNull Download download) {

            }
        };

        fetch.addListener(fetchListener);

        fetch.enqueue(request, new Func<Request>() {
            @Override
            public void call(@NotNull Request result) {
                //Request successfully Queued for download
                Toast.makeText(getContext(), "Started successfully", Toast.LENGTH_SHORT).show();

                progress_bar.setVisibility(View.VISIBLE);
                progress_bar.setValueAnimated(3);
                progress_bar.setText(""+3);

            }

        }, new Func<Error>() {
            @Override
            public void call(Error error) {
                //An error occurred when enqueuing a request.
                String err =  error.getHttpResponse().getErrorResponse();
                Toast.makeText(getContext(), "Error downloading", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public boolean setAsRingtone(String filePath) {
        // this function is use to On or Off Default Ringtone
        String[] files = { filePath };
        MediaScannerConnection.scanFile(getContext(), files, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        System.out.println("Ringtone file " + path + " was scanned seccessfully: " + uri);
                        if(path!=null&&!path.isEmpty()){
                            try {
                                RingtoneManager.setActualDefaultRingtoneUri(getContext(), RingtoneManager.TYPE_RINGTONE, uri);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            String defaultPath = Settings.System.DEFAULT_RINGTONE_URI.getPath();
                            File newSoundFile = new File(defaultPath);
                            Uri newUri = MediaStore.Audio.Media.getContentUriForPath(newSoundFile.getAbsolutePath());
                            try {
                                RingtoneManager.setActualDefaultRingtoneUri(getContext(), RingtoneManager.TYPE_RINGTONE, newUri);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        Toast.makeText(getContext(), "Ringtone has been set successfully", Toast.LENGTH_SHORT).show();

        return true;
    }



    private void getRingtones() {
        if (!checkConnection(getContext())){
//            showNoInternetDialog(getActivity(), "No internet Connection", "Close");

            return;
        }

//        showProgressDialog(getResources().getString(R.string.loading));

        final OkHttpClient okHttpClient= OkhttpUtilities.getTrustedHttpClient(new OkHttpClient());
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url("http://111.88.246.218/api/media/getAudio")
                .get()
                .build();

        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, final IOException e) {
//                Log.e("TAG", e.toString() + "");
                Log.e("TAG",e.toString()+"");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        hideprogressDialog();
                        showNoInternetDialog(getActivity(), e.getMessage(), "OK");
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
//                            hideprogressDialog();
                            JSONObject obj=new JSONObject(requestResult);
                            JSONArray data_array=obj.optJSONArray("Result");

                            if (data_array!=null && data_array.length()>0){
                                for (int i=0; i<data_array.length(); i++) {
                                    dataList.add(new Ringtone(data_array.getJSONObject(i).optString("Id"), data_array.getJSONObject(i).optString("FileName"), data_array.getJSONObject(i).optString("Url"), data_array.getJSONObject(i).optString("Download")));
//                                    dataList.add(new Ringtone(data_array.getJSONObject(i).optString("Id"), "ringtone"+data_array.getJSONObject(i).optString("Id"), "https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3"));
                                }

                                setAdapter();

                            }


                        } catch (Exception e) {
//                            hideprogressDialog();
                            e.printStackTrace();
                            return;
                        }
                    }
                });
            }
        });
    }



    private void saveLikeRingtone(Ringtone ringtone) {
        if (!checkConnection(getContext())){
            showNoInternetDialog(getActivity(), "No internet Connection", "Close");

            return;
        }

        RequestBody formBody = new FormEncodingBuilder()
                .add("Id", ringtone.getId())
                .build();
//        showProgressDialog(getResources().getString(R.string.loading));

        final OkHttpClient okHttpClient= OkhttpUtilities.getTrustedHttpClient(new OkHttpClient());
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url("http://111.88.246.218/api/media/Download")
                .put(formBody)
                .build();

        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, final IOException e) {
//                Log.e("TAG", e.toString() + "");
                Log.e("TAG",e.toString()+"");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        hideprogressDialog();
                        showNoInternetDialog(getActivity(), e.getMessage(), "OK");
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
//                            hideprogressDialog();
                            JSONObject obj=new JSONObject(requestResult);
                            JSONArray data_array=obj.optJSONArray("Result");

                            if (data_array!=null && data_array.length()>0){
                                for (int i=0; i<data_array.length(); i++) {
                                    dataList.add(new Ringtone(data_array.getJSONObject(i).optString("Id"), data_array.getJSONObject(i).optString("FileName"), data_array.getJSONObject(i).optString("Url"), data_array.getJSONObject(i).optString("Download")));
//                                    dataList.add(new Ringtone(data_array.getJSONObject(i).optString("Id"), "ringtone"+data_array.getJSONObject(i).optString("Id"), "https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3"));
                                }

                                setAdapter();

                            }


                        } catch (Exception e) {
//                            hideprogressDialog();
                            e.printStackTrace();
                            return;
                        }
                    }
                });
            }
        });
    }

}