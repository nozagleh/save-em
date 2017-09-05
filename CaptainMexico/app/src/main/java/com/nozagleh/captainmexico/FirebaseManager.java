package com.nozagleh.captainmexico;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by arnar on 2017-08-29.
 */

public class FirebaseManager {

    private final String TAG = "FirebaseManager";

    private DatabaseReference db;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private StorageReference storageReference;

    private StorageReference personsImagesReference;

    public FirebaseManager() {
        db = FirebaseDatabase.getInstance().getReference();
        addListener();

        storageReference = storage.getReference();
        personsImagesReference = storageReference.child("person-images");
    }

    public void addNewPerson(Person person) {
        db.child("person").child(person.getID()).setValue(person);
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
                    Log.d(TAG, "Name: " + person.getName());
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
     * @param id Person's id number
     * @return StorageReference, a reference to the image
     */
    public StorageReference getPersonsImageReference(String id) {
        //String imgid = id + ".jpg";
        return personsImagesReference.child(id);
    }

    public void addPersonsImage(Uri image, Person person) {
        StorageReference newImage = personsImagesReference.child(person.getID() + ".jpg");
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
