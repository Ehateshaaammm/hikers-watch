package com.example.mohammadehatesham.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager manager;
    LocationListener listener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                update(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            Location lastKnownLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                update(lastKnownLocation);
            }
        }
    }
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            }
        }

        public void startListening(){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            }

        }

    public void update(Location location){
        TextView latTextView = findViewById(R.id.latTextView);
        TextView longTextView = findViewById(R.id.longTextView);
        TextView altTextView = findViewById(R.id.altTextView);
        TextView accTextView = findViewById(R.id.accuracyTextView);
        TextView addTextView = findViewById(R.id.addTextView);

        latTextView.setText("Latitude: "+Double.toString(location.getLatitude()));
        longTextView.setText("Longitude: "+Double.toString(location.getLongitude()));
        altTextView.setText("Altitude: "+Double.toString(location.getAltitude()));
        accTextView.setText("Accuracy: "+Double.toString(location.getAccuracy()));

        String  address = "Counld not find address :( ";
        Geocoder geocoder = new Geocoder(this,Locale.getDefault());
        try {
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(listAddress!=null && listAddress.size()>0){
                address = "Address: \n";
                if(listAddress.get(0).getThoroughfare()!=null){
                    address += listAddress.get(0).getThoroughfare()+ "\n";
                }

                if(listAddress.get(0).getLocality()!=null){
                    address += listAddress.get(0).getLocality()+ " ";
                }
                if(listAddress.get(0).getPostalCode()!=null){
                    address += listAddress.get(0).getPostalCode()+ "\n";
                }
                if(listAddress.get(0).getAdminArea()!=null){
                    address += listAddress.get(0).getAdminArea()+ " ";
                }

                if(listAddress.get(0).getCountryName()!=null){
                    address += listAddress.get(0).getCountryName();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        addTextView.setText(address);
        }
}
