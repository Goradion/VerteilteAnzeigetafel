package AC.Client;
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;


/**
 *
 * @author andrea
 */
public class deleteMessage implements Serializable {
    
    
    int Msg_ID;
    boolean remove;
    String name;
    
     public deleteMessage(String user, boolean remove, int Msg_ID) {
     
         this.name = user;
         this.remove = remove;
         this.Msg_ID = Msg_ID;
         
     }
     
    
    
    
}
