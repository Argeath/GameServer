package Server;

import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author amino_000
 */
public class Games {
    public static Vector<Game> games = new Vector<Game>();
    
    public static void addGame(Game g)
    {
        games.addElement(g);
    }
    
}
