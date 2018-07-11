import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PunchyFist;
import com.sun.xml.internal.fastinfoset.util.CharArrayIntMap;
import com.sun.xml.internal.ws.message.saaj.SAAJHeader;

import java.awt.*;

public class GameScreen extends ScreenAdapter {
    private static final float WORLD_WITH = 1232;
    private static final float WORLD_HEIGHT = 448;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private final PunchyFist punchyFist;

    public GameScreen(PunchyFist punchyFist){
        this.punchyFist = punchyFist;
    }

        @Override
    public void show(){
        camera = new OrthographicCamera();
            camera.position.set(WORLD_WITH / 2, WORLD_HEIGHT / 2, 0);
            camera.update();
            viewport = new FitViewport(WORLD_WITH, WORLD_HEIGHT, camera);
            shapeRenderer = new ShapeRenderer();
            batch = new SpriteBatch();
        }

        @Override
    public void render(float delta){
        update(delta);
        clearScreen();
        draw();
        drawDebug();
    }

    private void update(float delta){

    }

    private void clearScreen(){
        Gdx.gl.glClearColor(
                Color.BLACK.getRed(),
                Color.BLACK.getGreen(),
                Color.BLACK.getBlue(),
                Color.BLACK.getAlpha()
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw(){
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.end();
    }

    private void drawDebug(){
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.end();
    }


}
