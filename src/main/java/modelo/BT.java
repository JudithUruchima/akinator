package modelo;

import ec.edu.uees.akinator.AkinatorGame;
import ec.edu.uees.akinator.App;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javafx.scene.control.Alert;

public class BT<E> {

    // Clase interna para representar un nodo del árbol binario
    private static class Node<E> {

        private E data;
        private Node<E> left;
        private Node<E> right;

        private Node(E data) {
            this.data = data;
        }

        private boolean isLeaf() {
            return left == null && right == null;
        }

    }

    private Node<E> root;
    private Node<E> nodoActual; // Referencia al nodo en la partida actual

    public BT() {
        this.root = null;
    }

    public void construirDesdeArchivo(String nombreArchivo) {
        Stack<Node<E>> pila = new Stack<>();

        File archivo = new File(nombreArchivo);

        if (!archivo.exists()) {
            System.out.println("El archivo no existe. Se creará uno nuevo después.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Node<E> nuevoNodo = new Node<>((E) linea.trim());

                    // Si la línea contiene un '?', es una pregunta
                    if (linea.contains("?") || linea.contains("¿") && pila.size() >= 2) {
                        nuevoNodo.right = pila.pop(); // Respuesta negativa
                        nuevoNodo.left = pila.pop();  // Respuesta positiva
                    }
                    pila.push(nuevoNodo);
                }
            }

            if (!pila.isEmpty()) {
                root = pila.pop();
                nodoActual = root;
            }
            else{
                nodoActual = null;
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

    }

    public String obtenerPreguntaActual() {
        return nodoActual != null ? (String) nodoActual.data : null;
    }

    public boolean esNodoActualHoja() {
        return nodoActual != null && nodoActual.isLeaf();
    }

    public void moverNodoActual(boolean respuesta) {
        if (nodoActual != null) {
            nodoActual = respuesta ? nodoActual.right : nodoActual.left;
        }
    }

    public void reiniciarJuego() {
        nodoActual = root;
    }

    public void recargarDesdeArchivo(String nombreArchivo) {
        this.root = null; // Reiniciar el árbol
        construirDesdeArchivo(nombreArchivo); // Volver a cargar
    }

    public void guardarEnArchivo(String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(nombreArchivo), StandardCharsets.UTF_8))) {
            guardarPostOrden(root, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    private void guardarPostOrden(Node<E> nodo, PrintWriter writer) {
        if (nodo == null) {
            return;
        }
        guardarPostOrden(nodo.left, writer);
        guardarPostOrden(nodo.right, writer);
        writer.println(nodo.data);
    }

    public void agregarNuevoAnimal(String nuevaPregunta, String nuevoAnimal) {
        if (nodoActual == null || nodoActual.isLeaf()) {
            Node<E> nuevoNodo = new Node<>((E) nuevaPregunta);
            nuevoNodo.right = new Node<>((E) nuevoAnimal);
            nuevoNodo.left = nodoActual;

            if (nodoActual == root) {
                root = nuevoNodo;
            } else {
                reemplazarNodo(root, nodoActual, nuevoNodo);
            }

            nodoActual = nuevoNodo;
            guardarEnArchivo("arbol.txt");
        }
    }

    private boolean reemplazarNodo(Node<E> actual, Node<E> objetivo, Node<E> nuevo) {
        if (actual == null) {
            return false;
        }
        if (actual.left == objetivo) {
            actual.left = nuevo;
            return true;
        } else if (actual.right == objetivo) {
            actual.right = nuevo;
            return true;
        }
        return reemplazarNodo(actual.left, objetivo, nuevo) || reemplazarNodo(actual.right, objetivo, nuevo);
    }
}
