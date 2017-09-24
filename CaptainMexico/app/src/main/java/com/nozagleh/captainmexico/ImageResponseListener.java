package com.nozagleh.captainmexico;

import android.graphics.Bitmap;

/**
 * Created by arnarfreyr on 23.9.2017.
 */

public interface ImageResponseListener {
    void onError(String message);
    void onResponse(Bitmap response);
}
