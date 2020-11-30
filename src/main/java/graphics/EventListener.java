package graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import engine.GameLoop;

// Implementation of the GLEventListener interface
// This acts as an EventListener for the events that get called by a OpenGL window
public class EventListener implements GLEventListener {

    // Public variable for the OpenGL interface that holds all the methods
    public static GL2 gl = null;

    // Gets called when the EventListener gets added to an OpenGL window
    // And initializes the OpenGL context with various settings
    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();

        gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
    }

    // Gets called when the window is closed
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.out.println("Window is closed!");
    }

    // Gets called by the Renderer to draw the next frame
    public void display(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
    }

    // Gets called everytime the shape of the windows changes
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }
}
