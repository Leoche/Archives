package org.leoche.slimeball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen{
	private Texture logo;
	private SpriteBatch spriteBatch;
	private float tick = 0;
	private SlimeBall game;
	
	public SplashScreen(SlimeBall game){
		this.game = game;
	}
	
	@Override
	public void show(){
		logo = new Texture(Gdx.files.internal("splash.png"));
		spriteBatch = new SpriteBatch();
	}
	
	@Override
	public void render(float delta){
		Gdx.graphics.getGL10().glClearColor(0f, 0f, 0f, 1f);
		tick +=1*delta;
		
		handleInput();
		
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
		Color c = spriteBatch.getColor();
		if(tick>=8f){
			dispose();
			game.initMenu();
		}
		if(tick>=7f) spriteBatch.setColor(c.r, c.g, c.b, 0f);
		else if(tick>=6f) spriteBatch.setColor(c.r, c.g, c.b, (float)(7-tick));
		else if(tick>=3f) spriteBatch.setColor(c.r, c.g, c.b, 1f);
		else if(tick>2f) spriteBatch.setColor(c.r, c.g, c.b, (float)(tick-2));
		else spriteBatch.setColor(c.r, c.g, c.b, 0f);
		spriteBatch.draw(logo,(SlimeBall.W-logo.getWidth())/2,(SlimeBall.H-logo.getHeight())/2);
		spriteBatch.end();
	}
	
	private void handleInput(){
	}

	@Override
	public void dispose() {
		this.logo.dispose();
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resize(int arg0, int arg1) {}

	@Override
	public void resume() {}

}
