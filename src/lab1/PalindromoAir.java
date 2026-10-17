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
}
