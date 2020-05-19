package com.wallpapers.ertugrul.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
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
import com.wallpapers.ertugrul.BuildConfig;
import com.wallpapers.ertugrul.R;
import com.wallpapers.ertugrul.adapters.StatusAdapter;
import com.wallpapers.ertugrul.model.PageViewModel;
import com.wallpapers.ertugrul.model.Status;
import com.wallpapers.ertugrul.model.Wallpaper;
import com.wallpapers.ertugrul.utilities.OkhttpUtilities;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import at.grabner.circleprogress.CircleProgressView;

public class StatusFragment extends BaseFragment implements StatusAdapter.OnItemListner {
    private static final String TAG = "SpeedDial";
    private PageViewModel pageViewModel;

    private RecyclerView status_listing;
    StatusAdapter adapter;
    ArrayList<Status> dataList;
    public static final int PERMISSION_WRITE = 0;

    CircleProgressView progress_bar;


    public StatusFragment() {
        // Required empty public constructor
    }
    /**
     * @return A new instance of fragment SpeedDialFragment.
     */
    public static StatusFragment newInstance() {
        return new StatusFragment();
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

//        getStatus();

        for (int i=0; i<25; i++) {

            if (i!=0 && i%7==0){
                dataList.add(new Status("", "", "", "Banner"));
            }
            dataList.add(new Status("video1", "https://mmnews.tv/wp-content/uploads/2020/04/turkish.jpg", "https://media.istockphoto.com/videos/the-helicopter-left-skiers-on-the-slope-of-the-mountain-and-flew-a-video-id1126651601"));
        }

        setAdapter();

        return root;
    }



    private void setAdapter() {

        adapter = new StatusAdapter(getActivity(), dataList, this);
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
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                // do something
            }
            return;
        }
    }


    private void getStatus() {
        if (!checkConnection(getContext())){
//            showNoInternetDialog(getActivity(), "Please connect to your internet.", "OK");
            return;
        }

//        showProgressDialog(getResources().getString(R.string.loading));
        RequestBody formBody = new FormEncodingBuilder()
                .build();

        //OkHttpClient okHttpClient = OkhttpUtilities.getTrustedHttpClient(new OkHttpClient());
        final OkHttpClient okHttpClient= OkhttpUtilities.getTrustedHttpClient(new OkHttpClient());
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url("http://111.88.246.218/api/media/getVideo")
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
//                            hideprogressDialog();

                            JSONObject obj=new JSONObject(requestResult);
                            JSONArray data_array=obj.optJSONArray("Result");

                            if (data_array!=null && data_array.length()>0){
                                for (int i=0; i<data_array.length(); i++) {

                                    if (i!=0 && i%3==0){
                                        dataList.add(new Status("", "", "Banner"));

                                    }
//                                    dataList.add(new Status("video1", "https://mmnews.tv/wp-content/uploads/2020/04/turkish.jpg", "https://media.istockphoto.com/videos/the-helicopter-left-skiers-on-the-slope-of-the-mountain-and-flew-a-video-id1126651601"));

                                    dataList.add(new Status("Wallpaper"+data_array.getJSONObject(i).optString("Id"), "https://mmnews.tv/wp-content/uploads/2020/04/turkish.jpg", data_array.getJSONObject(i).optString("Url")));
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

    String fileUri;


    @Override
    public void onShareImage(Bitmap bitmap) {
        try {
            File mydir = new File(Environment.getExternalStorageDirectory() + "/Ertugrul Wallpapers And Status");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            fileUri = mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";

            if (new File(fileUri).exists()){
                Uri uri= Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), BitmapFactory.decodeFile(fileUri),null,null));
                // use intent to share image
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share Image"));
                return;
            }
            FileOutputStream outputStream = new FileOutputStream(fileUri);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        Uri uri= Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), BitmapFactory.decodeFile(fileUri),null,null));
        // use intent to share image
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));

    }


    private Fetch fetch;

//    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//    FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
//            .setDownloadConcurrentLimit(10)
//            .setHttpDownloader(new OkHttpDownloader(okHttpClient))
//            .build();

//    Fetch fetch = Fetch.Impl.getInstance(fetchConfiguration);

    @Override
    public void onShareVideo(Status status) {
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(getContext())
                .setDownloadConcurrentLimit(1)
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);

        File mydir = new File(Environment.getExternalStorageDirectory() + "/Ertugrul Wallpapers And Status");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }

        fileUri = mydir.getAbsolutePath() + File.separator + status.getName() + ".mp4";

        if (new File(fileUri).exists()){
            shareVideoWhatsApp(new File(fileUri));
            return;
        }

        final Request request = new Request(status.getUrl(), fileUri);
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
                    shareVideoWhatsApp(new File(fileUri));
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


//                progress_bar.setValue(0);
//                progress_bar.spin();
//
//                progress_bar.setValueAnimated(2);

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

//        fetch.enqueue(request, updatedRequest -> {
//            //Request was successfully enqueued for download.
//        }, error -> {
//            //An error occurred enqueuing the request.
//        });
    }


    public void shareVideoWhatsApp(File fileImagePath) {

        Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", fileImagePath);

//        Uri uri = Uri.fromFile(path);
        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("*/*");
        videoshare.setPackage("com.whatsapp");
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM,uri);

        startActivity(videoshare);

    }
}