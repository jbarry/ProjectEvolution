package Interactive;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Frame.*;
import Evolution.*;
@SuppressWarnings("all")
public abstract class Food extends Matter{
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
	
	public Food(double aMxHlth, int anId, char type) {
		super(aMxHlth, anId, type);
	}

	public Food(int x, int y) {
		location = new Coordinate(x,y);
		hlth = 100.0;
	}

	public Food(Coordinate aCoord){
		location = aCoord;
		hlth = 100.0;
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
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int compareTo(Matter fd) {
		// TODO: Generate code.
		/*Food aFd = (Food) fd;
		if (fitness < aFd.getFitness())
			return -1;
		else if (fitness == aFd.getFitness())
			return 0;
		return 1;*/
		return 0;
	}

	public abstract Double getType();
	public abstract void paint(Graphics g, boolean isDepleted);
}