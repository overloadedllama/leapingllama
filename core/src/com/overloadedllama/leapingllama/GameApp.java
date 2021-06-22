package com.overloadedllama.leapingllama;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.overloadedllama.leapingllama.screens.LoadScreen;

public class GameApp extends Game {
	public SpriteBatch batch;
	public BitmapFont font;

	public final static float WIDTH = 1280f;
	public final static float HEIGHT = 720f;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("vcrosdmono.fnt")); // using a free font downloaded from dafont.com and then converted into a .fnt (32bit, white text with alpha) using a free tool called bmfont.

		this.setScreen(new LoadScreen(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

}

//ciao prova.....
//comunque devo capire cosa serve quell'icona a forma di ciliegia

// mi sa che riporta quello che uno ha scritto direttamente
//bhe ottimo, molto comodo


//ahahahaah, boh


