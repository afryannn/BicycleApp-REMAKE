package com.example.myapplication.adminActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.example.myapplication.config.Config;
import com.example.myapplication.R;
import com.example.myapplication.config.RS;
import com.example.myapplication.config.SessionManager;
import org.json.JSONObject;

import java.util.HashMap;

public class EditCustomer extends AppCompatActivity {
    private boolean isForm = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setLogin(false);
        final EditText etemail = findViewById(R.id.editemail);
        final EditText etpassword = findViewById(R.id.editpassword);
        final EditText etnama = findViewById(R.id.editnama);
        final EditText etnohp = findViewById(R.id.editnohp);
        final EditText etnoktp = findViewById(R.id.editnoktp);
        final EditText etalamat = findViewById(R.id.editalamat);
        Button btnsimpan = findViewById(R.id.btnsimpan);
        Button kembali = findViewById(R.id.btngajadi);

        Bundle extras = getIntent().getExtras();
        final Integer exid = extras.getInt("id");
        String exemail = extras.getString("email");
        String expassword = extras.getString("password");
        String exnama = extras.getString("nama");
        String exnoktp = extras.getString("noktp");
        String exnohp = extras.getString("nohp");
        String exalamat = extras.getString("alamat");
        etemail.setText(exemail);
        etpassword.setText(expassword);
        etnama.setText(exnama);
        etnohp.setText(exnohp);
        etnoktp.setText(exnoktp);
        etalamat.setText(exalamat);

        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isForm = true;
                final String toemail = etemail.getText().toString();
                final String topassword = etpassword.getText().toString();
                final String tonama = etnama.getText().toString();
                final String tonohp = etnohp.getText().toString();
                final String tonoktp = etnoktp.getText().toString();
                final String tolamat = etalamat.getText().toString();

                if(isForm){
                    HashMap<String, String> body = new HashMap<>();
                    body.put("id", String.valueOf(exid));
                    body.put("email", toemail);
                    body.put("password", topassword);
                    body.put("nama", tonama);
                    body.put("nohp", tonohp);
                    body.put("noktp", tonoktp);
                    body.put("alamat", tolamat);
                    AndroidNetworking.post(Config.BASE_URL + "customerupdate")
                            .addBodyParameter(body)
                            .setPriority(Priority.MEDIUM)
                            .setOkHttpClient(((RS) getApplication()).getOkHttpClient())
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent intent = new Intent(EditCustomer.this, ViewCustomerAdmin.class);
                                    startActivity(intent);
                                }
                                @Override
                                public void onError(ANError anError) {

                                }
                            });
                }
            }
        });
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditCustomer.this,ViewCustomerAdmin.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EditCustomer.this,ViewCustomerAdmin.class);
        startActivity(i);
    }
}

