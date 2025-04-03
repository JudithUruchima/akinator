package ec.edu.uees.akinator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class HumoAnimator {

    private Runnable onFinished;

    private final Image[] frames;
    private int currentFrame = 0;
    private final Timeline animation;

    public HumoAnimator(ImageView targetImageView) {
        // Cargar imágenes
        frames = new Image[10];
        for (int i = 0; i < 10; i++) {
            frames[i] = new Image(App.class.getResourceAsStream("humoimg/h_" + (i + 1) + ".png"));
        }

        // Crear animación con Timeline
        animation = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            targetImageView.setImage(frames[currentFrame]);
            currentFrame = (currentFrame + 1) % frames.length;
        }));

        animation.setCycleCount(frames.length);
        animation.setOnFinished(e -> {
            targetImageView.setVisible(false); // Oculta el humo
            if (onFinished != null) {
                onFinished.run(); // Ejecuta lo que le pasaste desde el controller
            }
        });
    }

    public void play() {
        animation.play();
    }

    public void stop() {
        animation.stop();
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }
}
