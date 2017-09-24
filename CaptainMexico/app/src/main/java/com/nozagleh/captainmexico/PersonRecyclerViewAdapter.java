package com.nozagleh.captainmexico;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


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
        public CardView personCard;
        public TextView txtName, txtGender, txtStatus, txtDate;
        public ImageView imageViewPerson;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.lstName);
            txtDate = (TextView) itemView.findViewById(R.id.lstDate);
            txtStatus = (TextView) itemView.findViewById(R.id.lstStatus);
            txtGender = (TextView) itemView.findViewById(R.id.lstGender);
            imageViewPerson = (ImageView) itemView.findViewById(R.id.imgPerson);
            personCard = (CardView) itemView.findViewById(R.id.personCard);

            personCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("RV", "clicked");
                }
            });
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
        Log.d(TAG, person.getFirstName() + ", " + person.get_ID());
        // Set the text fields
        final Resources res = context.getResources();
        holder.txtName.setText(res.getString(R.string.lst_name, person.getFirstName(), person.getLastName()));
        holder.txtGender.setText(person.getGenderString());

        holder.txtDate.setText(R.string.txt_no_date);
        if ( person.getMissingDate() != null ) {
            DateFormat df = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
            String date = String.format(res.getString(R.string.txt_date),df.format(person.getMissingDate()));
            holder.txtDate.setText(date);
        }

        if (person.getFound() == 0)
            holder.txtStatus.setText(R.string.txt_not_found);
        else
            holder.txtStatus.setText(R.string.txt_found);

        if (person.hasImage()) {
            holder.imageViewPerson.setImageBitmap(person.getImg());
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, String.valueOf(this.persons.size()));
        return this.persons.size();
    }
}
