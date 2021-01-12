package de.Tuuby.AgentSimulator.resource;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class PropertiesLoader {
    public static String CONFIG_PATH = "/config.properties";
    private static Properties appConfig;

    public static boolean loadConfig() {
        URL url = PropertiesLoader.class.getResource(CONFIG_PATH);
        appConfig = new Properties();
        try {
            appConfig.load(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return !appConfig.isEmpty();
    }

    public static Properties getAppConfig() {
        return appConfig;
    }
}
