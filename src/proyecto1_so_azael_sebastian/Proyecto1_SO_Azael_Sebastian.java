/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto1_so_azael_sebastian;

import proyecto1_so_azael_sebastian.modelo.GeneradorProcesos;
import proyecto1_so_azael_sebastian.modelo.Planificador;
import proyecto1_so_azael_sebastian.hilos.*;

public class Proyecto1_SO_Azael_Sebastian {
    public static void main(String[] args) {
        Planificador planificador = new Planificador(10, "FCFS");
        GeneradorProcesos generador = new GeneradorProcesos();
        
        generador.generar20Procesos(planificador);
        
        Reloj reloj = new Reloj(1000, planificador);
        ManejadorInterrupciones interrupciones = new ManejadorInterrupciones(planificador);
        
        reloj.start();
        interrupciones.start();
    }
}