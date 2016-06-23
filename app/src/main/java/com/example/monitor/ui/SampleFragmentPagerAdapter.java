package com.example.monitor.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.monitor.ui.fragments.GameInfoFragment;
import com.example.monitor.ui.fragments.PlayersFragment;
import com.example.monitor.ui.fragments.ServersFragment;


public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private String tabTitles[] = new String[] {"Сервера", "Игроки", "Об игре" };

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new ServersFragment();

            case 1:
                return new PlayersFragment();

            case 2:
                return new GameInfoFragment();

            default:
                return new ServersFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}