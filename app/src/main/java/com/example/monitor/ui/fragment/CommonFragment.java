package com.example.monitor.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lotr.steammonitor.app.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Содержит общую функциональность для всех остальных фрагментов в приложении.
 */
public abstract class CommonFragment extends Fragment {

    public AVLoadingIndicatorView mProgressBar;
    @BindView(R.id.refresh) protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(getLayoutId(), container, false);
        mProgressBar = (AVLoadingIndicatorView)getActivity().findViewById(R.id.avloadingIndicatorView);
        mProgressBar.setVisibility(View.GONE);
        return rootView;
    }

    protected abstract int getLayoutId();
}
