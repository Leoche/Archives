package org.leoche.slimeball;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ObjectMap;

 public class SlimeBall extends Game {

	//ouya graphical output
	public static int W;
	public static int H;
	public static float floor;
	public static boolean debug = false;
	public static boolean online = false;
	public MainActivity main;
	public long startSecond;
	public String mac;
	public String uuid = "null";
	
	public SlimeBall(MainActivity main,boolean online, String mac){
		startSecond = System.currentTimeMillis();
		this.main = main;
		this.mac = mac;
		SlimeBall.online = online;
	}
	public void setUuid(String u){
		this.uuid = u;
		System.out.println(u);
	}
	@Override
    public void create () {
        W = Gdx.graphics.getWidth();
        H = Gdx.graphics.getHeight();
        //setScreen(new SplashScreen(this));
        initSplash();
    }
	
	public void initSplash(){
		setScreen(new SplashScreen(this));
	}
	
	public void initMenu(){
		this.setScreen(new MenuScreen(this));
	}
	
	public void initGame(ObjectMap<Integer, Integer> colorPlayer){
		setScreen(new GameScreen(this, colorPlayer));
	}
	
	public void quit(){
		main.finish();
	}

    //Note this is where you do your updating and drawing. Use Gdx.graphics.getDeltaTime(); for your timestamp
	@Override
	public void render () {
    	super.render();
    }
	@Override
    public void resize (int width, int height) {
    }
	
	@Override
    public void pause () {
		long endSecond = System.currentTimeMillis();
    	int duration = (int) (endSecond-startSecond)/1000;
		try{
			URL url = new URL("http://leoche.org/assets/ouya/slimeball/addStat.php?mac="+mac+"&duration="+duration+"&uuid="+uuid);
			url.openStream();
		}catch (IOException e) {System.out.println(e);}
    }
	
	@Override
    public void resume () {
    }
	
	@Override
    public void dispose () {
    }

}