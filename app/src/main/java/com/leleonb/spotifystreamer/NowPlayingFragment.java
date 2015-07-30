package com.leleonb.spotifystreamer;

import android.app.DialogFragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;


/**
 * Fragment to play the selected track.
 */
public class NowPlayingFragment extends DialogFragment {

    private final String LOG_TAG = NowPlayingFragment.class.getSimpleName();
    private MediaPlayer mMediaPlayer;

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.v(LOG_TAG, "onCreate for Now Playing: ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_now_playing, container, false);

        TrackInfo trackInfo;

        Bundle arguments = getArguments();
        if (arguments != null) {
            trackInfo = arguments.getParcelable(TracksActivityFragment.KEY_TRACK);

            TextView artistView = (TextView) rootView.findViewById(R.id.track_player_text_artist);
            artistView.setText(trackInfo.getArtistName());
            TextView albumView = (TextView) rootView.findViewById(R.id.track_player_text_album);
            albumView.setText(trackInfo.getAlbumName());
            TextView trackView = (TextView) rootView.findViewById(R.id.track_player_text_track);
            trackView.setText(trackInfo.getName());

            ImageView imageView = (ImageView) rootView.findViewById(R.id.track_player_image);

            if (trackInfo.getLargeImageUrl() != null) {
                Picasso.with(getActivity())
                        .load(trackInfo.getLargeImageUrl())
                        .resize(ApiWrapperUtils.PLAYER_IMAGE_SIZE, ApiWrapperUtils.PLAYER_IMAGE_SIZE)
                        .into(imageView);
            }

            //TODO: Temporarily, just to play. Refactor
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(trackInfo.getPreviewUrl());
                Log.v(LOG_TAG, "Preview URL: " + trackInfo.getPreviewUrl());
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.v(LOG_TAG, "Start player: ");
                    mMediaPlayer.start();
                }
            });

            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.v(LOG_TAG, "Player error: " + what + "-" + extra);
                    return false;
                }
            });
        }

        return rootView;
    }
}
