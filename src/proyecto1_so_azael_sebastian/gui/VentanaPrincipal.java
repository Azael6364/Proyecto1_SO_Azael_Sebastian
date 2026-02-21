
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyecto1_so_azael_sebastian.gui;

import javax.swing.table.DefaultTableModel;
import proyecto1_so_azael_sebastian.hilos.Reloj;
import proyecto1_so_azael_sebastian.modelo.GeneradorProcesos; 
import proyecto1_so_azael_sebastian.estructuras.ColaProcesos;
import proyecto1_so_azael_sebastian.estructuras.Nodo;
import proyecto1_so_azael_sebastian.modelo.Proceso;
import proyecto1_so_azael_sebastian.modelo.Planificador;
/**
 *
 * @author COMPUGAMER
 */
public class VentanaPrincipal extends javax.swing.JFrame {
    
    private DefaultTableModel modeloListos;
    private DefaultTableModel modeloBloqueados;
    private Planificador planificadorGlobal;
    private DefaultTableModel modeloSuspendidos;
    private DefaultTableModel modeloTerminados;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
        configurarTablas();
        this.setLocationRelativeTo(null);
        
        this.planificadorGlobal = new Planificador(); 

        Reloj relojGlobal = new Reloj(1000, this.planificadorGlobal, this);
        relojGlobal.start();
        
        // (Asegúrate de que NO esté el ManejadorInterrupciones aquí)
    }

