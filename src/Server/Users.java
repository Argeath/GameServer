/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import com.esotericsoftware.kryonet.Connection;
import java.util.Vector;

/**
 *
 * @author amino_000
 */
public class Users {
    public static Vector<User> pool = new Vector<User>();
    
    public static void addUser(User usr)
    {
        pool.add(usr);
    }
    
    public static void removeUser(User usr)
    {
        pool.remove(usr);
    }
    
    public static User findUser(int id)
    {
        for(User cr: pool)
        {
            if(cr.id == id)
                return cr;
        }
        User u = new User();
        u.find(id);
        if(u.loaded)
        {
            addUser(u);
            return u;
        }
        return null;
    }
    
    public static User findUser(String name)
    {
        for(User cr: pool)
        {
            if(cr.username == null ? name == null : cr.username.equals(name))
                return cr;
        }
        User u = new User();
        u.find(name);
        if(u.loaded)
        {
            addUser(u);
            return u;
        }
        return null;
    }
    public static User findUser(Connection c)
    {
        for(User cr: pool)
        {
            if(cr.connection == c)
                return cr;
        }
        return null;
    }
    
    
}
