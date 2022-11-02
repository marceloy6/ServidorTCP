/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.eventos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.hilos.HiloMensajesCliente;

/**
 *
 * @author Marcelo
 */
public class Cliente implements Serializable {
    
    private transient Socket socketCliente;
    private transient DataInputStream flujoentrada;
    private transient DataOutputStream flujosalida;
    
    private String nombrePC;
    private String IP;
    private long fechahora;
    private long id;
    private long idref;
    
    private String user;
    private String password;
    private int logueado;
    private boolean conectado;
    
    private transient HiloMensajesCliente hiloMensajesCliente;
    
    public Cliente() {
        this.socketCliente = null;
        this.nombrePC = "";
        this.IP = "";
        this.fechahora = System.currentTimeMillis();
        this.id = -1;
        this.idref = -1;
        
        this.hiloMensajesCliente = null;
        
        this.user = "";
        this.password = "";
        this.logueado = 0;
        this.conectado = false;
        
        this.flujoentrada = null;
        this.flujosalida = null;
    }
    

    public Cliente(Socket socketCliente) {
        this.socketCliente = socketCliente;
        this.nombrePC = socketCliente.getInetAddress().getHostName();
        this.IP = socketCliente.getInetAddress().getHostAddress();
        this.fechahora = System.currentTimeMillis();
        this.id = -1;
        this.idref = -1;
        this.hiloMensajesCliente = null;
        
        this.user = "";
        this.password = "";
        this.logueado = 0;
        this.conectado = true;
        
        try {
            this.flujoentrada =  new DataInputStream(socketCliente.getInputStream());
            this.flujosalida =  new DataOutputStream(socketCliente.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Socket getSocketCliente() {
        return socketCliente;
    }

    public void setSocketCliente(Socket socketCliente) {
        this.socketCliente = socketCliente;
    }

    public String getNombrePC() {
        return nombrePC;
    }

    public void setNombrePC(String nombrePC) {
        this.nombrePC = nombrePC;
    }

    public long getFechahora() {
        return fechahora;
    }

    public void setFechahora(long fechahora) {
        this.fechahora = fechahora;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdref() {
        return idref;
    }

    public void setIdref(long idref) {
        this.idref = idref;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DataInputStream getFlujoentrada() {
        return flujoentrada;
    }

    public DataOutputStream getFlujosalida() {
        return flujosalida;
    }

    public HiloMensajesCliente getHiloMensajesCliente() {
        return hiloMensajesCliente;
    }

    public void setHiloMensajesCliente(HiloMensajesCliente hiloMensajesCliente) {
        this.hiloMensajesCliente = hiloMensajesCliente;
    }
    
    private int getCode() {
        return this.hashCode();
    }
    
    
    

    public void setLogueado(int logueado) {
        this.logueado = logueado;
    }
    
    public int getLogueado() {
        return logueado;
    }
    
    public void Loguear() {
        logueado = logueado + 1;
        conectado = true;
    }
    
    public void Desloguear() {
        logueado = logueado - 1;
    }
    
    
    
    
    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }
    
    public void Desconectar() {
        try {
            if (hiloMensajesCliente != null )
                hiloMensajesCliente.Detener();
            flujoentrada.close();
            flujosalida.close();
            socketCliente.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void Copiar(Cliente cliente) {
//        this.user = cliente.getUser();
//        this.password = cliente.getPassword();
//        
//        nombrePC = cliente.getNombrePC();
//        fechahora = cliente.getFechahora();
//        IP = cliente.getIP();
//        
//        logueado = cliente.isLogueado();
//        conectado = cliente.isConectado();
//    }
//    
//    public void Anular() {
//        this.user = "";
//        this.password = "";
//    }
    
//    public boolean esIgualA(ClienteTmp clienteTmp) {
//        //convertir esta Funcion a numerico para poder enviar mas valores, e identificar en que fallo
//        return (this.id == clienteTmp.getId()) && 
//                (this.user.equals(clienteTmp.getUser())) && 
//                (this.password.equals(clienteTmp.getPass()));
//    }
    
    public boolean estaRegistrado() {
        return (!user.isEmpty() && !password.isEmpty());
    }
    
}
