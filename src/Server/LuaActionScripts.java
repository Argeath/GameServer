/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Server.LuaScript.TYPE;
import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.xml.parsers.*;
import org.keplerproject.luajava.*;
import org.w3c.dom.*;

/**
 *
 * @author amino_000
 */
public class LuaActionScripts {
    public static LuaState L;
    
    
    public static List<LuaActionScript> scripts = new CopyOnWriteArrayList<LuaActionScript>();
    
    public static void create()
    {
        L = LuaStateFactory.newLuaState();
        L.openLibs();
    }
    
    
    public static void loadScriptsFile(String name)
    {
        try {
            File f = new File(name);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(f);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("action");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                LuaActionScript scr = new LuaActionScript();
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(eElement.hasAttribute("itemid"))
                        scr.itemid = Integer.parseInt(eElement.getAttribute("itemid"));
                    if(eElement.hasAttribute("actionid"))
                        scr.actionid = Integer.parseInt(eElement.getAttribute("actionid"));
                    if(eElement.hasAttribute("uniqueid"))
                        scr.uniqueid = Integer.parseInt(eElement.getAttribute("uniqueid"));
                    scr.type = TYPE.find(eElement.getAttribute("type"));
                    scr.name = eElement.getAttribute("name");
                }
            }
            
            
        } catch (Exception e) {
          e.printStackTrace();
        }
    }
}
