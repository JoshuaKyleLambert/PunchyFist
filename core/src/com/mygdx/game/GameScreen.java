package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
            resize((int)WORLD_WITH, (int)WORLD_HEIGHT);
            punchy = new Punchy();

    }

        @Override
    public void render(float delta){
        update(delta);
        clearScreen();
        draw();
        drawDebug();
    }

    private void update(float delta){
        punchy.update();

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
       // batch.begin();
       // batch.end();
    }

    private void drawDebug(){
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        punchy.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }


}
