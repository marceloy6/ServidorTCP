/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import javax.swing.event.EventListenerList;
import servidor.escuchadores.EscuchadorConexiones;
import servidor.escuchadores.EscuchadorMensajes;
import servidor.eventos.EventoCliente;
import servidor.eventos.EventoClienteTmp;
import servidor.eventos.EventoMensaje;
import servidorjuego.escuchadores.EscuchadorJuegoOpciones;
import servidorjuego.eventos.EventoOpcionJuego;

/**
 *
 * @author Marcelo
 */
public class Despachador {
    
    public static EventListenerList listaEscuchadores = new EventListenerList();
    
    public static void DespacharEventoConexion(EventoCliente eventoCliente) {
        EscuchadorConexiones[] escuchadores = listaEscuchadores.getListeners(EscuchadorConexiones.class);
        for (EscuchadorConexiones escuchador : escuchadores) {
            escuchador.OnClienteConectado(eventoCliente);
        }
    }
    
    public static void DespacharEventoDesconexion(EventoCliente eventoCliente) {
        EscuchadorConexiones[] escuchadores = listaEscuchadores.getListeners(EscuchadorConexiones.class);
        for (EscuchadorConexiones escuchador : escuchadores) {
            escuchador.OnClienteDesconectado(eventoCliente);
        }
    }
    
    public static void DespacharEventoMensajeRecibido(EventoMensaje eventoMensaje) {
        EscuchadorMensajes[] escuchadores = listaEscuchadores.getListeners(EscuchadorMensajes.class);
        for (EscuchadorMensajes escuchador : escuchadores) {
            escuchador.OnMensajeRecivido(eventoMensaje);
        }
    }
    
    
    public static void DespacharEventoLogin(EventoClienteTmp eventoClienteTmp) {
        EscuchadorMensajes[] escuchadores = listaEscuchadores.getListeners(EscuchadorMensajes.class);
        for (EscuchadorMensajes escuchador : escuchadores) {
            escuchador.OnLogin(eventoClienteTmp);
        }
    }
    
    public static void DespacharEventoRegistrar(EventoClienteTmp eventoClienteTmp) {
        EscuchadorMensajes[] escuchadores = listaEscuchadores.getListeners(EscuchadorMensajes.class);
        for (EscuchadorMensajes escuchador : escuchadores) {
            escuchador.OnRegistrar(eventoClienteTmp);
        }
    }
    
    public static void DespacharEventoComenzarPartida(EventoOpcionJuego eventoOpcionJuego) {
        EscuchadorJuegoOpciones[] escuchadores = listaEscuchadores.getListeners(EscuchadorJuegoOpciones.class);
        for (EscuchadorJuegoOpciones escuchador : escuchadores) {
            escuchador.OnComenzarPartida(eventoOpcionJuego);
        }
    }
    
    public static void DespacharEventoGastarTurno(EventoOpcionJuego eventoOpcionJuego) {
        EscuchadorJuegoOpciones[] escuchadores = listaEscuchadores.getListeners(EscuchadorJuegoOpciones.class);
        for (EscuchadorJuegoOpciones escuchador : escuchadores) {
            escuchador.OnGastarTurno(eventoOpcionJuego);
        }
    }
}
