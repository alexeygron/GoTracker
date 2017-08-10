package com.example.monitor.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.monitor.ui.adapter.ViewPagerAdapter;
import com.example.monitor.utils.Helpers;
import com.example.monitor.utils.NetworkReceiver;
import com.lotr.steammonitor.app.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PagerActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener, Drawer.OnDrawerItemClickListener{

    @BindView(R.id.my_toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewpager) ViewPager mViewPager;
    private Drawer mDrawer;

    private static final int TAG_SETTING = 1;
    private static final int TAG_HELP = 2;
    private static final int TAG_RATING = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(this);
            mViewPager.setOffscreenPageLimit(3);
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
            mViewPager.setAdapter(adapter);
        }
        assert mTabLayout != null;
        mTabLayout.setupWithViewPager(mViewPager);
        initNawDriver();
    }

    private void initNawDriver() {
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_help)
                                .withIcon(R.drawable.ic_help)
                                .withTag(TAG_HELP),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_setting)
                                .withIcon(R.drawable.ic_settings)
                                .withEnabled(false)
                                .withTag(TAG_SETTING),
                        new SectionDrawerItem().
                                withName(R.string.drawer_links),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_rating)
                                .withIcon(R.drawable.ic_rating)
                                .withTag(TAG_RATING)
                )
                .build();
        mDrawer.setOnDrawerItemClickListener(this);
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        int tag = (int)drawerItem.getTag();
        switch(tag){
            case TAG_SETTING:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case TAG_HELP:
                startActivity(new Intent(this, HelperActivity.class));
                break;
            case TAG_RATING:
                Helpers.openPlayStorePage(this);
            break;
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        if(mDrawer.isDrawerOpen()){
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    /**
     * Set toolbar title for each selected tab
     * @param position a tab position
     */
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
