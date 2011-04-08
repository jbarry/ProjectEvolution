package Frame;

import java.util.Random;

import Interactive.Matter;
import Interactive.Pair;

public class LocationMap {
	
	public Pair<Integer, Character>[][] locationMap;
	Random r;
	private static LocationMap instance;

	/**
	 * Public access for the singleton.
	 * 
	 * @return the singleton object.
	 */
	public static LocationMap getInstance() {
		if (instance == null) {
			instance = new LocationMap();
		}
		return instance;
	}

	private void LocationMap() {
		locationMap = new Pair[GridPanel.WIDTH][GridPanel.HEIGHT];
		r = new Random();
	}

	/**
	 * @param type
	 */
	private Coordinate place(Matter m) {
		// Potential location.
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);
		int width = m.getWidth();
		int height = m.getHeight();
		// Check for collisions.
		while (!canSpawn(x, y, width, height)) {
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		// Set boundaries.
		setWrapAround(m.getLocation(), width, height);
		setRange(width, height, m.getType(), m.getId(), x, y);
		return new Coordinate(x, y);
	}
	
	public void newLocation(Matter m) {
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
	}

	/**
	 * @param x
	 *            Potential x location.
	 * @param y
	 *            Potential y location.
	 * @return true if organism can spawn at given location.
	 */
	protected boolean canSpawn(int x, int y, int width, int height) {
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
	public void setRange(int width, int height, Character value, int id, int x, int y) {
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
	 * Handles objects that stray off of the GridPanel and wraps their location.
	 * @param rightLeftBound   - right and left boundary to trigger wrap
	 * @param topBottomBound   - top and bottom boundary to trigger wrap
	 */
	protected void setWrapAround(Coordinate location, int width, int height){
		int y = location.getY();
		int x = location.getX();
		if (x + (width / 2) >= GridPanel.WIDTH) // right.
			if (canSpawn((width / 2) + 1, y, width, height))
				location.setX((width / 2) + 1);
		if (x - (width / 2) <= 0) // left.
			if (canSpawn(GridPanel.WIDTH - (width / 2), y, width, height))
				location.setX(GridPanel.WIDTH - (width / 2));
		if (y + (height / 2) >= GridPanel.HEIGHT) // bottom.
			if (canSpawn(x, (height / 2) + 1, width, height))
				location.setY((height / 2) + 1);
		if (y - (height / 2) <= 0) // top.
			if (canSpawn(x, GridPanel.HEIGHT - (height / 2), width, height))
				location.setY(GridPanel.HEIGHT - (height / 2));
	}
	
	public void clearLocations() {
		for (int i = 0; i < locationMap.length; i++) {
			for (int j = 0; j < locationMap[i].length; j++) {
				// mark available
				locationMap[i][j] = new Pair<Integer, Character>(0, 'w');
			}
		}
	}
	
	
	public void newLocation() {
		setRange(width, height, 'w');
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);
		while(!canSpawn(x, y)){
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		location = new Coordinate(x, y);
		//set boundaries
		setWrapAround(width, height);
		setRange(width, height, 'o');
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
}
