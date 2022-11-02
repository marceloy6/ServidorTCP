/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.escuchadores;

import servidor.eventos.EventoCliente;
import java.util.EventListener;

/**
 *
 * @author Marcelo
 */
public interface EscuchadorConexiones extends EventListener {
    
    public void OnClienteConectado(EventoCliente eventoCliente);
    
    public void OnClienteDesconectado(EventoCliente eventoCliente);
    
}
