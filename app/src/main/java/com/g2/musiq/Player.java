package com.g2.musiq;

import android.media.MediaPlayer;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * The class that is responsible for playing the and managing the playlists. The PlayerActivity
 * interacts with this object to maintain the state of the current playlist and playback instance
 */
public class Player implements MediaPlayer.OnCompletionListener {

    private HashMap<String, IStreamingService> _playbackServices;
    private Playlist _currentPlaylist;
    private PlaybackInstance _currentPlaybackInstance;
    private int _currentPlaylistIndex;
    public boolean _shouldLoopSong;
    public boolean _shouldLoopPlaylist;


    private static Player _instance;
    public static Player getInstance() {
        return _instance;
    }
    public static Player createInstance() {
        if(_instance == null) {
            _instance = new Player();
        }
        return _instance;
    }

    private Player() {
        _playbackServices = new HashMap<String, IStreamingService>();

        // Automatically register a local streaming service
        registerPlaybackService("local", new LocalPlaybackService());
    }

    /**
     * Register a playback service that media can use to generate PlaybackInstances
     * @param name
     * @param service
     */
    public void registerPlaybackService(String name, IStreamingService service) {
        _playbackServices.put(name, service);
    }



    public void setPlaylist(Playlist playlist) {
        _currentPlaylist = playlist;
        if(!_currentPlaylist.tracks.isEmpty()) {
            TrackInfo track1 = _currentPlaylist.tracks.get(0);
            // TODO: catch errors if the streaming services are not registered
            playTrack(track1);
        }
    }

    private void playTrack(TrackInfo track) {
        try {
            IStreamingService service = _playbackServices.get(track.mediaSourceType);

            PlaybackInstance playbackInst = service.getPlaybackInstance(track);
            playbackInst.setOnCompletionListener(this);

            setPlaybackInstance(playbackInst);
        }
        catch (NullPointerException e) {
            Log.d("Musiq", "The streaming service " + track.mediaSourceType + " was not found" );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPlaybackInstance(PlaybackInstance inst) {
        if(_currentPlaybackInstance != null) {
            _currentPlaybackInstance.stop();
        }
        _currentPlaybackInstance = inst;
        inst.play();
    }

    public void playNextSong() {
        _currentPlaylistIndex++;

        if(_currentPlaylistIndex >= _currentPlaylist.tracks.size()) {
            _currentPlaylistIndex = 0;
        }
        playTrack(_currentPlaylist.tracks.get(_currentPlaylistIndex));

    }

    public void playPreviousSong() {
        _currentPlaylistIndex--;

        if(_currentPlaylistIndex < 0) {
            _currentPlaylistIndex = (_currentPlaylist.tracks.size() - 1);
        }
        playTrack(_currentPlaylist.tracks.get(_currentPlaylistIndex));

    }

    public void stop() {
        if(_currentPlaybackInstance == null) {
            return;
        }
        _currentPlaybackInstance.stop();
    }

    public void pause() {
        if(_currentPlaybackInstance == null) {
            return;
        }
        _currentPlaybackInstance.pause();
    }

    public void play() {
        if(_currentPlaybackInstance == null) {
            return;
        }
        _currentPlaybackInstance.play();
    }

    public boolean isPlaying() {
        return (_currentPlaybackInstance != null && _currentPlaybackInstance.isPlaying());
    }

    public PlaybackInstance getCurrentPlaybackInstance() {
        return _currentPlaybackInstance;
    }

    public float getCurrentPlaybackCompletionPercentage() {
        if (_currentPlaybackInstance == null) {
            return 0;
        }
        return _currentPlaybackInstance.getPositionPercentage();
    }

    public List<TrackInfo> getPlaylistTracks() {
        if (_currentPlaylist == null) {
            return null;
        }
        return _currentPlaylist.tracks;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO: add logic for adding more songs here or looping if that parameter is true
        playNextSong();
    }
}
