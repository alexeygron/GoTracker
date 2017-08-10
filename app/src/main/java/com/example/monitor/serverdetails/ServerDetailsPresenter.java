package com.example.monitor.serverdetails;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import com.example.monitor.servers.Server;
import com.example.monitor.utils.Helpers;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;

import java.util.ArrayList;

class ServerDetailsPresenter implements LoaderManager.LoaderCallbacks<ServerDetails>{

    private Context mContext;
    private IView mView;
    private LoaderManager mLoaderManager;
    private String mIpAddress;

    private static final String TAG = Helpers.makeLogTag(ServerDetailsPresenter.class);
    private static final int LOADER_ID = 1;

    ServerDetailsPresenter(Context context, String ip, LoaderManager loaderManager, IView view){
        mContext = context;
        mIpAddress = ip;
        mLoaderManager = loaderManager;
        mView = view;
        updateList();
    }

    private void updateList(){
        mLoaderManager.restartLoader(LOADER_ID, null, this);
    }

    void onDestroy(){
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
    public void onLoadFinished(Loader<ServerDetails> loader, ServerDetails data) {
        if(data != null){
            Server server = data.getServer();
            mView.updateFields(
                    server.getMap(),
                    server.getPlayers(),
                    server.getmName(),
                    server.getGame(),
                    server.getTags()
            );

            ArrayList<SteamPlayer> playersList = data.getPlayersList();
            mView.setAdapterData(playersList);
        }
    }

    @Override
    public void onLoaderReset(Loader<ServerDetails> loader) {

    }

    void onRefresh(){
        updateList();
    }
}

