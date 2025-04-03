package ec.edu.uees.akinator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.BT;

public class AkinatorGame implements Initializable {

    @FXML
    private Button btnNo;

    @FXML
    private Button btnSi;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView imgGenio;

    @FXML
    private Label lblpregunta;

    @FXML
    private Label lblrespuesta;

    private List<Image> expresionesGenio = new ArrayList<>();
    private int contadorExpresion = 0;

    @FXML
    private ImageView imgHumo;

    private HumoAnimator humoAnimator;

    private BT<String> arbol;
    WikipediaImageFetcher imageWiki = new WikipediaImageFetcher();
    Animacion animacion = new Animacion();

    // Constructor vacío requerido por JavaFX
    public AkinatorGame() {
        arbol = new BT<>();
        arbol.construirDesdeArchivo("arbol.txt");

        if (arbol.obtenerPreguntaActual() == null) {
            System.out.println("Error: el árbol no cargó ninguna pregunta.");
        } else {
            //System.out.println("Pregunta inicial: " + arbol.obtenerPreguntaActual());
        }
    }

    @FXML
    private void sonidoSelect() {
        SoundFX.play("audio_select.mp3");
    }

    @FXML
    public void responderSi(MouseEvent event) throws IOException {
        cambiarGenioCiclico(); // Camb
        if (arbol.esNodoActualHoja()) {
            SoundFX.play("audio_select1.mp3");
            switchToSecondary();
        } else {
            arbol.moverNodoActual(true);
            actualizarPregunta();
        }
    }

    @FXML
    public void responderNo(MouseEvent event) throws IOException {
        cambiarGenioCiclico();
        if (arbol.esNodoActualHoja()) {
            SoundFX.play("audio_select1.mp3");

            switchToSecondary();

        } else {
            arbol.moverNodoActual(false);
            actualizarPregunta();
        }
    }

    private void actualizarPregunta() throws IOException {
        if (arbol.esNodoActualHoja()) {  //Si llegamos a una hoja, cambiar de escena
            SoundFX.play("audio_select1.mp3");
            switchToSecondary();

        } else {
            String pregunta = arbol.obtenerPreguntaActual();
            if (pregunta != null) {
                lblpregunta.setText(pregunta);
            }
        }
    }

    private void cambiarGenioCiclico() {
        contadorExpresion = (contadorExpresion + 1) % expresionesGenio.size();
        imgGenio.setImage(expresionesGenio.get(contadorExpresion));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        humoAnimator = new HumoAnimator(imgHumo);
        humoAnimator.play();

        for (int i = 1; i <= 10; i++) {
            String ruta = "genio_" + i + ".png";
            expresionesGenio.add(new Image(App.class.getResourceAsStream(ruta)));
        }

        // Mostrar imagen inicial
        imgGenio.setImage(expresionesGenio.get(0));
        animacion.animacionGenio(imgGenio);

        if (arbol.obtenerPreguntaActual() == null) {
            System.out.println("Error: nodoActual es null. Asegúrate de que el árbol se carga correctamente.");
        } else {
            try {
                actualizarPregunta();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void guardarJuego() throws IOException {
        arbol.guardarEnArchivo("arbol.txt");
    }

    @FXML
    private void switchToSecondary() throws IOException {
        Platform.runLater(() -> {
            try {
                if (lblpregunta == null || lblpregunta.getScene() == null) {
                    System.out.println("Error: lblpregunta aún no está en la escena.");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("SCconfirm.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                Stage stage = (Stage) lblpregunta.getScene().getWindow();

                boolean estabaMaximizada = stage.isMaximized();

                stage.setScene(scene);

                stage.setMaximized(estabaMaximizada);

                stage.show();

                SecondaryController controller = loader.getController();
                if (controller == null) {
                    System.out.println("Error: No se pudo obtener el controlador de SecondaryController.");
                    return;
                }

                // Pasar los datos
                String nombreAdivina = arbol.obtenerPreguntaActual();
                String imageUrl = imageWiki.getWikipediaImageUrl(nombreAdivina);
                controller.setAnimalInfo(nombreAdivina, imageUrl, arbol);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
