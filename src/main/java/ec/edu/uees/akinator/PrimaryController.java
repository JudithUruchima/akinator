package ec.edu.uees.akinator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class PrimaryController implements Initializable {

    @FXML
    private Button btnvol;
    @FXML
    private Label lbl;
    @FXML
    private VBox vboxGenio;
    @FXML
    private Button btnPlay;

    @FXML
    private ImageView imgHumo;

    private HumoAnimator humoAnimator;
    
    @FXML
    private ImageView imageAnimate;
    
    Animacion animacion = new Animacion();

    @FXML
    private void switchToSecondary() throws IOException {
        SoundFX.play("audio_select1.mp3");
        App.setRoot("SCgame");

    }
    private boolean musicaPausada = false;

    @FXML
    private void toggleMusica() {
        if (musicaPausada) {
            MusicPlayer.resumeMusic();
            SoundFX.setEfectosActivos(true);
            btnvol.setText("🔇"); // prendido
        } else {
            MusicPlayer.pauseMusic();
            SoundFX.setEfectosActivos(false);
            btnvol.setText("🔊");
        }
        musicaPausada = !musicaPausada;
    }

    @FXML
    private void sonidoSelect() {
        SoundFX.play("audio_select.mp3");

    }

    private void mostrarGenioConFade() {
        vboxGenio.setVisible(true);        // Mostrarlo
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), vboxGenio);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cargar la fuente personalizada

        Font.loadFont(App.class.getResourceAsStream("font/Minangkabau.TTF"), 20);

        lbl.setStyle("-fx-font-family: 'Minangkabau';");
        vboxGenio.setVisible(false); // Ocultar al inicio
        vboxGenio.setOpacity(0);

        mostrarGenioConFade();
        animacion.animacionGenio1(imageAnimate);
        
        humoAnimator = new HumoAnimator(imgHumo);

        humoAnimator.setOnFinished(() -> {
            imgHumo.setVisible(false); // Solo oculta el humo
            // vboxGenio.setVisible(true); !
        });

        humoAnimator.play();
    }
    
   

}
