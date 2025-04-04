package ec.edu.uees.akinator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

/**
 * JavaFX App
 */
public class App extends Application {

    static Scene scene;
    private static FXMLLoader loader;
//SCitro  SCconfirm  SCgame SCpregunta

    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(this.getClass().getResource("font/Marhaban_Ramadhan_DEMO.otf").toExternalForm(), 1);
        // Imprime todas las familias disponibles
        //System.out.println("Fuentes disponibles:");
        for (String fontName : Font.getFamilies()) {
            // System.out.println(fontName);
        }

        scene = new Scene(loadFXML("SCintro"));
        stage.setTitle("Akinator Game");

        stage.setScene(scene);
        stage.getIcons().add(new Image(App.class.getResourceAsStream("icono.png")));
        stage.setResizable(false); // Desactiva el cambio de tamaño
        stage.setMaximized(false); 
        stage.show();
        MusicPlayer.playBackgroundMusic();
    }

    static void setRoot(String fxml) throws IOException {
        loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = loader.load();
        scene.setRoot(root);
    }

    public static <T> T getController(Class<T> controllerClass) {
        return loader.getController();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return loader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
