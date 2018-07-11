package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;


public class LoadingScreen extends ScreenAdapter {

    private final PunchyFist punchyFist;

    public LoadingScreen(PunchyFist punchyFist) {
        this.punchyFist = punchyFist;
    }
}
