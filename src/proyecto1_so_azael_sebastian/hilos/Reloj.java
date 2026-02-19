/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.hilos;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hilo que simula el reloj del sistema.
 * Mantiene el conteo de ciclos globales.
 * @author COMPUGAMER
 */

public class Reloj extends Thread {
    
    private int contadorCiclos;
    private boolean pausado;
    private int tiempoCiclo; // En milisegundos 
    
    // Referencia a la interfaz o controlador para notificar el cambio
    // private InterfazGrafica gui

    public Reloj(int tiempoCiclo) {
        this.contadorCiclos = 0;
        this.tiempoCiclo = tiempoCiclo;
        this.pausado = false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!pausado) {
                    // 1. Incrementar el reloj global
                    contadorCiclos++;
                    System.out.println("[RELOJ] Ciclo Global: " + contadorCiclos);
                    
                    // 2. AQUÍ CONECTAREMOS EL PLANIFICADOR (TODO)
                    // Planificador.ejecutarCiclo();
                    
                    // 3. AQUÍ ACTUALIZAREMOS LA GUI (TODO)
                    // gui.actualizarReloj(contadorCiclos);
                }
                
                // Simular la duración del ciclo
                Thread.sleep(tiempoCiclo);
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Metodos para controlar el tiempo desde la GUI
    public void pausar() {
        this.pausado = true;
    }

    public void reanudar() {
        this.pausado = false;
    }
    
    public int getCiclos() {
        return contadorCiclos;
    }
    
    public void setTiempoCiclo(int nuevoTiempo) {
        this.tiempoCiclo = nuevoTiempo;
    }
}