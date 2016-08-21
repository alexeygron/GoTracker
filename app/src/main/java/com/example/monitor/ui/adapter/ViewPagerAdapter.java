package com.example.monitor.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.monitor.players.PlayersFragment;
import com.example.monitor.servers.ServersFragment;
import com.example.monitor.ui.fragment.GameInfoFragment;
import com.example.monitor.utils.NetworkReceiver;

/**
 * Created by lotr on 05.07.2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private String tabTitles[] = new String[]{"Сервера", "Игроки", "Об игре"};

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
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
        // Generate title based on item position
        return tabTitles[position];
    }
}