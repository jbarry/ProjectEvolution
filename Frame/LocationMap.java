package Frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import Interactive.Food;
import Interactive.Organism;
import Interactive.Pair;

/**
 * Location map will consist of: key: current instance number of object value:
 * 'w' for white space or available. 'o' for organism. 'h' for healthy food. 'p'
 * for poisonous food.
 * 
 * @author projev.
 * 
 */
public class LocationMap {

	private Pair<Integer, Character>[][] locationMap;
	private Random r;
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
	public void placeTest(Coordinate c, int width, int height, int anId,
			Character aType) {
		// Set location.
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);

		// Check for collisions.
		spawn: while (!canSpawn(x, y, width, height)) {
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
			if (x == 0 || y == 0)
				continue spawn;
		}
		c.setX(x);
		c.setY(y);
		// Set boundaries.
		setWrapAround(c, width, height);
		System.out.println("After wrapAround: (" + c.getX() + ", " + c.getY()
				+ ")");
		/*setWrapAround(getWidth(), getHeight());*/
		setRange(c, width, height, aType, anId);
	}

	/**
	 * @param type
	 */
	public void place(Coordinate c, int width, int height, int anId,
			Character aType) {
		// set location
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);

		// check for collisions
		spawn: while (!canSpawn(x, y, width, height)) {
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
			if (x == 0 || y == 0)
				continue spawn;
		}
		c.setX(x);
		c.setY(y);
		setWrapAround(c, width, height);

		// set boundaries
		/*setWrapAround(getWidth(), getHeight());*/
		setRange(c, width, height, aType, anId);
	}

	// CLOSER TO THE ORIGINAL THAN THE OTHER NEW LOCATION.
	/**
	 * @param c
	 * @param width
	 * @param height
	 * @param anId
	 * @param aType
	 */
	public void newLocation(Coordinate c, int width, int height, int anId,
			Character aType) {
		setRangeToBlank(width, height, c.getX(), c.getY());
		int newX = r.nextInt(GridPanel.WIDTH);
		int newY = r.nextInt(GridPanel.HEIGHT);
		spawn: while (!canSpawn(newX, newY, width, height)) {
			newX = r.nextInt(GridPanel.WIDTH);
			newY = r.nextInt(GridPanel.HEIGHT);
			if (newX == 0 || newY == 0)
				continue spawn;
		}
		c.setX(newX);
		c.setY(newY);
		setWrapAround(c, width, height);
		// Set boundaries.
		setRange(c, width, height, aType, anId);
	}

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
					if (locationMap[i][j].getRight() != 'w') {
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
	public void setRange(Coordinate c, int width, int height, Character value,
			int id) {
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
	 * 
	 * @param rightLeftBound
	 *            - right and left boundary to trigger wrap
	 * @param topBottomBound
	 *            - top and bottom boundary to trigger wrap
	 */
	public void setWrapAround(Coordinate c, int width, int height) {
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
	 * 
	 * @param rightLeftBound
	 *            - right and left boundary to trigger wrap
	 * @param topBottomBound
	 *            - top and bottom boundary to trigger wrap
	 */
	// FOR TESTING OF THE SET WRAP AROUND METHOD.
	public void setWrapAroundTest(Coordinate c, int width, int height) {
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
		System.out.println("InSetWrap the location is: " + c.getX() + ", "
				+ c.getY());
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
					org.getMatterID(), org.getType());
		}
	}

	/**
	 * Finds an available location for a single organism, places the organism on
	 * the map, and sets the organisms location field.
	 * 
	 * @param orgList
	 */
	public void placeOrganism(Organism org) {
		place(org.getLocation(), org.getWidth(), org.getHeight(), org.getMatterID(),
				org.getType());
	}

	/**
	 * Places the organism on the map in the position that is passed as a
	 * parameter, and sets the organisms location field.
	 * 
	 * @param orgList
	 */
	public void setOrganism(Organism org) {
		setWrapAround(org.getLocation(), org.getWidth(), org.getHeight());
		setRange(org.getLocation(), org.getWidth(), org.getHeight(), 'o',
				org.getMatterID());
	}

	/**
	 * @param orgList
	 */
	// TEST FOR THE PLACEORGANISMS METHOD. THE SAME EXCEPT WITH PRINT LINES.
	public void placeOrganismsTest(LinkedList<Organism> orgList) {
		for (int i = 0; i < orgList.size(); i++) {
			Organism org = orgList.get(i);
			placeTest(org.getLocation(), org.getWidth(), org.getHeight(),
					org.getMatterID(), org.getType());
		}
	}

	/**
	 * Finds an available location for a single food source, places the food
	 * source on the map, and sets the food's location field.
	 * 
	 * @param foodList
	 */
	public void placeFoods(LinkedList<Food> foodList) {
		for (int i = 0; i < foodList.size(); i++) {
			Food f = foodList.get(i);
			place(f.getLocation(), f.getWidth(), f.getHeight(), f.getMatterID(),
					f.getType());
		}
	}

	/**
	 * Receives a foodList and places all of the food onto the map and sets
	 * their location fields.
	 * 
	 * @param foodList
	 */
	public void placeFood(Food f) {
		place(f.getLocation(), f.getWidth(), f.getHeight(), f.getMatterID(),
				f.getType());
	}

	/**
	 * Returns the next position produced by the Astar search algorithm.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public Coordinate search(Coordinate start, Coordinate end, int anId) {
		// Open list is a priority queue organized based on shortest distance to
		// end node.
		end.setPriority(0);
		start.setPriority(distance(start.getX(), start.getY(), end.getX(),
				end.getY()));
		// For each surrounding position assign priority, then add to queue.
		Coordinate current = start;
		PriorityQueue<Coordinate> adjacentList = adjacentCoordinates(
				current.getX(), current.getY(), 3, end, anId);
		// If there are available positions to move to, then remove the top one,
		// which will be the best place to move to.
		if (!adjacentList.isEmpty())
			return adjacentList.remove();
		return start;
	}

	/**
	 * Returns the next position produced by the Astar search algorithm. This
	 * search returns a list that contains the start coordinate if the
	 * adjacentCoordinates method returns an empty priority queue.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public PriorityQueue<Coordinate> searchWithList(Coordinate start,
			Coordinate end, int anId) {
		// Open list is a priority queue organized based on shortest distance to
		// end node.
		end.setPriority(0);
		start.setPriority(distance(start.getX(), start.getY(), end.getX(),
				end.getY()));
		// For each surrounding position assign priority, then add to queue.
		Coordinate current = start;
		PriorityQueue<Coordinate> adjacentList = adjacentCoordinates(
				current.getX(), current.getY(), 3, end, anId);
		// If there are available positions to move to, then remove the top one,
		// which will be the best place to move to.
		if (!adjacentList.isEmpty())
			return adjacentList;
		PriorityQueue<Coordinate> listWithSelfCoordinate =
			new PriorityQueue<Coordinate>();
		listWithSelfCoordinate.add(start);
		return listWithSelfCoordinate;
	}

	/**
	 * Only adds the adjacent nodes that don't have obstacles in them.
	 * 
	 * @return
	 */
	public PriorityQueue<Coordinate> adjacentCoordinates(int x, int y,
			int stepSize, Coordinate end, int anId) {
		PriorityQueue<Coordinate> adj = new PriorityQueue<Coordinate>();
		/*System.out.println("original position" + x + ", " + y);*/
		int moveWidth = stepSize;
		int moveHeight = stepSize;
		int endX = end.getX();
		int endY = end.getY();
		// W
		if (!hasObstacle(x - moveWidth, y, anId))
			adj.add(new Coordinate(x - moveWidth, y, distance(x - moveWidth, y,
					endX, endY)));
		// NW
		if (!hasObstacle(x - moveWidth, y - moveHeight, anId))
			adj.add(new Coordinate(x - moveWidth, y - moveHeight, distance(x
					- moveWidth, y - moveHeight, endX, endY)));
		// N
		if (!hasObstacle(x, y - moveHeight, anId))
			adj.add(new Coordinate(x, y - moveHeight, distance(x, y
					- moveHeight, endX, endY)));
		// NE
		if (!hasObstacle(x + moveWidth, y - moveHeight, anId))
			adj.add(new Coordinate(x + moveWidth, y - moveHeight, distance(x
					+ moveWidth, y - moveHeight, endX, endY)));
		// E
		if (!hasObstacle(x + moveWidth, y, anId))
			adj.add(new Coordinate(x + moveWidth, y, distance(x + moveWidth, y,
					endX, endY)));
		// SE
		if (!hasObstacle(x + moveWidth, y + moveHeight, anId)) {
			adj.add(new Coordinate(x + moveWidth, y + moveHeight, distance(x
					+ moveWidth, y + moveHeight, endX, endY)));
		}
		// S
		if (!hasObstacle(x, y + moveHeight, anId))
			adj.add(new Coordinate(x, y + moveHeight, distance(x, y
					+ moveHeight, endX, endY)));
		// SW
		if (!hasObstacle(x - moveWidth, y + moveHeight, anId))
			adj.add(new Coordinate(x - moveWidth, y + moveHeight, distance(x
					- moveWidth, y + moveHeight, endX, endY)));
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
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}

	/**
	 * @param scanRange
	 * @return number of surrounding objects, namely Food or Organism Instances
	 */
	public HashMap<String, ArrayList<Integer>> objectsInSpace(int x,
			int y) {
		HashSet<Integer> pois = new HashSet<Integer>();
		HashSet<Integer> heal = new HashSet<Integer>();
		HashSet<Integer> orgs = new HashSet<Integer>();
		checkObstacles: for (int i = x - Organism.width / 2; i <= x
				+ Organism.width / 2; i++) {
			for (int j = y - Organism.height / 2; j <= y + Organism.height / 2; j++) {
				try {
					Pair<Integer, Character> object = LocationMap.getInstance()
							.get(i, j);
					// Count all occurrences of objects in location map.
					if (object.getRight() == 'w')
						continue checkObstacles;
					else if (object.getRight() == 'p')
						pois.add(object.getLeft());
					else if (object.getRight() == 'h')
						heal.add(object.getLeft());
					else if (object.getRight() == 'o')
						orgs.add(object.getLeft());
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
	public boolean hasObstacle(int x, int y, int anId) {
		int edgeWidth = Organism.width / 2;
		int edgeHeight = Organism.height / 2;
		for (int i = x - edgeWidth; i <= x + edgeWidth; i++) {
			for (int j = y - edgeHeight; j <= y + edgeHeight; j++) {
				try {
					Pair<Integer, Character> aPair = LocationMap.getInstance()
							.get(i, j);
					Character charType = aPair.getRight();
					Integer spaceId = aPair.getLeft();
					if (charType == 'o' && spaceId != anId)
						return true;
					if (charType == 'h' || charType == 'p')
						return true;
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		return false;
	}
	
	/**
	 * @param scanRange
	 * @return number of surrounding objects, namely Food or Organism Instances
	 */
	public double numSurroundingObjects(Coordinate location,int width, int height, int scanRange) {
		Set<Integer> objectIds = new HashSet<Integer>();
		// Create a square from cornerTop to cornerBottom of
		// dimension scanRange+getWidth/2 X scanRange+getHeight/2 to be scanned.
		int widthSub = location.getX() - (width / 2);
		int widthPlus = location.getX() + (width / 2);
		int heightSub = location.getY() - (height / 2);
		int heightPlus = location.getY() + (height / 2);
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
					if (spaceType != 'w')
						objectIds.add(space.getLeft());
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		return objectIds.size();
	}
}
