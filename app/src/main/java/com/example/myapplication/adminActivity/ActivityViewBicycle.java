package com.example.myapplication.adminActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.example.myapplication.config.Adapter;
import com.example.myapplication.config.Config;
import com.example.myapplication.R;
import com.example.myapplication.config.RS;
import com.example.myapplication.admin.AdminImageAdapter;
import com.example.myapplication.config.SessionManager;
import com.example.myapplication.user.HomeModel;

import com.example.myapplication.userActivity.MainActivity;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityViewBicycle extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_GALLERY = 200;
    SessionManager sessionManager;
    Button upload2;
    TextView file_name;
    ImageView tstimg;
    String file_path = null;
    private RecyclerView daataList;
    private AdminImageAdapter mAdapter;
    List<String> titles;
    List<String> prices;
    List<Integer> images;
    Adapter adapter;
    private SwipeRefreshLayout swp;
    private ArrayList<HomeModel> mList = new ArrayList<>();
    Button btn;
    boolean sds;
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
        tstimg = findViewById(R.id.testimg);
        upload2 = findViewById(R.id.upload_file2);
        file_name = findViewById(R.id.daftarsepeda);
        Button upload = findViewById(R.id.upload_file);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=23){
                    filepicker();
                }else{
                    requestPermission();
                }
            }
        });
        upload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(file_path!=null){
                    UploadFile();
                }else{
                    Toast.makeText(ActivityViewBicycle.this,"please choose...",Toast.LENGTH_SHORT).show();
                }
            }
        });
        daataList = findViewById(R.id.daataList);
        daataList.setHasFixedSize(true);
        daataList.setLayoutManager(new LinearLayoutManager(this));
        getItemrList();
    }

    private void UploadFile(){
     UploadTask uploadTask = new UploadTask();
     uploadTask.execute(new String[]{file_path});
    }
    private void filepicker(){
        Toast.makeText(ActivityViewBicycle.this,"File Picker...",Toast.LENGTH_SHORT).show();
        Intent opengallery=new Intent(Intent.ACTION_PICK);
        opengallery.setType("image/*");
        startActivityForResult(opengallery,REQUEST_GALLERY);
    }

    private void requestPermission(){
     if(ActivityCompat.shouldShowRequestPermissionRationale(ActivityViewBicycle.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
         Toast.makeText(ActivityViewBicycle.this,"Acces Denied..",Toast.LENGTH_SHORT).show();
     }else{
         ActivityCompat.requestPermissions(ActivityViewBicycle.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
     }
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(ActivityViewBicycle.this,Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result== PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(ActivityViewBicycle.this,"Permission Succes...",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ActivityViewBicycle.this,"Permission Failed",Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_GALLERY && resultCode== Activity.RESULT_OK){
            String filePath=getRealPathFromUri(data.getData(),ActivityViewBicycle.this);
            Log.d("File Path bossqiu: "," "+filePath);
            this.file_path=filePath;
            File file = new File(filePath);
            file_name.setText(file.getName());
        }
    }
    public String getRealPathFromUri(Uri uri,Activity activity){
        String[] proj  = {MediaStore.Images.Media.DATA};
        Cursor cursor=activity.getContentResolver().query(uri,proj,null,null,null,null);
        if(cursor==null){
            return uri.getPath();
        }else{
            cursor.moveToFirst();
            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }

    public class UploadTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("true")){
                Toast.makeText(ActivityViewBicycle.this,"200 OK",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ActivityViewBicycle.this,"Failed",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(uploadFile(strings[0])){
                return "true";
            }else{
                return "failed";
            }
        }
        private boolean uploadFile(String path){
            File file = new File(path);

            try {
                RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image",file.getName(),RequestBody.create(MediaType.parse("image/*"),file))
                        .addFormDataPart("some_key","some_value")
                        .addFormDataPart("submit","submit")
                        .build();
                Request request = new Request.Builder()
                        .url("http://192.168.43.237:8000/api/image")
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Toast.makeText(ActivityViewBicycle.this,"200 OK",Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
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