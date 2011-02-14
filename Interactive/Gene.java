package Interactive;

import java.lang.Character;
import java.util.List;
import java.util.LinkedList;

public class Gene<A extends Crossable> extends Genetic implements Crossable<Gene<A>> {

	private List<Character> symList;

	public Gene(LinkedList<Character> aSymList) {
		symList = aSymList;
	}

	public int size() {
		return symList.size();
	}

	public List<Character> getList() {
		return symList;
	}

	public Character getSym(int index) {
		return symList.get(index);
	}

	public void setSym(int index, Character sym) {
		symList.set(index, sym);
	}

	public void setGene(LinkedList<Character> aSymList) {
		symList = aSymList;
	}

	@Override
	public Pair<Gene<A>, Gene<A>> crossOver(Gene<A> other) {

		return new Pair<Gene<A>, Gene<A>>(other, other);
	}

	public Character dequeue() {
		return symList.remove(0);
	}

	public void queue(Character e) {
		symList.add(e);
	}
}

