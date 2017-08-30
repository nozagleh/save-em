package com.nozagleh.captainmexico;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by arnar on 2017-08-30.
 */

public class PermissionManager {

    public static final int GPS_REQUEST_CODE = 42;

    public PermissionManager() {
    }

    public Boolean gpsPermission(Activity activity) {
        if ( ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_REQUEST_CODE);

            return false;
        }
        return true;
    }
}
