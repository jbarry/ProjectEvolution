package Frame;

import java.util.ArrayList;
import java.util.Comparator;
import Frame.getSurroundingObjects;
import Interactive.Organism;
import Searching.AStar;

public class Node extends Coordinate implements Comparable<Node>{
		private double priority;
		
		public Node(double aPriority, Coordinate c){
			super(c);
			priority = aPriority;
		}
		public Node(int x, int y, double aPriority){
			super(new Coordinate(x, y));
			priority = aPriority;
		}
		public double getPriority() {
			return priority;
		}
		
		public void setPriority(double aPriority) {
			priority = aPriority;
		}
		@Override
		public int getX() {
			return x;
		}
		@Override
		public int getY() {
			return y;
		}
		@Override
		public void setX(int x){
			if(x >= GridPanel.WIDTH){
				this.x = GridPanel.WIDTH -1;
			}
			else if(x < 0){
				this.x = 0;
			}
			else{
				this.x = x;
			}
		}
		@Override
		public void setY(int y){
			if(y >= GridPanel.HEIGHT){
				this.y = GridPanel.HEIGHT - 1;
			}
			else if(y < 0){
				this.y = 0;
			}
			else{
				this.y = y;
			}
		}
		
		//This compareTo accounts for only two cases;
		//case 1: Nodes are equal -> return 0.
		//case 2: Nodes are not equal -> return -1.
		@Override
		public int compareTo(Node other) {
			if (x == other.getX() && y == other.getY())
				return 0;
			else return -1;
		}
		
		public ArrayList<Coordinate> getAdjacent() {
			ArrayList<Coordinate> adj = new ArrayList<Coordinate>();
			adj.add(new Coordinate(x - Organism.WIDTH, y - Organism.HEIGHT));
			return new ArrayList<Coordinate>();
		}
		
		public boolean hasObstacle() {
			//TODO
			return false;
		}
	}