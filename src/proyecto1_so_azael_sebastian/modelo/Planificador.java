/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.modelo;

import proyecto1_so_azael_sebastian.estructuras.ColaProcesos;

public class Planificador {
    private ColaProcesos readyQueue;
    private ColaProcesos blockedQueue;
    private ColaProcesos readySuspended;
    private ColaProcesos blockedSuspended;
    private ColaProcesos finishedProcesses;
    private Proceso enEjecucion;
    private int maxRAM;
    private String politicaActual;
    private int quantum;
    private int contadorQuantum;
    private String bitacoraEventos = "";
    
    public Planificador() {
        this.readyQueue = new ColaProcesos();
        this.blockedQueue = new ColaProcesos();
        this.readySuspended = new ColaProcesos();
        this.blockedSuspended = new ColaProcesos();
        this.finishedProcesses = new ColaProcesos();
        this.maxRAM = 50; // 50 de RAM por defecto
        this.politicaActual = "Round Robin"; // Politica por defecto
        this.quantum = 3;
        this.contadorQuantum = 0;
        this.enEjecucion = null;
    }

    public Planificador(int maxRAM, String politicaInicial) {
        this.readyQueue = new ColaProcesos();
        this.blockedQueue = new ColaProcesos();
        this.readySuspended = new ColaProcesos();
        this.blockedSuspended = new ColaProcesos();
        this.finishedProcesses = new ColaProcesos();
        this.maxRAM = maxRAM;
        this.politicaActual = politicaInicial;
        this.quantum = 3;
        this.contadorQuantum = 0;
        this.enEjecucion = null;
    }

    public void ejecutarPaso() {
        gestionarBloqueados();
        gestionarSwap();
        
        if (enEjecucion != null) {
            enEjecucion.ejecutarCiclo();
            enEjecucion.reducirDeadline();
            contadorQuantum++;
            
            if (enEjecucion.esTerminado()) {
                enEjecucion.setEstado("Terminado");
                registrarEvento("[EXITO] El " + enEjecucion.getNombre() + " finalizo sus instrucciones.");
                finishedProcesses.encolar(enEjecucion);
                enEjecucion = null;
                contadorQuantum = 0;
            } else if (enEjecucion.debeBloquearse()) {
                enEjecucion.iniciarBloqueo();
                registrarEvento("[ALERTA] El " + enEjecucion.getNombre() + " solicito E/S (Va a Bloqueados).");
                blockedQueue.encolar(enEjecucion);
                enEjecucion = null;
                contadorQuantum = 0;
            } else {
                verificarPreempcion();
            }
        }
        
        if (enEjecucion == null && !readyQueue.esVacia()) {
            enEjecucion = readyQueue.desencolar();
            enEjecucion.setEstado("Ejecucion");
            contadorQuantum = 0;
        }
    }

    private void verificarPreempcion() {
        if (politicaActual.equals("Round Robin") && contadorQuantum >= quantum) {
            enEjecucion.setEstado("Listo");
            añadirAReady(enEjecucion);
            enEjecucion = null;
        } else if (politicaActual.equals("SRT") && !readyQueue.esVacia()) {
            if (readyQueue.verPrimero().getInstruccionesRestantes() < enEjecucion.getInstruccionesRestantes()) {
                enEjecucion.setEstado("Listo");
                readyQueue.encolarSRT(enEjecucion);
                enEjecucion = null;
            }
        } else if (politicaActual.equals("Prioridad") && !readyQueue.esVacia()) {
            if (readyQueue.verPrimero().getPrioridad() < enEjecucion.getPrioridad()) {
                enEjecucion.setEstado("Listo");
                readyQueue.encolarPrioridad(enEjecucion);
                enEjecucion = null;
            }
        } else if (politicaActual.equals("EDF") && !readyQueue.esVacia()) {
            if (readyQueue.verPrimero().getDeadlineRestante() < enEjecucion.getDeadlineRestante()) {
                enEjecucion.setEstado("Listo");
                readyQueue.encolarEDF(enEjecucion);
                enEjecucion = null;
            }
        }
    }

    private void gestionarBloqueados() {
        int tam = blockedQueue.getTamano();
        for (int i = 0; i < tam; i++) {
            Proceso p = blockedQueue.desencolar();
            p.reducirBloqueo();
            p.reducirDeadline();
            if (p.puedeDespertar()) {
                p.setEstado("Listo");
                añadirProceso(p);
            } else {
                blockedQueue.encolar(p);
            }
        }
    }

    private void gestionarSwap() {
        while (readyQueue.getTamano() + blockedQueue.getTamano() > maxRAM) {
            Proceso p = readyQueue.desencolar();
            if (p != null) {
                p.setEstado("Listo-Suspendido");
                readySuspended.encolar(p);
            }
        }
    }

    public void añadirProceso(Proceso p) {
        if (readyQueue.getTamano() + blockedQueue.getTamano() < maxRAM) {
            p.setEstado("Listo");
            añadirAReady(p);
        } else {
            p.setEstado("Listo-Suspendido");
            readySuspended.encolar(p);
        }
    }

    private void añadirAReady(Proceso p) {
        switch (politicaActual) {
            case "FCFS":
            case "Round Robin":
                readyQueue.encolar(p);
                break;
            case "SRT":
                readyQueue.encolarSRT(p);
                break;
            case "Prioridad":
                readyQueue.encolarPrioridad(p);
                break;
            case "EDF":
                readyQueue.encolarEDF(p);
                break;
        }
    }

    public synchronized void interrupcionEmergencia() {
        if (enEjecucion != null) {
            enEjecucion.setEstado("Listo");
            añadirAReady(enEjecucion);
            enEjecucion = null;
        }
    }

    public void cambiarPolitica(String nuevaPolitica) {
        this.politicaActual = nuevaPolitica;
        if (enEjecucion != null) {
            enEjecucion.setEstado("Listo");
            añadirAReady(enEjecucion);
            enEjecucion = null;
        }
    }
    
    public ColaProcesos getReadyQueue() {
     return this.readyQueue;
 }

 public ColaProcesos getBlockedQueue() {
     return this.blockedQueue;
 }
 
 public Proceso getEnEjecucion() {
     return this.enEjecucion;
 }
 
 public void registrarEvento(String mensaje) {
     this.bitacoraEventos += mensaje + "\n"; 
 }

 public String extraerEventos() {
     String eventos = this.bitacoraEventos;
     this.bitacoraEventos = ""; 
     return eventos;
 }
 
}
