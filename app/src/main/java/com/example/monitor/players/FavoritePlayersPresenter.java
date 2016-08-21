package com.example.monitor.players;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.monitor.db.PlayersDao;
import com.example.monitor.utils.Helpers;
import com.example.monitor.utils.NetworkReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lotr on 08.08.2016.
 */
public class FavoritePlayersPresenter implements LoaderManager.LoaderCallbacks<PlayerModel>, NetworkReceiver.NetChangeListener {

    public static final int LOADER_ID = 1;
    private Context mContext;
    private IView mView;
    private LoaderManager mLoaderManager;
    private PlayersDao mDB;
    private List<PlayerModel> mListData;
    private int loaderTaskIndex;

    private static final String TAG = Helpers.makeLogTag(FavoritePlayersPresenter.class);

    public FavoritePlayersPresenter(Context context, LoaderManager lm, IView view) {
        mLoaderManager = lm;
        mContext = context;
        mView = view;
        mDB = new PlayersDao(context);
        mListData = new ArrayList<>();
        //showList();
        //mDB.clear();
    }

    public void networkEnabled() {
        Log.i(TAG, "networkEnabled");
        showList();
        mView.showList();
    }

    public void networkDiasbled() {
        Log.i(TAG, "networkDiasbled");
        mView.hideList();
    }


    private void showList() {
        mListData.clear();
        mListData.addAll(mDB.get());
        Log.i(TAG, "list size " + mListData.size());
        loaderTaskIndex = 0;
        if(mLoaderManager.getLoader(LOADER_ID) != null) {
            mLoaderManager.restartLoader(LOADER_ID, null, this);
        } else {
            mLoaderManager.initLoader(LOADER_ID, null, this);
        }
    }

    private void updateList(PlayerModel player) {
        mListData.set(loaderTaskIndex, player);
        mView.setData(mListData);
        mView.updateList();
    }

    public void onRefresh(){
        mLoaderManager.destroyLoader(LOADER_ID);
        showList();
    }

    void onClickDelButton(int position) {
        Log.i(TAG, "list size " + mListData.size());
        String label = mListData.get(position).getDbLabel();
        mDB.delete(label);
        mListData.remove(position);
        mView.setData(mListData);
        Log.i(TAG, "del button click! " + position);
    }

    void onDestroy() {
        mLoaderManager.destroyLoader(LOADER_ID);
        mView = null;
    }

    /**
     * Вызывается при добавлении нового игрока в список. Проверяет корректность ID в AsyncTask
     * и записывает его в DB
     * @param steamId ID идентификатор игрока в Steam
     */
    void addPlayer(String steamId) {
        Log.i(TAG, "new item: " + steamId);
        // Делает сетевой запрос для проверки корректности введенного ID
        // И конвертирует его, если это нужно
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... idArr) {
                return PlayerModel.checkSteamId(idArr[0]);
            }

            @Override
            protected void onPostExecute(String id) {
                super.onPostExecute(id);
                Log.i(TAG, "Post Check id = " + id);
                mDB.insert(id);
                onRefresh();
            }
        }.execute(steamId);
    }

    @Override
    public Loader<PlayerModel> onCreateLoader(int id, Bundle args) {
        if (mListData.size() > 0 & loaderTaskIndex < mListData.size()) {
            mView.showProgress();
            return new PlayersDataLoader(mContext, mListData.get(loaderTaskIndex));
        } else {
            mView.hideProgress();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<PlayerModel> loader, PlayerModel data) {
        Log.i(TAG, "load: by index " + loaderTaskIndex + " " + data.toString());
            updateList(data);
            loaderTaskIndex++;
            mLoaderManager.restartLoader(1, null, this);
    }

    @Override
    public void onLoaderReset(Loader<PlayerModel> loader) {

    }
}
