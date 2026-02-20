
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
    private DefaultTableModel modeloTerminados;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
        configurarTablas();
        this.setLocationRelativeTo(null);
        
    // Instanciamos el planificador GLOBAL
     this.planificadorGlobal = new Planificador(); 

     // Iniciamos el Reloj (1000ms = 1 segundo)
     Reloj relojGlobal = new Reloj(1000, this.planificadorGlobal, this);
     relojGlobal.start();

        
    }

    private void configurarTablas() {
        // Configuramos las columnas para la Cola de Listos
        modeloListos = new DefaultTableModel();
        modeloListos.addColumn("ID");
        modeloListos.addColumn("Nombre");
        modeloListos.addColumn("Prioridad");
        modeloListos.addColumn("Deadline");
        tablaListos.setModel(modeloListos); // Conecta el modelo con tu tabla visual
        
        // Configuramos las columnas para la Cola de Bloqueados
        modeloBloqueados = new DefaultTableModel();
        modeloBloqueados.addColumn("ID");
        modeloBloqueados.addColumn("Nombre");
        modeloBloqueados.addColumn("Prioridad");
        modeloBloqueados.addColumn("Deadline");
        tablaBloqueados.setModel(modeloBloqueados);
        
        // Configuramos las columnas para la Cola de Terminados
        modeloTerminados = new DefaultTableModel();
        modeloTerminados.addColumn("ID");
        modeloTerminados.addColumn("Nombre");
        modeloTerminados.addColumn("Estado Final"); 
        tablaTerminados.setModel(modeloTerminados);
    }

    // Método que lee tu lista enlazada y pinta las filas en la tabla
    public void actualizarTablaListos(ColaProcesos cola) {
        modeloListos.setRowCount(0); // Limpia la tabla
        
        Nodo actual = cola.getInicio(); 
        while (actual != null) {
            Proceso p = actual.getContenido();
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getPrioridad(),
                p.getDeadline()
            };
            modeloListos.addRow(fila); // Agrega la fila visualmente
            actual = actual.getSiguiente();
        }
    }
    
    public void actualizarTablaBloqueados(ColaProcesos cola) {
        modeloBloqueados.setRowCount(0); // Limpia la tabla
        
        Nodo actual = cola.getInicio(); 
        while (actual != null) {
            Proceso p = actual.getContenido();
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getPrioridad(),
                p.getDeadline() // 
            };
            modeloBloqueados.addRow(fila); 
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
        
        // 4. Lee que proceso está en la CPU
        Proceso cpu = this.planificadorGlobal.getEnEjecucion();
        
        // 5. Muestra la info en pantalla
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(699, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(174, 174, 174))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReloj, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCpuDeadline, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCpuProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(159, 159, 159))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(167, 167, 167)
                        .addComponent(lblReloj, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(lblCpuProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblCpuDeadline, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(69, 69, 69))))
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
    private javax.swing.JButton btnGenerar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblCpuDeadline;
    private javax.swing.JLabel lblCpuProceso;
    private javax.swing.JLabel lblReloj;
    private javax.swing.JTable tablaBloqueados;
    private javax.swing.JTable tablaListos;
    private javax.swing.JTable tablaTerminados;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
