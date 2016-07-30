package com.example.monitor.serverdetails;

import com.example.monitor.servers.ServerModel;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;

import java.util.ArrayList;

public class ServerDetailsModel {

    private ServerModel mServer;
    private ArrayList<SteamPlayer> mPlayersList;

    public ServerDetailsModel(ServerModel serverData, ArrayList<SteamPlayer> playersList){
        mServer = serverData;
        mPlayersList = playersList;
    }

    public ArrayList<SteamPlayer> getPlayersList() {
        return mPlayersList;
    }

    public ServerModel getServer() {
        return mServer;
    }

    @Override
    public String toString(){
        return mServer.toString() + " " + mPlayersList.toString();
    }
}
