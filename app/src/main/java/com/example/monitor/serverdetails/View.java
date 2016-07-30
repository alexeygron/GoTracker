package com.example.monitor.serverdetails;

import com.example.monitor.servers.ServerModel;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;

import java.util.ArrayList;

/**
 * Created by lotr on 20.07.2016.
 */
public interface View {

    void updateFields(String mapName, String numPlayers);

    void setAdapterData(ArrayList<SteamPlayer> data);
}
