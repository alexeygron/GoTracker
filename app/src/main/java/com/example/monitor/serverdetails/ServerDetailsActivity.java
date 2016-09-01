package com.example.monitor.serverdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.monitor.utils.Helpers;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;
import com.lotr.steammonitor.app.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServerDetailsActivity extends AppCompatActivity implements
        IView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title) TextView mTitle;
    @BindView(R.id.game_name) TextView mGameName;
    @BindView(R.id.ip_address) TextView mIpAddress;
    @BindView(R.id.tags) TextView mTags;
    @BindView(R.id.map) TextView mMap;
    @BindView(R.id.players) TextView mPlayers;
    @BindView(R.id.details_list_players) RecyclerView mRecyclerView;
    private ListPlayersAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ServerDetailsPresenter mPresenter;

    private static final String TAG = Helpers.makeLogTag(ServerDetailsActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_servers);
        ButterKnife.bind(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ListPlayersAdapter();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        setTitle(null);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initFields(getIntent());

        if (mPresenter == null) {
            mPresenter = new ServerDetailsPresenter(this, getIntent().getStringExtra("ip"), getLoaderManager(), this);
        }
    }

    private void initFields(Intent i){
        mTitle.setText(i.getStringExtra("name"));
        mGameName.setText(i.getStringExtra("game"));
        mIpAddress.setText(i.getStringExtra("ip"));
        mTags.setText(i.getStringExtra("tags"));
        mMap.setText(i.getStringExtra("map"));
        mPlayers.setText(i.getStringExtra("players"));
    }

    public void updateFields(String map, String players, String name, String game, String tags){
        mTitle.setText(name);
        mMap.setText(map);
        mPlayers.setText(players);
        mGameName.setText(game);
        mTags.setText(tags);
    }

    public void setAdapterData(ArrayList<SteamPlayer> data){
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh();
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 700);
    }
}
