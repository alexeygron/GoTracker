package com.example.monitor.async;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.monitor.utils.Helpers;

import static com.example.monitor.utils.Helpers.makeLogTag;

public abstract class BaseLoader extends AsyncTaskLoader {

    private static String TAG = makeLogTag(BaseLoader.class);

    public BaseLoader(Context context, String childTag) {
        super(context);
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }
}
