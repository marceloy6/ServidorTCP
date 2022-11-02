/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.hilos;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.Despachador;
import servidor.eventos.Cliente;
import servidor.eventos.EventoCliente;
import servidor.eventos.EventoMensaje;
import servidor.eventos.Mensaje;

/**
 *
 * @author Marcelo
 */
public class HiloMensajesCliente extends Thread {
    
    private DataInputStream flujoentrada;
    private EventoCliente eventoCliente;
    private boolean sw;
    
    public HiloMensajesCliente(EventoCliente eventoCliente) {
        this.flujoentrada = eventoCliente.getCliente().getFlujoentrada();
        this.eventoCliente = eventoCliente;
        this.sw = true;
    }
    

    @Override
    public void run() {
        while (sw) {            
            try {
                String mensajeServidor = flujoentrada.readUTF();
                
                //notificar de un nuevo mensaje
                Despachador.DespacharEventoMensajeRecibido(new EventoMensaje(this, new Mensaje(eventoCliente.getCliente().getSocketCliente(), mensajeServidor, eventoCliente.getCliente().getId())));
            } catch (IOException ex) {
                //Logger.getLogger(HiloMensajesCliente.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Socket Cliente desconectado. ERROR: " + ex.getMessage());
                sw = false;
            }
        }
        //motivo de desconexion
        Despachador.DespacharEventoDesconexion(eventoCliente);
        try {
            flujoentrada.close();
        } catch (IOException ex) {
            Logger.getLogger(HiloMensajesCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Detener() {
        sw = false;
    }
    
}
