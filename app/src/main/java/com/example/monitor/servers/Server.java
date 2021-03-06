package com.example.monitor.servers;

import android.os.Parcel;
import android.os.Parcelable;

public class Server implements Parcelable {

    private String ipAddr;
    private String mName;
    private String numPlayers;
    private String maxPlayers;
    private String mMap;
    private String mGame;
    private String mTags;
    private String mDbLabel;

    public Server() {

    }

    public Server(String ip) {
        ipAddr = ip;
    }

    protected Server(Parcel in) {
        ipAddr = in.readString();
        mName = in.readString();
        numPlayers = in.readString();
        maxPlayers = in.readString();
        mMap = in.readString();
        mGame = in.readString();
        mTags = in.readString();
        mDbLabel = in.readString();
    }

    public static final Creator<Server> CREATOR = new Creator<Server>() {
        @Override
        public Server createFromParcel(Parcel in) {
            return new Server(in);
        }

        @Override
        public Server[] newArray(int size) {
            return new Server[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ipAddr);
        dest.writeString(mName);
        dest.writeString(numPlayers);
        dest.writeString(maxPlayers);
        dest.writeString(mMap);
        dest.writeString(mGame);
        dest.writeString(mTags);
        dest.writeString(mDbLabel);

    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ip) {
        ipAddr = ip;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getmName() {
        return mName;
    }

    public String getGame() {
        return mGame;
    }

    public void setGame(String game) {
        mGame = game;
    }

    public String getTags() {
        return mTags;
    }

    public void setTags(String tags) {
        mTags = tags;
    }

    public void setMaxPlayers(String max) {
        maxPlayers = max;
    }

    public String getPlayers() {
        if (numPlayers != null) {
            return numPlayers + "/" + maxPlayers;
        } else {
            return "";
        }
    }

    public String getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(String num) {
        numPlayers = num;
    }

    public String getMap() {
        return mMap;
    }

    public void setMap(String mapN) {
        mMap = mapN;
    }

    public String getDbId() {
        return mDbLabel;
    }

    public void setDbId(String id) {
        mDbLabel = id;
    }

    @Override
    public String toString() {
        return "Server{" +
                "ipAddr='" + ipAddr + '\'' +
                ", mName='" + mName + '\'' +
                ", numPlayers='" + numPlayers + '\'' +
                ", maxPlayers='" + maxPlayers + '\'' +
                ", mMap='" + mMap + '\'' +
                ", mGame='" + mGame + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mDbLabel='" + mDbLabel + '\'' +
                '}';
    }
}
