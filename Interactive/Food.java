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
		//set location
		r = new Random();
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);

		//check for collisions
		while(!canSpawn(x, y)){
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		location = new Coordinate(x, y);

		//set boundaries
		setWrapAround(width, height);
		setRange(width, height, false);
	}
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public Food(double aMxHlth, int anId) {
		super(aMxHlth, anId);
		//set location
		r = new Random();
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);

		//check for collisions
		while(!canSpawn(x, y)) {
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		location = new Coordinate(x, y);

		//set boundaries
		setWrapAround(width, height);
		setRange(width, height, false);
	}

	public Food(int x, int y){
		this.r = new Random();
		this.location=new Coordinate(x,y);
		this.health=100.0;
	}

	public Food(Coordinate aCoord){
		this.r = new Random();
		this.location=aCoord;
		this.health=100.0;
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
		return health;
	}

	public Coordinate getLocation(){
		return location;
	}

	public int getId() {
		return id;
	}
	//------------------------------------------------------------------------------------
	//--accessors/mutators--
	//------------------------------------------------------------------------------------
	public void deplete(){
		if(health > 0){
			health--;
//			System.out.println("FdHlth: " + health);
//			System.out.println();
		}
	}
	
        /**
	 * @param scanRange
	 * @return number of surrounding objects, namely Food or Organism Instances
	 */
	@Override
	public double numSurroundingObjects(int scanRange){
		double numObj = 0.0;
		for(int i=location.getX()-width/2-scanRange; i<=location.getX()+width/2+scanRange; i++){
			for(int j=location.getY()-height/2-scanRange; j<=location.getY()+height/2+scanRange; j++){
				try{	
					//count all occurrences of 'false' in location map
					if(!GridPanel.isValidLocation[i][j]){
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

	/**
	 * @param x - current x location if valid.
	 * @param y - current y location if valid.
	 * @return true if food can spawn at given location.
	 */
	private boolean canSpawn(int x, int y){
		for(int i=x-width/2; i<=x+width/2; i++){
			for(int j=y-height/2; j<=y+height/2; j++){
				try{
					if(!GridPanel.isValidLocation[i][j]){
						return false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					
				}
			}
		}
		return true;
	}
	
	/**
	 * This method will modify the boolean location map and account for wrapping.
	 * 
	 * @param x        x-size for rectangle
	 * @param y        y-size for rectangle
	 * @param validity the value to mark the location map.
	 */
	private void setRange(int x, int y, boolean validity){
		for(int i=(location.getX()-(x/2)); i<=(location.getX()+(x/2)); i++){
			for(int j=(location.getY()-(y/2)); j<=(location.getY()+(y/2)); j++){
				try{
					GridPanel.isValidLocation[i][j] = validity;
				}
				catch(ArrayIndexOutOfBoundsException e){
					
				}
			}
		}
	}
	
	/**
	 * Handles objects that stray off of the GridPanel and wraps their location.
	 * @param rightLeftBound   - right and left boundary to trigger wrap
	 * @param topBottomBound   - top and bottom boundary to trigger wrap
	 */
	private void setWrapAround(int rightLeftBound, int topBottomBound){
		if(location.getX() + (rightLeftBound/2) >= GridPanel.WIDTH){
			//right
			if(canSpawn(width/2+1, location.getY()))
				location.setX((width/2)+1);
		}
		if(location.getX() - (rightLeftBound/2) <= 0){
			//left
			if(canSpawn(GridPanel.WIDTH - (width/2), location.getY()))
				location.setX(GridPanel.WIDTH - (width/2));
		}
		if(location.getY() + (topBottomBound/2) >= GridPanel.HEIGHT){
			//bottom
			if(canSpawn(location.getX(), height/2 + 1))
				location.setY(height/2 + 1);
		}
		if(location.getY() - (topBottomBound/2) <= 0){
			//top
			if(canSpawn(location.getX(), GridPanel.HEIGHT - (height/2)))
				location.setY(GridPanel.HEIGHT - (height/2));
		}
	}
	
	public void paint(Graphics g, boolean isDepleted)
	{
		g.setColor(Color.BLUE);
		if(!isDepleted){
			g.fillRect((int)this.location.getX()-(width/2), 
					   (int)this.location.getY()-(height/2), 
					   width, height);
		}
	}

	/**
	 * @return a String representation of the Object.
	 */
	public String toString(){
		String str = "";
		str += "I am foooooood. Eat me."
			+  "\nLocation: " + location;
		return str;
	}
}