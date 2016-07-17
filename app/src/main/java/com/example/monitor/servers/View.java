package com.example.monitor.servers;

import java.util.ArrayList;

public interface View {

    void showList(ArrayList<ServerModel> data);

    void showSnackBar(String message);

    void showProgress();

    void hideProgress();

    void onPositiveClick();


}
