package com.example.monitor.serverdetails;

import com.github.koraktor.steamcondenser.steam.SteamPlayer;

import java.util.ArrayList;

public interface IView {

    void updateFields(String map, String numPlayers, String name, String game, String tags);

    void setAdapterData(ArrayList<SteamPlayer> data);
}
