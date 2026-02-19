/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectol_so_azael_sebastian.modelo;

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
        int instrucciones = (int) (Math.random() * 20) + 5; // Entre 5 y 25 instrucciones [cite: 42]
        int prioridad = (int) (Math.random() * 5) + 1;      // Prioridad 1-5 [cite: 42]
        int deadline = (int) (Math.random() * 50) + 10;    // Deadline 10-60 ciclos [cite: 42]

        return new Proceso(id, nombre, instrucciones, prioridad, deadline);
    }
}