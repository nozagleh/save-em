package com.nozagleh.captainmexico;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by arnarfreyr on 7.9.2017.
 */

class CustomOnSuccessListener implements OnSuccessListener<Uri>{
    Uri uri;

    @Override
    public void onSuccess(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

}
