package com.example.monitor.gameinfo;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.example.monitor.ui.fragment.CommonFragment;
import com.example.monitor.utils.NetworkReceiver;
import com.lotr.steammonitor.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.monitor.utils.Helpers.makeLogTag;

/**
 * Displays common information about game and its servers
 */
public class GameInfoFragment extends CommonFragment implements IView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh) android.support.v4.widget.SwipeRefreshLayout mRefresh;
    @BindView(R.id.message) TextView mMessageError;
    @BindView(R.id.players) TextView mPlayers;
    @BindView(R.id.servers) TextView mServers;
    @BindView(R.id.searching) TextView mSearching;
    @BindView(R.id.search_seconds) TextView mSearchSeconds;
    @BindView(R.id.scheduler) TextView mScheduler;
    @BindView(R.id.sessions_logon) TextView mSessionsLogon;
    @BindView(R.id.steam_community) TextView mSteamCommunity;
    @BindView(R.id.iecon_items) TextView mIeconItems;
    @BindView(R.id.leaderboards) TextView mLeaderboards;
    @BindView(R.id.version) TextView mVersion;
    @BindView(R.id.timestamp) TextView mTimestamp;
    @BindView(R.id.time) TextView mTime;

    private GameInfoPresenter mPresenter;
    private NetworkReceiver mReceiver;

    private static final String TAG = makeLogTag(GameInfoFragment.class);

    @Override
    public void onViewCreated(android.view.View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        if (mPresenter == null) {
            mPresenter = new GameInfoPresenter(getContext(), getLoaderManager(), this);
        }
        registerReceiver();
    }

    public void updateFields(String players, String servers, String searching,
                             String searchSeconds, String scheduler, String logon,
                             String community, String items, String leaderboards,
                             String version, String timestamp, String time) {
        mPlayers.setText(players);
        mServers.setText(servers);
        mSearching.setText(searching);
        mSearchSeconds.setText(searchSeconds);
        mScheduler.setText(scheduler);
        mSessionsLogon.setText(logon);
        mSteamCommunity.setText(community);
        mIeconItems.setText(items);
        mLeaderboards.setText(leaderboards);
        mVersion.setText(version);
        mTimestamp.setText(timestamp);
        mTime.setText(time);
    }

    /**
     * Register broadcast receiver for listen network connection changers
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        mReceiver = new NetworkReceiver();
        mReceiver.addListener(mPresenter);
        getActivity().getApplicationContext().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(mReceiver);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game_info;
    }

    @Override
    public void showProgress(boolean state) {
        super.showProgress(state);
    }

    public void showView(boolean state) {
        mRefresh.setVisibility(state ? View.VISIBLE : View.GONE);
        mMessageError.setVisibility(state ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        mPresenter.showLst();
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 700);
    }
}
