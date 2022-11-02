/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorjuego;

import servidor.ServidorUtils;
import servidor.eventos.EventoCliente;

/**
 *
 * @author Marcelo
 */
public class Jugador {
    
    private EventoCliente eventoCliente;
    
    private boolean turno;
    private boolean ganador;
            
    private final int dimf = 4;
    private final int dimc = 3;
    private final int[][] matriz = new int[dimf][dimc];
    private final int defaultValue = -1;
    
    private int cantGrandes = 0; 
    
    public Jugador(EventoCliente eventoCliente){
        this.eventoCliente = eventoCliente;
        turno = false;
        
        ganador = false;
        InicializarTablero();
    }
    
    private void InicializarTablero() {
        for (int i = 0; i < dimf; i++) {
            for (int j = 0; j < dimc; j++) {
                matriz[i][j] = defaultValue;
            }
        }
    }

    public EventoCliente getEventoCliente() {
        return eventoCliente;
    }

    public void setEventoCliente(EventoCliente eventoCliente) {
        this.eventoCliente = eventoCliente;
    }

    public boolean isTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }
    
    public long getID() {
        return eventoCliente.getCliente().getId();
    }
    
    public String getUser() {
        return eventoCliente.getCliente().getUser();
    }
    
    public boolean getConectado() {
        return eventoCliente.getCliente().isConectado();
    }

    public boolean isGanador() {
        return ganador;
    }

    public void setGanador(boolean ganador) {
        this.ganador = ganador;
    }
    
    

    public int getDimf() {
        return dimf;
    }

    public int getDimc() {
        return dimc;
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public int getDefaultValue() {
        return defaultValue;
    }
    
    
    
    public void PonerBala(int puntos) {
        matriz[0][0] = puntos;
    }
    
    public void PonerTonto(int puntos) {
        matriz[1][0] = puntos;
    }
    
    public void PonerTren(int puntos) {
        matriz[2][0] = puntos;
    }
    
    public void PonerCuadra(int puntos) {
        matriz[0][2] = puntos;
    }
    
    public void PonerQuina(int puntos) {
        matriz[1][2] = puntos;
    }
    
    public void PonerSena(int puntos) {
        matriz[2][2] = puntos;
    }
    
    public void PonerEscalera(int puntos) {
        matriz[0][1] = puntos;
    }
    
    public void PonerFull(int puntos) {
        matriz[1][1] = puntos;
    }
    
    public void PonerPoker(int puntos) {
        matriz[2][1] = puntos;
    }
    
    public boolean PonerGrande(int puntos) {
        cantGrandes = cantGrandes + 1;
        boolean res = false;
        if (cantGrandes == 1) {
            matriz[3][0] = puntos;
            res = true;
        }
        if (cantGrandes == 2) {
            matriz[3][1] = puntos;
            res = true;
        }
        System.out.println("\t\tSe puso una Grande = " + matriz[3][0] + ", " + matriz[3][1]);
        return res;
    }
    
    public boolean HayEspacioEnCasilla(String nombreCasilla){
        switch (nombreCasilla) {
            case Jugada.BALAS:
                return (matriz[0][0]==defaultValue);
            case Jugada.TONTOS:
                return (matriz[1][0]==defaultValue);
            case Jugada.TRENES:
                return (matriz[2][0]==defaultValue);
            case Jugada.CUADRAS:
                return (matriz[0][2]==defaultValue);
            case Jugada.QUINAS:
                return (matriz[1][2]==defaultValue);
            case Jugada.SENAS:
                return (matriz[2][2]==defaultValue);
            case Jugada.ESCALERA:
                return (matriz[0][1]==defaultValue);
            case Jugada.FULL:
                return (matriz[1][1]==defaultValue);
            case Jugada.POKER: 
                return (matriz[2][1]==defaultValue);
            case Jugada.GRANDE:
                return (matriz[3][0]==defaultValue || matriz[3][1]==defaultValue);
        }
        return false;
    }
    
    public int getValorCasilla(String nombreCasilla) {
        int res = defaultValue;
        switch (nombreCasilla) {
            case Jugada.BALAS:
                res = (matriz[0][0]);
                break;
            case Jugada.TONTOS:
                res = (matriz[1][0]);
                break;
            case Jugada.TRENES:
                res = (matriz[2][0]);
                break;
            case Jugada.CUADRAS:
                res = (matriz[0][2]);
                break;
            case Jugada.QUINAS:
                res = (matriz[1][2]);
                break;
            case Jugada.SENAS:
                res = (matriz[2][2]);
                break;
            case Jugada.ESCALERA:
                res = (matriz[0][1]);
                break;
            case Jugada.FULL:
                res = (matriz[1][1]);
                break;
            case Jugada.POKER: 
                res = (matriz[2][1]);
                break;
            case Jugada.GRANDE:
                if (matriz[3][0] != defaultValue) {
                    res = matriz[3][0];
                } else {
                    res = matriz[3][1];
                }
                break;
        }
        return res;
    }
    
    public String TableroToString() {
        String res;
        // BALAS=-1& ESCALERA=-1 & CUADRAS=-1 & TONTOS=-1 & FULL=-1 & QUINAS=-1 & TRENES=-1 & POKER=-1 & SENAS=24 & GRANDE1=-1 & GRANDE2=-1
        res =   Jugada.BALAS    + ServidorUtils.CHAR_IGUALDAD + matriz[0][0] + "&" +
                Jugada.TONTOS   + ServidorUtils.CHAR_IGUALDAD + matriz[1][0] + "&" +
                Jugada.TRENES   + ServidorUtils.CHAR_IGUALDAD + matriz[2][0] + "&" +
                Jugada.CUADRAS  + ServidorUtils.CHAR_IGUALDAD + matriz[0][2] + "&" +
                Jugada.QUINAS   + ServidorUtils.CHAR_IGUALDAD + matriz[1][2] + "&" +
                Jugada.SENAS    + ServidorUtils.CHAR_IGUALDAD + matriz[2][2] + "&" +
                Jugada.ESCALERA + ServidorUtils.CHAR_IGUALDAD + matriz[0][1] + "&" +
                Jugada.FULL     + ServidorUtils.CHAR_IGUALDAD + matriz[1][1] + "&" +
                Jugada.POKER    + ServidorUtils.CHAR_IGUALDAD + matriz[2][1] + "&" +
                Jugada.GRANDE1  + ServidorUtils.CHAR_IGUALDAD + matriz[3][0] + "&" +
                Jugada.GRANDE2  + ServidorUtils.CHAR_IGUALDAD + matriz[3][1];
        
        return res;
    }
    
}
