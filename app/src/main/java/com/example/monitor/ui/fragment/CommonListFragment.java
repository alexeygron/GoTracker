package com.example.monitor.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.monitor.servers.ListServersAdapter;
import com.example.monitor.ui.view.VerticalSpaceItemDecoration;
import com.lotr.steammonitor.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  Содержит общую функциональность для фрагментов отображающих экран списка.
 */
public abstract class CommonListFragment extends CommonFragment{

    @BindView(R.id.list_servers) protected RecyclerView mRecyclerView;
    protected ListServersAdapter mAdapter;
    protected LinearLayoutManager mLayoutManager;
    protected RecyclerView.ItemAnimator mitemAnimator = new DefaultItemAnimator();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
        mRecyclerView.setItemAnimator(mitemAnimator);
    }

    public void showProgress() {
        mProgressBar.setVisibility(android.view.View.VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(android.view.View.GONE);
    }
}
