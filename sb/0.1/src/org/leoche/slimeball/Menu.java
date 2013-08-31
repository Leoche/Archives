package org.leoche.slimeball;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

public class Menu extends Object{

	public String type = "title";
	private ArrayList<Object> buttons = new ArrayList<Object>();
	private BitmapFont font;
	private int index = 0;
	private boolean[] left_pad_active = {false,false,false,false};
	private SlimeBall game;
	private Screen parentScreen;
	public Controller[] c = new Controller[2];
	private boolean onQuit = false;
	private Texture textureLogo;
	private Texture textureSelectSlime;
	private Texture textureButtons;
	public PlayerIcon[] playerIcons = new PlayerIcon[2];
	private int state = 0;
	private float statey = 0;
	public ObjectMap<Integer, Integer> colorPlayer = new ObjectMap<Integer, Integer>();
	public ObjectMap<Integer, Integer> cId = new ObjectMap<Integer, Integer>();
	public Sound select_sound;
	private boolean onExit = false;
	public Sound valid_sound;
	
	public Menu(Screen screen, SlimeBall game, String type){
		parentScreen = screen;
		Controllers.addListener(controls);
		initControllers();
		this.game = game;
		this.type = type;
		this.texture = new Texture(Gdx.files.internal("title.png"));
		this.textureLogo = new Texture(Gdx.files.internal("logo.png"));
		this.font = new BitmapFont(Gdx.files.internal("cookie.fnt"),Gdx.files.internal("cookie_0.png"),false);
		this.textureSelectSlime = new Texture(Gdx.files.internal("selectslime.png"));
		this.textureButtons = new Texture(Gdx.files.internal("buttons.png"));
		this.select_sound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));
		this.valid_sound = Gdx.audio.newSound(Gdx.files.internal("sounds/valid.wav"));
		
		if(this.type.equals("title")){
			buttons.add(new TextWrapper("Play", 100, (float) (740-(0*120))));
			buttons.add(new TextWrapper("Quit", 100, (float) (740-(1*120))));
			buttons.add(new TextWrapper("", 100, (float) (740-(1*120))));
			
			playerIcons[0] = new PlayerIcon(game,1,765,742);
			playerIcons[1] = new PlayerIcon(game,1,825,742);
			buttons.add(playerIcons[0]);
			buttons.add(playerIcons[1]);

			float[] group1 = {100,(-SlimeBall.H+680)};
			
			buttons.add(new ButtonSlime(0, group1[0],     group1[1]));
			buttons.add(new ButtonSlime(1, group1[0]+160, group1[1]));
			buttons.add(new ButtonSlime(2, group1[0],     group1[1]-160));
			buttons.add(new ButtonSlime(3, group1[0]+160, group1[1]-160));
			
			buttons.add(new ButtonSlime(0, 400+group1[0],     group1[1]));
			buttons.add(new ButtonSlime(1, 400+group1[0]+160, group1[1]));
			buttons.add(new ButtonSlime(2, 400+group1[0],     group1[1]-160));
			buttons.add(new ButtonSlime(3, 400+group1[0]+160, group1[1]-160));

			buttons.add(new TextWrapper("Continue", 500, (float) this.pos.y+335-SlimeBall.H, new Color(1f,1f,1f,.8f),.7f));
			
		}
		else if(this.type.equals("pause")){
			this.pos.x = -SlimeBall.W/2;
			buttons.add(new TextWrapper("Resume", 100, (float) (740-(0*120))));
			buttons.add(new TextWrapper("Back to Menu", 100, (float) (740-(1*120))));
			buttons.add(new TextWrapper("Quit", 100, (float) (740-(2*120))));
		}
	}
	@Override
	public void act(float delta){
		handleInput();
		slideMenu(delta);
		if(!onQuit){
			if(this.pos.x<0) this.pos.x+=2000*delta;
			if(this.pos.x>0) this.pos.x=0;
		}else{
			if(this.pos.x>-SlimeBall.W/2) this.pos.x-=2000*delta;
			else if(onExit){
				((GameScreen) parentScreen).dispose();
				game.initMenu();
				dispose();   
			}else{					
				for (int i = 0; i < Controllers.getControllers().size; i++) {
					Controller c2 = Controllers.getControllers().get(i);
					c2.removeListener(controls);
				}
			parentScreen.pause();
			}
		}
	}
	public void slideMenu(float delta){
		if((float) (this.state*SlimeBall.H) == this.statey) return;
		if((float) (this.state*SlimeBall.H) > this.statey){
			this.statey+=2000*delta;
		}else this.statey=(float) (this.state*SlimeBall.H);
	}
	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha){
   		spriteBatch.end();
   		spriteBatch.begin();
   		spriteBatch.draw(this.texture,this.pos.x,0);
   		spriteBatch.draw(this.textureLogo,this.pos.x,this.statey+this.pos.y+SlimeBall.H-this.textureLogo.getHeight());
   		spriteBatch.draw(this.textureSelectSlime,this.pos.x,this.statey+this.pos.y-this.textureSelectSlime.getHeight());
   		for(int i = 0;i<buttons.size();i++){
   	   		font.setColor(1f, 1f, 1f, .5f);
   	   		if(index==i) font.setColor(1f, 1f, 1f, 1f);
   	   		buttons.get(i).act();
   	   		if(i != 13){
   	   			buttons.get(i).draw(spriteBatch, font, this.pos.x, this.statey+this.pos.y);
   	   		} else if(colorPlayer.containsKey(Controllers.getControllers().get(0).hashCode()) && colorPlayer.containsKey(Controllers.getControllers().get(1).hashCode())) buttons.get(i).draw(spriteBatch, font, this.pos.x, this.statey+this.pos.y);
   		}
   		if(this.state!=0 ){
   			if(colorPlayer.containsKey(Controllers.getControllers().get(0).hashCode()) && colorPlayer.containsKey(Controllers.getControllers().get(1).hashCode())) spriteBatch.draw(this.textureButtons, this.pos.x+400, this.statey+this.pos.y+290-SlimeBall.H,360,0,90,90);
   		}
	}
	public void handleInput(){
		for (int i = 0; i < Controllers.getControllers().size; i++) {
			Controller c = Controllers.getControllers().get(i);
			if(!this.type.equals("pause")) playerIcons[i].setState(2);
			float y = c.getAxis(Ouya.AXIS_LEFT_Y);
			if(Math.abs(y)>0.50F){
				if(!left_pad_active[i]){
					left_pad_active[i]=true;
					if(y<0) navigate(-1);
					else    navigate(1);
				}
			}else{
				left_pad_active[i]=false;
			}
		}
	}
	public void initControllers(){
		for (int i = 0; i < Controllers.getControllers().size; i++) {
			Controller c = Controllers.getControllers().get(i);
			cId.put(c.hashCode(), i);
		}
	}
	public ControllerListener controls = new ControllerListener(){
		@Override
		public boolean buttonDown(Controller c, int keyCode) {
			if(keyCode == Ouya.BUTTON_DPAD_DOWN) navigate(1);
			if(keyCode == Ouya.BUTTON_DPAD_UP) navigate(-1);
			if(keyCode == Ouya.BUTTON_O){
				System.out.println("Button down @ "+this.hashCode());
				if(type.equals("title")){
					if(statey == 0){
						switch(index){
						case 0:
							if(playerIcons[0].getState()!=2 || playerIcons[1].getState()!=2){
								if(playerIcons[0].getState()!=2){
									playerIcons[0].setState(0);
								}
								if(playerIcons[1].getState()!=2){
									playerIcons[1].setState(0);
								}
							}else{
								valid_sound.play(1f);
								state = 1;
							}
						break;
						case 1:
							game.quit();
						break;
						case 2:
							game.quit();
						break;
						}
					}else if(statey == SlimeBall.H){
						if(colorPlayer.containsKey(c.hashCode())) buttons.get(5+(cId.get(c.hashCode())*4)+colorPlayer.get(c.hashCode())).unsetActive();
						buttons.get(5+(cId.get(c.hashCode())*4)).setActive();
						colorPlayer.put(c.hashCode(),0);
						select_sound.play(1f);
					}
				}
				else if(type.equals("pause")){
					switch(index){
					case 0:
						valid_sound.play(1f);
						onQuit=true;
					break;
					case 1:
						valid_sound.play(1f);
						onExit = true;
						onQuit= true;
					break;
					case 2:
						game.quit();
					break;
					}
				}
			}
			if(keyCode == Ouya.BUTTON_A){
				if(type.equals("title")){
					if(statey == SlimeBall.H){
						if(colorPlayer.containsKey(c.hashCode())) buttons.get(5+(cId.get(c.hashCode())*4)+colorPlayer.get(c.hashCode())).unsetActive();
						buttons.get(6+(cId.get(c.hashCode())*4)).setActive();
						colorPlayer.put(c.hashCode(),1);
						select_sound.play(1f);
					}
				} else if(type.equals("pause") && pos.x> -siz.x/2){onQuit=true;}
			}
			if(keyCode == Ouya.BUTTON_U){
				if(type.equals("title")){
					if(statey == SlimeBall.H){
						System.out.println("ok");
						if(colorPlayer.containsKey(c.hashCode())) buttons.get(5+(cId.get(c.hashCode())*4)+colorPlayer.get(c.hashCode())).unsetActive();
						buttons.get(7+(cId.get(c.hashCode())*4)).setActive();
						colorPlayer.put(c.hashCode(), 2);
						select_sound.play(1f);
					}
				}
			}
			if(keyCode == Ouya.BUTTON_Y){
				if(type.equals("title")){
					if(statey == SlimeBall.H){
						if(colorPlayer.containsKey(c.hashCode())) buttons.get(5+(cId.get(c.hashCode())*4)+colorPlayer.get(c.hashCode())).unsetActive();
						buttons.get(8+(cId.get(c.hashCode())*4)).setActive();
						colorPlayer.put(c.hashCode(), 3);
						select_sound.play(1f);
					}
				}
			}
			if(keyCode == Ouya.BUTTON_MENU){
				if(type.equals("title")){
					if(statey == SlimeBall.H && colorPlayer.containsKey(Controllers.getControllers().get(0).hashCode()) && colorPlayer.containsKey(Controllers.getControllers().get(1).hashCode())){
						valid_sound.play(1f);
						Controllers.removeListener(controls);
						System.out.println(colorPlayer.toString());
						((MenuScreen) parentScreen).play(colorPlayer);
					}
				} else if(type.equals("pause") && pos.x> -siz.x/2){onQuit=true;}
			}
			return false;
		}
		@Override
		public boolean buttonUp(Controller c, int keyCode) {
			return false;
		}
		@Override
		public void connected(Controller c) {cId.put(c.hashCode(), cId.size);}
		@Override
		public void disconnected(Controller arg0) {}
		@Override
		public boolean povMoved(Controller arg0, int arg1,PovDirection arg2) {return false;}
		@Override
		public boolean xSliderMoved(Controller arg0, int arg1,boolean arg2) {return false;}
		@Override
		public boolean ySliderMoved(Controller arg0, int arg1,boolean arg2) {return false;}
		@Override
		public boolean accelerometerMoved(Controller arg0, int arg1,Vector3 arg2) {return false;}
		@Override
		public boolean axisMoved(Controller arg0, int arg1, float arg2) {return false;}
	};
	public void navigate(int y){
		if(this.type.equals("pause")){
			if(y==1 && index<2) index++;
			this.select_sound.play(1f);
		}
		else{
			if(y==1 && index<1) index++;
			this.select_sound.play(1f);
		}
		if(y==-1 && index>0){
			index--;
			this.select_sound.play(1f);
		}
	}
	public void dispose(){
		System.out.println("dispose controls menu");
		Controllers.removeListener(controls);
		this.texture.dispose();
		this.select_sound.dispose();
		this.valid_sound.dispose();
		this.textureLogo.dispose();
		if(this.type.equals("title")){
			this.textureSelectSlime.dispose();
			this.textureButtons.dispose();
			playerIcons[0].dispose();
			playerIcons[1].dispose();
		}
	}
}
