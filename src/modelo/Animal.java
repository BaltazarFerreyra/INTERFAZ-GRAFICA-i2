package modelo;

public abstract class Animal extends Entidad implements Mortal {

    private int velocidad;
    private double peso;

    public Animal(String nombre, double energia, int edad, int velocidad, double peso) {
        super(nombre, energia, edad);
        setVelocidad(velocidad);
        setPeso(peso);
    }

    public abstract void comer(Ecosistema eco);

    @Override
    public boolean estaVivo() {
        return isViva();
    }

    @Override
    public void morir() {
        setViva(false);
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = Math.max(1, velocidad);
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = Math.max(0.1, peso);
    }
}
