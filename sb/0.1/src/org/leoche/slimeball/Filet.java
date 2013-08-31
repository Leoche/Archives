package org.leoche.slimeball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Filet extends Object{
	public Slime top;

	public Filet(int x, int y){
		this.pos = new Vector2(x,y);
		this.vel = new Vector2(0,0);
		this.siz = new Vector2(35,170);
		this.texture = new Texture(Gdx.files.internal("filet.png"));
		this.texture.setFilter(TextureFilter.Linear,TextureFilter.Linear );
	}
	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha) {
		spriteBatch.end();
		spriteBatch.begin();
		if(!SlimeBall.debug)spriteBatch.draw(this.texture, this.pos.x-this.siz.x/2, this.pos.y-this.siz.y+50);
		if(SlimeBall.debug) drawBounds();
	}
}
