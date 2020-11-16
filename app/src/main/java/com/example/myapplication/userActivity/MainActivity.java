package com.example.myapplication.userActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.androidnetworking.interfaces.JSONObjectRequestListener;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;

import com.example.myapplication.config.Config;
import com.example.myapplication.R;
import com.example.myapplication.config.RS;
import com.example.myapplication.adminActivity.WhereAdmin;
import com.example.myapplication.config.SessionManager;
import com.example.myapplication.config.loading_dialog;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private EditText logEmail,logPassword;
    private Button logBtnLog;
    private boolean isForm = false;
    private Activity activity;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        sessionManager = new SessionManager(getApplicationContext());
        if(sessionManager.getLogin()){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        final loading_dialog loadingdialog = new loading_dialog(MainActivity.this);
        logEmail = findViewById(R.id.logEmail);
        logPassword = findViewById(R.id.logPassword);
        logBtnLog = findViewById(R.id.logBtnLog);
        logBtnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingdialog.startLoading();
                isForm = true;
                final String email = logEmail.getText().toString();
                final String password = logPassword.getText().toString();
                if (isForm) {
                HashMap<String, String> body = new HashMap<>();
                body.put("email", email);
                body.put("password", password);
                    AndroidNetworking.post(Config.BASE_URL + "login")
                            .addBodyParameter(body)
                            .setPriority(Priority.MEDIUM)
                            .setOkHttpClient(((RS) getApplication()).getOkHttpClient())
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                                    if (message.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                                        JSONObject payload = response.optJSONObject("data");
                                        String role = payload.optString("role_user");
                                        int userid = payload.optInt("id");
                                        Log.e("AF", "id: "+userid);
                                        sessionManager.setLogin(true);
                                        sessionManager.setUsername(email);
                                         String idd = Integer.toString(userid);
                                        sessionManager.setIdu(idd);
                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        if (role.equalsIgnoreCase("2"))
                                           intent = new Intent(MainActivity.this, WhereAdmin.class);
                                        startActivity(intent);
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                        finish();
                                        finishAffinity();
                                        loadingdialog.dismissDialog();
                                    }else {
                                        loadingdialog.dismissDialog();
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                    }

                                @Override
                                public void onError(ANError anError) {
                                    loadingdialog.dismissDialog();
                                    Toast.makeText(MainActivity.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
                                    Log.d("HBB", "onError: " + anError.getErrorBody());
                                    Log.d("HBB", "onError: " + anError.getLocalizedMessage());
                                    Log.d("HBB", "onError: " + anError.getErrorDetail());
                                    Log.d("HBB", "onError: " + anError.getResponse());
                                    Log.d("HBB", "onError: " + anError.getErrorCode());
                                }
                            });
                }
            }

        });
    }
    public void register(View view){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }
}