package com.example.monitor.servers;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.monitor.ui.fragment.CommonListFragment;
import com.example.monitor.ui.view.AddItemDialog;
import com.example.monitor.utils.NetworkReceiver;
import com.lotr.steammonitor.app.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.monitor.utils.Helpers.makeLogTag;

public class ServersFragment extends CommonListFragment implements
        IView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AddItemDialog.Callback {

    @BindView(R.id.button_add) FloatingActionButton mButtonAdd;
    @BindView(R.id.content) FrameLayout mContentFrame;
    @BindView(R.id.message) TextView mMessageError;
    private FavoriteServersPresenter mPresenter;
    private NetworkReceiver mReceiver;

    private static final String TAG = makeLogTag(ServersFragment.class);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mButtonAdd.setOnClickListener(this);
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

    public void updateList() {
        mAdapter.notifyDataSetChanged();
    }

    public void setData(List<ServerModel> data) {
        mAdapter.setData(data);
    }

    public void showList(boolean state) {
        mContentFrame.setVisibility(state ? View.VISIBLE : View.GONE);
        mMessageError.setVisibility(state ? View.GONE : View.VISIBLE);
    }

    private void showDialog(AddItemDialog.Callback callback, int title, int hint) {
        AddItemDialog dialogFragment = AddItemDialog.createInstance(
                callback, title, hint);
        dialogFragment.show(getFragmentManager(), null);
    }

    @Override
    public void showProgress(boolean state) {
        super.showProgress(state);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                showDialog(this, R.string.add_server_title, R.string.dialog_ip_text);
                break;
        }
    }

    @Override
    public void onPositiveClick(String item) {
        mPresenter.addServer(item);
    }
}
