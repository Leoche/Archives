package org.leoche.slimeball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Ball extends Object{
	public float vx;
	public float vy;
	public float r;
	private int tick;
	private float volume = 1f;
	private boolean demo = false;
	private GameScreen gameScreen;
	public Sound[] sounds = new Sound[4]; 

	public Ball(GameScreen parentScreen, float x, float y, float w, float h){
		this.gameScreen = parentScreen;
		this.pos = new Vector2(x,y);
		this.vel = new Vector2(0,0);
		this.siz = new Vector2(w,h);
		this.r = siz.x/2;
		this.gravity = .4f;
		this.friction = .99f;
		this.texture = new Texture(Gdx.files.internal("ball.png"));
		this.texture.setFilter(TextureFilter.Linear,TextureFilter.Linear );
		this.textureRegion = new TextureRegion( this.texture, 0, 0, this.texture.getWidth(), this.texture.getWidth());
		this.sounds[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ball (1).wav"));
		this.sounds[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ball (2).wav"));
		this.sounds[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ball (3).wav"));
		this.sounds[3] = Gdx.audio.newSound(Gdx.files.internal("sounds/ball (4).wav"));
	}

	public Ball(float x, float y, boolean demo){
		this.pos = new Vector2(x,y);
		this.vel = new Vector2(0,0);
		this.siz = new Vector2(96,96);
		this.r = siz.x/2;
		this.demo = demo;
		this.gravity = .4f;
		this.friction = .99f;
		this.texture = new Texture(Gdx.files.internal("ball.png"));
		this.texture.setFilter(TextureFilter.Linear,TextureFilter.Linear );
		this.textureRegion = new TextureRegion( this.texture, 0, 0, this.texture.getWidth(), this.texture.getWidth());
		this.sounds[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/ball (1).wav"));
		this.sounds[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/ball (2).wav"));
		this.sounds[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/ball (3).wav"));
		this.sounds[3] = Gdx.audio.newSound(Gdx.files.internal("sounds/ball (4).wav"));
	}
	public void setVolume(float v){
		this.volume = v;
	}
	public void playSound(){
		this.sounds[(int) Math.floor(Math.random()*(this.sounds.length-1))].play(volume);
	}
	public void playSound(float s){
		this.sounds[(int) Math.floor(Math.random()*(this.sounds.length-1))].play(s);
	}
	
	@Override
	 public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		spriteBatch.end();
		spriteBatch.begin();
		spriteBatch.draw(this.textureRegion, (int) this.pos.x-this.r, (int) this.pos.y-this.r, this.siz.x, this.siz.x);
		if(SlimeBall.debug) drawBounds();
	 }
	
	@Override
	public void act(float delta){
		tickit();
		if(this.vel.y>15) this.vel.y = 15;
		if(this.vel.x>15) this.vel.x = 15;
		this.vel.scl(this.friction,1);
		this.vel.sub(0,this.gravity);
		this.pos.add(this.vel.x*this.speed*delta,this.vel.y*this.speed*delta);
		if(this.pos.x<this.r){this.pos.x=this.r; this.vel.x*=-1;playSound();}
		if(this.pos.x>SlimeBall.W-this.r){this.pos.x=SlimeBall.W-this.r; this.vel.x*=-1;playSound();}
		if(this.demo && this.pos.x<SlimeBall.W/2+this.r/2){this.pos.x=SlimeBall.W/2+this.r/2; this.vel.x*=-1;playSound();}
		if(this.pos.y<SlimeBall.floor+this.r){
			playSound();
			this.pos.y=SlimeBall.floor+this.r; 
			this.vel.y*=-1;
			if(!demo && (gameScreen.mode.equals("tennis") || gameScreen.mode.equals("volley"))){
				if(this.pos.x>SlimeBall.W/2) gameScreen.addPoint(this.gameScreen.c[0].hashCode());
				else gameScreen.addPoint(this.gameScreen.c[1].hashCode());
				if(gameScreen.paused == true) return;
				this.pos = new Vector2(SlimeBall.W/2,SlimeBall.H);
				this.gameScreen.slimes.get(this.gameScreen.c[0].hashCode()).vel = new Vector2(-400,10);
				this.gameScreen.slimes.get(this.gameScreen.c[1].hashCode()).vel = new Vector2(400,10);
				this.vel = new Vector2(0,0);
			}
		}
	}
	public void tickit(){
		this.tick++;
		if(this.tick<this.frameSpeed){
			this.textureRegion = new TextureRegion( this.texture, 0, 0, (int) this.siz.x, (int) this.siz.x);
		}else if(this.tick<this.frameSpeed*2){
			this.textureRegion = new TextureRegion( this.texture, 0, (int) this.siz.x, (int) this.siz.x, (int) this.siz.x);
		}else if(this.tick<this.frameSpeed*3){
			this.textureRegion = new TextureRegion( this.texture, 0, (int) this.siz.x*2, (int) this.siz.x, (int) this.siz.x);
		}else if(this.tick<this.frameSpeed*4){
			this.tick=0;
		}
	}
	public void dispose(){
		this.texture.dispose();
		this.sounds[0].dispose();
		this.sounds[1].dispose();
		this.sounds[2].dispose();
		this.sounds[3].dispose();
	}
}
