package ec.edu.uees.akinator;

import javafx.scene.media.AudioClip;

public class SoundFX {

    private static boolean efectosActivos = true;

    public static void setEfectosActivos(boolean activos) {
        efectosActivos = activos;
    }

    public static void play(String fileName) {
        if (!efectosActivos) {
            return; // si están desactivados, no suena nada
        }
        try {
            AudioClip clip = new AudioClip(SoundFX.class
                    .getResource("sounds/" + fileName)
                    .toExternalForm());
            clip.setVolume(0.6);
            clip.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
