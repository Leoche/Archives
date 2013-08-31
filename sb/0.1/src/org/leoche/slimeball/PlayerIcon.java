package org.leoche.slimeball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PlayerIcon extends Object {
	private int state = 0;
	private float tick = 1;
	private int sens = -1;
	private int cycle = 0;
	
	public PlayerIcon(SlimeBall game, int state, float x, float y){
		this.state = state;
		this.pos = new Vector2(x,y);
		this.vel = new Vector2(0,0);
		this.siz = new Vector2(44,57);
		this.texture = new Texture(Gdx.files.internal("controllers.png"));
		if(state == 0 || state == 1) this.textureRegion = new TextureRegion(this.texture,44,0,44,57);
		else this.textureRegion = new TextureRegion(this.texture,0,0,44,57);
	}
	
	public void act(){
		if(this.state == 0){
			if(sens<0){
				tick-=.03;
				if(tick<.8) sens *= -1;
			}else{
				tick+=.03;
				if(tick>1.2){
					sens *= -1;
					this.cycle++;
				}
			}
			if(cycle>3){
				this.cycle=0;
				this.tick=1;
				this.state = 1;
			}
		}
	}
	public void setState(int state){
		if(this.state == state) return;
		this.state = state;
		this.tick = 1;
		this.sens = -1;
		if(state == 0 || state == 1) this.textureRegion = new TextureRegion(this.texture,44,0,44,57);
		else this.textureRegion = new TextureRegion(this.texture,0,0,44,57);
	}
	public int getState(){
		return this.state;
	}

	public void draw(SpriteBatch spriteBatch,BitmapFont fnt, float x, float y){
		spriteBatch.end();
		spriteBatch.begin();
		spriteBatch.draw(this.textureRegion,x+this.pos.x-(this.siz.x*tick)/2,y+this.pos.y-(this.siz.y*tick)/2,this.siz.x*tick,this.siz.y*tick);
	}
	public void dispose(){
		this.texture.dispose();
	}
}
