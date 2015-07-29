/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.leleonb.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Fragment containing the top tracks of the selected artist.
 */
public class TracksActivityFragment extends Fragment {

    public static final String KEY_TRACKS = "tracks";
    public static final String TOP_TRACKS_COUNTRY_CODE = "US";

    private final String LOG_TAG = TracksActivityFragment.class.getSimpleName();

    private List<TrackInfo> mTracks;

    public TracksActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(ArtistsActivityFragment.KEY_ARTIST)) {
            String artistName = ((ArtistInfo)intent.getParcelableExtra(
                    ArtistsActivityFragment.KEY_ARTIST)).getName();
            ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(artistName);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_TRACKS)) {
            Log.v(LOG_TAG, "onCreate without saved: ");
            mTracks = new ArrayList<>();
        }
        else {
            Log.v(LOG_TAG, "onCreate: ");
            mTracks = savedInstanceState.getParcelableArrayList(KEY_TRACKS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_tracks, container, false);

        ArtistInfo artistInfo = null;

        Bundle arguments = getArguments();
        if (arguments != null) {
            artistInfo = arguments.getParcelable(ArtistsActivityFragment.KEY_ARTIST);
        }

        final TracksAdapter tracksAdapter = new TracksAdapter(
                getActivity(),
                mTracks);

        ListView tracksView = (ListView) rootView.findViewById(R.id.listview_tracks);
        tracksView.setAdapter(tracksAdapter);

        if (mTracks.isEmpty() && artistInfo != null) {
            final String artistId = artistInfo.getId();
            final String artistName = artistInfo.getName();

            Log.v(LOG_TAG, "Loading tracks for artist " + artistId);

            SpotifyApi api = new SpotifyApi();

            SpotifyService spotify = api.getService();

            Map<String, Object> options = new HashMap<>();
            options.put("country", TOP_TRACKS_COUNTRY_CODE);

            spotify.getArtistTopTrack(artistId, options, new Callback<Tracks>() {
                @Override
                public void success(Tracks tracks, Response response) {
                    tracksAdapter.clear();

                    if (!tracks.tracks.isEmpty()) {
                        for (Track track : tracks.tracks) {
                            String urlSmall = ApiWrapperUtils.extractThumbnailImage(
                                    track.album.images);
                            String urlLarge = ApiWrapperUtils.extractPlayerImage(
                                    track.album.images);

                            TrackInfo trackInfo = new TrackInfo(track.name, track.album.name,
                                    artistName, urlSmall, urlLarge, track.preview_url);

                            mTracks.add(trackInfo);
                        }

                    } else {

                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                R.string.tracks_no_results,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(LOG_TAG, R.string.tracks_retrieve_failure +
                            ": " + error.toString());

                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            R.string.tracks_retrieve_failure,
                            Toast.LENGTH_SHORT)
                            .show();
                }

            });
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_TRACKS, (ArrayList<? extends Parcelable>) mTracks);
        super.onSaveInstanceState(outState);
    }
}
