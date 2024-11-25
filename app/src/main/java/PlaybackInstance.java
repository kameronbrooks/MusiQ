import android.media.MediaPlayer;

public class PlaybackInstance {
    //Variables
    private MediaPlayer player;
    private float length;
    private float position;
    private boolean isPlaying;

    //Constructor
    public PlaybackInstance(MediaPlayer player, float length) {
        this.player = player;
        this.length = length;
        this.position = 0.0f;
        this.isPlaying = false;
    }

    //Methods
    //Start playback
    public void play() {
        player.start();
        isPlaying = true;
    }

    //Pause playback
    public void pause() {
        player.pause();
        position = player.getCurrentPosition();
        isPlaying = false;
    }

    //Stop playback
    public void stop() {
        player.stop();
        player.seekTo(0);
        position = 0.0f;
        isPlaying = false;
    }

    //Get current position percentage
    public float getPositionPercentage() {
        return (player.getCurrentPosition() / length) * 100.0f;
    }

    //Check if playing
    public boolean isPlaying() {
        return isPlaying;
    }
}
