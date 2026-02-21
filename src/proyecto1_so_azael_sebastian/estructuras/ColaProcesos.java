/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.estructuras;
import proyecto1_so_azael_sebastian.modelo.Proceso;

/**
 * Estructura de datos que gestiona los procesos en memoria 
 * sin utilizar colecciones nativas de java, cumpliendo con la restriccion.
 */

public class ColaProcesos {
    private Nodo inicio;
    private int tamano;

    public ColaProcesos() {
        this.inicio = null;
        this.tamano = 0;
    }

    public boolean esVacia() { return inicio == null; }
    public int getTamano() { return tamano; }
    
    /*
     * Algoritmo de insercion estandar utilizado por las politicas FCFS y Round Robin.
     * Inserta el nuevo proceso al final de la cola (First In, First Out).
     */
    
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

    /*
     * Algoritmo de insercion ordenada para la politica de Prioridad Estatica.
     * ubica el proceso comparando su nivel de prioridad para asegurar 
     * que los procesos mas criticos queden al frente de la cola.
     */
    
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

    /*
     * Algoritmo de insercion ordenada para la politica EDF (Earliest Deadline First).
     * el sistema evalua el tiempo limite restante de cada proceso y coloca
     * al frente aquel cuyo deadline este mas proximo a vencer.
     */
    
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

    /*
     * Algoritmo de insercion ordenada para la politica SRT (Shortest Remaining Time).
     * compara las instrucciones restantes de los procesos para posicionar al frente
     * al proceso que requiera menos tiempo de procesador para finalizar.
     */
    
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
    
   public Nodo getInicio() {
        return inicio;
    }
   
    public Proceso verPrimero() { return (inicio != null) ? inicio.getContenido() : null; }
}