package com.nozagleh.captainmexico;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by arnar on 2017-09-05.
 */

public class PersonRecyclerViewAdapter extends RecyclerView.Adapter<PersonRecyclerViewAdapter.ViewHolder> {
    private String TAG = "PersonRViewAdapter";

    // Init the context
    private Context context;

    // Init the list of persons
    private ArrayList<Person> persons;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Elements present for each person
        public TextView txtName;
        public TextView txtAge;
        public TextView txtMissingDate;
        public TextView txtStatus;
        public ImageView imageViewPerson;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtAge = (TextView) itemView.findViewById(R.id.txtHeight);
            txtMissingDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            imageViewPerson = (ImageView) itemView.findViewById(R.id.imgPerson);
        }
    }

    public PersonRecyclerViewAdapter(Context context, ArrayList<Person> persons) {
        // Set the variables passed with the constuctor
        this.context = context;
        this.persons = persons;
    }

    @Override
    public PersonRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate a custom view from XML
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_item, parent, false);

        // Return a new viewholder with the custom view
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PersonRecyclerViewAdapter.ViewHolder holder, int position) {
        // Set the current iterating person
        Person person = this.persons.get(position);
        Log.d(TAG, person.getName() + ", " + person.get_ID());
        // Set the text fields
        holder.txtName.setText(person.getName());
        //holder.txtAge.setText(String.valueOf(person.getAge()));
        //holder.txtMissingDate.setText(String.valueOf(person.getDateAdded()));
        holder.txtStatus.setText(person.getFound().toString());
    }

    @Override
    public int getItemCount() {
        return this.persons.size();
    }
}
