package modelo;

public class Conejo extends Animal implements Reproducible {

    private Ecosistema ecoRef;

    public Conejo(String nombre, double energia, int edad, int velocidad, double peso) {
        super(nombre, energia, edad, velocidad, peso);
    }

    @Override
    public void actuar(Ecosistema eco) {
        if (!isViva()) return;
        this.ecoRef = eco;

        comer(eco);
        if (isViva()) {
            intentarReproduccion(eco);
        }
    }

    @Override
    public void comer(Ecosistema eco) {
        if (!isViva() || eco == null) return;

        Planta plantaElegida = eco.obtenerPlantaVivaAleatoria();

        if (plantaElegida != null) {
            double valorNutricional = plantaElegida.serComida();
            eco.registrarMuertePlanta(plantaElegida);
            if (valorNutricional < 0) {
                descontarEnergia(-valorNutricional);
                String tag = getEnergia() < 20 ? " [PELIGRO: energia=" + (int)getEnergia() + "]" : "";
                eco.registrarEvento("Conejo '" + getNombre() + "' comio la planta toxica '"
                        + plantaElegida.getNombre() + "' (-30 energia por veneno)" + tag);
            } else {
                sumarEnergia(valorNutricional);
                eco.registrarEvento("Conejo '" + getNombre() + "' comio '"
                        + plantaElegida.getNombre() + "' (+" + (int)valorNutricional + " energia)");
            }
        } else {
            descontarEnergia(15.0);
            String tag = getEnergia() < 20 ? " [PELIGRO: energia=" + (int)getEnergia() + "]" : "";
            eco.registrarEvento("Conejo '" + getNombre() + "' no encontro comida (-15 energia)" + tag);
        }
    }

    @Override
    public boolean puedeReproducirse() {
        if (!isViva() || getEnergia() <= 60.0) {
            return false;
        }
        if (ecoRef != null) {
            return ecoRef.contarConejosVivos() >= 2;
        }
        return true;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        if (eco == null) return;
        descontarEnergia(25.0);

        Conejo nuevoConejo = eco.crearNuevoConejo();
        eco.registrarNacimientoConejo(nuevoConejo);
        eco.registrarEvento("Conejo '" + getNombre() + "' se reprodujo -> nuevo conejo '"
                + nuevoConejo.getNombre() + "' (energia: " + (int)nuevoConejo.getEnergia() + ")");
    }
}
