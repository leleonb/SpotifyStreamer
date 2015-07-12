/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.leleonb.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Artist info to store and pass to the adapter.
 */
public class ArtistInfo implements Parcelable {
    private String mId;
    private String mName;
    private String mImageUrl;

    public ArtistInfo(String id, String text, String image) {
        this.mId = id;
        this.mName = text;
        this.mImageUrl = image;
    }

    private ArtistInfo(Parcel parcel){
        this.mId = parcel.readString();
        this.mName = parcel.readString();
        this.mImageUrl = parcel.readString();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getImage() {
        return mImageUrl;
    }

    public void setImage(String image) {
        this.mImageUrl = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mImageUrl);
    }

    public static final Parcelable.Creator<ArtistInfo> CREATOR =
            new Parcelable.Creator<ArtistInfo>() {

        @Override
        public ArtistInfo createFromParcel(Parcel parcel) {
            return new ArtistInfo(parcel);
        }

        @Override
        public ArtistInfo[] newArray(int i) {
            return new ArtistInfo[i];
        }

    };
}
