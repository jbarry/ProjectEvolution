package Interactive;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Frame.*;
import Evolution.*;
@SuppressWarnings("all")
public class Food extends Matter{
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	public static final int width = 5;
	public static final int height = 5;
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

	public Food(Coordinate aCoord){
		location = aCoord;
		hlth = 100.0;
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
	
	public void paint(Graphics g, boolean isDepleted)
	{
		if(!isDepleted){
			g.drawImage(icon, getLocation().getX()-2*getWidth()/2, getLocation().getY()-2*getHeight()/2, 2*getWidth(), 2*getHeight(), null);
		}
	}
}
