package com.example.monitor.gameinfo;

public interface IView {

    void showProgress(boolean state);

    void showView(boolean state);

    void updateFields(String servers, String players, String searching,
                 String searchSeconds, String scheduler, String logon,
                 String community, String items, String leaderboards,
                 String version, String timestamp, String time);
}
