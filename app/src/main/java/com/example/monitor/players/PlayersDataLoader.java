package com.example.monitor.players;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.monitor.utils.Config;
import com.example.monitor.utils.Helpers;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.WebApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlayersDataLoader extends AsyncTaskLoader<PlayerModel> {

    public final String TAG = Helpers.makeLogTag(PlayersDataLoader.class);

    private Context mContext;
    private PlayerModel mPlayer;

    public PlayersDataLoader(Context context, PlayerModel player) {
        super(context);
        mContext = context;
        mPlayer = player;
    }

    @Override
    public PlayerModel loadInBackground() {
        Log.d(TAG, "loadInBackground");
        String responce;
        try {
            WebApi.setApiKey(Config.WEB_API_KEY);
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

    private PlayerModel convertToModel(String json) {
        Log.i(TAG, "json " + json);
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
    public void deliverResult(PlayerModel data) {
        super.deliverResult(data);
        Log.d(TAG, "deliverResult");
    }
}