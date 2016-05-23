package com.mygdx.game.desktop.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.desktop.handlers.Utils;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = Game.TITLE;
		config.width = Utils.V_WIDTH * Utils.SCALE;
		config.height = Utils.V_HEIGHT * Utils.SCALE;

		new LwjglApplication(new Game(), config);
	}
}
