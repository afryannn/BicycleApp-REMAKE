package com.example.myapplication.adminActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.myapplication.R;
import com.example.myapplication.config.Config;
import com.example.myapplication.config.RS;
import com.example.myapplication.userActivity.HomeActivity;
import com.example.myapplication.userActivity.MainActivity;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;
import id.zelory.compressor.Compressor;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AddItemActivity extends AppCompatActivity implements IPickResult {
    ImageView imageView;
    private Bitmap mSelectedImage;
    private String mSelectedImagePath;
    File mSelectedFileBanner;
    EditText mKodesepeda,mMerk,mWarna,mHargasewa;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_add_item);
        mKodesepeda = findViewById(R.id.Akodesepeda);
        mMerk = findViewById(R.id.Amerk);
        mWarna = findViewById(R.id.Awarna);
        mHargasewa = findViewById(R.id.Ahargasewa);
        imageView = findViewById(R.id.imggetimage);
        save = findViewById(R.id.simpanitem);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).show(AddItemActivity.this);
                new PickSetup().setCameraButtonText("Gallery");
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postitem();
            }
        });
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
                imageView.setImageBitmap(mSelectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(AddItemActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void postitem(){
        final String kodesepeda = mKodesepeda.getText().toString();
        final String merk = mMerk.getText().toString();
        final String warna = mHargasewa.getText().toString();
        final String hargasewa = mHargasewa.getText().toString();
        HashMap<String, String> body = new HashMap<>();
        body.put("kodesepeda", kodesepeda);
        body.put("merk", merk);
        body.put("warna", warna);
        body.put("hargasewa", hargasewa);
        AndroidNetworking.upload(Config.BASE_URL + "postsepeda")
                .addMultipartFile("image",mSelectedFileBanner)
                .addMultipartParameter(body)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(((RS) getApplication()).getOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                        if (message.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                            Toast.makeText(AddItemActivity.this, "BERHASIL DITAMBAHKAN", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(AddItemActivity.this,ActivityViewBicycle.class);
                            startActivity(i);

                        }else {
                            Toast.makeText(AddItemActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AddItemActivity.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
                        Log.d("HBB", "onError: " + anError.getErrorBody());
                        Log.d("HBB", "onError: " + anError.getLocalizedMessage());
                        Log.d("HBB", "onError: " + anError.getErrorDetail());
                        Log.d("HBB", "onError: " + anError.getResponse());
                        Log.d("HBB", "onError: " + anError.getErrorCode());
                    }
                });
    }

    }
