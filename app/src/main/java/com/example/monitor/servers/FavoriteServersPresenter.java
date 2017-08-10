package com.example.monitor.servers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.monitor.db.ServersDao;
import com.example.monitor.serverdetails.ServerDetailsActivity;
import com.example.monitor.utils.NetworkReceiver;
import com.lotr.steammonitor.app.BuildConfig;

import java.util.ArrayList;

import static com.example.monitor.utils.Helpers.makeLogTag;

class FavoriteServersPresenter implements LoaderManager.LoaderCallbacks<Server>, NetworkReceiver.NetChangeListener {

    private IView mView;
    private Context mContext;
    private LoaderManager mLoaderManager;
    private ServersDao mDB;
    private ArrayList<Server> mListData;
    private int loaderTaskIndex;

    private static final int LOADER_ID = 1;

    private static final String TAG = makeLogTag(FavoriteServersPresenter.class);

    FavoriteServersPresenter(LoaderManager lm, Context context, IView view) {
        mView = view;
        mContext = context;
        mLoaderManager = lm;
        mDB = new ServersDao(context);
        mListData = new ArrayList<>();
    }

    private void updateList(Server server) {
        mListData.set(loaderTaskIndex, server);
        mView.setData(mListData);
        mView.updateList();
    }

    private void showList() {
        mListData.clear();
        mListData.addAll(mDB.get());
        loaderTaskIndex = 0;
        if (mLoaderManager.getLoader(LOADER_ID) != null) {
            mLoaderManager.restartLoader(LOADER_ID, null, this);
        } else {
            mLoaderManager.initLoader(LOADER_ID, null, this);
        }
    }

    public ArrayList<Server> getData() {
        return mListData;
    }

    public void setData(ArrayList<Server> data) {
        mListData.addAll(data);
    }

    public void onNetworkEnabled() {
        if (BuildConfig.DEBUG) Log.i(TAG, "network_enabled");
        showList();
        mView.showList(true);
    }

    public void onNetworkDiasbled() {
        if (BuildConfig.DEBUG) Log.i(TAG, "network_disabled");
        mView.showList(false);
    }

    void onClickDelButton(int position) {
        String id = mListData.get(position).getDbId();
        mDB.delete(id);
        mListData.remove(position);
        mView.setData(mListData);
    }

    void addServer(String ip) {
        mDB.insert(ip);
        onRefresh();
    }

    void onRefresh() {
        mLoaderManager.destroyLoader(LOADER_ID);
        showList();
    }

    void onClickListItem(int position) {
        Intent i = new Intent(mContext, ServerDetailsActivity.class);
        Server server = mListData.get(position);
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
    public Loader<Server> onCreateLoader(int id, Bundle args) {
        if (mListData.size() > 0 & loaderTaskIndex < mListData.size()) {
            mView.showProgress(true);
            return new ListServersLoader(mContext, mListData.get(loaderTaskIndex));
        } else {
            mView.showProgress(false);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Server> loader, Server data) {
        if (data != null) {
            updateList(data);
            loaderTaskIndex++;
            mLoaderManager.restartLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public void onLoaderReset(Loader<Server> loader) {
    }

    void onResume() {
        if (mListData.size() != 0) {
            mView.setData(mListData);
            mView.updateList();
        }
    }

    void onDestroy() {
        mView = null;
        mLoaderManager.destroyLoader(LOADER_ID);
    }
}
