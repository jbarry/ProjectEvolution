package Interactive;

public class Pair<A, B> {
	  A fst;
	  A snd;
	  public Pair(A a, A b) {
	    fst = a;
	    snd = b;
	  }
	  public A left() {return fst;}
	  public A right() {return snd;}
	}