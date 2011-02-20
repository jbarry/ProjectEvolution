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
	public void setSymValue(int index, double sym) {
		symList.set(index, sym);
	}

	public void setGene(LinkedList<Character> aSymList) {
		symList = aSymList;
	}

	@Override
	public Pair<Gene<A>, Gene<A>> crossOver(Gene<A> other) {
		int crossPoint = ran.nextInt(size());
		if(crossPoint == 0) {
			crossPoint++;
		}
		List<Character> firstHalf1 = subListCharCopy(0, crossPoint);
		List<Character> secHalf2 = other.subListCharCopy(crossPoint, other.size());
		firstHalf1.addAll(secHalf2);
		symList = firstHalf1;
		List<Character> secHalf1 = subListCharCopy(crossPoint, size());
		List<Character> firstHalf2 = other.subListCharCopy(0, crossPoint);
		firstHalf2.addAll(secHalf1);
		other.setSymList(firstHalf2);
		return new Pair<Gene<A>, Gene<A>>(this, other);
	}

	private List<Character> subListChar(int x, int y) {
		return symList.subList(x, y);
	}
	
	private List<Character> subListCharCopy(int x, int y) {
		LinkedList<Character> sListCop = new LinkedList<Character>();
		for(int i = 0; i < (y-x); i++) {
			sListCop.add(symList.get(i));
		}
		return sListCop;
	}

	public void setSymList(List<Character> firstHalf2) {
		symList = firstHalf2;
	}
	public Character dequeue() {
		return symList.remove(0);
	}

	public void queue(Character e) {
		symList.add(e);
	}
}

