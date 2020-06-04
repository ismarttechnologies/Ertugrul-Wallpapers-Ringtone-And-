package com.status.ertugrul.activities;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.status.ertugrul.R;
import com.status.ertugrul.model.Wallpaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WallpaperDetail extends AppCompatActivity {

    FloatingActionsMenu fab_menu_share, fab_menu_set;
    FloatingActionButton set_wallpaper_home, set_wallpaper_lock,share_whatsapp,share_facebook;
    String path = "";
    ImageView image_wallpaper;
    Bitmap image_bitmap;
    //    private AdView mAdView;
    ImageView float_share, float_set;
    Wallpaper wallpaper_obj;
    private AdView mAdView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_detail);

        image_wallpaper = findViewById(R.id.image);
//        mAdView = view.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        wallpaper_obj = (Wallpaper)getIntent().getSerializableExtra("wallpaper");

        image_wallpaper = findViewById(R.id.image);
        setWallpapers();
    }


    public void setWallpapers(){

        set_wallpaper_home = findViewById(R.id.fab2);
        set_wallpaper_lock = findViewById(R.id.fab1);
        fab_menu_share = findViewById(R.id.fab_menu_share);
        fab_menu_set = findViewById(R.id.fab_menu_set);

        share_whatsapp = findViewById(R.id.whatsapp);
        share_facebook = findViewById(R.id.facebook);

        Picasso.get()
                .load(wallpaper_obj.getImage())
                .priority(Picasso.Priority.HIGH)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        image_bitmap = bitmap;
                        image_wallpaper.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {


                    }
                });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        fab_menu_share.setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("############", " "+animation);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        float_share = findViewById(R.id.float_share);
        float_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab_menu_share.isExpanded()){
                    fab_menu_share.collapse();
                    float_share.setImageResource(R.drawable.share_icon);

                }
                else {
                    fab_menu_share.expand();
                    float_share.setImageResource(R.drawable.cross_icon);
                }
            }
        });


        float_set = findViewById(R.id.float_set);
        float_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab_menu_set.isExpanded()){
                    fab_menu_set.collapse();
                    float_set.setImageResource(R.drawable.set_icon);
                }
                else {
                    fab_menu_set.expand();
                    float_set.setImageResource(R.drawable.cross_icon);
                }
            }
        });

//        FloatingActionMenu menu = findViewById(R.id.menu);
//        menu.setIc(getResources().getDrawable(R.drawable.social));

//        fab_menu.setImageDrawable(getResources().getDrawable(R.drawable.social));

        set_wallpaper_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog("Setting Wallpaper... Please Wait");

//                if (mPublisherInterstitialAd.isLoaded()) {
//                    mPublisherInterstitialAd.show();
//                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //add your code here
                                try {
                                    WallpaperManager.getInstance(getApplicationContext()).setBitmap(image_bitmap);
                                    hideprogressDialog();
                                    Toast.makeText(getApplicationContext(), "Wallpaper changed successfully", Toast.LENGTH_SHORT).show();
                                }catch (Exception e){
                                    hideprogressDialog();
                                    e.printStackTrace();
                                }
                            }
                        }, 1500);

                    }
                });


            }
        });



        set_wallpaper_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog("Setting Wallpaper... Please Wait");

//                if (mPublisherInterstitialAd.isLoaded()) {
//                    mPublisherInterstitialAd.show();
//                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //add your code here
                                try {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        WallpaperManager.getInstance(getApplicationContext()).setBitmap(image_bitmap, null, true, WallpaperManager.FLAG_LOCK);

                                        hideprogressDialog();
                                        Toast.makeText(getApplicationContext(), "Wallpaper changed successfully", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        hideprogressDialog();
                                        Toast.makeText(getApplicationContext(), "Your Android version does not support to change the wallpaper.", Toast.LENGTH_LONG).show();
                                    }

                                }catch (Exception e){
                                    hideprogressDialog();
                                    e.printStackTrace();
                                }
                            }
                        }, 1500);

                    }
                });

            }
        });


        share_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(image_bitmap, false, wallpaper_obj.getName());
            }
        });

        share_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(image_bitmap, true, wallpaper_obj.getName());
            }
        });

    }


    String fileUri;

    public void shareImage(Bitmap bitmap, boolean is_whatsapp, String name) {

        try {
            File mydir = new File(Environment.getExternalStorageDirectory() + "/Ertugrul Wallpapers And Status");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            fileUri = mydir.getAbsolutePath() + File.separator + name + ".jpg";
            File image_file = new File(fileUri);
            if (image_file.exists()){
//                Uri uri= Uri.fromFile(image_file);
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", image_file);


                if (is_whatsapp){
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    share.putExtra(Intent.EXTRA_STREAM, photoURI);
                    share.setPackage("com.whatsapp");//package name of the app
                    startActivity(Intent.createChooser(share, "Share Image"));

                }
                else {
                    // use intent to share image
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/*");
                    share.putExtra(Intent.EXTRA_STREAM, photoURI);
                    startActivity(Intent.createChooser(share, "Share Wallpaper"));
                }

                return;
            }
            FileOutputStream outputStream = new FileOutputStream(fileUri);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        File image_file = new File(fileUri);
//        Uri uri= Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(fileUri),null,null));
//        Uri uri= Uri.fromFile(image_file);
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", image_file);



        if (is_whatsapp){
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            share.putExtra(Intent.EXTRA_STREAM, photoURI);
            share.setPackage("com.whatsapp");//package name of the app
            startActivity(Intent.createChooser(share, "Share Image"));

        }
        else {
            // use intent to share image
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_STREAM, photoURI);
            startActivity(Intent.createChooser(share, "Share Wallpaper"));
        }

    }


    protected ProgressDialog progressDialog;

    protected void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else {
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    protected void hideprogressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


//    private Target target = new Target() {
//        @Override
//        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//            image_wallpaper.setScaleType(ImageView.ScaleType.FIT_XY);
//            image_wallpaper.setImageBitmap(bitmap);
//            image_bitmap = bitmap;
//        }
//
//        @Override
//        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//            image_wallpaper.setImageDrawable(errorDrawable);
//
//        }
//
//
//        @Override
//        public void onPrepareLoad(Drawable placeHolderDrawable) {
//            image_wallpaper.setImageDrawable(placeHolderDrawable);
//        }
//    };

}
