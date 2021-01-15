package de.Tuuby.AgentSimulator.graphics;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import de.Tuuby.AgentSimulator.input.KeyInput;
import de.Tuuby.AgentSimulator.input.MouseInput;
import de.Tuuby.AgentSimulator.resource.PropertiesLoader;

// Class that Initializes the OpenGL context and window and calls the display method from window
public class Renderer {

    // Variables for the OpenGL profile and window
    private static GLProfile profile = null;
    private static GLWindow window = null;

    // Variables for the window size in pixels
    public static int screenWidth = 1200;
    public static int screenHeight = 1200;

    // Variables for the window size in Units
    public static float unitsWide = 800;
    public static float unitsTall = 0;

    // Variables for a moving camera; probably not needed here
    //public static float cameraX = 0;
    //public static float cameraY = 0;

    // Gets called by the main method to initialize the window and the OpenGL profile
    public static void init() {

        GLProfile.initSingleton();
        profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        window = GLWindow.create(caps);
        window.setResizable(true);
        window.requestFocus();
        if (!PropertiesLoader.getAppConfig().isEmpty()) {
            screenWidth = Integer.parseInt(PropertiesLoader.getAppConfig().getProperty("windowWidth"));
            screenHeight = Integer.parseInt(PropertiesLoader.getAppConfig().getProperty("windowHeight"));
            window.setTitle(PropertiesLoader.getAppConfig().getProperty("name"));
        }
        window.setSize(screenWidth, screenHeight);
        window.addGLEventListener(new EventListener());
        window.addMouseListener(new MouseInput());
        window.addKeyListener(new KeyInput());
        window.setVisible(true);
    }

    // Gets called by the GameLoop to draw the next frame
    public static void render() {
        if (window == null)
            return;

        window.display();
    }

    public static int getWindowHeight() {
        return window.getHeight();
    }

    public static int getWindowWidth() {
        return window.getWidth();
    }

    public static GLProfile getProfile() {
        return profile;
    }

    public static void stop() {
        window.destroy();
    }
}
