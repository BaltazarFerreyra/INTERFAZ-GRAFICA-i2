package modelo;

public abstract class Entidad {

    private String nombre;
    private double energia;
    private int edad;
    private boolean viva;

    public Entidad(String nombre, double energia, int edad) {
        setNombre(nombre);
        setEnergia(energia);
        setEdad(edad);
        this.viva = (this.energia > 0);
    }

    public abstract void actuar(Ecosistema eco);

    public void envejecer() {
        if (!viva) return;
        this.edad++;
        descontarEnergia(5.0);
    }

    public void descontarEnergia(double cantidad) {
        if (cantidad > 0) {
            setEnergia(this.energia - cantidad);
        }
    }

    public void sumarEnergia(double cantidad) {
        if (cantidad > 0 && viva) {
            setEnergia(this.energia + cantidad);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            this.nombre = "Entidad-SinNombre";
        } else {
            this.nombre = nombre.trim();
        }
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        if (energia <= 0) {
            this.energia = 0.0;
            this.viva = false;
        } else {
            this.energia = energia;
        }
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = Math.max(0, edad);
    }

    public boolean isViva() {
        return viva;
    }

    public void setViva(boolean viva) {
        this.viva = viva;
        if (!viva) {
            this.energia = 0.0;
        }
    }

    @Override
    public String toString() {
        return nombre + " (Energia: " + String.format("%.1f", energia) + ", Edad: " + edad + ")";
    }
}
