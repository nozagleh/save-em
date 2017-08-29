package com.nozagleh.captainmexico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AddPerson extends AppCompatActivity {

    private final String TAG = "AddPerson";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        GPSManager gps = new GPSManager(getApplicationContext());

        Double[] location = gps.getLocation();

        for (Double latlng : location) {
            Log.d(TAG, latlng.toString());
        }
    }
}
