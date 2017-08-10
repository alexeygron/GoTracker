package com.example.monitor.servers;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;
import com.lotr.steammonitor.app.R;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import static com.example.monitor.utils.Helpers.makeLogTag;

/**
 * Receives game servers list from network in background thread
 */
class ListServersLoader extends AsyncTaskLoader<Server> {

    public final String TAG = makeLogTag(ListServersLoader.class);

    private Context mContext;
    private Server mServer;

    ListServersLoader(Context context, Server server) {
        super(context);
        mContext = context;
        mServer = server;
    }

    @Override
    public Server loadInBackground() {
        HashMap<String, Object> response;
        SteamSocket.setTimeout(3000);
        SourceServer sourceServer;
        try {
            sourceServer = new SourceServer(mServer.getIpAddr());
            sourceServer.initialize();
            response = sourceServer.getServerInfo();

            return convertToModel(response);
        } catch (SteamCondenserException | TimeoutException e) {
            e.printStackTrace();
            mServer.setName(mContext.getResources().getString(R.string.err_get_server));

            return mServer;
        }
    }

    private Server convertToModel(HashMap<String, Object> response) {
        mServer.setName(response.get("serverName").toString());
        mServer.setMap(response.get("mapName").toString());
        mServer.setNumPlayers(response.get("numberOfPlayers").toString());
        mServer.setMaxPlayers(response.get("maxPlayers").toString());
        mServer.setGame(response.get("gameDescription").toString());
        mServer.setTags(response.get("serverTags").toString());
        return mServer;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}