package com.example.tejas.girlsecurity;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class Welcome extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    Button btadd, bthelp, btsafezone;
    ImageButton btcaution;
    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    SQLiteDatabase db;
    String scon1, scon2, scon3, smsg, StateName, CityName, CountryName, add, premises, street, local;

    double latitude, longitude;
    int i=1;

    boolean gps_enabled = false;
    boolean network_enabled = false;
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
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
        setContentView(R.layout.activity_welcome);

        if (shouldAskPermissions()) {
            askPermissions();
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        bthelp = (Button) findViewById(R.id.help_button);
        btadd = (Button) findViewById(R.id.addc_button);
        btcaution = (ImageButton) findViewById(R.id.caution_button);
        btsafezone=(Button)findViewById(R.id.Safezone_button);
        db = openOrCreateDatabase("girlsecurity", MODE_PRIVATE, null);
        bthelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor c = db.rawQuery("select * from gscontacts1", null);
                c.moveToFirst();
                if (c != null) {
                    do {
                        if (c.getCount() > 0) {
                            smsg = c.getString(c.getColumnIndex("msg"));
                            scon1 = c.getString(c.getColumnIndex("name1"));
                            scon2 = c.getString(c.getColumnIndex("name2"));
                            scon3 = c.getString(c.getColumnIndex("name3"));

                        }
                    } while (c.moveToNext());
                    String d1,d2;
                    SharedPreferences sharedpreferences = getSharedPreferences("key", Context.MODE_PRIVATE);
                    d1= sharedpreferences.getString("kamal1", "");
                    d2 = sharedpreferences.getString("kamal2", "");
                    latitude = Double.valueOf(d1);
                    longitude = Double.valueOf(d2);
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
                    getlocaddress();
                    String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + "";
                    sendmsg(uri);
                }


            }
                private void sendmsg(String ss) {
                    // TODO Auto-generated method stub

                    SmsManager sm=SmsManager.getDefault();
                    System.out.println("@@@@@@@@@@@@"+scon1);
                    sm.sendTextMessage(scon1, null, smsg+"\n"+add, null, null);
                    sm.sendTextMessage(scon2, null, smsg+"\n"+add, null,null);
                    sm.sendTextMessage(scon3, null,smsg+"\n"+add, null,null);
                    sm.sendTextMessage("100",null,smsg+"\n"+add,null,null);

                    SmsManager sm2=SmsManager.getDefault();
                    sm2.sendTextMessage(scon1, null, smsg+"\n"+ss, null, null);
                    sm2.sendTextMessage(scon2, null, smsg+"\n"+ss, null,null);
                    sm2.sendTextMessage(scon3, null,smsg+"\n"+ss, null,null);
                    sm2.sendTextMessage("100",null,smsg+"\n"+add,null,null);

                }


                private void getlocaddress() {
                    // TODO Auto-generated method stub
                    try
                    {
                        Geocoder geocoder;

                        List<Address> addresses;
                        geocoder = new Geocoder(Welcome.this, Locale.getDefault());

                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        street=addresses.get(0).getAddressLine(0);
                        local=addresses.get(0).getSubAdminArea();
                        StateName= addresses.get(0).getAdminArea();
                        CityName = addresses.get(0).getLocality();
                        CountryName = addresses.get(0).getCountryName();
                        premises=addresses.get(0).getPremises();

                        System.out.println(" StateName " + StateName);
                        System.out.println(" CityName " + CityName);
                        System.out.println(" CountryName " + CountryName);
                        System.out.println(" CountryName " + premises);
                        add=street+"\n"+StateName+"\n"+CityName+"\n"+CountryName;

                        Toast.makeText(getApplicationContext(), StateName+CityName+CountryName, Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
        });

        btsafezone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = db.rawQuery("select * from gscontacts1", null);
                c.moveToFirst();
                if (c != null) {
                    do {
                        if (c.getCount() > 0) {
                            scon1 = c.getString(c.getColumnIndex("name1"));
                            scon2 = c.getString(c.getColumnIndex("name2"));
                            scon3 = c.getString(c.getColumnIndex("name3"));
                            Toast.makeText(getApplicationContext(), "Im in Safe Zone", Toast.LENGTH_SHORT).show();

                        }
                    } while (c.moveToNext());
                    String d1,d2;
                    SharedPreferences sharedpreferences = getSharedPreferences("key", Context.MODE_PRIVATE);
                    d1= sharedpreferences.getString("kamal1", "");
                    d2 = sharedpreferences.getString("kamal2", "");
                    latitude = Double.valueOf(d1);
                    longitude = Double.valueOf(d2);
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
                    getlocaddress();
                    String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + "";
                    sendmsg(uri);
                    }
                }


            private void sendmsg(String ss) {
                // TODO Auto-generated method stub

                SmsManager sm=SmsManager.getDefault();
                sm.sendTextMessage(scon1, null, "I feel safe now"+"\n"+add, null, null);
                sm.sendTextMessage(scon2, null, "I feel safe now"+"\n"+add, null,null);
                sm.sendTextMessage(scon3, null,"I feel safe now"+"\n"+add, null,null);

                SmsManager sm2=SmsManager.getDefault();
                sm2.sendTextMessage(scon1, null, "I feel safe now"+"\n"+ss, null, null);
                sm2.sendTextMessage(scon2, null, "I feel safe now"+"\n"+ss, null,null);
                sm2.sendTextMessage(scon3, null,"I feel safe now"+"\n"+ss, null,null);

            }


            private void getlocaddress() {
                // TODO Auto-generated method stub
                try
                {

                    Geocoder geocoder;

                    List<Address> addresses;
                    geocoder = new Geocoder(Welcome.this, Locale.getDefault());

                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    street=addresses.get(0).getAddressLine(0);


                    local=addresses.get(0).getSubAdminArea();
                    StateName= addresses.get(0).getAdminArea();
                    CityName = addresses.get(0).getLocality();
                    CountryName = addresses.get(0).getCountryName();
                    premises=addresses.get(0).getPremises();

                    System.out.println(" StateName " + StateName);
                    System.out.println(" CityName " + CityName);
                    System.out.println(" CountryName " + CountryName);
                    System.out.println(" CountryName " + premises);
                    add=street+"\n"+local+"\n"+StateName+"\n"+CityName+"\n"+CountryName;

                    Toast.makeText(getApplicationContext(), StateName+CityName+CountryName, Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });



        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(Welcome.this,Addcontacts.class);
                startActivity(it);
            }
        });

        btcaution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it= new Intent(Welcome.this,Cautionlist.class);
                startActivity(it);
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
               Log.d("kamal","With in if");
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            Log.i("kamal","Else");
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null)
        {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        SharedPreferences sharedpreferences = getSharedPreferences("key", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("kamal1",String.valueOf(latitude));
        editor.putString("kamal2", String.valueOf(longitude));
        editor.commit();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                            buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            Bundle extras = data.getExtras();
           longitude = extras.getDouble("Longitude");
           latitude = extras.getDouble("Latitude");
        }
    }
}