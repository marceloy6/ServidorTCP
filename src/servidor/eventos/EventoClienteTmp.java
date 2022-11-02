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
public class EventoClienteTmp extends EventObject {
    
    private ClienteTmp clienteTmp;
    
    public EventoClienteTmp(Object source, ClienteTmp clienteTmp) {
        super(source);
        this.clienteTmp = clienteTmp;
    }

    public ClienteTmp getClienteTmp() {
        return clienteTmp;
    }

    public void setClienteTmp(ClienteTmp clienteTmp) {
        this.clienteTmp = clienteTmp;
    }
    
    
    
}
