package modelo;

import java.util.Random;

public class Lobo extends Animal implements Peligroso {

    private int exitosCaza;
    private static final Random random = new Random();

    public Lobo(String nombre, double energia, int edad, int velocidad, double peso) {
        super(nombre, energia, edad, velocidad, peso);
        this.exitosCaza = 0;
    }

    @Override
    public void actuar(Ecosistema eco) {
        if (!isViva()) return;
        comer(eco);
    }

    @Override
    public void comer(Ecosistema eco) {
        if (!isViva() || eco == null) return;

        Conejo presa = eco.obtenerConejoVivoAleatorio();
        if (presa == null) {
            eco.registrarEvento("Lobo '" + getNombre() + "' no encontro conejos para cazar");
            return;
        }

        double probExito = Math.min(0.85, Math.max(0.15, getEnergia() / 100.0));

        probExito += eco.getClimaActual().getBonoExitoCazaLobos();
        probExito = Math.min(0.95, probExito);

        if (random.nextDouble() < probExito) {
            presa.morir();
            eco.registrarMuerteConejo(presa);
            sumarEnergia(30.0);
            exitosCaza++;
            eco.registrarEvento("Lobo '" + getNombre() + "' cazo a Conejo '"
                    + presa.getNombre() + "' (+30 energia) [cacerias: " + exitosCaza + "]");
        } else {
            descontarEnergia(5.0);
            eco.registrarEvento("Lobo '" + getNombre() + "' fallo la caza");
        }
    }

    @Override
    public int getNivelPeligro() {
        return 5 + Math.min(5, exitosCaza);
    }

    public int getExitosCaza() {
        return exitosCaza;
    }

    public void setExitosCaza(int exitosCaza) {
        this.exitosCaza = Math.max(0, exitosCaza);
    }
}
