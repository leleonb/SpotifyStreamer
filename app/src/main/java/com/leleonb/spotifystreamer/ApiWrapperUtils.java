/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.leleonb.spotifystreamer;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Provides utility methods to handle the Spotify API Wrapper data.
 */
public class ApiWrapperUtils {

    public static final int THUMBNAIL_IMAGE_SIZE = 200;
    public static final int PLAYER_IMAGE_SIZE = 640;

    /** Extract an image with the suggested size for thumbnails. If not found returns the first image. */
    public static String extractThumbnailImage(List<Image> images) {

        if (!images.isEmpty()) {
            for(Image image : images) {
                if (image.height == THUMBNAIL_IMAGE_SIZE) {
                    return image.url;
                }
            }
            return images.get(0).url;
        }

        return "";
    }

    /** Extract an image with the suggested size for the music player. If not found returns the first image. */
    public static String extractPlayerImage(List<Image> images) {

        if (!images.isEmpty()) {
            for(Image image : images) {
                if (image.height == PLAYER_IMAGE_SIZE) {
                    return image.url;
                }
            }
            return images.get(0).url;
        }

        return "";
    }
}
