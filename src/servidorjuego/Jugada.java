/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorjuego;

/**
 *
 * @author Marcelo
 */
public class Jugada {
    
    public static final String BALAS = "BALAS"; //radbtn_balas
    public static final String TONTOS = "TONTOS";
    public static final String TRENES = "TRENES";
    public static final String CUADRAS = "CUADRAS";
    public static final String QUINAS = "QUINAS";
    public static final String SENAS = "SENAS";
    
    public static final String ESCALERA = "ESCALERA";
    public static final String FULL = "FULL";
    public static final String POKER = "POKER";
    
    public static final String GRANDE = "GRANDE";
    
    public static final String GRANDE1 = "GRANDE1";
    public static final String GRANDE2 = "GRANDE2";
    
    
    public static void InsertarJugada(Jugador jugador, String nombreJugada, int valorJugada) {
        switch(nombreJugada) {
            case BALAS:
                jugador.PonerBala(valorJugada);
                break;
            case TONTOS:
                jugador.PonerTonto(valorJugada);
                break;
            case TRENES:
                jugador.PonerTren(valorJugada);
                break;
            case CUADRAS:
                jugador.PonerCuadra(valorJugada);
                break;
            case QUINAS:
                jugador.PonerQuina(valorJugada);
                break;
            case SENAS:
                jugador.PonerSena(valorJugada);
                break;
            
            case ESCALERA:
                jugador.PonerEscalera(valorJugada);
                break;
            case FULL:
                jugador.PonerFull(valorJugada);
                break;
            case POKER:
                jugador.PonerPoker(valorJugada);
                break;
            case GRANDE:
                jugador.PonerGrande(valorJugada);
                break;
        }
    }
}
