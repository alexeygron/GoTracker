package com.example.monitor.gameinfo;

import com.example.monitor.servers.ServerModel;

import java.util.List;

public interface IView {

    void showProgress();

    void hideProgress();

    void showView();

    void hideView();

    void updateFields(String servers, String players, String searching,
                 String searchSeconds, String scheduler, String logon,
                 String community, String items, String leaderboards,
                 String version, String timestamp, String time);
}
