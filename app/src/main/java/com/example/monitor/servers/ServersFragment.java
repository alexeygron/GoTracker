package com.example.monitor.servers;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.example.monitor.async.ServersLoader;
import com.example.monitor.db.DBWorker;
import com.example.monitor.ui.adapter.ListServersAdapter;
import com.example.monitor.ui.fragment.CommonListFragment;
import com.lotr.steammonitor.app.R;
import com.example.monitor.model.Server;

import java.util.ArrayList;

public class ServersFragment extends CommonListFragment implements LoaderManager.LoaderCallbacks<Server>
        ,SwipeRefreshLayout.OnRefreshListener{


    Bundle mArgs = new Bundle();
    String[] mas;

    public static final int LOADER_ID = 1;
    private int srvIndex;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        Server srv = new Server("21.23.45.631:25016");
        srv.setSrvName("Бойцовский Клуб 18+");
        srv.setMap("de_dust");
        srv.setNumPlayers("23");

        mListData = new ArrayList<Server>();
      /*  for (int i = 0; i < 4; i++){
            mListData.add(srv);
        }
       */
/*
        DBWorker db = new DBWorker(getActivity(), "favorites");
        db.insert("46.174.53.216:27015");
        db.insert("46.174.53.216:27015");
        db.insert("46.174.53.216:27015");*/

        showList();
        mAdapter = new ListServersAdapter(mListData);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public ServersLoader onCreateLoader(int id, Bundle args) {

            return new ServersLoader(getActivity(), args);

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Server> loader, Server data) {
        if (data != null){
            updateList(data);
            srvIndex++;
            Log.i("LOADER_RESULT", data.toString());
            if (srvIndex < mas.length) {
                mArgs.putString("ip", mas[srvIndex]);
                getLoaderManager().restartLoader(LOADER_ID, mArgs, this);
            }
        }
    }


    private void updateList(Server server){
        mListData.add(server);
        mAdapter.notifyDataSetChanged();
    }

    private void showList(){
        DBWorker db = new DBWorker(getActivity(), "favorites");
        mas = db.read();
        srvIndex = 0;
        mListData.clear();
        mArgs.putString("ip", mas[srvIndex]);
        getLoaderManager().restartLoader(LOADER_ID, mArgs, this);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Server> loader) {

    }

    protected int getLayoutId(){
        return R.layout.fragment_servers;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                showList();
            }
        }, 1000);
    }
}
