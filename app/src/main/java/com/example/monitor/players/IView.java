package com.example.monitor.players;

import java.util.List;

/**
 * Created by lotr on 08.08.2016.
 */
public interface IView {

    void updateList();

    void showProgress();

    void hideProgress();

    void setData(List<PlayerModel> data);

    void showList();

    void hideList();
}
