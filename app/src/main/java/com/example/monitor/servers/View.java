package com.example.monitor.servers;

import java.util.ArrayList;

public interface View {

    void updateList();

    void setData(ArrayList<Server> data);

    void showSnackBar(String message);

    void showProgress();

    void hideProgress();
}
