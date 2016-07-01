package com.example.monitor.ui.fragments;

import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.monitor.async.ServersLoader;
import com.example.monitor.db.DBWorker;
import com.example.monitor.net.GetServersTask;
import com.example.monitor.ui.adapter.ListServersAdapter;
import com.lotr.steammonitor.app.R;
import com.lotr.steammonitor.app.Server;

import java.util.ArrayList;

public class ServersFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ServersLoader mLoader;
    private ArrayList<Server> mListData = new ArrayList<Server>();

    public static final int LOADER_ID = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servers, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_doctors);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Server srv = new Server();
        srv.setSrvName("Бойцовский Клуб 18+");
        mListData.add(srv);

         srv.setSrvName("Бойцовский Клуб 18+");
        mListData.add(srv);

         srv.setSrvName("Бойцовский Клуб 18+");
        mListData.add(srv);

         srv.setSrvName("Бойцовский Клуб 18+");
        mListData.add(srv);

         srv.setSrvName("Бойцовский Клуб 18+");
        mListData.add(srv);

         srv.setSrvName("Бойцовский Клуб 18+");
        mListData.add(srv);

         srv.setSrvName("Бойцовский Клуб 18+");
        mListData.add(srv);

         srv.setSrvName("Бойцовский Клуб 18+");
        mListData.add(srv);

         srv.setSrvName("Бойцовский Клуб 18+");
        mListData.add(srv);

         srv.setSrvName("Бойцовский Клуб 18+");
        mListData.add(srv);

         srv.setSrvName("Бойцовский Клуб 18+");
        mListData.add(srv);


        mAdapter = new ListServersAdapter(mListData);
        mRecyclerView.setAdapter(mAdapter);

        //getLoaderManager().initLoader(LOADER_ID, null, this);
        return view;
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

}
