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
	private Random r;

	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public Food() {
		super(100.0);
	}
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public Food(double aMxHlth, int anId) {
		super(aMxHlth, anId);
	}

	public Food(int x, int y){
		this.r = new Random();
		this.location=new Coordinate(x,y);
		this.hlth=100.0;
	}

	public Food(Coordinate aCoord){
		this.r = new Random();
		this.location=aCoord;
		this.hlth=100.0;
	}

	 /**
	 * @param scanRange
	 * @return number of surrounding objects, namely Food or Organism Instances
	 */
	public double numSurroundingObjects(int scanRange){
		double numObj = 0.0;
		for(int i=location.getX()-width/2-scanRange; i<=location.getX()+width/2+scanRange; i++){
			for(int j=location.getY()-height/2-scanRange; j<=location.getY()+height/2+scanRange; j++){
				try{	
					//count all occurrences of objects in location map
					if(GridPanel.locationMap[i][j].snd == 'f' 
						|| GridPanel.locationMap[i][j].snd == 'o'){
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
	
	public double getFoodRemaining(){
		return hlth;
	}

	public Coordinate getLocation(){
		return location;
	}

	public int getId() {
		return id;
	}
}