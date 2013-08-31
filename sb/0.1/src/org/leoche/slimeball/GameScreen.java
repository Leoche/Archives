package org.leoche.slimeball;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ObjectMap;

public class GameScreen implements Screen{
	
	private SlimeBall game;
	private Stage stage;
	private Group group;
	private Ball ball;
	private Filet filet;
	public ObjectMap<Integer, Slime> slimes = new ObjectMap<Integer, Slime>();
	public Controller[] c = new Controller[2];
	private Slime top;
	private Level level;
	private Menu menu;
	private Score score;
	public boolean winned = false;
	public String mode = "tennis";
	private float totalPoints = 10f;
	public boolean paused = false;
	public boolean[] jumpActive = {false,false};
	public boolean[] powerActive = {false,false};
	public boolean canPause = false;
	public int winner = -1;
	public ObjectMap<Integer, Integer> colorPlayer;
	
	public GameScreen(SlimeBall game, ObjectMap<Integer, Integer> colorPlayer){
		this.game = game;
		this.colorPlayer = colorPlayer;
		Texture.setEnforcePotImages(false);
		stage = new Stage();
		group = new Group();
		

		ball = new Ball(this,SlimeBall.W/2,SlimeBall.H,96,96);
		level = new Level(game,this,ball,"beach", mode);
		SlimeBall.floor = level.floor;
		
		stage.addActor(level);
		stage.addActor(group);
		initSlime();
		
		filet = new Filet(940,level.floor);
		stage.addActor(filet);
		
		stage.addActor(ball);
		top = new Slime(2, SlimeBall.W/2, filet.pos.y+filet.siz.y, 30f, 1f, 0);
		
		score = new Score(level.scoreY+30, slimes.get(c[0].hashCode()).color, slimes.get(c[1].hashCode()).color,totalPoints);
		stage.addActor(score);
		
		stage.addActor(top);

		Controllers.addListener(new ControllerAdapter() {
			@Override
			public void connected(Controller controller) {}
			public void disconnected(Controller controller) {
				quitGame();
			}
		});
		initControllers();
	}
	public void quitGame(){game.initMenu();}
	@Override
	public void render(float delta) {
		if(!paused) handleInput(delta);
		if(score.points == 0){
			slimes.get(c[0].hashCode()).isWinning = 0;
			slimes.get(c[1].hashCode()).isWinning = 0;
		}else if(score.points > totalPoints/2){
			slimes.get(c[0].hashCode()).isWinning = -1;
			slimes.get(c[1].hashCode()).isWinning = 1;
		}else if(score.points < -totalPoints/2){
			slimes.get(c[0].hashCode()).isWinning = 1;
			slimes.get(c[1].hashCode()).isWinning = -1;
		}
		resolveCollisions();
		Gdx.gl.glClearColor(.17f, .23f, .33f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if(!paused) stage.act(delta);
		else if(this.menu != null) stage.getActors().get(stage.getActors().size-1).act(delta);
		if(winner != -1) win(delta);
		stage.draw();
	}
	public void initControllers(){
		for (int i = 0; i < Controllers.getControllers().size; i++) {
			Controller c = Controllers.getControllers().get(i);
			c.addListener(controls);
		}
	}
	private ControllerListener controls = new ControllerListener(){
		@Override
		public boolean accelerometerMoved(Controller arg0, int arg1,Vector3 arg2) {return false;}
		@Override
		public boolean axisMoved(Controller arg0, int arg1, float arg2) {return false;}
		@Override
		public boolean buttonDown(Controller c, int keyCode) {
			if(paused&&!winned) return false;
			else if(winned){
				if(keyCode == Ouya.BUTTON_O){
					dispose();
					game.initGame(colorPlayer);
				}
				if(keyCode == Ouya.BUTTON_A){
					dispose();
					game.initMenu();
				}
			}
			if(keyCode == Ouya.BUTTON_O){
				if(slimes.get(c.hashCode()).onfloor){
					slimes.get(c.hashCode()).playJumpSound();
					slimes.get(c.hashCode()).vel.add(0,slimes.get(c.hashCode()).jump);
				}
			}
			if(keyCode == Ouya.BUTTON_MENU){
				if(canPause) pause();
			}
			return false;
		}
		@Override
		public boolean buttonUp(Controller c, int keyCode) {
			if(paused) return false;
			if(keyCode == Ouya.BUTTON_MENU) canPause = true;
			return false;
		}
		@Override
		public void connected(Controller arg0) {}
		@Override
		public void disconnected(Controller arg0) {}
		@Override
		public boolean povMoved(Controller arg0, int arg1,PovDirection arg2) {return false;}
		@Override
		public boolean xSliderMoved(Controller arg0, int arg1,boolean arg2) {return false;}
		@Override
		public boolean ySliderMoved(Controller arg0, int arg1,boolean arg2) {return false;}
		
	};
	public void handleInput(float delta){
		for (int i = 0; i < Controllers.getControllers().size; i++) {
			Controller c = Controllers.getControllers().get(i);
			float x = c.getAxis(Ouya.AXIS_LEFT_X);
			if(Math.abs(x)>0.25F) slimes.get(c.hashCode()).pos.add(x*slimes.get(c.hashCode()).speed,0);
		}
	}
	public void win(float delta){
		Slime s = slimes.get(this.winner);
		float x = SlimeBall.W/4 + (s.id*SlimeBall.W/2);
		float y = SlimeBall.H/2;
		if(s.pos.x<x)s.pos.x+=s.speed/2;
		if(s.pos.y<y)s.pos.y+=s.speed/2;
		if(s.pos.x>x)s.pos.x-=s.speed/2;
		if(s.pos.y>y)s.pos.y-=s.speed/2;
		if(Math.abs(s.pos.x-x)<20 && Math.abs(s.pos.y-y)<20){
			this.level.win(this.winner);
		}
	}
	public void addPoint(int player){
		score.addPoint(slimes.get(player).id);
		if(Math.abs(score.points)==totalPoints){
			score.remove();
			this.paused = true;
			this.winner = player;
		}
	}
	public void resolveCollisions(){
		if(mode.equals("tennis")){
			if(ball.vel.x>0 && ball.pos.x+ball.r>filet.pos.x && ball.pos.x+ball.r<filet.pos.x+filet.siz.x/2 && ball.pos.y<filet.pos.y+filet.siz.y){
				ball.vel.x*=-1;ball.playSound(.8f);
			}
			if(ball.vel.x<0 && ball.pos.x-ball.r>filet.pos.x+filet.siz.x/2 && ball.pos.x-ball.r<filet.pos.x+filet.siz.x && ball.pos.y<filet.pos.y+filet.siz.y){
				ball.vel.x*=-1;ball.playSound(.8f);
			}
			if(ball.vel.y<0 && ball.pos.x>filet.pos.x && ball.pos.x<filet.pos.x+filet.siz.x && ball.pos.y-ball.r<filet.pos.y+filet.siz.y){
				ball.vel.y*=-1;
			}
			resolveSlimeCollisions(ball,top);
		}
		resolveSlimeCollisions(ball,slimes.get(c[0].hashCode()));
		if(slimes.get(c[1].hashCode()) != null){
			resolveSlimeCollisions(ball,slimes.get(c[1].hashCode()));
		}
	}
	
	public float distance(Vector2 v){
		return (float) Math.sqrt(v.x * v.x + v.y * v.y);
	}
	
	public Vector2 normale(Vector2 v){
		float d = distance(v);
		return new Vector2(v.x/d,v.y/d);
	}
	public void resolveSlimeCollisions(Ball o1,Slime o2){
		Vector2 b = new Vector2(o1.pos);
		Vector2 c = new Vector2(o2.pos);
		Vector2 d = new Vector2(c.sub(b));
		float distance_squared = (float) d.dot(d);
		float radius = o1.r + o2.r;
		float radius_squared = radius * radius;
		if(distance_squared > radius_squared || o1.pos.y-o1.r<o2.pos.y) return;
		ball.playSound(.8f);
		o2.wink = true;
		o2.tick = 0;
		float distance = (float) Math.sqrt(distance_squared);
	    Vector2 ncoll = d.div(distance).scl(8,10);
	    o1.vel.sub(ncoll);
	}
	
	public void initSlime(){
		for (int i = 0; i < Controllers.getControllers().size; i++) {
			c[i] = Controllers.getControllers().get(i);
			int id = c[i].hashCode();
			slimes.put(id, new Slime(i,SlimeBall.W/4+(SlimeBall.W/2)*i,level.floor,(float) (i-1),(float) colorPlayer.get(id)));
			group.addActor(slimes.get(id));
		}
		level.addSlimes(slimes);
	}
	
	@Override
	public void resize(int x, int y) {
	}
	@Override
	public void resume() {
	}
	@Override
	public void show() {
	}
	@Override
	public void dispose() {
		for (int i = 0; i < Controllers.getControllers().size; i++) {
			Controller c2 = Controllers.getControllers().get(i);
			c2.removeListener(controls);
		}
		level.dispose();
		level.remove();
		slimes.get(c[0].hashCode()).dispose();
		slimes.get(c[1].hashCode()).dispose();
		slimes.get(c[0].hashCode()).remove();
		slimes.get(c[1].hashCode()).remove();
		ball.dispose();
		ball.remove();
		score.dispose();
		score.remove();
	}
	@Override
	public void hide() {
	}
	@Override
	public void pause() {
		if(paused){
			this.menu.dispose();
			this.menu.remove();
			this.menu = null;
			paused = false;
		}else{
			this.menu = new Menu(this, game, "pause");
			stage.addActor(menu);
			paused = true;
		}
	}
}
