package com.example.monitor.GameInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;

import com.example.monitor.servers.ServerModel;
import com.example.monitor.utils.Helpers;
import com.example.monitor.utils.NetworkReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class GameInfoPresenter implements LoaderManager.LoaderCallbacks<String>, NetworkReceiver.NetChangeListener {

    private IView mView;
    private Context mContext;
    private LoaderManager mLoaderManager;

    public static final int LOADER_ID = 1;

    private static final String TAG = Helpers.makeLogTag(GameInfoPresenter.class);

    public GameInfoPresenter(Context context, LoaderManager lm, IView view){
        mContext = context;
        mLoaderManager = lm;
        mView = view;
    }

    public void showLst(){
        if(mLoaderManager.getLoader(LOADER_ID) != null) {
            mLoaderManager.restartLoader(LOADER_ID, null, this);
        } else {
            mLoaderManager.initLoader(LOADER_ID, null, this);
        }
    }

    private void jsonToFields(String json){
        try {
            JSONObject root = new JSONObject(json);
            JSONObject app = root.getJSONObject("result").getJSONObject("app");
            JSONObject services = root.getJSONObject("result").getJSONObject("services");
            JSONObject matchmaking = root.getJSONObject("result").getJSONObject("matchmaking");

            mView.updateFields(
                    matchmaking.getString("online_players"),
                    matchmaking.getString("online_servers"),
                    matchmaking.getString("searching_players"),
                    matchmaking.getString("search_seconds_avg"),
                    matchmaking.getString("scheduler"),
                    services.getString("SessionsLogon"),
                    services.getString("SteamCommunity"),
                    services.getString("IEconItems"),
                    services.getString("Leaderboards"),
                    app.getString("version"),
                    app.getString("timestamp"),
                    app.getString("time")
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void networkEnabled() {
        Log.i(TAG, "networkEnabled");
        showLst();
        mView.showView();
    }

    @Override
    public void networkDiasbled() {
        Log.i(TAG, "networkDiasbled");
        mView.hideView();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new GameInfoLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (!TextUtils.isEmpty(data)){
            Log.i(TAG, "loading result: " + data.toString());
            jsonToFields(data);
        } else {
            Log.i(TAG, "loading result is NULL");
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    public void onDestroy(){
        mView = null;
        mLoaderManager.destroyLoader(LOADER_ID);
    }
}
