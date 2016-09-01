package com.example.monitor.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.monitor.players.PlayersFragment;
import com.example.monitor.servers.ServersFragment;
import com.example.monitor.gameinfo.GameInfoFragment;
import com.lotr.steammonitor.app.R;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private String mTabTitles[];

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        mTabTitles = mContext.getResources().getStringArray(R.array.tabs_name);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
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
        return mTabTitles[position];
    }
}