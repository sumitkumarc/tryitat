package com.app.tryitat.ui.localbusiness.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.tryitat.ui.localbusiness.fragments.NearbyFragment;
import com.app.tryitat.ui.localbusiness.fragments.SearchFragment;
import com.app.tryitat.ui.localbusiness.fragments.ShowAllFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        if (position == 0)
            fragment = new ShowAllFragment();
        else if (position == 1)
            fragment = new NearbyFragment();
        else
            fragment = new SearchFragment();

        return fragment;
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = null;
        if (position == 0)
            title = "Show All";
        else if (position == 1)
            title = "Nearby";
        else if (position == 2)
            title = "Search";
        return title;
    }
}