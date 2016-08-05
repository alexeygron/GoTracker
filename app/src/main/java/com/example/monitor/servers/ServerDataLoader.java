package com.example.monitor.servers;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.monitor.utils.LogUtils;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;
import com.lotr.steammonitor.app.R;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class ServerDataLoader extends AsyncTaskLoader<ServerModel> {

    public final String TAG = LogUtils.makeLogTag(ServerDataLoader.class);

    private Context mContext;
    private ServerModel mServer;

    public ServerDataLoader(Context context, ServerModel server) {
        super(context);
        mContext = context;
        mServer = server;
    }

    @Override
    public ServerModel loadInBackground() {
        Log.d(TAG, "loadInBackground");
        HashMap<String, Object> response;
        SteamSocket.setTimeout(3000);
        SourceServer sourceServer;

        try {
            sourceServer = new SourceServer(mServer.getIpAddr());
            sourceServer.initialize();
            response = sourceServer.getServerInfo();
        } catch (SteamCondenserException | TimeoutException e) {
            e.printStackTrace();
            mServer.setSrvName(mContext.getResources().getString(R.string.err_get_server));
            return mServer;
        }
        return convertToModel(response);
    }

    private ServerModel convertToModel(HashMap<String, Object> response){
        mServer.setSrvName(response.get("serverName").toString());
        mServer.setMap(response.get("mapName").toString());
        mServer.setNumPlayers(response.get("numberOfPlayers").toString());
        mServer.setMaxPlayers(response.get("maxPlayers").toString());
        mServer.setGame(response.get("gameDescription").toString());
        mServer.setTags(response.get("serverTags").toString());
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
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(TAG, "onStopLoading");
    }

    @Override
    public void deliverResult(ServerModel data) {
        super.deliverResult(data);
        Log.d(TAG, "deliverResult");
    }
}