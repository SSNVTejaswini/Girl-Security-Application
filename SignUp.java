package com.example.tejas.girlsecurity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignUp extends AppCompatActivity {
    Button ssignup,slogin,sisignup;
    EditText suser,semail,sphone,saddress,spwd;
    SQLiteDatabase db;
    String usersignup,phonesignup,emailsignup,addresssignup,pwdsignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        slogin=(Button)findViewById(R.id.signup_loginbutton);
        ssignup=(Button)findViewById(R.id.signup_signupbutton);

        suser=(EditText)findViewById(R.id.signup_username);
        sphone=(EditText)findViewById(R.id.signup_phone);
        semail=(EditText)findViewById(R.id.signup_email);
        saddress=(EditText)findViewById(R.id.signup_address);
        spwd=(EditText)findViewById(R.id.signup_password);

        sisignup=(Button)findViewById(R.id.signup_supbutton);

        db=openOrCreateDatabase("girlsecurity", MODE_PRIVATE, null);
        db.execSQL("create table if not exists gssignup(siusername varchar(40),siphone varchar(10),siemail varchar(30),siaddress varchar(50),sipassword varchar(20))");



        sisignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usersignup=suser.getText().toString();
              emailsignup=semail.getText().toString();
              phonesignup=sphone.getText().toString();
              addresssignup=saddress.getText().toString();
              pwdsignup=spwd.getText().toString();
                if(usersignup.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter username",Toast.LENGTH_SHORT).show();
                }
                else if(emailsignup.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter your email",Toast.LENGTH_SHORT).show();
                }
                else if(phonesignup.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter your number",Toast.LENGTH_SHORT).show();
                }
                else if(addresssignup.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter address",Toast.LENGTH_SHORT).show();
                }
                else if(pwdsignup.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                }
                else {
                    db.execSQL("insert into gssignup values('" + usersignup + "','" + emailsignup + "','" + phonesignup + "','"+addresssignup+"','"+pwdsignup+"')");

                    Toast.makeText(getApplicationContext(),"Signup successfull",Toast.LENGTH_SHORT).show();
                    Intent it =new Intent(SignUp.this,Login.class);
                    startActivity(it);
                }
            }
        });

        slogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(SignUp.this,Login.class);
                startActivity(it);
            }
        });

        ssignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(SignUp.this,SignUp.class);
                startActivity(it);
            }
        });


    }
}
