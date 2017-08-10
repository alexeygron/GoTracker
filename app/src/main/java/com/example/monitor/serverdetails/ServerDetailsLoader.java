package com.example.monitor.serverdetails;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.monitor.servers.Server;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import static com.example.monitor.utils.Helpers.makeLogTag;

/**
 * Receives details about this game server and players list from network in background thread
 */
class ServerDetailsLoader extends AsyncTaskLoader<ServerDetails> {

    public final String TAG = makeLogTag(ServerDetailsLoader.class);
    private String ip;

    ServerDetailsLoader(Context context, String ip) {
        super(context);
        this.ip = ip;
    }

    @Override
    public ServerDetails loadInBackground() {
        HashMap<String, Object> data;
        HashMap<String, SteamPlayer> players;
        try {
            SourceServer server = new SourceServer(ip);
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

    private ServerDetails convertToModel(HashMap data, HashMap<String, SteamPlayer> players) {
        Server serverData = new Server();
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
        ServerDetails model = new ServerDetails(serverData, playersList);
        return model;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
