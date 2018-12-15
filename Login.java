package com.example.tejas.girlsecurity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class Login extends AppCompatActivity {
    Button lsignup,llogin,loginbutton;
    EditText username,password;
    SQLiteDatabase db;
    String luser,lpwd;
    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    static final Integer GPS_SETTINGS = 0x7;
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.SEND_SMS","android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION",
                "android.permission.INTERNET","android.permission.ACCESS_NETWORK_STATE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (shouldAskPermissions())
        {
            askPermissions();
        }
        askForGPS();
        loginbutton=(Button)findViewById(R.id.login_login);
        lsignup=(Button)findViewById(R.id.login_signup);
        llogin=(Button)findViewById(R.id.login_loginbutton);
        username=(EditText)findViewById(R.id.login_username);
        password=(EditText)findViewById(R.id.login_password);

        db=openOrCreateDatabase("girlsecurity", MODE_PRIVATE, null);
        db.execSQL("create table if not exists gssignup(siusername varchar(40),siphone varchar(10),siemail varchar(30),siaddress varchar(50),sipassword varchar(20))");

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                luser=username.getText().toString();
                lpwd=password.getText().toString();
                Cursor c=db.rawQuery("select * from gssignup where siusername='"+luser+"' and '"+lpwd+"'",null);
                c.moveToFirst();
                if(c.getCount()>0){
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                    Intent it=new Intent(Login.this,Welcome.class);
                    startActivity(it);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                    Intent it=new Intent(Login.this,Login.class);
                    startActivity(it);
                }
//                Intent it=new Intent(Login.this,Welcome.class);
//                startActivity(it);
            }
        });
        llogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(Login.this,Login.class);
                startActivity(it);
            }
        });

        lsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(Login.this,SignUp.class);
                startActivity(it);
            }
        });


    }
    private void askForGPS() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }
}
