package com.nozagleh.captainmexico;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PresentMissing extends AppCompatActivity
            implements FragmentListener {
    // Activity tag
    private static final String TAG = "PresentMissing";

    // Init static fragment tags
    private final String TAG_FRAG_LIST = "FRAG_LIST";
    private final String TAG_FRAG_MAP = "FRAG_MAP";
    private final String TAG_FRAG_RECENT = "FRAG_RECENT";

    protected final String SELECTED_FRAG = "SELECTED_FRAG";

    private ArrayList<Person> persons;

    private Fragment fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_list:
                    Log.d(TAG_FRAG_LIST, "Clicked list");
                    startFragment(TAG_FRAG_LIST);
                    return true;
                case R.id.navigation_recent:
                    startFragment(TAG_FRAG_RECENT);
                    return true;
                case R.id.navigation_map:
                    startFragment(TAG_FRAG_MAP);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_missing);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        persons = new ArrayList<>();
        fetchList();

        Bundle extras = getIntent().getExtras();

        if (extras.getString(SELECTED_FRAG) != null) {
            String frag = extras.getString(SELECTED_FRAG);

            if (frag != null && frag.equals("map"))
                startFragment(TAG_FRAG_MAP);
            else if (frag != null && frag.equals("list"))
                startFragment(TAG_FRAG_LIST);
            else if (frag != null && frag.equals("recent"))
                startFragment(TAG_FRAG_RECENT);

        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * Start a fragment based on a fragment tag
     * @param fragTag String fragment tag
     */
    private void startFragment(String fragTag) {
        FragmentManager fm = getSupportFragmentManager();
        fragment = null;

        Fragment currentFrag = getSupportFragmentManager().findFragmentByTag(fragTag);

        switch (fragTag) {
            case TAG_FRAG_LIST:
                fragment = new ListPersonFragment();
                break;
            case TAG_FRAG_RECENT:
                fragment = new ListPersonFragment();
                break;
            case TAG_FRAG_MAP:
                fragment = new MapPersonFragment();
                break;
        }

        if ( fragment != null && currentFrag != fragment)
            fm.beginTransaction()
                    .replace(R.id.frag_container, fragment, fragTag)
                    .commit();
    }

    /**
     * Finish the activity on navigate up
     * @return boolean true
     */
    @Override
    public boolean onSupportNavigateUp() {
        // Finish the activity
        finish();
        return true;
    }

    private void fetchList() {
        String url = "http://10.0.2.2:8000/mex/persons/";
        //String url = "http://192.168.1.139:8000/mex/persons/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject personsData = response.getJSONObject("persons");

                            if (!persons.isEmpty())
                                persons.clear();

                            for (int i = 0; i < personsData.length(); i++) {
                                final JSONObject personData = personsData.getJSONObject(String.valueOf(i));
                                final Person person = new Person();
                                person.set_ID(personData.getString("id"));
                                person.setFirstName(personData.getString("firstname"));
                                person.setLastName(personData.getString("lastname"));
                                person.setGender(personData.getInt("gender"));

                                if (personData.has("img_url")) {
                                    person.setImgUrl(personData.getString("img_url"));
                                    if (person.getImgUrl() != null) {
                                        String url = "http://" + ToolBox.SERVER_ROOT + "/mex" + person.getImgUrl();
                                        FetchPersons.getPersonImage(getApplicationContext(), url, new ImageResponseListener() {
                                            @Override
                                            public void onError(String message) {
                                                // TODO error
                                            }

                                            @Override
                                            public void onResponse(Bitmap response) {
                                                person.setImg(response);
                                                notifyListFragment();
                                            }
                                        });
                                    }
                                }

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
                                person.setGpsLocation(personData.getString("gpsLocation"));

                                persons.add(person);
                            }

                            notifyListFragment();
                        }catch (JSONException e) {
                            Log.d("JSON ERROR", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Snackbar noInternet = Snackbar.make(findViewById(R.id.rvPersons),R.string.hint_no_internet,Snackbar.LENGTH_LONG);
                        //noInternet.show();
                    }
                });

        ToolBox.queue.add(jsonObjectRequest);
    }

    @Override
    public ArrayList<Person> getPersons() {
        return this.persons;
    }

    @Override
    public void updateList() {
        fetchList();
    }

    public void notifyListFragment() {
        ListPersonFragment listFrag = (ListPersonFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAG_LIST);

        if (listFrag != null && listFrag.isVisible()) {
            listFrag.dataChange();
        }
    }
}
