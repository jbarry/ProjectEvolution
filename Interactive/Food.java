package Interactive;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import Frame.*;
import Evolution.*;
@SuppressWarnings("all")
public class Food extends Matter{
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	public static final int width = 5;
	public static final int height = 5;

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
	
	public Food(double aMxHlth, int anId, int aScanRange, char type) {
		super(aMxHlth, anId, aScanRange, type);
	}

	public Food(int x, int y){
		location = new Coordinate(x,y);
		hlth = 100.0;
	}

	public Food(Coordinate aCoord){
		location = aCoord;
		hlth = 100.0;
	}

	public void paint(Graphics g, boolean isDepleted) {
		g.setColor(Color.BLUE);
		if(!isDepleted){
			g.fillRect((int)this.location.getX()-(width/2), 
					   (int)this.location.getY()-(height/2), 
					   width, height);
		}
	}

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
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}