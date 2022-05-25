package org.jmatrix.graphical;

import java.io.*;
import java.util.Enumeration;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.*;

import org.jmatrix.graphical.Window;

public class Window extends JFrame {
    private static final long serialVersionUID = 1L;
    
    public static final int DEFAULT_WIDTH = 600;
    public static final int DEFAULT_HEIGHT = 400;
    public static final String DEFAULT_TITLE = "";
    public static final ImageIcon DEFAULT_ICON = null; 
    public static final Font FALLBACK_FONT = new Font ("Serif", Font.TRUETYPE_FONT, 12);
    public static final Font DEFAULT_FONT = addCustomFont (new File ("src/org/jmatrix/graphical/Ubuntu Nerd Font Complete.ttf"));
    public static final Color DEFAULT_FOREGROUND = new Color (129, 161, 193);
    public static final Color DEFAULT_BACKGROUND = new Color (236, 239, 244);
    
    protected int width;
    protected int height;
    protected String title;
    protected ImageIcon icon;
    protected Font font;
    protected Color foreground;
    protected Color background;
    
    public Window () {
        this (DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public Window (int width, int height) {
        this (width, height, DEFAULT_TITLE);
    }
    
    public Window (int width, int height, String title) {
        this (width, height, title, DEFAULT_ICON);
    }
    
    public Window (int width, int height, String title, ImageIcon icon) {
        this (width, height, title, icon, DEFAULT_FONT);
    }
    
    public Window (int width, int height, String title, ImageIcon icon, Font font) {
        this (width, height, title, icon, font, DEFAULT_FOREGROUND, DEFAULT_BACKGROUND);
    }
    
    public Window (int width, int height, String title, ImageIcon icon, Font font, Color foreground, Color background) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.icon = icon;
        this.font = font;
        this.foreground = foreground;
        this.background = background;
        
        this.setTitle (this.title);
        if (this.icon != null) {
            this.setIconImage (this.icon.getImage ());
        }
        this.setSize (this.width, this.height);
        this.setDefaultCloseOperation (DISPOSE_ON_CLOSE);
        
        this.applyTheme ();
        this.setResizable (false);
    }
    
    public static Font addCustomFont (File font) {
        try {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment ();
            
            Font customFont = Font.createFont (Font.TRUETYPE_FONT, font).deriveFont (14f);
            graphicsEnvironment.registerFont (customFont);
            
            return customFont;
        }
        
        catch (IOException | FontFormatException e) {
            return Window.FALLBACK_FONT;
        }
    }
    
    public static void setUIFont (FontUIResource font) {
        for (Enumeration <Object> keys = UIManager.getDefaults ().keys (); keys.hasMoreElements ();) {
            Object key = keys.nextElement ();
            Object value = UIManager.get (key);

            if (value instanceof FontUIResource) {
                UIManager.put (key, font);
            }
        }
    }
    
    public void applyTheme () {
        this.setBackground (this.background);
        
        for (Component component : this.getComponents ()) {
            if (component instanceof JComponent) {
                ((JComponent) component).setForeground (this.foreground);
                ((JComponent) component).setFont (this.font);
            }
        }
    }
}
