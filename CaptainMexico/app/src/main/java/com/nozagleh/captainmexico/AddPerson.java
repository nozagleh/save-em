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

import com.nozagleh.captainmexico.ToolBox;

public class AddPerson extends AppCompatActivity {

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

            person.setFirstName(txtFirstName.getText().toString());
            person.setLastName(txtLastName.getText().toString());
            //person.setBirthdate();
            person.setNationality(txtNationality.getText().toString());
            person.setHeight(Double.valueOf(txtHeight.getText().toString()));
            person.setHairColor(txtHairColor.getText().toString());
            person.setWeight(Double.valueOf(txtWeight.getText().toString()));

            person.setGender(spinnerSex.getSelectedItem().toString());

            // Get the current location
            String loc = "";
            Location location = gps.getLocation();
            if (location != null) {
                loc = String.valueOf(location.getLatitude());
                loc += "," + String.valueOf(location.getLongitude());
            }

            Log.d(TAG, "blarhe");
            Log.d(TAG, loc);

            // Set the persons location
            if ( loc.length() > 0 )
                person.setGpsLocation(loc);

            if (imageUri != null) {
                fbm.addPersonsImage(imageUri, person);
                person.hasImage(true);
            }

            // Add the new person
            fbm.addNewPerson(person);

            // On success, call function to end the activity
            endCurrentActivity();
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
