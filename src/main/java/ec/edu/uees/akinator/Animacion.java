/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.uees.akinator;

import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 *
 * @author judit
 */
public class Animacion {
    
    public void animacionGenio1(ImageView image){

        TranslateTransition translate = new TranslateTransition();
        translate.setNode(image);
        translate.setDuration(Duration.millis(1000));
        translate.setCycleCount(TranslateTransition.INDEFINITE); // el ciclo es indefinido 
        translate.setByX(0); 
        translate.setByY(-10);
        translate.setAutoReverse(true);
        translate.play(); 
        
    }
    
    public void animacionGenio(ImageView image){
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(image);
        translate.setDuration(Duration.millis(1000));  
        translate.setCycleCount(TranslateTransition.INDEFINITE); 
        translate.setByX(0); 
        translate.setByY(-20);
        translate.setAutoReverse(true); 
        translate.play();
    }
    
}
