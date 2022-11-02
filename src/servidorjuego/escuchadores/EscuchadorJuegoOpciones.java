/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorjuego.escuchadores;

import java.util.EventListener;
import servidorjuego.eventos.EventoOpcionJuego;

/**
 *
 * @author Marcelo
 */
public interface EscuchadorJuegoOpciones extends EventListener {
    
    public void OnComenzarPartida(EventoOpcionJuego eventoOpcionJuego);
    
    public void OnGastarTurno(EventoOpcionJuego eventoOpcionJuego);
    
}
