package com.example.monitor.ui.fragments;

import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.monitor.async.ServersLoader;
import com.example.monitor.ui.adapter.ListServersAdapter;
import com.example.monitor.ui.view.VerticalSpaceItemDecoration;
import com.lotr.steammonitor.app.R;
import com.lotr.steammonitor.app.Server;

import java.util.ArrayList;

public class ServersFragment extends CommonFragment implements LoaderManager.LoaderCallbacks<String>{

    private ServersLoader mLoader;
    private ArrayList<Server> mListData = new ArrayList<Server>();

    public static final int LOADER_ID = 1;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Server srv = new Server();
        srv.setSrvName("Бойцовский Клуб 18+");

        for (int i = 0; i < 4; i++){
            mListData.add(srv);
        }


        mAdapter = new ListServersAdapter(mListData);
        mRecyclerView.setAdapter(mAdapter);

        //getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public ServersLoader onCreateLoader(int id, Bundle args) {

        if (LOADER_ID == id){
            mLoader = new ServersLoader(getActivity());
        }

        return mLoader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader) {

    }

    protected int getLayoutId(){
        return R.layout.fragment_servers;
    }
}
