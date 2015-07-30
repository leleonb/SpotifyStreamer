/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.leleonb.spotifystreamer;


import android.os.Bundle;
import android.os.Parcelable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Fragment containing the artist search.
 */
public class ArtistsActivityFragment extends Fragment {

    public static final String KEY_ARTISTS = "artists";
    public static final String KEY_ARTIST = "artist";

    private final String LOG_TAG = ArtistsActivityFragment.class.getSimpleName();

    private List<ArtistInfo> mArtists;

    public ArtistsActivityFragment() {
    }

    //TODO: Rename
    public interface Callback {
        void onItemSelected(ArtistInfo artistInfo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_ARTISTS)) {
            mArtists = new ArrayList<>();
        } else {
            mArtists = savedInstanceState.getParcelableArrayList(KEY_ARTISTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_artists, container, false);

        final ArtistsAdapter artistAdapter = new ArtistsAdapter(
                getActivity(),
                mArtists);

        ListView artistsView = (ListView) rootView.findViewById(R.id.listview_artists);
        artistsView.setAdapter(artistAdapter);

        SearchView searchView = (SearchView) rootView.findViewById(R.id.search_artist);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.v(LOG_TAG, "Submit query: " + query);

                        SpotifyApi api = new SpotifyApi();

                        SpotifyService spotify = api.getService();

                        spotify.searchArtists("artist:" + query, new retrofit.Callback<ArtistsPager>() {
                            @Override
                            public void success(ArtistsPager artistsPager, Response response) {
                                artistAdapter.clear();

                                if (artistsPager.artists.total > 0) {
                                    for (Artist artist : artistsPager.artists.items) {
                                        String url = ApiWrapperUtils.extractThumbnailImage(
                                                artist.images);

                                        ArtistInfo artistInfo = new ArtistInfo(artist.id,
                                                artist.name, url);

                                        mArtists.add(artistInfo);
                                    }

                                } else {
                                    Toast.makeText(
                                            getActivity().getApplicationContext(),
                                            R.string.artists_no_results,
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d(LOG_TAG,
                                        R.string.artists_retrieve_failure + ": " + error.toString());

                                Toast.makeText(
                                        getActivity().getApplicationContext(),
                                        R.string.artists_retrieve_failure,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );

        artistsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ArtistInfo artistInfo = (ArtistInfo) adapterView.getItemAtPosition(position);
                if (artistInfo != null) {
                    ((Callback) getActivity())
                            .onItemSelected(artistInfo);
                }

            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_ARTISTS, (ArrayList<? extends Parcelable>) mArtists);
        super.onSaveInstanceState(outState);
    }
}
