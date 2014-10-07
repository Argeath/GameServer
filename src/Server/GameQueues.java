/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amino_000
 */
public class GameQueues implements Runnable {
    public static List<GameQueue> queues = new CopyOnWriteArrayList<GameQueue>();
    public static List<User> waitingUsers = new CopyOnWriteArrayList<User>();
    
    public static Thread T;
    
    public static void addUser(User u)
    {
        u.inQueue = true;
        u.timeInQueue = System.currentTimeMillis();
        waitingUsers.add(u);
    }
    
    public static void create()
    {
        Runnable runner = new GameQueues();
        T = new Thread(runner);
        T.start();
    }
    
    @Override
    public void run()
    {
        System.out.println("GameQueues have started.");
        while(true) {
            checkQueues();
            if(waitingUsers.isEmpty())
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameQueues.class.getName()).log(Level.SEVERE, null, ex);
                }
                continue;
            }
            for(User u: waitingUsers)
            {
                if(!u.loggedIn)
                {
                    waitingUsers.remove(u);
                    continue;
                }
                    
                if(queues.isEmpty())
                {
                    GameQueue q = new GameQueue();
                    q.addUser(u);
                    queues.add(q);
                    waitingUsers.remove(u);
                    continue;
                }
                for(GameQueue q: queues)
                {
                    if(q.checkUserMMR(u))
                    {
                        q.addUser(u);
                        waitingUsers.remove(u);
                        break;
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameQueues.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void checkQueues()
    {
        long now = System.currentTimeMillis();
        for(GameQueue q: queues)
        {
            long lastCheckDiff = now - q.lastCheck;
            if(lastCheckDiff > 5000)
            {
                q.check();
            }
        }
    }
    
    
}
