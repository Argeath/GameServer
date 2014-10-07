/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Server.Packets.*;
import static Server.ServerListener.server;
import com.esotericsoftware.kryonet.Connection;

/**
 *
 * @author amino_000
 */
public class Protocol {
    public static void sendError(Connection c, ERROR err)
    {
        ErrorPacket packet = new ErrorPacket();
        packet.id = err.id;
        packet.description = err.getDescription();
        System.out.println("Sent error message.");
        server.sendToTCP(c.getID(), packet);
    }
    
    public static void sendUserInfo(Connection c, User u)
    {
        UserInfoPacket packet = new UserInfoPacket(u);
        System.out.println("Sent user info.");
        server.sendToTCP(c.getID(), packet);
    }
}
