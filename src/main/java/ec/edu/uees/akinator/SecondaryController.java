package ec.edu.uees.akinator;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modelo.BT;

public class SecondaryController implements Initializable {

    @FXML
    private Label lblAnimal;

    @FXML
    private ImageView imageView;

    @FXML
    private BorderPane borderPane;
    @FXML
    private Button botonNo;

    @FXML
    private Button botonSi;

    private String nombreAnimal;
    private String imageUrl;
    private BT<String> arbol;

    @FXML
    private ImageView imgGenio;

    Animacion animacion = new Animacion();

    // Método estático para recibir los datos antes de cargar la escena
    //REVISAR ESTE CONSTRUCTOR
    public void setAnimalInfo(String nombre, String url, BT<String> arbolRecibido) {
        this.nombreAnimal = nombre;
        this.imageUrl = url;
        this.arbol = arbolRecibido;

        cargarDatos();
    }

    // Método para actualizar la UI con los datos recibidos
    private void cargarDatos() {

        if (lblAnimal != null) {
            lblAnimal.setText("¿El animal en el que piensas es: " + nombreAnimal + "?");
        }

        if (imageUrl != null && !imageUrl.equals("No se encontró una imagen.") && !imageUrl.startsWith("Error")) {
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() {
                    return new Image(imageUrl, true);
                }
            };

            loadImageTask.setOnSucceeded(event -> {
                imageView.setImage(loadImageTask.getValue());
                imageView.setFitWidth(300);
                imageView.setPreserveRatio(true);

                VBox vbox = new VBox(imageView);
                vbox.setAlignment(Pos.CENTER); // Asegura que la imagen esté centrada
                vbox.setStyle("-fx-background-color: white; -fx-background-radius: 15;"); // Fondo blanco para la imagen

                // Aplicar el borde al VBox (que envuelve la imagen)
                vbox.setBorder(new Border(new BorderStroke(
                        Color.BLACK, // Color del borde
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(10), // Esquinas redondeadas
                        new BorderWidths(10) // Grosor del borde
                )));

                // Ajustar el VBox al tamaño de la imagen para que el borde sea visible
                // Espaciado interno opcional para mejor apariencia
                vbox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

                // Colocar el VBox dentro del BorderPane
                borderPane.setCenter(vbox);
            });

            loadImageTask.setOnFailed(event -> {
                lblAnimal.setText(lblAnimal.getText() + "\n(No se pudo cargar la imagen)");
            });

            new Thread(loadImageTask).start(); // Inicia la tarea en un hilo separado
        } else {
            lblAnimal.setText(lblAnimal.getText() + "\n(No se encontró imagen)");
        }
    }

    public String getNombreAnimal() {
        return nombreAnimal;
    }

    public BT<String> getArbol() {
        return arbol;
    }

    @FXML
    void preguntarIncorrecto(MouseEvent event) throws IOException {
        switchToTertiary();
    }

    @FXML
    private void sonidoVictoria() {
        SoundFX.play("auidio_victoria.mp3");

    }

    @FXML
    void preguntarCorrecto(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("¡Adivinaste!");
        alert.setHeaderText("¡Genial! Adiviné tu animal.");
        alert.setContentText("¿Quieres jugar de nuevo?");

        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(botonSi, botonNo);

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonSi) {
            reiniciarJuego();  // Reinicia el juego
        } else {
            despedirseYSalir();  // Cierra la aplicación
        }
    }

// Reinicia el juego sin cambiar de escena
    private void reiniciarJuego() throws IOException {
        try {
            // Obtener la ventana actual y cerrarla
            Stage stageActual = (Stage) lblAnimal.getScene().getWindow();
            stageActual.close();

            // Cargar la escena inicial desde `primary.fxml`
            arbol.recargarDesdeArchivo("arbol.txt"); // Si no tienes este método, dime
            SoundFX.play("audio_select1.mp3");
            FXMLLoader loader = new FXMLLoader(App.class.getResource("SCgame.fxml"));
            Parent root = loader.load();

            // Crear un nuevo `Stage`
            Stage nuevoStage = new Stage();
            nuevoStage.getIcons().add(new Image(App.class.getResourceAsStream("icono.png")));
            nuevoStage.setTitle("Akinator Game");
            nuevoStage.setScene(new Scene(root));
            nuevoStage.setResizable(false); // Desactiva el cambio de tamaño
            nuevoStage.setMaximized(false);

            nuevoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// Muestra mensaje de despedida y cierra el programa
    private void despedirseYSalir() {
        SoundFX.play("audio_select1.mp3");
        Alert despedida = new Alert(Alert.AlertType.INFORMATION);
        despedida.setTitle("¡Gracias por jugar!");
        despedida.setHeaderText(null);
        despedida.setContentText("Gracias por jugar. ¡Hasta la próxima!");
        despedida.showAndWait();

        Stage stage = (Stage) lblAnimal.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void switchToTertiary() throws IOException {

        FXMLLoader loader = new FXMLLoader(App.class.getResource("SCpregunta.fxml"));

        if (loader == null) {
            System.out.println("Error: No se encontró tertiary.fxml");
            return;
        }

        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Obtener el controlador
        TertiaryController tertiaryController = loader.getController();
        if (tertiaryController == null) {
            System.out.println("Error: No se pudo obtener el controlador de TertiaryController.");
            return;
        }

        // Pasar datos a TertiaryController
        tertiaryController.setAnimalInfo(nombreAnimal, arbol);

        // Cambiar la escena en el mismo Stage
        Stage stage = (Stage) lblAnimal.getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false); // Desactiva el cambio de tamaño
        stage.setMaximized(false);

        stage.show();
        // System.out.println("Cambio de escena a Tertiary completado.");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        animacion.animacionGenio(imgGenio);
        Font.loadFont(App.class.getResourceAsStream("font/Minangkabau.TTF"), 20);

        // Aquí no tenemos los datos aún, solo preparamos la UI
        if (lblAnimal != null) {
            lblAnimal.setText("Cargando...");
        }

    }

}
