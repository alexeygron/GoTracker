package com.example.monitor.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.monitor.ui.view.VerticalSpaceItemDecoration;
import com.lotr.steammonitor.app.R;

public abstract class CommonFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    LinearLayoutManager mLayoutManager;
    RecyclerView.ItemAnimator mitemAnimator = new DefaultItemAnimator();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(getLayoutId(), container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_doctors);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));
        mRecyclerView.setItemAnimator(mitemAnimator);
        return rootView;
    }

    protected abstract int getLayoutId();
}
