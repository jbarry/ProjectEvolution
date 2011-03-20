package Interactive;

import java.util.Random;

import Frame.Coordinate;
import Frame.GridPanel;

public abstract class Matter {
	protected Coordinate location;
	protected double hlth;
	protected double mxHlth;
	protected int id;
	protected int scnRng;
	protected Random r;
	
	public static int width = 5;
	public static int height = 5;
	
	public Matter() {
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
		setRange(width, height, 'f');
	}
	
	public Matter(double aMxHlth) {
		hlth = mxHlth = aMxHlth;
		scnRng = 10;
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
		setRange(width, height, 'f');
	}
	
	public Matter(double aMxHlth, int anId) {
		hlth = mxHlth = aMxHlth;
		id = anId;
		scnRng = 10;
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
		setRange(width, height, 'f');
	}
	
	public Matter(double aMxHlth, int anId, int aScnRng) {
		hlth = mxHlth = aMxHlth;
		id = anId;
		scnRng = aScnRng;
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
		setRange(width, height, 'f');
	}
	
	public void deplete(double val) {
		if (hlth - val < 0) hlth = 0;
		else hlth-=val;
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
	
	/**
	 * @param x - current x location if valid.
	 * @param y - current y location if valid.
	 * @return true if organism can spawn at given location.
	 */
	protected boolean canSpawn(int x, int y){
		for(int i=x-width/2; i<=x+width/2; i++){
			for(int j=y-height/2; j<=y+height/2; j++){
				try{
					if(GridPanel.locationMap[i][j].snd != 'w'){
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
	public void setRange(int x, int y, Character value){
		//check to see if a valid entry was given.
//		if(value != 'o' && value != 'w'){
//			value = 'w';
//		}
//		else{
			for(int i=(location.getX()-(x/2)); i<=(location.getX()+(x/2)); i++){
				for(int j=(location.getY()-(y/2)); j<=(location.getY()+(y/2)); j++){
					try{
						GridPanel.locationMap[i][j].fst = id;
						GridPanel.locationMap[i][j].snd = value;
					}
					catch(ArrayIndexOutOfBoundsException e){
						
					}
				}
			}
//		}
	}
	
	/**
	 * Handles objects that stray off of the GridPanel and wraps their location.
	 * @param rightLeftBound   - right and left boundary to trigger wrap
	 * @param topBottomBound   - top and bottom boundary to trigger wrap
	 */
	protected void setWrapAround(int rightLeftBound, int topBottomBound){
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

}
