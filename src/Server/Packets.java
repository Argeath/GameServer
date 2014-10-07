/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

/**
 *
 * @author amino_000
 */
public class Packets {
    public enum ERROR {
        WRONG_LOGIN_DATA(101),
        SERVER_DOWN(200);
        
        public final int id;
        
        ERROR(int i)
        {
            this.id = i;
        }
        
        String getDescription()
        {
            switch(this)
            {
                case WRONG_LOGIN_DATA:
                    return "Wrong username or password.";
                case SERVER_DOWN:
                    return "Server is down.";
            }
            return "";
        }
    }
    public static class UserInfoPacket {
        public int id;
        public String username;
        public boolean inQueue;
        public long timeInQueue;
        public long timeInThisQueue;
        
        UserInfoPacket(User u)
        {
            id = u.id;
            username = u.username;
            inQueue = u.inQueue;
            timeInQueue = u.timeInQueue;
            timeInThisQueue = u.timeInThisQueue;
        }
    }
    
    public static class LoginDataPacket {
        public String username;
        public String password;
    }
    
    public static class ErrorPacket {
        public int id;
        public String description;
    }
}
