package com.example.monitor.serverdetails;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import com.example.monitor.servers.ServerModel;
import com.example.monitor.utils.Helpers;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;

import java.util.ArrayList;

public class ServerDetailsPresenter implements LoaderManager.LoaderCallbacks<ServerDetailsModel>{

    private Context mContext;
    private IView mView;
    private LoaderManager mLoaderManager;
    private String mIpAddress;

    private static final String TAG = Helpers.makeLogTag(ServerDetailsPresenter.class);
    private static final int LOADER_ID = 1;

    public ServerDetailsPresenter(Context context, String ip, LoaderManager loaderManager, IView view){
        mContext = context;
        mIpAddress = ip;
        mLoaderManager = loaderManager;
        mView = view;
        updateList();
    }

    private void updateList(){
        mLoaderManager.restartLoader(LOADER_ID, null, this);
    }

    public void onDestroy(){
        mView = null;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID){
            return new ServerDetailsLoader(mContext, mIpAddress);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ServerDetailsModel> loader, ServerDetailsModel data) {
        if(data != null){
            ServerModel server = data.getServer();
            mView.updateFields(
                    server.getMap(),
                    server.getPlayers(),
                    server.getmName(),
                    server.getGame(),
                    server.getTags()
            );

            ArrayList<SteamPlayer> playersList = data.getPlayersList();
            mView.setAdapterData(playersList);
            Log.i(TAG, "result " + data.toString());
        } else {
            Log.i(TAG, "result " + "data is NULL");
        }
    }

    @Override
    public void onLoaderReset(Loader<ServerDetailsModel> loader) {

    }

    void onRefresh(){
        updateList();
    }
}

