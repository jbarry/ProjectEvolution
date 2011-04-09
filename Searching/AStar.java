/*package Searching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import Frame.Coordinate;
import Frame.GridPanel;
import Frame.LocationMap;
import Interactive.Organism;
import Interactive.Pair;

public class AStar {
	
	*//**
	 * Returns the next position produced by the Astar search algorithm.
	 * 
	 * @param start
	 * @param end
	 * @return
	 *//*
	public static Coordinate search(Coordinate start, Coordinate end) {
		// openList is a priority queue organized based
		// on shortest distance to end node.
		 Coordinate endCoordinate = (Coordinate) end.spawnCoordinate(0); 
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
		if (!adjacentList.isEmpty())
			return adjacentList.remove();
		System.out.println("adjacentList is empty");
		return start;
	}

	*//**
	 * Only adds the adjacent nodes that don't have obstacles in them.
	 * @return
	 *//*
	public static StarQueue<Coordinate> adjacentCoordinates(int x, int y,
			Coordinate end) {
		StarQueue<Coordinate> adj = new StarQueue<Coordinate>();
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

	*//**
	 * Calculates the priority of a given coordinate start o
	 * the coordinate end. Priority is based on the distance of
	 * a straight line between the two points.
	 * @param current
	 * @param end
	 * @return
	 *//*
	public static double distance(double orgX, double orgY, double foodX, double foodY) {
		return Math.sqrt(Math.pow((foodX - orgX), 2) +
				Math.pow((foodY - orgY), 2));
	}

	*//**
	 * @param scanRange
	 * @return number of surrounding objects, namely Food or Organism Instances
	 *//*
	public static HashMap<String, ArrayList<Integer>> objectsInSpace(int x, int y) {
		HashSet<Integer> pois = new HashSet<Integer>();
		HashSet<Integer> heal = new HashSet<Integer>();
		HashSet<Integer> orgs = new HashSet<Integer>();
		double numObj = 0.0;
		LocationMap locationMap = LocationMap.getInstance();
		checkObstacles: for(int i = x - Organism.width/2; i <= x + Organism.width/2; i++){
			for(int j = y - Organism.height/2; j <= y + Organism.height/2; j++){
				try{	
					Pair<Integer, Character> object = locationMap.get(i, j);
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

	*//**
	 * @param scanRange
	 * @return boolean whether or not there are objects occupying the space.
	 *//*
	public static boolean hasObstacle(int x, int y) {
		LocationMap locationMap = LocationMap.getInstance();
		checkObstacles: for (int i = x - (Organism.width / 2); i <= x
				+ (Organism.width / 2); i++) {
			for (int j = y - (Organism.height / 2); j <= y
					+ (Organism.height / 2); j++) {
				try {
					Character matter = locationMap.get(i, j).getSnd();
					// count all occurrences of objects in location map
					if (matter == 'w')
						continue checkObstacles;
					else
						return true;
				} catch (ArrayIndexOutOfBoundsException e){
				}
			}
		}
		return false;
	}
}
*/