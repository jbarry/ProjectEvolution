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
	
	
	public double numSurroundingObjects(int scanRange) {
		double numObj = 0.0;
		for(int i=location.getX()-width/2-scanRange; i<=location.getX()+width/2+scanRange; i++){
			for(int j=location.getY()-height/2-scanRange; j<=location.getY()+height/2+scanRange; j++){
				try{	
					//count all occurrences of objects in location map
					if(GridPanel.locationMap[i][j].snd == 'f' ||
							GridPanel.locationMap[i][j].snd == 'h' ||
							GridPanel.locationMap[i][j].snd == 'o') {
						numObj++;
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
				}
			}
		}
		//make sure that scanning object was not included in scan.
		if(numObj >= width*height){
			numObj -= width*height; 
		}
		//return a normalized value. Will count "partially" discovered organisms
		//as a whole number, does not include "wrapped" scan.
		return Math.ceil(numObj/(width*height));
	}
	
	public ArrayList<Integer> getSurroundingObjects(char type, int scanRange) {
		Set<Integer> objectIds = new HashSet<Integer>();
		for(int i=location.getX()-width/2-scanRange; i<=location.getX()+width/2+scanRange; i++){
			for(int j=location.getY()-height/2-scanRange; j<=location.getY()+height/2+scanRange; j++){
				try{	
					//count all occurrences of objects in location map
					if(GridPanel.locationMap[i][j].snd == type){
						objectIds.add(GridPanel.locationMap[i][j].fst);
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
				}
			}
		}
		//make sure that scanning object was not included in scan.
		//TODO: will do this outside of class. In GridPanel probably.
		//		if(numObj >= width*height){
		//			numObj -= width*height; 
		//		}
		return new ArrayList<Integer>(objectIds);
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