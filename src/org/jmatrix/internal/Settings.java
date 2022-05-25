package org.jmatrix.internal;

import java.util.Map;
import java.util.Properties;

public class Settings {
    public static final Properties DEFAULT_SETTINGS = new DEFAULT ().settings;;
    public static Properties settings = new Properties ();
    
    record DEFAULT (Properties settings) {
        public DEFAULT () {
            this (null);
        }
        
        public DEFAULT (Properties settings) {
            this.settings = new Properties ();

            this.settings.setProperty ("Style", "Brackets");
        }
    }
    
    public Settings () {
        for (Map.Entry <Object, Object> setting : Settings.DEFAULT_SETTINGS.entrySet ()) {
            Settings.settings.setProperty (setting.getKey ().toString (), setting.getValue ().toString ());
        }
    }
}
