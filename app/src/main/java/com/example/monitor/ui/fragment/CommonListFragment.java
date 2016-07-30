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

/**
 *  Содержит общую функциональность для фрагментов отображающих экран списка.
 */
public abstract class CommonListFragment extends CommonFragment{

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected ListServersAdapter mAdapter;
    protected LinearLayoutManager mLayoutManager;
    protected RecyclerView.ItemAnimator mitemAnimator = new DefaultItemAnimator();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_servers);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
        mRecyclerView.setItemAnimator(mitemAnimator);
    }
}
