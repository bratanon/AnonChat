package se.stjerneman.anonchat.client.ui.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class UISettings {
    private static UISettings instace = null;

    private static File settingsFile;

    public static Properties settings;

    private UISettings () {
        settingsFile = new File("settings.properties");
        loadSettings();
    }

    public static UISettings getInstance () {
        if (UISettings.instace == null) {
            UISettings.instace = new UISettings();
        }

        return UISettings.instace;
    }

    public Properties getSettings () {
        return UISettings.settings;
    }

    public static boolean saveSettings () {

        try {
            UISettings.settings.store(new FileWriter(settingsFile),
                    "AnonChat UI settings properties.");
            return true;
        }
        catch (IOException e) {
            System.err.println("Couldn't save the settings.properties file.");
            return false;
        }

    }

    private void loadSettings () {
        UISettings.settings = new Properties(defaultSettings());

        if (settingsFile.exists()) {
            try {
                UISettings.settings.load(new FileReader(settingsFile));
            }
            catch (FileNotFoundException e) {}
            catch (IOException e) {}
        }
    }

    private Properties defaultSettings () {
        Properties defaultsettings = new Properties();
        defaultsettings.setProperty("hostIP", "127.0.0.1");
        defaultsettings.setProperty("hostPort", "6789");
        defaultsettings.setProperty("username", "");

        return defaultsettings;
    }
}
