package com.example.monitor.players;

import java.util.List;

public interface IView {

    void updateList();

    void showProgress(boolean state);

    void showList(boolean state);

    void setData(List<PlayerModel> data);
}
