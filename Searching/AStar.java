package Searching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import Frame.Coordinate;
import Frame.GridPanel;
import Frame.Node;
import Interactive.Organism;
import Interactive.Pair;
import Searching.StarQueue;

import static java.lang.System.out;

public class AStar {

	public static void main(String[] args) {
		AStar a = new AStar();
		Coordinate coord1 = new Coordinate();
		coord1.setX(2);
		coord1.setY(5);
		Coordinate coord2 = new Coordinate();
		coord2.setX(2);
		coord2.setY(5);
		//Test getPriority method.
		//Test case where distance should be zero.
		out.println(a.distance(coord1, coord2));
		coord1.setX(2);
		coord1.setY(5);
		coord2.setX(4);
		coord2.setY(6);
		out.println(a.distance(coord1, coord2));
		Node nd = (Node) coord1.spawnNode(0);
		out.println("node x: " + nd.getX());
		out.println("node x: " + nd.getY());
		nd.setX(13);
		nd.setY(14);
		out.println("node x: " + nd.getX());
		out.println("node x: " + nd.getY());
	}

	public static Coordinate search(Coordinate start, Coordinate end) {
		//openList is a priority queue organized based
		//on shortest distance to end node.
		StarQueue<Coordinate> openList = new StarQueue<Coordinate>(0);
		HashSet<Node> closedSet = new HashSet<Node>();
		Node endNode = (Node) end.spawnNode(0);
		//for each surrounding position.
		//assign priority, then add to queue.
		openList.add((Node) start.spawnNode(distance(start, end)));
		while (openList.size() != 0) {
			Node current = (Node) openList.remove();
			if (current.compareTo(endNode) == 0) {

			} else {
				closedSet.add(current);
				for (Coordinate c: current.getAdjacent()) {
					Node nd = (Node) c.spawnNode(distance(c, end));
					if(!openList.contains(nd) && !closedSet.contains(nd) &&
							!nd.hasObstacle())
						openList.add(nd);
				}
			}
		}
		return new Coordinate();
	}

	public Coordinate search2(Coordinate start, Coordinate end) {
		//openList is a priority queue organized based
		//on shortest distance to end node.
		StarQueue<Coordinate> openList = new StarQueue<Coordinate>(0);
		Node endNode = (Node) end.spawnNode(0);
		//for each surrounding position.
		//assign priority, then add to queue.
		Node current = (Node) start.spawnNode(distance(start, end));
		ArrayList<Nodes> adjacent =
			adjacentNodes(current.getX(), current.getY());
		if(adjacent != null) {
			for (Nodes nd: adjacent) {
				if(nd.compareTo(endNode) == 0)
					return start;
				else if(!nd.hasObstacle())
					openList.add(nd);
				return new Coordinate();
			}
		}
		return start;
	}

	/**
	 * Only adds the adjacent nodes that don't have obstacles in them.
	 * @return
	 */
	public ArrayList<Node> adjacentNodes(int x, int y, Node end) {
		ArrayList<Node> adj = new ArrayList<Node>();
		int moveWidth = (2*Organism.WIDTH);
		int moveHeight = (2*Organism.HEIGHT);
		int ex = end.getX();
		int ey = end.getY();
		//W
		if(!hasObstacle(x - moveWidth, y))
			adj.add(new Node(x - moveWidth, y, 
					distance(x - moveWidth, y, ex, ey)));
		//NW
		if(!hasObstacle(x - moveWidth, y + moveHeight))
			adj.add(new Node(x - moveWidth, y + moveHeight, 
					distance(x - moveWidth, y + moveHeight, ex, ey)));
		//N
		if(!hasObstacle(x, y + moveHeight))
			adj.add(new Node(x, y + moveHeight, 
					distance(x, y + moveHeight, ex, ey)));
		//NE
		if(!hasObstacle(x + moveWidth, y + moveHeight))
			adj.add(new Node(x + moveWidth, y + moveHeight, 
					distance(x + moveWidth, y + moveHeight, ex, ey)));
		//E
		if(!hasObstacle(x + moveWidth, y))
			adj.add(new Node(x + moveWidth, y, 
					distance(x + moveWidth, y, ex, ey)));
		//SE
		if(!hasObstacle(x + moveWidth, y - moveHeight))
			adj.add(new Node(x + moveWidth, y - moveHeight, 
					distance(x + moveWidth, y - moveHeight, ex, ey)));
		//S
		if(!hasObstacle(x, y - moveHeight))
			adj.add(new Node(x, y - moveHeight, 
					distance(x, y - moveHeight, ex, ey)));
		//SW
		if(!hasObstacle(x - moveWidth, y - moveHeight))
			adj.add(new Node(x - moveWidth, y - moveHeight, 
					distance(x - moveWidth, y - moveHeight, ex, ey)));
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
	//TODO: make private.
	public static double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow((x2 - x1), 2) +
				Math.pow((y2 - y1), 2));
	}

	public boolean hasObstacle(int x, int y) {
		HashMap<String, ArrayList<Integer>> obstacles =
			objectsInSpace(x, y);
		if(obstacles.get("org") != null ||
				obstacles.get("hFood") != null ||
				obstacles.get("pFood") != null)
			return true;
		return false;
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
		checkObstacles: for(int i = x - Organism.WIDTH/2; i <= x + Organism.WIDTH/2; i++){
			for(int j = y - Organism.HEIGHT/2; j <= y + Organism.HEIGHT/2; j++){
				try{	
					Pair<Integer, Character> object = GridPanel.locationMap[i][j];
					//count all occurrences of objects in location map
					if(object.getSnd() == 'w') 
						continue checkObstacles;
					if(object.getSnd() == 'p')
						pois.add(object.getFst());
					if(object.getSnd() == 'h')
						heal.add(object.getFst());
					if(object.getSnd() == 'o')
						orgs.add(object.getFst());
				}
				catch(ArrayIndexOutOfBoundsException e){
				}
			}
		}
		HashMap<String, ArrayList<Integer>> obstacles = 
			new HashMap<String, ArrayList<Integer>>();
		if(orgs.size() > 0)
			obstacles.put("org", new ArrayList<Integer>(orgs));
		else obstacles.put("org", null);
		if(heal.size() > 0)
			obstacles.put("hFood", new ArrayList<Integer>(heal));
		else obstacles.put("hFood", null);
		if(pois.size() > 0)
			obstacles.put("pFood", new ArrayList<Integer>(pois));
		else obstacles.put("pFood", null);
		return obstacles;
	}
}
