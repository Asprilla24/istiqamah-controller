package com.team.istiqomah.istiqomahcontroller.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.team.istiqomah.istiqomahcontroller.R;
import com.team.istiqomah.istiqomahcontroller.fragment.DashboardFragment;
import com.team.istiqomah.istiqomahcontroller.fragment.MainFragment;

/**
 * Created by alde.asprilla on 09/08/2016.
 */

public class viewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    final Context context;

    public viewPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0 : fragment = new MainFragment();break;
            case 1 : fragment = new DashboardFragment();break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0 : return context.getResources().getString(R.string.MainFragment);
            case 1 : return context.getResources().getString(R.string.DashboardFragment);
        }
        return null;
    }
}
