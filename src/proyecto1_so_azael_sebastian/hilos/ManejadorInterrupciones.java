/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.hilos;

import proyecto1_so_azael_sebastian.modelo.Planificador;

public class ManejadorInterrupciones extends Thread {
    private Planificador planificador;

    public ManejadorInterrupciones(Planificador planificador) {
        this.planificador = planificador;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(12000);
                planificador.interrupcionEmergencia();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}