package com.example.monitor.servers;

import java.util.ArrayList;
import java.util.List;

public interface IView {

    void updateList();

    void setData(List<ServerModel> data);

    void showSnackBar(String message);

    void showProgress();

    void hideProgress();

    void showList();

    void hideList();
}
