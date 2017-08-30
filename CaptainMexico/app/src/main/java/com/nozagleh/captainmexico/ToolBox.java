package com.nozagleh.captainmexico;

import android.support.design.widget.Snackbar;

import java.util.UUID;

/**
 * Created by arnar on 2017-08-30.
 */

public class ToolBox {

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    // TODO add snackbar generator
    /*public static Snackbar generateSnackBar() {

        return;
    }*/
}
