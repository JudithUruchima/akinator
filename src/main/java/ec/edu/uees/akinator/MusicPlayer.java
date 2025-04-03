package ec.edu.uees.akinator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer {

    private static MediaPlayer mediaPlayer;

    public static void playBackgroundMusic() {
        if (mediaPlayer == null) {
            try {
                Media media = new Media(MusicPlayer.class.getResource("sounds/audio_fondo.mp3").toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Repetir
                mediaPlayer.setVolume(0.3);
                Timeline delay = new Timeline(new KeyFrame(Duration.millis(800), e -> {
                    mediaPlayer.play();
                }));
                delay.setCycleCount(1);
                delay.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public static void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public static void resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
}
