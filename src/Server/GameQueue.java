/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author amino_000
 */
public class GameQueue {
    private long start;
    private long lastActive;
    public int maxUsers;
    public List<User> users = new CopyOnWriteArrayList<User>();
    public int averageMMR;
    public boolean active;
    public long lastCheck;
    
    public GameQueue()
    {
        averageMMR = 0;
        maxUsers = 10;
        lastCheck = 0;
        lastActive = System.currentTimeMillis();
        start = System.currentTimeMillis();
        active = true;
        
        System.out.println("GameQueue created.");
    }
    
    public void addUser(User usr)
    {
        if(!active)
            return;
        
        long now = System.currentTimeMillis();
        if(users.size() < maxUsers)
        {
            usr.timeInThisQueue = now;
            users.add(usr);
            System.out.println("Adding user - " + usr.username + " to a GameQueue.");
        }
        
        if(users.size() == 1)
        {
            averageMMR = users.get(0).mmr;
        }
        lastActive = now;
        check();
    }
    
    public boolean checkUserMMR(User usr)
    {
        if(!active)
            return false;
        
        long now = System.currentTimeMillis();
        int mmrDiff = Math.abs(usr.mmr - averageMMR);
        long timeDiff = (long)(Math.ceil((float)(now - usr.timeInQueue)/1000));
        int acceptableDiff = (int) (15 * timeDiff);
        System.out.println("User - " + usr.username + " - MMR - "+usr.mmr+" - mmrDiff - "+mmrDiff+" - acceptableDiff - "+acceptableDiff+" - timeDiff - "+timeDiff);
        if(mmrDiff <= acceptableDiff)
            return true;
        return false;
    }
    
    public void check()
    {
        if(!active)
            return;
        
        long now = System.currentTimeMillis();
        if(users.size() == maxUsers)
        {
            //TODO: If the diffrence is too high - kick sb
            
            //TODO: Group into 2 teams
            User[] _users = new User[users.size()];
            users.toArray(_users);
            Arrays.sort(_users, new MMRComparator());
            Game.Teams team = Game.Teams.BLUE;
            
            for(int i = 0; i<(maxUsers/2);i++)
            {
                int remaining = (maxUsers-i-1)-i+1;
                if(remaining > 3)
                {
                    User first = _users[i];
                    User last = _users[maxUsers-i-1];
                    first.team = team;
                    last.team = team;
                } else {
                    User first = _users[i];
                    User last = _users[maxUsers-i-1];
                    first.team = team;
                    team = team.getOther();
                    last.team = team;
                }
                team = team.getOther();
                
            }
            
            //Start game
            Game g = new Game(users);
            Games.addGame(g);
            
            GameQueues.queues.remove(this);
            active = false;
            return;
        }
        long lastActiveDiff = now - lastActive;
        long startDiff = now - start;
        if( (lastActiveDiff > 60000) || (startDiff > 300000) ) //1 min lastActive or 5 mins from start
        { 
            //Closing queue and sending users to other queue
            
            active = false;
            for(User u: users)
            {
                GameQueues.waitingUsers.add(u);
                users.remove(u);
            }
            System.out.println("Closing GameQueue due to an inactive queue.");
            GameQueues.queues.remove(this);
            return;
        }
        
        lastCheck = now;
        
    }
    
    
}


class MMRComparator implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        return o2.mmr - o1.mmr;
    }
}