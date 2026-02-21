/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.hilos;

import proyecto1_so_azael_sebastian.modelo.Planificador;
import proyecto1_so_azael_sebastian.modelo.Proceso; 
import proyecto1_so_azael_sebastian.modelo.GeneradorProcesos; 

/**
 * Hilo concurrente e independiente que simula la llegada asincrona 
 * de eventos externos para evaluar el planificador.
 */
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
                Thread.sleep(12000);
                
                // Genera el proceso correspondiente a la Rutina de Servicio de Interrupcion (ISR)
                Proceso emergencia = generador.crearProcesoAleatorio();
                emergencia.setNombre("ISR_AUTOMATICO");
                emergencia.setPrioridad(1); 
                emergencia.setDeadline(15);
                
                planificador.interrupcionEmergencia(emergencia);
                
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}