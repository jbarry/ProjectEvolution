package Searching;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import Frame.Coordinate;
import Frame.GridPanel;
import Interactive.Organism;
import Interactive.Pair;

public class AStar {

	public static void main(String[] args) {
		AStar a = new AStar();
		Coordinate coord1 = new Coordinate(2, 5);
		Coordinate coord2 = new Coordinate(2, 5);
		//Test getPriority method.
		//Test case where distance should be zero.
	}

	/**
	 * Returns the next position produced by the Astar search algorithm.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public Coordinate search1(Coordinate start, Coordinate end) {
		// openList is a priority queue organized based
		// on shortest distance to end node.
		StarQueue<Coordinate> openList = new StarQueue<Coordinate>(0);
		/* Coordinate endCoordinate = (Coordinate) end.spawnCoordinate(0); */
		end.setPriority(0);
		start.setPriority(distance(start.getX(), start.getY(), end.getX(),
				end.getY()));
		// for each surrounding position.
		// assign priority, then add to queue.
		Coordinate current = start;
		StarQueue<Coordinate> adjacentList = adjacentCoordinates(current.getX(),
				current.getY(), end);
		// If there are available positions to move to.
		// Then remove the top one. Which will be the best place to move to.
		if (adjacentList != null)
			return adjacentList.remove();
		return start;
	}

	/**
	 * Only adds the adjacent nodes that don't have obstacles in them.
	 * @return
	 */
	public StarQueue<Coordinate> adjacentCoordinates(int x, int y,
			Coordinate end) {
		StarQueue<Coordinate> adj = new StarQueue<Coordinate>(0);
		int moveWidth = (2 * Organism.width);
		int moveHeight = (2 * Organism.height);
		int endX = end.getX();
		int endY = end.getY();
		// W
		if (!hasObstacle(x - moveWidth, y))
			adj.add(new Coordinate(x - moveWidth, y, distance(x - moveWidth, y,
					endX, endY)));
		// NW
		if (!hasObstacle(x - moveWidth, y + moveHeight))
			adj.add(new Coordinate(x - moveWidth, y + moveHeight, distance(x
					- moveWidth, y + moveHeight, endX, endY)));
		// N
		if (!hasObstacle(x, y + moveHeight))
			adj.add(new Coordinate(x, y + moveHeight, distance(x, y
					+ moveHeight, endX, endY)));
		// NE
		if (!hasObstacle(x + moveWidth, y + moveHeight))
			adj.add(new Coordinate(x + moveWidth, y + moveHeight, distance(x
					+ moveWidth, y + moveHeight, endX, endY)));
		// E
		if (!hasObstacle(x + moveWidth, y))
			adj.add(new Coordinate(x + moveWidth, y, distance(x + moveWidth, y,
					endX, endY)));
		// SE
		if (!hasObstacle(x + moveWidth, y - moveHeight))
			adj.add(new Coordinate(x + moveWidth, y - moveHeight, distance(x
					+ moveWidth, y - moveHeight, endX, endY)));
		// S
		if (!hasObstacle(x, y - moveHeight))
			adj.add(new Coordinate(x, y - moveHeight, distance(x, y
					- moveHeight, endX, endY)));
		// SW
		if (!hasObstacle(x - moveWidth, y - moveHeight))
			adj.add(new Coordinate(x - moveWidth, y - moveHeight, distance(x
					- moveWidth, y - moveHeight, endX, endY)));
		return adj;
	}

	/**
	 * Calculates the priority of a given coordinate start o
	 * the coordinate end. Priority is based on the distance of
	 * a straight line between the two points.
	 * @param current
	 * @param end
	 * @return
	 */
	private double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow((x2 - x1), 2) +
				Math.pow((y2 - y1), 2));
	}

	/**
	 * @param scanRange
	 * @return number of surrounding objects, namely Food or Organism Instances
	 */
	public HashMap<String, ArrayList<Integer>> objectsInSpace(int x, int y) {
		HashSet<Integer> pois = new HashSet<Integer>();
		HashSet<Integer> heal = new HashSet<Integer>();
		HashSet<Integer> orgs = new HashSet<Integer>();
		double numObj = 0.0;
		checkObstacles: for(int i = x - Organism.width/2; i <= x + Organism.width/2; i++){
			for(int j = y - Organism.height/2; j <= y + Organism.height/2; j++){
				try{	
					Pair<Integer, Character> object = GridPanel.locationMap[i][j];
					//count all occurrences of objects in location map
					if(object.getSnd() == 'w') 
						continue checkObstacles;
					else if(object.getSnd() == 'p')
						pois.add(object.getFst());
					else if(object.getSnd() == 'h')
						heal.add(object.getFst());
					else if(object.getSnd() == 'o')
						orgs.add(object.getFst());
				}
				catch(ArrayIndexOutOfBoundsException e){
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
	public boolean hasObstacle(int x, int y) {
		int numObj = 0;
		checkObstacles: for (int i = x - (Organism.width / 2); i <= x
				+ (Organism.width / 2); i++) {
			for (int j = y - (Organism.height / 2); j <= y
					+ (Organism.height / 2); j++){
				try{	
					Character matter = GridPanel.locationMap[i][j].getSnd();
					//count all occurrences of objects in location map
					if(matter == 'w') 
						continue checkObstacles;
					else
						return true;
				}
				catch(ArrayIndexOutOfBoundsException e){
				}
			}
		}
		return false;
	}
}
