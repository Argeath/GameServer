/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import java.io.IOException;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());
  
    public static Connection conn;
    
    public static boolean running;
    
    static {
        try {
            LOGGER.addHandler(new FileHandler("errors.log",true));
        }
        catch(IOException ex) {
            LOGGER.log(Level.WARNING,ex.toString(),ex);
        }
    }
    
    
    public Main() {
        running = true;
    }
    
    public static void main(String[] args) {
        Config.load();
        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(Config.db_host+Config.db_db, Config.db_user, Config.db_password);
            
            GameQueues.create();
            ServerListener.start();
            
        } catch(SQLException ex)
        {
            LOGGER.log(Level.SEVERE, "Couldn't connect to database (" + driver + ").");
        } catch (Exception ex) 
        {
            LOGGER.log(Level.SEVERE,ex.toString(),ex);
        } finally {
            try{
               if(conn!=null)
                  conn.close();
            } catch(SQLException se){
               se.printStackTrace();
            }
        }
    }
}



/*
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Main {
  public static final int DISPLAY_WIDTH  = 640;
  public static final int DISPLAY_HEIGHT = 480;
  
  public static final Logger LOGGER = Logger.getLogger(Main.class.getName());
  
  public static Connection conn;

  private int squareSize;
  private int squareX;
  private int squareY;
  private int squareZ;
  
  private long lastFrame;
  private long lastFPS;
  private int fps;

  static {
    try {
      LOGGER.addHandler(new FileHandler("errors.log",true));
    }
    catch(IOException ex) {
      LOGGER.log(Level.WARNING,ex.toString(),ex);
    }
  }

  public static void main(String[] args) {
    Main main = null;
    Config.load();
    String driver = "com.mysql.jdbc.Driver";
    try {
        Class.forName(driver).newInstance();
        conn = DriverManager.getConnection(Config.db_host+Config.db_db, Config.db_user, Config.db_password);
        
        
        
        User user = new User();
        if(user.find(Config.user_username, Config.user_password))
        {
            System.out.println("Logged in.");
        } else {
            System.out.println("Couldn't log in.");
        }
        
        try {
          System.out.println("Keys:");
          System.out.println("down  - Shrink");
          System.out.println("up    - Grow");
          System.out.println("left  - Rotate left");
          System.out.println("right - Rotate right");
          System.out.println("esc   - Exit");
          main = new Main();
          main.create();
          main.run();
        }
        catch(Exception ex) {
          LOGGER.log(Level.SEVERE,ex.toString(),ex);
        }
        finally {
          if(main != null) {
            main.destroy();
          }
        }
        conn.close();
    } catch (Exception ex) {
        LOGGER.log(Level.SEVERE,ex.toString(),ex);
    }
  }

  public Main() {
    squareSize = 100;
    squareX = 0;
    squareY = 0;
    squareZ = 0;
  }

  public void create() throws LWJGLException {
    //Display
    Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,DISPLAY_HEIGHT));
    Display.setFullscreen(false);
    Display.setTitle("Hello LWJGL World!");
    Display.create();

    //Keyboard
    Keyboard.create();

    //Mouse
    Mouse.setGrabbed(false);
    Mouse.create();

    //OpenGL
    initGL();
    resizeGL();
    System.out.println("OpenGL version: " + glGetString(GL_VERSION));
    getDelta();
    lastFPS = getTime();
  }

  public void destroy() {
    //Methods already check if created before destroying.
    Mouse.destroy();
    Keyboard.destroy();
    Display.destroy();
  }

  public void initGL() {
    //2D Initialization
    glClearColor(0.0f,0.0f,0.0f,0.0f);
    glDisable(GL_DEPTH_TEST);
    glDisable(GL_LIGHTING);
  }

  public void processKeyboard() {
    //Square's Size
    if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
      --squareSize;
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
      ++squareSize;
    }

    //Square's Z
    if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
      ++squareZ;
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
      --squareZ;
    }
  }

  public void processMouse() {
    squareX = Mouse.getX();
    squareY = Mouse.getY();
  }

  public void render() {
    glClear(GL_COLOR_BUFFER_BIT);
    glLoadIdentity();

    //Draw a basic square
    glTranslatef(squareX,squareY,0.0f);
    glRotatef(squareZ,0.0f,0.0f,1.0f);
    glTranslatef(-(squareSize >> 1),-(squareSize >> 1),0.0f);
    glColor3f(0.0f,0.5f,0.5f);
    glBegin(GL_QUADS);
      glTexCoord2f(0.0f,0.0f); glVertex2f(0.0f,0.0f);
      glTexCoord2f(1.0f,0.0f); glVertex2f(squareSize,0.0f);
      glTexCoord2f(1.0f,1.0f); glVertex2f(squareSize,squareSize);
      glTexCoord2f(0.0f,1.0f); glVertex2f(0.0f,squareSize);
    glEnd();
  }

  public void resizeGL() {
    //2D Scene
    glViewport(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(0.0f,DISPLAY_WIDTH,0.0f,DISPLAY_HEIGHT);
    glPushMatrix();

    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glPushMatrix();
  }

  public void run() {
    while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
      if(Display.isVisible()) {
        processKeyboard();
        processMouse();
        updateFPS();
        update();
        render();
      }
      else {
        if(Display.isDirty()) {
          render();
        }
        try {
          Thread.sleep(100);
        }
        catch(InterruptedException ex) {
        }
      }
      Display.update();
      //Display.sync(60);
    }
  }

  public void update() {
    if(squareSize < 5) {
      squareSize = 5;
    }
    else if(squareSize >= DISPLAY_HEIGHT) {
      squareSize = DISPLAY_HEIGHT;
    }
  }
  
  public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }


    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    public void updateFPS() {
            if (getTime() - lastFPS > 1000) {
                    Display.setTitle("FPS: " + fps);
                    fps = 0;
                    lastFPS += 1000;
            }
            fps++;
    }
}

*/