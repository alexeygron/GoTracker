package com.example.monitor.serverdetails;

import android.os.Bundle;

import com.example.monitor.ui.fragment.CommonFragment;
import com.lotr.steammonitor.app.R;

/**
 * Created by lotr on 15.07.2016.
 */
public class ServerDetailsFragment extends CommonFragment {

    @Override
    public void onViewCreated(android.view.View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_details_servers;
    }
}
