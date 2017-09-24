package com.nozagleh.captainmexico;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListener} interface
 * to handle interaction events.
 */
public class ListPersonFragment extends Fragment {
    // Fragment's tag
    private static final String TAG = "ListPersonFragment";

    private View view;

    private FragmentListener mListener;
    private PersonRecyclerViewAdapter mAdapter;

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView rvPersons;

    private ArrayList<Person> persons;

    private Boolean refreshing;

    public ListPersonFragment() {
        // Required empty public constructor
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
        refreshing = false;

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateListData();
            }
        });
    }

    public void updateListData() {
        mListener.updateList();
    }

    public void dataChange() {
        toggleRefresh();
        persons = mListener.getPersons();

        if (mAdapter == null)
            bindRvAdapter();
        mAdapter.notifyDataSetChanged();
        toggleRefresh();
    }

    public void bindRvAdapter() {
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(getActivity());
        rvPersons.setLayoutManager(mLayout);

        mAdapter = new PersonRecyclerViewAdapter(getContext(), persons);
        rvPersons.setAdapter(mAdapter);
    }

    public void toggleRefresh() {
        if (!refreshing)
            swipeRefreshLayout.setRefreshing(false);
        else
            swipeRefreshLayout.setRefreshing(true);
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
