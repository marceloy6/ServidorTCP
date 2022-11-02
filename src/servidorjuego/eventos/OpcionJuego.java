/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorjuego.eventos;

/**
 *
 * @author Marcelo
 */
public class OpcionJuego {
    
    private long idUserEmisor;
    private String mensaje;
    
    public OpcionJuego(long idUserEmisor, String mensaje) {
        this.idUserEmisor = idUserEmisor;
        this.mensaje = mensaje;
    }

    public long getIdUserEmisor() {
        return idUserEmisor;
    }

    public void setIdUserEmisor(long idUserEmisor) {
        this.idUserEmisor = idUserEmisor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    
    
}
