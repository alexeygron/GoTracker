package com.example.monitor.servers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;
import com.lotr.steammonitor.app.R;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class ServersLoader extends AsyncTaskLoader<Server> {

    public final String TAG = "LOADER_SERVERS";

    private Context mContext;
    private Server mServer;

    public ServersLoader(Context context, Server server) {
        super(context);
        mContext = context;
        mServer = server;
    }

    @Override
    public Server loadInBackground() {
        Log.d(TAG, "loadInBackground");

        HashMap<String, Object> response = null;

        SteamSocket.setTimeout(3000);
        SourceServer sourceServer;
        try {
            sourceServer = new SourceServer(mServer.getIpAddr());
            sourceServer.initialize();
            response = sourceServer.getServerInfo();
        } catch (SteamCondenserException | TimeoutException e) {
            e.printStackTrace();
        }
        return convertToModel(response);
    }

    private Server convertToModel(HashMap<String, Object> response){

        if (response != null) {
            mServer.setSrvName(response.get("serverName").toString());
            mServer.setMap(response.get("mapName").toString());
            mServer.setNumPlayers(response.get("numberOfPlayers").toString());
            mServer.setMaxPlayers(response.get("maxPlayers").toString());
            mServer.setGame(response.get("gameDescription").toString());
            mServer.setTags(response.get("serverTags").toString());
        } else {
            mServer.setSrvName(mContext.getResources().getString(R.string.err_get_server));
        }
        return mServer;
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
        Log.d(TAG, "forceLoad");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG, "onStartLoading");
        if (true)
             forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(TAG, "onStopLoading");
    }

    @Override
    public void deliverResult(Server data) {
        super.deliverResult(data);
        Log.d(TAG, "deliverResult");
    }
}