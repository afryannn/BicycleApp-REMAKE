package com.example.myapplication.userActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.myapplication.BookingActivity;
import com.example.myapplication.UserTransaksi;
import com.example.myapplication.config.Adapter;
import com.example.myapplication.config.Config;
import com.example.myapplication.R;
import com.example.myapplication.config.RS;
import com.example.myapplication.config.SessionManager;
import com.example.myapplication.user.HomeAdapter;
import com.example.myapplication.user.HomeModel;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView dataList;
    private HomeAdapter mAdapter;
    public static String UserId;
    SessionManager sessionManager;
    List<String> titles;
    List<String> prices;
    List<Integer> imageslide;
    Adapter adapter;
    private SwipeRefreshLayout swp;
    private ArrayList<HomeModel> mList = new ArrayList<>();
    Button btn;
    ImageView logout;
    boolean sds;
    int[] sampleImages =new int[] {R.drawable.banner1,R.drawable.banner2,R.drawable.banner3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's oate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_home);
        sessionManager = new SessionManager(getApplicationContext());
        logout = findViewById(R.id.btnlogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionlogout();
            }
        });
        CarouselView carouselView = findViewById(R.id.imgslide);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
                imageView.setAdjustViewBounds(true);
            }
        });
        ImageView trns = findViewById(R.id.trns);
        trns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, UserTransaksi.class);
                startActivity(i);
            }
        });
        dataList = findViewById(R.id.dataList);
        dataList.setHasFixedSize(true);
        dataList.setLayoutManager(new LinearLayoutManager(this));
        getItemrList();
    }
    public void getItemrList() {
        AndroidNetworking.get(Config.BASE_URL + "getitem")
//                .addBodyParameter(body)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(((RS) getApplication()).getOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (mAdapter != null) {
                            mAdapter.clearData();
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mList != null) mList.clear();
                        Log.d("RBA", "res" + response);
                        String status = response.optString(Config.RESPONSE_STATUS_FIELD);
                        String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                        Log.d("RBA", "mes" + message);
                        if (message.trim().equalsIgnoreCase("Succes")) {
                            JSONArray payload = response.optJSONArray(Config.RESPONSE_PAYLOAD_FIELD);
                            if (payload == null) {
                                Toast.makeText(HomeActivity.this, "Tidak ada user", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for (int i = 0; i < payload.length(); i++) {
                                JSONObject dataUser = payload.optJSONObject(i);
                                HomeModel item = new HomeModel(dataUser);
                                item.setIditem(dataUser.optInt("id"));
                                item.setMerk(dataUser.optString("merk"));
                                item.setKodesepeda(dataUser.optString("kodesepeda"));
                                item.setWarna(dataUser.optString("warna"));
                                item.setHarga(dataUser.optString("hargasewa"));
                                item.setGambar(dataUser.optString("gambar"));
                                mList.add(item);
                            }
                            mAdapter = new HomeAdapter(HomeActivity.this, mList, HomeActivity.this);
                            GridLayoutManager grd = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
                            grd.getPaddingTop();
                            dataList.setLayoutManager(grd);
                            dataList.setAdapter(mAdapter);
                        } else {
                            Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                            JSONObject payload = response.optJSONObject(Config.RESPONSE_PAYLOAD_FIELD);
                            if (payload != null && payload.optString("API_ACTION").equalsIgnoreCase("LOGOUT"))
                                Config.forceLogout(HomeActivity.this);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(HomeActivity.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
                        Log.d("RBA", "onError: " + anError.getErrorBody());
                        Log.d("RBA", "onError: " + anError.getLocalizedMessage());
                        Log.d("RBA", "onError: " + anError.getErrorDetail());
                        Log.d("RBA", "onError: " + anError.getResponse());
                        Log.d("RBA", "onError: " + anError.getErrorCode());
                    }
                });
    }
    public void sessionlogout(){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah anda mau Logout?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sessionManager.setLogin(false);
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).create().show();
    }
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Keluar")
                .setMessage("Apakah anda yakin mau keluar?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).create().show();
    }
}