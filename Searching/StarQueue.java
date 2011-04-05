package Searching;

import java.util.Comparator;
import java.util.PriorityQueue;

public class StarQueue<Node> {

	private PriorityQueue<Node> list; 
	
	public StarQueue(int startSize) {
		list = new PriorityQueue<Node>(startSize,
				(Comparator<? super Node>) new CoordinateComparator());
	}
	
	public void add(Node c) {
		list.add(c);
	}
	
	public Node poll() {
		return list.poll();
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public Node peek() {
		return list.peek();
	}
	
	public Node remove() {
		return list.remove();
	}
	
	public int size() {
		return list.size();
	}
	
	public boolean contains(Node nd) {
		return list.contains(nd);
	}
}
