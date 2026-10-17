
package lab1;


public class Ticket {
    String nombre;
    double precioOg;
    double precioTot;
    boolean palindromo;

    public Ticket(String nombre, double precioOg, double precioTot, boolean palindromo) {
        this.nombre = nombre;
        this.precioOg = precioOg;
        this.precioTot = precioTot;
        this.palindromo = palindromo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioOg() {
        return precioOg;
    }

    public double getPrecioTot() {
        return precioTot;
    }

    public boolean isPalindromo() {
        return palindromo;
    }
    
    
    
}
