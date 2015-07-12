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
 * Adapter to manage the Artists views.
 */
public class ArtistsAdapter extends ArrayAdapter<ArtistInfo> {

    public ArtistsAdapter(Activity context, List<ArtistInfo> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ArtistInfo artistInfoItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.artist_item_visual_info, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.artist_item_image);

        if (!artistInfoItem.getImage().isEmpty()) {
            Picasso.with(parent.getContext())
                    .load(artistInfoItem.getImage())
                    .resize(ApiWrapperUtils.THUMBNAIL_IMAGE_SIZE, ApiWrapperUtils.THUMBNAIL_IMAGE_SIZE)
                    .into(imageView);
        }

        TextView mainTextView = (TextView) convertView.findViewById(R.id.artist_item_text_main);
        mainTextView.setText(artistInfoItem.getName());

        return convertView;
    }
}
