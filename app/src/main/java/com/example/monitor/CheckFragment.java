package com.lotr.steammonitor.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Класс фрагмента, который будет выводить сообщения в случае отсутствия записей в БД
 */

public class CheckFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    // переопределенный метод в котором заполняется макет представления фрагмента
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_check, parent, false);
        return v;
    }

}
