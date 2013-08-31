package org.leoche.slimeball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Score extends Object{
	
	private float medium;
	private float mediumX;
	public float points = 0;
	private float mediumDelta;
	private TextureRegion barTexture;
	private TextureRegion color1Texture;
	private TextureRegion color2Texture;
	private int tick = 0;
	
	public Score(float y, float color1, float color2, float pointsTotal){
		this.pos = new Vector2(0,y);
		this.siz = new Vector2(1920,89);
		this.medium = this.mediumX = SlimeBall.W/2;
		this.mediumDelta = SlimeBall.W/2/pointsTotal;
		this.texture = new Texture(Gdx.files.internal("scores.png"));
		this.barTexture = new TextureRegion(this.texture,0,0,(int) this.siz.x,(int) this.siz.y);
		this.color1Texture = new TextureRegion(this.texture,0,(int) (178+(70*color1)),(int) this.siz.x,(int) 70);
		this.color2Texture = new TextureRegion(this.texture,0,(int) (178+(70*color2)),(int) this.siz.x,(int) 70);
	}
	public void addPoint(int player){
		if(player == 0){
			this.points--;
		}
		if(player == 1){
			this.points++;
		}
		this.medium = (SlimeBall.W/2)+(points*mediumDelta);
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha){
		spriteBatch.draw(this.color1Texture,-this.mediumX,this.pos.y+10);
		spriteBatch.draw(this.color2Texture,SlimeBall.W-this.mediumX,this.pos.y+10);
		spriteBatch.draw(this.barTexture,-this.mediumX,this.pos.y);
		spriteBatch.draw(this.barTexture,SlimeBall.W-this.mediumX+SlimeBall.W,this.pos.y,0f,0f,this.siz.x,this.siz.y , -1f, 1f, 0f);
	}
	
	@Override
	public void act(float delta){
		if(Math.abs(this.mediumX-this.medium)<10) this.mediumX = this.medium;
		if(this.mediumX>this.medium) this.mediumX-=9;
		if(this.mediumX<this.medium) this.mediumX+=9;
		this.tick++;
		if(this.tick<this.frameSpeed){
			this.barTexture = new TextureRegion(this.texture,0,0,(int) this.siz.x,(int) this.siz.y);
		}else if(this.tick<this.frameSpeed*2){
			this.barTexture = new TextureRegion(this.texture,0,(int) this.siz.y,(int) this.siz.x,(int) this.siz.y);
		}else{
			this.tick = 0;
		}
	}
	public void dispose(){
		this.texture.dispose();
	}
}
