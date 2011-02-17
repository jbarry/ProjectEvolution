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
		try{
			while(!GridPanel.isValidLocation[x+width/2][y+height/2] 
			   || !GridPanel.isValidLocation[x+width/2][y-height/2]
			   || !GridPanel.isValidLocation[x-width/2][y+height/2]
			   || !GridPanel.isValidLocation[x-width/2][y-height/2]
			   || !GridPanel.isValidLocation[x][y-height/2]
			   || !GridPanel.isValidLocation[x][y+height/2]
			   || !GridPanel.isValidLocation[x+width/2][y]
			   || !GridPanel.isValidLocation[x-width/2][y]
			){
				x = r.nextInt(GridPanel.WIDTH);
			    y = r.nextInt(GridPanel.HEIGHT);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
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
	 * This method will modify the boolean location map and account for wrapping.
	 * 
	 * @param x        x-size for rectangle
	 * @param y        y-size for rectangle
	 * @param validity the value to mark the location map.
	 */
	public void setRange(int x, int y, boolean validity){
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
	public void setWrapAround(int rightLeftBound, int topBottomBound){
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