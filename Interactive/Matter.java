package Interactive;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Frame.Coordinate;
import Frame.GridPanel;

public abstract class Matter{

	protected Coordinate location;
	protected double hlth;
	protected double mxHlth;
	protected int id;
	protected Random r;
	protected double fitness;
	
	public Matter() {
		r = new Random();
	}

	public Matter(double aMxHlth, char type) {
		hlth = mxHlth = aMxHlth;
		r = new Random();
		place(type);
	}

	public Matter(double aMxHlth, int anId, char type) {
		hlth = mxHlth = aMxHlth;
		id = anId;
		r = new Random();
		place(type);
	}

	public void deplete(double val) {
		if (hlth - val < 0) hlth = 0;
		else hlth-=val;
		if(hlth==0){
			setRange(this.getWidth(), this.getHeight(), 'w');
		}
	}
	
	/**
	 * @param scanRange
	 * @return number of surrounding objects, namely Food or Organism Instances
	 */
	public double numSurroundingObjects(int scanRange) {
		double numObj = 0.0;
		for(int i=location.getX()-getWidth()/2-scanRange; i<=location.getX()+getWidth()/2+scanRange; i++){
			for(int j=location.getY()-getHeight()/2-scanRange; j<=location.getY()+getHeight()/2+scanRange; j++){
				try{	
					//count all occurrences of objects in location map
					if(GridPanel.locationMap[i][j].getSnd() == 'p' ||
							GridPanel.locationMap[i][j].getSnd() == 'h' ||
							GridPanel.locationMap[i][j].getSnd() == 'o') {
						numObj++;
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
				}
			}
		}
		//make sure that scanning object was not included in scan.
		if(numObj >= getWidth()*getHeight())
			numObj -= getWidth()*getHeight(); 
		//return a normalized value. Will count "partially" discovered organisms
		//as a whole number, does not include "wrapped" scan.
		return Math.ceil(numObj/(getWidth()*getHeight()));
	}

	/**
	 * @param scanRange
	 * @param type
	 * @return a list of id numbers of the surrounding objects of choice.
	 */
	public ArrayList<Integer> getSurroundingObjects(char type, int scanRange) {
		Set<Integer> objectIds = new HashSet<Integer>();
		for(int i=location.getX()-getWidth()/2-scanRange; i<=location.getX()+getWidth()/2+scanRange; i++){
			for(int j=location.getY()-getHeight()/2-scanRange; j<=location.getY()+getHeight()/2+scanRange; j++){
				try{	
					//count all occurrences of objects in location map
					if(GridPanel.locationMap[i][j].getSnd() == type){
						objectIds.add(GridPanel.locationMap[i][j].getFst());
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
				}
			}
		}
		return new ArrayList<Integer>(objectIds);
	}
	
	private void place(char type) {
		//set location
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);

		//check for collisions
		while(!canSpawn(x, y)) {
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		location = new Coordinate(x, y);

		//set boundaries
		setWrapAround(getWidth(), getHeight());
		setRange(getWidth(), getHeight(), type);
	}

	/**
	 * @param x - current x location if valid.
	 * @param y - current y location if valid.
	 * @return true if organism can spawn at given location.
	 */
	protected boolean canSpawn(int x, int y){
		for(int i = x-getWidth()/2; i <= x+getWidth()/2; i++){
			for(int j=y-getHeight()/2; j<=y+getHeight()/2; j++){
				try{
					if(GridPanel.locationMap[i][j].getSnd() != 'w'){
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
		for(int i=(location.getX()-(x/2)); i<=(location.getX()+(x/2)); i++){
			for(int j=(location.getY()-(y/2)); j<=(location.getY()+(y/2)); j++){
				try{
					GridPanel.locationMap[i][j].setLeft(id);
					GridPanel.locationMap[i][j].setRight(value);
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
	protected void setWrapAround(int rightLeftBound, int topBottomBound){
		if(location.getX() + (rightLeftBound/2) >= GridPanel.WIDTH){
			//right
			if(canSpawn(getWidth()/2+1, location.getY()))
				location.setX((getWidth()/2)+1);
		}
		if(location.getX() - (rightLeftBound/2) <= 0){
			//left
			if(canSpawn(GridPanel.WIDTH - (getWidth()/2), location.getY()))
				location.setX(GridPanel.WIDTH - (getWidth()/2));
		}
		if(location.getY() + (topBottomBound/2) >= GridPanel.HEIGHT){
			//bottom
			if(canSpawn(location.getX(), getHeight()/2 + 1))
				location.setY(getHeight()/2 + 1);
		}
		if(location.getY() - (topBottomBound/2) <= 0){
			//top
			if(canSpawn(location.getX(), GridPanel.HEIGHT - (getHeight()/2)))
				location.setY(GridPanel.HEIGHT - (getHeight()/2));
		}
	}
	
	protected abstract int getHeight();	
	
	protected abstract int getWidth();

	public void setMxHlth(double aMxHlth) {
		mxHlth = aMxHlth;
	}

	public void setHealth(double aHealth) {
		hlth = aHealth;
	}
	public double getFitness() {
		return fitness;
	}

	public void setFitness(double aFit) {
		fitness = aFit;
	}

	public void incHlth(double val) {
		if(hlth + val > mxHlth)
			hlth = mxHlth;
		else hlth+=val;
	}

	public double getHealth() {
		return hlth;
	}

	public double getMaxHealth() {
		return mxHlth;
	}

	public Coordinate getLocation() {
		return location;
	}

	public int getId() {
		return id;
	}
}
