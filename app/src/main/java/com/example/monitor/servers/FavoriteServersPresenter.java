package com.example.monitor.servers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.monitor.db.ServersDao;
import com.example.monitor.serverdetails.ServerDetailsActivity;
import com.example.monitor.utils.Helpers;
import com.example.monitor.utils.NetworkReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Презентер из пттерна MVP для
 */
public class FavoriteServersPresenter implements LoaderManager.LoaderCallbacks<ServerModel>, NetworkReceiver.NetChangeListener {

    private IView mView;
    private Context mContext;
    private LoaderManager mLoaderManager;
    private ServersDao mDB;
    private List<ServerModel> mListData;
    private int loaderTaskIndex;
    private NetworkReceiver mReceiver;

    public static final int LOADER_ID = 1;

    private static final String TAG = Helpers.makeLogTag(FavoriteServersPresenter.class);

    public FavoriteServersPresenter(LoaderManager lm, Context context, IView view) {
        mView = view;
        mContext = context;
        mLoaderManager = lm;
        mDB = new ServersDao(context);
        mListData = new ArrayList<>();
        //registerReceiver();
    }

    private void updateList(ServerModel server) {
        mListData.set(loaderTaskIndex, server);
        mView.setData(mListData);
        mView.updateList();
    }

    private void showList() {
        mListData.clear();
        mListData.addAll(mDB.get());
        loaderTaskIndex = 0;
        if(mLoaderManager.getLoader(LOADER_ID) != null) {
            mLoaderManager.restartLoader(LOADER_ID, null, this);
        } else {
            mLoaderManager.initLoader(LOADER_ID, null, this);
        }
        Log.i(TAG, "list size " + mListData.size());
    }

    public void networkEnabled() {
        Log.i(TAG, "networkEnabled");
        showList();
        mView.showList();
    }

    public void networkDiasbled() {
        Log.i(TAG, "networkDiasbled");
        mView.hideList();
    }

    void onClickDelButton(int position) {
        Log.i(TAG, "list size " + mListData.size());
        String id = mListData.get(position).getDbId();
        mDB.delete(id);
        mListData.remove(position);
        mView.setData(mListData);
        Log.i(TAG, "del button click! " + position);
    }

    void addServer(String ip) {
        mDB.insert(ip);
        onRefresh();
    }

    public void onRefresh() {
        mLoaderManager.destroyLoader(LOADER_ID);
        showList();
    }

    void onClickListItem(int position) {
        Log.i(TAG, "list item click!");
        //ServerDetailsFragment fragment = new ServerDetailsFragment();
        //mFragmentManager.beginTransaction().add(R.id.main_container, fragment, "DETAILS").addToBackStack(null).commit();
        Intent i = new Intent(mContext, ServerDetailsActivity.class);
        ServerModel server = mListData.get(position);
        i.putExtra("name", server.getmName());
        i.putExtra("game", server.getGame());
        i.putExtra("ip", server.getIpAddr());
        i.putExtra("tags", server.getTags());
        i.putExtra("map", server.getMap());
        i.putExtra("players", server.getPlayers());
        mContext.startActivity(i);
    }

    public void onTakeView(IView view) {
        this.mView = view;
    }

    @Override
    public Loader<ServerModel> onCreateLoader(int id, Bundle args) {
        if (mListData.size() > 0 & loaderTaskIndex < mListData.size()) {
            mView.showProgress();
            return new ServerDataLoader(mContext, mListData.get(loaderTaskIndex));
        } else {
            mView.hideProgress();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ServerModel> loader, ServerModel data) {
        if (data != null) {
            updateList(data);
            loaderTaskIndex++;
            Log.i(TAG, "loader result " + data.toString());
            mLoaderManager.restartLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public void onLoaderReset(Loader<ServerModel> loader) {
    }

   /* private void registerReceiver(){
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        mReceiver = new NetworkReceiver();
        mReceiver.addListener(this);
        mContext.registerReceiver(mReceiver, filter);
    }
    */

    void onDestroy() {
        mView = null;
        mLoaderManager.destroyLoader(LOADER_ID);
    }


    void init() {
        mDB.insert("37.187.205.242:27015");
        mDB.insert("86.104.11.123:27015");
        mDB.insert("46.174.53.181:27015");
        mDB.insert("192.95.29.182:27017");
        mDB.insert("185.113.141.19:27103");
        mDB.insert("46.174.50.142:55555");
        // showList();
    }
}
