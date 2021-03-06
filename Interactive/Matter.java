package Interactive;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Frame.Coordinate;
import Frame.LocationMap;

public abstract class Matter implements Comparable<Matter>, Cloneable{

	protected Coordinate location;
	protected double hlth;
	protected double mxHlth;
	protected int id;
	protected Random r;
	protected char type;
	public static boolean graphicsEnabled = true;
	
	public Matter() {
		r = new Random();
	}

	public Matter(double aMxHlth, char type) {
		hlth = mxHlth = aMxHlth;
		r = new Random();
		this.type = type;
		location = new Coordinate();
		/*place(type);*/
	}

	public Matter(double aMxHlth, int anId, char type) {
		hlth = mxHlth = aMxHlth;
		id = anId;
		r = new Random();
		this.type = type;
		location = new Coordinate();
		/*place(type);*/
	}
	
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
	
	public void decreaseHealth(double val) {
		if (hlth - val < 0) {
			hlth = 0;
			/*setRange(getWidth(), getHeight(), 'w');*/
		}
		else hlth-=val;
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
					Character spaceType = space.getRight();
					Integer spaceId = space.getLeft();
					if (spaceType == type && spaceId != id)
						objectIds.add(space.getLeft());
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
	
	public abstract int getHeight();	
	
	public abstract int getWidth();
	
	public abstract int getId();

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

	public void setId(int anId) {
		id = anId;
	}
	
	public abstract char getType();

	public void printLocation() {
		out.println("(" + location.getX() +
				", " + location.getY() + ")");
	}
}
