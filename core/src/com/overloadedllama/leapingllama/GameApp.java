package com.overloadedllama.leapingllama;

import android.content.Context;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.screens.LoadScreen;



public class GameApp extends Game {
	public final Context context;

	public SpriteBatch batch;
	public BitmapFont font;

	public final static float WIDTH = 1440f;
	public final static float HEIGHT = 720f;


	public GameApp(Context context) {
		this.context = context;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("vcrosdmono.fnt")); // using a free font downloaded from dafont.com and then converted into a .fnt (32bit, white text with alpha) using a free tool called bmfont.


		setScreen(new LoadScreen(LlamaUtil.getUniqueInstance(this)));
	}

	@Override
	public void render() {
		super.render(); // important!
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

}

