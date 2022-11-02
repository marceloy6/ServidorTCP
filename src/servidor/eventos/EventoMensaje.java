/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.eventos;

import java.util.EventObject;

/**
 *
 * @author Marcelo
 */
public class EventoMensaje extends EventObject {
    
    private Mensaje mensaje;
    
    public EventoMensaje(Object source, Mensaje mensaje) {
        super(source);
        this.mensaje = mensaje;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }
    
    
    
}
