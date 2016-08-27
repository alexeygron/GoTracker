package com.example.monitor.servers;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
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

public class ServersFragment extends CommonListFragment implements
        IView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AddItemDialog.Callback {

    private static final String TAG = Helpers.makeLogTag(ServersFragment.class);

    @BindView(R.id.button_add) FloatingActionButton mButtonAdd;
    @BindView(R.id.content) FrameLayout mContentFrame;
    @BindView(R.id.message) TextView mMessageError;
    //PullToRefreshView mPullToRefreshView;
    //private AVLoadingIndicatorView mProgressBar;
    private FavoriteServersPresenter mPresenter;
    private NetworkReceiver mReceiver;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mButtonAdd.setOnClickListener(this);
        //setRetainInstance(true);
        setHasOptionsMenu(true);
        if (mPresenter == null) {
            mPresenter = new FavoriteServersPresenter(getLoaderManager(), getContext(), this);
        }
        mAdapter = new ListServersAdapter(mPresenter);
        mRecyclerView.setAdapter(mAdapter);
        registerReceiver();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_button:
                mPresenter.init();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(mReceiver);
        Log.i(TAG, "OnDestroy");
    }

    public void updateList() {
        mAdapter.notifyDataSetChanged();
    }

    public void setData(List<ServerModel> data) {
        mAdapter.setData(data);
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

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
         super.showProgress();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    protected int getLayoutId() {
        return R.layout.fragment_servers;
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
                showDialog(this, R.string.add_server_title, R.string.dialog_ip_text);
                break;
        }
    }

    @Override
    public void onPositiveClick(String item) {
        Log.i(TAG, item);
        mPresenter.addServer(item);
    }
}
