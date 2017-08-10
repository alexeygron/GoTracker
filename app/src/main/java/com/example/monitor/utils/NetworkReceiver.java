package com.example.monitor.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NetworkReceiver extends BroadcastReceiver {

    private List<NetChangeListener> mListeners;
    private boolean mNetState;

    private static final String TAG = Helpers.makeLogTag(NetworkReceiver.class);

    public NetworkReceiver() {
        this.mListeners = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive list size " + mListeners.size());
        mNetState = Helpers.isNetworkConnected(context);
        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for (NetChangeListener listener : mListeners) {
            notifyState(listener);
        }
    }

    public void addListener(NetChangeListener listener) {
        this.mListeners.add(listener);
    }

    private void notifyState(NetChangeListener listener) {
        if (mNetState) {
            listener.onNetworkEnabled();
            //Log.i(TAG, "notify - Enabled for: " + listener.getClass().getSimpleName());
        } else {
            listener.onNetworkDiasbled();
        }
    }

    public interface NetChangeListener {
        void onNetworkEnabled();
        void onNetworkDiasbled();
    }
}

