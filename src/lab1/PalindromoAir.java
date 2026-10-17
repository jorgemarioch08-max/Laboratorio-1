/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;

/**
 *
 * @author gigiw
 */
public class PalindromoAir {
    public final int capacidad = 30;
    public final double precioInicial = 100;
    public final double descuento = 0.20;
    
    Ticket[] asientos;
    
    //metodo para quitar tildes y mayusculas
    public PalindromoAir(){
        asientos = new Ticket[capacidad];
    }
    
    public String limpiarTexto(String texto) {
        if (texto == null) return "";

        String limpio = texto.toLowerCase();

        limpio = limpio.replace("á", "a")
                       .replace("é", "e")
                       .replace("í", "i")
                       .replace("ó", "o")
                       .replace("ú", "u")
                       .replace("ü", "u")
                       .replace("ñ", "n");

        String resultado = "";
        for (int i = 0; i < limpio.length(); i++) {
            char c = limpio.charAt(i);


            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
                resultado = resultado + c;
                }
            }

        return resultado;
    }
    
    public int firstAvailable(int index){
        if (index>=capacidad){
            return -1;
            
        }
        if (asientos[index] == null ) {
            return index;
        }
        return firstAvailable(index+1);
    }
    
    public int searchPassenger(String name, int index){
        if (index>=capacidad){
            return -1;
            
        }
        if (asientos[index] != null ) {
            String nombrePasajeroLimpio = limpiarTexto(asientos[index].nombre);
            String nombreBuscarLimpio = limpiarTexto(name);
            if(nombrePasajeroLimpio.equals(nombreBuscarLimpio)){
                return index;
            }
        }
        return searchPassenger(name, index + 1);
    }
    
    public boolean isPalindromo(String name) {
        if (name == null) {
            return false;
        }

        name = name.toLowerCase();
        name = name.replace('á', 'a');
        name = name.replace('é', 'e');
        name = name.replace('í', 'i');
        name = name.replace('ó', 'o');
        name = name.replace('ú', 'u');
        name = name.replace('ü', 'u');

        return comparar(name, 0, name.length() - 1, false);
    }
  
    //esto es  siempre de isPalindromo, solamente para eso se usa
    private boolean comparar(String name, int inicio, int fin, boolean encontroLetra) {
        if (inicio > fin) {
            return encontroLetra;
        }

        char primera = name.charAt(inicio);
        char ultima = name.charAt(fin);

        if (!((primera >= 'a' && primera <= 'z') || primera == 'ñ')) {
            return comparar(name, inicio + 1, fin, encontroLetra);
        }

        if (!((ultima >= 'a' && ultima <= 'z') || ultima == 'ñ')) {
            return comparar(name, inicio, fin - 1, encontroLetra);
        }

        if (primera != ultima) {
            return false;
        }

        return comparar(name, inicio + 1, fin - 1, true);
    }
    

    public void printPassengers(int index, boolean isEmpty) {
        if (index >= capacidad) {
            if (isEmpty) {
                System.out.println("No hay pasajeros registrados");
            }
            return;
        }

        if (asientos[index] != null) {
            isEmpty = false;

            System.out.println("Asiento: " + (index + 1));
            System.out.println("Nombre: " + asientos[index].getNombre());
            System.out.println("Monto original: $" + asientos[index].getPrecioOg());
            System.out.println("Monto pagado: $" + asientos[index].getPrecioTot());
            System.out.println("¿Es palíndromo?: " + asientos[index].isPalindromo());
        }

        printPassengers(index + 1, isEmpty);
    }
    
    public double income(int index, double income) {
        if (index >= capacidad) {
            return income;
        }

        if (asientos[index] != null) {
            income += asientos[index].getPrecioTot();
        }

        return income(index + 1, income);
    }

    public String obtenerCodigoAsiento(int index) {
        int fila = index / 6;
        int columna = (index % 6) + 1;
        char letraFila = (char) ('A' + fila);
        return letraFila + "" + columna;
    }

    private boolean tieneLetras(String name, int index) {
      if (index >= name.length()) {
          return false;
      }

      if (Character.isLetter(name.charAt(index))) {
          return true;
      }

      return tieneLetras(name, index + 1);
  }
    
  public boolean sellTicket(String name) {
    if (name == null || !tieneLetras(name, 0)) {
        System.out.println("Ingrese un nombre valido.");
        return false;
    }

    int posicion = firstAvailable(0);

    if (posicion == -1) {
        System.out.println("Avion lleno. No se puede vender el boleto.");
        return false;
    }

    if (searchPassenger(name, 0) != -1) {
        System.out.println("El pasajero ya tiene un asiento asignado.");
        return false;
    }

    boolean palindromo = isPalindromo(name);
    double precioFinal = precioInicial;

    if (palindromo) {
        precioFinal = precioInicial * (1 - descuento);
    }

    asientos[posicion] = new Ticket(name, precioInicial, precioFinal, palindromo);

    char fila = (char) ('A' + posicion / 6);
    int columna = posicion % 6 + 1;
    String codigo = "" + fila + columna;

    System.out.println("Vendido: " + name + " en " + codigo + ", pago $" + precioFinal);

    return true;
}
  
  public boolean cancelTicket(String name) {
    if (name == null || !tieneLetras(name, 0)) {
        System.out.println("Ingrese un nombre valido.");
        return false;
    }

    int posicion = searchPassenger(name, 0);

    if (posicion == -1) {
        System.out.println("No se encontro al pasajero. No se pudo cancelar.");
        return false;
    }

    String nombre = asientos[posicion].nombre;
    double reembolso = asientos[posicion].precioTot;

    asientos[posicion] = null;

    System.out.println("Cancelado: " + nombre + ". Reembolso de $" + reembolso);

    return true;
}

  public double dispatch() {
        double totalIngresos = income(0);
       
        reset(0);
        
        return totalIngresos;
  }
  

}
