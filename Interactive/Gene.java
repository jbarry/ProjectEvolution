package Interactive;

//TODO: Gene can evaluate itself.
@SuppressWarnings("all")
public class Gene<A extends Crossable> implements Crossable<Gene<A>> {

	@Override
	public Pair<Gene<A>, Gene<A>> crossover(Gene<A> other) {
		// TODO Auto-generated method stub
		return new Pair<Gene<A>, Gene<A>>(this, other);
	}

}
