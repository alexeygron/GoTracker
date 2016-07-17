package com.example.monitor.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Содержит общую функциональность для всех остальных фрагментов в приложении.
 */
public abstract class CommonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(getLayoutId(), container, false);
        return rootView;
    }

    protected abstract int getLayoutId();
}
