package org.leoche.slimeball;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class News extends Object{
	private String[][] boards;
	private TextWrapper pseudo;
	private TextWrapper date;
	private TextWrapper message;
	private TextWrapper nextpseudo;
	private TextWrapper nextdate;
	private TextWrapper nextmessage;
	private BitmapFont font;
	private int numBoard = 1;
	private int indexBoard = 0;
	private Texture textureWindow;
	private TextureRegion textureR2;
	private TextureRegion textureL2;
	private String[] currentBoard = new String[3];
	private String[] nextBoard = new String[3];
	private boolean[] R2 = {false,false,false,false};
	private boolean[] L2 = {false,false,false,false};
	private boolean onAnim = false;
	private float animSens = 1;
	private float currentX = 0;
	private boolean lastR2 = true;
	private Sound slide;
	private boolean lastL2 = false;
	
	public News() throws IOException{
		this.texture = new Texture(Gdx.files.internal("news.png"));
		Texture L2R2 = new Texture(Gdx.files.internal("news-controls.png"));
		this.slide = Gdx.audio.newSound(Gdx.files.internal("sounds/slide.mp3"));
		this.textureR2 = new TextureRegion(L2R2,0,0,150,100);
		this.textureL2 = new TextureRegion(L2R2,0,100,150,100);
		this.textureWindow = new Texture(Gdx.files.internal("news-window.png"));
		this.siz = new Vector2(this.textureWindow.getWidth(),this.textureWindow.getHeight());
		this.pos = new Vector2(SlimeBall.W/2, SlimeBall.H-texture.getHeight()-90);
		this.font = new BitmapFont(Gdx.files.internal("cookie.fnt"),Gdx.files.internal("cookie_0.png"),false);
		
		this.pseudo = new TextWrapper("God",this.pos.x+95,this.pos.y+60);
		this.date = new TextWrapper("01/01/01",this.pos.x+this.siz.x-120,this.pos.y+60,1);
		this.message = new TextWrapper("No connexion !",this.pos.x+95,this.pos.y+this.texture.getHeight()-150,500f,320f);
		
		this.nextpseudo = new TextWrapper("God",this.pos.x+95,this.pos.y+60);
		this.nextdate = new TextWrapper("01/01/01",this.pos.x+this.siz.x-120,this.pos.y+60,1);
		this.nextmessage = new TextWrapper("No connexion !",this.pos.x+95,this.pos.y+this.texture.getHeight()-150,500f,320f);
		if(SlimeBall.online)	readNews();
		else lastL2 = true;
	}
	public void readNews() throws IOException{
		String data = "";
		try{
			URL url = new URL("http://leoche.org/assets/ouya/slimeball/news");
			InputStream is = url.openStream();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			int n;
			while((n = is.read(buf)) >= 0)
				os.write(buf, 0, n);
			os.close();
			is.close();
			data = os.toString();
		}
		catch (IOException e) {System.out.println(e);}
		if(data.equals("")){
			lastL2 = true;
			return;
		}
		String[] lines = data.split("\\n");
		numBoard = lines.length;
		boards = new String[numBoard][3];
		for(int i = 0;i<lines.length;i++){
			String[] elements = lines[i].split("#");
			boards[i][0] = elements[0];
			boards[i][1] = elements[1];
			boards[i][2] = elements[2];
			if(i==0){
				currentBoard = boards[i];
				this.date.setText(currentBoard[0]);
				this.pseudo.setText(currentBoard[1]);
				this.message.setText(currentBoard[2]);
			}
		}
	}
	public void changeBoard(int sens){
		if(numBoard == 1) return;
		lastR2 = lastL2 = false;
		if(sens == 1){
			indexBoard++; 
			if(indexBoard+1>=numBoard) lastL2 = true;
			if(indexBoard>=numBoard){
				indexBoard=numBoard-1;
				return;
			}
			slide.play(.8f);
			onAnim =true;
			animSens = sens*1500;
			this.nextpseudo.addX(-this.texture.getWidth());
			this.nextdate.addX(-this.texture.getWidth());
			this.nextmessage.addX(-this.texture.getWidth());
		}else if(sens == -1){
			indexBoard--; 
			if(indexBoard<=0) lastR2 = true;
			if(indexBoard<0){
				indexBoard=0;
				return;
			}
			slide.play(.8f);
			onAnim =true;
			animSens = sens*1500;
			this.nextpseudo.addX(this.texture.getWidth());
			this.nextdate.addX(this.texture.getWidth());
			this.nextmessage.addX(this.texture.getWidth());
		}
		nextBoard = boards[indexBoard];
		this.nextdate.setText(nextBoard[0]);
		this.nextpseudo.setText(nextBoard[1]);
		this.nextmessage.setText(nextBoard[2]);
	}
	public void handleInput(){
		if(onAnim) return;
		for (int i = 0; i < Controllers.getControllers().size; i++) {
			Controller c = Controllers.getControllers().get(i);
			if(c.getButton(Ouya.BUTTON_R1)){
				if(R2[i]!=true){
					R2[i]=true;
					changeBoard(-1);
				}
			}else R2[i]=false;
			if(c.getButton(Ouya.BUTTON_L1)){
				if(L2[i]!=true){
					L2[i]=true;
					changeBoard(1);
				}
			}else L2[i]=false;
		}
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha){
		spriteBatch.end();
		spriteBatch.begin();
		spriteBatch.draw(this.texture, this.pos.x+73, this.pos.y+23);
		if(!lastL2) spriteBatch.draw(this.textureL2, this.pos.x+73, this.pos.y+23+this.texture.getHeight()-101);
		if(!lastR2) spriteBatch.draw(this.textureR2, this.pos.x+73+this.texture.getWidth()-150, this.pos.y+23+this.texture.getHeight()-101);
		if(onAnim){
		this.font.setColor(.8f, .8f, .9f, .6f);
		this.font.setScale(0.4f);
		this.nextpseudo.draw(spriteBatch,this.font,currentX);
		this.font.setColor(.8f, .8f, .8f, .3f);
		this.nextdate.draw(spriteBatch,this.font,currentX);
		this.font.setScale(.6f);
		this.font.setColor(.8f, .8f, .8f, .8f);
		this.nextmessage.draw(spriteBatch,this.font,currentX);
		}
		this.font.setColor(.8f, .8f, .9f, .6f);
		this.font.setScale(0.4f);
		this.pseudo.draw(spriteBatch,this.font,currentX);
		this.font.setColor(.8f, .8f, .8f, .3f);
		this.date.draw(spriteBatch,this.font,currentX);
		this.font.setScale(.6f);
		this.font.setColor(.8f, .8f, .8f, .8f);
		this.message.draw(spriteBatch,this.font,currentX);
		spriteBatch.draw(this.textureWindow, this.pos.x, this.pos.y);
	}
	@Override 
	public void act(float delta){
		handleInput();
		if(onAnim){
			currentX += animSens*delta;
			if((currentX>=this.texture.getWidth() && animSens>0) || (currentX<=-this.texture.getWidth() && animSens<0)){
				if(animSens>0) currentX=this.texture.getWidth();
				if(animSens<0) currentX=-this.texture.getWidth();
				onAnim=false;
				animSens=0;
				this.pseudo.setText(this.nextpseudo.text);
				this.message.setText(this.nextmessage.text);
				this.date.setText(this.nextdate.text);
				this.nextpseudo.setX(this.pseudo.pos.x);
				this.nextdate.setX(this.date.pos.x);
				this.nextmessage.setX(this.message.pos.x);
				currentX=0;
			}
		}
	}
	public void dispose(){
		slide.dispose();
		pseudo.remove();
		message.remove();
		date.remove();
		nextpseudo.remove();
		nextdate.remove();
		nextmessage.remove();
		font.dispose();
		texture.dispose();
		textureWindow.dispose();
	}
}
