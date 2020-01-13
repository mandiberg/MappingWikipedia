/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikiparticipants;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Danara
 */
public class WikiParticipants {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        try {
            int i = 1;
            while(true){
            SQLProcessor processSQL = new SQLProcessor(i);
//            processSQL.createTableForIpInfo();
            processSQL.getUniqueIPs();
            i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(WikiParticipants.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
