package com.example.monitor.servers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;

import com.example.monitor.db.ServersDao;
import com.example.monitor.ui.fragment.CommonListFragment;
import com.example.monitor.ui.view.AddItemDialog;
import com.lotr.steammonitor.app.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class ServersFragment extends CommonListFragment implements View, SwipeRefreshLayout.OnRefreshListener,
        android.view.View.OnClickListener, AddItemDialog.DialogCallback {

    private static final String TAG = "server_list_fragment";

    //private AVLoadingIndicatorView mProgressBar;
    protected FloatingActionButton mButtonAdd;
    private FavoriteServersPresenter mPresenter;
    //PullToRefreshView mPullToRefreshView;

    @Override
    public void onViewCreated(android.view.View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //mProgressBar = (AVLoadingIndicatorView) getActivity().findViewById(R.id.avloadingIndicatorView);
        mButtonAdd = (FloatingActionButton) view.findViewById(R.id.button_add);
        mButtonAdd.setOnClickListener(this);
        setHasOptionsMenu(true);

        if (mPresenter == null){
            mPresenter = new FavoriteServersPresenter(getLoaderManager(), getActivity(), this, getFragmentManager());
        }
        //mPresenter.onTakeView(this);
        mAdapter = new ListServersAdapter(mPresenter);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_button:
                mPresenter.init();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void updateList(){
        mAdapter.notifyDataSetChanged();
    }

  @Override
    public void setData(ArrayList<Server> data){
       mAdapter.setData(data);
    }

    @Override
    public void showSnackBar(String message){
       Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(android.view.View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (mProgressBar != null)
            mProgressBar.setVisibility(android.view.View.GONE);
    }

    protected int getLayoutId(){
        return R.layout.fragment_servers;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.onRefresh();
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 700);
    }

    @Override
    public void onClick(android.view.View v) {
        switch(v.getId()){
            case R.id.button_add:
                AddItemDialog dialogFragment = AddItemDialog.createInstance(this);
                dialogFragment.show(getFragmentManager(), null);
                break;
        }
    }

    @Override
    public void onPositiveClick(String item) {
        Log.i(TAG, item);
        mPresenter.addNewItem(item);
    }
}
