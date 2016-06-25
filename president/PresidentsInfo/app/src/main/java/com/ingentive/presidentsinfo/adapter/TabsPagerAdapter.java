package com.ingentive.presidentsinfo.adapter;

/**
 * Created by PC on 25-06-2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ingentive.presidentsinfo.fragment.ContentsFragment;
import com.ingentive.presidentsinfo.fragment.CreditsFragment;
import com.ingentive.presidentsinfo.fragment.PresidentialFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new ContentsFragment();
            case 1:
                return new PresidentialFragment();
            case 2:
                return new CreditsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}