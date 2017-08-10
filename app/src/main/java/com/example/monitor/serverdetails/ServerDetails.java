package com.example.monitor.serverdetails;

import com.example.monitor.servers.Server;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;

import java.util.ArrayList;

class ServerDetails {

    private Server mServer;
    private ArrayList<SteamPlayer> mPlayersList;

    ServerDetails(Server serverData, ArrayList<SteamPlayer> playersList){
        mServer = serverData;
        mPlayersList = playersList;
    }

    ArrayList<SteamPlayer> getPlayersList() {
        return mPlayersList;
    }

    public Server getServer() {
        return mServer;
    }
}
