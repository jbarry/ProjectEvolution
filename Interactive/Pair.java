package Interactive;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.System.out;

public class Pair<A, B> {
	
	public static void main(String[] args) {
		//case1: 
		Pair<Integer, Integer> aPair =
			new Pair<Integer, Integer>(7, 4);
		ArrayList<Pair<Integer, Integer>> aList1 =
			new ArrayList<Pair<Integer, Integer>>();
		ArrayList<Pair<Integer, Integer>> aList2 =
			new ArrayList<Pair<Integer, Integer>>();
		aList1.add(aPair);
		aList2.add(aList1.get(0));
		out.println(aList1.get(0).getFst() +
				" " + aList1.get(0).getSnd());
		out.println(aList2.get(0).getFst() +
				" " + aList2.get(0).getSnd());
		aList1.get(0).setLeft(5);
		out.println(aList1.get(0).getFst() +
				" " + aList1.get(0).getSnd());
		out.println(aList2.get(0).getFst() +
				" " + aList2.get(0).getSnd());
		
		//case2.
		aList2.get(0).setRight(6);
		out.println(aList1.get(0).getFst() +
				" " + aList1.get(0).getSnd());
		out.println(aList2.get(0).getFst() +
				" " + aList2.get(0).getSnd());
		
		//case2.
		ArrayList<Pair<Integer, Integer>> aList3 =
			(ArrayList<Pair<Integer, Integer>>) aList2.clone();
		out.print("list3 ");
		out.println(aList3.get(0).getFst() +
				" " + aList3.get(0).getSnd());
		aList2.get(0).setRight(10);
		aList2.get(0).setLeft(3);
		out.print("list2 ");
		out.println(aList2.get(0).getFst() +
				" " + aList2.get(0).getSnd());
		out.print("list3 ");
		out.println(aList3.get(0).getFst() +
				" " + aList3.get(0).getSnd());
	}
	
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
	
	public A getFst() {
		return fst;
	}
	
	public B getSnd() {
		return snd;
	}
}

