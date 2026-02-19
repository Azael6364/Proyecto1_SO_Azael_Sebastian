/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectol_so_azael_sebastian.modelo;

/**
 * Representa el Bloque de Control de Proceso (PCB).
 * Cumple con: ID, Nombre, Status, PC, MAR, Prioridad, Deadline.
 * @author COMPUGAMER
 */

public class Proceso {
    private int id;
    private String nombre;
    private String estado; 
    
    // Registros simulados 
    private int pc;  // Program Counter
    private int mar; // Memory Address Register
    
    // Datos de Planificación
    private int prioridad;
    private int instruccionesTotales;
    private int instruccionesEjecutadas;
    private int deadline; 
    
    // Datos para Interrupciones/Bloqueo (E/S)
    private boolean esES; 
    private int ciclosParaGenerarExcepcion;
    private int ciclosParaSatisfacerExcepcion;

    // Constructor basico para crear procesos aleatorios
    public Proceso(int id, String nombre, int instruccionesTotales, int prioridad, int deadline) {
        this.id = id;
        this.nombre = nombre;
        this.instruccionesTotales = instruccionesTotales;
        this.prioridad = prioridad;
        this.deadline = deadline;
        
        // Valores iniciales por defecto
        this.estado = "Nuevo";
        this.pc = 0;
        this.mar = 0;
        this.instruccionesEjecutadas = 0;
    }

    // Metodo para simular ejecución de 1 ciclo (PC y MAR aumentan en 1)
    public void ejecutar() {
        this.pc++;
        this.mar++;
        this.instruccionesEjecutadas++;
    }
    
    // Getters y Setters 
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getPrioridad() { return prioridad; }
    public int getDeadline() { return deadline; }
    public int getInstruccionesRestantes() { return instruccionesTotales - instruccionesEjecutadas; }
    
    @Override
    public String toString() {
        return nombre + " (ID:" + id + ") - " + estado;
    }
}
