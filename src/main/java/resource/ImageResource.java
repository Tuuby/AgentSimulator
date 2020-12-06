package resource;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import graphics.Renderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageResource {

    private Texture texture;

    private BufferedImage image;

    public ImageResource(String path) {
        URL url = ImageResource.class.getResource(path);

        try {
            if (url == null)
                image = ImageIO.read(new File(path));
            else
                image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null) {
            image.flush();
        }
    }

    public Texture getTexture() {
        if (image == null) {
            return null;
        }

        if (texture == null) {
            texture = AWTTextureIO.newTexture(Renderer.getProfile(), image, true);
        }

        return texture;
    }
}
