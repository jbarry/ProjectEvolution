package Interactive;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.System.out;

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
	
	public A getFst() {
		return fst;
	}
	
	public B getSnd() {
		return snd;
	}
}

