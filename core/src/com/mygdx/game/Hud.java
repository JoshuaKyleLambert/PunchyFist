package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Hud {
    Camera camera;
    private float x;
    private float y;
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;
    float levelWidth;

    public int score = 0;

    public Hud(Camera camera, PunchyFist punchyFist, float levelWidth){
        this.camera = camera;
        this.levelWidth = levelWidth;
        bitmapFont = punchyFist.getAssetManager().get("score.fnt");
        glyphLayout = new GlyphLayout();
    }

    public void draw(Batch batch){
        drawScore(batch);
    }

    private void drawScore(Batch batch){
        String scoreAsString = Integer.toString(score);
        glyphLayout.setText(bitmapFont, scoreAsString);
        bitmapFont.draw(batch,
                scoreAsString,
                camera.position.x - glyphLayout.width + camera.viewportWidth / 2 * .9f , camera.viewportHeight * 0.95f - glyphLayout.height / 2);
    }
}
