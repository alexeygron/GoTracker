package com.example.monitor.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.monitor.ui.view.VerticalSpaceItemDecoration;
import com.lotr.steammonitor.app.R;

/**
 *  Содержит общую функциональность для похожих фрагментов содержащих список.
 */
public abstract class CommonListFragment extends CommonFragment{

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    LinearLayoutManager mLayoutManager;
    RecyclerView.ItemAnimator mitemAnimator = new DefaultItemAnimator();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_doctors);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));
        mRecyclerView.setItemAnimator(mitemAnimator);
    }
}
