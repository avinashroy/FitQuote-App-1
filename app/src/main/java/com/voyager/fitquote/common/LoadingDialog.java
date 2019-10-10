package com.voyager.fitquote.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import com.voyager.fitquote.R;

public class LoadingDialog {
    public static Dialog show(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.loading_spinner);
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static void hide(Dialog dialog) {
        dialog.dismiss();
    }
}
