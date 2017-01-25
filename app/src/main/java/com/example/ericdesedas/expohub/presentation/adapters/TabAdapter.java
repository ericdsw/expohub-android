package com.example.ericdesedas.expohub.presentation.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * General FragmentPagerAdapter implementation
 * Can be used on any page displaying a set of fragments using a tab format
 */
public class TabAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> fragmentTitles;

    public TabAdapter(FragmentManager fm) {
        super(fm);
        fragments       = new ArrayList<>();
        fragmentTitles  = new ArrayList<>();
    }

    public void addFragment(String title, Fragment fragment) {
        fragments.add(fragment);
        fragmentTitles.add(title);
    }

    public void removeFragment(Fragment fragment) {
        int position = fragments.indexOf(fragment);
        fragmentTitles.remove(position);
        fragments.remove(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
