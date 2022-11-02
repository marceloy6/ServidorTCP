/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.hilos;

import servidor.Despachador;
import servidor.ServidorUtils;
import servidor.eventos.Cliente;
import servidor.eventos.ClienteTmp;
import servidor.eventos.EventoClienteTmp;
import servidor.eventos.EventoMensaje;
import servidorjuego.eventos.EventoOpcionJuego;
import servidorjuego.eventos.OpcionJuego;

/**
 *
 * @author Marcelo
 */
public class TaskLeerComando extends Thread {
    
    private String mensaje;
    private EventoMensaje eventoMensaje;
    
    public TaskLeerComando(EventoMensaje eventoMensaje) {
        mensaje = eventoMensaje.getMensaje().getMensajeCliente();
        this.eventoMensaje = eventoMensaje;
    }

    @Override
    public void run() {
        //IDENTIFICAR COMANDO
        switch (ServidorUtils.LeerCOMANDO(mensaje)){
            case ServidorUtils.COMCLI_LOGIN:
                //#LOGIN,ID=1651651,user=juan,pass=31316516sd
                //leemos los valores del Cliente que quiere loguearse              
                
                //notificamos del nuevo intento de logueo (para que sea verificado)                
                Despachador.DespacharEventoLogin(new EventoClienteTmp(this, ServidorUtils.LeerClienteTmp(mensaje)));
                break;
            case ServidorUtils.COMCLI_REGISTRAR:
                //#REGISTRAR,ID=9156513,user=juan,pass=4565161sdf
                //Leemos los valores del cliente que quiere registrarse
                
                //notificamos del nuevo intento de registro (para que sea verificado)
                Despachador.DespacharEventoRegistrar(new EventoClienteTmp(this, ServidorUtils.LeerClienteTmp(mensaje)));
                break;
                
            case ServidorUtils.COMCLI_COMENZARPARTIDA:
                Despachador.DespacharEventoComenzarPartida(new EventoOpcionJuego(this, new OpcionJuego(eventoMensaje.getMensaje().getIdClienteEmisor(), mensaje)));
                break;
                
            case ServidorUtils.COMCLI_GASTARTURNO:
                Despachador.DespacharEventoGastarTurno(new EventoOpcionJuego(this, new OpcionJuego(eventoMensaje.getMensaje().getIdClienteEmisor(), mensaje)));
                break;
            default:
                System.out.println("\tComando no encontrado!!!");
        }
    }
    
}
