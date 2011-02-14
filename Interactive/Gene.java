package Interactive;

import java.lang.Character;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
public class Gene<A extends Crossable> extends Genetic implements Crossable<Gene<A>> {

	private List<Character> symList;
	private Random ran;

	public Gene(LinkedList<Character> aSymList) {
		symList = aSymList;
		ran = new Random();
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
		int crossPoint = ran.nextInt(size());
		List<Character> firstHalf1 = subListChar(0, crossPoint);
		List<Character> secHalf1 = subListChar(crossPoint, size());
		List<Character> firstHalf2 = other.subListChar(0, crossPoint);
		List<Character> secHalf2 = other.subListChar(crossPoint, other.size());
		firstHalf1.addAll(secHalf2);
		firstHalf2.addAll(secHalf1);
		symList = firstHalf1;
		//TODO: make setSym().
		other.symList = firstHalf2;
		return new Pair<Gene<A>, Gene<A>>(this, other);
	}

	private List<Character> subListChar(int x, int y) {
		return symList.subList(x, y);
	}

	public Character dequeue() {
		return symList.remove(0);
	}

	public void queue(Character e) {
		symList.add(e);
	}
}

