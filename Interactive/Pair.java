package Interactive;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.System.out;

public class Pair<A, B> {
	
//	public static void main(String[] args) {
//		
//		Pair<Integer, Integer> aPair =
//			new Pair<Integer, Integer>(7, 4);
//		
//		ArrayList<Pair<Integer, Integer>> aList1 =
//			new ArrayList<Pair<Integer, Integer>>();
//		Random r = new Random();
//		for(int i = 0; i < 10; i++)
//			aList1.add(new Pair<Integer, Integer>(
//					r.nextInt(20), r.nextInt(20)));
//		for(int i = 0; i < 10; i++)
//			out.println("fst: " + aList1.get(i).getFst() +
//					" sec: " + aList1.get(i).getSnd());
//		out.println();
//		out.println();
//		aPair.change2(aList1);
//		out.println("after change: ");
//		out.println();
//		for(int i = 0; i < 10; i++)
//			out.println("fst: " + aList1.get(i).getFst() +
//					" sec: " + aList1.get(i).getSnd());
//		
//		List<Pair<Integer, Integer>> aList2 =
//			new ArrayList<Pair<Integer, Integer>>();
//		aList1.add(aPair);
//		aList2.add(aList1.get(0));
//		out.println(aList1.get(0).getFst() +
//				" " + aList1.get(0).getSnd());
//		out.println(aList2.get(0).getFst() +
//				" " + aList2.get(0).getSnd());
//		aList1.get(0).setLeft(5);
//		out.println(aList1.get(0).getFst() +
//				" " + aList1.get(0).getSnd());
//		out.println(aList2.get(0).getFst() +
//				" " + aList2.get(0).getSnd());
//
//		//case2.
//		aList2.get(0).setRight(6);
//		out.println(aList1.get(0).getFst() +
//				" " + aList1.get(0).getSnd());
//		out.println(aList2.get(0).getFst() +
//				" " + aList2.get(0).getSnd());
//	}
	
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
	
	public ArrayList<Pair<Integer, Integer>> change1(
			ArrayList<Pair<Integer, Integer>> toChange) {
		for (Pair<Integer, Integer> p: toChange)
			p.setLeft(19);
		return toChange;
	}
	
	public void change2(
			ArrayList<Pair<Integer, Integer>> toChange) {
		for (Pair<Integer, Integer> p: toChange)
			p.setLeft(19);
	}
}

