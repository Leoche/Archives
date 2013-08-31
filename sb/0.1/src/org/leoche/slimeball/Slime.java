package org.leoche.slimeball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Slime extends Object{
	public int id;
	public boolean onfloor = true;
	public float floor;
	public float color;
	public float r;
	public int tick;
	private float sens;
	private TextureRegion[] eyesRegions = new TextureRegion[3];
	private float[] eyesPos = {0f,0f};
	public int isWinning = 0;
	public boolean wink = false;
	public Sound[] sounds = new Sound[4];

	public Slime(int id, float x, float y, float r, float sens, float color){
		this.id = id;
		this.pos = new Vector2(x,y);
		this.vel = new Vector2(0,0);
		this.siz = new Vector2(r*2,r*2);
		this.r = siz.x/2;
		this.speed = 30;
		this.friction = .9f;
		this.floor = y;
		this.color = color;
		if(sens<0) this.sens = 1;
		else this.sens = -1;
		this.texture = new Texture(Gdx.files.internal("slimes.png"));
		this.texture.setFilter(TextureFilter.Linear,TextureFilter.Linear );
		this.textureRegion = new TextureRegion( this.texture, 0, (int) ((this.siz.y+20)*this.color), (int) this.siz.x, (int) (this.siz.y+20));
		this.eyesRegions[0] = new TextureRegion( this.texture, 0, (int) ((this.siz.y+20)*this.color), 62, 49);
		this.sounds[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/Splash (1).wav"));
		this.sounds[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/Splash (2).wav"));
		this.sounds[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/Splash (3).wav"));
		this.sounds[3] = Gdx.audio.newSound(Gdx.files.internal("sounds/Splash (4).wav"));
	}
	
	public Slime(int id, float x, float y, float sens, float color){
		this.id = id;
		this.pos = new Vector2(x,y);
		this.vel = new Vector2(0,0);
		this.siz = new Vector2(278,151);
		this.r = siz.x/2;
		this.speed = 30;
		this.friction = .9f;
		this.floor = y;
		this.color = color;
		if(sens<0) this.sens = 1;
		else this.sens = -1;
		this.texture = new Texture(Gdx.files.internal("slimes.png"));
		this.texture.setFilter(TextureFilter.Linear,TextureFilter.Linear );
		this.textureRegion = new TextureRegion( this.texture, 0, (int) ((this.siz.y+20)*this.color), (int) this.siz.x, (int) (this.siz.y+20));
		this.eyesRegions[0] = new TextureRegion( this.texture, 834, (int) ((this.siz.y+20)*this.color), 62, 51 );
		this.eyesRegions[1] = new TextureRegion( this.texture, 834, (int) ((this.siz.y+20)*this.color)+51, 62, 44);
		this.eyesRegions[2] = new TextureRegion( this.texture, 834, (int) ((this.siz.y+20)*this.color)+51+44, 62, 43);
		this.sounds[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/Splash (1).wav"));
		this.sounds[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/Splash (2).wav"));
		this.sounds[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/Splash (3).wav"));
		this.sounds[3] = Gdx.audio.newSound(Gdx.files.internal("sounds/Splash (4).wav"));
	}

	public void playJumpSound(){
		this.sounds[(int) Math.floor(Math.random()*(this.sounds.length-1))].play(1f);
	}
	public void playJumpSound(float s){
		this.sounds[(int) Math.floor(Math.random()*(this.sounds.length-1))].play(s);
	}
	
	@Override
	 public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		spriteBatch.end();
		spriteBatch.begin();
		if(this.siz.x<278) return;
		if(this.sens==1f){
			spriteBatch.draw(this.textureRegion, this.pos.x-this.siz.x/2, this.pos.y-20, (int) 0, (int) 0, this.siz.x , (this.siz.y+20), this.sens, 1f, 0f);
			spriteBatch.draw(this.eyesRegions[0], this.pos.x + this.siz.x -this.siz.x/2-90+eyesPos[0], this.pos.y+60+eyesPos[1]);
			if(this.isWinning == -1 || this.wink) spriteBatch.draw(this.eyesRegions[1], this.pos.x + this.siz.x -this.siz.x/2-92+eyesPos[0], this.pos.y+70+eyesPos[1]);
			if(this.isWinning == 1 || this.wink)spriteBatch.draw(this.eyesRegions[2], this.pos.x + this.siz.x -this.siz.x/2-88+eyesPos[0], this.pos.y+62+eyesPos[1]);
		}
		else{
			spriteBatch.draw(this.textureRegion, this.pos.x + this.siz.x -this.siz.x/2, this.pos.y-20, (int) 0, (int) 0, this.siz.x , (this.siz.y+20), this.sens, 1f, 0f);
			spriteBatch.draw(this.eyesRegions[0], this.pos.x + this.siz.x -this.siz.x/2-195+eyesPos[0], this.pos.y+60+eyesPos[1], 0, 0, 62, 51, -1f, 1f, 0f);
			if(this.isWinning == -1 || this.wink) spriteBatch.draw(this.eyesRegions[1], this.pos.x + this.siz.x -this.siz.x/2-193+eyesPos[0], this.pos.y+70+eyesPos[1], 0, 0, 62, 44, -1f, 1f, 0f);
			if(this.isWinning == 1 || this.wink)spriteBatch.draw(this.eyesRegions[2], this.pos.x + this.siz.x -this.siz.x/2-198+eyesPos[0], this.pos.y+62+eyesPos[1], 0, 0, 62, 43, -1f, 1f, 0f);
		}
		/* drawEyes */
		if(SlimeBall.debug) drawBounds();
	}

	@Override
	public void act(float delta){
		tickit();
		this.vel.scl(this.friction,this.friction);
		this.vel.sub(0,this.gravity);
		this.pos.add(this.vel.x*this.speed*delta,this.vel.y*this.speed*delta);
		if(this.id == 0){
			if(this.pos.x-this.r<0){this.pos.x=this.r;}
			if(this.pos.x+this.r>942){this.pos.x=942-this.r;}
		} // 942
		if(this.id == 1){
			if(this.pos.x+this.r>SlimeBall.W){this.pos.x=SlimeBall.W-this.r;}
			if(this.pos.x-this.r<977){this.pos.x=977+this.r;}
		}
		if(this.pos.y<this.floor){this.pos.y=this.floor; this.vel.scl(0,-restitution);}
		this.onfloor = this.pos.y<this.floor+20;
	}
	public void tickit(){
		this.tick++;
		if(this.tick<this.frameSpeed){
			this.eyesPos[0] = 0;
			this.eyesPos[1] = 0;
			this.textureRegion = new TextureRegion( this.texture, 0, (int) ((this.siz.y+20)*this.color), (int) this.siz.x, (int) (this.siz.y+20));
		}else if(this.tick<this.frameSpeed*2){
			this.eyesPos[0] = 2;
			this.eyesPos[1] = 2;
			this.textureRegion = new TextureRegion( this.texture, (int) this.siz.x, (int) ((this.siz.y+20)*this.color), (int) this.siz.x, (int) (this.siz.y+20));
		}else if(this.tick<this.frameSpeed*3){
			this.eyesPos[0] = 1;
			this.eyesPos[1] = 2;
			this.textureRegion = new TextureRegion( this.texture, (int) this.siz.x*2, (int) ((this.siz.y+20)*this.color), (int) this.siz.x, (int) (this.siz.y+20));
		}else if(this.tick<this.frameSpeed*4){
			this.tick=0;
			this.wink=false;
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
