package com.example.monitor.players;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.monitor.servers.ServerModel;
import com.example.monitor.utils.Config;
import com.example.monitor.utils.LogUtils;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.WebApi;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;
import com.lotr.steammonitor.app.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class PlayersDataLoader extends AsyncTaskLoader<String> {

    public final String TAG = LogUtils.makeLogTag(PlayersDataLoader.class);

    private Context mContext;
    private ServerModel mServer;

    public PlayersDataLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "loadInBackground");

        try {
            WebApi.setApiKey(Config.WEB_API_KEY);
            Map<String, Object> params = new HashMap<>();

            String ids = "76561198158747054,76561198307741380,";
            params.put("steamids", ids);

            String json = WebApi.getJSON("ISteamUser", "GetPlayerSummaries", 2, params);

            Log.i (TAG, "responce: " + json.toString());
        } catch (SteamCondenserException e) {
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