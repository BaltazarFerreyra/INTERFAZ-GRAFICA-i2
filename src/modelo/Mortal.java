package modelo;

public interface Mortal {

    boolean estaVivo();

    void morir();

    double getEnergia();

    String getNombre();

    default void verificarMuerte() {
        if (estaVivo() && getEnergia() <= 0) {
            morir();
        }
    }
}
