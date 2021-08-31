package de.Tuuby.AgentSimulator.graphics;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import de.Tuuby.AgentSimulator.guis.SwingManager;
import de.Tuuby.AgentSimulator.input.KeyInput;
import de.Tuuby.AgentSimulator.input.KeyInputSwing;
import de.Tuuby.AgentSimulator.input.MouseInput;
import de.Tuuby.AgentSimulator.resource.PropertiesLoader;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Class that Initializes the OpenGL context and window and calls the display method from window
public class Renderer {

    // Variables for the OpenGL profile and window
    private static GLProfile profile = null;
    private static GLCanvas glCanvas = null;
    private static JFrame mainFrame = null;

    // Variables for the window size in pixels
    public static int screenWidth = 1000;
    public static int screenHeight = 800;

    // Variables for the window size in Units
    public static float unitsWide = 1200;
    public static float unitsTall = 0;

    // Gets called by the main method to initialize the window and the OpenGL profile
    public static void init() {

        GLProfile.initSingleton();
        profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        glCanvas = new GLCanvas(caps);
        glCanvas.addGLEventListener(new EventListener());

        mainFrame = new JFrame("Agenten Simulation");
        mainFrame.setResizable(false);
        mainFrame.requestFocus();
        if (!PropertiesLoader.getAppConfig().isEmpty()) {
            screenWidth = Integer.parseInt(PropertiesLoader.getAppConfig().getProperty("windowWidth"));
            screenHeight = Integer.parseInt(PropertiesLoader.getAppConfig().getProperty("windowHeight"));
            mainFrame.setTitle(PropertiesLoader.getAppConfig().getProperty("name"));
        }

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainFrame.dispose();
                System.exit(0);
            }
        });

        mainFrame.setSize(screenWidth, screenHeight);
        // TODO: figure out if this is needed still
        //glCanvas.addMouseListener();
        mainFrame.addKeyListener(new KeyInputSwing());
        SwingManager.build(mainFrame, glCanvas, PropertiesLoader.getAppConfig());
        mainFrame.setVisible(true);
    }

    // Gets called by the GameLoop to draw the next frame
    public static void render() {
        if (glCanvas == null)
            return;

        glCanvas.display();
    }

    public static int getWindowHeight() {
        return mainFrame.getHeight();
    }

    public static int getWindowWidth() {
        return mainFrame.getWidth();
    }

    public static GLProfile getProfile() {
        return profile;
    }

    public static void stop() {
        mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
    }
}
