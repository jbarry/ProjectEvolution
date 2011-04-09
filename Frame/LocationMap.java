package Frame;

import java.util.LinkedList;
import java.util.Random;

import Interactive.Food;
import Interactive.Organism;
import Interactive.Pair;

public class LocationMap {
	
	private Pair<Integer, Character>[][] locationMap;
	Random r;
	private static LocationMap instance;

	/**
	 * Public access for the singleton.
	 * 
	 * @return the singleton object.
	 */
	public static LocationMap getInstance() {
		if (instance == null)
			instance = new LocationMap();
		return instance;
	}

	@SuppressWarnings("unchecked")
	private LocationMap() {
		locationMap = new Pair[GridPanel.WIDTH][GridPanel.HEIGHT];
		clearLocations();
		r = new Random();
	}

	/**
	 * @param type
	 */
	public void placeTest(Coordinate c, int width, int height, int anId, Character aType) {
		//set location
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);

		//check for collisions
		spawn: while(!canSpawn(x, y, width, height)) {
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
			if (x == 0 || y == 0)
				continue spawn;
		}
		c.setX(x);
		c.setY(y);
		setWrapAround(c, width, height);
		System.out.println("After wrapAround: (" + c.getX() + ", " + c.getY() + ")");
		//set boundaries
		/*setWrapAround(getWidth(), getHeight());*/
		setRange(c, width, height, aType, anId);
	}
	
	/**
	 * @param type
	 */
	public void place(Coordinate c, int width, int height, int anId, Character aType) {
		//set location
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);

		//check for collisions
		spawn: while(!canSpawn(x, y, width, height)) {
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
			if (x == 0 || y == 0)
				continue spawn;
		}
		c.setX(x);
		c.setY(y);
		setWrapAround(c, width, height);

		//set boundaries
		/*setWrapAround(getWidth(), getHeight());*/
		setRange(c, width, height, aType, anId);
	}
	
	// CLOSER TO THE ORIGINAL THAN THE OTHER NEW LOCATION.
	public void newLocation(Coordinate c, int width, int height, int anId, Character aType) {
		setRangeToBlank(width, height, c.getX(), c.getY());
		int newX = r.nextInt(GridPanel.WIDTH);
		int newY = r.nextInt(GridPanel.HEIGHT);
		spawn: while(!canSpawn(newX, newY, width, height)) {
			newX = r.nextInt(GridPanel.WIDTH);
			newY = r.nextInt(GridPanel.HEIGHT);
			if (newX == 0 || newY == 0)
				continue spawn;
		}
		c.setX(newX);
		c.setY(newY);
		setWrapAround(c, width, height);
		//set boundaries
		/*setWrapAround(newLocation, width, height);*/
		setRange(c, width, height, aType, anId);
	}
	
	/*public void newLocation(Matter m) {
		int width = m.getWidth();
		int height = m.getHeight();
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);
		// Find an available position.
		while(!canSpawn(x, y, width, height)){
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		Coordinate location = m.getLocation();
		location.setX(x);
		location.setY(y);
		// Replace old position with 'w'.
		int mX = location.getX();
		int mY = location.getY();
		setRange(width, height, 'w', m.getId(), mX, mY);
		// Set boundaries.
		setWrapAround(width, height);
		setRange(width, height, 'o');
	}*/

	/**
	 * @param x
	 *            Potential x location.
	 * @param y
	 *            Potential y location.
	 * @return true if organism can spawn at given location.
	 */
	public boolean canSpawn(int x, int y, int width, int height) {
		for (int i = x - (width / 2); i <= x + (width / 2); i++) {
			for (int j = y - (height / 2); j <= y + (height / 2); j++) {
				try {
					if (locationMap[i][j].getSnd() != 'w') {
						return false;
					}
				} catch (ArrayIndexOutOfBoundsException e) {

				}
			}
		}
		return true;
	}

	/**
	 * @param width
	 * @param height
	 * @param value
	 * @param id
	 * @param x
	 * @param y
	 */
	public void setRange(Coordinate c, int width, int height, Character value, int id) {
		int x = c.getX();
		int y = c.getY();
		for (int i = (x - (width / 2)); i <= (x + (width / 2)); i++) {
			for (int j = (y - (height / 2)); j <= (y + (height / 2)); j++) {
				try {
					locationMap[i][j].setLeft(id);
					locationMap[i][j].setRight(value);
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
	}
	
	/**
	 * @param width
	 * @param height
	 * @param value
	 * @param id
	 * @param x
	 * @param y
	 */
	public void setRangeToBlank(int width, int height, int x, int y) {
		for (int i = (x - (width / 2)); i <= (x + (width / 2)); i++) {
			for (int j = (y - (height / 2)); j <= (y + (height / 2)); j++) {
				try {
					locationMap[i][j].setLeft(null);
					/*locationMap[i][j].setLeft(0);*/
					locationMap[i][j].setRight('w');
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
	}

	/**
	 * Handles objects that stray off of the GridPanel and wraps their location.
	 * @param rightLeftBound   - right and left boundary to trigger wrap
	 * @param topBottomBound   - top and bottom boundary to trigger wrap
	 */
	public void setWrapAround(Coordinate c, int width, int height){
		int x = c.getX();
		int y = c.getY();
		if (x + (width / 2) >= GridPanel.WIDTH) // right.
			if (canSpawn((width / 2) + 1, y, width, height))
				c.setX((width / 2) + 1);
		if (x - (width / 2) <= 0) // left.
			if (canSpawn(GridPanel.WIDTH - (width / 2), y, width, height))
				c.setX(GridPanel.WIDTH - (width / 2));
		if (y + (height / 2) >= GridPanel.HEIGHT) // bottom.
			if (canSpawn(x, (height / 2) + 1, width, height))
				c.setY((height / 2) + 1);
		if (y - (height / 2) <= 0) // top.
			if (canSpawn(x, GridPanel.HEIGHT - (height / 2), width, height))
				c.setY(GridPanel.HEIGHT - (height / 2));
	}
	
	/**
	 * Handles objects that stray off of the GridPanel and wraps their location.
	 * @param rightLeftBound   - right and left boundary to trigger wrap
	 * @param topBottomBound   - top and bottom boundary to trigger wrap
	 */
	// FOR TESTING OF THE SET WRAP AROUND METHOD.
	public void setWrapAroundTest(Coordinate c, int width, int height){
		int x = c.getX();
		int y = c.getY();
		if (x + (width / 2) >= GridPanel.WIDTH) // right.
			if (canSpawn((width / 2) + 1, y, width, height))
				c.setX((width / 2) + 1);
		if (x - (width / 2) <= 0) // left.
			if (canSpawn(GridPanel.WIDTH - (width / 2), y, width, height))
				c.setX(GridPanel.WIDTH - (width / 2));
		if (y + (height / 2) >= GridPanel.HEIGHT) // bottom.
			if (canSpawn(x, (height / 2) + 1, width, height))
				c.setY((height / 2) + 1);
		if (y - (height / 2) <= 0) // top.
			if (canSpawn(x, GridPanel.HEIGHT - (height / 2), width, height))
				c.setY(GridPanel.HEIGHT - (height / 2));
		System.out.println("InSetWrap the location is: " + c.getX()
				+ ", " + c.getY());
	}
	
	public void clearLocations() {
		for (int i = 0; i < locationMap.length; i++) {
			for (int j = 0; j < locationMap[i].length; j++) {
				// mark available
				/*locationMap[i][j] = new Pair<Integer, Character>(0, 'w');*/
				locationMap[i][j] = new Pair<Integer, Character>(null, 'w');
			}
		}
	}
	
	public int length() {
		return locationMap.length;
	}
	
	public int colLength(int i) {
		return locationMap[i].length;
	}
	
	public void set(int i, int j, Pair<Integer, Character> aPair) {
		locationMap[i][j] = aPair;
	}
	
	public Pair<Integer, Character> get(int i, int j) {
		return locationMap[i][j];
	}

	public void placeOrganisms(LinkedList<Organism> orgList) {
		for (int i = 0; i < orgList.size(); i++) {
			Organism org = orgList.get(i);
			place(org.getLocation(), org.getWidth(), org.getHeight(),
					org.getId(), org.getType());
		}
	}

	public void placeOrganismsTest(LinkedList<Organism> orgList) {
		for (int i = 0; i < orgList.size(); i++) {
			Organism org = orgList.get(i);
			placeTest(org.getLocation(), org.getWidth(), org.getHeight(), org.getId(),
					org.getType());
		}
	}
	
	public void placeFood(LinkedList<Food> foodList) {
		for (int i = 0; i < foodList.size(); i++) {
			Food f = foodList.get(i);
			place(f.getLocation(), f.getWidth(), f.getHeight(), f.getId(),
					f.getType());
		}
	}
}
