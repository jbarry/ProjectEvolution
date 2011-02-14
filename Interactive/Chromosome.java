package Interactive;

import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;

public class Chromosome extends Genetic implements Crossable<Chromosome> {

	private List<Character> terminals;
	private List<Character> nonTerminals;
	private List<Gene> chromosome;
	private Random ran;
	private int numGenes=4;
	private int lenGenes=4;

	//Default ctor.
	public Chromosome() {
		chromosome = new LinkedList<Gene>();
		terminals = new LinkedList<Character>();
		nonTerminals = new LinkedList<Character>();
		terminals.add('*');
		terminals.add('/');
		terminals.add('-');
		terminals.add('+');
		nonTerminals.add('a');
		nonTerminals.add('b');
		nonTerminals.add('c');
		nonTerminals.add('d');
		ran = new Random();
		for(int i=0;i<numGenes;i++){
			LinkedList<Character> toAdd= new LinkedList<Character>();
			for(int j=0;j<lenGenes;j++){
				if(ran.nextInt(2)==0){
					toAdd.add(terminals.get(ran.nextInt(terminals.size())));
				}
				else{
					toAdd.add(nonTerminals.get(ran.nextInt(nonTerminals.size())));
				}
			}
			Gene gene = new Gene(toAdd);
			chromosome.add(gene);
		}
		
	}

	//For testing purposes of the GEP class.
	public Chromosome(LinkedList<Gene> aChrom) {
		chromosome = aChrom;
		ran = new Random();
	}

	//TODO: Make specific ctor.

	public void rotate(int gene) {
		Gene theGene = (Gene)chromosome.get(gene);
		int rotNum = ran.nextInt(theGene.size());
		for(int i = 0; i < rotNum; i++) {
			theGene.queue(theGene.dequeue());
		}
	}

	public void mutate(int gene, Character mutation) {
		Gene aGene = (Gene)chromosome.get(gene);
		int changeGene = ran.nextInt(aGene.size());
		aGene.setSym(changeGene, mutation);
	}

	@Override
	public Pair<Chromosome, Chromosome> crossOver(Chromosome other) {
		//The point where the crossover will occur.
		int crossPoint = ran.nextInt(size());
		//Generate two sublists of the current chromosome
		//at the crossover point.
		List<Gene> child1 = (List<Gene>) subListGene(0, crossPoint);
		List<Gene> child2 = (List<Gene>) other.subListGene(
				crossPoint, other.size());
		child1.addAll(child2);
		List<Gene> tempChild1 = (List<Gene>) subListGene(crossPoint, size());
		List<Gene> tempChild2 = (List<Gene>) other.subListGene(0, crossPoint);
		tempChild2.addAll(tempChild1);
		child2 = tempChild2;
		//Call Crossover on the current chromosomes 
		//gene at crossPoint with the other chromosomes gene
		//at the same crossPoint.
		Pair<Gene, Gene> crossedGenes = getGene(crossPoint).crossOver(
				other.getGene(crossPoint));
		child1.set(crossPoint-1, crossedGenes.left());
		child2.set(crossPoint-1, crossedGenes.right());
		return new Pair<Chromosome, Chromosome>(this, other);
	}

	public List<Gene> subListGene(int x, int y) {
		return (List<Gene>) chromosome.subList(x, y);
	}
	public Gene getGene(int index) {
		return (Gene) chromosome.get(index);
	}

	public int size() {
		return chromosome.size();
	}
}