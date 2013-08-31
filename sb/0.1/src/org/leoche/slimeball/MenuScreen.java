package org.leoche.slimeball;

import java.io.IOException;

import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ObjectMap;
public class MenuScreen implements Screen{
	
	private SlimeBall game;
	private Stage stage;
	private Menu menu;
	private Object trottoir;
	private Ball ball;
	private Slime slime;
	private Level level;
	private News news;
	
	public MenuScreen(SlimeBall game){
		Gdx.input.setCatchMenuKey(true);
		this.game = game;
		Texture.setEnforcePotImages(false);
		this.stage = new Stage();
		
		try {this.news = new News();}
		catch (IOException e) {System.out.println(e);}
		stage.addActor(news);
		
		this.menu = new Menu(this,game,"title");
		stage.addActor(menu);
		
		this.trottoir = new Object(new Texture(Gdx.files.internal("trottoir.png")),0f,0f);
		stage.addActor(trottoir);
		
		SlimeBall.floor = 260;
		
		this.slime = new Slime(1, SlimeBall.W/4*3, SlimeBall.floor, 1f,(float) Math.floor(Math.random()*4));
		stage.addActor(slime);
		
		this.ball = new Ball(SlimeBall.W/4*3-50, 900, true);
		ball.setVolume(.1f);
		ball.vel.x = -5;
		stage.addActor(ball);
		
		level = new Level(game,this,ball,"menu", "tennis");
		level.addSlimes(slime);
		stage.addActor(level);
	}
	
	public void play(ObjectMap<Integer, Integer> colorPlayer){
		game.initGame(colorPlayer);
		this.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.graphics.getGL10().glClearColor(.3f, .3f, .3f, 1f);
   		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		animateDemo();
	}
	public void animateDemo(){
		if(Math.abs(slime.pos.x-ball.pos.x)>200){
			if(slime.pos.x<ball.pos.x) slime.pos.add((float) 0.5*slime.speed,0);
			else slime.pos.add((float) -0.5*slime.speed,0);
		}else if(Math.abs(slime.pos.x-ball.pos.x)>70){
			if(slime.pos.x<ball.pos.x) slime.pos.add((float) 0.3*slime.speed,0);
			else slime.pos.add((float) -0.3*slime.speed,0);
		}else{
			if(Math.random()<0.05 && slime.onfloor){
				slime.vel.add(0,slime.jump);
				slime.playJumpSound(.1f);
			}
		}
		resolveSlimeCollisions(ball,slime);
	}
	public void resolveSlimeCollisions(Ball o1,Slime o2){
		Vector2 b = new Vector2(o1.pos);
		Vector2 c = new Vector2(o2.pos);
		Vector2 d = new Vector2(c.sub(b));
		float distance_squared = (float) d.dot(d);
		float radius = o1.r + o2.r;
		float radius_squared = radius * radius;
		if(distance_squared > radius_squared || o1.pos.y-o1.r<o2.pos.y) return;
		ball.playSound(.2f);
		o2.wink = true;
		o2.tick = 0;
		float distance = (float) Math.sqrt(distance_squared);
	    Vector2 ncoll = d.div(distance).scl(8,14);
	    o1.vel.sub(ncoll);
	}
	@Override
	public void dispose() {
		System.out.println("dispose Menu");
		level.dispose();
		menu.dispose();
		news.dispose();
		slime.dispose();
		ball.dispose();
		trottoir.texture.dispose();
		stage.dispose();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resize(int arg0, int arg1) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
	}
}
