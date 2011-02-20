package Interactive;

import java.lang.Character;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;

import static java.lang.System.out;
import static java.lang.System.err;

public class Gene<A extends Crossable> extends Genetic implements Crossable<Gene<A>> {

	private List<Character> symList;
	private Random ran;
	private List<Character> terminals;
	private List<Character> nonTerminals;
	private int lenGenes;
	//Possible variable meanings:
	//x.Amount of health left
	//y.Number of organisms around food
	//z.distance to food
	//w.Amount of food left in food source.
	public Gene(int aLenGenes) {
		lenGenes = aLenGenes;
		symList = new LinkedList<Character>();
		ran = new Random();
		terminals = new LinkedList<Character>();
		nonTerminals = new LinkedList<Character>();
		nonTerminals.add('*');
		nonTerminals.add('/');
		nonTerminals.add('-');
		nonTerminals.add('+');
		terminals.add('x');
		terminals.add('y');
		terminals.add('z');
		terminals.add('w');
		ran = new Random();
		ArrayList<Integer> indexChoices = new ArrayList<Integer>();
		Character[] finiteList = new Character[lenGenes];
		for (int i = 0; i < lenGenes; i++) {
			indexChoices.add(i);
		}
		for(int i = 0; i < terminals.size(); i++) {
			int nextRan = ran.nextInt(indexChoices.size());
			finiteList[indexChoices.remove(nextRan)]
			           = terminals.get(ran.nextInt(terminals.size()));
		}
		while (!indexChoices.isEmpty()) {
			int nextRan = ran.nextInt(indexChoices.size());
			finiteList[indexChoices.remove(nextRan)]
			           = nonTerminals.get(ran.nextInt(nonTerminals.size()));
		}
		for(int i = 0; i < finiteList.length; i++) {
			symList.add(finiteList[i]);
		}
	}

	public Gene(LinkedList<Character> aSymList) {
		symList = aSymList;
		ran = new Random();
	}

	public int size() {
		return lenGenes;
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

	//TODO: Make the symList a String Array.
	//no need to have two separate lists.
	public ArrayList<String> makeStringArray() {
		ArrayList<String> retString = new ArrayList<String>();
		for(int i = 0; i < lenGenes; i++)
			retString.add(symList.get(i).toString());
		return retString;
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

