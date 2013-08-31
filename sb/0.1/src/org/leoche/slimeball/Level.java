package org.leoche.slimeball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ObjectMap;

public class Level extends Object{
	private SlimeBall game;
	private Screen gamescreen;
	public int floor = 240;
	public int level_id = 0;
	public int level_mode = 0;
	public int scoreY = 20;
	public float[] dynamics = new float[5];
	public Ball ball;
	public ObjectMap<Integer, Slime> slimes = new ObjectMap<Integer, Slime>();
	private BitmapFont font;
	private TextureRegion ballTextureRegion;
	private TextureRegion slimeTextureRegion;
	private TextureRegion[] lightsTexture = new TextureRegion[4];
	private int winner = -1;
	private int winner_id = -1;
	private float[] lightsTicks = {0,360f,0,0};
	private Texture winoverlay;
	private Texture buttonsTexture;
	private TextWrapper[] wintexts = new TextWrapper[3];
	private float wintexttick = 1.2f;
	private int wintextsens = -1;
	public Sound god_sound;
	public Music level_sound;
	
	public Level(SlimeBall game,Screen gamescreen, Ball ball,String level,String mode){
		this.game = game;
		this.gamescreen = gamescreen;
		this.ball = ball;
		this.god_sound = Gdx.audio.newSound(Gdx.files.internal("sounds/god.wav"));
		if(level.equals("menu")) this.level_id = 0;
		if(level.equals("beach")) this.level_id = 1;
		if(mode.equals("tennis")) this.level_mode = 1;
		if(this.level_id != 0) this.font = new BitmapFont(Gdx.files.internal("cookie.fnt"),Gdx.files.internal("cookie_0.png"),false);
		if(this.level_id == 0) this.floor = 260;
		if(this.level_id != 0){
			this.texture = new Texture(Gdx.files.internal("level"+this.level_id+".png"));
			this.texture.setFilter(TextureFilter.Linear,TextureFilter.Linear );
		}
		/*Ombre ball texture*/
		Texture ballTexture = new Texture(Gdx.files.internal("ball.png"));
		ballTexture.setFilter(TextureFilter.Linear,TextureFilter.Linear );
		this.ballTextureRegion = new TextureRegion( ballTexture, 0, 288, 96, 42);
		/*Ombre slime texture*/
		Texture slimeTexture = new Texture(Gdx.files.internal("slimes.png"));
		slimeTexture.setFilter(TextureFilter.Linear,TextureFilter.Linear );
		this.slimeTextureRegion = new TextureRegion( slimeTexture, 0, 684, 278, 68);
		if(this.level_id != 0){
			/*Lumieres texture*/
			Texture lightTexture = new Texture(Gdx.files.internal("light.png"));
			lightsTexture[0] = new TextureRegion(lightTexture,1000,0,1000,1000);
			lightsTexture[1] = new TextureRegion(lightTexture,0,0,1000,1000);
			lightsTexture[2] = new TextureRegion(lightTexture,0,1000,1000,1000);
			lightsTexture[3] = new TextureRegion(lightTexture,1000,1000,1000,1000);
			this.winoverlay = new Texture(Gdx.files.internal("winoverlay.png"));
			/*Buttons texture*/
			this.buttonsTexture = new Texture(Gdx.files.internal("buttons.png"));
	
			switch(this.level_id){
			case 1:
				dynamics[0] = 413f;
				dynamics[1] = 946f;
				dynamics[2] = 1382f;
				dynamics[3] = 1900f;
				level_sound = Gdx.audio.newMusic(Gdx.files.internal("sounds/beach.mp3"));
				level_sound.setLooping(true);
				level_sound.setVolume(1f);
				level_sound.play();
			break;
			}
		}
	}

	public void addSlimes(ObjectMap<Integer, Slime> s){
		this.slimes=s; 
	}
	public void addSlimes(Slime s){
		ObjectMap<Integer, Slime> tmp = new ObjectMap<Integer, Slime>();
		tmp.put(0,s);
		this.slimes=tmp; 
	}
	
	public void win(int player){
		if(this.winner != player) god_sound.play(1f);
		this.winner = player;
		if(Controllers.getControllers().get(0).hashCode() == player) this.winner_id = 0;
		else this.winner_id = 1;
	}
	
