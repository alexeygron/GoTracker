package com.example.monitor.players;

import android.content.res.Resources;

import com.example.monitor.utils.Config;
import com.example.monitor.utils.ConvertUtils;
import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import com.github.koraktor.steamcondenser.steam.community.WebApi;
import com.lotr.steammonitor.app.R;

public class PlayerModel {

    private String mSteamID;
    private int mPersonaState;
    private String mPersonName;
    private String mRealName;
    private String mProfileUrl;
    private String mAvatarUrl;
    private String mGameExtraInfo;
    private String mDbLabel;
    private long mLastLogoff;

    public PlayerModel() {
        mPersonaState = 7;
    }

    public PlayerModel(String steamID, int personaState, String personName,
                       long lastLogoff, String profileUrl, String avatar, String realName) {
        this.mAvatarUrl = avatar;
        this.mPersonaState = personaState;
        this.mPersonName = personName;
        this.mLastLogoff = lastLogoff;
        this.mProfileUrl = profileUrl;
        this.mRealName = realName;
        this.mSteamID = steamID;
    }

    /**
     * Проверяет является ли URL 64bit SteamID, если нет, то конвертирует в него
     */
    public static String checkSteamId(String sourceId) {
        Long convertedId = null;
        try {
            WebApi.setApiKey(Config.WEB_API_KEY);
            convertedId = SteamId.resolveVanityUrl(sourceId);
        } catch (WebApiException e) {
            e.printStackTrace();
        }

        if (convertedId != null)
            return String.valueOf(convertedId);
        else
            return sourceId;
    }

    /**
     * Формирует статус игрока в Steam Community
     */
    public String getStatus(Resources res) {
        // Если игрок находится в игре, возвращаем статус с названием игры
        if (mGameExtraInfo != null) {
            return res.getString(R.string.playing) + " " + mGameExtraInfo;
        } else {
            // Если не в игре, перебираем варианты статуса community и возвращаем его
            switch (mPersonaState) {
                case 0:
                    return res.getString(R.string.last_online_status) + " " + ConvertUtils.secondToDate(mLastLogoff);
                case 1:
                    return res.getString(R.string.online_status);
                case 2:
                    return res.getString(R.string.busy_status);
                case 3:
                    return res.getString(R.string.away_status);
                case 4:
                    return res.getString(R.string.snooze_status);
                case 5:
                    return res.getString(R.string.looking_to_trade_status);
                case 6:
                    return res.getString(R.string.looking_to_play_status);
            }
        }
        return "";
    }

    public int getStatusColor() {
        if (mGameExtraInfo != null) {
            return R.color.status_playing;
        } else {
            switch (mPersonaState) {
                case 0:
                    return R.color.status_offline;
                default:
                    return R.color.status_online;
            }
        }
    }

    public String getSteamID() {
        return mSteamID;
    }

    public void setSteamID(String steamID) {
        this.mSteamID = steamID;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String mAvatar) {
        this.mAvatarUrl = mAvatar;
    }

    public int getPersonaState() {
        return mPersonaState;
    }

    public void setPersonaState(int personaState) {
        this.mPersonaState = personaState;
    }

    public String getGameExtraInfo() {
        return mGameExtraInfo;
    }

    public void setmGameExtraInfo(String mGameExtraInfo) {
        this.mGameExtraInfo = mGameExtraInfo;
    }

    public String getPersonName() {
        return mPersonName;
    }

    public void setPersonName(String personName) {
        this.mPersonName = personName;
    }

    public String getProfileUrl() {
        return mProfileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.mProfileUrl = profileUrl;
    }

    public String getRealName() {
        return mRealName;
    }

    public void setRealName(String realName) {
        this.mRealName = realName;
    }

    public String getDbLabel() {
        return mDbLabel;
    }

    public void setDbLabel(String dbLabel) {
        this.mDbLabel = dbLabel;
    }

    public void setLastLogoff(long lastLogoff) {
        this.mLastLogoff = lastLogoff;
    }

    @Override
    public String toString() {
        return "Player{" +
                "mAvatar='" + mAvatarUrl + '\'' +
                ", mSteamID='" + mSteamID + '\'' +
                ", mPersonaState='" + mPersonaState + '\'' +
                ", mPersonName='" + mPersonName + '\'' +
                ", mRealName='" + mRealName + '\'' +
                ", mProfileUrl='" + mProfileUrl + '\'' +
                ", mGameExtraInfo='" + mGameExtraInfo + '\'' +
                ", mDbLabel='" + mDbLabel + '\'' +
                '}';
    }
}

