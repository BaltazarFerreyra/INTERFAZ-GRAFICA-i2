package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Ecosistema {

    private final ArrayList<Planta> plantas;
    private final ArrayList<Conejo> conejos;
    private final ArrayList<Lobo> lobos;

    private Clima climaActual;
    private int turnoActual;
    private final int turnosTotales;
    private boolean intervencionRealizada;

    private int contadorPlantas = 0;
    private int contadorConejos = 0;
    private int contadorLobos = 0;

    private static final String[] NOMBRES_CONEJOS = {
        "Blas", "Luna", "Topo", "Rex", "Pelusa", "Tambor", "Copito",
        "Saltarin", "Bugs", "Orejitas", "Mota", "Bobi", "Nieve", "Pecas", "Duna"
    };
    private static final String[] NOMBRES_LOBOS = {
        "Fang", "Sombra", "Akela", "Ghost", "Loki", "Fenrir", "Balto", "Klaus"
    };

    private int lobosAgregadosEnIntervencion = 0;
    public static final int MAX_LOBOS_INTERVENCION = 5;

    private final List<String> eventosDelTurno;
    private final Map<Integer, Integer> cantidadEventosPorTurno;
    private final List<Entidad> historicoTodasLasEntidades;

    private int nacimientosPlantas = 0;
    private int muertesPlantas = 0;
    private int nacimientosConejos = 0;
    private int muertesConejos = 0;
    private int muertesLobos = 0;

    public static class RegistroPoblacion {
        public final int turno;
        public final int plantas;
        public final int conejos;
        public final int lobos;

        public RegistroPoblacion(int turno, int plantas, int conejos, int lobos) {
            this.turno = turno;
            this.plantas = plantas;
            this.conejos = conejos;
            this.lobos = lobos;
        }
    }
    private final List<RegistroPoblacion> historialPoblaciones;

    private String causaFinSimulacion = "En curso";
    private static final Random random = new Random();

    private Ecosistema(Clima climaInicial, int turnosTotales) {
        this.plantas = new ArrayList<>();
        this.conejos = new ArrayList<>();
        this.lobos = new ArrayList<>();
        this.climaActual = Objects.requireNonNull(climaInicial);
        this.turnoActual = 0;
        this.turnosTotales = turnosTotales;

        this.eventosDelTurno = new ArrayList<>();
        this.cantidadEventosPorTurno = new LinkedHashMap<>();
        this.historicoTodasLasEntidades = new ArrayList<>();
        this.historialPoblaciones = new ArrayList<>();
    }

    public static Ecosistema iniciar(Clima clima, int plantas, int conejos, int lobos, int turnos) {
        if (plantas < 5 || plantas > 30 || conejos < 2 || conejos > 15
                || lobos < 1 || lobos > MAX_LOBOS_INTERVENCION || turnos < 10 || turnos > 50) {
            throw new IllegalArgumentException("La configuración está fuera de los rangos permitidos.");
        }
        Ecosistema ecosistema = new Ecosistema(clima, turnos);
        for (int i = 0; i < plantas; i++) ecosistema.agregarEntidad("planta");
        for (int i = 0; i < conejos; i++) ecosistema.agregarEntidad("conejo");
        for (int i = 0; i < lobos; i++) ecosistema.agregarEntidad("lobo");
        return ecosistema;
    }

    public boolean puedeIntervenir() {
        return turnoActual > 0 && turnoActual % 3 == 0 && !intervencionRealizada
                && !esFinDeSimulacion();
    }

    public boolean agregarEnIntervencion(String tipo) {
        if (!puedeIntervenir() || tipo == null || !agregarEntidad(tipo)) {
            return false;
        }
        intervencionRealizada = true;
        registrarEvento("Intervención: se agregó " + tipo.trim().toLowerCase(Locale.ROOT) + ".");
        return true;
    }

    public boolean cambiarClimaEnIntervencion(Clima clima) {
        if (!puedeIntervenir() || clima == null) {
            return false;
        }
        climaActual = clima;
        intervencionRealizada = true;
        registrarEvento("Intervención: clima cambiado a " + clima.getNombre() + ".");
        return true;
    }

    private boolean agregarEntidad(String tipo) {
        double energiaPorDefecto;
        switch (tipo.toLowerCase(Locale.ROOT).trim()) {
            case "planta":
            case "planta_venenosa":
                energiaPorDefecto = 35.0 + random.nextInt(26);
                break;
            case "conejo":
                energiaPorDefecto = 45.0 + random.nextInt(36);
                break;
            case "lobo":
                energiaPorDefecto = 60.0 + random.nextInt(31);
                break;
            default:
                return false;
        }
        return agregarEntidad(tipo, energiaPorDefecto);
    }

    private boolean agregarEntidad(String tipo, double energiaInicial) {
        String tipoNorm = tipo.toLowerCase(Locale.ROOT).trim();
        switch (tipoNorm) {
            case "planta": {
                contadorPlantas++;
                int tamanio = 1 + random.nextInt(5);
                Planta p = new Planta("Helecho-" + contadorPlantas, energiaInicial, 0, tamanio);
                plantas.add(p);
                historicoTodasLasEntidades.add(p);
                return true;
            }
            case "planta_venenosa": {
                contadorPlantas++;
                int tamanio = 1 + random.nextInt(5);
                PlantaVenenosa pv = new PlantaVenenosa("Cicuta-" + contadorPlantas, energiaInicial, 0, tamanio);
                plantas.add(pv);
                historicoTodasLasEntidades.add(pv);
                return true;
            }
            case "conejo": {
                contadorConejos++;
                String nombreConejo = (contadorConejos <= NOMBRES_CONEJOS.length)
                        ? NOMBRES_CONEJOS[contadorConejos - 1]
                        : "Conejo-" + contadorConejos;
                int velocidad = 8 + random.nextInt(6);
                double peso = 1.5 + (random.nextDouble() * 2.5);
                Conejo c = new Conejo(nombreConejo, energiaInicial, 0, velocidad, peso);
                conejos.add(c);
                historicoTodasLasEntidades.add(c);
                return true;
            }
            case "lobo": {
                if (lobosAgregadosEnIntervencion >= MAX_LOBOS_INTERVENCION) {
                    return false;
                }
                contadorLobos++;
                String nombreLobo = (contadorLobos <= NOMBRES_LOBOS.length)
                        ? NOMBRES_LOBOS[contadorLobos - 1]
                        : "Lobo-" + contadorLobos;
                int velocidad = 12 + random.nextInt(6);
                double peso = 25.0 + (random.nextDouble() * 20.0);
                Lobo l = new Lobo(nombreLobo, energiaInicial, 0, velocidad, peso);
                lobos.add(l);
                historicoTodasLasEntidades.add(l);
                lobosAgregadosEnIntervencion++;
                return true;
            }
            default:
                return false;
        }
    }

    public Planta crearNuevaPlanta() {
        contadorPlantas++;
        int tamanio = 1 + random.nextInt(5);
        double energia = 30.0 + random.nextInt(25);
        if (random.nextDouble() < 0.15) {
            PlantaVenenosa pv = new PlantaVenenosa("Cicuta-" + contadorPlantas, energia, 0, tamanio);
            plantas.add(pv);
            historicoTodasLasEntidades.add(pv);
            return pv;
        } else {
            Planta p = new Planta("Helecho-" + contadorPlantas, energia, 0, tamanio);
            plantas.add(p);
            historicoTodasLasEntidades.add(p);
            return p;
        }
    }

    public Conejo crearNuevoConejo() {
        contadorConejos++;
        String nombreConejo = (contadorConejos <= NOMBRES_CONEJOS.length)
                ? NOMBRES_CONEJOS[contadorConejos - 1]
                : "Conejo-" + contadorConejos;
        int vel = 8 + random.nextInt(5);
        double peso = 1.2 + (random.nextDouble() * 1.5);
        Conejo c = new Conejo(nombreConejo, 40.0, 0, vel, peso);
        conejos.add(c);
        historicoTodasLasEntidades.add(c);
        return c;
    }

    public void procesarTurno() {
        if (esFinDeSimulacion()) return;
        turnoActual++;
        intervencionRealizada = false;
        eventosDelTurno.clear();

        ArrayList<Reproducible> reproductores = new ArrayList<>();
        for (Planta p : new ArrayList<>(plantas)) {
            if (p.isViva()) reproductores.add(p);
        }
        for (Reproducible r : reproductores) {
            r.intentarReproduccion(this);
        }

        for (Conejo c : new ArrayList<>(conejos)) {
            if (c.isViva()) {
                c.actuar(this);
            }
        }

        for (Lobo l : new ArrayList<>(lobos)) {
            if (l.isViva()) {
                l.actuar(this);
            }
        }

        aplicarEnvejecimientoYEfectosClima();

        verificarMuertesGenerales();

        cantidadEventosPorTurno.put(turnoActual, eventosDelTurno.size());
        historialPoblaciones.add(new RegistroPoblacion(
                turnoActual, contarPlantasVivas(), contarConejosVivos(), contarLobosVivos()
        ));
    }

    private void aplicarEnvejecimientoYEfectosClima() {
        for (Planta p : plantas) {
            if (p.isViva()) {
                p.envejecer();
            }
        }

        for (Conejo c : conejos) {
            if (c.isViva()) {
                c.envejecer();
                double delta = climaActual.getDeltaEnergiaConejos();
                if (delta > 0) {
                    c.sumarEnergia(delta);
                } else if (delta < 0) {
                    c.descontarEnergia(-delta);
                }
            }
        }

        for (Lobo l : lobos) {
            if (l.isViva()) {
                l.envejecer();
                double delta = climaActual.getDeltaEnergiaLobos();
                if (delta < 0) {
                    l.descontarEnergia(-delta);
                }
            }
        }
    }

    private void verificarMuertesGenerales() {
        Iterator<Planta> itP = plantas.iterator();
        while (itP.hasNext()) {
            Planta p = itP.next();
            if (p.getEnergia() <= 0 || !p.isViva()) {
                if (p.isViva()) {
                    p.setViva(false);
                }
                muertesPlantas++;
                registrarEvento("Planta '" + p.getNombre() + "' murió marchita");
                itP.remove();
            }
        }

        Iterator<Conejo> itC = conejos.iterator();
        while (itC.hasNext()) {
            Conejo c = itC.next();
            if (c.getEnergia() <= 0 || !c.estaVivo()) {
                c.verificarMuerte();
                muertesConejos++;
                registrarEvento("Conejo '" + c.getNombre() + "' murió al quedarse sin energía");
                itC.remove();
            }
        }

        Iterator<Lobo> itL = lobos.iterator();
        while (itL.hasNext()) {
            Lobo l = itL.next();
            if (l.getEnergia() <= 0 || !l.estaVivo()) {
                l.verificarMuerte();
                muertesLobos++;
                registrarEvento("Lobo '" + l.getNombre() + "' murió al quedarse sin energía");
                itL.remove();
            }
        }
    }

    public boolean ecosistemaColapsado() {
        int vPlantas = contarPlantasVivas();
        int vConejos = contarConejosVivos();
        int vLobos = contarLobosVivos();

        if (vPlantas == 0 && vConejos == 0 && vLobos == 0) {
            causaFinSimulacion = "Colapso total: se extinguieron todas las especies.";
            return true;
        }
        if (vPlantas == 0) {
            causaFinSimulacion = "Colapso del ecosistema: se extinguieron las plantas (sin productores).";
            return true;
        }
        if (vConejos == 0) {
            causaFinSimulacion = "Colapso del ecosistema: se extinguieron los conejos (herbívoros).";
            return true;
        }
        if (vLobos == 0) {
            causaFinSimulacion = "Colapso del ecosistema: se extinguieron los lobos (depredadores).";
            return true;
        }
        return false;
    }

    public boolean esFinDeSimulacion() {
        if (ecosistemaColapsado()) {
            return true;
        }
        if (turnoActual >= turnosTotales) {
            causaFinSimulacion = "Finalización exitosa: se completaron los " + turnosTotales + " turnos configurados.";
            return true;
        }
        return false;
    }

    public String generarReporteFinal() {
        esFinDeSimulacion();
        StringBuilder reporte = new StringBuilder();
        reporte.append("Turnos transcurridos: " + turnoActual + " de " + turnosTotales).append('\n');
        reporte.append("Causa de fin: " + causaFinSimulacion).append('\n');

        int turnoMayorActividad = 1;
        int maxEventos = -1;
        for (Map.Entry<Integer, Integer> entry : cantidadEventosPorTurno.entrySet()) {
            if (entry.getValue() > maxEventos) {
                maxEventos = entry.getValue();
                turnoMayorActividad = entry.getKey();
            }
        }
        reporte.append("Turno de mayor actividad: Turno " + turnoMayorActividad + " (" + Math.max(0, maxEventos) + " eventos)").append('\n');

        reporte.append("\n--- Entidad más longeva por especie ---").append('\n');
        Entidad plantaLongeva = buscarMasLongeva(Planta.class);
        Entidad conejoLongevo = buscarMasLongeva(Conejo.class);
        Entidad loboLongevo = buscarMasLongeva(Lobo.class);

        reporte.append("  * Planta más longeva: " + (plantaLongeva != null ? plantaLongeva.getNombre() + " (" + plantaLongeva.getEdad() + " turnos de vida)" : "Ninguna")).append('\n');
        reporte.append("  * Conejo más longevo: " + (conejoLongevo != null ? conejoLongevo.getNombre() + " (" + conejoLongevo.getEdad() + " turnos de vida)" : "Ninguno")).append('\n');
        reporte.append("  * Lobo más longevo: " + (loboLongevo != null ? loboLongevo.getNombre() + " (" + loboLongevo.getEdad() + " turnos de vida)" : "Ninguno")).append('\n');

        reporte.append("\n--- Depredador más eficiente ---").append('\n');
        Lobo mejorLobo = null;
        for (Entidad e : historicoTodasLasEntidades) {
            if (e instanceof Lobo) {
                Lobo l = (Lobo) e;
                if (mejorLobo == null || l.getExitosCaza() > mejorLobo.getExitosCaza()) {
                    mejorLobo = l;
                }
            }
        }
        if (mejorLobo != null) {
            reporte.append("  * Lobo con más cacerías: " + mejorLobo.getNombre() + " con " + mejorLobo.getExitosCaza() + " capturas exitosas.").append('\n');
        } else {
            reporte.append("  * No hubo lobos registrados.").append('\n');
        }

        reporte.append("\n--- Balance demográfico (Nacimientos y Muertes) ---").append('\n');
        reporte.append("  * Plantas : Nacidas/Brotadas = " + nacimientosPlantas + " | Muertas = " + muertesPlantas).append('\n');
        reporte.append("  * Conejos : Nacidos = " + nacimientosConejos + " | Muertos = " + muertesConejos).append('\n');
        reporte.append("  * Lobos   : Nacidos = 0 (no se reproducen) | Muertos = " + muertesLobos).append('\n');

        reporte.append("\n--- Picos históricos de población ---").append('\n');
        reportarPicosPoblacion(reporte);

        reporte.append("\n--- Elementos peligrosos del ecosistema ---").append('\n');
        reportarElementosPeligrosos(reporte);

        return reporte.toString();
    }

    private void reportarPicosPoblacion(StringBuilder reporte) {
        if (historialPoblaciones.isEmpty()) return;

        int maxP = -1, minP = Integer.MAX_VALUE, tMaxP = 1, tMinP = 1;
        int maxC = -1, minC = Integer.MAX_VALUE, tMaxC = 1, tMinC = 1;
        int maxL = -1, minL = Integer.MAX_VALUE, tMaxL = 1, tMinL = 1;

        for (RegistroPoblacion r : historialPoblaciones) {
            if (r.plantas > maxP) { maxP = r.plantas; tMaxP = r.turno; }
            if (r.plantas < minP) { minP = r.plantas; tMinP = r.turno; }

            if (r.conejos > maxC) { maxC = r.conejos; tMaxC = r.turno; }
            if (r.conejos < minC) { minC = r.conejos; tMinC = r.turno; }

            if (r.lobos > maxL) { maxL = r.lobos; tMaxL = r.turno; }
            if (r.lobos < minL) { minL = r.lobos; tMinL = r.turno; }
        }

        reporte.append("  * Plantas : Máximo " + maxP + " (Turno " + tMaxP + ") | Mínimo " + minP + " (Turno " + tMinP + ")").append('\n');
        reporte.append("  * Conejos : Máximo " + maxC + " (Turno " + tMaxC + ") | Mínimo " + minC + " (Turno " + tMinC + ")").append('\n');
        reporte.append("  * Lobos   : Máximo " + maxL + " (Turno " + tMaxL + ") | Mínimo " + minL + " (Turno " + tMinL + ")").append('\n');
    }

    private void reportarElementosPeligrosos(StringBuilder reporte) {
        List<Peligroso> listaPeligrosos = new ArrayList<>();
        for (Entidad e : historicoTodasLasEntidades) {
            if (e instanceof Peligroso) {
                listaPeligrosos.add((Peligroso) e);
            }
        }

        listaPeligrosos.sort((a, b) -> Integer.compare(b.getNivelPeligro(), a.getNivelPeligro()));

        if (listaPeligrosos.isEmpty()) {
            reporte.append("  No se identificaron elementos con la interfaz Peligroso.").append('\n');
        } else {
            for (Peligroso p : listaPeligrosos) {
                Entidad ent = (Entidad) p;
                reporte.append("  [Peligro: " + p.getNivelPeligro() + "] " + ent.getNombre() + " (" + ent.getClass().getSimpleName() + ")").append('\n');
            }
        }
    }

    private <T extends Entidad> Entidad buscarMasLongeva(Class<T> clase) {
        Entidad masLongeva = null;
        for (Entidad e : historicoTodasLasEntidades) {
            if (clase.isInstance(e)) {
                if (masLongeva == null || e.getEdad() > masLongeva.getEdad()) {
                    masLongeva = e;
                }
            }
        }
        return masLongeva;
    }

    public void registrarEvento(String desc) {
        eventosDelTurno.add(desc);
    }

    public void registrarNacimientoPlanta(Planta p) {
        nacimientosPlantas++;
    }

    public void registrarNacimientoConejo(Conejo c) {
        nacimientosConejos++;
    }

    void registrarMuertePlanta(Planta planta) {
        if (plantas.remove(planta)) muertesPlantas++;
    }

    public void registrarMuerteConejo(Conejo c) {
        if (conejos.remove(c)) muertesConejos++;
    }

    public Planta obtenerPlantaVivaAleatoria() {
        List<Planta> vivas = new ArrayList<>();
        for (Planta p : plantas) {
            if (p.isViva()) vivas.add(p);
        }
        if (vivas.isEmpty()) return null;
        return vivas.get(random.nextInt(vivas.size()));
    }

    public Conejo obtenerConejoVivoAleatorio() {
        List<Conejo> vivos = new ArrayList<>();
        for (Conejo c : conejos) {
            if (c.isViva()) vivos.add(c);
        }
        if (vivos.isEmpty()) return null;
        return vivos.get(random.nextInt(vivos.size()));
    }

    public int contarPlantasVivas() {
        int c = 0;
        for (Planta p : plantas) if (p.isViva()) c++;
        return c;
    }

    public int contarConejosVivos() {
        int c = 0;
        for (Conejo con : conejos) if (con.isViva()) c++;
        return c;
    }

    public int contarLobosVivos() {
        int c = 0;
        for (Lobo l : lobos) if (l.isViva()) c++;
        return c;
    }

    public Clima getClimaActual() {
        return climaActual;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public int getTurnosTotales() {
        return turnosTotales;
    }

    public List<Entidad> getEntidadesVivas() {
        List<Entidad> vivas = new ArrayList<>();
        for (Entidad entidad : historicoTodasLasEntidades) {
            if (entidad.isViva()) vivas.add(entidad);
        }
        return Collections.unmodifiableList(vivas);
    }

    public List<String> getEventosDelTurno() {
        return Collections.unmodifiableList(new ArrayList<>(eventosDelTurno));
    }

    public String exportarEstado() {
        StringBuilder texto = new StringBuilder("ECOSISTEMA\t1\n");
        escribirFila(texto, "estado", climaActual.name(), turnoActual, turnosTotales, intervencionRealizada);
        escribirFila(texto, "balance", nacimientosPlantas, muertesPlantas,
                nacimientosConejos, muertesConejos, muertesLobos);
        for (Entidad entidad : historicoTodasLasEntidades) {
            escribirEntidad(texto, entidad);
        }
        for (RegistroPoblacion registro : historialPoblaciones) {
            escribirFila(texto, "turno", registro.turno, registro.plantas, registro.conejos,
                    registro.lobos, cantidadEventosPorTurno.get(registro.turno));
        }
        for (String evento : eventosDelTurno) {
            escribirFila(texto, "evento", evento);
        }
        escribirFila(texto, "fin");
        return texto.toString();
    }

    public static Ecosistema restaurarEstado(String texto) {
        String[] filas = texto.split("\\R");
        if (filas.length < 4 || !filas[0].equals("ECOSISTEMA\t1")
                || !filas[filas.length - 1].equals("fin")) {
            throw new IllegalArgumentException("Formato o versión de guardado inválidos.");
        }

        String[] estado = campos(filas[1], "estado", 5);
        Clima clima = Clima.valueOf(estado[1]);
        int turnosTotales = entero(estado[3], 10, 50);
        int turno = entero(estado[2], 0, turnosTotales);
        Ecosistema ecosistema = new Ecosistema(clima, turnosTotales);
        ecosistema.turnoActual = turno;
        ecosistema.intervencionRealizada = booleano(estado[4]);
        if (ecosistema.intervencionRealizada && (turno == 0 || turno % 3 != 0)) {
            throw new IllegalArgumentException("La intervención guardada corresponde a un turno inválido.");
        }

        String[] balance = campos(filas[2], "balance", 6);
        ecosistema.nacimientosPlantas = noNegativo(balance[1]);
        ecosistema.muertesPlantas = noNegativo(balance[2]);
        ecosistema.nacimientosConejos = noNegativo(balance[3]);
        ecosistema.muertesConejos = noNegativo(balance[4]);
        ecosistema.muertesLobos = noNegativo(balance[5]);

        Set<String> nombres = new HashSet<>();
        for (int i = 3; i < filas.length - 1; i++) {
            String[] valores = filas[i].split("\t", -1);
            switch (valores[0]) {
                case "entidad":
                    Entidad entidad = leerEntidad(valores, turno);
                    if (!nombres.add(entidad.getNombre())) {
                        throw new IllegalArgumentException("Hay entidades repetidas en el guardado.");
                    }
                    ecosistema.restaurarEntidad(entidad);
                    break;
                case "turno":
                    campos(filas[i], "turno", 6);
                    int turnoRegistrado = ecosistema.historialPoblaciones.size() + 1;
                    if (entero(valores[1], 1, turno) != turnoRegistrado) {
                        throw new IllegalArgumentException("El historial de turnos está desordenado.");
                    }
                    ecosistema.historialPoblaciones.add(new RegistroPoblacion(turnoRegistrado,
                            noNegativo(valores[2]), noNegativo(valores[3]), noNegativo(valores[4])));
                    ecosistema.cantidadEventosPorTurno.put(turnoRegistrado, noNegativo(valores[5]));
                    break;
                case "evento":
                    ecosistema.eventosDelTurno.add(campos(filas[i], "evento", 2)[1]);
                    break;
                default:
                    throw new IllegalArgumentException("Registro de guardado desconocido.");
            }
        }
        if (ecosistema.historialPoblaciones.size() != turno || ecosistema.contadorPlantas < 5
                || ecosistema.contadorConejos < 2 || ecosistema.contadorLobos < 1
                || ecosistema.contadorLobos > MAX_LOBOS_INTERVENCION) {
            throw new IllegalArgumentException("El guardado está incompleto.");
        }
        ecosistema.esFinDeSimulacion();
        return ecosistema;
    }

    private static void escribirEntidad(StringBuilder texto, Entidad entidad) {
        String tipo = entidad.getClass().getSimpleName();
        if (entidad instanceof Planta) {
            Planta planta = (Planta) entidad;
            int nivelPeligro = planta instanceof PlantaVenenosa ? ((PlantaVenenosa) planta).getNivelPeligro() : 0;
            escribirFila(texto, "entidad", tipo, planta.getNombre(), planta.getEnergia(),
                    planta.getEdad(), planta.isViva(), planta.getTamanio(), nivelPeligro);
        } else {
            Animal animal = (Animal) entidad;
            int exitosCaza = animal instanceof Lobo ? ((Lobo) animal).getExitosCaza() : 0;
            escribirFila(texto, "entidad", tipo, animal.getNombre(), animal.getEnergia(),
                    animal.getEdad(), animal.isViva(), animal.getVelocidad(), animal.getPeso(), exitosCaza);
        }
    }

    private static Entidad leerEntidad(String[] valores, int turno) {
        if (valores.length < 8 || valores[2].trim().isEmpty()) {
            throw new IllegalArgumentException("Entidad incompleta en el guardado.");
        }
        String tipo = valores[1];
        String nombre = valores[2];
        double energia = decimal(valores[3]);
        int edad = entero(valores[4], 0, turno);
        boolean viva = booleano(valores[5]);
        if (viva ? energia <= 0 : energia != 0) {
            throw new IllegalArgumentException("El estado y la energía de la entidad son incompatibles.");
        }
        Entidad entidad;
        switch (tipo) {
            case "Planta":
            case "PlantaVenenosa":
                if (valores.length != 8) {
                    throw new IllegalArgumentException("Planta inválida en el guardado.");
                }
                int tamanio = entero(valores[6], 1, 5);
                if (tipo.equals("Planta")) {
                    entero(valores[7], 0, 0);
                    entidad = new Planta(nombre, energia, edad, tamanio);
                } else {
                    PlantaVenenosa venenosa = new PlantaVenenosa(nombre, energia, edad, tamanio);
                    venenosa.setNivelPeligro(entero(valores[7], 1, Integer.MAX_VALUE));
                    entidad = venenosa;
                }
                break;
            case "Conejo":
            case "Lobo":
                if (valores.length != 9) {
                    throw new IllegalArgumentException("Animal inválido en el guardado.");
                }
                int velocidad = entero(valores[6], 1, Integer.MAX_VALUE);
                double peso = decimal(valores[7]);
                if (peso < 0.1) throw new IllegalArgumentException("Peso inválido en el guardado.");
                int exitosCaza = entero(valores[8], 0, turno);
                if (tipo.equals("Lobo")) {
                    Lobo lobo = new Lobo(nombre, energia, edad, velocidad, peso);
                    lobo.setExitosCaza(exitosCaza);
                    entidad = lobo;
                } else {
                    if (exitosCaza != 0) {
                        throw new IllegalArgumentException("El conejo no puede tener cacerías registradas.");
                    }
                    entidad = new Conejo(nombre, energia, edad, velocidad, peso);
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de entidad desconocido.");
        }
        entidad.setViva(viva);
        return entidad;
    }

    private void restaurarEntidad(Entidad entidad) {
        historicoTodasLasEntidades.add(entidad);
        if (entidad instanceof Planta) {
            contadorPlantas++;
            if (entidad.isViva()) plantas.add((Planta) entidad);
        } else if (entidad instanceof Conejo) {
            contadorConejos++;
            if (entidad.isViva()) conejos.add((Conejo) entidad);
        } else {
            contadorLobos++;
            lobosAgregadosEnIntervencion++;
            if (entidad.isViva()) lobos.add((Lobo) entidad);
        }
    }

    private static void escribirFila(StringBuilder texto, String tipo, Object... valores) {
        texto.append(tipo);
        for (Object valor : valores) texto.append('\t').append(valor);
        texto.append('\n');
    }

    private static String[] campos(String fila, String tipo, int cantidad) {
        String[] campos = fila.split("\t", -1);
        if (campos.length != cantidad || !campos[0].equals(tipo)) {
            throw new IllegalArgumentException("Registro inválido: " + tipo + ".");
        }
        return campos;
    }

    private static int noNegativo(String valor) {
        return entero(valor, 0, Integer.MAX_VALUE);
    }

    private static int entero(String valor, int minimo, int maximo) {
        int numero = Integer.parseInt(valor);
        if (numero < minimo || numero > maximo) {
            throw new IllegalArgumentException("Número fuera del rango del guardado.");
        }
        return numero;
    }

    private static boolean booleano(String valor) {
        if ("true".equals(valor)) return true;
        if ("false".equals(valor)) return false;
        throw new IllegalArgumentException("Valor lógico inválido en el guardado.");
    }

    private static double decimal(String valor) {
        double numero = Double.parseDouble(valor);
        if (!Double.isFinite(numero)) {
            throw new IllegalArgumentException("Valor decimal inválido en el guardado.");
        }
        return numero;
    }
}
