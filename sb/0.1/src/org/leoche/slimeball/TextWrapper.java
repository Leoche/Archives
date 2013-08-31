package org.leoche.slimeball;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TextWrapper extends Object{
  public String text;
  public Vector2 pos;
  private Vector2 siz;
  private float width;
  private float height;
  private float align = -1;
  private float scale = 1;
  private Color color = new Color(0f,0f,0f,0f);
  
  public TextWrapper(String text,float x, float y){
	  this.text = text;
      this.pos = new Vector2(x,y);
      this.siz = new Vector2(0,0);
  }
  
  public TextWrapper(String text,float x, float y, int align){
	  this.text = text;
      this.pos = new Vector2(x,y);
      this.siz = new Vector2(0,0);
      this.align = align;
  }
  
  public TextWrapper(String text,float x, float y, Color color){
	  this.text = text;
      this.pos = new Vector2(x,y);
      this.siz = new Vector2(0,0);
      this.color = color;
  }
  
  public TextWrapper(String text,float x, float y, Color color, float scale){
	  this.text = text;
      this.pos = new Vector2(x,y);
      this.siz = new Vector2(0,0);
      this.color = color;
      this.scale = scale;
  }
  
  public TextWrapper(String text,float x, float y, float w, float h){
	  this.text = text;
      this.pos = new Vector2(x,y);
      this.siz = new Vector2(w,h);
  }

  public void draw(SpriteBatch sp,BitmapFont fnt){
	  Color oldColor = fnt.getColor();
	  if(!this.color.equals(new Color(0f,0f,0f,0f))){
		  fnt.setColor(this.color);
	  }
	  float oldScale = fnt.getScaleX();
	  if(this.scale != 1f){
		  fnt.setScale(this.scale);
	  }
      height= fnt.getBounds(text).height;
      width= fnt.getBounds(text).width;
      if(this.siz.x!=0){
    	  fnt.drawWrapped(sp,text,this.pos.x,this.pos.y+height/2,this.siz.x);
      }else{
          if(align == -1)     fnt.draw(sp,text,this.pos.x,this.pos.y+height/2);
          else if(align == 0) fnt.draw(sp,text,this.pos.x-width/2,this.pos.y+height/2);
          else if(align == 1) fnt.draw(sp,text,this.pos.x-width,this.pos.y+height/2);
      }
	  if(!this.color.equals(new Color(0f,0f,0f,0f))){
		  fnt.setColor(oldColor);
	  }
	  if(this.scale != 1f){
		  fnt.setScale(oldScale);
	  }
  }

  public void draw(SpriteBatch sp,BitmapFont fnt, float x){
	  Color oldColor = fnt.getColor();
	  if(!this.color.equals(new Color(0f,0f,0f,0f))){
		  fnt.setColor(this.color);
	  }
	  float oldScale = fnt.getScaleX();
	  if(this.scale != 1f){
		  fnt.setScale(this.scale);
	  }
      height= fnt.getBounds(text).height;
      width= fnt.getBounds(text).width;
      if(this.siz.x!=0){
    	  fnt.drawWrapped(sp,text,x+this.pos.x,this.pos.y+height/2,this.siz.x);
      }else{
          if(align == -1)     fnt.draw(sp,text,x+this.pos.x,this.pos.y+height/2);
          else if(align == 0) fnt.draw(sp,text,x+this.pos.x-width/2,this.pos.y+height/2);
          else if(align == 1) fnt.draw(sp,text,x+this.pos.x-width,this.pos.y+height/2);
      }
	  if(!this.color.equals(new Color(0f,0f,0f,0f))){
		  fnt.setColor(oldColor);
	  }
	  if(this.scale != 1f){
		  fnt.setScale(oldScale);
	  }
  }

  public void draw(SpriteBatch sp,BitmapFont fnt, float x, float y){
	  Color oldColor = fnt.getColor();
	  if(!this.color.equals(new Color(0f,0f,0f,0f))){
		  fnt.setColor(this.color);
	  }
	  float oldScale = fnt.getScaleX();
	  if(this.scale != 1f){
		  fnt.setScale(this.scale);
	  }
      height= fnt.getBounds(text).height;
      width= fnt.getBounds(text).width;
      if(this.siz.x!=0){
    	  fnt.drawWrapped(sp,text,x+this.pos.x,y+this.pos.y+height/2,this.siz.x);
      }else{
          if(align == -1)     fnt.draw(sp,text,x+this.pos.x,y+this.pos.y+height/2);
          else if(align == 0) fnt.draw(sp,text,x+this.pos.x-width/2,y+this.pos.y+height/2);
          else if(align == 1) fnt.draw(sp,text,x+this.pos.x-width,y+this.pos.y+height/2);
      }
	  if(!this.color.equals(new Color(0f,0f,0f,0f))){
		  fnt.setColor(oldColor);
	  }
	  if(this.scale != 1f){
		  fnt.setScale(oldScale);
	  }
  }

  public String getText() {
      return text;
  }

  public void setText(String text) {
      this.text = text;
  }

  public Vector2 getPosition() {
      return this.pos;
  }

  public void setPosition(Vector2 position) {
      this.pos = position;
  }
  public void addX(float x) {
      this.pos.x += x;
  }
  public void setX(float x) {
      this.pos.x = x;
  }
}