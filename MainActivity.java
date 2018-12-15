package com.example.tejas.girlsecurity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Intent mServiceIntent;
    private yourservice mYourService;
    int i=0;
    boolean status=false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    int MODE = 0;
    private static final String PREFERENCE = "mypreference";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(PREFERENCE, MODE);
        spEditor = sharedPreferences.edit();
//        public boolean onKeyDown(int keyCode, KeyEvent event) {
//            System.out.println("In Key Down Method." + event.getKeyCode());
//            if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
//                i++;
//                System.out.println("Power button pressed.");
//                if (i == 2) {
//                    System.out.println("Power button pressed continuoulsy 3 times.");
//                    Toast.makeText(MainActivity.this, "Power button pressed " + i + " times.", Toast.LENGTH_SHORT).show();
//                } else {
//                    System.out.println("Power button pressed continuoulsy " + i + " times.");
//                    Toast.makeText(MainActivity.this, "Power button pressed " + i + " times.", Toast.LENGTH_SHORT).show();
//                }
//            }
//            return super.onKeyDown(keyCode, event);
//        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent it=new Intent(MainActivity.this,Login.class);
                startActivity(it);
                finish();
            }
        }, 2000);
    }
}
