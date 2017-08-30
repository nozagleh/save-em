package com.nozagleh.captainmexico;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
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

    public void startLocationTracking() {

        // Get the permission
        hasPerm = pm.checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                context.getPackageName()
        );

        // Check if the needed permission for the GPS has been granted
        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 5, ll);
        }
    }

    public Location getLocation() {
        // Get the permission
        hasPerm = pm.checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                context.getPackageName()
        );

        // Check if the needed permission for the GPS has been granted
        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
            return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return new Location(LocationManager.GPS_PROVIDER);
    }
}

class CustomLocationListener implements LocationListener {

    private final String TAG = "CustomLocationListener";

    private Location location;

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
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

    public Location getLocation() {
        return this.location;
    }
}
