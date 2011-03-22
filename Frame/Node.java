package Frame;

import java.util.Comparator;

public class Node extends Coordinate implements Comparator<Node>{
		private double priority;
		
		public Node(int aPriority, Coordinate c){
			super(c);
			priority = aPriority;
		}
		public double getPriority() {
			return priority;
		}
		
		public void setPriority(double aPriority) {
			priority = aPriority;
		}
		@Override 
		public int compare(Node fst, Node sec) {
			if (fst.getPriority() > sec.getPriority())
				return 1;
			else if(fst.getPriority() == sec.getPriority())
				return 0;
			else
				return -1;
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
	}