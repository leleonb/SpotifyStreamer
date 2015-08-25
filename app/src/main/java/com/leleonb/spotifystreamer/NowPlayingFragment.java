package com.leleonb.spotifystreamer;

import android.app.DialogFragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to play the selected track.
 */
public class NowPlayingFragment extends DialogFragment {

    private final String LOG_TAG = NowPlayingFragment.class.getSimpleName();

    private View mRootView;

    private MediaPlayer mMediaPlayer;

    private TrackInfo mCurrentTrack;
    private int mTrackNumber;
    private List<TrackInfo> mTracks;
    private int mCurrentPosition;

    private SeekBar mSeekBar;
    private ImageButton mPlayButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private Boolean mIsPlaying;
    private Handler mHandler = new Handler();

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    public void nextTrack() {
        mMediaPlayer.stop();

        if (mTrackNumber == mTracks.size() - 1) {
            mTrackNumber = 0;
        } else {
            mTrackNumber++;
        }

        mCurrentTrack = mTracks.get(mTrackNumber);
        mCurrentPosition = 0;
        setCurrentTrack();
    }

    public void previousTrack() {
        mMediaPlayer.stop();

        if (mTrackNumber == 0) {
            mTrackNumber = mTracks.size() - 1;
        } else {
            mTrackNumber--;
        }

        mCurrentTrack = mTracks.get(mTrackNumber);
        mCurrentPosition = 0;
        setCurrentTrack();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "on Create");
        if(savedInstanceState == null || !savedInstanceState.containsKey("time")) {
            mCurrentPosition = 0;
            mIsPlaying = true;
        } else {
            mCurrentPosition = savedInstanceState.getInt("time");
            mTrackNumber = savedInstanceState.getInt("position");
            mIsPlaying = savedInstanceState.getBoolean("playing");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "on CreateView");
        mRootView =  inflater.inflate(R.layout.fragment_now_playing, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mTracks = arguments.getParcelableArrayList(TracksActivityFragment.KEY_TRACK);

            if (mCurrentPosition == 0) {
                mTrackNumber = arguments.getInt("position");
            }

            mCurrentTrack = mTracks.get(mTrackNumber);

            setCurrentTrack();

            mSeekBar = (SeekBar) mRootView.findViewById(R.id.track_player_seek_bar);
            mPlayButton = (ImageButton) mRootView.findViewById(R.id.track_player_play);
            mPreviousButton = (ImageButton) mRootView.findViewById(R.id.track_player_previous);
            mNextButton = (ImageButton) mRootView.findViewById(R.id.track_player_next);

            mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMediaPlayer != null) {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                            ((ImageButton) v).setImageResource(android.R.drawable.ic_media_play);
                        } else {
                            mMediaPlayer.start();
                            ((ImageButton) v).setImageResource(android.R.drawable.ic_media_pause);
                        }
                    } else {
                        setPlayer(mCurrentTrack);
                        setSeeker();
                    }
                }
            });

            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousTrack();
                    setPlayer(mCurrentTrack);
                    setSeeker();
                }
            });

            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextTrack();
                    setPlayer(mCurrentTrack);
                    setSeeker();
                }
            });

            setPlayer(mCurrentTrack);
            setSeeker();
        }

        return mRootView;
    }

    private void setCurrentTrack() {
        TextView artistView = (TextView) mRootView.findViewById(R.id.track_player_text_artist);
        artistView.setText(mCurrentTrack.getArtistName());
        TextView albumView = (TextView) mRootView.findViewById(R.id.track_player_text_album);
        albumView.setText(mCurrentTrack.getAlbumName());
        TextView trackView = (TextView) mRootView.findViewById(R.id.track_player_text_track);
        trackView.setText(mCurrentTrack.getName());

        ImageView imageView = (ImageView) mRootView.findViewById(R.id.track_player_image);

        if (mCurrentTrack.getLargeImageUrl() != null) {
            Picasso.with(getActivity())
                    .load(mCurrentTrack.getLargeImageUrl())
                    .resize(ApiWrapperUtils.PLAYER_IMAGE_SIZE, ApiWrapperUtils.PLAYER_IMAGE_SIZE)
                    .into(imageView);
        }

        mPlayButton = (ImageButton) mRootView.findViewById(R.id.track_player_play);
        mPlayButton.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void setSeeker() {
        mSeekBar.setMax(30);
        mSeekBar.setProgress(mCurrentPosition);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.seekTo(progress * 1000);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mCurrentPosition = mMediaPlayer.getCurrentPosition() / 1000;
                    mSeekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 500);
            }
        });
    }

    private void setPlayer(TrackInfo trackInfo) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(trackInfo.getPreviewUrl());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in media player configuration", e);
        }

        mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.v(LOG_TAG, "Start player");
                mPlayButton = (ImageButton) mRootView.findViewById(R.id.track_player_play);
                mPlayButton.setImageResource(android.R.drawable.ic_media_pause);
                mMediaPlayer.seekTo(mCurrentPosition * 1000);
                mMediaPlayer.start();
                if (!mIsPlaying) {
                    mPlayButton.setImageResource(android.R.drawable.ic_media_play);
                    mMediaPlayer.pause();
                }
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayButton = (ImageButton) mRootView.findViewById(R.id.track_player_play);
                mPlayButton.setImageResource(android.R.drawable.ic_media_play);
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

    @Override
    public void onStop() {
        super.onStop();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.v(LOG_TAG, "Save state");
        outState.putParcelableArrayList(TracksActivityFragment.KEY_TRACK, (ArrayList<? extends Parcelable>) mTracks);
        outState.putInt("position", mTrackNumber);
        outState.putInt("time", mCurrentPosition);
        outState.putBoolean("playing", mMediaPlayer != null && mMediaPlayer.isPlaying());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        Log.v(LOG_TAG, "On resume");
        super.onResume();
        mPlayButton = (ImageButton) mRootView.findViewById(R.id.track_player_play);
        mPlayButton.setImageResource(android.R.drawable.ic_media_play);
    }
}
