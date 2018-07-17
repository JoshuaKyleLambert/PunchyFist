package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;


public class GameScreen extends ScreenAdapter {
    private static final float WORLD_WITH = 1280;
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

    private int score = 0;

    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;
    private int CELL_SIZE = 16;

    private Array<Heart> hearts = new Array<Heart>();

    public GameScreen(PunchyFist punchyFist) {
        this.punchyFist = punchyFist;
    }

    private Hud hud;

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WITH, WORLD_HEIGHT, camera);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        tileMap = punchyFist.getAssetManager().get("map.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap, batch);
        orthogonalTiledMapRenderer.setView(camera);
        TextureAtlas textureAtlas = punchyFist.getAssetManager().get("punchy_fist_assets.atlas");

        punchy = new Punchy(textureAtlas.findRegion("punchy"), punchyFist.getAssetManager().get("fly.wav", Sound.class));
        fist = new Fist(textureAtlas.findRegion("fist"));
        bitmapFont = punchyFist.getAssetManager().get("score.fnt");
        glyphLayout = new GlyphLayout();
        populateHearts(textureAtlas);
        punchyFist.getAssetManager().get("punchyTheme.mp3", Music.class).setLooping(true);
        punchyFist.getAssetManager().get("punchyTheme.mp3", Music.class).play();

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tileMap.getLayers().get(0);
        float levelWidth = tiledMapTileLayer.getWidth() * tiledMapTileLayer.getTileWidth();
        hud = new Hud(camera, punchyFist, levelWidth);

    }

    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        draw();
        //drawDebug();
    }

    private void updateScore(int points) {
        score += points;
    }

    private void update(float delta) {
        punchy.update(delta);
        //fist.setPosition(punchy.getX() + 32F, punchy.getY() + 32F);
        fist.setPosition(punchy);
        fist.update();
        //updateScore();
        //stopFistPunch();
        stopPunchyLeavingTheScreen();
        handlePunchyCollisionWithHeart();
        handlePunchyCollision();
        updateCameraX();

    }

    private void updateCameraX(){
        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tileMap.getLayers().get(0);
        float levelWidth = tiledMapTileLayer.getWidth() * tiledMapTileLayer.getTileWidth();

        if((punchy.getX() > WORLD_WITH / 2f) && (punchy.getX() < (levelWidth - WORLD_WITH / 2f))){
            camera.position.set(punchy.getX(), camera.position.y, camera.position.z);
            camera.update();
            orthogonalTiledMapRenderer.setView(camera);
        }
    }


    private void drawScore() {
        String scoreAsString = Integer.toString(score);
        glyphLayout.setText(bitmapFont, scoreAsString);
        bitmapFont.draw(batch,
                scoreAsString,
                viewport.getWorldWidth() * 0.95f - glyphLayout.width / 2, viewport.getWorldHeight() * 0.95f - glyphLayout.height / 2);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(
                Color.BLACK.r,
                Color.BLACK.g,
                Color.BLACK.b,
                Color.BLACK.a
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        orthogonalTiledMapRenderer.render();
        batch.begin();
        for (Heart heart : hearts) {
            heart.draw(batch);
        }
        punchy.draw(batch);
        fist.draw(batch);
        hud.draw(batch);
        //drawScore();
        batch.end();
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        punchy.drawDebug(shapeRenderer);
        fist.drawDebug(shapeRenderer);
        for(Heart heart: hearts){
            heart.drawDebug(shapeRenderer);
        }
        shapeRenderer.end();
    }

    private void stopFistPunch() {
        if (fist.isPunching())
            fist.punched();
    }

    private void stopPunchyLeavingTheScreen() {
        if (punchy.getY() < 0) {
            punchy.setPosition(punchy.getX(), 0);
            punchy.landed();
        }

        if (punchy.getY() > WORLD_HEIGHT - punchy.HEIGHT) {
            punchy.setPosition(punchy.getX(), WORLD_HEIGHT - punchy.HEIGHT);
        }

        if (punchy.getX() < 0) {
            punchy.setPosition(0, punchy.getY());
        }

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tileMap.getLayers().get(0);
        float levelWidth = tiledMapTileLayer.getWidth() * tiledMapTileLayer.getTileWidth();

        if (punchy.getX() + punchy.WIDTH > levelWidth) {
            punchy.setPosition(levelWidth - punchy.WIDTH, punchy.getY());
        }


    }

    private Array<CollisionCell> punchyCoversWhat() {
        float x = punchy.getX();
        float y = punchy.getY();
        Array<CollisionCell> cellsCovered = new Array<CollisionCell>();
        float cellX = x / CELL_SIZE;
        float cellY = y / CELL_SIZE;

        int bottomLeftCellX = MathUtils.floor(cellX);
        int bottomLeftCellY = MathUtils.floor(cellY);

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tileMap.getLayers().get(0);

        cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(bottomLeftCellX, bottomLeftCellY), bottomLeftCellX, bottomLeftCellY));

        if (cellX % 1 != 0 && cellY % 1 != 0) {
            int topRightCellX = bottomLeftCellX + 1;
            int topRightCellY = bottomLeftCellY + 1;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topRightCellX, topRightCellY), topRightCellX, topRightCellY));
        }

        if (cellX % 1 != 0) {
            int bottomRightCellX = bottomLeftCellX + 1;
            int bottomRightCellY = bottomLeftCellY;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(bottomRightCellX, bottomRightCellY), bottomRightCellX, bottomLeftCellY));
        }

        if (cellY % 1 != 0) {
            int topLeftCellX = bottomLeftCellX;
            int topLeftCellY = bottomLeftCellY + 1;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topLeftCellX, topLeftCellY), topLeftCellX, topLeftCellY));
        }

        return cellsCovered;

    }

    private Array<CollisionCell> filterOutNonTiledCells(Array<CollisionCell> cells) {
        for (Iterator<CollisionCell> iter = cells.iterator(); iter.hasNext(); ) {
            CollisionCell collisionCell = iter.next();
            if (collisionCell.isEmpty()) {
                iter.remove();
            }
        }
        return cells;
    }


    private void handlePunchyCollision() {
        Array<CollisionCell> punchyCells = punchyCoversWhat();
        punchyCells = filterOutNonTiledCells(punchyCells);
        for (CollisionCell cell : punchyCells) {
            float cellLevelX = cell.cellX * CELL_SIZE;
            float cellLevelY = cell.cellY * CELL_SIZE;
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(punchy.getCollisionRectangle(), new Rectangle(cellLevelX, cellLevelY, CELL_SIZE, CELL_SIZE), intersection);
            if (intersection.getHeight() < intersection.getWidth()) {
                punchy.setPosition(punchy.getX(), intersection.getY() + intersection.getHeight());
                punchy.landed();
            } else if (intersection.getWidth() < intersection.getHeight()) {
                if (intersection.getX() == punchy.getX()) {
                    punchy.setPosition(intersection.getX(), punchy.getY());
                }
                if (intersection.getX() > punchy.getX()) {
                    punchy.setPosition(intersection.getX() - Punchy.WIDTH, punchy.getY());
                }
            }
        }


    }

    private void handlePunchyCollisionWithHeart() {
        for (Iterator<Heart> iter = hearts.iterator(); iter.hasNext(); ) {
            Heart heart = iter.next();
            if (punchy.getCollisionRectangle().overlaps(heart.getCollisionRectangle())) {
                punchyFist.getAssetManager().get("heart.wav", Sound.class).play();
                iter.remove();
                updateScore(25);
            }
        }
    }

    private void populateHearts(TextureAtlas textureAtlas) {
        MapLayer mapLayer = tileMap.getLayers().get("Collectables");
        for (MapObject mapObject : mapLayer.getObjects()) {
            hearts.add(new Heart(
                           textureAtlas.findRegion("heart"),
                           mapObject.getProperties().get("x", Float.class),
                           mapObject.getProperties().get("y", Float.class)));
        }
    }

    private class CollisionCell {
        private final TiledMapTileLayer.Cell cell;
        private final int cellX;
        private final int cellY;

        public CollisionCell(TiledMapTileLayer.Cell cell, int cellX, int cellY) {
            this.cell = cell;
            this.cellX = cellX;
            this.cellY = cellY;
        }

        public boolean isEmpty() {
            return cell == null;
        }
    }

}
