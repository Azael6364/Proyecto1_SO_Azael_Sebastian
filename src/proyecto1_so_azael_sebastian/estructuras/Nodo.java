/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.estructuras;
import proyecto1_so_azael_sebastian.modelo.Proceso;

/**
 * Clase que representa un nodo individual para la implementacion 
 * de las listas enlazadas personalizadas del sistema.
 */

public class Nodo {
    private Proceso contenido;
    private Nodo siguiente;

    public Nodo(Proceso contenido) {
        this.contenido = contenido;
        this.siguiente = null;
    }

    public Proceso getContenido() { return contenido; }
    public void setContenido(Proceso contenido) { this.contenido = contenido; }
    public Nodo getSiguiente() { return siguiente; }
    public void setSiguiente(Nodo siguiente) { this.siguiente = siguiente; }
}
