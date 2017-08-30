package com.nozagleh.captainmexico;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.UUID;

/**
 * Created by arnar on 2017-08-30.
 */

public class ToolBox {

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

}
