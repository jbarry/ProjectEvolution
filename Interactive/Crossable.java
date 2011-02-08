package Interactive;
@SuppressWarnings("all")
public interface Crossable <A extends Crossable> {
	public Pair<A, A> crossover(A other);
}
