/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.hilos;

import proyecto1_so_azael_sebastian.modelo.Planificador;
import proyecto1_so_azael_sebastian.modelo.Proceso; 
import proyecto1_so_azael_sebastian.modelo.GeneradorProcesos; 

public class ManejadorInterrupciones extends Thread {
    private Planificador planificador;

    public ManejadorInterrupciones(Planificador planificador) {
        this.planificador = planificador;
    }

    @Override
    public void run() {
        GeneradorProcesos generador = new GeneradorProcesos();
        
        while (true) {
            try {
                // Espera 12 segundos (12000 milisegundos)
                Thread.sleep(12000);
                
                // 1. Creamos el proceso de emergencia (ISR)
                Proceso emergencia = generador.crearProcesoAleatorio();
                emergencia.setNombre("ISR_AUTOMATICO");
                emergencia.setPrioridad(1); // Prioridad máxima
                emergencia.setDeadline(15);
                
                // 2. Disparamos tu método nuevo (el que sí recibe un proceso)
                planificador.interrupcionEmergencia(emergencia);
                
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}