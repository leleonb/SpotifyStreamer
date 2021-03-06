/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.leleonb.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the top tracks list
 */
public class TracksActivity extends ActionBarActivity implements
        TracksActivityFragment.TracksCallback{

    private final String LOG_TAG = TracksActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        if (savedInstanceState == null) {

            Bundle args = new Bundle();
            args.putParcelable(ArtistsActivityFragment.KEY_ARTIST, getIntent().getParcelableExtra(
                    ArtistsActivityFragment.KEY_ARTIST));

            TracksActivityFragment fragment = new TracksActivityFragment();
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

    @Override
    public void onTrackSelected(int position, List<TrackInfo> tracks) {
        Log.v(LOG_TAG, "Showing Dialog as new screen");

        Intent intent = new Intent(this, NowPlayingActivity.class)
                .putParcelableArrayListExtra(TracksActivityFragment.KEY_TRACK, (ArrayList<? extends Parcelable>) tracks)
                .putExtra("position", position);

        startActivity(intent);

    }
}
