package com.g2.musiq;

import java.util.Date;
import java.util.List;

public class Playlist {
    public int id;
    public String name;
    public String description;
    public Date createdDate;
    public List<TrackInfo> tracks;
    public boolean isDynamic;

    public static Playlist current;
}
