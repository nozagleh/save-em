package com.nozagleh.captainmexico;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddPerson extends AppCompatActivity {

    private final String TAG = "AddPerson";

    private Boolean gpsGranted;

    private PermissionManager pm;
    private GPSManager gps;

    // Input fields and buttons
    private ImageButton imageButton;
    private TextView lblName;
    private EditText txtName;
    private TextView lblEyeColor;
    private EditText txtEyeColor;
    private TextView lblShoeSize;
    private EditText txtShoeSize;
    private TextView lblSex;
    private Spinner spinnerSex;
    private TextView lblHeight;
    private EditText txtHeight;
    private TextView lblHairColor;
    private EditText txtHairColor;
    /** Add more **/
    private Button btnSubmit;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        ActionBar actionBar = getSupportActionBar();

        // Show action bar to go back a step
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        // Set new Permission manager
        pm = new PermissionManager();

        // Set new GPS manager
        gps = new GPSManager(getApplicationContext());

        // Check if gps permission is granted
        gpsGranted = pm.gpsPermission(this);

        // Init the text inputs
        txtName = (EditText) findViewById(R.id.txtName);
        txtEyeColor = (EditText) findViewById(R.id.txtEyeColor);
        txtShoeSize = (EditText) findViewById(R.id.txtShoeSize);
        txtHeight = (EditText) findViewById(R.id.txtHeight);
        txtHairColor = (EditText) findViewById(R.id.txtHairColor);

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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
    }

    /**
     * Call on submit button press.
     * Calls the Firebase class for sending the persons data to the server
     */
    private void sendData() {
        // If is granted
        if ( gpsGranted ) {

            Person person = new Person();
            person.set_ID(ToolBox.randomString());

            if (txtName.length() > 0)
                person.setName(txtName.getText().toString());

            person.setGender(spinnerSex.getSelectedItem().toString());

            if (txtShoeSize.length() > 0)
                person.setShoeSize(Double.valueOf(txtShoeSize.getText().toString()));

            if (txtHeight.length() > 0)
                person.setHeight(Double.valueOf(txtHeight.getText().toString()));

            if (txtEyeColor.length() > 0)
                person.setEyeColor(txtEyeColor.getText().toString());

            if (txtHairColor.length() > 0)
                person.setHairColor(txtHairColor.getText().toString());

            // Get the current location
            String loc = "";
            Location location = gps.getLocation();
            loc = String.valueOf(location.getLatitude());
            loc += "," + String.valueOf(location.getLongitude());

            Log.d(TAG, loc);

            // Set the persons location
            if ( loc.length() > 0 )
                person.setGpsLocation(loc);

            // Init the FirebaseManager to send the data to the server
            FirebaseManager fm = new FirebaseManager();
            // Add the new person
            fm.addNewPerson(person);

            // On success, call function to end the activity
            endCurrentActivity();
        } else {
            gpsSnackBar();
        }
    }

    public void endCurrentActivity() {
        btnSubmit.setEnabled(false);

        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), R.string.hint_person_added,Snackbar.LENGTH_LONG);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                super.onShown(sb);
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);

                finish();
            }
        });
        snackbar.show();
    }

    public void gpsSnackBar() {
        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), R.string.hint_gps_deny,Snackbar.LENGTH_LONG);
        snackbar.setAction("Grant", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pm.gpsPermission(AddPerson.this);
            }
        });
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    imageButton.setImageURI(selectedImage);
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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
        finish();
        return super.onSupportNavigateUp();
    }
}
