package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Heart {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;

    private final Texture texture;
    private  float x;
    private  float y;
    private final Rectangle collision;

    public Heart (Texture texture, float x, float y){
        this. texture = texture;
        this.x = x;
        this.y = y;
        this.collision = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public Rectangle getCollisionRectangle(){ return collision; }

    public void draw(Batch batch){
        batch.draw(texture, x, y);
    }
}
