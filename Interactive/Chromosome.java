package Interactive;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Chromosome extends Genetic implements Crossable<Chromosome> {

	private List<Gene> chromosome;
	private Random ran;
	public static final int EAT = 2;
	private int id; // For testing only.
	
	// Default ctor.
	public Chromosome(int numGenes) {
		ran = new Random();
		chromosome = new LinkedList<Gene>();
		for (int i = 0; i < numGenes; i++)
			chromosome.add(new Gene(true, 30));
	}
	
	// For testing purposes of the GEP class.
	public Chromosome(LinkedList<Gene> aChrom) {
		chromosome = aChrom;
		ran = new Random();
	}

	// TODO: Make specific ctor.
	public Chromosome(int numGenes, int anId) {
		ran = new Random();
		id = anId;
		chromosome = new LinkedList<Gene>();
		for (int i = 0; i < numGenes; i++)
			chromosome.add(new Gene(true, 30));
	}

	// For GEP testing only.
	public Chromosome(int numGenes, int anId, boolean so) {
		ran = new Random();
		id = anId;
		chromosome = new LinkedList<Gene>();
		for (int i = 0; i < numGenes; i++)
			chromosome.add(new Gene(true, 7));
	}
	public void rotate(int gene) {
		@SuppressWarnings("rawtypes")
		Gene theGene = (Gene)chromosome.get(gene);
		int rotNum = ran.nextInt(theGene.size());
		while (rotNum == 0)
			rotNum = ran.nextInt(theGene.size());
		for(int i = 0; i < rotNum; i++)
			theGene.queue(theGene.dequeue());
	}

	public void mutate() {
		getGene(ran.nextInt(size())).mutate();
	}
	
	@Override
	public Pair<Chromosome, Chromosome> crossOver(Chromosome other) {
		// Define the point where the crossover will occur.
		int crossPoint = ran.nextInt(size());
		// If there are more than one Genes in the Chromosome.
		if (size() > 1) {
			while (crossPoint == 0)
				crossPoint = ran.nextInt(size());
			// Generate two sublists for each chromosome.
			// Splitting them into their respective halves.
			List<Gene> fstThis = subListGeneCopy(0, crossPoint);
			List<Gene> secThis = subListGeneCopy(crossPoint, size());
			List<Gene> fstOther = other.subListGeneCopy(0, crossPoint);
			List<Gene> secOther = other.subListGeneCopy(crossPoint,
					other.size());
			// combine first part of this to second part of other.
			fstThis.addAll(secOther);
			chromosome = fstThis;
			// first part of other with the second part of this.
			fstOther.addAll(secThis);
			other.chromosome = fstOther;
			// Call crossover on the genes at the crossPoint.
			Pair<Gene, Gene> crossedGenes = getGene(crossPoint).crossOver(
					other.getGene(crossPoint));
			// Set the index of the crossed over chromosomes
			// to the crossed over genes.
			setGene(crossPoint, crossedGenes.left());
			other.setGene(crossPoint, crossedGenes.right());
			return new Pair<Chromosome, Chromosome>(this, other);
		} else { // There is 1 Gene or not Genes.
			Pair<Gene, Gene> crossedGenes = getGene(0).crossOver(
					other.getGene(0));
			// set the index of the crossed over chromosomes
			// to the crossed over genes.
			setGene(crossPoint, crossedGenes.left());
			other.setGene(crossPoint, crossedGenes.right());
			return new Pair<Chromosome, Chromosome>(this, other);
		}
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

	// For testing GEP.
	public int getId() {
		return id;
	}
}