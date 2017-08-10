package com.example.monitor.servers;

import java.util.List;

public interface IView {

    void updateList();

    void setData(List<Server> data);

    void showProgress(boolean state);

    void showList(boolean state);
}
