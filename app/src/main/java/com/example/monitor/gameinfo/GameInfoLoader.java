package com.example.monitor.gameinfo;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.monitor.servers.ServerModel;
import com.example.monitor.utils.Helpers;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.WebApi;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;
import com.lotr.steammonitor.app.R;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class GameInfoLoader extends AsyncTaskLoader<String> {

    public final String TAG = Helpers.makeLogTag(GameInfoLoader.class);

    public GameInfoLoader(Context context) {
        super(context);
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "loadInBackground");
        String result = "";
        try {
            WebApi.setApiKey("20AA3876A3B48CB80B88033C77056A1F");
            result = WebApi.getJSON("ICSGOServers_730", "GetGameServersStatus", 1);
        } catch (SteamCondenserException e) {
            e.printStackTrace();
        }
        return result;
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