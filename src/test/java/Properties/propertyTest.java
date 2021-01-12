package Properties;

import de.Tuuby.AgentSimulator.resource.PropertiesLoader;

import java.util.Properties;

public class propertyTest {
    public static void main(String[] args) {
        System.out.println("Application entry");
        if (PropertiesLoader.loadConfig()) {
            Properties appConfig = PropertiesLoader.getAppConfig();
            int value = Integer.parseInt(appConfig.getProperty("testValue"));
            String appVersion = appConfig.getProperty("version");
            System.out.println("The property has been loaded correctly. Verison is: " + appVersion + " and the test Value is: " + value);
        }

        System.out.println("Application exit");
    }
}
