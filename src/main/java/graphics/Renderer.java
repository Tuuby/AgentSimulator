package graphics;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

public class Renderer {

    private static GLProfile profile = null;
    private static GLWindow window = null;

    public static int screenWidth = 1280;
    public static int screenHeight = 720;

    public static float unitsWide = 20;
    public static float unitsTall = 0;

    public static float cameraX = 0;
    public static float cameraY = 0;

    public static void init() {
        GLProfile.initSingleton();
        profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        window = GLWindow.create(caps);
        window.requestFocus();
        window.setSize(screenWidth, screenHeight);
        window.addGLEventListener(new EventListener());
        window.setVisible(true);
    }

    public static void render() {
        if (window == null)
            return;

        window.display();
    }
}
