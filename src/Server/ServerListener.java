/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Server.Packets.*;
import Server.Packets.ERROR;
import Server.Packets.ErrorPacket;
import Server.Packets.LoginDataPacket;
import Server.Packets.UserInfoPacket;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author amino_000
 */
public class ServerListener extends Listener {
    static Server server;
    static final int port = 27960;
    
    public static void start()
    {
        try {
            server = new Server();
            //Registers
            server.getKryo().register(ErrorPacket.class);
            server.getKryo().register(LoginDataPacket.class);
            server.getKryo().register(UserInfoPacket.class);
            
            
            
            //Binding and starting
            server.bind(port, port);
            server.start();
            server.addListener(new ServerListener());
            
            System.out.println("Server has started.");
            
        } catch (IOException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void connected(Connection c){
        System.out.println("Connection received.");
    }
    
    @Override
    public void received(Connection c, Object o){
        if(o instanceof LoginDataPacket) {
            LoginDataPacket packet = (LoginDataPacket) o;
            if(packet.username.isEmpty() || packet.password.isEmpty())
            {
                Protocol.sendError(c, ERROR.WRONG_LOGIN_DATA);
                return;
            }
            
            User u = new User();
            System.out.println("Received: "+packet.username+" - "+packet.password);
            u.find(packet.username, packet.password);
            if(!u.login())
            {
                System.out.println("Sending Error.");
                Protocol.sendError(c, ERROR.WRONG_LOGIN_DATA);
                return;
            }
            System.out.println("Sending UserInfo.");
            Protocol.sendUserInfo(c, u);
        }
    }

    @Override
    public void disconnected(Connection c){
        User u = Users.findUser(c);
        if(u != null && u.loaded)
            u.logout();
        else
            System.out.println("Connection dropped.");
    }
}
