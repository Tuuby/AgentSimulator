package world;

import graphics.Animation;

public interface GameObject {

    float x = 0;
    float y = 0;

    float width = 1;
    float height = 1;

    float rotation = 0;

    Animation[] animations = null;
    int currentAnimation = 0;

    void update();

    void render();
}
