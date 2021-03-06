package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.game.PunchyFist;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 448;
		config.width = 1280;  // ultra panavision
		TexturePacker.process("../assets/sprites", "../assets", "punchy_fist_assets");

		new LwjglApplication(new PunchyFist(), config);
	}
}

