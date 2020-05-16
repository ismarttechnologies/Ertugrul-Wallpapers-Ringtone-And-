package com.wallpapers.ertugrul.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wallpapers.ertugrul.R;
import com.wallpapers.ertugrul.fragments.RingtoneFragment;
import com.wallpapers.ertugrul.fragments.StatusFragment;
import com.wallpapers.ertugrul.fragments.WallpaperFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {


    private static final String[] TAB_TITLES =
            new String[] { "Wallpapers", "Ringtone", "Status" };

    private final Context mContext;


    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return WallpaperFragment.newInstance();
            case 1:
                return RingtoneFragment.newInstance();
            case 2:
                return StatusFragment.newInstance();
            default:
                return null;
        }
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }


    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}