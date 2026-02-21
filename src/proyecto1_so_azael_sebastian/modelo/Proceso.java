/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_so_azael_sebastian.modelo;

/**
 * Clase que representa el Bloque de Control de Proceso (PCB).
 * almacena la informacion de estado, contadores de programa y parametros de planificacion.
 */
public class Proceso {
    private int id;
    private String nombre;
    private String estado; 
    private int pc;  
    private int mar; 
    private int prioridad;
    private int instruccionesTotales;
    private int instruccionesEjecutadas;
    private int deadline;
    private int deadlineRestante; 
    private int ciclosParaBloqueo; 
    private int ciclosEnBloqueoRestantes; 

    public Proceso(int id, String nombre, int instruccionesTotales, int prioridad, int deadline) {
        this.id = id;
        this.nombre = nombre;
        this.instruccionesTotales = instruccionesTotales;
        this.prioridad = prioridad;
        this.deadline = deadline;
        this.deadlineRestante = deadline;
        this.estado = "Nuevo";
        this.pc = 0;
        this.mar = 0;
        this.instruccionesEjecutadas = 0;
        this.ciclosParaBloqueo = (int) (Math.random() * 4) + 3; 
        this.ciclosEnBloqueoRestantes = 0;
    }

    /*
     * Simula la ejecucion lineal del proceso en el procesador.
     * se incrementan el PC y el MAR en una unidad por cada ciclo de instruccion.
     */
    public void ejecutarCiclo() {
        this.pc++;
        this.mar++;
        this.instruccionesEjecutadas++;
        this.ciclosParaBloqueo--;
    }

    public boolean debeBloquearse() {
        return ciclosParaBloqueo <= 0 && !esTerminado();
    }

    /*
     * Gestiona la transicion de estado hacia la cola de Bloqueados.
     * genera un tiempo de espera aleatorio simulando una operacion de Entrada/Salida.
     */
    public void iniciarBloqueo() {
        this.estado = "Bloqueado";
        this.ciclosEnBloqueoRestantes = (int) (Math.random() * 3) + 2; 
        this.ciclosParaBloqueo = (int) (Math.random() * 5) + 5; 
    }

    public void reducirBloqueo() {
        if (this.ciclosEnBloqueoRestantes > 0) this.ciclosEnBloqueoRestantes--;
    }

    public boolean puedeDespertar() {
        return ciclosEnBloqueoRestantes <= 0;
    }

    public void reducirDeadline() {
        if (this.deadlineRestante > 0) this.deadlineRestante--;
    }

    // GETTERS
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEstado() { return estado; }
    public int getPrioridad() { return prioridad; }
    public int getDeadlineRestante() { return deadlineRestante; }
    public int getInstruccionesRestantes() { return instruccionesTotales - instruccionesEjecutadas; }
    public int getPC() { return pc; }
    public int getMAR() { return mar; }
    public boolean esTerminado() { return instruccionesEjecutadas >= instruccionesTotales; }
    public int getDeadline() { return deadline; }

    // SETTERS 
    public void setEstado(String estado) { this.estado = estado; }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public void setPrioridad(int prioridad) { 
        this.prioridad = prioridad; 
    }
    
    public void setDeadline(int deadline) { 
        this.deadline = deadline; 
        this.deadlineRestante = deadline; 
    }
}







