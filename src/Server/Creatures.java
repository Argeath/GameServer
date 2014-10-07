/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import java.util.*;

/**
 *
 * @author amino_000
 */
public class Creatures {
    public static Vector<Creature> pool = new Vector<Creature>();
    
    public static Creature createCreature(String name)
    {
        Creature newcreature = new Creature();
        newcreature.name = name;
        pool.add(newcreature);
        newcreature.id = pool.indexOf(newcreature);
        return newcreature;
    }
    
    public static void addCreature(Creature cr)
    {
        if(cr.id > 0)
            pool.add(cr.id, cr);
        else
        {
            pool.add(cr);
            cr.id = pool.indexOf(cr);
        }
    }
    
    public static Creature findCreature(int id)
    {
        for(Creature cr: pool)
        {
            if(cr.id == id)
                return cr;
        }
        return null;
    }
    
    public static Creature findCreature(String name)
    {
        for(Creature cr: pool)
        {
            if(cr.name == null ? name == null : cr.name.equals(name))
                return cr;
        }
        return null;
    }
}
