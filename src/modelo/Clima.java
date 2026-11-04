package modelo;

public enum Clima {
    SOLEADO("Soleado", 1.5, 5.0, 0.0, 0.0),
    LLUVIOSO("Lluvioso", 2.0, 3.0, -5.0, 0.0),
    SEQUIA("Sequia", 0.5, -5.0, 0.0, 0.0),
    INVIERNO("Invierno", 0.0, -8.0, 0.0, 0.20);

    private final String nombre;
    private final double factorReproduccionPlantas;
    private final double deltaEnergiaConejos;
    private final double deltaEnergiaLobos;
    private final double bonoExitoCazaLobos;

    Clima(String nombre, double factorReproduccionPlantas, double deltaEnergiaConejos,
          double deltaEnergiaLobos, double bonoExitoCazaLobos) {
        this.nombre = nombre;
        this.factorReproduccionPlantas = factorReproduccionPlantas;
        this.deltaEnergiaConejos = deltaEnergiaConejos;
        this.deltaEnergiaLobos = deltaEnergiaLobos;
        this.bonoExitoCazaLobos = bonoExitoCazaLobos;
    }

    public String getNombre() {
        return nombre;
    }

    public double getFactorReproduccionPlantas() {
        return factorReproduccionPlantas;
    }

    public double getDeltaEnergiaConejos() {
        return deltaEnergiaConejos;
    }

    public double getDeltaEnergiaLobos() {
        return deltaEnergiaLobos;
    }

    public double getBonoExitoCazaLobos() {
        return bonoExitoCazaLobos;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
