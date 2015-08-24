/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.leleonb.spotifystreamer;

import android.app.FragmentManager;
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
 * Contains the artist search fragment
 */
public class ArtistsActivity extends ActionBarActivity implements
        ArtistsActivityFragment.Callback, TracksActivityFragment.TracksCallback {

    private static final String TRACKSFRAGMENT_TAG = "TFTAG";
    private final String LOG_TAG = ArtistsActivity.class.getSimpleName();
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);

        if (findViewById(R.id.tracks_detail_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.tracks_detail_container, new TracksActivityFragment(), TRACKSFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_streamer, menu);
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
    public void onItemSelected(ArtistInfo artistInfo) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(ArtistsActivityFragment.KEY_ARTIST, artistInfo);

            TracksActivityFragment fragment = new TracksActivityFragment();
            fragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.tracks_detail_container, fragment, TRACKSFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, TracksActivity.class)
                    .putExtra(ArtistsActivityFragment.KEY_ARTIST, artistInfo);
            startActivity(intent);
        }
    }

    @Override
    public void onTrackSelected(int position, List<TrackInfo> tracks) {

        FragmentManager fragmentManager = getFragmentManager();
        NowPlayingFragment fragment = new NowPlayingFragment();

        if (mTwoPane) {
            Log.v(LOG_TAG, "Showing Dialog");

            Bundle args = new Bundle();
            args.putParcelableArrayList(TracksActivityFragment.KEY_TRACK, (ArrayList<? extends Parcelable>) tracks);
            args.putInt("position", position);
            fragment.setArguments(args);

            //TODO: Use resource (title)
            fragment.show(fragmentManager, "Now Playing");
        }
    }
}
