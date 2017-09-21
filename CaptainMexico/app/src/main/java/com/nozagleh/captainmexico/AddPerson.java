package com.nozagleh.captainmexico;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nozagleh.captainmexico.ToolBox;

import static com.nozagleh.captainmexico.ToolBox.*;

public class AddPerson extends AppCompatActivity {

    private static final String UPLOAD_ROOT = "http://10.0.2.2:8000/mex/img/";

    private final String TAG = "AddPerson";

    private Boolean gpsGranted;

    private PermissionManager pm;
    private GPSManager gps;

    // Input fields and buttons
    private ImageButton imageButton;
    private Spinner spinnerSex;
    private Button btnSubmit;
    private EditText txtFirstName, txtLastName, txtBirthday, txtNationality,
            txtHeight, txtHairColor, txtWeight;

    private FirebaseManager fbm;
    private File imgFile;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        ActionBar actionBar = getSupportActionBar();

        // Show action bar to go back a step
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        // Get the app global firebase manager
        fbm = ToolBox.firebaseManager;

        // Set new Permission manager
        pm = new PermissionManager();

        // Set new GPS manager
        gps = new GPSManager(getApplicationContext());

        // Check if gps permission is granted
        gpsGranted = pm.gpsPermission(this);

        // Init the text inputs
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtBirthday = (EditText) findViewById(R.id.txtBirthdate);
        txtNationality = (EditText) findViewById(R.id.txtNationality);
        txtHeight = (EditText) findViewById(R.id.txtHeight);
        txtHairColor = (EditText) findViewById(R.id.txtHairColor);
        txtWeight = (EditText) findViewById(R.id.txtWeight);

        // Init the image button, add click listener
        imageButton = (ImageButton) findViewById(R.id.imgBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGalleryCamera();
            }
        });

        // Init the spinner for sex, attach adapter
        spinnerSex = (Spinner) findViewById(R.id.spinnerSex);
        ArrayAdapter<String> sexArray = new ArrayAdapter<String>(AddPerson.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.sex_array) );
        spinnerSex.setAdapter(sexArray);

        // Init the submit button, add listener
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call send data
                sendData();
            }
        });
    }

    /**
     * Launch the gallery or camera application for adding a photo
     */
    private void launchGalleryCamera() {
        // Start the gallery picker
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
    }

    /**
     * Call on submit button press.
     * Calls the Firebase class for sending the persons data to the server
     */
    private void sendData() {
        // If is granted
        if ( gpsGranted ) {

            SimpleDateFormat birthdateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            Person person = new Person();

            person.setFirstName(txtFirstName.getText().toString());
            person.setLastName(txtLastName.getText().toString());

            /*if (txtBirthday.length() > 0) {
                try {
                    person.setBirthdate(birthdateFormat.parse(txtBirthday.getText().toString()));
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }*/

            person.setNationality(txtNationality.getText().toString());
            person.setHeight(Double.valueOf(txtHeight.getText().toString()));
            person.setHairColor(txtHairColor.getText().toString());
            person.setWeight(Double.valueOf(txtWeight.getText().toString()));

            person.setGender(spinnerSex.getSelectedItemPosition());

            // Get the current location
            String loc = "";
            Location location = gps.getLocation();
            if (location != null) {
                loc = String.valueOf(location.getLatitude());
                loc += "," + String.valueOf(location.getLongitude());
            }

            // Set the persons location
            if ( loc.length() > 0 )
                person.setGpsLocation(loc);

            UrlBuilder urlBuilder = new UrlBuilder();
            urlBuilder.addPath("mex/person/add/");
            urlBuilder.addField("firstname", person.getFirstName());
            urlBuilder.addField("lastname", person.getLastName());

            urlBuilder.addField("birthdate", "1970-01-01");
            //urlBuilder.addField("birthdate", birthdateFormat.format(person.getBirthdate()));

            urlBuilder.addField("sex", person.getGender().toString());
            urlBuilder.addField("nationality", person.getNationality());
            urlBuilder.addField("height", person.getHeight().toString());
            urlBuilder.addField("haircolor", person.getHairColor());
            urlBuilder.addField("weight", person.getWeight().toString());
            urlBuilder.addField("gpslocation", person.getGpsLocation());
            urlBuilder.addField("found", person.getFound().toString());
            urlBuilder.addField("comments", person.getExtraFeatures());

            // TODO get some unique key for the device
            urlBuilder.addField("pkey", "133243423434");

            Log.d(TAG, urlBuilder.getUrl());
            String url = urlBuilder.getUrl();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d(TAG, response.getString("id"));
                    } catch (JSONException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            ToolBox.queue.add(jsonObjectRequest);

            if (imageUri != null) {
                UploadImageTask asyncTask = new UploadImageTask();
                asyncTask.setImgFile(imgFile);
                asyncTask.setPersonId(1);
                try {
                    URL uploadImageUrl = new URL(UPLOAD_ROOT);
                    //asyncTask.execute(uploadImageUrl);
                } catch (MalformedURLException e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            // On success, call function to end the activity
            //endCurrentActivity();
        } else {
            gpsSnackBar();
        }
    }

    /**
     * End the activity on insert
     */
    public void endCurrentActivity() {
        // Disable the add button
        btnSubmit.setEnabled(false);

        // Create a snackbar letting the user know of the insert
        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), R.string.hint_person_added,Snackbar.LENGTH_LONG);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                super.onShown(sb);
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                // Finish the current activity on snackbar dismiss
                finish();
            }
        });

        // Show the snackbar
        snackbar.show();
    }

    /**
     * Show GPS permission denied snackbar
     */
    public void gpsSnackBar() {
        // Create a snackbar for the messabe
        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), R.string.hint_gps_deny,Snackbar.LENGTH_LONG);
        snackbar.setAction("Grant", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Run function on Grant click
                pm.gpsPermission(AddPerson.this);
            }
        });

        // Show the snackbar
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Get the request code when gallery is closed
        switch (requestCode) {
            case 1:
                // Check if the results are fine
                if(resultCode == RESULT_OK){
                    // Get the image
                    Uri selectedImage = data.getData();

                    // Set the image button's image to the image
                    imageButton.setImageURI(selectedImage);

                    imgFile = getFile(getApplicationContext(), data);
                    imageUri = selectedImage;
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // Get results on permissions
        switch (requestCode) {
            case PermissionManager.GPS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    gpsGranted = true;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // TODO handle rejected permission
                    gpsGranted = false;

                   gpsSnackBar();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Finish the activity on back press from ActionBar
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
