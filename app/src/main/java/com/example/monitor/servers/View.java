package com.example.monitor.servers;

import java.util.ArrayList;
import java.util.List;

public interface View {

    void updateList();

    void setData(List<ServerModel> data);

    void showSnackBar(String message);

    void showProgress();

    void hideProgress();
}
