/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.modelo;

import proyecto1_so_azael_sebastian.estructuras.ColaProcesos;

/**
 * Motor central del sistema operativo.
 * administra las colas de estados, aplica los algoritmos de planificacion
 * y gestiona las transiciones de los procesos en memoria.
 */
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
        this.maxRAM = 50; 
        this.politicaActual = "Round Robin"; 
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

    /*
     * Metodo principal de transicion de estados invocado por el reloj del sistema.
     * ejerce el control sobre la CPU evaluando si el proceso actual termino, fallo por deadline,
     * o si solicita una operacion de Entrada/Salida.
     */
    public void ejecutarPaso() {
        gestionarBloqueados();
        gestionarSwap();
        recuperarDeSwap();
        envejecerColasDeEspera();
        
        if (enEjecucion != null) {
            enEjecucion.ejecutarCiclo();
            enEjecucion.reducirDeadline();
            contadorQuantum++;
            
            if (enEjecucion.getDeadlineRestante() <= 0 && !enEjecucion.esTerminado()) {
                enEjecucion.setEstado("Fallido");
                registrarEvento("💥 [FALLO DE MISIÓN] " + enEjecucion.getNombre() + " no cumplió su Deadline.");
                finishedProcesses.encolar(enEjecucion);
                enEjecucion = null;
                contadorQuantum = 0;
            
            } else if (enEjecucion.esTerminado()) {
                enEjecucion.setEstado("Terminado");
                registrarEvento("✅ [EXITO] El " + enEjecucion.getNombre() + " finalizó sus instrucciones.");
                finishedProcesses.encolar(enEjecucion);
                enEjecucion = null;
                contadorQuantum = 0;
            } else if (enEjecucion.debeBloquearse()) {
                enEjecucion.iniciarBloqueo();
                registrarEvento("⚠️ [ALERTA] El " + enEjecucion.getNombre() + " solicitó E/S (Va a Bloqueados).");
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
            
            if (enEjecucion.getDeadlineRestante() <= 0) {
                enEjecucion.setEstado("Fallido");
                registrarEvento("💥 [FALLO POR ESPERA] " + enEjecucion.getNombre() + " expiró en la cola de Listos.");
                finishedProcesses.encolar(enEjecucion);
                enEjecucion = null;
            }
        }
    }

    /*
     * Logica de envejecimiento. Simula el paso del tiempo reduciendo el deadline
     * de todos los procesos que se encuentran esperando en RAM o en memoria secundaria (Swap).
     */
    private void envejecerColasDeEspera() {
        proyecto1_so_azael_sebastian.estructuras.Nodo actual = readyQueue.getInicio();
        while (actual != null) {
            actual.getContenido().reducirDeadline();
            actual = actual.getSiguiente();
        }
        actual = readySuspended.getInicio();
        while (actual != null) {
            actual.getContenido().reducirDeadline();
            actual = actual.getSiguiente();
        }
    }

    /*
     * Logica central de las politicas de planificacion preemptivas.
     * evalua constantemente si el proceso en ejecucion debe ser desalojado de la CPU
     * basado en el quantum (Round Robin) o si ha llegado un proceso mas apto (SRT, Prioridad, EDF).
     */
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

    /*
     * Transicion de estados por gestion de memoria (Swap-Out).
     * si el sistema supera la capacidad maxima de la RAM, mueve procesos hacia las colas de suspendidos,
     * priorizando desalojar aquellos procesos que se encuentren bloqueados.
     */
    private void gestionarSwap() {
        while (readyQueue.getTamano() + blockedQueue.getTamano() > maxRAM) {
            if (!blockedQueue.esVacia()) {
                Proceso p = blockedQueue.desencolar();
                p.setEstado("Bloqueado-Suspendido");
                blockedSuspended.encolar(p);
                registrarEvento("💾 [SWAP-OUT] Proceso " + p.getNombre() + " movido a Bloqueados-Suspendidos.");
            } else if (!readyQueue.esVacia()) {
                Proceso p = readyQueue.desencolar();
                p.setEstado("Listo-Suspendido");
                readySuspended.encolar(p);
                registrarEvento("💾 [SWAP-OUT] Proceso " + p.getNombre() + " movido a Listos-Suspendidos.");
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

    /*
     * Gestiona la llegada asincrona de interrupciones de hardware.
     * desaloja forzosamente al proceso en ejecucion y posiciona la Rutina de Servicio (ISR)
     * en la cola con la mas alta prioridad posible.
     */
    public synchronized void interrupcionEmergencia(Proceso emergencia) {
        registrarEvento("⚠️ [INTERRUPCION HARDWARE] Impacto detectado. Suspendiendo CPU...");
        
        if (enEjecucion != null) {
            registrarEvento("⚠️ Proceso " + enEjecucion.getNombre() + " interrumpido y devuelto a la cola.");
            enEjecucion.setEstado("Listo");
            añadirAReady(enEjecucion);
            enEjecucion = null;
        }
        
        emergencia.setEstado("Listo");
        this.readyQueue.encolarPrioridad(emergencia); 
        registrarEvento("🚨 Rutina de Emergencia encolada con prioridad maxima.");
    }

    public void cambiarPolitica(String nuevaPolitica) {
        this.politicaActual = nuevaPolitica;
        if (enEjecucion != null) {
            enEjecucion.setEstado("Listo");
            añadirAReady(enEjecucion);
            enEjecucion = null;
        }
    }
    
    public ColaProcesos getReadyQueue() { return this.readyQueue; }
    public ColaProcesos getBlockedQueue() { return this.blockedQueue; }
    public Proceso getEnEjecucion() { return this.enEjecucion; }
    
    public void registrarEvento(String mensaje) {
        this.bitacoraEventos += mensaje + "\n"; 
    }

    public String extraerEventos() {
        String eventos = this.bitacoraEventos;
        this.bitacoraEventos = ""; 
        return eventos;
    }
 
    public ColaProcesos getFinishedProcesses() { return this.finishedProcesses; }
    
    /*
     * Transicion de estados (Swap-In).
     * devuelve los procesos suspendidos en memoria secundaria a la memoria principal
     * cuando se detecta que hay espacio disponible en la RAM.
     */
    private void recuperarDeSwap() {
        while ((readyQueue.getTamano() + blockedQueue.getTamano()) < maxRAM && !readySuspended.esVacia()) {
            Proceso p = readySuspended.desencolar();
            p.setEstado("Listo");
            añadirAReady(p);
            registrarEvento("💾 [SWAP-IN] Proceso " + p.getNombre() + " rescatado del Disco y devuelto a la RAM.");
        }
    }

    public ColaProcesos getReadySuspended() { return this.readySuspended; }
}