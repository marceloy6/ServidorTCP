/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorjuego;

import java.util.ArrayList;
import servidor.eventos.EventoCliente;

/**
 *
 * @author Marcelo
 */
public class Partida {
    
    //Contiene las EventoCliente Referenciado, no al EventoCliente que lo apunta
    private ArrayList<Jugador> listaJugadores;
    private boolean iniciada;
    
    public Partida() {
        listaJugadores = new ArrayList<>();
        iniciada = false;
    }

    public ArrayList<Jugador> getListaJugadores() {
        return listaJugadores;
    }

    public void setListaJugadores(ArrayList<Jugador> listaJugadores) {
        this.listaJugadores = listaJugadores;
    }
    
    public Jugador ObtenerJugador(long id) {
        Jugador jres = null;
        for (Jugador jugador : listaJugadores) {
            if (id == jugador.getEventoCliente().getCliente().getId()) {
                jres = jugador;
            }
        }
        return jres;
    }
    
    
    public boolean fueIniciada(){
        return iniciada;
    }
    
    public void setIniciada(boolean valor) {
        iniciada = valor;
    }
    
}
