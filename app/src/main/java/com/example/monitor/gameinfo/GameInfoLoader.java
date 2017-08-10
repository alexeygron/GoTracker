package com.example.monitor.gameinfo;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.monitor.Config;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.WebApi;

import static com.example.monitor.utils.Helpers.makeLogTag;

/**
 * Receives common info about game from network in background thread
 */
class GameInfoLoader extends AsyncTaskLoader<String> {

    public final String TAG = makeLogTag(GameInfoLoader.class);

    GameInfoLoader(Context context) {
        super(context);
    }

    @Override
    public String loadInBackground() {
        String result = "";
        try {
            WebApi.setApiKey(Config.STEAM_API_KEY);
            result = WebApi.getJSON("ICSGOServers_730", "GetGameServersStatus", 1);
        } catch (SteamCondenserException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}