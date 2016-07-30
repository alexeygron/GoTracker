package com.example.monitor.async;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.monitor.utils.LogUtils;

public abstract class BaseLoader extends AsyncTaskLoader {

    private static String TAG = LogUtils.makeLogTag(BaseLoader.class);

    public BaseLoader(Context context, String childTag) {
        super(context);
        TAG = childTag + "Loader";
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
        Log.d(TAG, "forceLoad");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(TAG, "onStopLoading");
    }

}
