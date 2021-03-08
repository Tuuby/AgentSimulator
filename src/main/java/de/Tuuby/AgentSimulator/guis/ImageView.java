package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.graphics.Animation;
import de.Tuuby.AgentSimulator.resource.ImageResource;

public class ImageView extends GuiElement{

    public ImageView(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void setImage(ImageResource image) {
        texture = new Animation();
        texture.frames = new ImageResource[1];
        texture.frames[0] = image;
    }

    public void update() {
        // nothing to update
    }
}
