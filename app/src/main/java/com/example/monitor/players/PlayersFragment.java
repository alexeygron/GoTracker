package com.example.monitor.players;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.monitor.ui.fragment.CommonListFragment;
import com.example.monitor.ui.view.AddItemDialog;
import com.example.monitor.utils.Helpers;
import com.example.monitor.utils.NetworkReceiver;
import com.lotr.steammonitor.app.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayersFragment extends CommonListFragment implements
        IView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AddItemDialog.Callback {

    @BindView(R.id.button_add) FloatingActionButton mButtonAdd;
    @BindView(R.id.message) TextView mMessageError;
    @BindView(R.id.content) FrameLayout mContentFrame;
    private FavoritePlayersPresenter mPresenter;
    private ListPlayersAdapter mAdapter;
    private NetworkReceiver mReceiver;

    private static final String TAG = Helpers.makeLogTag(PlayersFragment.class);

    @Override
    public void onViewCreated(android.view.View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mButtonAdd.setOnClickListener(this);
        setHasOptionsMenu(true);

        if (mPresenter == null) {
            mPresenter = new FavoritePlayersPresenter(getContext(), getLoaderManager(), this);
        }
        mAdapter = new ListPlayersAdapter(mPresenter);
        mRecyclerView.setAdapter(mAdapter);
        registerReceiver();
    }

    public void showList() {
        mContentFrame.setVisibility(View.VISIBLE);
        mMessageError.setVisibility(View.GONE);
    }

    public void hideList() {
        mContentFrame.setVisibility(View.GONE);
        mMessageError.setVisibility(View.VISIBLE);
    }

    private void showDialog(AddItemDialog.Callback callback,int title, int hint){
        AddItemDialog dialogFragment = AddItemDialog.createInstance(
                callback, title, hint);
        dialogFragment.show(getFragmentManager(), null);
    }

    public void updateList() {
        mAdapter.notifyDataSetChanged();
    }

    public void setData(List<PlayerModel> data) {
        mAdapter.setData(data);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_servers;
    }


    @Override
    public void showProgress() {
        super.showProgress();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(mReceiver);
    }

    /**
     * Регистрирует broadcast receiver для прослушивания изменения соединения с интернетом
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        mReceiver = new NetworkReceiver();
        mReceiver.addListener(mPresenter);
        getActivity().getApplicationContext().registerReceiver(mReceiver, filter);
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

    @Override
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.button_add:
                showDialog(this, R.string.add_player_title, R.string.dialog_id_text);
                break;
        }
    }

    @Override
    public void onPositiveClick(String item) {
        mPresenter.addPlayer(item);
    }
}
