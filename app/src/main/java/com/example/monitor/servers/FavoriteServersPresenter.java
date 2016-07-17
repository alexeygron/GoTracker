package com.example.monitor.servers;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.monitor.db.ServersDao;
import com.example.monitor.serverdetails.ServerDetailsActivity;
import com.example.monitor.serverdetails.ServerDetailsFragment;
import com.lotr.steammonitor.app.R;

import java.util.ArrayList;

/**
 * Created by lotr on 06.07.2016.
 */
public class FavoriteServersPresenter implements LoaderManager.LoaderCallbacks<Server> {

    private static final String TAG = "list_servers_presenter";
    public static final int LOADER_ID = 1;

    private View mView;
    private Context mContext;
    private LoaderManager mLoaderManager;
    private FragmentManager mFragmentManager;
    private ServersDao mDB;
    private ArrayList<Server> mListData;

    private int loaderTaskIndex;

    public FavoriteServersPresenter(LoaderManager lm, Context context, View view, FragmentManager fm){
        mFragmentManager = fm;
        mView = view;
        mContext = context;
        mLoaderManager = lm;
        mDB = new ServersDao(context);
        mListData = new ArrayList<>();
        showList();
    }

    private void updateList(Server server){
        mListData.set(loaderTaskIndex, server);
        mView.setData(mListData);
        mView.updateList();
    }

    private void showList(){
        mView.showProgress();
        mListData.clear();
        mListData.addAll(mDB.readToList());
        loaderTaskIndex = 0;
        mLoaderManager.restartLoader(LOADER_ID, null, this);
        Log.i(TAG, "list size " + mListData.size());
    }

    void onClickDelButton(int position){
        Log.i(TAG, "list size " + mListData.size());
        String id = mListData.get(position).getDbId();
        mDB.delete(id);
        mListData.remove(position);
        mView.setData(mListData);
        Log.i(TAG, "del button click! " + position);
    }

    void addNewItem(String value){
        mDB.insert(value);
        showList();
    }

    void onClickListItem(){
        Log.i(TAG, "list item click!");
        //ServerDetailsFragment fragment = new ServerDetailsFragment();
        //mFragmentManager.beginTransaction().add(R.id.main_container, fragment, "DETAILS").addToBackStack(null).commit();
        Intent i = new Intent(mContext, ServerDetailsActivity.class);
        mContext.startActivity(i);
    }

    public void onTakeView(View view) {
        this.mView = view;
    }

    @Override
    public Loader<Server> onCreateLoader(int id, Bundle args) {
        if (LOADER_ID == id) {
            if(mListData.size() > 0)
                return new ServersLoader(mContext, mListData.get(loaderTaskIndex));
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Server> loader, Server data) {
        if (data != null){
            updateList(data);
            loaderTaskIndex++;
            Log.i(TAG, "loader result " + data.toString());

            if (loaderTaskIndex < mListData.size()) {
                mLoaderManager.restartLoader(LOADER_ID, null, this);
            } else {
                mView.hideProgress();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Server> loader) {
    }

    public void onRefresh() {
        mLoaderManager.destroyLoader(LOADER_ID);
        showList();
    }

    void onDestroy(){
        mDB.close();
        mView = null;
    }

    void init(){
        mDB.insert("37.187.205.242:27015");
        mDB.insert("86.104.11.123:27015");
        mDB.insert("46.174.53.181:27015");
        mDB.insert("192.95.29.182:27017");
        mDB.insert("185.113.141.19:27103");
        mDB.insert("46.174.50.142:55555");
       // showList();
    }
}
