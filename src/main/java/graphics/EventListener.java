package graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import engine.GameLoop;
import sun.java2d.loops.DrawGlyphList;

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

        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_BLEND);
        Graphics.setColor(1, 1, 0, 1);
        //Graphics.setRotation(45);
    }

    // Gets called when the window is closed
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.out.println("Window is closed!");
    }

    // Gets called by the Renderer to draw the next frame
    public void display(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        Graphics.fillCircle(Renderer.unitsWide / 2, Renderer.unitsTall / 2, 1);
    }

    // Gets called everytime the shape of the windows changes
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        gl = glAutoDrawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // Calculate the height of the Window with the new AspectRatio
        Renderer.unitsTall = Renderer.getWindowHeight() / (Renderer.getWindowWidth() / Renderer.unitsWide);

        // Apply an offset to the OpenGL context to match with world Coordinates
        gl.glOrtho(0, Renderer.unitsWide, Renderer.unitsTall, 0, -1, 1);

        // Old offset
        //gl.glOrtho(-Renderer.unitsWide / 2, Renderer.unitsWide / 2, -Renderer.unitsTall / 2, Renderer.unitsTall / 2, -1, 1);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
}
