/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.escuchadores.EscuchadorConexiones;
import servidor.escuchadores.EscuchadorMensajes;
import servidor.eventos.Cliente;
import servidor.eventos.EventoCliente;
import servidor.eventos.EventoClienteTmp;
import servidor.eventos.EventoMensaje;
import servidor.hilos.HiloConexiones;
import servidor.hilos.HiloMensajesCliente;
import servidor.hilos.TaskLeerComando;
import servidor.hilos.TaskMensaje;
import servidorjuego.Jugador;
import servidorjuego.escuchadores.EscuchadorJuegoOpciones;
import servidorjuego.eventos.EventoOpcionJuego;

/**
 *
 * @author Marcelo
 */
public class ServidorSocket implements EscuchadorConexiones, EscuchadorMensajes, EscuchadorJuegoOpciones{
    
    private ServerSocket serverSocket;
    private int puerto;
    private HiloConexiones hiloConexiones;
//    private HashMap<Integer, EventoCliente> listaClientesConectados;
    private HashMap<Long, EventoCliente> listaClientesConectados;
    private ExecutorService ejecutor;
    private final String archivoRespaldo = "listaUsers.dat";
    
    public ServidorSocket( int puerto ){
        try {
            this.puerto = puerto;
            serverSocket =  new ServerSocket(puerto);
            hiloConexiones = new HiloConexiones(serverSocket);
            ejecutor =  Executors.newCachedThreadPool();
            
            System.out.println("Servidor creado!");
            
            if (!CargarLista())
                listaClientesConectados = new HashMap<>();
        } catch (IOException ex) {
//            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("No se puedo Crear al servidor > " + ex.toString());
        }
    }
    
    public void Iniciar(){
        
        try {
            Despachador.listaEscuchadores.add(EscuchadorConexiones.class, this);
            Despachador.listaEscuchadores.add(EscuchadorMensajes.class, this);
            Despachador.listaEscuchadores.add(EscuchadorJuegoOpciones.class, this);
            hiloConexiones.start();
            System.out.println("Servidor Iniciado en puerto: " + puerto);
            
            System.out.println("Tamanho lista Clientes Conectados = " + listaClientesConectados.size());
            System.out.println("Tamanho lista Clientes Partida = " + ServidorUtils.partida.getListaJugadores().size());
        } catch (Exception e) {
            System.out.println("Error > " + e.toString());
        }
    }
    
