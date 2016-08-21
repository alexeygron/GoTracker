package com.example.monitor.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.monitor.ui.adapter.ViewPagerAdapter;
import com.example.monitor.utils.NetworkReceiver;
import com.lotr.steammonitor.app.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    private NetworkReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);

        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(this);
            mViewPager.setOffscreenPageLimit(3);
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
            mViewPager.setAdapter(adapter);
        }

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        assert mTabLayout != null;
        mTabLayout.setupWithViewPager(mViewPager);
        initNawDriver();
    }

    private void initNawDriver() {
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home");

//create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Settings")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return true;
                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
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
