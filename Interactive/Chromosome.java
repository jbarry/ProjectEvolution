package Interactive;

import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Collections;
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
		//Sublist From beginning to crossPoint of this chromosome.
		List<Gene> child1 = subListGeneCopy(0, crossPoint);
		//Sublist from crossPoing to end of the partner chromosome.
		List<Gene> child2 = other.subListGeneCopy(crossPoint, other.size());
		//combine first part of this to second part of other.
		child1.addAll(child2);
		setChrom(child1);
		List<Gene> tempChild1 = subListGeneCopy(crossPoint, size());
		List<Gene> tempChild2 = other.subListGeneCopy(0, crossPoint);
		//combine first part of other to second part of this.
		tempChild2.addAll(tempChild1);
		other.setChrom(tempChild2);
		tempChild1 = child1;
		Pair<Gene, Gene> crossedGenes = 
				getGene(crossPoint).crossOver(other.getGene(crossPoint));
		tempChild1.set(crossPoint, crossedGenes.left());
		tempChild2.set(crossPoint, crossedGenes.right());
		chromosome = child1;
		other.chromosome = tempChild2;
		return new Pair<Chromosome, Chromosome>(this, other);
	}
	
	/**
	 * Returns a copy of a sublist of genes made from 
	 * the chromosome instance variable.
	 * @param x
	 * @param y
	 * @return
	 */
	private LinkedList<Gene> subListGeneCopy(int x, int y) {
		LinkedList<Gene> sListCop = new LinkedList<Gene>();
		for(int i = 0; i < (y-x); i++) {
			sListCop.add(chromosome.get(i));
		}
		return sListCop;
	}

//	@Override
//	public Pair<Chromosome, Chromosome> crossOver(Chromosome other) {
//		int crossPoint = ran.nextInt(size());
//		LinkedList<Gene> chromTemp = 
//			chromosome;
//		Collections.copy(chromTemp, chromosome);
//		List<Gene> child1 =
//			(List<Gene>) chromTemp.subList(0, crossPoint);
//		List<Gene> sndThis =
//			(List<Gene>) chromTemp.subList(0, crossPoint);
//		List<Gene> chromTemp2 = 
//			new LinkedList<Gene>();
//		Collections.copy(chromTemp2, other.chromosome);
//		List<Gene> child2 =
//			(List<Gene>) chromTemp2.subList(0, crossPoint);
//		List<Gene> sndOther =
//			(List<Gene>) chromTemp2.subList(crossPoint, other.size());
////		child1 = (LinkedList<Gene>) child1.clone();
////		sndThis = (LinkedList<Gene>) sndThis.clone();
////		child2 = (LinkedList<Gene>) child2.clone();
////		sndOther = (LinkedList<Gene>) sndOther.clone();
//		child1.addAll(sndOther);
//		child2.addAll(sndThis);
//		Pair<Gene, Gene> crossedGenes = 
//			getGene(crossPoint).crossOver(other.getGene(crossPoint));
//		child1.set(crossPoint, crossedGenes.left());
//		child2.set(crossPoint, crossedGenes.right());
//		setChrom(child1);
//		other.setChrom(child2);
//		return new Pair<Chromosome, Chromosome>(this, other);
//	}
	
	public void setChrom(List<Gene> aChrom) {
		chromosome = aChrom;
	}
	public List<Gene> subListGene(int x, int y) {
		return chromosome.subList(x, y);
	}
	public Gene getGene(int index) {
		return (Gene) chromosome.get(index);
	}

	public int size() {
		return chromosome.size();
	}
}