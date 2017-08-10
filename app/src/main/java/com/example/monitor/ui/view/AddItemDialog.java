package com.example.monitor.ui.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.monitor.utils.Helpers;
import com.lotr.steammonitor.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.monitor.utils.Helpers.makeLogTag;

/**
 * Показывает диалог для добавления нового элемента в список серверов или игроков.
 */
public class AddItemDialog extends DialogFragment implements AlertDialog.OnShowListener{

    private static final String TAG = makeLogTag(AddItemDialog.class);
    @BindView(R.id.add_item_field) EditText mField;
    @BindView(R.id.helper_hint) TextView mHintMessage;
    @BindView(R.id.phone_input) TextInputLayout mInputLayout;
    private Callback mCallback;
    private AlertDialog dialog;
    private int mTitle;
    private int mHint;

    public static AddItemDialog createInstance(Callback callback, int title, int hint) {
        AddItemDialog dialog = new AddItemDialog();
        dialog.setCallback(callback);
        dialog.setTitle(title);
        dialog.setHint(hint);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_item, null);
        ButterKnife.bind(this, view);
        mHintMessage.setText(mHint);
        dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(mTitle)
                .setPositiveButton(R.string.dialog_add_button, null)
                .setNegativeButton(getResources().getString(R.string.dialog_cansel_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface iDialog) {
        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mField.getText().toString();

                if (!TextUtils.isEmpty(text)) {
                    mCallback.onPositiveClick(mField.getText().toString());
                    dialog.dismiss();
                } else {
                    mInputLayout.setError(getResources().getString(R.string.input_error));
                }
            }
        });
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setTitle(int title) {
        mTitle = title;
    }

    public void setHint(int hint) {
        mHint = hint;
    }

    public interface Callback {
        void onPositiveClick(String item);
    }
}
