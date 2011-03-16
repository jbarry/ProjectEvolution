package Interactive;

import java.util.List;

public class Pair<A, B> {
	
	A fst;
	B snd;
	
	public Pair(A a, B b) {
		fst = a;
		snd = b;
	}

	public A left() {return fst;}

	public B right() {return snd;}
}

