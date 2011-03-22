package Searching;

import Frame.Coordinate;
public class Node {
	
	private double priority;
	private Coordinate pos;
	
	public Node(Coordinate aPos, double aPriority) {
		pos = aPos;
		priority = aPriority;
	}
}
