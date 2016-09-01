package com.example.monitor.gameinfo;

public class GameInfo {

    private String mOnlinePlayers;
    private String mOnlineServers;
    private String mSearchingPlayers;
    private String mSearchSeconds;
    private String mScheduler;
    private String mSessionsLogon;
    private String mSteamCommunity;
    private String mIEconItems;
    private String mLeaderboards;
    private String mVersion;
    private String mTimeStamp;
    private String mTime;

    public GameInfo(String mIEconItems, String mLeaderboards, String mOnlinePlayers,
                    String mOnlineServers, String mScheduler, String mSearchingPlayers,
                    String mSearchSeconds, String mSessionsLogon, String mSteamCommunity,
                    String mTime, String mTimeStamp, String mVersion) {
        this.mIEconItems = mIEconItems;
        this.mLeaderboards = mLeaderboards;
        this.mOnlinePlayers = mOnlinePlayers;
        this.mOnlineServers = mOnlineServers;
        this.mScheduler = mScheduler;
        this.mSearchingPlayers = mSearchingPlayers;
        this.mSearchSeconds = mSearchSeconds;
        this.mSessionsLogon = mSessionsLogon;
        this.mSteamCommunity = mSteamCommunity;
        this.mTime = mTime;
        this.mTimeStamp = mTimeStamp;
        this.mVersion = mVersion;
    }

}