private void configurarTablas() {
        // 1. Configuramos las columnas para la Cola de Listos
        modeloListos = new DefaultTableModel();
        modeloListos.addColumn("ID");
        modeloListos.addColumn("Nombre");
        modeloListos.addColumn("Estado"); 
        modeloListos.addColumn("PC");     
        modeloListos.addColumn("MAR");    
        modeloListos.addColumn("Prioridad");
        modeloListos.addColumn("Deadline");
        tablaListos.setModel(modeloListos); 
        
        // 2. Configuramos las columnas para la Cola de Bloqueados
        modeloBloqueados = new DefaultTableModel();
        modeloBloqueados.addColumn("ID");
        modeloBloqueados.addColumn("Nombre");
        modeloBloqueados.addColumn("Estado"); 
        modeloBloqueados.addColumn("PC");     
        modeloBloqueados.addColumn("MAR");    
        modeloBloqueados.addColumn("Prioridad");
        modeloBloqueados.addColumn("Deadline");
        tablaBloqueados.setModel(modeloBloqueados);
        
        // 3. Configuramos las columnas para la Cola de Terminados 
        modeloTerminados = new DefaultTableModel();
        modeloTerminados.addColumn("ID");
        modeloTerminados.addColumn("Nombre");
        modeloTerminados.addColumn("Estado Final"); 
        tablaTerminados.setModel(modeloTerminados);
        
        // 4. Configuramos las columnas para la Cola de Suspendidos (SWAP)
        modeloSuspendidos = new DefaultTableModel();
        modeloSuspendidos.addColumn("ID");
        modeloSuspendidos.addColumn("Nombre");
        modeloSuspendidos.addColumn("Estado"); 
        modeloSuspendidos.addColumn("Prioridad");
        modeloSuspendidos.addColumn("Deadline");
        tablaSuspendidos.setModel(modeloSuspendidos);


}   

    // Método que lee tu lista enlazada y pinta las filas en la tabla
    // Método que lee tu lista enlazada y pinta las filas en la tabla de Listos
    
    public void actualizarTablaListos(ColaProcesos cola) {
        modeloListos.setRowCount(0); // Limpia la tabla
        
        Nodo actual = cola.getInicio(); 
        while (actual != null) {
            Proceso p = actual.getContenido();
            
            // EL PCB COMPLETO: Agregamos Estado, PC y MAR en el orden correcto
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getEstado(),
                p.getPC(),
                p.getMAR(),
                p.getPrioridad(),
                p.getDeadlineRestante() // Usamos el Restante para ver cómo baja el reloj
            };
            modeloListos.addRow(fila); // Agrega la fila visualmente
            actual = actual.getSiguiente();
        }
    }
    
    // Método que lee tu lista enlazada y pinta las filas en la tabla de Bloqueados
    public void actualizarTablaBloqueados(ColaProcesos cola) {
        modeloBloqueados.setRowCount(0); // Limpia la tabla
        
        Nodo actual = cola.getInicio(); 
        while (actual != null) {
            Proceso p = actual.getContenido();
            
            // EL PCB COMPLETO: Agregamos Estado, PC y MAR en el orden correcto
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getEstado(),
                p.getPC(),
                p.getMAR(),
                p.getPrioridad(),
                p.getDeadlineRestante() // Usamos el Restante para ver cómo baja el reloj
            };
            modeloBloqueados.addRow(fila); 
            actual = actual.getSiguiente();
        }
    }
    public void actualizarTablaSuspendidos(ColaProcesos cola) {
        modeloSuspendidos.setRowCount(0); 
        
        Nodo actual = cola.getInicio(); 
        while (actual != null) {
            Proceso p = actual.getContenido();
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getEstado(),
                p.getPrioridad(),
                p.getDeadlineRestante()
            };
            modeloSuspendidos.addRow(fila);
            actual = actual.getSiguiente();
        }
    }
    public void refrescarTablas() {
        // 1. Actualizamos la tabla de Listos 
        ColaProcesos colaListosReal = this.planificadorGlobal.getReadyQueue();
        actualizarTablaListos(colaListosReal);
        
        // 2. Actualiza la tabla de Bloqueados 
        ColaProcesos colaBloqueadosReal = this.planificadorGlobal.getBlockedQueue();
        actualizarTablaBloqueados(colaBloqueadosReal);
        
        // 3. Actualizamos la tabla de Terminados 
        ColaProcesos colaTerminadosReal = this.planificadorGlobal.getFinishedProcesses();
        actualizarTablaTerminados(colaTerminadosReal);
        
        // 4. Actualizamos la tabla de SWAP
        ColaProcesos colaSuspendidosReal = this.planificadorGlobal.getReadySuspended();
        actualizarTablaSuspendidos(colaSuspendidosReal);
        
        // 5. Lee que proceso está en la CPU
        Proceso cpu = this.planificadorGlobal.getEnEjecucion();
        
        // 6. Muestra la info en pantalla
        if (cpu != null) {
            this.lblCpuProceso.setText("Ejecutando: " + cpu.getNombre() + " (ID: " + cpu.getId() + ")");
            this.lblCpuDeadline.setText("Deadline Restante: " + cpu.getDeadline());
        } else {
            this.lblCpuProceso.setText("CPU: Inactiva");
            this.lblCpuDeadline.setText("Deadline: --");
        }
        // Lee el log de la CPU y lo pasa a la consola
        String nuevosEventos = this.planificadorGlobal.extraerEventos();
        
        if (!nuevosEventos.isEmpty()) {
            this.txtLog.append(nuevosEventos);
            this.txtLog.setCaretPosition(this.txtLog.getDocument().getLength()); 
        }
    }
    
    public void actualizarTablaTerminados(ColaProcesos cola) {
        modeloTerminados.setRowCount(0); 
        
        Nodo actual = cola.getInicio(); 
        while (actual != null) {
            Proceso p = actual.getContenido();
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getEstado() // Usamos el estado para saber si fallo o tuvo éxito
            };
            modeloTerminados.addRow(fila);
            actual = actual.getSiguiente();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblReloj = new javax.swing.JLabel();
        btnGenerar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaBloqueados = new javax.swing.JTable();
        lblCpuProceso = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaListos = new javax.swing.JTable();
        lblCpuDeadline = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaTerminados = new javax.swing.JTable();
        btnEmergencia = new javax.swing.JButton();
        cbxPoliticas = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaSuspendidos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1024, 600));

        jPanel1.setBackground(new java.awt.Color(15, 20, 35));
        jPanel1.setMinimumSize(new java.awt.Dimension(1024, 600));
        jPanel1.setPreferredSize(new java.awt.Dimension(1480, 720));

        lblReloj.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        lblReloj.setForeground(new java.awt.Color(0, 255, 0));
        lblReloj.setText("Ciclo Global: 0");

        btnGenerar.setText("Generar 20 Procesos");
        btnGenerar.addActionListener(this::btnGenerarActionPerformed);

        tablaBloqueados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaBloqueados);

        lblCpuProceso.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        lblCpuProceso.setForeground(new java.awt.Color(0, 255, 0));
        lblCpuProceso.setText("CPU: Inactiva");

        tablaListos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tablaListos);

        lblCpuDeadline.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        lblCpuDeadline.setForeground(new java.awt.Color(0, 255, 0));
        lblCpuDeadline.setText("Deadline: --");

        txtLog.setEditable(false);
        txtLog.setColumns(20);
        txtLog.setRows(5);
        jScrollPane3.setViewportView(txtLog);

        tablaTerminados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tablaTerminados);

        btnEmergencia.setText("¡EMERGENCIA! (Meteorito)");
        btnEmergencia.addActionListener(this::btnEmergenciaActionPerformed);

        cbxPoliticas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Round Robin", "FCFS", "SRT", "Prioridad", "EDF" }));
        cbxPoliticas.addActionListener(this::cbxPoliticasActionPerformed);

        tablaSuspendidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tablaSuspendidos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblReloj, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCpuProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblCpuDeadline, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(164, 164, 164)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cbxPoliticas, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(btnEmergencia, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane5)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(197, 197, 197))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(89, 89, 89)
                                .addComponent(lblReloj, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(lblCpuProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCpuDeadline, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(88, 88, 88)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEmergencia)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbxPoliticas)
                                .addGap(13, 13, 13))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(25, 25, 25))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed
        // 1. Instanciamos el generador
        GeneradorProcesos generador = new GeneradorProcesos();
        
        // 2. Creamos 20 procesos y los enviamos al Planificador Global
        for (int i = 0; i < 20; i++) {
            Proceso p = generador.crearProcesoAleatorio();
            this.planificadorGlobal.añadirProceso(p); // <-- Ahora entran a la CPU
        }
        
        // 3. Actualizamos la vista
        refrescarTablas();
        
        System.out.println("¡20 procesos enviados a la CPU!");
    }//GEN-LAST:event_btnGenerarActionPerformed

    private void btnEmergenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmergenciaActionPerformed
    // Cumplimos la rúbrica: La interrupción usa un Hilo (Thread) independiente
        Thread hiloInterrupcion = new Thread(new Runnable() {
            @Override
            public void run() {
                // 1. Creamos la Rutina de Servicio de Interrupción (ISR)
                GeneradorProcesos generador = new GeneradorProcesos();
                Proceso emergencia = generador.crearProcesoAleatorio();
                emergencia.setNombre("ISR_METEORITO");
                emergencia.setPrioridad(1); // Prioridad máxima para que la CPU lo atienda YA
                emergencia.setDeadline(15);
                
                // 2. Disparamos la interrupción en el Planificador
                planificadorGlobal.interrupcionEmergencia(emergencia);
                
                // 3. Forzamos la actualización visual
                refrescarTablas();
            }
        });
        
        hiloInterrupcion.start();    // TODO add your handling code here:
    }//GEN-LAST:event_btnEmergenciaActionPerformed

    private void cbxPoliticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxPoliticasActionPerformed
    // Le avisamos al planificador que cambie el algoritmo
        String politicaSeleccionada = cbxPoliticas.getSelectedItem().toString();
        planificadorGlobal.cambiarPolitica(politicaSeleccionada);
        planificadorGlobal.registrarEvento("⚙️ [SISTEMA] Política cambiada a: " + politicaSeleccionada);    // TODO add your handling code here:
    }//GEN-LAST:event_cbxPoliticasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
    
    public void actualizarReloj(int ciclo) {
    this.lblReloj.setText("Ciclo Global: " + ciclo);
    this.refrescarTablas();
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEmergencia;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JComboBox<String> cbxPoliticas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblCpuDeadline;
    private javax.swing.JLabel lblCpuProceso;
    private javax.swing.JLabel lblReloj;
    private javax.swing.JTable tablaBloqueados;
    private javax.swing.JTable tablaListos;
    private javax.swing.JTable tablaSuspendidos;
    private javax.swing.JTable tablaTerminados;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
