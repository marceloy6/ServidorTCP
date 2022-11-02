/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorjuego;

import servidor.ServidorSocket;
import servidorjuego.escuchadores.EscuchadorJuegoOpciones;
import servidorjuego.eventos.EventoOpcionJuego;

/**
 *
 * @author Marcelo
 */
public class ServidorJuego implements EscuchadorJuegoOpciones{
    
    private static ServidorSocket servidorSocket;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        servidorSocket = new ServidorSocket(81);
        servidorSocket.Iniciar();
    }

    @Override
    public void OnComenzarPartida(EventoOpcionJuego eventoOpcionJuego) {
        //
    }

    @Override
    public void OnGastarTurno(EventoOpcionJuego eventoOpcionJuego) {
        ///
    }
    
}
