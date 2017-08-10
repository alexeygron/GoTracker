package com.example.monitor.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lotr.steammonitor.app.R;

import butterknife.ButterKnife;

/**
 * In process
 */
public class HelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        ButterKnife.bind(this);
    }
}

