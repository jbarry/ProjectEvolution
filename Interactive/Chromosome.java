package Interactive;

import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Collections;
public class Chromosome extends Genetic implements Crossable<Chromosome> {

	private List<Gene> chromosome;
	private Random ran;
	public static final int EAT = 2;
	
	//Default ctor.
	public Chromosome(int numGenes) {
		ran = new Random();
		chromosome = new LinkedList<Gene>();
		for (int i = 0; i < numGenes; i++)
			chromosome.add(new Gene(10));
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
	
	public Pair<Chromosome, Chromosome> crossOver(Chromosome other) {
		for (int i = 0; i < chromosome.size(); i++) {
			Pair<Gene, Gene> genePair = getGene(i).crossOver(other.getGene(i));
			setGene(i, genePair.left());
			other.setGene(i, genePair.right());
		}
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
		for(int i = x; i < y; i++) {
			sListCop.add(getGene(i));
		}
		return sListCop;
	}
	
	public void setGene(int index, Gene aGene) {
		chromosome.set(index, aGene);
	}
	
	public Gene getGene(int index) {
		return (Gene) chromosome.get(index);
	}
	
	public void setChrom(List<Gene> aChrom) {
		chromosome = aChrom;
	}
	
	public List<Gene> subListGene(int x, int y) {
		return chromosome.subList(x, y);
	}

	public int size() {
		return chromosome.size();
	}
}