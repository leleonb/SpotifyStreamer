/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.leleonb.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter to manage the Tracks views.
 */
public class TracksAdapter extends ArrayAdapter<TrackInfo> {

    public TracksAdapter(Activity context, List<TrackInfo> tracks) {
        super(context, 0, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrackInfo trackItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.track_item_visual_info,
                    parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.track_item_image);

        if (!trackItem.getSmallImageUrl().isEmpty()) {
            Picasso.with(parent.getContext())
                    .load(trackItem.getSmallImageUrl())
                    .resize(ApiWrapperUtils.THUMBNAIL_IMAGE_SIZE,
                            ApiWrapperUtils.THUMBNAIL_IMAGE_SIZE)
                    .into(imageView);
        }

        TextView mainTextView = (TextView) convertView.findViewById(R.id.track_item_text_main);
        mainTextView.setText(trackItem.getName());

        TextView secondaryTextView = (TextView) convertView.findViewById(
                R.id.track_album_item_text_main);
        secondaryTextView.setText(trackItem.getAlbumName());

        return convertView;
    }
}
