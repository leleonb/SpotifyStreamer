package com.leleonb.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class StreamerActivityFragment extends Fragment {

    private final String LOG_TAG = StreamerActivityFragment.class.getSimpleName();
    //TODO: Same fragment for both?
    private MusicInfoAdapter mArtistAdapter;

    public StreamerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_streamer, container, false);

        mArtistAdapter = new MusicInfoAdapter(
                getActivity(),
                new ArrayList<MusicInfo>());

        ListView artistsView = (ListView) rootView.findViewById(R.id.listview_artists);
        artistsView.setAdapter(mArtistAdapter);

        //TODO: Align with the results list / Visual contrast between both
        //TODO Improve the visuals of the search bar
        SearchView searchView = (SearchView) rootView.findViewById(R.id.search_artist);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        SpotifyApi api = new SpotifyApi();

                        SpotifyService spotify = api.getService();

                        //FIXME: App displays a message (for example, a toast) if the artist name/top tracks list for an artist is not found (asks to refine search)
                        //FIXME: App implements Artist Search + GetTopTracks API Requests (Using the Spotify wrapper or by making a HTTP request and deserializing the JSON data)
                        spotify.searchArtists("artist:" + query, new Callback<ArtistsPager>() {
                            @Override
                            public void success(ArtistsPager artistsPager, Response response) {
                                Log.v(LOG_TAG, "Artists: " + artistsPager.toString());

                                if (artistsPager.artists.total > 0) {
                                    mArtistAdapter.clear();

                                    for (Artist artist : artistsPager.artists.items) {
                                        String url = "";
                                        if (artist.images.size() > 0) {
                                            url = artist.images.get(0).url;
                                        }

                                        MusicInfo musicInfo = new MusicInfo(artist.name, url);

                                        //FIXME: App stores the most recent top tracks query results and their respective metadata (track name, artist name, album name) locally in list. The queried results are retained on rotation.
                                        mArtistAdapter.add(musicInfo);
                                    }
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d(LOG_TAG, "Failure retrieving artists." + error.toString());
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
                MusicInfo musicInfo = (MusicInfo) adapterView.getItemAtPosition(position);
                if (musicInfo != null) {
                    Log.v(LOG_TAG, "Artist " + position + " selected : " + musicInfo.getMainText());
                    //FIXME: [Phone] UI contains a screen for displaying the top tracks for a selected artist
                    //FIXME: Individual track layout contains - Album art thumbnail, track name, album name
                    //FIXME: When an artist is selected, app launches the “Top Tracks” View
                    //FIXME: App displays a list of top tracks
                }

            }
        });

        return rootView;
    }
}
