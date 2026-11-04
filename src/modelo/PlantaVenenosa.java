package modelo;

public class PlantaVenenosa extends Planta implements Peligroso {

    private int nivelPeligro;

    public PlantaVenenosa(String nombre, double energia, int edad, int tamanio) {
        super(nombre, energia, edad, tamanio);
        this.nivelPeligro = 4;
    }

    @Override
    public double serComida() {
        setEnergia(0.0);
        setViva(false);
        return -30.0;
    }

    @Override
    public int getNivelPeligro() {
        return nivelPeligro;
    }

    public void setNivelPeligro(int nivelPeligro) {
        this.nivelPeligro = Math.max(1, nivelPeligro);
    }
}
