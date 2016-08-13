package com.example.monitor.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.monitor.ui.adapter.ViewPagerAdapter;
import com.lotr.steammonitor.app.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    @BindView(R.id.my_toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);

        if (mViewPager != null){
            mViewPager.addOnPageChangeListener(this);
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), this));
        }

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        assert mTabLayout != null;
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch(position){
            case 0:
                mToolbar.setTitle(R.string.fav_srv_text);
                break;
            case 1:
                mToolbar.setTitle(R.string.title_favorites_players);
                break;
            case 2:
                mToolbar.setTitle(R.string.title_info_page);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
