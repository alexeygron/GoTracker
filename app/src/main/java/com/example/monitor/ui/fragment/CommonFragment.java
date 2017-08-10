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

public abstract class CommonFragment extends Fragment {

    @BindView(R.id.refresh) protected SwipeRefreshLayout mSwipeRefreshLayout;
    public AVLoadingIndicatorView mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(getLayoutId(), container, false);
        mProgressBar = (AVLoadingIndicatorView)getActivity().findViewById(R.id.avloadingIndicatorView);
        mProgressBar.setVisibility(View.GONE);
        return rootView;
    }

    public void showProgress(boolean state) {
        mProgressBar.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    protected abstract int getLayoutId();
}
