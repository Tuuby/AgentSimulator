package de.Tuuby.AgentSimulator.resource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesManager {
    public static String CONFIG_PATH = "/config.properties";
    private static Properties appConfig;

    public static boolean loadConfig() {
        URL url = PropertiesManager.class.getResource(CONFIG_PATH);
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

    public static void writeProperties(Properties properties) {
        URL url = PropertiesManager.class.getResource(CONFIG_PATH);
        try {
            File configFile = Paths.get(url.toURI()).toFile();
            FileWriter writer = new FileWriter(configFile);
            properties.store(writer, "Saving Properties");
            writer.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
