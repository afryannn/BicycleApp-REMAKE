package com.example.myapplication.userActivity;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.example.myapplication.config.Config;
import com.example.myapplication.R;
import com.example.myapplication.config.RS;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText regEmail,regAlamat,regPassword,regNama,regHp,regKtp;
    Button btnRegister;
    private boolean mIsFormFilled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        btnRegister = findViewById(R.id.btnRegister);
        regEmail = findViewById(R.id.regEmail);
        regAlamat = findViewById(R.id.regAlamat);
        regPassword = findViewById(R.id.regPassword);
        regNama = findViewById(R.id.regNama);
        regHp = findViewById(R.id.regHp);
        regKtp = findViewById(R.id.regKtp);

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = 800;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(final View view) {
                final String email = regEmail.getText().toString();
                final String alamat = regAlamat.getText().toString();
                final String password = regPassword.getText().toString();
                final String nama = regNama.getText().toString();
                final String hp = regHp.getText().toString();
                final String ktp = regKtp.getText().toString();

                HashMap<String, String> body = new HashMap<>();
                body.put("email", email);
                body.put("password",password);
                body.put("nohp",hp);
                body.put("nama", nama);
                body.put("alamat", alamat);
                body.put("noktp", ktp);
                AndroidNetworking.post("http://192.168.6.233:8000/api/register")
                        .addBodyParameter(body)
                        .setPriority(Priority.MEDIUM)
                        .setOkHttpClient(((RS) getApplication()) .getOkHttpClient())
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                                if (message.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
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
                                                        int userid = payload.optInt("id");
                                                        Log.e("AF", "id: "+userid);
                                                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                                        intent.putExtra("userid",userid);
                                                        startActivity(intent);
                                                        finish();
                                                        finishAffinity();
                                                    }else {
                                                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                                @Override
                                                public void onError(ANError anError) {
                                                    Toast.makeText(RegisterActivity.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
                                                    Log.d("HBB", "onError: " + anError.getErrorBody());
                                                    Log.d("HBB", "onError: " + anError.getLocalizedMessage());
                                                    Log.d("HBB", "onError: " + anError.getErrorDetail());
                                                    Log.d("HBB", "onError: " + anError.getResponse());
                                                    Log.d("HBB", "onError: " + anError.getErrorCode());
                                                }
                                            });
                                }else {
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(RegisterActivity.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
                                Log.d("HBB", "onError: " + anError.getErrorBody());
                                Log.d("HBB", "onError: " + anError.getLocalizedMessage());
                                Log.d("HBB", "onError: " + anError.getErrorDetail());
                                Log.d("HBB", "onError: " + anError.getResponse());
                                Log.d("HBB", "onError: " + anError.getErrorCode());
                            }
                        });
            }
        });
    }
    public void orLogin(View view){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void popup(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = 1000;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}