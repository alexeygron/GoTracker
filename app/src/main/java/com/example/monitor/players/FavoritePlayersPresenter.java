package com.example.monitor.players;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.monitor.db.PlayersDao;
import com.example.monitor.utils.NetworkReceiver;

import java.util.ArrayList;
import java.util.List;

import static com.example.monitor.utils.Helpers.makeLogTag;

class FavoritePlayersPresenter implements LoaderManager.LoaderCallbacks<Player>, NetworkReceiver.NetChangeListener {

    private Context mContext;
    private IView mView;
    private LoaderManager mLoaderManager;
    private PlayersDao dao;
    private List<Player> mListData;
    private int loaderTaskIndex;

    private static final int LOADER_ID = 1;
    private static final String TAG = makeLogTag(FavoritePlayersPresenter.class);

    FavoritePlayersPresenter(Context context, LoaderManager lm, IView view) {
        mLoaderManager = lm;
        mContext = context;
        mView = view;
        dao = new PlayersDao(context);
        mListData = new ArrayList<>();
    }

    public void onNetworkEnabled() {
        showList();
        mView.showList(true);
    }

    public void onNetworkDiasbled() {
        mView.showList(false);
    }

    private void showList() {
        mListData.clear();
        mListData.addAll(dao.get());
        loaderTaskIndex = 0;
        if (mLoaderManager.getLoader(LOADER_ID) != null) {
            mLoaderManager.restartLoader(LOADER_ID, null, this);
        } else {
            mLoaderManager.initLoader(LOADER_ID, null, this);
        }
    }

    private void updateList(Player player) {
        mListData.set(loaderTaskIndex, player);
        mView.setData(mListData);
        mView.updateList();
    }

    void onRefresh() {
        mLoaderManager.destroyLoader(LOADER_ID);
        showList();
    }

    void onClickDelButton(int position) {
        String label = mListData.get(position).getDbLabel();
        dao.delete(label);
        mListData.remove(position);
        mView.setData(mListData);
    }

    void onDestroy() {
        mLoaderManager.destroyLoader(LOADER_ID);
        mView = null;
    }

    /**
     * Validate and save new player ID in DB
     */
    void addPlayer(String steamId) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String id = Player.convertSteamIdToCommunityId(params[0]);
                dao.insert(id);
                return null;
            }

            @Override
            protected void onPostExecute(String id) {
                super.onPostExecute(id);
                onRefresh();
            }
        }.execute(steamId);
    }

    @Override
    public Loader<Player> onCreateLoader(int id, Bundle args) {
        if (mListData.size() > 0 & loaderTaskIndex < mListData.size()) {
            mView.showProgress(true);
            return new PlayersDataLoader(mContext, mListData.get(loaderTaskIndex));
        } else {
            mView.showProgress(false);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Player> loader, Player data) {
        updateList(data);
        loaderTaskIndex++;
        mLoaderManager.restartLoader(1, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Player> loader) {

    }
}
