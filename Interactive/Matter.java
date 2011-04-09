package Interactive;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Frame.Coordinate;
import Frame.GridPanel;
import Frame.LocationMap;

public abstract class Matter implements Comparable<Matter>{

	protected Coordinate location;
	protected double hlth;
	protected double mxHlth;
	protected int id;
	protected Random r;
	protected char type;
	
	
	public Matter() {
		r = new Random();
	}

	public Matter(double aMxHlth, char type) {
		hlth = mxHlth = aMxHlth;
		r = new Random();
		this.type = type;
		/*place(type);*/
	}

	public Matter(double aMxHlth, int anId, char type) {
		hlth = mxHlth = aMxHlth;
		id = anId;
		r = new Random();
		this.type = type;
		/*place(type);*/
	}
	
	/*public Matter(double aMxHlth, int anId, char type, LocationMap aMap) {
		hlth = mxHlth = aMxHlth;
		id = anId;
		r = new Random();
		this.type = type;
		place(type);
	}*/

	public boolean deplete(double val) {
		if (hlth - val < 0) {
			hlth = 0;
			int x = location.getX();
			int y = location.getY();
			LocationMap.getInstance().setRangeToBlank(x, y, getWidth(), getHeight());
			return true;
		}
		else hlth-=val;
		return false;
	}
	
	public boolean changeHealth(double val) {
		hlth+=val;
		if (hlth <= 0) {
			hlth = 0;
			return true;
		} else if (hlth > mxHlth)
			hlth = mxHlth;
		return false;
	}

	public void decreaseHealth(double val) {
		if (hlth - val < 0) {
			hlth = 0;
			/*setRange(getWidth(), getHeight(), 'w');*/
		}
		else hlth-=val;
	}
	
	/**
	 * @param scanRange
	 * @return number of surrounding objects, namely Food or Organism Instances
	 */
	public double numSurroundingObjects(int scanRange) {
		double numObj = 0.0;
		// create a square from cornerTop to cornerBottom of dimension
		// scanRange+getWidth/2 X scanRange+getHeight/2
		Coordinate cornerTop = new Coordinate(location.getX()
				- (getWidth() / 2 - scanRange), location.getY()
				- (getHeight() / 2) - scanRange);
		Coordinate cornerBottom = new Coordinate(location.getX()
				+ (getWidth() / 2 + scanRange), location.getY()
				+ (getHeight() / 2) + scanRange);
		for (int i = cornerTop.getX(); i <= cornerBottom.getX(); i++) {
			for (int j = cornerTop.getY(); j <= cornerBottom.getY(); j++) {
				try {
					// count all occurrences of objects in location map
					if (LocationMap.getInstance().get(i, j).getSnd() != 'w') {
						numObj++;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		// make sure that scanning object was not included in scan.
		if (numObj >= getWidth() * getHeight())
			numObj -= getWidth() * getHeight();
		// return a normalized value. Will count "partially" discovered
		// organisms
		// as a whole number, does not include "wrapped" scan.
		return Math.ceil(numObj / (getWidth() * getHeight()));
	}

	/**
	 * @param scanRange
	 * @param type
	 * @return a list of id numbers of the surrounding objects of choice.
	 */
	public ArrayList<Integer> getSurroundingObjects(char type, int scanRange) {
		Set<Integer> objectIds = new HashSet<Integer>();
		// Create a square from cornerTop to cornerBottom of
		// dimension scanRange+getWidth/2 X scanRange+getHeight/2 to be scanned.
		int widthSub = location.getX() - (getWidth() / 2);
		int widthPlus = location.getX() + (getWidth() / 2);
		int heightSub = location.getY() - (getHeight() / 2);
		int heightPlus = location.getY() + (getHeight() / 2);
		Coordinate cornerTop = new Coordinate(widthSub - scanRange, heightSub
				- scanRange);
		Coordinate cornerBottom = new Coordinate(widthPlus + scanRange,
				heightPlus + scanRange);
		for (int i = cornerTop.getX(); i <= cornerBottom.getX(); i++) {
			for (int j = cornerTop.getY(); j <= cornerBottom.getY(); j++) {
				try {
					// Count all occurrences of objects in location map
					Pair<Integer, Character> space = LocationMap.getInstance()
							.get(i, j);
					Character spaceType = space.getSnd();
					Integer spaceId = space.getFst();
					if (spaceType == type && spaceId != id)
						objectIds.add(space.getFst());
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		// Test prints
		/*System.out.println("Organism " + this.getId() + " is scanning from"
				+ location.getX() + ", " + location.getY());
		System.out.println("The scan range is " + scanRange
				+ " and the square is from " + cornerTop.getX() + ", "
				+ cornerBottom.getY() + "to " + cornerBottom.getX() + ", "
				+ cornerBottom.getY());*/
		return new ArrayList<Integer>(objectIds);
	}
	
	/**
	 * @param type
	 *//*
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

	*//**
	 * @param x - current x location if valid.
	 * @param y - current y location if valid.
	 * @return true if organism can spawn at given location.
	 *//*
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

	*//**
	 * This method will modify the boolean location map and account for wrapping.
	 * 
	 * @param width        x-size for rectangle
	 * @param height        y-size for rectangle
	 * @param validity the value to mark the location map.
	 *//*
	public void setRange(int width, int height, Character value){
		int x = location.getX();
		int y = location.getY();
		for (int i = (x - (width / 2)); i <= (x + (width / 2)); i++) {
			for (int j = (y - (height / 2)); j <= (y + (height / 2)); j++) {
				try {
					GridPanel.locationMap[i][j].setLeft(this.getId());
					GridPanel.locationMap[i][j].setRight(value);
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
	}

	*//**
	 * Handles objects that stray off of the GridPanel and wraps their location.
	 * @param rightLeftBound   - right and left boundary to trigger wrap
	 * @param topBottomBound   - top and bottom boundary to trigger wrap
	 *//*
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
	}*/
	
	public abstract int getHeight();	
	
	public abstract int getWidth();

	public void setMxHlth(double aMxHlth) {
		mxHlth = aMxHlth;
	}

	public void setHealth(double aHealth) {
		hlth = aHealth;
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
	
	public void setLocation(Coordinate newLocation) {
		location = newLocation;
	}

	public int getId() {
		return id;
	}
	
	public abstract char getType();

	public void printLocation() {
		out.println("(" + location.getX() +
				", " + location.getY() + ")");
	}
}
