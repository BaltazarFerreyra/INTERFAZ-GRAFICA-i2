package principal;

import controlador.Controlador;
import dao.EcosistemaDao;
import java.nio.file.Path;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import modelo.Clima;
import vista.VistaEcosistema;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] climas = Arrays.stream(Clima.values()).map(Clima::getNombre).toArray(String[]::new);
            VistaEcosistema vista = new VistaEcosistema(climas);
            Controlador controlador = new Controlador(vista, new EcosistemaDao(Path.of("simulacion.txt")));
            vista.setControlador(controlador);

            JFrame ventana = new JFrame("Simulador de ecosistema");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setContentPane(vista);
            ventana.pack();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
            controlador.cargar();
        });
    }
}
