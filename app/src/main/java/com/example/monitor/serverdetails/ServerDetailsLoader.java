package com.example.monitor.serverdetails;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.monitor.servers.ServerModel;
import com.example.monitor.utils.Helpers;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * Загружает из сети в фоновом потоке информацию о сервере.
 */
public class ServerDetailsLoader extends AsyncTaskLoader<ServerDetailsModel> {

    public final String TAG = Helpers.makeLogTag(ServerDetailsLoader.class);
    private String ipAddress;

    public ServerDetailsLoader(Context context, String ip) {
        super(context);
        ipAddress = ip;
    }

    @Override
    public ServerDetailsModel loadInBackground() {
        HashMap<String, Object> data;
        HashMap<String, SteamPlayer> players;
        try {
            SourceServer server = new SourceServer(ipAddress);
            SteamSocket.setTimeout(3000);
            server.initialize();
            data = server.getServerInfo();
            players = server.getPlayers();
        } catch (SteamCondenserException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
        return convertToModel(data, players);
    }

    private ServerDetailsModel convertToModel(HashMap data, HashMap<String, SteamPlayer> players) {
        ServerModel serverData = new ServerModel();
        serverData.setMap(data.get("mapName").toString());
        serverData.setNumPlayers(data.get("numberOfPlayers").toString());
        serverData.setMaxPlayers(data.get("maxPlayers").toString());
        serverData.setName(data.get("serverName").toString());
        serverData.setGame(data.get("gameDescription").toString());
        serverData.setTags(data.get("serverTags").toString());

        ArrayList<SteamPlayer> playersList = new ArrayList<>();
        if (players != null) {
            for (HashMap.Entry<String, SteamPlayer> player : players.entrySet()) {
                playersList.add(player.getValue());
            }
            Collections.sort(playersList, new Comparator<SteamPlayer>() {
                public int compare(SteamPlayer o1, SteamPlayer o2) {
                    Integer value1 = o1.getScore();
                    Integer value2 = o2.getScore();

                    return value2.compareTo(value1);
                }
            });
        }
        ServerDetailsModel model = new ServerDetailsModel(serverData, playersList);
        return model;
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
    public void deliverResult(ServerDetailsModel data) {
        super.deliverResult(data);
        Log.d(TAG, "deliverResult");
    }
}
