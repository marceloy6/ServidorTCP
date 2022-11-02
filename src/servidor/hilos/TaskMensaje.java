/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.hilos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.Despachador;
import servidor.ServidorUtils;
import servidor.eventos.EventoCliente;

/**
 *
 * @author Marcelo
 */
public class TaskMensaje extends Thread {
    
    private DataOutputStream flujosalida;
    private EventoCliente eventoCliente;
    private String mensaje;
    
    public TaskMensaje(EventoCliente eventoCliente, String mensaje) {
        this.eventoCliente = eventoCliente;
        this.flujosalida= eventoCliente.getCliente().getFlujosalida();
        this.mensaje = mensaje;
//        try {
//            //Sugerencia: agregar un atributo DataOutStream en EventoCliente para no crearlo reiteradamente...
//            this.flujosalida= new DataOutputStream(eventoCliente.getCliente().getSocketCliente().getOutputStream());
//            this.mensaje = mensaje;
//        } catch (IOException ex) {
//            Logger.getLogger(TaskMensaje.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void run() {
        try {
            flujosalida.writeUTF(mensaje);
            System.out.println("Mensaje enviado al Cliente ID=" + eventoCliente.getCliente().getId());
            System.out.println("Mensaje enviado texto=" + mensaje);
        } catch (IOException ex) {
            Logger.getLogger(TaskMensaje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
