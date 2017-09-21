package com.nozagleh.captainmexico;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
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

    private Bitmap img;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Elements present for each person
        public TextView txtName, txtGender, txtStatus, txtDate;
        public ImageView imageViewPerson;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.lstName);
            txtDate = (TextView) itemView.findViewById(R.id.lstDate);
            txtStatus = (TextView) itemView.findViewById(R.id.lstStatus);
            txtGender = (TextView) itemView.findViewById(R.id.lstGender);
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
        Log.d(TAG, person.getFirstName() + ", " + person.get_ID());
        // Set the text fields
        final Resources res = context.getResources();
        holder.txtName.setText(res.getString(R.string.lst_name, person.getFirstName(), person.getLastName()));
        holder.txtGender.setText(person.getGenderString());

        holder.txtDate.setText("No date");
        if ( person.getMissingDate() != null ) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            holder.txtDate.setText(df.format(person.getMissingDate()));
        }

        if (person.getImgUrl() != null) {
            String url = "http://" + ToolBox.SERVER_ROOT + "/mex" + person.getImgUrl();

            final ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    img = response;
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, error.getMessage());
                }
            });

            ToolBox.queue.add(imageRequest);

            if (img != null)
                holder.imageViewPerson.setImageBitmap(img);
        }
    }

    @Override
    public int getItemCount() {
        return this.persons.size();
    }
}
