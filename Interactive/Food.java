package Interactive;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import Frame.*;
import Evolution.*;
@SuppressWarnings("all")
public abstract class Food extends Matter{
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	public static final int WIDTH = 5;
	public static final int HEIGHT = 5;
	protected Image icon;

	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
//	public Food() {
//		super(100.0);
//	}
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
//	public Food(double aMxHlth, int anId) {
//		super(aMxHlth, anId);
//	}
	
	public Food(double aMxHlth, int anId, char type) {
		super(aMxHlth, anId, type);
	}

	public Food(int x, int y) {
		location = new Coordinate(x,y);
		hlth = 100.0;
	}

	//testing.
	public Food(Coordinate coord, boolean w) {
		location = coord;
		hlth = 100.0;
	}
	
	public Food(Coordinate aCoord){
		location = aCoord;
		hlth = 100.0;
	}
	
	public abstract void eaten(double val);
	
	/**
	 * @return a String representation of the Object.v
	 */
	public String toString(){
		String str = "";
		str += "I am foooooood. Eat me."
			+  "\nLocation: " + location;
		return str;
	}
	
	//------------------------------------------------------------------------------------
	//--getters/setters--
	//------------------------------------------------------------------------------------
	@Override
	public int getHeight() {
		return Food.HEIGHT;
	}
	
	@Override
	public int getWidth() {
		return Food.WIDTH;
	}
	
	public void paint(Graphics g, boolean isDepleted)
	{
		if(!isDepleted){
			g.drawImage(icon, getLocation().getX()-2*getWidth()/2, getLocation().getY()-2*getHeight()/2, 2*getWidth(), 2*getHeight(), null);
		}
	}
	
//	public void paint(Graphics g, boolean isDepleted) {
//		g.setColor(Color.BLUE);
//		if(!isDepleted){
//			g.fillRect((int)this.location.getX()-(WIDTH/2), 
//					   (int)this.location.getY()-(HEIGHT/2), 
//					   WIDTH, HEIGHT);
//		}
//	}
}