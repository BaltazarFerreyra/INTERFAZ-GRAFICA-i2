package controlador;

import dao.EcosistemaDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelo.Clima;
import modelo.Ecosistema;
import modelo.Entidad;
import vista.VistaEcosistema;

public final class Controlador {
    private final VistaEcosistema vista;
    private final EcosistemaDao dao;
    private Ecosistema ecosistema;

    public Controlador(VistaEcosistema vista, EcosistemaDao dao) {
        this.vista = vista;
        this.dao = dao;
    }

    public void cargar() {
        try {
            ecosistema = dao.cargar().orElse(null);
            if (ecosistema != null) {
                actualizar();
                return;
            }
            vista.mostrarConfiguracion();
        } catch (IOException ex) {
            vista.mostrarConfiguracion();
            vista.mostrarError("No se pudo cargar la simulación. " + ex.getMessage());
        }
    }

    public void iniciar(String plantas, String conejos, String lobos, String clima, String turnos) {
        List<String> errores = new ArrayList<>();
        Integer cantidadPlantas = entero(plantas, "Plantas", 5, 30, errores);
        Integer cantidadConejos = entero(conejos, "Conejos", 2, 15, errores);
        Integer cantidadLobos = entero(lobos, "Lobos", 1, 5, errores);
        Clima climaInicial = null;
        try {
            climaInicial = buscarClima(clima);
        } catch (IllegalArgumentException ex) {
            errores.add(ex.getMessage());
        }
        Integer cantidadTurnos = entero(turnos, "Turnos", 10, 50, errores);

        if (!errores.isEmpty()) {
            vista.mostrarError(String.join("\n", errores));
            return;
        }
        ecosistema = Ecosistema.iniciar(climaInicial, cantidadPlantas, cantidadConejos,
                cantidadLobos, cantidadTurnos);
        actualizar();
        guardar();
    }

    public void avanzar() {
        if (ecosistema == null || ecosistema.esFinDeSimulacion()) {
            return;
        }
        ecosistema.procesarTurno();
        actualizar();
        guardar();
    }

    public void cambiarClima(String clima) {
        try {
            if (ecosistema == null || !ecosistema.cambiarClimaEnIntervencion(buscarClima(clima))) {
                vista.mostrarError("El clima solo se puede cambiar durante una intervención.");
                return;
            }
            actualizar();
            if (guardar()) {
                vista.mostrarMensaje("Clima actualizado.");
            }
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    public void agregar(String tipo) {
        if (ecosistema == null || !ecosistema.puedeIntervenir()) {
            vista.mostrarError("Solo se pueden agregar entidades durante una intervención.");
            return;
        }
        if (!ecosistema.agregarEnIntervencion(tipo)) {
            vista.mostrarError("No se pudo agregar la entidad. El límite acumulado es de 5 lobos.");
            return;
        }
        actualizar();
        if (guardar()) {
            vista.mostrarMensaje("Entidad agregada.");
        }
    }

    public void nueva() {
        if (ecosistema != null && !ecosistema.esFinDeSimulacion() && !vista.confirmarNueva()) {
            return;
        }
        try {
            dao.borrar();
            ecosistema = null;
            vista.mostrarConfiguracion();
        } catch (IOException ex) {
            vista.mostrarError("No se pudo quitar la simulación guardada. " + ex.getMessage());
        }
    }

    public void verReporte() {
        if (ecosistema != null && ecosistema.esFinDeSimulacion()) {
            vista.mostrarReporte(ecosistema.generarReporteFinal());
        }
    }

    private void actualizar() {
        Object[][] filas = ecosistema.getEntidadesVivas().stream()
                .map(Controlador::fila)
                .toArray(Object[][]::new);
        vista.mostrarSimulacion(ecosistema.getTurnoActual() + " / " + ecosistema.getTurnosTotales(),
                ecosistema.getClimaActual().getNombre(), ecosistema.contarPlantasVivas(),
                ecosistema.contarConejosVivos(), ecosistema.contarLobosVivos(),
                filas, ecosistema.getEventosDelTurno(), ecosistema.puedeIntervenir());
        if (ecosistema.esFinDeSimulacion()) {
            verReporte();
        }
    }

    private static Object[] fila(Entidad entidad) {
        String tipo = entidad.getClass().getSimpleName().replace("PlantaVenenosa", "Planta venenosa");
        return new Object[]{tipo, entidad.getNombre(), entidad.getEdad(), entidad.getEnergia()};
    }

    private boolean guardar() {
        try {
            dao.guardar(ecosistema);
            return true;
        } catch (IOException ex) {
            vista.mostrarError("No se pudo guardar la simulación. " + ex.getMessage());
            return false;
        }
    }

    private static Integer entero(String valor, String campo, int minimo, int maximo,
            List<String> errores) {
        String texto = valor == null ? "" : valor.trim();
        if (texto.isEmpty()) {
            errores.add("Completá el campo " + campo + ".");
            return null;
        }
        int numero;
        try {
            numero = Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            errores.add(campo + " debe ser un número entero.");
            return null;
        }
        if (numero < minimo || numero > maximo) {
            errores.add(campo + " debe estar entre " + minimo + " y " + maximo + ".");
            return null;
        }
        return numero;
    }

    private static Clima buscarClima(String nombre) {
        for (Clima clima : Clima.values()) {
            if (clima.getNombre().equals(nombre)) {
                return clima;
            }
        }
        throw new IllegalArgumentException("Elegí un clima válido.");
    }
}
