package com.example.tejas.girlsecurity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Addcontacts extends AppCompatActivity {
    ImageView btadd1,btadd2,btadd3;
    TextView contact1,contact2,contact3,message;
    Button savecontacts;
    int i=1,j=2,k=3;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontacts);
        btadd1 = (ImageView) findViewById(R.id.add_img1);
        btadd2 = (ImageView) findViewById(R.id.add_img2);
        btadd3 = (ImageView) findViewById(R.id.add_img3);

        contact1=(TextView)findViewById(R.id.text_addcontact1);
        contact2=(TextView)findViewById(R.id.text_addcontact2);
        contact3=(TextView)findViewById(R.id.text_addcontact3);
        message=(TextView)findViewById(R.id.ac_alerttextpreview);

        savecontacts=(Button)findViewById(R.id.ac_save);

        db=openOrCreateDatabase("girlsecurity",MODE_PRIVATE,null);
        db.execSQL("create table if not exists gscontacts1(name1 varchar(20),name2 varchar(20),name3 varchar(20),msg varchar(100))");


        savecontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String con1=contact1.getText().toString();
                String con2=contact2.getText().toString();
                String con3=contact3.getText().toString();
                String msg=message.getText().toString();
                if(con1.equals("")){
                    Toast.makeText(getApplicationContext(),"Please select a contact",Toast.LENGTH_SHORT).show();
                }
                else if(con2.equals("")){
                    Toast.makeText(getApplicationContext(),"Please select a contact",Toast.LENGTH_SHORT).show();
                }
                else if(con3.equals("")){
                    Toast.makeText(getApplicationContext(),"Please select a contact",Toast.LENGTH_SHORT).show();
                }
                else if(msg.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter a message",Toast.LENGTH_SHORT).show();
                }
                else {
                    db.execSQL("insert into gscontacts1 values('" + con1 + "','" + con2 + "','" + con3 + "','"+msg+"')");

                    Toast.makeText(getApplicationContext(),"Contacts updated successfully",Toast.LENGTH_SHORT).show();
                    Intent it =new Intent(Addcontacts.this,Welcome.class);
                    startActivity(it);
                }
            }
        });

        btadd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_PICK, Uri.parse("Contents://contacts"));
                it.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(it, i);
            }
        });
        btadd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_PICK, Uri.parse("Contents://contacts"));
                it.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(it, j);
            }
        });
        btadd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_PICK, Uri.parse("Contents://contacts"));
                it.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(it, k);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==i){
            if(resultCode==RESULT_OK){
                Uri uri=data.getData();
                String[] s={ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor c=getContentResolver().query(uri,s,null,null,null);
                c.moveToFirst();
                if(c.getCount()>0){
                    int i=c.getColumnIndex(s[0]);
                    String s1=c.getString(i);
                    contact1.setText(s1);
                }

            }
        }
        if(requestCode==j){
            if(resultCode==RESULT_OK){
                Uri uri=data.getData();
                String[] s={ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor c=getContentResolver().query(uri,s,null,null,null);
                c.moveToFirst();
                if(c.getCount()>0){
                    int j=c.getColumnIndex(s[0]);
                    String s2=c.getString(j);
                    contact2.setText(s2);
                }

            }
        }
        if(requestCode==k){
            if(resultCode==RESULT_OK){
                Uri uri=data.getData();
                String[] s={ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor c=getContentResolver().query(uri,s,null,null,null);
                c.moveToFirst();
                if(c.getCount()>0){
                    int k=c.getColumnIndex(s[0]);
                    String s3=c.getString(k);
                    contact3.setText(s3);
                }

            }
        }

    }


}
