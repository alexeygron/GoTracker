package com.example.monitor.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import static com.example.monitor.utils.Helpers.makeLogTag;

/**
 * {@link BroadcastReceiver} to listen network status changes and notification all subscribers
 */
public class NetworkReceiver extends BroadcastReceiver {

    // Subscribers list
    private List<NetChangeListener> mListeners;
    private boolean state;

    private static final String TAG = makeLogTag(NetworkReceiver.class);

    public NetworkReceiver() {
        this.mListeners = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        state = Helpers.isNetworkConnected(context);
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
        if (state) {
            listener.onNetworkEnabled();
        } else {
            listener.onNetworkDiasbled();
        }
    }

    public interface NetChangeListener {
        void onNetworkEnabled();
        void onNetworkDiasbled();
    }
}

