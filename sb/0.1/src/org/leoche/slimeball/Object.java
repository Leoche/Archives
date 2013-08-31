package org.leoche.slimeball;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Object extends Actor{
	
	private Rectangle bounds = new Rectangle();
	public Vector2 pos;
	public Vector2 vel;
	public Vector2 siz;
	public float speed = 100;
	public float jump = 130;
	public float friction = .97f;
	public float gravity = 4f;
	public float restitution = .7f;
	public int frameSpeed = 5;
	public Texture texture;
	public TextureRegion textureRegion;
	public float active = 0;
	
	public Object(){
		this.pos = new Vector2(0,0);
		this.vel = new Vector2(0,0);
		this.siz = new Vector2(10,10);
	}
	
	public Object(float x, float y){
		this.pos = new Vector2(x,y);
		this.vel = new Vector2(0,0);
		this.siz = new Vector2(10,10);
	}
	
	public Object(Texture texture, float x, float y){
		this.pos = new Vector2(x,y);
		this.vel = new Vector2(0,0);
		this.texture = texture;
		this.siz = new Vector2(texture.getWidth(),texture.getHeight());
	}
	
	public Object(float x, float y, float w, float h){
		this.pos = new Vector2(x,y);
		this.vel = new Vector2(0,0);
		this.siz = new Vector2(w,h);
	}
	
	@Override
	public void act(float delta) {
	  super.act(delta);
	  bounds.set(pos.x, pos.y, siz.x, siz.y);
	}
	
	public void act() {}
	public void drawBounds(){
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 0, 0, .5f);
		shapeRenderer.rect(pos.x, pos.y, siz.x, siz.y);
		shapeRenderer.setColor(1, 1, 1, .5f);
		shapeRenderer.rect(pos.x, pos.y, 5, 5);
		shapeRenderer.setColor(0, 0, 0, .5f);
		shapeRenderer.rect(pos.x-1, pos.y-1, 7, 7);
	    shapeRenderer.end();
	}
	
	@Override
	 public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		spriteBatch.end();
		spriteBatch.begin();
		spriteBatch.draw(this.texture, this.pos.x, this.pos.y);
	 }
	
	 public void draw(SpriteBatch spriteBatch, BitmapFont fnt, float x, float y) {
		spriteBatch.end();
		spriteBatch.begin();
		spriteBatch.draw(this.texture, this.pos.x, this.pos.y);
	 }
	 public void setActive(){ this.active = 1;}
	 public void unsetActive(){ this.active = 0;}
}
