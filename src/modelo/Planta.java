package modelo;

public class Planta extends Entidad implements Reproducible {

    private int tamanio;

    public Planta(String nombre, double energia, int edad, int tamanio) {
        super(nombre, energia, edad);
        setTamanio(tamanio);
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        if (tamanio < 1) {
            this.tamanio = 1;
        } else if (tamanio > 5) {
            this.tamanio = 5;
        } else {
            this.tamanio = tamanio;
        }
    }

    public double serComida() {
        double valorNutritivo = this.tamanio * 10.0;
        setEnergia(0.0);
        setViva(false);
        return valorNutritivo;
    }

    @Override
    public boolean puedeReproducirse() {
        return isViva() && getEnergia() >= 30.0;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        if (eco == null) return;
        Clima clima = eco.getClimaActual();
        if (clima == Clima.INVIERNO) {
            return;
        }

        descontarEnergia(15.0);

        double factor = clima.getFactorReproduccionPlantas();
        int crias = 1;
        if (factor >= 2.0) {
            crias = 2;
        } else if (factor == 0.5 && Math.random() < 0.5) {
            crias = 0;
        }

        for (int i = 0; i < crias; i++) {
            Planta nuevaPlanta = eco.crearNuevaPlanta();
            eco.registrarNacimientoPlanta(nuevaPlanta);
            eco.registrarEvento("Planta '" + getNombre() + "' se reprodujo -> nueva planta '"
                    + nuevaPlanta.getNombre() + "' (energia: " + (int)nuevaPlanta.getEnergia() + ")");
        }
    }

    @Override
    public void actuar(Ecosistema eco) {
        if (!isViva()) return;
        if (eco.getClimaActual() != Clima.INVIERNO) {
            intentarReproduccion(eco);
        }
    }
}
