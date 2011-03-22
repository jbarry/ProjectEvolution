package Searching;

import java.util.ArrayList;
import java.util.HashSet;

import Frame.Coordinate;
import Frame.Node;
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

	public static Coordinate search2(Coordinate start, Coordinate end) {
		//openList is a priority queue organized based
		//on shortest distance to end node.
		StarQueue<Coordinate> openList = new StarQueue<Coordinate>(0);
		Node endNode = (Node) end.spawnNode(0);
		//for each surrounding position.
		//assign priority, then add to queue.
		Node current = (Node) start.spawnNode(distance(start, end));
		for (Coordinate c: current.getAdjacent()) {
			Node nd = (Node) c.spawnNode(distance(c, end));
			if(nd.compareTo(endNode) == 0)
				return start;
			else if(!nd.hasObstacle())
				openList.add(nd);
			return new Coordinate();
		}
		return start;
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
	public static double distance(Coordinate current, Coordinate end) {
		double x1 = current.getX();
		double y1 = current.getY();
		double x2 = end.getX();
		double y2 = end.getY();
		return Math.sqrt(Math.pow((x2 - x1), 2) +
				Math.pow((y2 - y1), 2));
	}
}
