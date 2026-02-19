/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectol_so_azael_sebastian.modelo;

public class Proceso {
    private int id;
    private String nombre;
    private String estado; 
    private int pc;  
    private int mar; 
    private int prioridad;
    private int instruccionesTotales;
    private int instruccionesEjecutadas;
    private int deadlineRestante; 
    private int ciclosParaBloqueo; 
    private int ciclosEnBloqueoRestantes; 

    public Proceso(int id, String nombre, int instruccionesTotales, int prioridad, int deadline) {
        this.id = id;
        this.nombre = nombre;
        this.instruccionesTotales = instruccionesTotales;
        this.prioridad = prioridad;
        this.deadlineRestante = deadline;
        this.estado = "Nuevo";
        this.pc = 0;
        this.mar = 0;
        this.instruccionesEjecutadas = 0;
        this.ciclosParaBloqueo = (int) (Math.random() * 4) + 3; 
        this.ciclosEnBloqueoRestantes = 0;
    }

    public void ejecutarCiclo() {
        this.pc++;
        this.mar++;
        this.instruccionesEjecutadas++;
        this.ciclosParaBloqueo--;
    }

    public boolean debeBloquearse() {
        return ciclosParaBloqueo <= 0 && !esTerminado();
    }

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

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getPrioridad() { return prioridad; }
    public int getDeadlineRestante() { return deadlineRestante; }
    public int getInstruccionesRestantes() { return instruccionesTotales - instruccionesEjecutadas; }
    public int getPC() { return pc; }
    public int getMAR() { return mar; }
    public boolean esTerminado() { return instruccionesEjecutadas >= instruccionesTotales; }
}
