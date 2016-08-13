package com.example.monitor.players;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.example.monitor.ui.fragment.CommonListFragment;
import com.example.monitor.ui.view.AddItemDialog;
import com.example.monitor.utils.LogUtils;
import com.lotr.steammonitor.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayersFragment extends CommonListFragment implements
        IView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener,
        AddItemDialog.Callback {

    @BindView(R.id.button_add)
    protected FloatingActionButton mButtonAdd;
    private FavoritePlayersPresenter mPresenter;
    private ListPlayersAdapter mAdapter;

    private static final String TAG = LogUtils.makeLogTag(PlayersFragment.class);

    @Override
    public void onViewCreated(android.view.View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mButtonAdd.setOnClickListener(this);
        setHasOptionsMenu(true);

        if (mPresenter == null) {
            mPresenter = new FavoritePlayersPresenter(getActivity(), getLoaderManager());
        }

        mAdapter = new ListPlayersAdapter(mPresenter);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_servers;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.button_add:
                AddItemDialog dialogFragment = AddItemDialog.createInstance(this);
                dialogFragment.show(getFragmentManager(), null);
                break;
        }
    }

    @Override
    public void onPositiveClick(String item) {
        mPresenter.addNewItem(item);
    }
}
