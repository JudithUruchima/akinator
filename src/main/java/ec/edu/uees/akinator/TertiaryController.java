package ec.edu.uees.akinator;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modelo.BT;

public class TertiaryController implements Initializable {

    @FXML
    private TextField txtNuevoAnimal;

    @FXML
    private TextField txtNuevaPregunta;

    @FXML
    private Button btnGuardar;

    @FXML
    private Label lblPregunta;

    @FXML
    private Label lblPreguntaBase;

    private String nombreAnimal;
    private BT<String> arbol;

    @FXML
    private VBox vboxbur;

    @FXML
    private VBox vboxlblpreg;

    @FXML
    private ImageView imgGenio;
    Animacion animacion = new Animacion();

    public void setAnimalInfo(String nombre, BT<String> arbolRecibido) {
        this.nombreAnimal = nombre;
        this.arbol = arbolRecibido;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        animacion.animacionGenio(imgGenio);

        Font.loadFont(App.class.getResourceAsStream("font/Minangkabau.TTF"), 20);

        // Ocultar VBoxes al inicio
        vboxbur.setVisible(false);
        vboxbur.setManaged(false);
        txtNuevaPregunta.setVisible(false);
        txtNuevaPregunta.setManaged(false);

        vboxlblpreg.setVisible(false);
        vboxlblpreg.setManaged(false);

        // Mostrar cuando se escribe en txtNuevaPregunta
        txtNuevoAnimal.textProperty().addListener((obs, oldVal, newVal) -> {
            boolean mostrar = !newVal.trim().isEmpty();

            vboxbur.setVisible(mostrar);
            vboxbur.setManaged(mostrar);

            txtNuevaPregunta.setVisible(mostrar);
            txtNuevaPregunta.setManaged(mostrar);

            vboxlblpreg.setVisible(mostrar);
            vboxlblpreg.setManaged(mostrar);
        });

        lblPreguntaBase.setText("¿En qué animal estás pensando?");

        txtNuevoAnimal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                lblPregunta.setText("¿En qué se diferencia el animal " + newValue + " de " + nombreAnimal + "?");
            }
        });

        Object controller = App.getController(Object.class
        );

        if (controller instanceof SecondaryController) {
            SecondaryController secondaryController = (SecondaryController) controller;
            this.nombreAnimal = secondaryController.getNombreAnimal();
            this.arbol = secondaryController.getArbol();
            //this.nodoActual = secondaryController.getNodoActual();
            System.out.println("Aprendiendo nuevo animal: " + nombreAnimal);
        } else {
            //System.out.println("Error: No se pudo obtener SecondaryController.");
        }
    }

    @FXML
    private void guardarNuevoAnimal() throws IOException {
        String nuevoAnimal = txtNuevoAnimal.getText().trim();
        String nuevaPregunta = txtNuevaPregunta.getText().trim();
        if (nuevoAnimal.isEmpty() || nuevaPregunta.isEmpty()) {
            mostrarAlerta("Error", "No puedes dejar los campos vacíos.", Alert.AlertType.ERROR);
            return;
        }

        if (!nuevaPregunta.startsWith("¿") || !nuevaPregunta.endsWith("?")) {
            mostrarAlerta("Error", "La pregunta debe comenzar con '¿' y terminar con '?'.", Alert.AlertType.ERROR);
            return;
        }

        // Validación: No permitir que el usuario escriba solo "?"
        if (nuevoAnimal.contains("¿") || (nuevoAnimal.contains("?"))) {
            mostrarAlerta("Error", "El nombre del animal no puede contener '¿?'.", Alert.AlertType.ERROR);
            return;
        }

        // Crear el nuevo nodo con la pregunta
        // Agregar el nuevo animal al árbol
        arbol.agregarNuevoAnimal(nuevaPregunta, nuevoAnimal);

        // Guardar los cambios en el archivo
        arbol.guardarEnArchivo("arbol.txt");

        // Volver a la pantalla principal
        preguntarReinicio();

    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void preguntarReinicio() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Akinator");
        alert.setHeaderText("¿Quieres jugar de nuevo?");

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

    private void reiniciarJuego() throws IOException {
        try {
            // Obtener la ventana actual y cerrarla
            Stage stageActual = (Stage) lblPreguntaBase.getScene().getWindow();
            stageActual.close();

            // Cargar la escena inicial desde `primary.fxml`
            arbol.recargarDesdeArchivo("arbol.txt"); // 
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
        Alert despedida = new Alert(Alert.AlertType.INFORMATION);
        despedida.setTitle("¡Gracias por jugar!");
        despedida.setHeaderText(null);
        despedida.setContentText("Gracias por jugar. ¡Hasta la próxima!");
        despedida.showAndWait();

        Stage stage = (Stage) lblPreguntaBase.getScene().getWindow();
        stage.close();
    }
}