	@Override
	public void act(float deltaTime){
		dynamics[0]-=.5;
		if(dynamics[0]<-235) dynamics[0] = SlimeBall.W;
		dynamics[1]-=.5;
		if(dynamics[1]<-128) dynamics[1] = SlimeBall.W;
		dynamics[2]-=.5;
		if(dynamics[2]<-261) dynamics[2] = SlimeBall.W;
		dynamics[3]-=.5;
		if(dynamics[3]<-128) dynamics[3] = SlimeBall.W;
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		if(SlimeBall.debug){
			ShapeRenderer shapeRenderer = new ShapeRenderer();
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(1, 0, 0, 1f);
			shapeRenderer.rect(0, this.floor, SlimeBall.W, 1);
		    shapeRenderer.end();
		    return;
		}
		spriteBatch.end();
		spriteBatch.begin();
		switch(this.level_id){
		case 1:			
			/* Fond de l'image */
			this.textureRegion = new TextureRegion(this.texture, 0, 0, SlimeBall.W, SlimeBall.H);
			spriteBatch.draw(this.textureRegion, 0, 0);
			/* Nuage 1 */
			this.textureRegion = new TextureRegion(this.texture, 661, SlimeBall.H, 235, 138);
			spriteBatch.draw(this.textureRegion, dynamics[0], SlimeBall.H-121-138);
			/* Nuage 2 */
			this.textureRegion = new TextureRegion(this.texture, 661, SlimeBall.H+138, 128, 92);
			spriteBatch.draw(this.textureRegion, dynamics[1], SlimeBall.H-230-91);
			/* Nuage 3 */
			this.textureRegion = new TextureRegion(this.texture, 661, SlimeBall.H+138+92, 261, 154);
			spriteBatch.draw(this.textureRegion, dynamics[2], SlimeBall.H-86-154);
			/* Nuage 4 */
			this.textureRegion = new TextureRegion(this.texture, 661, SlimeBall.H+138, 128, 92);
			spriteBatch.draw(this.textureRegion, dynamics[3], SlimeBall.H-230-191);
			/* Palmier*/
			this.textureRegion = new TextureRegion(this.texture, 0, SlimeBall.H, 660, 825);
			spriteBatch.draw(this.textureRegion, 0, SlimeBall.H-825);
			handleWinner(spriteBatch);
		break;
		}
		/* Ombre ball */
		float ballY = (float) (this.ball.pos.y-this.floor-ball.r)/900;
		spriteBatch.draw(this.ballTextureRegion, this.ball.pos.x-47+(ballY*90)/2, this.floor-21+(ballY*40)/2, 96 -(ballY*90), 42-(ballY*40));
		/* Ombres Slime */
		if(slimes.size == 0) return;
		for (ObjectMap.Entry<Integer, Slime> entry : slimes.entries()){
		    Slime s = entry.value;
		    
			float slimeY = (float) (s.pos.y-this.floor)/280;
			spriteBatch.draw(this.slimeTextureRegion, s.pos.x-139+(slimeY*130)/2, this.floor-34+(slimeY*40)/2, 278-(slimeY*130), 68-(slimeY*40));
		}
	 }
	public void handleWinner(SpriteBatch spriteBatch){
		if(this.winner != -1){
			spriteBatch.setColor(1.0f, 1.0f, 1.0f, lightsTicks[3]);
			lightsTicks[0]+=.2;
			lightsTicks[1]-=.3;
			lightsTicks[2]+=.4;
			lightsTicks[3]+=.1;
			if(lightsTicks[0]>=360) lightsTicks[0]=0;
			if(lightsTicks[1]<=0) lightsTicks[1]=360;
			if(lightsTicks[2]>=360) lightsTicks[2]=0;
			if(lightsTicks[3]>1f) lightsTicks[3]=1f;
			if(lightsTicks[3]==1f) ((GameScreen) gamescreen).winned = true;
			float x = SlimeBall.W/4 + (((GameScreen) gamescreen).slimes.get(this.winner).id*SlimeBall.W/2)-500;
			float y = SlimeBall.H/2-500+60;
			if(((GameScreen) gamescreen).slimes.get(this.winner).id==0)spriteBatch.draw(this.winoverlay,0,0,SlimeBall.W,SlimeBall.H,0,0,(int) SlimeBall.W,(int) SlimeBall.H,true,false);
			else spriteBatch.draw(this.winoverlay,0,0);
			spriteBatch.draw(lightsTexture[0],x,y);
			spriteBatch.draw(lightsTexture[1],x,y,500f,500f,1000f,1000f,1f,1f,lightsTicks[0]);
			spriteBatch.draw(lightsTexture[2],x,y,500f,500f,1000f,1000f,1f,1f,lightsTicks[1]);
			spriteBatch.draw(lightsTexture[3],x,y,500f,500f,1000f,1000f,1f,1f,lightsTicks[2]);
			spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			switch((int) ((GameScreen) gamescreen).slimes.get(this.winner).color){
			case 2:
				font.setColor(.1f, .7f, 1f, 1f);
			break;
			case 1:
				font.setColor(.9f, .2f, .6f, 1f);
			break;
			case 0:
				font.setColor(.3f, .9f, .1f, 1f);
			break;
			case 3:
				font.setColor(.9f, .8f, .1f, 1f);
			break;
			}
			wintexts[0] = new TextWrapper("Player "+(this.winner_id+1),SlimeBall.W/2+100,SlimeBall.H-170,1);
			wintexts[1] = new TextWrapper(" Win!",SlimeBall.W/2+100,SlimeBall.H-170,-1);
			wintexts[2] = new TextWrapper("    Play Again        Back to Menu",SlimeBall.W/2,120,0);
			spriteBatch.draw(buttonsTexture,(float) (SlimeBall.W/2-700),75f,0,0,90,90);
			spriteBatch.draw(buttonsTexture,(float) (SlimeBall.W/2+40),75f,90,0,90,90);
			font.setScale(wintexttick);
			wintexts[0].draw(spriteBatch, this.font);
			font.setColor(1f, 1f, 1f, 1f);
			wintexts[1].draw(spriteBatch, this.font);
			font.setColor(1f, 1f, 1f, 1f);
			font.setScale(.8f);
			wintexts[2].draw(spriteBatch, this.font);
			if(wintextsens<0){
				wintexttick-=.015;
				if(wintexttick<1) wintextsens *= -1;
			}else{
				wintexttick+=.015;
				if(wintexttick>1.2){
					wintextsens *= -1;
				}
			}
		}
	}
	public void dispose(){
		if(this.level_id != 0) this.texture.dispose();
		if(this.level_id != 0) this.level_sound.dispose();
		if(this.level_id != 0) this.god_sound.dispose();
		if(this.level_id != 0) this.font.dispose();
	}
}
