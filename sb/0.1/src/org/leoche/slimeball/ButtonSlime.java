package org.leoche.slimeball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class ButtonSlime extends Object {
	public float color;
	public ButtonSlime(float color, float x, float y){
		this.color = color;
		this.pos = new Vector2(x,y);
		this.siz = new Vector2(152,152);
		this.texture = new Texture(Gdx.files.internal("players.png"));
	}
	public void draw(SpriteBatch spriteBatch,BitmapFont fnt, float x, float y){
		spriteBatch.end();
		spriteBatch.begin();
		spriteBatch.draw(this.texture,x+this.pos.x,y+this.pos.y,(int) (this.siz.x*this.color),(int) (this.active*this.siz.y),(int) this.siz.x,(int) this.siz.y);
	}
}
