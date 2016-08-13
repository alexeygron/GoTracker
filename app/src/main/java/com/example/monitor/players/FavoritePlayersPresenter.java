package com.example.monitor.players;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.monitor.servers.ServerModel;
import com.example.monitor.utils.LogUtils;

/**
 * Created by lotr on 08.08.2016.
 */
public class FavoritePlayersPresenter implements LoaderManager.LoaderCallbacks<String>{

    private Context mContext;
    private LoaderManager mLoaderManager;

    private static final String TAG = LogUtils.makeLogTag(FavoritePlayersPresenter.class);

    public FavoritePlayersPresenter(Context context, LoaderManager lm) {
        mLoaderManager = lm;
        mContext = context;
        loadData();
    }

    private void loadData(){
        mLoaderManager.restartLoader(1, null, this);
    }

    void addNewItem(String item) {
      Log.i(TAG,"new item: " + item);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new PlayersDataLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.i(TAG, "load: " + data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
