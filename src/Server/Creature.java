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
public class Creature {
    public int id;
    public int health;
    public int maxHealth;
    public String name;
    
    public Creature() {
        health = 0;
        maxHealth = 0;
        name = "";
    }
}