/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import static Server.Main.LOGGER;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Wini;

/**
 *
 * @author amino_000
 */
public class Config {
    public static String user_username;
    public static String user_password;
    
    public static String db_host;
    public static String db_db;
    public static String db_user;
    public static String db_password;
    
    
    public static void load()
    {
        Wini ini;
        try {
            ini = new Wini(new File("config.ini"));

            //int age = ini.get("happy", "age", int.class);
            //double height = ini.get("happy", "height", double.class);

            user_username = ini.get("USER", "username");
            user_password = ini.get("USER", "password");
            
            db_host = ini.get("DB", "host");
            db_db = ini.get("DB", "db");
            db_user = ini.get("DB", "user");
            db_password = ini.get("DB", "password");
            
            
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE,ex.toString(),ex);
        }
    }
}
