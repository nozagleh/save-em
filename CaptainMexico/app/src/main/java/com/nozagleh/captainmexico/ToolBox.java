package com.nozagleh.captainmexico;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.HttpException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;

/**
 * Created by arnar on 2017-08-30.
 */

public class ToolBox {

    private static final String TAG = "ToolBox";

    public static final String SERVER_ROOT = "10.0.2.2:8000";
    public static final String IMG_URL = SERVER_ROOT + "/media/";

    public static RequestQueue queue;

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static File getFile(Context context, Intent data) {
        Uri image = data.getData();
        Cursor cursor = context.getContentResolver().query(image, new String[] { MediaStore.Images.ImageColumns.DATA}, null, null, null);

        String imgPath = "";
        try {
            cursor.moveToFirst();

            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            imgPath = cursor.getString(id);
            cursor.close();
        } catch (CursorIndexOutOfBoundsException e) {
            Log.d(TAG, e.getMessage());
        }

        return new File(imgPath);
    }

    public static Boolean initQueue(Context context) {
        if (context == null)
            return false;

        queue = Volley.newRequestQueue(context);
        return true;
    }
}
