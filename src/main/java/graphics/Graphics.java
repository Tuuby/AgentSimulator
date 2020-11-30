package graphics;

import com.jogamp.opengl.GL2;

// Class that handles all the maths and drawing of the graphics
public class Graphics {

    private static GL2 gl;

    // Color values
    private static float red = 1;
    private static float green = 1;
    private static float blue = 1;
    private static float alpha = 1;

    // Rotation in mathematically positive direction
    private static float rotation = 0;

    public static void fillRect(float x, float y, float width, float height) {
        gl = EventListener.gl;

        gl.glTranslatef(x, y, 0);
        gl.glRotatef(rotation, 0, 0, 1);

        gl.glColor4f(red, green, blue, alpha);

        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-width / 2, -height / 2);
        gl.glVertex2f(width / 2, -height / 2);
        gl.glVertex2f(width / 2, height / 2);
        gl.glVertex2f(-width / 2, height / 2);
        gl.glEnd();
        gl.glFlush();

        gl.glRotatef(-rotation, 0, 0, 1);
        gl.glTranslatef(-x, -y, 0);
    }

    // Set the color Variables
    public static void setColor(float r, float g, float b, float a) {
        red = Math.max(0, Math.min(1, r));
        green = Math.max(0, Math.min(1, g));
        blue = Math.max(0, Math.min(1, b));
        alpha = Math.max(0, Math.min(1, a));
    }

    public static void setRotation(float rotation) {
        Graphics.rotation = rotation;
    }
}
