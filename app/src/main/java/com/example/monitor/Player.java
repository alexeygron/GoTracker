package com.example.monitor;

import android.graphics.Bitmap;

/**
 * Created by lotr on 17.08.2016.
 */
public class Player {

    private String plId;
    private String mPlayerName, urlAvatar, gameextrainfo, personastate = "", lastlogoff;
    Bitmap avs;

    Player (String id) {

        plId = id;
    }

    String getPlayerId(){

        return plId;
    }

    void setPlayerId(String id){

        plId = id;
    }

    void setPlayerName (String name){

        mPlayerName = name;

    }

    String getPlayerName(){

        return mPlayerName;
    }

    void setUrlAvatar (String url){

        urlAvatar = url;

    }

    String getUrlAvatar(){

        return urlAvatar;
    }

    void setgameextrainfo (String info){

        gameextrainfo = info;

    }

    String getgameextrainfo(){

        return gameextrainfo;
    }

    void setBtmAvs(Bitmap btm){

        avs = btm;
    }

    Bitmap getBtmAvs(){

        return avs;
    }

    void setpersonastate (String state){

        personastate = state;

    }

    String getpersonastate(){

        return personastate;
    }

    void setlastlogoff (String time){

        lastlogoff = time;

    }

    String getlastlogoff(){

        return lastlogoff;
    }
}