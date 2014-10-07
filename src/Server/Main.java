/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import java.io.IOException;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());
  
    public static Connection conn;
    
    public static boolean running;
    
    static {
        try {
            LOGGER.addHandler(new FileHandler("errors.log",true));
        }
        catch(IOException ex) {
            LOGGER.log(Level.WARNING,ex.toString(),ex);
        }
    }
    
    
    public Main() {
        running = true;
    }
    
    public static void main(String[] args) {
        Config.load();
        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(Config.db_host+Config.db_db, Config.db_user, Config.db_password);
            
            GameQueues.create();
            ServerListener.start();
            
        } catch(SQLException ex)
        {
            LOGGER.log(Level.SEVERE, "Couldn't connect to database (" + driver + ").");
        } catch (Exception ex) 
        {
            LOGGER.log(Level.SEVERE,ex.toString(),ex);
        } finally {
            try{
               if(conn!=null)
                  conn.close();
            } catch(SQLException se){
               se.printStackTrace();
            }
        }
    }
}