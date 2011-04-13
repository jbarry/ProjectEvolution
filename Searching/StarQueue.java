package Searching;

import java.util.PriorityQueue;

public class StarQueue<Coordinate> {

	private PriorityQueue<Coordinate> list; 
	
	public StarQueue() {
		list = new PriorityQueue<Coordinate>();
	}
	
	public void add(Coordinate c) {
		list.add(c);
	}
	
	public Coordinate poll() {
		if (list.isEmpty())
			return null;
		return list.poll();
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public Coordinate peek() {
		return list.peek();
	}
	
	public Coordinate remove() {
		return list.remove();
	}
	
	public int size() {
		return list.size();
	}
	
	public boolean contains(Coordinate nd) {
		return list.contains(nd);
	}

	public void clear() {
		list.clear();
	}
}
