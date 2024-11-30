package com.g2.musiq;

import android.media.Image;

public class TrackInfo {
    public int requestId;
    public long mediaId;
    public String mediaLocator;
    public String mediaSourceType;
    public String trackName;
    public float trackLengthSeconds;
    public Artist artist;
    public Album album;
    public long genreId;
    public int trackNumber;
    public Image image;
    public String lyrics;
    public float[] embedding;
    public int userRating;

    // Used to efficiently check for database conflicts
    public String getUID() {
        String ar = (artist != null) ? artist.name : "";
        String al = (album != null) ? album.name : "";

        return ar + "." + al + "." + trackName;

    }
}
