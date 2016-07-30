package com.example.monitor.model;

import android.graphics.Bitmap;

public class Player {

    Bitmap avs;
    private String plId;
    private String mPlayerName, urlAvatar, gameextrainfo, personastate = "", lastlogoff;

    public Player(String id) {

        plId = id;
    }

    public String getPlayerId() {
        return plId;
    }

    public void setPlayerId(String id) {
        int c;
        plId = id;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public void setPlayerName(String name) {
        mPlayerName = name;

    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String url) {
        urlAvatar = url;
    }

    public void setgameextrainfo(String info) {
        gameextrainfo = info;
    }

    public String getgameextrainfo() {
        return gameextrainfo;
    }

    public Bitmap getBtmAvs() {
        return avs;
    }

    public void setBtmAvs(Bitmap btm) {
        avs = btm;
    }

    public void setpersonastate(String state) {
        personastate = state;

    }

    public String getpersonastate() {
        return personastate;
    }

    public void setlastlogoff(String time) {
        lastlogoff = time;

    }

    public String getlastlogoff() {
        return lastlogoff;
    }
}

