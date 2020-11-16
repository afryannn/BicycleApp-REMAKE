package com.example.myapplication;

import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.myapplication.config.RS;
import com.example.myapplication.config.SessionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.config.Config;
import com.example.myapplication.userActivity.MainActivity;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;

import java.util.HashMap;

public class BookingActivity extends AppCompatActivity {
    ImageView bImage;
    TextView bMerk,bWarna,bKode,bHarga;
    Button bKonfir;
    private String merkk,warnaa,kodee,hargaa,gambarr;
    private int iid;
    SessionManager sessionManager;
    private EditText membayar;
    private String vBank;
    TextView bBank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_booking);
        sessionManager = new SessionManager(getApplicationContext());
        Toast.makeText(BookingActivity.this,sessionManager.getIdu(),Toast.LENGTH_LONG).show();
        bImage = findViewById(R.id.BK_image);
        bMerk = findViewById(R.id.BK_merk);
        bWarna = findViewById(R.id.BK_warna);
        bKode = findViewById(R.id.BK_kode);
        bHarga = findViewById(R.id.BK_harga);
        bKonfir = findViewById(R.id.BK_btnkonfir);
        membayar = findViewById(R.id.BK_jumlahuang);
        bBank = findViewById(R.id.BK_bank);

        //PUT
        Bundle extras = getIntent().getExtras();
        iid = extras.getInt("id");
        kodee = extras.getString("kode");
        merkk = extras.getString("merk");
        warnaa = extras.getString("warna");
        hargaa = extras.getString("harga");
        gambarr = extras.getString("gambar");

        //SET
        bKode.setText(kodee);
        bMerk.setText(merkk);
        bWarna.setText(warnaa);
        bHarga.setText(hargaa);
        Picasso.get()
                .load(Config.BASE_URL+"image/"+gambarr)
                .into(bImage);
        ImageView bca = findViewById(R.id.bBca);
        ImageView bri = findViewById(R.id.bBri);
        ImageView mandiri = findViewById(R.id.bMandiri);
        ImageView cod = findViewById(R.id.bCod);
        bca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vBank = "BCA ";
                bBank.setText("BCA ");
                bBank.setTextColor(R.color.colorPrimary);
            }
        });
        bri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vBank = "BRI ";
                bBank.setText("BRI ");
                bBank.setTextColor(R.color.colorPrimary);
            }
        });
        mandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vBank = "MANDIRI ";
                bBank.setText("MANDIRI ");
                bBank.setTextColor(R.color.colorPrimary);
            }
        });
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vBank = "COD ";
                bBank.setText("COD ");
                bBank.setTextColor(R.color.colorPrimary);
            }
        });
        bKonfir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bayar = membayar.getText().toString();
                String ss =  sessionManager.getIdu();
                String tm = Integer.toString(iid);
                HashMap<String, String> body = new HashMap<>();
                body.put("u_id",ss);
                body.put("i_id",tm);
                body.put("bank",vBank);
                body.put("price",bayar);
                AndroidNetworking.post(Config.BASE_URL + "user-booking")
                        .addBodyParameter(body)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(BookingActivity.this,"Alhamdulillah....", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(BookingActivity.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
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

    public void booking(){
        Toast.makeText(BookingActivity.this,vBank, Toast.LENGTH_SHORT).show();
        String bayar = membayar.getText().toString();
        String tm = Integer.toString(iid);
        String ss =  sessionManager.getIdu();
        HashMap<String, String> body = new HashMap<>();
//        body.put("u_id",);
//        body.put("i_id",tm);
//        body.put("price",bayar);
//        body.put("bank",vBank);
        AndroidNetworking.post(Config.BASE_URL + "user-booking")
                .addBodyParameter(body)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(BookingActivity.this,"Alhamdulillah....", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(BookingActivity.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
                        Log.d("HBB", "onError: " + anError.getErrorBody());
                        Log.d("HBB", "onError: " + anError.getLocalizedMessage());
                        Log.d("HBB", "onError: " + anError.getErrorDetail());
                        Log.d("HBB", "onError: " + anError.getResponse());
                        Log.d("HBB", "onError: " + anError.getErrorCode());
                    }
                });
    }
}