package com.leleonb.spotifystreamer;

/**
 * Created by usuario on 05/07/2015.
 */
public class MusicInfo {
    //TODO: m prefix??
    private String mainText;
    private String imageUrl;

    public MusicInfo(String text, String image) {
        this.mainText = text;
        this.imageUrl = image;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }

}
