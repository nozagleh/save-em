package com.nozagleh.captainmexico;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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

        uris = getUris();

        ToolBox.firebaseManager.db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate the persons list from Firebase

                for (DataSnapshot tempSnap : dataSnapshot.child("person").getChildren()) {
                    Person person = tempSnap.getValue(Person.class);
                    persons.add(person);
                }

                mAdapter = new PersonRecyclerViewAdapter(getContext(), persons, uris);
                rvPersons.setAdapter(mAdapter);

                RecyclerView.LayoutManager mLayout = new LinearLayoutManager(getActivity());
                rvPersons.setLayoutManager(mLayout);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d("Persons", String.valueOf(persons.size()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

    }

    public void refreshList() {
        uris = getUris();

        mAdapter.setUris(uris);
        mAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
    }

    public ArrayList<Uri> getUris() {
        return ToolBox.firebaseManager.getPersonsImageReference(this.persons);
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
