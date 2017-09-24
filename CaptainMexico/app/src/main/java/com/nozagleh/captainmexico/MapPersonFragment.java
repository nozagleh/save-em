package com.nozagleh.captainmexico;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListener} interface
 * to handle interaction events.
 */
public class MapPersonFragment extends Fragment
        implements OnMapReadyCallback {
    private static final String TAG = "MapPersonFragment";

    private View view;

    private FragmentListener mListener;

    private GoogleMap googleMap;
    //private MapView map;
    private SupportMapFragment map;

    private PermissionManager pm;
    private GPSManager gps;
    private Boolean gpsGranted;

    private ArrayList<Person> persons;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    public MapPersonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        persons = new ArrayList<>();
        mListener.updateList();
        persons = mListener.getPersons();

        // Set new Permission manager
        pm = new PermissionManager();

        // Set new GPS manager
        gps = new GPSManager(getContext());

        // Check if gps permission is granted
        gpsGranted = pm.gpsPermission(this.getActivity());

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map_person, container, false);

        //map = view.findViewById(R.id.missingMap);
        map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        Log.d(TAG, "lol");

        // Inflate the layout for this fragment
        return view;
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

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setScrollGesturesEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);

        try {
            if (gpsGranted) {
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(location.getLatitude(),
                                            location.getLongitude()), 15));
                        } else {

                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }

        addMarkers();
    }

    public void addMarkers() {
        persons = mListener.getPersons();
        if (googleMap == null || persons.isEmpty())
            return;
        for (Person person:persons) {
            if (person.getGpsLocation() != null) {
                String gpsLocation = person.getGpsLocation();
                String[] latLng = gpsLocation.split(",");

                LatLng personLoc = new LatLng(Double.valueOf(latLng[0]), Double.valueOf(latLng[1]));
                googleMap.addMarker(new MarkerOptions().position(personLoc).title(person.getFirstName() + " " + person.getLastName()));
            }
        }
    }
}
