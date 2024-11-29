package com.g2.musiq;

import android.media.MediaPlayer;

public class LocalPlaybackService implements IStreamingService {
    private boolean ready;

    public LocalPlaybackService() {
        this.ready = false;
    }

    //Get playback instance from local files
    @Override
    public PlaybackInstance getPlaybackInstance(TrackInfo req) {
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(req.mediaLocator);
            player.prepare();
            this.ready = true;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new PlaybackInstance(player, req.trackLengthSeconds);
    }

    @Override
    public boolean isReady() {
        return this.ready;
    }
}