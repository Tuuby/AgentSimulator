package de.Tuuby.AgentSimulator.graphics;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import de.Tuuby.AgentSimulator.resource.ImageResource;

// Class that handles all the maths and drawing of the de.Tuuby.AgentSimulator.graphics
public class Graphics {

    private static GL2 gl;

    // Color values
    private static float red = 1;
    private static float green = 1;
    private static float blue = 1;
    private static float alpha = 1;

    // Rotation in mathematically positive direction
    private static float rotation = 0;

    // Method to draw a line between to coordinates
    public static void drawLine(float x1, float y1, float x2, float y2, float thickness) {
        gl = EventListener.gl;

        gl.glColor4f(red, green, blue, alpha);

        gl.glLineWidth(thickness);

        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2f(x1, y1);
        gl.glVertex2f(x2, y2);
        gl.glEnd();

        gl.glLineWidth(1);
    }

    // Method to draw a single Colored Rectangle at the position (x|y) with the dimensions width * height
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

    // Method to draw a single Colored Circle at the position (x|y) with the radius r
    public static void fillCircle(float x, float y, float r) {
        gl = EventListener.gl;

        int numVertices = 100;

        gl.glTranslatef(x, y, 0);
        gl.glRotatef(rotation, 0, 0, 1);

        gl.glColor4f(red, green, blue, alpha);
        gl.glBegin(GL2.GL_POLYGON);
        {
            double angle = 0;
            double angleIncrement = 2 * Math.PI / numVertices;
            for (int i = 0; i < numVertices; i++) {
                angle = i * angleIncrement;
                double a = r * Math.cos(angle);
                double b = r * Math.sin(angle);
                gl.glVertex2d(a, b);
            }
        }
        gl.glEnd();
        gl.glFlush();

        gl.glRotatef(-rotation, 0, 0, 1);
        gl.glTranslatef(-x, -y, 0);
    }

    // Method to draw an Image on a rectangle
    public static void drawImage(ImageResource image, float x, float y, float width, float height) {
        gl = EventListener.gl;

        Texture texture = image.getTexture();

        if (x - width / 2 > Renderer.unitsWide ||
            x + width / 2 < 0 ||
            y - height / 2 > Renderer.unitsTall ||
            y + height / 2 < 0) {
            return;
        }

        if (texture != null) {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
        }

        gl.glTranslatef(x, y, 0);
        gl.glRotatef(rotation, 0, 0, 1);

        gl.glColor4f(1, 1, 1, 1);
        gl.glBegin(GL2.GL_QUADS);

        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(-width / 2, -height / 2);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(width / 2, -height / 2);
        gl.glTexCoord2f(1,1);
        gl.glVertex2f(width / 2,  height / 2);
        gl.glTexCoord2f(0,1);
        gl.glVertex2f(-width / 2, height / 2);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);

        gl.glRotatef(-rotation, 0, 0, 1);
        gl.glTranslatef(-x, -y, 0);
    }

    public static void drawText(String text, int x, int y, int width, int height) {
        TextRenderer textRnd = EventListener.renderer;
        textRnd.beginRendering(width, height);
        textRnd.setColor(red, green, blue, alpha);
        textRnd.draw(text, x, y);
        textRnd.endRendering();
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
