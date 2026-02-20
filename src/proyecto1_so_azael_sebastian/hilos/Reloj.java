/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.hilos;

import java.util.concurrent.Semaphore;
import proyecto1_so_azael_sebastian.modelo.Planificador;
import proyecto1_so_azael_sebastian.gui.VentanaPrincipal;

public class Reloj extends Thread {
    private int contadorCiclos;
    private boolean pausado;
    private int tiempoCiclo;
    private Semaphore mutex;
    private Planificador planificador;
    private VentanaPrincipal ventana;

    public Reloj(int tiempoCiclo, Planificador planificador, VentanaPrincipal ventana) {
        this.contadorCiclos = 0;
        this.tiempoCiclo = tiempoCiclo;
        this.pausado = false;
        this.mutex = new Semaphore(1);
        this.planificador = planificador; 
        this.ventana = ventana;
    }

   @Override
    public void run() {
        while (true) {
            try {
                if (!pausado) {
                    mutex.acquire();
                    contadorCiclos++;
                    
                    if (planificador != null) {
                        planificador.ejecutarPaso();
                    }
                    
                    // 3. ACTUALIZAR LA GUI
                    if (ventana != null) {
                        ventana.actualizarReloj(contadorCiclos);
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