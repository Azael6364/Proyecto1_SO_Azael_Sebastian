/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectol_so_azael_sebastian.estructuras;
import proyectol_so_azael_sebastian.modelo.Proceso;

/**
 *
 * @author COMPUGAMER
 */

public class ColaProcesos {
    private Nodo inicio;
    private Nodo fin;
    private int tamano;

    public ColaProcesos() {
        this.inicio = null;
        this.fin = null;
        this.tamano = 0;
    }

    // Método para ver si esta vacia
    public boolean esVacia() {
        return inicio == null;
    }

    // Insertar al final 
    public void encolar(Proceso p) {
        Nodo nuevo = new Nodo(p);
        if (esVacia()) {
            inicio = nuevo;
            fin = nuevo;
        } else {
            fin.setSiguiente(nuevo);
            fin = nuevo;
        }
        tamano++;
    }

    // Extraer del inicio 
    public Proceso desencolar() {
        if (esVacia()) return null;
        
        Proceso p = inicio.getContenido();
        inicio = inicio.getSiguiente();
        if (inicio == null) {
            fin = null;
        }
        tamano--;
        return p;
    }
    
    // Metodo para obtener el tamaño 
    public int getTamano() {
        return tamano;
    }

    // Método auxiliar para ver el primero sin sacarlo
    public Proceso verPrimero() {
        return (inicio != null) ? inicio.getContenido() : null;
    }
}