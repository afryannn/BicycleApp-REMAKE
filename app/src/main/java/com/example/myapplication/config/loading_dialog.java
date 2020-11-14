package com.example.myapplication.config;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import com.example.myapplication.R;

public class loading_dialog {
    private Activity activity;
    private AlertDialog dialog;

    public loading_dialog(Activity myActivity){
        activity = myActivity;
    }

     public void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
