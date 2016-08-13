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

import com.example.monitor.utils.LogUtils;
import com.lotr.steammonitor.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Показывает диалог для добавления нового элемента в список серверов или игроков.
 */
public class AddItemDialog extends DialogFragment implements DialogInterface.OnShowListener{

    @BindView(R.id.add_item_field) EditText mField;
    @BindView(R.id.error_message) TextView mErrorMessage;
    private Callback mCallback;
    private AlertDialog dialog;

    private static final String TAG = LogUtils.makeLogTag(AddItemDialog.class);

    public static AddItemDialog createInstance(Callback callback){
        AddItemDialog dialog = new AddItemDialog();
        dialog.setCallback(callback);
        return dialog;
    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_item, null);
        ButterKnife.bind(this, view);

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
       //return value.contains(":");
        return true;
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

    public interface Callback {
        void onPositiveClick(String item);
    }
}
