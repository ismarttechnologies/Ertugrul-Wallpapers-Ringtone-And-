package com.wallpapers.ertugrul.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkButtonBuilder;
import com.wallpapers.ertugrul.R;
import com.wallpapers.ertugrul.adapters.TabsPagerAdapter;

import java.io.File;

public class Home extends AppCompatActivity {
  ImageView shareImage;
//  Toolbar toolbar;

    private int[] tabIcons = {
            R.drawable.wallpaper,
            R.drawable.ringtone,
            R.drawable.status
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//     toolbar = findViewById(R.id.toolbar_id);
//     setSupportActionBar(toolbar);

//        Mint.initAndStartSession(this.getApplication(), "7624c5f1");

     shareImage = findViewById(R.id.share_icon);
     shareImage.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
             Intent myIntent=new Intent(Intent.ACTION_SEND);
             myIntent.setType("text/plain");
             String sharebody="Ertugrul Wallpapers, Ringtones and Status";
             String sharesub="Your friend is inviting you to download Ertugrul Wallpapers, Ringtones and Status application from play store, \n\n"
                     +"https://play.google.com/store/apps/details?id=" + appPackageName;
             myIntent.putExtra(Intent.EXTRA_SUBJECT,sharebody);
             myIntent.putExtra(Intent.EXTRA_TEXT,sharesub);
             startActivity(Intent.createChooser(myIntent,"Share Via"));
         }
     });



        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main,menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id =item.getItemId();
//        if (id==R.id.share){
//            ApplicationInfo api= getApplicationContext().getApplicationInfo();
//            String apkpath=api.sourceDir;
//
//            Intent intent=new Intent(Intent.ACTION_SEND);
//            intent.setType("text/plain");
//            intent.putExtra(intent.EXTRA_STREAM, Uri.fromFile(new File(apkpath)));
//
//           startActivity(Intent.createChooser(intent,"ShareVia"));
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
