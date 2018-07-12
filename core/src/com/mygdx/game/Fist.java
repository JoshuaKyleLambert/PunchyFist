package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Fist {
    private static final float MAX_X_SPEED = 64;
    private static float MIN_X_POSITION = 0;
    private static float MIN_Y_POSITION = 0;
    public static final int WIDTH = 24;
    public static final int HEIGHT = 24;
    public static final float OFFSET = 32;

    private final Rectangle collisionRectangle = new Rectangle(0, 0, WIDTH, HEIGHT);

    private float x = 0;
    private float y = 0;
    private float xSpeed = 0;
    private boolean blockPunch = false;

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        MIN_X_POSITION = x;
        updateCollisionRectangle();
    }

    public void setPosition(Punchy punchy){
        MIN_X_POSITION = punchy.getX() + OFFSET;
        this.y = punchy.getY() + OFFSET;
    }

    public boolean isPunching(){
        return blockPunch;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void update(){
        Input input = Gdx.input;
        if(input.isKeyPressed(Input.Keys.SPACE) ){
            xSpeed = MAX_X_SPEED;
        } else {
            xSpeed = -MAX_X_SPEED;
        }
       this.x += xSpeed;

        if ( x < MIN_X_POSITION)  x = MIN_X_POSITION;
        //if ( y < MIN_Y_POSITION) y = MIN_Y_POSITION;
        updateCollisionRectangle();

    }


    public void punched(){
       blockPunch = false;
    }

    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(
                collisionRectangle.x,
                collisionRectangle.y,
                collisionRectangle.width,
                collisionRectangle.height
        );
    }

    private void updateCollisionRectangle(){
        collisionRectangle.setPosition(x,y);
    }
}
