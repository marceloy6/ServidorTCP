/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorjuego.eventos;

import java.util.EventObject;

/**
 *
 * @author Marcelo
 */
public class EventoOpcionJuego extends EventObject {
    
    private OpcionJuego opcionJuego;
    
    public EventoOpcionJuego(Object source, OpcionJuego opcionJuego) {
        super(source);
        this.opcionJuego = opcionJuego;
    }

    public OpcionJuego getOpcionJuego() {
        return opcionJuego;
    }

    public void setOpcionJuego(OpcionJuego opcionJuego) {
        this.opcionJuego = opcionJuego;
    }
    
    
    
}
