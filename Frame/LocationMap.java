package Frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import Interactive.Food;
import Interactive.Organism;
import Interactive.Pair;
import Searching.StarQueue;

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

	/**
	 * Receives an organismList and places all of the organisms onto the map and
	 * sets their location fields.
	 * 
	 * @param orgList
	 */
	public void placeOrganisms(LinkedList<Organism> orgList) {
		for (int i = 0; i < orgList.size(); i++) {
			Organism org = orgList.get(i);
			place(org.getLocation(), org.getWidth(), org.getHeight(),
					org.getId(), org.getType());
		}
	}

	/**
	 * Finds an available location for a single organism, places the organism on
	 * the map, and sets the organisms location field.
	 * 
	 * @param orgList
	 */
	public void placeOrganism(LinkedList<Organism> orgList) {
		for (int i = 0; i < orgList.size(); i++) {
			Organism org = orgList.get(i);
			place(org.getLocation(), org.getWidth(), org.getHeight(),
					org.getId(), org.getType());
		}
	}
	
	/**
	 * @param orgList
	 */
	// TEST FOR THE PLACEORGANISMS METHOD. THE SAME EXCEPT WITH PRINT LINES.
	public void placeOrganismsTest(LinkedList<Organism> orgList) {
		for (int i = 0; i < orgList.size(); i++) {
			Organism org = orgList.get(i);
			placeTest(org.getLocation(), org.getWidth(), org.getHeight(), org.getId(),
					org.getType());
		}
	}

	/**
	 * Receives a foodList and places all of the food onto the map and sets
	 * their location fields.
	 * 
	 * @param foodList
	 */
	public void placeFoods(LinkedList<Food> foodList) {
		for (int i = 0; i < foodList.size(); i++) {
			Food f = foodList.get(i);
			place(f.getLocation(), f.getWidth(), f.getHeight(), f.getId(),
					f.getType());
		}
	}

	/**
	 * Finds an available location for a single food source, places the food
	 * source on the map, and sets the food's location field.
	 * 
	 * @param foodList
	 */
	public void placeFood(LinkedList<Food> foodList) {
		for (int i = 0; i < foodList.size(); i++) {
			Food f = foodList.get(i);
			place(f.getLocation(), f.getWidth(), f.getHeight(), f.getId(),
					f.getType());
		}
	}
	
	/**
	 * Returns the next position produced by the Astar search algorithm.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static Coordinate search(Coordinate start, Coordinate end, int anId) {
		// openList is a priority queue organized based
		// on shortest distance to end node.
		/* Coordinate endCoordinate = (Coordinate) end.spawnCoordinate(0); */
		end.setPriority(0);
		start.setPriority(distance(start.getX(), start.getY(), end.getX(),
				end.getY()));
		// for each surrounding position.
		// assign priority, then add to queue.
		Coordinate current = start;
		StarQueue<Coordinate> adjacentList = adjacentCoordinates(
				current.getX(), current.getY(), 1, end, anId);
		// If there are available positions to move to.
		// Then remove the top one. Which will be the best place to move to.
		if (!adjacentList.isEmpty())
			return adjacentList.remove();
		return start;
	}

	/**
	 * Only adds the adjacent nodes that don't have obstacles in them.
	 * @return
	 */
	public static StarQueue<Coordinate> adjacentCoordinates(int x, int y,
			int stepSize, Coordinate end, int anId) {
		StarQueue<Coordinate> adj = new StarQueue<Coordinate>();
		/*System.out.println("original position" + x + ", " + y);*/
		int moveWidth = stepSize;
		int moveHeight = stepSize;
		/*int moveWidth = Organism.width;
		int moveHeight = Organism.height;*/
		int endX = end.getX();
		int endY = end.getY();
		// W
		if (!hasObstacle(x - moveWidth, y, anId))
			adj.add(new Coordinate(x - moveWidth, y, distance(x - moveWidth, y,
					endX, endY)));
		// NW
		if (!hasObstacle(x - moveWidth, y + moveHeight, anId))
			adj.add(new Coordinate(x - moveWidth, y + moveHeight, distance(x
					- moveWidth, y + moveHeight, endX, endY)));
		// N
		if (!hasObstacle(x, y + moveHeight, anId))
			adj.add(new Coordinate(x, y + moveHeight, distance(x, y
					+ moveHeight, endX, endY)));
		// NE
		if (!hasObstacle(x + moveWidth, y + moveHeight, anId))
			adj.add(new Coordinate(x + moveWidth, y + moveHeight, distance(x
					+ moveWidth, y + moveHeight, endX, endY)));
		// E
		if (!hasObstacle(x + moveWidth, y, anId))
			adj.add(new Coordinate(x + moveWidth, y, distance(x + moveWidth, y,
					endX, endY)));
		// SE
		if (!hasObstacle(x + moveWidth, y - moveHeight, anId))
			adj.add(new Coordinate(x + moveWidth, y - moveHeight, distance(x
					+ moveWidth, y - moveHeight, endX, endY)));
		// S
		if (!hasObstacle(x, y - moveHeight, anId))
			adj.add(new Coordinate(x, y - moveHeight, distance(x, y
					- moveHeight, endX, endY)));
		// SW
		if (!hasObstacle(x - moveWidth, y - moveHeight, anId))
			adj.add(new Coordinate(x - moveWidth, y - moveHeight, distance(x
					- moveWidth, y - moveHeight, endX, endY)));
		return adj;
	}

	/**
	 * Calculates the priority of a given coordinate start o the coordinate end.
	 * Priority is based on the distance of a straight line between the two
	 * points.
	 * 
	 * @param current
	 * @param end
	 * @return
	 */
	public static double distance(double orgX, double orgY, double foodX,
			double foodY) {
		return Math.sqrt(Math.pow((foodX - orgX), 2)
				+ Math.pow((foodY - orgY), 2));
	}

	/**
	 * @param scanRange
	 * @return number of surrounding objects, namely Food or Organism Instances
	 */
	public static HashMap<String, ArrayList<Integer>> objectsInSpace(int x,
			int y) {
		HashSet<Integer> pois = new HashSet<Integer>();
		HashSet<Integer> heal = new HashSet<Integer>();
		HashSet<Integer> orgs = new HashSet<Integer>();
		checkObstacles: for (int i = x - Organism.width / 2; i <= x
				+ Organism.width / 2; i++) {
			for (int j = y - Organism.height / 2; j <= y + Organism.height / 2; j++) {
				try {
					Pair<Integer, Character> object = LocationMap.getInstance().get(i, j);
					// count all occurrences of objects in location map
					if (object.getSnd() == 'w')
						continue checkObstacles;
					else if (object.getSnd() == 'p')
						pois.add(object.getFst());
					else if (object.getSnd() == 'h')
						heal.add(object.getFst());
					else if (object.getSnd() == 'o')
						orgs.add(object.getFst());
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		HashMap<String, ArrayList<Integer>> obstacles = new HashMap<String, ArrayList<Integer>>();
		if (orgs.size() > 0)
			obstacles.put("org", new ArrayList<Integer>(orgs));
		else
			obstacles.put("org", null);
		if (heal.size() > 0)
			obstacles.put("hFood", new ArrayList<Integer>(heal));
		else
			obstacles.put("hFood", null);
		if (pois.size() > 0)
			obstacles.put("pFood", new ArrayList<Integer>(pois));
		else
			obstacles.put("pFood", null);
		return obstacles;
	}

	/**
	 * @param scanRange
	 * @return boolean whether or not there are objects occupying the space.
	 */
	private static boolean hasObstacle(int x, int y, int anId) {
		int edgeWidth = (int) Math.ceil((double) Organism.width / 2);
		int edgeHeight = (int) Math.ceil((double) Organism.height / 2);

		/*System.out.println("checking position" + x + ", " + y);*/
		checkObstacles: for (int i = x - edgeWidth; i <= x
		+ edgeWidth; i++) {
			for (int j = y - edgeHeight; j <= y
			+ edgeHeight; j++) {
				/*System.out.println("i: " + i);
				System.out.println("j: " + j);*/
				try {
					Pair<Integer, Character> aPair = LocationMap.getInstance().get(i, j);
					Character charType = aPair.getSnd();
					Integer spaceId = aPair.getFst();
					/*
					 * System.out.println("charType: " + charType);
					 * System.out.println("spaceId: " + spaceId);
					 * System.out.println("orgId: " + anId);
					 * System.out.println();
					 */
					// Count all occurrences of objects in location map.
					if (charType == 'w') {
						continue checkObstacles;
					}
					//ignore self
					if (charType == 'o' && spaceId == anId) {
						continue checkObstacles;
					} else
						return true;
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		/*System.out.println("should move");*/
		return false;
	}
}
