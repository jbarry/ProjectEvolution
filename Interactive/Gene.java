package Interactive;

import java.lang.Character;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import static java.lang.System.out;
import static java.lang.System.err;
import Evaluation.Eval;
import Evaluation.Expr;

public class Gene<A extends Crossable> extends Genetic implements Crossable<Gene<A>> {

	private List<Character> symList;
	private Random ran;
	private List<Character> terminals;
	private List<Character> nonTerminals;
	private List<Character> symbols;
	private int lenGenes;
	private Expr evaledList;
	
	//Possible variable meanings:
	//Amount of health left.
	//Number of organisms around food.
	//distance to food.
	//Amount of food left in food source.
	//Rate at which health is decreasing.
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
		terminals.add('a');
		terminals.add('b');
		terminals.add('c');
		terminals.add('d');
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
		evaledList = Eval.evaluation(makeStringArray());
	}
	
	//Ctor that does not inform the init of the genes.
	public Gene(boolean boo, int aLenGenes) {
		lenGenes = aLenGenes;
		symList = new LinkedList<Character>();
		ran = new Random();
		symbols = new LinkedList<Character>();
		symbols.add('*');
		symbols.add('/');
		symbols.add('-');
		symbols.add('+');
		symbols.add('a');
		symbols.add('b');
		symbols.add('c');
		symbols.add('d');
		terminals.add('a');
		terminals.add('b');
		terminals.add('c');
		terminals.add('d');
		Collections.shuffle(symbols);
		ran = new Random();
		for (int i = 0; i < aLenGenes; i++) 
			symList.add(symbols.get(ran.nextInt(symList.size())));
		//Ensure that there is always at least one variable
		//in the Gene.
		int variable = 0;
		for (int i = 0; i < aLenGenes; i++) 
			if(symList.get(i).equals("a")) variable++;
		if (variable == 0) 
			symList.set(ran.nextInt(symList.size()),
				terminals.get(ran.nextInt(terminals.size())));
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

	public Expr getEvaledList() {
		return evaledList;
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
		//Define the point where the crossover will occur.
		int crossPoint = ran.nextInt(size());
		while(crossPoint == 0) {
			crossPoint = ran.nextInt(size());
		}
		//Generate two sublists for each Gene.
		//Splitting them into their respective halves.
		List<Character> fstThis = subListCharCopy(0, crossPoint);
		List<Character> secThis = subListCharCopy(crossPoint, size());
		List<Character> fstOther = other.subListCharCopy(0, crossPoint);
		List<Character> secOther = other.subListCharCopy(crossPoint, other.size());
		fstThis.addAll(secOther);
		fstOther.addAll(secThis);
		//set the symbol lists to the new crossed over genes.
		symList = fstThis;
		other.symList = fstOther;
		return new Pair<Gene<A>, Gene<A>>(this, other);
	}

	private List<Character> subListChar(int x, int y) {
		return symList.subList(x, y);
	}
	
	private void printSymList(List<Character> aSymList) {
		for(int i = 0; i < aSymList.size(); i++) {
			out.print(aSymList.get(i).charValue());
		}
		out.println();
	}
	
	private List<Character> subListCharCopy(int x, int y) {
		LinkedList<Character> sListCop = new LinkedList<Character>();
		for(int i = x; i < y; i++) {
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

