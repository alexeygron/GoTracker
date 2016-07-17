package com.example.monitor.servers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;
import com.example.monitor.model.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class ServersLoader extends AsyncTaskLoader<Server> {

    public final String TAG = "LOADER_SERVERS";

    private String ipAddr;

    public ServersLoader(Context context, Bundle args) {
        super(context);
        ipAddr = args.getString("ip");
    }

    @Override
    public Server loadInBackground() {
        Log.d(TAG, "loadInBackground");

        HashMap<String, Object> response = null;

        SteamSocket.setTimeout(3000);
        SourceServer sourceServer = null;
        try {

            sourceServer = new SourceServer(ipAddr);
            sourceServer.initialize();
            response = sourceServer.getServerInfo();

        } catch (SteamCondenserException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return convertToModel(response);
    }

    private Server convertToModel(HashMap<String, Object> response){
        Log.i("RESPONCE", response.toString());
        Server server = new Server(ipAddr);
        server.setSrvName(response.get("serverName").toString());
        server.setMap(response.get("mapName").toString());
        server.setNumPlayers(response.get("numberOfPlayers").toString());
        server.setMaxPlayers(response.get("maxPlayers").toString());
        server.setGame(response.get("gameDescription").toString());
        server.setTags(response.get("serverTags").toString());

        return server;
    }

    @Override
    public void forceLoad() {
        Log.d(TAG, "forceLoad");
        super.forceLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG, "onStartLoading");
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(TAG, "onStopLoading");
    }

    @Override
    public void deliverResult(Server data) {
        Log.d(TAG, "deliverResult");
        super.deliverResult(data);
    }
}