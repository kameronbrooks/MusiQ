package com.g2.musiq;

public interface IStreamingService {
    PlaybackInstance getPlaybackInstance(TrackInfo req);
    boolean isReady();
}
