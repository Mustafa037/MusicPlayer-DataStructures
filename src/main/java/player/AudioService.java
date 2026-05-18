package player;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;

public class AudioService {

    private MediaPlayer mediaPlayer;

    public void play(String filePath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        File file = new File(filePath);
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void seek(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(seconds));
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public double getCurrentTime() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getCurrentTime().toSeconds();
    }

    public double getTotalDuration() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getTotalDuration().toSeconds();
    }
}