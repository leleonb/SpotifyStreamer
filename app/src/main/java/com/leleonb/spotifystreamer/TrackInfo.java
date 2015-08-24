/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.leleonb.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Track info to store and pass to the adapter.
 */
public class TrackInfo implements Parcelable {

    private String mName;
    private String mAlbumName;
    private String mArtistName;
    private String mSmallImageUrl;
    private String mLargeImageUrl;
    private String mPreviewUrl;
    private long mDuration;

    public TrackInfo(String name, String albumName, String artistName, String smallImageUrl,
                     String largeImageUrl, String previewUrl, long duration) {
        this.mName = name;
        this.mAlbumName = albumName;
        this.mArtistName = artistName;
        this.mSmallImageUrl = smallImageUrl;
        this.mLargeImageUrl = largeImageUrl;
        this.mPreviewUrl = previewUrl;
        this.mDuration = duration;
    }

    private TrackInfo(Parcel parcel){
        this.mName = parcel.readString();
        this.mAlbumName = parcel.readString();
        this.mArtistName = parcel.readString();
        this.mSmallImageUrl = parcel.readString();
        this.mLargeImageUrl = parcel.readString();
        this.mPreviewUrl = parcel.readString();
        this.mDuration = parcel.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mAlbumName);
        dest.writeString(this.mArtistName);
        dest.writeString(this.mSmallImageUrl);
        dest.writeString(this.mLargeImageUrl);
        dest.writeString(this.mPreviewUrl);
        dest.writeLong(this.mDuration);
    }

    public static final Parcelable.Creator<TrackInfo> CREATOR =
            new Parcelable.Creator<TrackInfo>() {

        @Override
        public TrackInfo createFromParcel(Parcel parcel) {
            return new TrackInfo(parcel);
        }

        @Override
        public TrackInfo[] newArray(int i) {
            return new TrackInfo[i];
        }

    };

    public String getPreviewUrl() {
        return mPreviewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.mPreviewUrl = previewUrl;
    }

    public String getLargeImageUrl() {
        return mLargeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.mLargeImageUrl = largeImageUrl;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String albumName) {
        this.mAlbumName = albumName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getSmallImageUrl() {
        return mSmallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.mSmallImageUrl = smallImageUrl;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String artistName) {
        this.mArtistName = artistName;
    }

    public long getmDuration() {
        return mDuration;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }
}
