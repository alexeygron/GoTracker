package com.example.monitor.players;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.monitor.Config;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.WebApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.monitor.utils.Helpers.makeLogTag;

class PlayersDataLoader extends AsyncTaskLoader<Player> {

    public final String TAG = makeLogTag(PlayersDataLoader.class);

    private Player mPlayer;

    PlayersDataLoader(Context context, Player player) {
        super(context);
        mPlayer = player;
    }

    @Override
    public Player loadInBackground() {
        String responce;
        try {
            WebApi.setApiKey(Config.STEAM_API_KEY);
            Map<String, Object> params = new HashMap<>();
            params.put("steamids", mPlayer.getSteamID());
            responce = WebApi.getJSON("ISteamUser", "GetPlayerSummaries", 2, params);
            return convertToModel(responce);
        } catch (SteamCondenserException e) {
            e.printStackTrace();
            mPlayer.setPersonName(mPlayer.getSteamID());
            return mPlayer;
        }
    }

    private Player convertToModel(String json) {
        try {
            JSONObject steamUser = new JSONObject(json).
                    getJSONObject("response").
                    getJSONArray("players").
                    getJSONObject(0);
            // Инициируем основные поля класса
            mPlayer.setSteamID(steamUser.getString("steamid"));
            mPlayer.setPersonaState(Integer.parseInt(steamUser.getString("personastate")));
            mPlayer.setPersonName(steamUser.getString("personaname"));
            mPlayer.setLastLogoff(Long.parseLong(steamUser.getString("lastlogoff")));
            mPlayer.setProfileUrl(steamUser.getString("profileurl"));
            mPlayer.setAvatarUrl(steamUser.getString("avatarfull"));

            // Поле gameextrainfo в json присутствует не всегда
            // А только тогда, когда игрок находится в игре
            // Проверяем есть ли оно, если да, то инициируем его в объекте тоже
            if (!steamUser.isNull("gameextrainfo")) {
                mPlayer.setmGameExtraInfo(steamUser.getString("gameextrainfo"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mPlayer.setPersonName(mPlayer.getSteamID());
        }
        return mPlayer;
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}