/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.modelo;

/**
 * Clase encargada de generar procesos de manera automatica 
 * asignando valores aleatorios para evaluar la tolerancia y el estres del simulador.
 */
public class GeneradorProcesos {
    private int contadorId;

    public GeneradorProcesos() {
        this.contadorId = 1;
    }

    public void generar20Procesos(Planificador planificador) {
        for (int i = 0; i < 20; i++) {
            planificador.añadirProceso(crearProcesoAleatorio());
        }
    }

    public Proceso crearProcesoAleatorio() {
        int id = contadorId++;
        String nombre = "Proc_" + id;
        int instrucciones = (int) (Math.random() * 20) + 5; 
        int prioridad = (int) (Math.random() * 5) + 1;      
        int deadline = (int) (Math.random() * 300) + 100;   

        return new Proceso(id, nombre, instrucciones, prioridad, deadline);
    }
}