/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.eventos;

/**
 *
 * @author Marcelo
 */
public class ClienteTmp {
    
    private long id;
    private String user;
    private String pass;
    
    public ClienteTmp() {
        this.id = 0;
        this.user = "";
        this.pass = "";
    }

    public ClienteTmp(long id, String user, String pass) {
        this.id = id;
        this.user = user;
        this.pass = pass;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    
    
}
