package com.example.monitor.serverdetails;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * Загружает из сети в фоновом потоке информацию о сервере.
 */
public class ServerDetailsLoader extends AsyncTaskLoader<String> {

    public final String TAG = "ServersDetailsLoader";
    private String ipAddress;

    public ServerDetailsLoader(Context context, String ip) {
        super(context);
        ipAddress = ip;
    }

    @Override
    public String loadInBackground() {

        HashMap players;
        HashMap info;

        try {

            SourceServer server = new SourceServer(ipAddress);
            server.initialize();
            players = server.getPlayers();
            info = server.getServerInfo();

            Log.i(TAG, players.toString());
            Log.i(TAG, info.toString());

        } catch (SteamCondenserException | TimeoutException e) {
            e.printStackTrace();
        }

        return "";
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
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(TAG, "onStopLoading");
    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
        Log.d(TAG, "deliverResult");
    }
}
