package com.example.myapplication.adminActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.PermissionRequest;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.example.myapplication.config.Adapter;
import com.example.myapplication.config.Config;
import com.example.myapplication.R;
import com.example.myapplication.config.RS;
import com.example.myapplication.admin.AdminImageAdapter;
import com.example.myapplication.config.SessionManager;
import com.example.myapplication.user.HomeModel;

import com.example.myapplication.userActivity.HomeActivity;
import com.example.myapplication.userActivity.MainActivity;

import com.example.myapplication.userActivity.RegisterActivity;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import id.zelory.compressor.Compressor;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityViewBicycle extends AppCompatActivity implements IPickResult {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_GALLERY = 200;
    SessionManager sessionManager;
    Button upload2;
    TextView file_name;
    ImageView result_img;
    String file_path = null;
    private RecyclerView daataList;
    private AdminImageAdapter mAdapter;
    private SwipeRefreshLayout swp;
    private ArrayList<HomeModel> mList = new ArrayList<>();
    Button btn;
    boolean sds;
    private Bitmap mSelectedImage;
    private String mSelectedImagePath;
    File mSelectedFileBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_view_bicycle);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setLogin(false);
        file_name = findViewById(R.id.daftarsepeda);

        daataList = findViewById(R.id.daataList);
        daataList.setHasFixedSize(true);
        daataList.setLayoutManager(new LinearLayoutManager(this));
        ImageView add = findViewById(R.id.img_additem);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityViewBicycle.this,AddItemActivity.class);
                startActivity(intent);
            }
        });
        getItemrList();
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PickImageDialog.build(new PickSetup()).show(ActivityViewBicycle.this);
//            }
//        });
//        upload2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                testimg();
//            }
//        });

    }
    @Override
    public void onPickResult(PickResult r) {
      if(r.getError() == null){
          try {
              File fileku = new Compressor(this)
                      .setQuality(50)
                      .setCompressFormat(Bitmap.CompressFormat.WEBP)
                      .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                      .compressToFile(new File(r.getPath()));
              mSelectedImagePath = fileku.getAbsolutePath();
              mSelectedFileBanner = new File(mSelectedImagePath);
              mSelectedImage=r.getBitmap();
              result_img.setImageBitmap(mSelectedImage);
          } catch (IOException e) {
              e.printStackTrace();
          }
      }else{
          Toast.makeText(ActivityViewBicycle.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
      }
    }
//    public void testimg(){
//            AndroidNetworking.upload("http://192.168.43.237:8000/api/image")
//                .addMultipartFile("image",mSelectedFileBanner)
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
//                        if (message.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
//                            Toast.makeText(ActivityViewBicycle.this, "Y", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onError(ANError anError) {
//                        Toast.makeText(ActivityViewBicycle.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
//                        Log.d("HBB", "onError: " + anError.getErrorBody());
//                        Log.d("HBB", "onError: " + anError.getLocalizedMessage());
//                        Log.d("HBB", "onError: " + anError.getErrorDetail());
//                        Log.d("HBB", "onError: " + anError.getResponse());
//                        Log.d("HBB", "onError: " + anError.getErrorCode());
//                    }
//                });
//    }
       public void getItemrList() {
        AndroidNetworking.get(Config.BASE_URL + "getitem")
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
                        Log.d("RBA", "ressss" + response);

                        String status = response.optString(Config.RESPONSE_STATUS_FIELD);
                        String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                        Log.d("RBA", "messs" + message);
                        if (message.trim().equalsIgnoreCase("Succes")) {
                            JSONArray payload = response.optJSONArray(Config.RESPONSE_PAYLOAD_FIELD);
                            if (payload == null) {
                                Toast.makeText(ActivityViewBicycle.this, "Tidak ada user", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for (int i = 0; i < payload.length(); i++) {
                                JSONObject dataUser = payload.optJSONObject(i);
                                HomeModel item = new HomeModel(dataUser);
                                item.setIditem(dataUser.optInt("id"));
                                item.setMerk(dataUser.optString("merk"));
                                item.setWarna(dataUser.optString("warna"));
                                item.setKodesepeda(dataUser.optString("kodesepeda"));
                                item.setHarga(dataUser.optString("hargasewa"));
                                item.setGambar(dataUser.optString("gambar"));
                                mList.add(item);
                            }
                            mAdapter = new AdminImageAdapter(ActivityViewBicycle.this, mList, ActivityViewBicycle.this);
                            GridLayoutManager grd = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
                            daataList.setLayoutManager(grd);
                            daataList.setAdapter(mAdapter);
                        } else {
                            Toast.makeText(ActivityViewBicycle.this, message, Toast.LENGTH_SHORT).show();
                            JSONObject payload = response.optJSONObject(Config.RESPONSE_PAYLOAD_FIELD);
                            if (payload != null && payload.optString("API_ACTION").equalsIgnoreCase("LOGOUT"))
                                Config.forceLogout(ActivityViewBicycle.this);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityViewBicycle.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
                        Log.d("RBA", "onError: " + anError.getErrorBody());
                        Log.d("RBA", "onError: " + anError.getLocalizedMessage());
                        Log.d("RBA", "onError: " + anError.getErrorDetail());
                        Log.d("RBA", "onError: " + anError.getResponse());
                        Log.d("RBA", "onError: " + anError.getErrorCode());
                    }
                });
    }


}