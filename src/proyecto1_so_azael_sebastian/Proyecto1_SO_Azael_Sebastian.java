/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto1_so_azael_sebastian;
import proyecto1_so_azael_sebastian.hilos.Reloj;

/**
 *
 * @author COMPUGAMER
 */
public class Proyecto1_SO_Azael_Sebastian {
    public static void main(String[] args) {
        // Crear el reloj con ciclos de 1 segundo (1000ms)
        Reloj relojSistema = new Reloj(1000);
        
        // Iniciar el hilo
        relojSistema.start();
        
        System.out.println("Sistema Operativo del Satélite Iniciado...");
    }
}