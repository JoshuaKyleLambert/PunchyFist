package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Punchy {
    private static final float MAX_X_SPEED = 6;
    private static final float MAX_Y_SPEED = 6;
    public static final int WIDTH = 32;
    public static final int HEIGHT = 64;

    private final Rectangle collisionRectangle = new Rectangle(0, 0, WIDTH, HEIGHT);

    private float x = 0;
    private float y = 0;
    private float xSpeed = 0;
    private float ySpeed = 0;

    private boolean blockJump = false;
    private float jumpYDistance = 0;

    private static final float MAX_JUMP_DISTANCE = 3 * HEIGHT;


    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCollisionRectangle();
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void update(){
        Input input = Gdx.input;
        if(input.isKeyPressed(Input.Keys.RIGHT)){
            xSpeed = MAX_X_SPEED;
        } else if (input.isKeyPressed(Input.Keys.LEFT)){
            xSpeed = -MAX_X_SPEED;
        } else {
            xSpeed = 0;
        }

        // easy change here to enable a flying.  just take out !blockJump
        if(input.isKeyPressed(Input.Keys.UP) ){
            ySpeed = MAX_Y_SPEED;
            jumpYDistance += ySpeed;
            blockJump = jumpYDistance > MAX_JUMP_DISTANCE;
        } else {
            ySpeed = -MAX_Y_SPEED*2;
            blockJump = jumpYDistance > 0;
        }


        x += xSpeed;
        y += ySpeed;
        updateCollisionRectangle();

    }

    public void landed(){
        blockJump = false;
        jumpYDistance = 0;
        ySpeed = 0;
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
