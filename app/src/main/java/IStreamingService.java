public interface IStreamingService {
    PlaybackInstance getPlaybackInstance(TrackInfo req);
    boolean isReady();
}
