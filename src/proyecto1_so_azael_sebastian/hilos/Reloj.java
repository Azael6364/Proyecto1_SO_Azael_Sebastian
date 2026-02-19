/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.hilos;

import java.util.concurrent.Semaphore;
import proyectol_so_azael_sebastian.modelo.Planificador;

public class Reloj extends Thread {
    private int contadorCiclos;
    private boolean pausado;
    private int tiempoCiclo;
    private Semaphore mutex;
    private Planificador planificador; // Añadir esto

    public Reloj(int tiempoCiclo, Planificador planificador) {
        this.contadorCiclos = 0;
        this.tiempoCiclo = tiempoCiclo;
        this.pausado = false;
        this.mutex = new Semaphore(1);
        this.planificador = planificador; // Añadir esto
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!pausado) {
                    mutex.acquire();
                    contadorCiclos++;
                    
                    // Ordenar al planificador que ejecute un ciclo
                    if (planificador != null) {
                        planificador.ejecutarPaso();
                    }
                    
                    System.out.println("[RELOJ] Ciclo Global: " + contadorCiclos);
                    mutex.release();
                }
                Thread.sleep(tiempoCiclo);
            } catch (InterruptedException ex) {
                break;
            }
        }
    }
}