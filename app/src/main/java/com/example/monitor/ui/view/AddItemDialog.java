package com.example.monitor.ui.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.monitor.db.ServersDao;
import com.lotr.steammonitor.app.R;

/**
 * Показывает диалог для добавления нового элемента в список серверов или игроков.
 */
public class AddItemDialog extends DialogFragment implements DialogInterface.OnShowListener{

    private EditText mField;
    private TextView mErrorMessage;
    private DialogCallback mCallback;
    private AlertDialog dialog;

    private static final String TAG = "add_item_dialog";

    public static AddItemDialog createInstance(DialogCallback callback){
        AddItemDialog dialog = new AddItemDialog();
        dialog.setCallback(callback);
        return dialog;
    }

    public void setCallback(DialogCallback callback){
        mCallback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_item, null);
        mField = (EditText) view.findViewById(R.id.add_item_field);
        mErrorMessage = (TextView) view.findViewById(R.id.error_message);

        dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_server_title).setView(view)
                .setPositiveButton("Добавить", null)
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddItemDialog.this.getDialog().cancel();
                    }
                }).create();

        dialog.setOnShowListener(this);
        return dialog;
    }

    private boolean isFormat(String value){
       return value.contains(":");
    }

    private void showError(){
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShow(DialogInterface iDialog) {
        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = mField.getText().toString();
                if(isFormat(item)){
                    mCallback.onPositiveClick(mField.getText().toString());
                    dialog.dismiss();
                } else {
                    showError();
                }
            }
        });
    }

    public interface DialogCallback{

        void onPositiveClick(String item);
    }
}
