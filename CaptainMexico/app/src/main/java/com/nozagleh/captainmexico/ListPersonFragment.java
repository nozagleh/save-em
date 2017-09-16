package com.nozagleh.captainmexico;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListener} interface
 * to handle interaction events.
 * Use the {@link ListPersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListPersonFragment extends Fragment {
    // Fragment's tag
    private static final String TAG = "ListPersonFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    private FragmentListener mListener;
    private PersonRecyclerViewAdapter mAdapter;

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView rvPersons;

    private ArrayList<Person> persons;
    private ArrayList<Uri> uris;

    public ListPersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListPersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListPersonFragment newInstance(String param1, String param2) {
        ListPersonFragment fragment = new ListPersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_person, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshPersons);
        rvPersons = view.findViewById(R.id.rvPersons);
        persons = new ArrayList<>();

        fetchList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchList();
            }
        });

    }

    private void fetchList() {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = "http://192.168.1.139:8000/mex/persons/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject personsData = response.getJSONObject("persons");
                            for (int i = 0; i < personsData.length(); i++) {
                                Log.d("JSON LENGTH", String.valueOf(personsData.length()));
                                JSONObject personData = personsData.getJSONObject(String.valueOf(i));
                                Person person = new Person();
                                Log.d("JSON", personData.getString("id"));
                                Log.d("JSON FRISTNAME", personData.getString("firstname"));
                                Log.d("JSON LASTNAME", personData.getString("lastname"));
                                //person.set_ID(personData.getString("id"));
                                //person.setName(personData.getString("firstname") + personData.getString("lastname"));
                                Log.d("JSON FOUND", String.valueOf(personData.getInt("found")));
                                //person.setFound(personData.get("found"));
                                person.set_ID(personData.getString("id"));
                                person.setFirstName(personData.getString("firstname"));
                                person.setLastName(personData.getString("lastname"));
                                person.setGender(personData.getString("gender"));

                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                String date = personData.getString("birthday");

                                try {
                                    Date bday = dateFormat.parse(date);
                                    person.setBirthdate(bday);
                                } catch (ParseException e) {
                                    Log.d(TAG, e.getMessage());
                                }

                                try {
                                    Date missingDay = dateFormat.parse(personData.getString("missingDate"));
                                    person.setMissingDate(missingDay);
                                } catch (ParseException e) {
                                    Log.d(TAG, e.getMessage());
                                }

                                person.setHairColor(personData.getString("haircolor"));
                                person.setFound(personData.getInt("found"));
                                person.setHeight(personData.getDouble("height"));

                                persons.add(person);
                            }

                            RecyclerView.Adapter mAdapter = new PersonRecyclerViewAdapter(getContext(), persons);
                            rvPersons.setAdapter(mAdapter);

                            RecyclerView.LayoutManager mLayout = new LinearLayoutManager(getActivity());
                            rvPersons.setLayoutManager(mLayout);

                            swipeRefreshLayout.setRefreshing(false);

                        }catch (JSONException e) {
                            Log.d("JSON ERROR", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar noInternet = Snackbar.make(getActivity().findViewById(R.id.rvPersons),R.string.hint_no_internet,Snackbar.LENGTH_LONG);
                        noInternet.show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

        queue.add(jsonObjectRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