    public void Detener(){
        try {
            Despachador.listaEscuchadores.remove(EscuchadorConexiones.class, this);
            Despachador.listaEscuchadores.remove(EscuchadorMensajes.class, this);
            hiloConexiones.Detener();
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private long obtenerNuevoCodigo(){
        long key = System.currentTimeMillis();
        while (listaClientesConectados.containsKey(key))
            key = System.currentTimeMillis();
        return key; 
    }

    @Override
    public void OnClienteConectado(EventoCliente eventoCliente) {
        eventoCliente.getCliente().setConectado(true);
        eventoCliente.getCliente().setHiloMensajesCliente(new HiloMensajesCliente(eventoCliente));
        eventoCliente.getCliente().getHiloMensajesCliente().start();
        
        long key = obtenerNuevoCodigo();
        eventoCliente.getCliente().setId(key);
        listaClientesConectados.put(key, eventoCliente);
        
        //Responder enviando ID
        String mensaje = ServidorUtils.COMSERV_IDENTIFICAR + "," + 
                ServidorUtils.USER_ID + "=" + eventoCliente.getCliente().getId();
        Future future = ejecutor.submit(new TaskMensaje(eventoCliente, mensaje));
        
        System.out.println("\n\u2588\u2588 Nuevo Cliente Conectado! ClienteID=" + eventoCliente.getCliente().getId() + "; HORA=" + new Date(eventoCliente.getCliente().getFechahora()));
        System.out.println("Tamanho lista Clientes Conectados = " + listaClientesConectados.size());
    }

    @Override
    public void OnClienteDesconectado(EventoCliente eventoCliente) {
        //Desconectamos a la conexionCliente
        eventoCliente.getCliente().Desconectar();
        System.out.println("Cliente ID = " + eventoCliente.getCliente().getId() + " DESCONECTADO !!");
        
        //Si No esta registrado lo borramos
        if (!eventoCliente.getCliente().estaRegistrado()) {
            listaClientesConectados.remove(eventoCliente.getCliente().getId());
            System.out.println("Cliente ID = " + eventoCliente.getCliente().getId() + " ELIMINADO !!");
        }
        
        //si era una conexionCliente que apuntaba a otra conexionCliente (Si la conexion se habia logueado)
        //  deslogueamos al Cliente apuntado, y marcamos como desconectado si ya no hay mas logueados (multilogueo)
        Cliente cliente = eventoCliente.getCliente();
        if (cliente.getIdref() != -1) {
            
            Jugador jugadorRef = ServidorUtils.partida.ObtenerJugador(cliente.getIdref());
            
            Cliente clienteRef = listaClientesConectados.get(cliente.getIdref()).getCliente();
            cliente.setIdref(-1); //quitamos su referencia
            clienteRef.Desloguear();
            System.out.println("Cliente ID=" + clienteRef.getId() + " CantidadLogueos=" + clienteRef.getLogueado());
            if (clienteRef.getLogueado()<1) {
                clienteRef.setConectado(false);
                
                String jugadorRefTXT = ServidorUtils.CHAR_DELIMITADOR
                        + ServidorUtils.USER_ID + ServidorUtils.CHAR_IGUALDAD + jugadorRef.getID() + "&"
                        + ServidorUtils.USER_USER + ServidorUtils.CHAR_IGUALDAD + jugadorRef.getUser() + "&"
                        + "conectado" + ServidorUtils.CHAR_IGUALDAD + jugadorRef.getConectado() + "&"
                        + "turno" + ServidorUtils.CHAR_IGUALDAD + jugadorRef.isTurno();

                //Notificando a los usuarios logueados y conectados, de esta desconexion
                System.out.println("\t\tUn cliente Logueado se Desconecto. Notificando al Resto");
                for (EventoCliente value : listaClientesConectados.values()) {
                    if (value.getCliente().getIdref()!=-1) {
                        String mensaje = ServidorUtils.COMSERV_UNJUGADOR_DESCONECTADO + jugadorRefTXT;
                        Future future = ejecutor.submit(new TaskMensaje(value, mensaje ));
                    }
                }
            }
            System.out.println("Cliente ID = " + clienteRef.getId() + " EstadoConexion = " + clienteRef.isConectado());
        }
        System.out.println("Tamanho lista Clientes Conectados = " + listaClientesConectados.size());
    }

    @Override
    public void OnMensajeRecivido(EventoMensaje eventoMensaje) {
        //prueba de mensaje recibido
        System.out.println("Nuevo Mensaje Recibido = " + eventoMensaje.getMensaje().getMensajeCliente());
        
        //IDENTIFICAR COMANDO
        Future future = ejecutor.submit(new TaskLeerComando(eventoMensaje));
    }

    @Override
    public void OnLogin(EventoClienteTmp eventoClienteTmp) {
        System.out.println("INTENTO DE LOGUEO DE cliente = " + eventoClienteTmp.getClienteTmp().getId());
        
        //buscamos al Cliente en el HashMap, para cambiarle su estado a logueado, y conectado, y asignarle su nuevo ID
        byte resp = 0;
        long newID = -1;
        EventoCliente ecConexion = listaClientesConectados.get(eventoClienteTmp.getClienteTmp().getId()); // lo necesito para poder responderle
        for (EventoCliente ecIntentoDeLogin : listaClientesConectados.values()) {
            if (ecIntentoDeLogin.getCliente().getUser().equals(eventoClienteTmp.getClienteTmp().getUser())) {
                //encontrado pero no es la contrasenha
                resp = 2;
                if (ecIntentoDeLogin.getCliente().getPassword().equals(eventoClienteTmp.getClienteTmp().getPass())) {
                    //encontrado y logueado
                    resp = 1;
                    
                    ecIntentoDeLogin.getCliente().Loguear();
                    
                    newID = ecIntentoDeLogin.getCliente().getId();
                    ecConexion.getCliente().setIdref(newID);
                    
                    System.out.println("Cliente ID=" + ecIntentoDeLogin.getCliente().getId() + " CantidadLogueos=" + ecIntentoDeLogin.getCliente().getLogueado());
                    System.out.println("Cliente ID=" + ecIntentoDeLogin.getCliente().getId() + " EstadoConexion = " + ecIntentoDeLogin.getCliente().isConectado());
                }
                break;
            }
        }
        String mensaje = ServidorUtils.COMSERV_LOGIN_OK + ServidorUtils.CHAR_DELIMITADOR + 
                resp + ServidorUtils.CHAR_DELIMITADOR + 
                ServidorUtils.USER_ID + ServidorUtils.CHAR_IGUALDAD + newID;
        
        Future future = ejecutor.submit(new TaskMensaje(ecConexion, mensaje));
    }

    @Override
    public void OnRegistrar(EventoClienteTmp eventoClienteTmp) {
        System.out.println("INTENTO DE REGISTRO DE cliente = " + eventoClienteTmp.getClienteTmp().getId());
        
        //Verificamos que no exista el nombreusuario en el HASHMAP, antes de registrar.
        boolean isOK = true;
        for (EventoCliente value : listaClientesConectados.values()) {
            if (value.getCliente().getUser().equals(eventoClienteTmp.getClienteTmp().getUser())) {
                isOK = false;
                break;
            }
        }
        
        String mensaje;
        EventoCliente ec = listaClientesConectados.get(eventoClienteTmp.getClienteTmp().getId());
        if (isOK) {
            //registramos los valores en un nuevo elemento, y lo guardamos en la lista
            Cliente newCliente = new Cliente();
            
            System.out.println("\tBuscando nuevo Codigo...");
            long key = obtenerNuevoCodigo();
            System.out.println("\tNuevo Codigo Solicitado es >" + key);
            newCliente.setId(key);
            newCliente.setUser(eventoClienteTmp.getClienteTmp().getUser());
            newCliente.setPassword(eventoClienteTmp.getClienteTmp().getPass());
            newCliente.setIP(ec.getCliente().getIP());
            newCliente.setNombrePC(ec.getCliente().getNombrePC());
            
            EventoCliente newEc = new EventoCliente(this, newCliente);
            
            listaClientesConectados.put(key, newEc);
            System.out.println("\tCreado Nuevo EVENTOCLIENTE ID= " + key);
            System.out.println("\tTamanho lista Clientes Conectados = " + listaClientesConectados.size());
            
            mensaje = ServidorUtils.COMSERV_REGISTRO_OK + ServidorUtils.CHAR_DELIMITADOR + 1;
            
            //almacenamos al cliente en el archivo
            //si hay muchos clientes conectados(sin registrar) igual los almacena por eso guarda datos sin valor en el file
            //al registrar a Un cliente nuevo, va a registrar el "login" y "conectado" de todos los clientes.
                        //por eso DEPURAMOS al iniciar el servidor
            Almacenar();
        } else {
            mensaje = ServidorUtils.COMSERV_REGISTRO_OK + ServidorUtils.CHAR_DELIMITADOR + 0;
        }
        Future future = ejecutor.submit(new TaskMensaje(ec, mensaje));
        
    }
    
    private void DepurarLista() {
        MostrarUsuariosEnLista();
        int sizeAntes = listaClientesConectados.size();
        ArrayList<Long> keysNoRegistrados = new ArrayList<>();
        for (EventoCliente value : listaClientesConectados.values()) {
            if (!value.getCliente().estaRegistrado()) {
                keysNoRegistrados.add(value.getCliente().getId());
            } else {
                value.getCliente().setLogueado(0);
                value.getCliente().setConectado(false);
            }
        }
        for (int i = 0; i < keysNoRegistrados.size(); i++) {
            listaClientesConectados.remove(keysNoRegistrados.get(i));
        }
        if (sizeAntes!=listaClientesConectados.size()) {
            Almacenar();
            MostrarUsuariosEnLista();
        }
    }
    
    private void BorrarUsuario(long id) {
        listaClientesConectados.remove(id);
        Almacenar();
        MostrarUsuariosEnLista();
    }
    
    private boolean CargarLista() {
        boolean res = false;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoRespaldo));
            listaClientesConectados = (HashMap<Long, EventoCliente>) ois.readObject();
            DepurarLista();
            ois.close();
            res = true;
            System.out.println("LISTA CARGADA !!");
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no Encontrado!! ");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    
    private void Almacenar() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoRespaldo));
            oos.writeObject(listaClientesConectados);
            oos.close();
            System.out.println("ALMACENADO !!");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void MostrarUsuariosEnLista() {
        System.out.println("\tContenido de la Lista de Usuario Registrados (user,pass).  TamanhoLista= " + listaClientesConectados.size());
        for (Map.Entry<Long, EventoCliente> entry : listaClientesConectados.entrySet()) {
            Long key = entry.getKey();
            EventoCliente value = entry.getValue();
            String texto = "\t\tid=" + value.getCliente().getId() + 
                    " , user= " + value.getCliente().getUser() + 
                    " , pass= " + value.getCliente().getPassword() + 
                    " , logueado= " + value.getCliente().getLogueado() + 
                    " , conectado= " + value.getCliente().isConectado() +
                    " , idref= " + value.getCliente().getIdref();
            System.out.println(texto);
        }
    }
    
    private String ObtenerListaJugadoresEnPartida() {
        // ",iduser=51651651651 & user=juan & conectado=false"
        // ""
        String listajugadores = "";
        for (Jugador jugador : ServidorUtils.partida.getListaJugadores()) {
            listajugadores = listajugadores + ServidorUtils.CHAR_DELIMITADOR
                    + ServidorUtils.USER_ID + ServidorUtils.CHAR_IGUALDAD + jugador.getID()+ "&"
                    + ServidorUtils.USER_USER + ServidorUtils.CHAR_IGUALDAD + jugador.getUser() + "&"
                    + "conectado" + ServidorUtils.CHAR_IGUALDAD + jugador.getConectado() + "&"
                    + "turno" + ServidorUtils.CHAR_IGUALDAD + jugador.isTurno() + "&"
                    
                    + "ganador"+ ServidorUtils.CHAR_IGUALDAD + jugador.isGanador();
        }
        return listajugadores;
    }
    
    public void AgregarJugadorAPartida(EventoCliente eventoClienteRef) {
        //verificamos si no esta ya en partida, y lo agregamos en caso contrario
        boolean estaEnPartida = false;
        for (Jugador jugador : ServidorUtils.partida.getListaJugadores()) {
            if (eventoClienteRef.getCliente().getId() == jugador.getID()) {
                estaEnPartida = true;
            }
        }
        if( !estaEnPartida ) {
            Jugador jugador = new Jugador(eventoClienteRef);
            if (ServidorUtils.partida.getListaJugadores().isEmpty()) {
                jugador.setTurno(true);
            } else {
                jugador.setTurno(false);
            }
            ServidorUtils.partida.getListaJugadores().add(jugador);
        }
        System.out.println("\t JUGADORES EN PARTIDA = " + ServidorUtils.partida.getListaJugadores().size());
    }

    @Override
    public void OnComenzarPartida(EventoOpcionJuego eventoOpcionJuego) {
        System.out.println("INTENTO DE COMIENZO DE PARTIDA del cliente = " + eventoOpcionJuego.getOpcionJuego().getIdUserEmisor());
        //Agregamos a la PARTIDA
        EventoCliente ecEmisor = listaClientesConectados.get(eventoOpcionJuego.getOpcionJuego().getIdUserEmisor());
        EventoCliente ecRef = listaClientesConectados.get( ecEmisor.getCliente().getIdref() );
        
        AgregarJugadorAPartida(ecRef);
        
        //Confirmamos el Comienzo de Partida y lo enviamos, con la lista de usuarios en la partida
        String mensaje = ServidorUtils.COMSERV_COMENZARPARTIDA_OK  + ObtenerListaJugadoresEnPartida();
        Future future = ejecutor.submit(new TaskMensaje(ecEmisor, mensaje));
        
        //Notificamos a LOS DEMAS jugadores(en la Partida) que este jugador InicioPartida / Se unio a la partida
        while (!future.isDone()) {}
        System.out.println("\t SE COMPLETO EL COMIENZOPARTIDA del cliente = " + ecEmisor.getCliente().getId());
        System.out.println("\t NOTIFICANDO AL RESTO DE JUGADORES de este COMIENZOPARTIDA...");
        
        Jugador thisJugador = ServidorUtils.partida.ObtenerJugador(ecRef.getCliente().getId());
        String thisJugadorTXT = ServidorUtils.CHAR_DELIMITADOR
                + ServidorUtils.USER_ID + ServidorUtils.CHAR_IGUALDAD + thisJugador.getID() + "&"
                + ServidorUtils.USER_USER + ServidorUtils.CHAR_IGUALDAD + thisJugador.getUser() + "&"
                + "conectado" + ServidorUtils.CHAR_IGUALDAD + thisJugador.getConectado() + "&"
                + "turno" + ServidorUtils.CHAR_IGUALDAD + thisJugador.isTurno();

        for (Jugador jugador : ServidorUtils.partida.getListaJugadores()) {
            if (jugador.getID() != thisJugador.getID()) {
                
                for (EventoCliente value : listaClientesConectados.values()) {
                    if (value.getCliente().getIdref() == jugador.getID()) {
                        
                        if (value.getCliente().getId() != ecEmisor.getCliente().getId()) {
                            String mensajeAOtros = ServidorUtils.COMSERV_UNJUGADOR_CONECTADO + thisJugadorTXT;
                            Future future2 = ejecutor.submit(new TaskMensaje(value, mensajeAOtros));
                            System.out.println("\t NOTIFICADO A Cliente ID= " + value.getCliente().getId());
                        }
                    }
                }
            }
        }
    }
    
    private void PasarTurnoASiguienteJugador(Jugador jugador) {
        ArrayList<Jugador> lista = ServidorUtils.partida.getListaJugadores();
        if (lista.size() > 1) {
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getID() == jugador.getID()) {
                    jugador.setTurno(false);
                    int nextpos;
                    if (i + 1 == lista.size()) {
                        nextpos = 0;
                    } else {
                        nextpos = i + 1;
                    }
                    lista.get(nextpos).setTurno(true);
                    return;
                }
            }
        }
    }

    @Override
    public void OnGastarTurno(EventoOpcionJuego eventoOpcionJuego) {
        System.out.println("GASTO DE TURNO del cliente = " + eventoOpcionJuego.getOpcionJuego().getIdUserEmisor());
        
        EventoCliente ecEmisor = listaClientesConectados.get(eventoOpcionJuego.getOpcionJuego().getIdUserEmisor());
        EventoCliente ecRef = listaClientesConectados.get( ecEmisor.getCliente().getIdref() );
        Jugador thisJugador = ServidorUtils.partida.ObtenerJugador(ecRef.getCliente().getId());
        
        //#GASTARTURNO,ID=51651616516, ganador=false, SENAS=24
        //actualizando al jugador con los valores recibidos, para futuros usos
        ServidorUtils.ActualizarDatosDeJugador(thisJugador, eventoOpcionJuego.getOpcionJuego().getMensaje());
        
        PasarTurnoASiguienteJugador(thisJugador);
        
        //Creamos el mensaje, el cual incluya el movimiento realizado ( ID=51651616516 & SENAS=24 )
        String nuevoMovimiento = ServidorUtils.CHAR_DELIMITADOR +
                ServidorUtils.USER_ID + ServidorUtils.CHAR_IGUALDAD + thisJugador.getID() + "&" + 
                ServidorUtils.obtenerCampoJugadaFromTurno(eventoOpcionJuego.getOpcionJuego().getMensaje());
        
        //notificamos a TODOS los cliente del gasto de Turno, 
        for (Jugador jugador : ServidorUtils.partida.getListaJugadores()) {
            for (EventoCliente value : listaClientesConectados.values()) {
                if (value.getCliente().getIdref() == jugador.getID()) {
                    
                    String mensajeAOtros = ServidorUtils.COMSERV_GASTARTURNO_OK  + nuevoMovimiento + ObtenerListaJugadoresEnPartida();
                    Future future = ejecutor.submit( new TaskMensaje(value, mensajeAOtros) );
                    System.out.println("\t NOTIFICADO A Cliente ID= " + value.getCliente().getId());
                }
            }
        }
    }
    
}
