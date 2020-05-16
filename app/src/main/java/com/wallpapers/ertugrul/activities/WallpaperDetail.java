package com.wallpapers.ertugrul.activities;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wallpapers.ertugrul.R;

public class WallpaperDetail extends AppCompatActivity {

    FloatingActionButton set_wallpaper_home, set_wallpaper_lock;
    String path = "";
    ImageView image_wallpaper;
    Bitmap image_bitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_detail);


        image_wallpaper = findViewById(R.id.image);



        setWallpapers();
    }


    public void setWallpapers(){


        set_wallpaper_home = findViewById(R.id.set_wallpaper_home);
        set_wallpaper_lock = findViewById(R.id.set_wallpaper_lock);

        image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ertugrul);

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
