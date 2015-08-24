/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.leleonb.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Contains the top tracks list
 */
public class NowPlayingActivity extends ActionBarActivity {

    private final String LOG_TAG = NowPlayingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        if (savedInstanceState == null) {
            Log.v(LOG_TAG, "Creating player activity");
            Bundle args = new Bundle();
            args.putParcelableArrayList(TracksActivityFragment.KEY_TRACK, getIntent().getParcelableArrayListExtra(
                    TracksActivityFragment.KEY_TRACK));
            args.putInt("position", getIntent().getIntExtra("position", 0));

            NowPlayingFragment fragment = new NowPlayingFragment();
            fragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .add(R.id.tracks_detail_container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
