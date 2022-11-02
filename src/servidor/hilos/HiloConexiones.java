/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.hilos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.Despachador;
import servidor.eventos.Cliente;
import servidor.eventos.EventoCliente;

/**
 *
 * @author Marcelo
 */
public class HiloConexiones extends Thread {
    
    private ServerSocket serverSocket;
    private boolean sw;
    
    public HiloConexiones(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        sw = true;
    }

    @Override
    public void run() {
        while (sw) {            
            try {
                System.out.println("Esperando conexiones...");
                Socket socket = serverSocket.accept();
                
                //notificar sobre la nueva conexion
                Despachador.DespacharEventoConexion(new EventoCliente(this, new Cliente(socket)));
            } catch (IOException ex) {
                Logger.getLogger(HiloConexiones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean estaCorriendo() {
        return sw;
    }
    
    public void Detener() {
        sw = false;
    }
    
    
    
}
