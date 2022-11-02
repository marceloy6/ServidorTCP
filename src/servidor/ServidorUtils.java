/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import servidor.eventos.Cliente;
import servidor.eventos.ClienteTmp;
import servidorjuego.Jugada;
import servidorjuego.Jugador;
import servidorjuego.Partida;
import servidorjuego.eventos.OpcionJuego;

/**
 *
 * @author Marcelo
 */
public class ServidorUtils {
    
    public static Partida partida = new Partida();
    
    //CAMPOS DE USUARIO
    public static final String USER_ID = "ID";
    public static final String USER_USER = "user";
    public static final String USER_PASS = "pass";
    
    public static final char CHAR_DELIMITADOR = ',';
    public static final char CHAR_IGUALDAD = '=';
    
    public static final String REGEX_DELIMITADOR = "\\s*,\\s*";
    public static final String REGEX_IGUAL = "\\s*=\\s*";
    
    //COMANDOS
    //envia
    public static final String COMSERV_IDENTIFICAR = "#CONEXION";
    public static final String COMSERV_LOGIN_OK = "#LOGINOK";
    public static final String COMSERV_REGISTRO_OK = "#REGISTROOK";
    public static final String COMSERV_COMENZARPARTIDA_OK = "#COMENZARPARTIDAOK";
    public static final String COMSERV_UNJUGADOR_CONECTADO = "#UNJUGADORCONECTADO";
    public static final String COMSERV_UNJUGADOR_DESCONECTADO = "#UNJUGADORDESCONECTADO";
    public static final String COMSERV_GASTARTURNO_OK = "#GASTARTURNOOK";
    
    //recibe
    public static final String COMCLI_LOGIN = "#LOGIN";
    public static final String COMCLI_REGISTRAR = "#REGISTRAR";
    public static final String COMCLI_COMENZARPARTIDA = "#COMENZARPARTIDA";
    public static final String COMCLI_GASTARTURNO = "#GASTARTURNO";
    
    
    public static abstract class VarsUtil {
        private static StringBuilder valor_sb = null;
        public static StringBuilder getValorSB(){
            if (valor_sb==null) {
                valor_sb = new StringBuilder();
            }
            return valor_sb;
        }
    }
    
    public static String LeerCOMANDO(String mensaje) {
        return mensaje.trim().split(REGEX_DELIMITADOR)[0];
    }
    
    public static String LeerOK(String mensaje) {
        //#COMSERV,1
        //#COMSERV,0
        return mensaje.split(REGEX_DELIMITADOR)[1];
    }
    
    public static ClienteTmp LeerClienteTmp(String mensaje) {
        //#LOGIN,ID=1651651,user=juan,pass=hola
        //#REGISTRAR,ID=9156513,user=juan,pass=hola
        ClienteTmp clienteTmp = new ClienteTmp();
        String[] tokensVal = mensaje.split(REGEX_DELIMITADOR);
        boolean error = false;
        for (int i = 1; i < tokensVal.length; i++) {
            //ID=9156513
            switch (LeerNOMBRECAMPO(tokensVal[i])) {
                case USER_ID:
                    try {
                        clienteTmp.setId(Long.parseLong(LeerValor(tokensVal[i])));
                    } catch (NumberFormatException numberFormatException) {
                        error = true;
                    }
                    break;
                case USER_USER:
                    clienteTmp.setUser(LeerValor(tokensVal[i]));
                    break;
                case USER_PASS:
                    clienteTmp.setPass(LeerValor(tokensVal[i]));
                    break;
            }
        }
        if (error)
            System.out.println("\tError al parsear al ClienteTmp!!");
        return clienteTmp;
    }
    
//    public static OpcionJuego LeerOpcionJuego(String mensaje) {
//        //#COMENZARPARTIDA,ID=56168416516
//        OpcionJuego opcionJuego = new OpcionJuego();
//        String[] tokensVal = mensaje.split(REGEX_DELIMITADOR);
//        
//        return opcionJuego;
//    }
    
    private static String LeerValor(String XigualValor) {
        //X=a;
        return XigualValor.split(REGEX_IGUAL)[1];
    }
    
    private static String LeerNOMBRECAMPO(String XigualValor) {
        //X=a;
        return XigualValor.split(REGEX_IGUAL)[0];
    }
    
    
    
    public static void ActualizarDatosDeJugador(Jugador jugador, String mensajeJugada) {
        //#GASTARTURNO,ID=51651616516, ganador=false, SENAS=24

        boolean ganador = Boolean.parseBoolean(LeerValor(mensajeJugada.split(REGEX_DELIMITADOR)[2]));
        String nombreJugada = LeerNOMBRECAMPO(obtenerCampoJugadaFromTurno(mensajeJugada));
        int valorJugada = Integer.parseInt(LeerValor(obtenerCampoJugadaFromTurno(mensajeJugada)));

        jugador.setGanador(ganador);
        Jugada.InsertarJugada(jugador, nombreJugada, valorJugada);
    }
    
    public static String obtenerCampoJugadaFromTurno(String mensajeTurno) {
        //#GASTARTURNO,ID=51651616516, ganador=false, SENAS=24
        return mensajeTurno.split(REGEX_DELIMITADOR)[3];
        //SENAS=24
    }
    
    public static String QuitarComando(String mensaje) {
        //#GASTARTURNO,ID=51651616516, ganador=false, SENAS=24
        String[] tokensVal = mensaje.split(REGEX_DELIMITADOR);
        String res = "";
        for (int i = 1; i < tokensVal.length; i++) {
            res = res + CHAR_DELIMITADOR + tokensVal[i];
        }
        return res;
    }
    
}
