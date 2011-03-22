package Searching;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class StarQueue<A> {

	private LinkedList<A> list; 
	
	public StarQueue() {
		list = new LinkedList<A>();
	}
	
	public void add(A e) {
		list.add(e);
	}
	
	public A get(int i) {
		return list.get(i);
	}
}
