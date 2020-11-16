package com.example.myapplication.adminActivity;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.R;
import com.example.myapplication.config.Config;
import com.example.myapplication.config.SessionManager;
import com.squareup.picasso.Picasso;

public class EditItem extends AppCompatActivity {

    ImageView img;
    EditText edtkodesepeda,edtmerk,edtwarna,edthargasewa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_edit_item);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setLogin(false);
        edtkodesepeda = findViewById(R.id.updatekodesepeda);
        edtmerk = findViewById(R.id.updatemerk);
        edtwarna = findViewById(R.id.updatewarna);
        img = findViewById(R.id.updateimg);
        edthargasewa = findViewById(R.id.updatehargasewa);

        Bundle extras = getIntent().getExtras();
        final Integer exid = extras.getInt("id");
        String exkodesepeda = extras.getString("kodesepeda");
        String exmerk = extras.getString("merk");
        String exwarna = extras.getString("warna");
        String exgambar = extras.getString("gambar");
        String exhargasewa = extras.getString("harga");
        Picasso.get()
                .load(Config.BASE_URL+"image/"+exgambar)
                .into(img);
        edtmerk.setText(exmerk);
        edtwarna.setText(exwarna);
        edthargasewa.setText(exhargasewa);
    }
}