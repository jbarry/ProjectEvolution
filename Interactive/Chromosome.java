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

	//Default ctor.
	public Chromosome() {
		chromosome = new LinkedList<Gene>();
		ran = new Random();
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
		int crossPoint = ran.nextInt(this.size());
		//Generate two sublists of the current chromosome
		//at the crossover point.
		List<Gene> child1 = (List<Gene>) chromosome.subList(0, crossPoint);
		List<Gene> child2 = (List<Gene>) other.subListGene(
				crossPoint, other.size());
		//Call Crossover on the current chromosomes 
		//gene at crossPoint with the other chromosomes gene
		//at the same crossPoint.
		Pair<Gene, Gene> crossedGenes = getGene(crossPoint).crossOver(
				other.getGene(crossPoint));
		child1.set(crossPoint, crossedGenes.left());
		child2.set(crossPoint, crossedGenes.right());
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

