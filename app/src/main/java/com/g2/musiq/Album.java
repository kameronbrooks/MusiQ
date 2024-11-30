package com.g2.musiq;

import android.media.Image;

import java.util.Date;

public class Album {
    public int id;
    public String name;
    public Date releaseDate;
    public String description;
    public Image image;
    public String imageLocator;
    public TrackInfo[] tracks;

    public Artist artist;
}
