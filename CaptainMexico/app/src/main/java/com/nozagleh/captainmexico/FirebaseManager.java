package com.nozagleh.captainmexico;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by arnar on 2017-08-29.
 */

public class FirebaseManager {

    private final String TAG = "FirebaseManager";

    public DatabaseReference db;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private StorageReference storageReference;

    private StorageReference personsImagesReference;

    private FirebaseAuth mAuth;

    private ArrayList<Uri> uris = new ArrayList<>();

    public FirebaseManager() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.signInAnonymously();

        db = FirebaseDatabase.getInstance().getReference();
        //addListener();

        storageReference = storage.getReference();
        personsImagesReference = storageReference.child("person-images");
    }

    public void addNewPerson(Person person) {
        db.child("person").child(person.get_ID()).setValue(person);
    }

    public Person getPerson(Person person) {
        return new Person();
    }

    public Person getPersonByID(String ID) {
        return new Person();
    }

    public void addListener() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Iterate the persons list from Firebase
                for (DataSnapshot tempSnap : dataSnapshot.child("person").getChildren()) {
                    Person person = tempSnap.getValue(Person.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPerson:onCancelled", databaseError.toException());
            }
        };

        db.addValueEventListener(listener);
    }

    /**
     * Get a person's image reference
     * @param persons Arraylist of persons
     * @return StorageReference, a reference to the image
     */
    public ArrayList<Uri> getPersonsImageReference(ArrayList<Person> persons) {
        Log.d(TAG,persons.toString());
        for (Person person: persons) {
            final Person p = person;
            StorageReference sr = personsImagesReference.child(person.get_ID() + ".jpg");

            sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    uris.add(uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, p.get_ID());
                    Log.d(TAG, "Failed --> " + e.getMessage());
                }
            });
        }

        return uris;
    }

    public void addPersonsImage(Uri image, Person person) {
        StorageReference newImage = personsImagesReference.child(person.get_ID() + ".jpg");
        if ( image != null )
            newImage.putFile(image)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if ( taskSnapshot.getDownloadUrl() != null )
                            Log.d(TAG, taskSnapshot.getDownloadUrl().toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            });
    }
}
