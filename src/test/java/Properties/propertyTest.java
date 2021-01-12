package Properties;

import de.Tuuby.AgentSimulator.resource.PropertiesLoader;

import java.util.Properties;

public class propertyTest {
    public static void main(String[] args) {
        System.out.println("Application entry");
        if (PropertiesLoader.loadConfig()) {
            int value = Integer.parseInt(PropertiesLoader.getAppConfig().getProperty("testValue"));
            String appVersion = PropertiesLoader.getAppConfig().getProperty("version");
            System.out.println("The property has been loaded correctly. Verison is: " + appVersion + " and the test Value is: " + value);
        }
    }
}
