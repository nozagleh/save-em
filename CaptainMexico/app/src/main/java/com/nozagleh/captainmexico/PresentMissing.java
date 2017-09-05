package com.nozagleh.captainmexico;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class PresentMissing extends AppCompatActivity
            implements FragmentListener {

    // Init static fragment tags
    private final String TAG_FRAG_LIST = "FRAG_LIST";
    private final String TAG_FRAG_MAP = "FRAG_MAP";

    protected final String SELECTED_FRAG = "SELECTED_FRAG";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startFragment(TAG_FRAG_LIST);
                    return true;
                case R.id.navigation_dashboard:
                    startFragment(TAG_FRAG_MAP);
                    return true;
                case R.id.navigation_notifications:

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

        Bundle extras = getIntent().getExtras();

        if (extras.getString(SELECTED_FRAG) != null) {
            String frag = extras.getString(SELECTED_FRAG);

            if (frag.equals("map"))
                startFragment(TAG_FRAG_MAP);
            else
                startFragment(TAG_FRAG_LIST);

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
        Fragment fragment = null;

        Fragment currentFrag = getSupportFragmentManager().findFragmentByTag(fragTag);

        switch (fragTag) {
            case TAG_FRAG_LIST:
                fragment = new ListPersonFragment();
            case TAG_FRAG_MAP:
                fragment = new MapPersonFragment();
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

}
