package Interactive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import Evaluation.Eval;
import Evaluation.Expr;

public class Gene<A extends Crossable<?>> extends Genetic implements Cloneable{

	private List<Character> symList;
	private Random ran;
	private List<Character> terminals;
	private List<Character> symbols;
	private int lenGenes;
	private Expr evaledList;

	// Ctor that does not inform the init of the genes.
	public Gene(boolean boo, int aLenGenes) {
		// TODO: make symList a static global somewhere else so that it does not
		// need to be initialized at ever ctor call.
		lenGenes = aLenGenes;
		symList = new LinkedList<Character>();
		ran = new Random();
		symbols = new LinkedList<Character>();
		terminals = new LinkedList<Character>();
		symbols.add('*');
		symbols.add('/');
		symbols.add('-');
		symbols.add('+');
		symbols.add('a');
		/*symbols.add('b');*/
		symbols.add('c');
		symbols.add('d');
		symbols.add('e');
		symbols.add('f');
		symbols.add('g');
		terminals.add('a'); // x distance.
		/*terminals.add('b');*/// y distance.
		terminals.add('c'); // Organisms near food source.
		terminals.add('d'); // Organism's health.
		terminals.add('e'); // Food remaining in food source.
		terminals.add('f'); // If poison = -1 normalized, otherwise 1
		// normalized.
		terminals.add('g'); // NumOrganisms surrounding this organism.
		Collections.shuffle(symbols);
		ran = new Random();
		// Construct the symList using random elt.'s from symbols.
		for (int i = 0; i < aLenGenes; i++)
			symList.add(symbols.get(ran.nextInt(symbols.size())));
		// Ensure that there is always at least one variable
		// in the Gene.
		int variable = 0;
		for (int i = 0; i < aLenGenes; i++) {
			if (symList.get(i).equals('a'))
				variable++;
			if (symList.get(i).equals('b'))
				variable++;
			if (symList.get(i).equals('c'))
				variable++;
			if (symList.get(i).equals('d'))
				variable++;
			if (symList.get(i).equals('e'))
				variable++;
			if (symList.get(i).equals('f'))
				variable++;
			if (symList.get(i).equals('g'))
				variable++;
		}
		if (variable == 0)
			symList.set(ran.nextInt(symList.size()),
					terminals.get(ran.nextInt(terminals.size())));
		// Make the makeSymbol list into a string array. otherwise it cannot
		// be
		// handled.
		// TODO: program to interfaces.
		evaledList = Eval.evaluation(makeStringArray(symList));
	}

	public Gene(LinkedList<Character> aSymList) {
		symList = aSymList;
		lenGenes = aSymList.size();
		ran = new Random();
		symbols = new LinkedList<Character>();
		terminals = new LinkedList<Character>();
		symbols.add('*');
		symbols.add('/');
		symbols.add('-');
		symbols.add('+');
		symbols.add('a');
		/*symbols.add('b');*/
		symbols.add('c');
		symbols.add('d');
		symbols.add('e');
		symbols.add('f');
		symbols.add('g');
		terminals.add('a'); // x distance.
		/*terminals.add('b');*/// y distance.
		terminals.add('c'); // Organisms near food source.
		terminals.add('d'); // Organism's health.
		terminals.add('e'); // Food remaining in food source.
		terminals.add('f'); // If poison = -1 normalized, otherwise 1
		// normalized.
		terminals.add('g'); // NumOrganisms surrounding this organism.
		Collections.shuffle(symbols);
		ran = new Random();
	}

	public int size() {
		return symList.size();
	}

	public List<Character> getSymList() {
		return symList;
	}

	public Character getSym(int index) {
		return symList.get(index);
	}

	public void setSym(int index, Character sym) {
		symList.set(index, sym);
	}

	public Expr getEvaledList() {
		return evaledList;
	}

	public void updateEvaledList() {
		evaledList = Eval.evaluation(makeStringArray(symList));
	}

	/**
	 * Turns the symList into a string array for handling.
	 * 
	 * @param aSymList
	 * @return
	 */
	public ArrayList<String> makeStringArray(List<Character> aSymList) {
		ArrayList<String> retString = new ArrayList<String>();
		for (int i = 0; i < lenGenes; i++)
			retString.add(aSymList.get(i).toString());
		return retString;
	}

	public void setGene(LinkedList<Character> aSymList) {
		symList = aSymList;
	}

	public Pair<Gene<A>, Gene<A>> crossOver(Gene<A> other) {
		// Define the point where the crossover will occur.
		int crossPoint = ran.nextInt(size());
		while (crossPoint == 0)
			crossPoint = ran.nextInt(size());
		// Generate two sublists for each Gene.
		// Splitting them into their respective halves.
		List<Character> fstThis = subListCharCopy(0, crossPoint);
		List<Character> secThis = subListCharCopy(crossPoint, size());
		List<Character> fstOther = other.subListCharCopy(0, crossPoint);
		List<Character> secOther = other.subListCharCopy(crossPoint,
				other.size());
		fstThis.addAll(secOther);
		fstOther.addAll(secThis);
		// set the symbol lists to the new crossed over genes.
		symList = fstThis;
		other.symList = fstOther;
		return new Pair<Gene<A>, Gene<A>>(this, other);
	}

	public List<Character> subListChar(int x, int y) {
		return symList.subList(x, y);
	}

	public void printSymList() {
		System.out.println("Printing symList");
		for (int i = 0; i < symList.size(); i++) {
			System.out.print(symList.get(i).charValue());
		}
		System.out.println();
	}

	private List<Character> subListCharCopy(int x, int y) {
		LinkedList<Character> sListCop = new LinkedList<Character>();
		for (int i = x; i < y; i++) {
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

	public void mutate() {
		int size2 = ran.nextInt(symbols.size());
		char mut = symbols.get(size2);
		setSym(ran.nextInt(size()), mut);
	}
	
	public Object clone() throws CloneNotSupportedException {
		Gene gene = (Gene) super.clone();
		/*gene.symList
		gene.symbols
		gene.terminals
		gene.ran*/
		return super.clone();
	}
}
