package Interactive;

import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;
@SuppressWarnings("all")
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
		int crossPoint = ran.nextInt(this.size());
		LinkedList<Gene> child1 = (LinkedList<Gene>) chromosome.subList(0, crossPoint);
		LinkedList<Gene> child2 = (LinkedList<Gene>) other.subListGene(crossPoint, other.size());
		Gene g = (Gene)this.getGene(crossPoint);
		g.crossOver(other.getGene(crossPoint));
		Pair<Gene, Gene> crossedGenes = getGene(crossPoint).crossOver(other.getGene(crossPoint));
		child1.set(crossPoint, crossedGenes.left());
		child2.set(crossPoint, crossedGenes.right());
		return new Pair<Chromosome, Chromosome>(this, other);
	}

	public LinkedList<Gene> subListGene(int x, int y) {
		return (LinkedList<Gene>) chromosome.subList(x, y);
	}
	public Gene getGene(int index) {
		return (Gene) chromosome.get(index);
	}

	public int size() {
		return chromosome.size();
	}
}

