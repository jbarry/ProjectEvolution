package Interactive;

import java.util.ArrayList;

public class Pair<A, B> {
	
	private A fst;
	private B snd;

	public Pair(A a, B b) {
		fst = a;
		snd = b;
	}

	public A left() {return fst;}

	public B right() {return snd;}

	public void setLeft(A a){
		fst = a;
	}

	public void setRight(B b){
		snd = b;
	}
	
	public A getLeft() {
		return fst;
	}
	
	public B getRight() {
		return snd;
	}
	
	public ArrayList<Pair<Integer, Integer>> change1(
			ArrayList<Pair<Integer, Integer>> toChange) {
		for (Pair<Integer, Integer> p: toChange)
			p.setLeft(19);
		return toChange;
	}
	
	public static void change(ArrayList<Integer> toChange) {
		toChange.add(4);
		toChange.add(12);
		toChange.add(13);
	}
}

