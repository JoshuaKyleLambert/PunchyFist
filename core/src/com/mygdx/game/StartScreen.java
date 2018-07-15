package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class StartScreen extends ScreenAdapter {
    private static final float WORLD_WITH = 1280;
    private static final float WORLD_HEIGHT = 448;

    private Stage stage;

    Texture backgroundTexture;
    Texture playPressTexture;
    Texture playTexture;
    Texture titleTexture;

    private final PunchyFist game;

    public StartScreen(PunchyFist game) {
        this.game = game;
    }

    public void show() {

        stage = new Stage(new FitViewport(WORLD_WITH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        backgroundTexture = new Texture(Gdx.files.internal("../assets/title/bg.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        playTexture = new Texture(Gdx.files.internal("../assets/title/play.png"));
        playPressTexture = new Texture((Gdx.files.internal("../assets/title/playPress.png")));
        ImageButton play = new ImageButton(new TextureRegionDrawable(new TextureRegion(playTexture)), new TextureRegionDrawable(new TextureRegion(playPressTexture)));
        play.setPosition(WORLD_WITH * 0.5f, WORLD_HEIGHT * 0.15f, Align.center);
        stage.addActor(play);

        play.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new LoadingScreen(game));
                dispose();
            }
        });


        titleTexture = new Texture((Gdx.files.internal("../assets/title/title.png")));
        Image title = new Image(titleTexture);
        title.setPosition(WORLD_WITH * 0.5f, WORLD_HEIGHT * 0.65f, Align.center);
        stage.addActor(title);


    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
        backgroundTexture.dispose();
        playPressTexture.dispose();
        playTexture.dispose();
        titleTexture.dispose();
    }

}
