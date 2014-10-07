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
public class LuaScript {
    
    public enum TYPE {
        FILE(1),
        FUNCTION(2),
        NONE(999);
        
        public final int id;
        
        TYPE(int i)
        {
            this.id = i;
        }
        
        public static TYPE find(String s)
        {
            if("file".equals(s.toLowerCase()))
                return FILE;
            else if("function".equals(s.toLowerCase()))
                return FUNCTION;
            return NONE;
        }
    }
    
    public TYPE type;
    public String name;
    public boolean loaded;
    
    LuaScript()
    {
        type = TYPE.NONE;
        name = "";
        loaded = false;
    }
    
    LuaScript(TYPE t, String n)
    {
        type = t;
        name = n;
        loaded = false;
    }
    
    public void load()
    {
        
    }
}
