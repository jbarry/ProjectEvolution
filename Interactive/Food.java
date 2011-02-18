package Interactive;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import Frame.*;
import Evolution.*;
@SuppressWarnings("all")
public class Food {
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	public static final int width = 5;
	public static final int height = 5;
	
	private Coordinate location;
	private double foodRemaining;
	private Random r;

	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public Food(){
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
		
		foodRemaining=100.0;
	}

	public Food(int x, int y){
		this.r = new Random();
		this.location=new Coordinate(x,y);
		this.foodRemaining=100.0;
	}

	public Food(Coordinate aCoord){
		this.r = new Random();
		this.location=aCoord;
		this.foodRemaining=100.0;
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
		return foodRemaining;
	}

	public Coordinate getLocation(){
		return location;
	}

	//------------------------------------------------------------------------------------
	//--accessors/mutators--
	//------------------------------------------------------------------------------------
	public void deplete(){
		if(foodRemaining>0){
			foodRemaining--;
		}
	}
	
	/**
	 * @param x - current x location if valid.
	 * @param y - current y location if valid.
	 * @return true if food can spawn at given location.
	 */
	private boolean canSpawn(int x, int y){
		for(int i=x-width/2; i<=x+width/2; i++){
			//adjust coordinates for wrapping
			for(int j=y-height/2; j<=y+height/2; j++){
				//no conflicts
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
		for(int i=(getLocation().getX()-(x/2)); i<=(getLocation().getX()+(x/2)); i++){
			//adjust coordinates for wrapping
			for(int j=(getLocation().getY()-(y/2)); j<=(getLocation().getY()+(y/2)); j++){
				//no conflicts
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
		if(getLocation().getX() + (rightLeftBound/2) >= GridPanel.WIDTH){
			//right
			location.setX((width/2)+1);
		}
		if(getLocation().getX() - (rightLeftBound/2) <= 0){
			//left
			location.setX(GridPanel.WIDTH - (width/2));
		}
		if(getLocation().getY() + (topBottomBound/2) >= GridPanel.HEIGHT){
			//bottom
			location.setY(height/2 + 1);
		}
		if(getLocation().getY() - (topBottomBound/2) <= 0){
			//top
			location.setY(GridPanel.HEIGHT - (height/2));
		}
	}
	
	public void paint(Graphics g, boolean isDepleted)
	{
		g.setColor(Color.BLUE);
		if(!isDepleted){
			g.fillRect((int)this.getLocation().getX()-(width/2), 
					   (int)this.getLocation().getY()-(height/2), 
					   width, height);
		}
	}

	/**
	 * @return a String representation of the Object.
	 */
	public String toString(){
		String str = "";
		str += "I am foooooood. Eat me."
			+  "\nLocation: " + getLocation();
		return str;
	}

}