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

    public static final String FIREBASE_ROOT = "https://firebasestorage.googleapis.com/v0/b/lostinmex-31d91.appspot.com/o/person-images%%%1$s.jpg?alt=media";

    public static FirebaseManager firebaseManager = new FirebaseManager();

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

}
