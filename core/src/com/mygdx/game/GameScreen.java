package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PunchyFist;
import com.sun.xml.internal.fastinfoset.util.CharArrayIntMap;
import com.sun.xml.internal.ws.message.saaj.SAAJHeader;

public class GameScreen extends ScreenAdapter {
    private static final float WORLD_WITH = 1232;
    private static final float WORLD_HEIGHT = 448;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private final PunchyFist punchyFist;

    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private Punchy punchy;
    private Fist fist;


    public GameScreen(PunchyFist punchyFist){
        this.punchyFist = punchyFist;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

        @Override
    public void show(){
        camera = new OrthographicCamera();
            //
            //
            // camera.position.set(WORLD_WITH / 2, WORLD_HEIGHT / 2, 0);
            //camera.update();
            viewport = new FitViewport(WORLD_WITH, WORLD_HEIGHT, camera);
            viewport.apply(true);
            shapeRenderer = new ShapeRenderer();
            batch = new SpriteBatch();
            tileMap = punchyFist.getAssetManager().get("map.tmx");
            orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap, batch);
            orthogonalTiledMapRenderer.setView(camera);
            //resize((int)WORLD_WITH, (int)WORLD_HEIGHT);

            TextureAtlas textureAtlas = punchyFist.getAssetManager().get("punchy_fist_assets.atlas");

            punchy = new Punchy(textureAtlas.findRegion("punchy"));
            fist = new Fist(textureAtlas.findRegion("fist"));
    }

        @Override
    public void render(float delta){
        update(delta);
        clearScreen();
        draw();
        drawDebug();
    }

    private void update(float delta){
        punchy.update(delta);
        //fist.setPosition(punchy.getX() + 32F, punchy.getY() + 32F);
        fist.setPosition(punchy);
        fist.update();
        //stopFistPunch();
        stopPunchyLeavingTheScreen();
    }

    private void clearScreen(){
        Gdx.gl.glClearColor(
                Color.BLACK.r,
                Color.BLACK.g,
                Color.BLACK.b,
                Color.BLACK.a
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw(){
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        orthogonalTiledMapRenderer.render();
        batch.begin();
        punchy.draw(batch);
        fist.draw(batch);
        batch.end();
    }

    private void drawDebug(){
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        punchy.drawDebug(shapeRenderer);
        fist.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }

    private void stopFistPunch(){
        if (fist.isPunching())
            fist.punched();
    }
    private void stopPunchyLeavingTheScreen(){
        if(punchy.getY() < 0){
            punchy.setPosition(punchy.getX(), 0);
            punchy.landed();
        }

        if(punchy.getY() > WORLD_HEIGHT - punchy.HEIGHT){
            punchy.setPosition(punchy.getX(), WORLD_HEIGHT - punchy.HEIGHT);
        }

        if(punchy.getX() < 0){
            punchy.setPosition(0, punchy.getY());
        }

        if(punchy.getX() + punchy.WIDTH >  WORLD_WITH){
            punchy.setPosition(WORLD_WITH - punchy.WIDTH, punchy.getY());
        }


    }

}
