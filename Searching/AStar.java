package Searching;

import java.util.ArrayList;

import Frame.Coordinate;
import static java.lang.System.out;
public class AStar {
	
	public AStar() {
		
	}
	
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
	}
	
	public Coordinate search(Coordinate start, Coordinate end) {
		ArrayList<Coordinate> openList = new ArrayList<Coordinate>();
		//open list priority should be organized based on shortest distance.
		//for each surrounding position.
		//assign priority.
		start.setPriority(distance(start, end));
//		openList.add();// could make own priority queue data type.
		while (openList.size() != 0) {
			//open list = node with lowest cost.
			// if(current node = goal node)
				//path complete
			//else
				//move current node to the closed list
				//examine each node adjacent to the current node
				//for each adjacent node
					//if it isn't on the open list
					//and isn't on the closed list
					//and it isn't an obstacle
					//move it to open list.
		}
		return new Coordinate();
	}
	
	/**
	 * Calculates the priority of a given coordinate start o
	 * the coordinate end. Priority is based on the distance of
	 * a straight line between the two points.
	 * @param start
	 * @param end
	 * @return
	 */
	//TODO: make private.
	public double distance(Coordinate start, Coordinate end) {
		double x1 = start.getX();
		double y1 = start.getY();
		double x2 = end.getX();
		double y2 = end.getY();
		return Math.sqrt(Math.pow((x2 - x1), 2) +
				Math.pow((y2 - y1), 2));
	}
}
