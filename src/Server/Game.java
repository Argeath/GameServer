/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author amino_000
 */
public class Game {
    public enum Teams {
        BLUE, PURPLE;
        public Teams getOther()
        {
            if(this == BLUE)
                return PURPLE;
            return BLUE;
        }
    }
    public List<User> users = new CopyOnWriteArrayList<User>();
    private long start;
    
    public Game(List<User> u)
    {
        users = u;
        start = System.currentTimeMillis();
        System.out.println("Game has been created.");
        System.out.println("Team 1: ");
        int sumaBlue = 0;
        float sredniaBlue = 0;
        int sumaPurple = 0;
        float sredniaPurple = 0;
        for(User user: users)
        {
            if(user.team == Teams.BLUE)
            {
                System.out.println(user.username+": "+user.mmr);
                sumaBlue += user.mmr;
            }
        }
        sredniaBlue = sumaBlue / (users.size()/2);
        System.out.println("Srednia: "+sredniaBlue);
        System.out.println("Team 2: ");
        for(User user: users)
        {
            if(user.team == Teams.PURPLE)
            {
                System.out.println(user.username+": "+user.mmr);
                sumaPurple += user.mmr;
            }
        }
        sredniaPurple = sumaPurple / (users.size()/2);
        System.out.println("Srednia: "+sredniaPurple);
    }
}
