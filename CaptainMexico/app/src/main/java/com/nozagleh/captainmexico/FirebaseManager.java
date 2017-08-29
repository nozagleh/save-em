package com.nozagleh.captainmexico;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by arnar on 2017-08-29.
 */

public class FirebaseManager {

    private final String TAG = "FirebaseManager";

    private DatabaseReference db;

    public FirebaseManager() {
        db = FirebaseDatabase.getInstance().getReference();
        addListener();
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
}
