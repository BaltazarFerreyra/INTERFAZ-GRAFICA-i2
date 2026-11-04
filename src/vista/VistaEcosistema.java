package vista;

import java.awt.CardLayout;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import controlador.Controlador;

public class VistaEcosistema extends javax.swing.JPanel {
    private Controlador controlador;
    private final DefaultListModel<String> eventosDelTurno = new DefaultListModel<>();

    public VistaEcosistema(String[] climas) {
        initComponents();
        cmbClimaInicial.setModel(new DefaultComboBoxModel<>(climas));
        cmbClimaNuevo.setModel(new DefaultComboBoxModel<>(climas));
        cmbEntidad.setModel(new DefaultComboBoxModel<>(new String[] {"Planta", "Conejo", "Lobo"}));
        lstEventos.setModel(eventosDelTurno);
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public void mostrarConfiguracion() {
        lblConfigEstado.setText("Todavía no hay una simulación en curso.");
        lblEstadoSimulacion.setText(" ");
        mostrarPantalla("configuracion");
    }

    public void mostrarSimulacion(String turno, String clima, int plantas, int conejos,
            int lobos, Object[][] entidades, List<String> eventos, boolean intervencion) {
        lblEstadoSimulacion.setText(" ");
        lblTurno.setText("Turno: " + turno);
        lblClima.setText("Clima: " + clima);
        lblPlantas.setText("Plantas: " + plantas);
        lblConejos.setText("Conejos: " + conejos);
        lblLobos.setText("Lobos: " + lobos);
        mostrarEstado(entidades, eventos, intervencion);
    }

    private void mostrarEstado(Object[][] entidades, List<String> eventos, boolean intervencion) {
        DefaultTableModel modeloTabla = (DefaultTableModel) tblEntidades.getModel();
        modeloTabla.setRowCount(0);
        for (Object[] entidad : entidades) {
            modeloTabla.addRow(entidad);
        }

        eventosDelTurno.clear();
        if (eventos.isEmpty()) {
            eventosDelTurno.addElement("Todavía no hay eventos para este turno.");
        }
        for (String evento : eventos) {
            eventosDelTurno.addElement(evento);
        }

        lblIntervencion.setText(intervencion
                ? "Intervención disponible" : "Intervención disponible cada tres turnos");
        cmbClimaNuevo.setEnabled(intervencion);
        cmbEntidad.setEnabled(intervencion);
        btnCambiarClima.setEnabled(intervencion);
        btnAgregarEntidad.setEnabled(intervencion);
        btnAvanzar.setEnabled(true);
        btnVerReporte.setEnabled(false);
        mostrarPantalla("simulacion");
    }

    public void mostrarReporte(String reporte) {
        txtReporte.setText(reporte);
        txtReporte.setCaretPosition(0);
        lblIntervencion.setText("La simulación terminó.");
        cmbClimaNuevo.setEnabled(false);
        cmbEntidad.setEnabled(false);
        btnCambiarClima.setEnabled(false);
        btnAgregarEntidad.setEnabled(false);
        btnAvanzar.setEnabled(false);
        btnVerReporte.setEnabled(true);
        mostrarPantalla("reporte");
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Revisá los datos", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        lblEstadoSimulacion.setText(mensaje);
    }

    public boolean confirmarNueva() {
        return JOptionPane.showConfirmDialog(this,
                "¿Querés empezar de nuevo? Se perderá la simulación guardada.",
                "Nueva simulación", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void mostrarPantalla(String pantalla) {
        ((CardLayout) getLayout()).show(this, pantalla);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlConfiguracion = new javax.swing.JPanel();
        pnlConfigEncabezado = new javax.swing.JPanel();
        lblConfigPaso = new javax.swing.JLabel();
        lblConfigTitulo = new javax.swing.JLabel();
        lblConfigAyuda = new javax.swing.JLabel();
        scrConfiguracion = new javax.swing.JScrollPane();
        pnlConfigCentro = new javax.swing.JPanel();
        pnlFormulario = new javax.swing.JPanel();
        lblPlantasIniciales = new javax.swing.JLabel();
        txtPlantas = new javax.swing.JTextField();
        lblConejosIniciales = new javax.swing.JLabel();
        txtConejos = new javax.swing.JTextField();
        lblLobosIniciales = new javax.swing.JLabel();
        txtLobos = new javax.swing.JTextField();
        lblClimaInicial = new javax.swing.JLabel();
        cmbClimaInicial = new javax.swing.JComboBox<>();
        lblTurnosIniciales = new javax.swing.JLabel();
        txtTurnos = new javax.swing.JTextField();
        pnlConfigAcciones = new javax.swing.JPanel();
        btnIniciar = new javax.swing.JButton();
        lblConfigEstado = new javax.swing.JLabel();
        pnlSimulacion = new javax.swing.JPanel();
        pnlSimEncabezado = new javax.swing.JPanel();
        lblSimPaso = new javax.swing.JLabel();
        lblSimTitulo = new javax.swing.JLabel();
        pnlContadores = new javax.swing.JPanel();
        lblTurno = new javax.swing.JLabel();
        lblClima = new javax.swing.JLabel();
        lblPlantas = new javax.swing.JLabel();
        lblConejos = new javax.swing.JLabel();
        lblLobos = new javax.swing.JLabel();
        pnlDatos = new javax.swing.JPanel();
        pnlEntidades = new javax.swing.JPanel();
        lblEntidades = new javax.swing.JLabel();
        scrEntidades = new javax.swing.JScrollPane();
        tblEntidades = new javax.swing.JTable();
        pnlEventos = new javax.swing.JPanel();
        lblEventos = new javax.swing.JLabel();
        scrEventos = new javax.swing.JScrollPane();
        lstEventos = new javax.swing.JList<>();
        pnlSimAcciones = new javax.swing.JPanel();
        lblIntervencion = new javax.swing.JLabel();
        pnlIntervencion = new javax.swing.JPanel();
        lblNuevoClima = new javax.swing.JLabel();
        cmbClimaNuevo = new javax.swing.JComboBox<>();
        btnCambiarClima = new javax.swing.JButton();
        lblTipoEntidad = new javax.swing.JLabel();
        cmbEntidad = new javax.swing.JComboBox<>();
        btnAgregarEntidad = new javax.swing.JButton();
        pnlSimNavegacion = new javax.swing.JPanel();
        btnAvanzar = new javax.swing.JButton();
        btnNueva = new javax.swing.JButton();
        btnVerReporte = new javax.swing.JButton();
        lblEstadoSimulacion = new javax.swing.JLabel();
        pnlReporte = new javax.swing.JPanel();
        pnlReporteEncabezado = new javax.swing.JPanel();
        lblReportePaso = new javax.swing.JLabel();
        lblReporteTitulo = new javax.swing.JLabel();
        lblReporteAyuda = new javax.swing.JLabel();
        scrReporte = new javax.swing.JScrollPane();
        txtReporte = new javax.swing.JTextArea();
        pnlReporteAcciones = new javax.swing.JPanel();
        btnNuevaDesdeReporte = new javax.swing.JButton();
        btnVolverDesdeReporte = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1010, 610));
        setLayout(new java.awt.CardLayout());
        pnlConfiguracion.setBackground(new java.awt.Color(240, 245, 241));
        pnlConfiguracion.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));
        pnlConfiguracion.setLayout(new java.awt.BorderLayout(14, 14));
        pnlConfigEncabezado.setOpaque(false);
        pnlConfigEncabezado.setLayout(new java.awt.GridLayout(3, 1, 0, 4));
        lblConfigPaso.setText("01  CONFIGURACIÓN");
        lblConfigPaso.setFont(new java.awt.Font("Dialog", 1, 16));
        lblConfigPaso.setForeground(new java.awt.Color(23, 57, 44));
        lblConfigTitulo.setText("Simulador de ecosistema");
        lblConfigTitulo.setFont(new java.awt.Font("Dialog", 1, 26));
        lblConfigTitulo.setForeground(new java.awt.Color(23, 57, 44));
        lblConfigAyuda.setText("Elegí las poblaciones, el clima y la duración para empezar.");
        scrConfiguracion.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scrConfiguracion.setOpaque(false);
        pnlConfigCentro.setBackground(new java.awt.Color(240, 245, 241));
        pnlConfigCentro.setLayout(new java.awt.BorderLayout(12, 12));
        pnlFormulario.setBackground(new java.awt.Color(255, 255, 255));
        pnlFormulario.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
        pnlFormulario.setLayout(new java.awt.GridLayout(5, 2, 12, 10));
        lblPlantasIniciales.setText("Plantas  ·  5 a 30");
        txtPlantas.setText("10");
        lblConejosIniciales.setText("Conejos  ·  2 a 15");
        txtConejos.setText("6");
        lblLobosIniciales.setText("Lobos  ·  1 a 5");
        txtLobos.setText("2");
        lblClimaInicial.setText("Clima inicial");
        lblTurnosIniciales.setText("Turnos  ·  10 a 50");
        txtTurnos.setText("20");
        pnlConfigAcciones.setOpaque(false);
        pnlConfigAcciones.setLayout(new java.awt.FlowLayout(0, 10, 7));
        btnIniciar.setText("Iniciar simulación");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });
        lblConfigEstado.setText("Todavía no hay una simulación en curso.");
        pnlSimulacion.setBackground(new java.awt.Color(240, 245, 241));
        pnlSimulacion.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));
        pnlSimulacion.setLayout(new java.awt.BorderLayout(14, 14));
        pnlSimEncabezado.setOpaque(false);
        pnlSimEncabezado.setLayout(new java.awt.BorderLayout(6, 8));
        lblSimPaso.setText("02  SIMULACIÓN");
        lblSimPaso.setFont(new java.awt.Font("Dialog", 1, 16));
        lblSimPaso.setForeground(new java.awt.Color(23, 57, 44));
        lblSimTitulo.setText("Ecosistema en curso");
        lblSimTitulo.setFont(new java.awt.Font("Dialog", 1, 26));
        lblSimTitulo.setForeground(new java.awt.Color(23, 57, 44));
        pnlContadores.setBackground(new java.awt.Color(255, 255, 255));
        pnlContadores.setBorder(javax.swing.BorderFactory.createEmptyBorder(11, 12, 11, 12));
        pnlContadores.setLayout(new java.awt.GridLayout(1, 5, 12, 10));
        lblTurno.setText("Turno 0");
        lblTurno.setFont(new java.awt.Font("Dialog", 1, 13));
        lblClima.setText("Clima");
        lblClima.setFont(new java.awt.Font("Dialog", 1, 13));
        lblPlantas.setText("Plantas: 0");
        lblPlantas.setFont(new java.awt.Font("Dialog", 1, 13));
        lblConejos.setText("Conejos: 0");
        lblConejos.setFont(new java.awt.Font("Dialog", 1, 13));
        lblLobos.setText("Lobos: 0");
        lblLobos.setFont(new java.awt.Font("Dialog", 1, 13));
        pnlDatos.setOpaque(false);
        pnlDatos.setLayout(new java.awt.GridLayout(1, 2, 12, 10));
        pnlEntidades.setOpaque(false);
        pnlEntidades.setLayout(new java.awt.BorderLayout(4, 5));
        lblEntidades.setText("Entidades vivas");
        lblEntidades.setFont(new java.awt.Font("Dialog", 1, 16));
        lblEntidades.setForeground(new java.awt.Color(23, 57, 44));
        tblEntidades.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"Tipo", "ID", "Edad", "Energía"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        pnlEventos.setOpaque(false);
        pnlEventos.setLayout(new java.awt.BorderLayout(4, 5));
        lblEventos.setText("Eventos del turno");
        lblEventos.setFont(new java.awt.Font("Dialog", 1, 16));
        lblEventos.setForeground(new java.awt.Color(23, 57, 44));
        pnlSimAcciones.setOpaque(false);
        pnlSimAcciones.setLayout(new java.awt.BorderLayout(5, 7));
        lblIntervencion.setText("Intervención disponible cada tres turnos");
        lblIntervencion.setFont(new java.awt.Font("Dialog", 1, 16));
        lblIntervencion.setForeground(new java.awt.Color(23, 57, 44));
        pnlIntervencion.setBackground(new java.awt.Color(255, 255, 255));
        pnlIntervencion.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4));
        pnlIntervencion.setLayout(new java.awt.FlowLayout(0, 10, 7));
        lblNuevoClima.setText("Clima");
        btnCambiarClima.setText("Cambiar clima");
        btnCambiarClima.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarClimaActionPerformed(evt);
            }
        });
        lblTipoEntidad.setText("Entidad");
        btnAgregarEntidad.setText("Agregar entidad");
        btnAgregarEntidad.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEntidadActionPerformed(evt);
            }
        });
        pnlSimNavegacion.setOpaque(false);
        pnlSimNavegacion.setLayout(new java.awt.FlowLayout(0, 10, 7));
        btnAvanzar.setText("Avanzar turno");
        btnAvanzar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAvanzarActionPerformed(evt);
            }
        });
        btnNueva.setText("Nueva simulación");
        btnNueva.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaActionPerformed(evt);
            }
        });
        btnVerReporte.setText("Ver reporte");
        btnVerReporte.setEnabled(false);
        btnVerReporte.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerReporteActionPerformed(evt);
            }
        });
        lblEstadoSimulacion.setText(" ");
        pnlReporte.setBackground(new java.awt.Color(240, 245, 241));
        pnlReporte.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));
        pnlReporte.setLayout(new java.awt.BorderLayout(14, 14));
        pnlReporteEncabezado.setOpaque(false);
        pnlReporteEncabezado.setLayout(new java.awt.GridLayout(3, 1, 0, 4));
        lblReportePaso.setText("03  REPORTE");
        lblReportePaso.setFont(new java.awt.Font("Dialog", 1, 16));
        lblReportePaso.setForeground(new java.awt.Color(23, 57, 44));
        lblReporteTitulo.setText("Resultado de la simulación");
        lblReporteTitulo.setFont(new java.awt.Font("Dialog", 1, 26));
        lblReporteTitulo.setForeground(new java.awt.Color(23, 57, 44));
        lblReporteAyuda.setText("Resumen de actividad, longevidad y cambios de población.");
        txtReporte.setEditable(false);
        txtReporte.setLineWrap(true);
        txtReporte.setWrapStyleWord(true);
        txtReporte.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        pnlReporteAcciones.setOpaque(false);
        pnlReporteAcciones.setLayout(new java.awt.FlowLayout(0, 10, 7));
        btnNuevaDesdeReporte.setText("Nueva simulación");
        btnNuevaDesdeReporte.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaDesdeReporteActionPerformed(evt);
            }
        });
        btnVolverDesdeReporte.setText("Ver último turno");
        btnVolverDesdeReporte.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverDesdeReporteActionPerformed(evt);
            }
        });

        pnlConfigEncabezado.add(lblConfigPaso);
        pnlConfigEncabezado.add(lblConfigTitulo);
        pnlConfigEncabezado.add(lblConfigAyuda);
        pnlConfiguracion.add(pnlConfigEncabezado, java.awt.BorderLayout.NORTH);
        pnlFormulario.add(lblPlantasIniciales);
        pnlFormulario.add(txtPlantas);
        pnlFormulario.add(lblConejosIniciales);
        pnlFormulario.add(txtConejos);
        pnlFormulario.add(lblLobosIniciales);
        pnlFormulario.add(txtLobos);
        pnlFormulario.add(lblClimaInicial);
        pnlFormulario.add(cmbClimaInicial);
        pnlFormulario.add(lblTurnosIniciales);
        pnlFormulario.add(txtTurnos);
        pnlConfigCentro.add(pnlFormulario, java.awt.BorderLayout.NORTH);
        scrConfiguracion.setViewportView(pnlConfigCentro);
        pnlConfiguracion.add(scrConfiguracion, java.awt.BorderLayout.CENTER);
        pnlConfigAcciones.add(btnIniciar);
        pnlConfigAcciones.add(lblConfigEstado);
        pnlConfiguracion.add(pnlConfigAcciones, java.awt.BorderLayout.SOUTH);
        add(pnlConfiguracion, "configuracion");
        pnlSimEncabezado.add(lblSimPaso, java.awt.BorderLayout.NORTH);
        pnlSimEncabezado.add(lblSimTitulo, java.awt.BorderLayout.CENTER);
        pnlContadores.add(lblTurno);
        pnlContadores.add(lblClima);
        pnlContadores.add(lblPlantas);
        pnlContadores.add(lblConejos);
        pnlContadores.add(lblLobos);
        pnlSimEncabezado.add(pnlContadores, java.awt.BorderLayout.SOUTH);
        pnlSimulacion.add(pnlSimEncabezado, java.awt.BorderLayout.NORTH);
        pnlEntidades.add(lblEntidades, java.awt.BorderLayout.NORTH);
        scrEntidades.setViewportView(tblEntidades);
        pnlEntidades.add(scrEntidades, java.awt.BorderLayout.CENTER);
        pnlDatos.add(pnlEntidades);
        pnlEventos.add(lblEventos, java.awt.BorderLayout.NORTH);
        scrEventos.setViewportView(lstEventos);
        pnlEventos.add(scrEventos, java.awt.BorderLayout.CENTER);
        pnlDatos.add(pnlEventos);
        pnlSimulacion.add(pnlDatos, java.awt.BorderLayout.CENTER);
        pnlSimAcciones.add(lblIntervencion, java.awt.BorderLayout.NORTH);
        pnlIntervencion.add(lblNuevoClima);
        pnlIntervencion.add(cmbClimaNuevo);
        pnlIntervencion.add(btnCambiarClima);
        pnlIntervencion.add(lblTipoEntidad);
        pnlIntervencion.add(cmbEntidad);
        pnlIntervencion.add(btnAgregarEntidad);
        pnlSimAcciones.add(pnlIntervencion, java.awt.BorderLayout.CENTER);
        pnlSimNavegacion.add(btnAvanzar);
        pnlSimNavegacion.add(btnNueva);
        pnlSimNavegacion.add(btnVerReporte);
        pnlSimNavegacion.add(lblEstadoSimulacion);
        pnlSimAcciones.add(pnlSimNavegacion, java.awt.BorderLayout.SOUTH);
        pnlSimulacion.add(pnlSimAcciones, java.awt.BorderLayout.SOUTH);
        add(pnlSimulacion, "simulacion");
        pnlReporteEncabezado.add(lblReportePaso);
        pnlReporteEncabezado.add(lblReporteTitulo);
        pnlReporteEncabezado.add(lblReporteAyuda);
        pnlReporte.add(pnlReporteEncabezado, java.awt.BorderLayout.NORTH);
        scrReporte.setViewportView(txtReporte);
        pnlReporte.add(scrReporte, java.awt.BorderLayout.CENTER);
        pnlReporteAcciones.add(btnNuevaDesdeReporte);
        pnlReporteAcciones.add(btnVolverDesdeReporte);
        pnlReporte.add(pnlReporteAcciones, java.awt.BorderLayout.SOUTH);
        add(pnlReporte, "reporte");
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        controlador.iniciar(txtPlantas.getText(), txtConejos.getText(), txtLobos.getText(), (String) cmbClimaInicial.getSelectedItem(), txtTurnos.getText());
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnCambiarClimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarClimaActionPerformed
        String clima = (String) cmbClimaNuevo.getSelectedItem();
        if (confirmarIntervencion("¿Cambiar el clima a " + clima + "?")) {
            controlador.cambiarClima(clima);
        }
    }//GEN-LAST:event_btnCambiarClimaActionPerformed

    private void btnAgregarEntidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEntidadActionPerformed
        String tipo = (String) cmbEntidad.getSelectedItem();
        if (confirmarIntervencion("¿Agregar al ecosistema: " + tipo + "?")) {
            controlador.agregar(tipo);
        }
    }//GEN-LAST:event_btnAgregarEntidadActionPerformed

    private void btnAvanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvanzarActionPerformed
        controlador.avanzar();
    }//GEN-LAST:event_btnAvanzarActionPerformed

    private void btnNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaActionPerformed
        controlador.nueva();
    }//GEN-LAST:event_btnNuevaActionPerformed

    private void btnVerReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerReporteActionPerformed
        controlador.verReporte();
    }//GEN-LAST:event_btnVerReporteActionPerformed

    private void btnNuevaDesdeReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaDesdeReporteActionPerformed
        controlador.nueva();
    }//GEN-LAST:event_btnNuevaDesdeReporteActionPerformed

    private void btnVolverDesdeReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverDesdeReporteActionPerformed
        mostrarPantalla("simulacion");
    }//GEN-LAST:event_btnVolverDesdeReporteActionPerformed

    private boolean confirmarIntervencion(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Intervención",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlConfiguracion;
    private javax.swing.JPanel pnlConfigEncabezado;
    private javax.swing.JLabel lblConfigPaso;
    private javax.swing.JLabel lblConfigTitulo;
    private javax.swing.JLabel lblConfigAyuda;
    private javax.swing.JPanel pnlConfigCentro;
    private javax.swing.JScrollPane scrConfiguracion;
    private javax.swing.JPanel pnlFormulario;
    private javax.swing.JLabel lblPlantasIniciales;
    private javax.swing.JTextField txtPlantas;
    private javax.swing.JLabel lblConejosIniciales;
    private javax.swing.JTextField txtConejos;
    private javax.swing.JLabel lblLobosIniciales;
    private javax.swing.JTextField txtLobos;
    private javax.swing.JLabel lblClimaInicial;
    private javax.swing.JComboBox<String> cmbClimaInicial;
    private javax.swing.JLabel lblTurnosIniciales;
    private javax.swing.JTextField txtTurnos;
    private javax.swing.JPanel pnlConfigAcciones;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JLabel lblConfigEstado;
    private javax.swing.JPanel pnlSimulacion;
    private javax.swing.JPanel pnlSimEncabezado;
    private javax.swing.JLabel lblSimPaso;
    private javax.swing.JLabel lblSimTitulo;
    private javax.swing.JPanel pnlContadores;
    private javax.swing.JLabel lblTurno;
    private javax.swing.JLabel lblClima;
    private javax.swing.JLabel lblPlantas;
    private javax.swing.JLabel lblConejos;
    private javax.swing.JLabel lblLobos;
    private javax.swing.JPanel pnlDatos;
    private javax.swing.JPanel pnlEntidades;
    private javax.swing.JLabel lblEntidades;
    private javax.swing.JScrollPane scrEntidades;
    private javax.swing.JTable tblEntidades;
    private javax.swing.JPanel pnlEventos;
    private javax.swing.JLabel lblEventos;
    private javax.swing.JScrollPane scrEventos;
    private javax.swing.JList<String> lstEventos;
    private javax.swing.JPanel pnlSimAcciones;
    private javax.swing.JLabel lblIntervencion;
    private javax.swing.JPanel pnlIntervencion;
    private javax.swing.JLabel lblNuevoClima;
    private javax.swing.JComboBox<String> cmbClimaNuevo;
    private javax.swing.JButton btnCambiarClima;
    private javax.swing.JLabel lblTipoEntidad;
    private javax.swing.JComboBox<String> cmbEntidad;
    private javax.swing.JButton btnAgregarEntidad;
    private javax.swing.JPanel pnlSimNavegacion;
    private javax.swing.JButton btnAvanzar;
    private javax.swing.JButton btnNueva;
    private javax.swing.JButton btnVerReporte;
    private javax.swing.JLabel lblEstadoSimulacion;
    private javax.swing.JPanel pnlReporte;
    private javax.swing.JPanel pnlReporteEncabezado;
    private javax.swing.JLabel lblReportePaso;
    private javax.swing.JLabel lblReporteTitulo;
    private javax.swing.JLabel lblReporteAyuda;
    private javax.swing.JScrollPane scrReporte;
    private javax.swing.JTextArea txtReporte;
    private javax.swing.JPanel pnlReporteAcciones;
    private javax.swing.JButton btnNuevaDesdeReporte;
    private javax.swing.JButton btnVolverDesdeReporte;
    // End of variables declaration//GEN-END:variables
}
