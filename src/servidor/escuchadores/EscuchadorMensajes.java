/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.escuchadores;

import servidor.eventos.EventoMensaje;
import java.util.EventListener;
import servidor.eventos.EventoCliente;
import servidor.eventos.EventoClienteTmp;

/**
 *
 * @author Marcelo
 */
public interface EscuchadorMensajes extends EventListener {
    
    public void OnMensajeRecivido(EventoMensaje eventoMensaje);
    
    public void OnLogin(EventoClienteTmp eventoClienteTmp);
    
    public void OnRegistrar(EventoClienteTmp eventoClienteTmp);
    
}
