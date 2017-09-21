package com.nozagleh.captainmexico;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by arnarfreyr on 17.9.2017.
 */

public class UploadImageTask extends AsyncTask<URL, Integer, Long> {
    private static final String TAG = "UploadImageTask";

    private File imgFile;
    private Integer personId;

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Long doInBackground(URL... urls) {
        int count = urls.length;
        long totalSize = 0;

        for (int i = 0; i < count; i++) {
            uploadFile(urls[i]);
            publishProgress((int) ((i / (float) count) * 100));

            if (isCancelled())
                break;
        }

        return totalSize;
    }

    public void setImgFile(File file) {
        this.imgFile = file;
    }

    public void setPersonId(Integer id) {
        this.personId = id;
    }

    public Boolean uploadFile(URL u) {
        HttpURLConnection connection;
        DataOutputStream dataOutputStream;

        int serverResponseCode = 0;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String boundary = "*****";

        try {
            FileInputStream fileInputStream = new FileInputStream(imgFile);
            connection = (HttpURLConnection) u.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.setRequestProperty("uploaded_file",imgFile.getPath());
            connection.setRequestProperty("person_id", "1");

            dataOutputStream = new DataOutputStream(connection.getOutputStream());

            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                    + imgFile.getPath() + "\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer,0,bufferSize);

            while (bytesRead > 0) {
                dataOutputStream.write(buffer,0,bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                bytesRead = fileInputStream.read(buffer,0,bufferSize);
            }

            dataOutputStream.writeBytes(lineEnd);

            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"person_id\"" + lineEnd);
            dataOutputStream.writeBytes("Content-Type: text/plain; charset=utf8" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);

            String str = this.personId.toString();
            byte[] strOut = str.getBytes("UTF-8");
            dataOutputStream.write(strOut);

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            Log.d(TAG, "Server response: " + serverResponseMessage + ": " + serverResponseCode);

            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();

        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        return true;
    }
}
