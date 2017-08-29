package com.nozagleh.captainmexico;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.sql.Time;
import java.util.Date;

/**
 * Created by arnar on 2017-08-29.
 */

public class GPSManager {

    private final String TAG = "GPSManager";

    private Context context;

    private LocationManager lm;
    private LocationListener ll;
    private PackageManager pm;

    private Location loc;

    private int hasPerm;

    /**
     * Empty default constructor
     */
    public GPSManager(Context context) {
        this.context = context;
        initLocationManager();
    }

    private void initLocationManager() {
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        ll = new CustomLocationListener();

        pm = context.getPackageManager();

    }

    public Double[] getLocation() {
        Double[] location = new Double[]{};

        // Get the permission
        hasPerm = pm.checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                context.getPackageName()
        );

        // Check if the needed permission for the GPS has been granted
        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
            // Check if the location is not null and that it is newly fetched
            if (loc == null || loc.getTime() < java.util.Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
                // Request location updates
                lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 5000, 10, ll);
                loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            location = new Double[]{loc.getLongitude(), loc.getLatitude()};

        } else {
            // TODO ask for permission for GPS
        }

        lm.removeUpdates(ll);
        return location;
    }
}

class CustomLocationListener implements LocationListener {

    private final String TAG = "CustomLocationListener";

    @Override
    public void onLocationChanged(Location location) {
        //Log.d(TAG, location.getLatitude() + " : " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
