
package lab1;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApp extends JFrame implements ActionListener {
    
    
        
    PalindromoAir avion;
    
    JTextField campoNombre;
    JTextArea consola;
    JButton[] arregloBotones;
    
    JButton btnVender;
    JButton btnCancelar;
    JButton btnBuscar;
    JButton btnImprimir;
    JButton btnIngresos;
    JButton btnDespachar;

    public MainApp() {
        avion = new PalindromoAir();

        setTitle("PalindromoAir - Venta de Boletos");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelArriba = new JPanel();
        JLabel etiquetaTitulo = new JLabel("Control de Vuelos - PalindromoAir");
        panelArriba.add(etiquetaTitulo);
        add(panelArriba, BorderLayout.NORTH);

        // Panel central (Asientos y Botones)
        JPanel panelCentro = new JPanel(new BorderLayout());

        // Cuadrícula de 5x6 para los asientos
        JPanel panelAsientos = new JPanel(new GridLayout(5, 6));
        arregloBotones = new JButton[30];

        for (int i = 0; i < 30; i++) {
            String codigo = avion.obtenerCodigoAsiento(i);
            arregloBotones[i] = new JButton(codigo);
            arregloBotones[i].setBackground(Color.GREEN); // Libre por defecto
            
            // Para saber qué asiento se presionó de forma sencilla
            final int pos = i;
            arregloBotones[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Ticket t = avion.asientos[pos];
                    String cod = avion.obtenerCodigoAsiento(pos);
                    if (t == null) {
                        JOptionPane.showMessageDialog(null, "El asiento " + cod + " está libre.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Asiento: " + cod + "\nPasajero: " + t.nombre + "\nPagó: $" + t.precioPagado);
                    }
                }
            });

            panelAsientos.add(arregloBotones[i]);
        }
        panelCentro.add(panelAsientos, BorderLayout.CENTER);

        // Panel derecho con los controles
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new GridLayout(8, 1, 5, 5));

        panelDerecho.add(new JLabel("Escriba el nombre:"));
        campoNombre = new JTextField(10);
        panelDerecho.add(campoNombre);

        btnVender = new JButton("Vender Boleto");
        btnCancelar = new JButton("Cancelar Boleto");
        btnBuscar = new JButton("Buscar Pasajero");
        btnImprimir = new JButton("Imprimir Pasajeros");
        btnIngresos = new JButton("Ver Ingresos");
        btnDespachar = new JButton("Despachar Vuelo");

        btnVender.addActionListener(this);
        btnCancelar.addActionListener(this);
        btnBuscar.addActionListener(this);
        btnImprimir.addActionListener(this);
        btnIngresos.addActionListener(this);
        btnDespachar.addActionListener(this);

        panelDerecho.add(btnVender);
        panelDerecho.add(btnCancelar);
        panelDerecho.add(btnBuscar);
        panelDerecho.add(btnImprimir);
        panelDerecho.add(btnIngresos);
        panelDerecho.add(btnDespachar);

        panelCentro.add(panelDerecho, BorderLayout.EAST);
        add(panelCentro, BorderLayout.CENTER);

        // Panel inferior para la consola
        JPanel panelAbajo = new JPanel(new BorderLayout());
        panelAbajo.add(new JLabel("Consola de mensajes:"), BorderLayout.NORTH);
        
        consola = new JTextArea(5, 30);
        consola.setEditable(false);
        JScrollPane barra = new JScrollPane(consola);
        panelAbajo.add(barra, BorderLayout.CENTER);
        
        add(panelAbajo, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        
        Object botonPresionado = evento.getSource();

        // vender
        if (botonPresionado == btnVender) {
            String nombre = campoNombre.getText();
            
           
            int asientoLibre = avion.firstAvailable(0);
            
            boolean resultadoVenta = avion.sellTicket(nombre);

            if (resultadoVenta && asientoLibre != -1) {
                String codigoAsiento = avion.obtenerCodigoAsiento(asientoLibre);
                Ticket t = avion.asientos[asientoLibre];
                
                
                arregloBotones[asientoLibre].setText(codigoAsiento + "\n" + t.nombre);
                
                if (t.palindromo) {
                    arregloBotones[asientoLibre].setBackground(Color.ORANGE); // Color palíndromo
                } else {
                    arregloBotones[asientoLibre].setBackground(Color.RED); // Ocupado normal
                }

                consola.append("Vendido: " + t.nombre + " en " + codigoAsiento + ", pagó $" + t.precioTot + "\n");
            } else {
                // Mensajes de error si falló la validación o el avión está lleno
                if (avion.firstAvailable(0) == -1) {
                    consola.append("Avión lleno. No se puede vender el boleto.\n");
                } else if (avion.searchPassenger(nombre, 0) != -1) {
                    consola.append("El pasajero ya tiene un asiento asignado.\n");
                } else {
                    consola.append("Ingrese un nombre válido.\n");
                }
            }
            campoNombre.setText("");
        }

        // buscar
        if (botonPresionado == btnBuscar) {
            String nombre = campoNombre.getText();
            int indice = avion.searchPassenger(nombre, 0);

            if (indice == -1) {
                consola.append(nombre + " no está en el vuelo.\n");
            } else {
                String codigo = avion.obtenerCodigoAsiento(indice);
                consola.append(nombre + " está en el asiento " + codigo + ".\n");
            }
            campoNombre.setText("");
        
        }

        // imprimir la informacion
        if (botonPresionado == btnImprimir) {
            String resultado = avion.printPassengers(0, true);
            if (resultado.equals("")) {
                consola.append("Sin pasajeros registrados.\n");
            } else {
                consola.append("--- LISTA DE PASAJEROS ---\n" + resultado);
            }
        }

        // ingresos
        if (botonPresionado == btnIngresos) {
            double total = avion.income(0,0);
            consola.append("Ingresos actuales: $" + total + "\n");
        }

        // despacho 
        if (botonPresionado == btnDespachar) {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea despachar el vuelo?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                double total = avion.income(0,0);
                avion.reset(0);

                
                for (int i = 0; i < 30; i++) {
                    String codigo = avion.obtenerCodigoAsiento(i);
                    arregloBotones[i].setText(codigo);
                    arregloBotones[i].setBackground(Color.GREEN);
                }

                consola.append("Vuelo despachado. Total ingresado: $" + total + "\n");
            }
        }
    }
    
}
