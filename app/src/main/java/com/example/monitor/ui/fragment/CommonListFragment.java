package com.example.monitor.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.monitor.servers.ListServersAdapter;
import com.example.monitor.ui.view.VerticalSpaceItemDecoration;
import com.lotr.steammonitor.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class CommonListFragment extends CommonFragment {

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
        }

        mRecyclerView.setItemAnimator(mitemAnimator);
    }
}
