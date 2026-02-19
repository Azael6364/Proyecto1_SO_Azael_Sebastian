/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectol_so_azael_sebastian.estructuras;
import proyectol_so_azael_sebastian.modelo.Proceso;

public class ColaProcesos {
    private Nodo inicio;
    private int tamano;

    public ColaProcesos() {
        this.inicio = null;
        this.tamano = 0;
    }

    public boolean esVacia() { return inicio == null; }
    public int getTamano() { return tamano; }

    public void encolar(Proceso p) {
        Nodo nuevo = new Nodo(p);
        if (esVacia()) {
            inicio = nuevo;
        } else {
            Nodo temp = inicio;
            while (temp.getSiguiente() != null) temp = temp.getSiguiente();
            temp.setSiguiente(nuevo);
        }
        tamano++;
    }

    public void encolarPrioridad(Proceso p) {
        Nodo nuevo = new Nodo(p);
        if (esVacia() || p.getPrioridad() < inicio.getContenido().getPrioridad()) {
            nuevo.setSiguiente(inicio);
            inicio = nuevo;
        } else {
            Nodo actual = inicio;
            while (actual.getSiguiente() != null && actual.getSiguiente().getContenido().getPrioridad() <= p.getPrioridad()) {
                actual = actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
        tamano++;
    }

    public void encolarEDF(Proceso p) {
        Nodo nuevo = new Nodo(p);
        if (esVacia() || p.getDeadlineRestante() < inicio.getContenido().getDeadlineRestante()) {
            nuevo.setSiguiente(inicio);
            inicio = nuevo;
        } else {
            Nodo actual = inicio;
            while (actual.getSiguiente() != null && actual.getSiguiente().getContenido().getDeadlineRestante() <= p.getDeadlineRestante()) {
                actual = actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
        tamano++;
    }

    public void encolarSRT(Proceso p) {
        Nodo nuevo = new Nodo(p);
        if (esVacia() || p.getInstruccionesRestantes() < inicio.getContenido().getInstruccionesRestantes()) {
            nuevo.setSiguiente(inicio);
            inicio = nuevo;
        } else {
            Nodo actual = inicio;
            while (actual.getSiguiente() != null && actual.getSiguiente().getContenido().getInstruccionesRestantes() <= p.getInstruccionesRestantes()) {
                actual = actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
        tamano++;
    }

    public Proceso desencolar() {
        if (esVacia()) return null;
        Proceso p = inicio.getContenido();
        inicio = inicio.getSiguiente();
        tamano--;
        return p;
    }

    public Proceso verPrimero() { return (inicio != null) ? inicio.getContenido() : null; }
}