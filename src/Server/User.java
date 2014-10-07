/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import static Server.Main.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.logging.*;

/**
 *
 * @author amino_000
 */

public class User {
    public int id;
    public String username;
    public String password;
    public int logins;
    public int mmr;
    
    public boolean loggedIn;
    
    public Connection connection;
    
    
    public boolean inQueue;
    public long timeInQueue;
    public long timeInThisQueue;
    public Game.Teams team;
    
    
    
    
    public static final String[] inDatabase = {"username", "password", "logins", "mmr"};
    public boolean loaded;
    
    public User()
    {
        inQueue = false;
        timeInQueue = 0;
        timeInThisQueue = 0;
        team = null;
        connection = null;
        loggedIn = false;
    }
    
    public boolean find(int x)
    {
        try {
            Statement st = Main.conn.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM  users WHERE id = " + x);
            if (res.next()) {
                
                
                id = res.getInt("id");
                username = res.getString("username");
                password = res.getString("password");
                logins = res.getInt("logins");
                mmr = res.getInt("mmr");
                loaded = true;
                
                
                return true;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE,ex.toString(),ex);
        }
        return false;
    }
    
    public boolean find(String username)
    {
        try {
            Statement st = Main.conn.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM  users WHERE username = '" + username + "'");
            if (res.next()) {
                
                
                id = res.getInt("id");
                this.username = res.getString("username");
                this.password = res.getString("password");
                logins = res.getInt("logins");
                mmr = res.getInt("mmr");
                loaded = true;
                
                
                return true;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE,ex.toString(),ex);
        }
        return false;
    }
    
    public boolean find(String username, String password)
    {
        try {
            Statement st = Main.conn.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM  users WHERE username = '" + username + "' AND password = SHA1('" + password + "')");
            if (res.next()) {
                
                
                id = res.getInt("id");
                this.username = res.getString("username");
                this.password = res.getString("password");
                logins = res.getInt("logins");
                mmr = res.getInt("mmr");
                loaded = true;
                
                
                return true;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE,ex.toString(),ex);
        }
        return false;
    }
    
    public boolean login()
    {
        if(!loaded || username.isEmpty() || password.isEmpty())
            return false;
        
        Users.addUser(this);
        logins++;
        loggedIn = true;
        save();
        System.out.println(username+" has logged in.");
        return true;
    }
    
    public void logout()
    {
        loggedIn = false;
        Users.removeUser(this);
        System.out.println(username+" has logged out.");
    }
    
    public int save()
    {
        try {
            Statement st = Main.conn.createStatement();
            int result = 0;
            String query = "";
            if(loaded) //Then Update
            {
                String set = "";
                for(Field f : User.class.getDeclaredFields())
                {
                    if(!Helpers.contains(inDatabase, f.getName()))
                        continue;
                    
                    
                    if(!set.isEmpty())
                        set += ", ";
                    set += f.getName();
                    set += " = ";
                    if("java.lang.String".equals(f.getType().getName())) {
                        try {
                            set    += "'" + f.get(this) + "'";
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            set    += f.get(this);
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                query = "UPDATE users SET "+set+" WHERE id = "+id;
                try {
                    result = st.executeUpdate(query);
                } catch (SQLException ex) {
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {   //Then Create a new one
                String variables = "";
                String values = "";
                for(Field f : User.class.getDeclaredFields())
                {
                    if(!Helpers.contains(inDatabase, f.getName()))
                        continue;
                    
                    
                    if(!variables.isEmpty())
                        variables += ", ";
                    variables += f.getName();
                    
                    
                    if(!values.isEmpty())
                        values += ", ";
                    if("java.lang.String".equals(f.getType().getName())) {
                        try {
                            values    += "'" + f.get(this) + "'";
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            values    += f.get(this);
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                query = "INSERT INTO users("+variables+") VALUES("+values+")";
            }
            //System.out.println(query);
            try {
                result = st.executeUpdate(query);
            } catch(Exception ex)
            {
                LOGGER.log(Level.SEVERE,ex.toString(),ex);
            }
            return result;
        } catch(SQLException ex)
        {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public void addToQueue()
    {
        GameQueues.addUser(this);
    }
}