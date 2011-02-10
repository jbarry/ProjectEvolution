package Evolution;

import Interactive.Chromosome;
import Interactive.Crossable;
import Interactive.Gene;
import Interactive.Organism;
import Interactive.Pair;

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.LinkedList;
import java.lang.Double;
import java.awt.List;

import static java.lang.System.out;
import static java.lang.System.err;

/**
 * 
 *
 */
//TODO: implement in such a way that so many probability
//variables do not need to be passed to the ctor.
public class GEP {
	
	// Class variables.
	private LinkedList<Organism> orgList;
	private LinkedList<Chromosome> aChromList;
	private Random ran;
	
	//TODO: mutProb = .01
	//TODO: rotProb = .01
	//TODO: onePt = .8
	//TODO: tournProb = .75
	//TODO: mut is only used for the testing of GEP class.
	//		will have another way of inserting random mutations.
	public GEP(LinkedList <Organism>orgList,
			double tournProb,
			boolean level,
			double mutProb, 
			double rotProb,
			double onePtProb,
			double twoPtProb,
			Character mut) {
		this.orgList = orgList;
		ran = new Random();
		
		//Assess the fitness of each organism
		//Case1: Fitness will be assessed based on the
		//organism with the most amount of health left
		//receives the highest fitness.
		for(int i = 0; i < orgList.size(); i++) {
			orgList.get(i).setFitness(fitness(orgList.get(i)));
		}
		
		//TODO: another way of carrying fitness info for organisms.
		//They have no knowledge of their own fitness.
		//By removing fitness from Organism class.
		LinkedList<Pair<Organism, Double>> orgFitPairList =
			new LinkedList<Pair<Organism, Double>>();
		for(int i = 0; i < orgList.size(); i++) {
			orgFitPairList.add(new Pair<Organism,
			Double>(orgList.get(i), fitness(orgList.get(i))));
		}
		printOrgList(orgList);
		aChromList = tournament(partnerSelect(orgList), tournProb);
		printChromList(aChromList);
		rotation(aChromList, rotProb);
		printChromList(aChromList);
		mutation(aChromList, mutProb, mut);
		printChromList(aChromList);
	}
	
	/**
	* This method assigns a double representing fitness of each organism.
	* @param org - a single organism to be assessed.
	* @return a double representing the evaluated fitness of the organism.
	*/
	private double fitness(Organism org) {
		return (Double)org.getHealth();
	}
	
	/**
	 * @param partners - A LinkedList of organism Pairs.
	 * Each member of a pair will compete with the other member.
	 * @param tournProb-the probability at which either the 
	 * more fit, or the less fit individual will be chosen.
	 * @param handicap- Handicap to false means that the tournProb
	 * probability will be in favor of the most fit individuals.
	 * Handicap to true means that the prob will favor the less fit individuals.
	 * @return returns each winner of a match, as a Chromosome, in a LinkedList.
	 */
	private LinkedList<Chromosome> tournament(
			LinkedList<Pair<Organism, Organism>> partners,
			double tournProb) {
		LinkedList<Chromosome> newPop = new LinkedList<Chromosome>();
		for(int i = 0; i < partners.size(); i++) {
			if(fitness(partners.get(i).left()) < fitness(partners.get(i).right())) {
				newPop.add(partners.get(i).right().getChromosome());
			} else {
				newPop.add(partners.get(i).left().getChromosome());
			}
		}
		return newPop;
	}
	
	/**
	 * 
	 * @param generation
	 * @param prob
	 * @param mutation
	 */
	private void mutation(LinkedList<Chromosome> generation, double prob, Character mutation) {
		for(Chromosome chrom: generation) {
			if(ran.nextDouble() < prob) {
				int gene = ran.nextInt((chrom.size()));
				chrom.mutate(gene, mutation);
			}
		}
	}
	
	
	@SuppressWarnings("unused")
	private LinkedList <Pair<Chromosome, Chromosome>> onePointCrossOver(
			LinkedList<Chromosome> generation, double prob) {
	LinkedList <Pair<Chromosome, Chromosome>> pairList =
		mateSelect(generation);
	LinkedList <Pair<Chromosome, Chromosome>> result =
		new LinkedList <Pair<Chromosome, Chromosome>>();
		for(Pair<Chromosome, Chromosome> mates: pairList) {
			if(ran.nextDouble() < prob) {
				result.add(mates.left().crossOver(mates.right()));
			}
		}
		//TODO: correct the return.
		return result;
	}
	
	
	@SuppressWarnings("unused")
	private void twoPointCrossOver(
			LinkedList <Pair<Chromosome, Chromosome>> generation, double prob) {
	LinkedList <Pair<Chromosome, Chromosome>> pairList =
		new LinkedList <Pair<Chromosome, Chromosome>>();
		for(Pair<Chromosome, Chromosome> mates: pairList) {
			if(ran.nextDouble() < prob) {
				mates.left().crossOver(mates.right());
			}
		}
	}
	
	//Pairs up indiv from the chromosome list parameter and 
	//makes them into Pair objects. Puts Pairs into a LinkedList.
	//TODO: Make mate select an efficient symmetries on n.
	private LinkedList <Pair<Chromosome, Chromosome>> mateSelect(
		LinkedList<Chromosome> generation) {
		
		LinkedList<Chromosome> selection = generation;
		LinkedList<Pair<Chromosome, Chromosome>> pairList =
			new LinkedList<Pair<Chromosome, Chromosome>>();
		
		for(int i = 0; i < generation.size(); i++) {
			if(selection.size() == 0) {
				selection = generation;
			}
			int mate = ran.nextInt(selection.size());
			Pair<Chromosome, Chromosome> mates =
				new Pair<Chromosome, Chromosome>(
			generation.get(i), selection.get(mate));
			pairList.set(i, mates);
			selection.remove(i);
			selection.remove(mate);
		}
		return pairList;
	}
	
	/**
	 *Pairs up indiv from the Organism Pair list parameter and 
	 *makes them into Pair objects. Puts Pairs into a LinkedList.
	 */
	//TODO: Make mateSelect and partnerSelect the same. Either by 
	//changing the method or how the method is called.
	//TODO: Have partner select be the Symmetries on n.
	@SuppressWarnings("unchecked")
	private LinkedList <Pair<Organism, Organism>> partnerSelect(
			LinkedList<Organism> population) {
				LinkedList<Organism> selection;
				LinkedList<Pair<Organism, Organism>> pairList =
					new LinkedList<Pair<Organism, Organism>>();
				for(int i = 0; i < population.size(); i++) {
					selection = (LinkedList<Organism>) population.clone();
					selection.remove(i);
					int mate = ran.nextInt(selection.size());
					Pair<Organism, Organism> mates =
						new Pair<Organism, Organism>(
					population.get(i), selection.get(mate));
					
					pairList.add(mates);
				}
				return pairList;
		}
	
	/**
	 * Iterates through the chromList
	 * @param aChromList
	 * @param prob
	 */
	private void rotation(LinkedList<Chromosome> aChromList, double prob) {
		for(Chromosome chrom: aChromList) {
			if(ran.nextDouble() <= prob) {
				int x = ran.nextInt(chrom.size());
				chrom.rotate(x);
			}
		}
	}
	
	/**
	 * Used for testing the GEP class. Simply prints a organism list
	 * that is passed to it.
	 * @param list
	 */
	private void printOrgList(LinkedList<Organism> list) {
		for(int i = 0; i < list.size(); i++) {
			out.println("Chromosome " + i);
			Chromosome chromOne = orgList.get(i).getChromosome();
			for(int j = 0; j < chromOne.size(); j++) {
				@SuppressWarnings("rawtypes")
				Gene aGene = chromOne.getGene(j);
				for(int k = 0; k < aGene.size(); k++) {
					out.print(aGene.getSym(k).charValue() + " ");
				}
				out.println();
			}
		}
	}
	
	/**
	 * Used for testing the GEP class. Simply prints a chromosome list
	 * that is passed to it.
	 * @param list
	 */
	private void printChromList(LinkedList<Chromosome> list) {
		for(int i = 0; i < list.size(); i++) {
			Chromosome chrom = list.get(i);
			out.println("Chromosome" + i);
			for(int j = 0; j < chrom.size(); j++) {
				for(int k = 0; k < chrom.size(); k++) {
					out.print(chrom.getGene(j).getSym(k).charValue() + " ");
				}
				out.println();
			}
		}
	}
	
	//TODO: Implement GEP in such a way that this is unecessary.
	//TODO: This is probably something that won't be needed if all 
	// of the changes that happen as a result of this class occur
	// within the organisms themselves.
	public LinkedList<Organism> getOrgList() {
	return orgList;
	}
	
	//This main method is for basic testing.
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		ArrayList<Character> symList = new ArrayList<Character>();
		symList.add(new Character('a'));
		symList.add(new Character('b'));
		symList.add(new Character('c'));
		symList.add(new Character('d'));
		symList.add(new Character('e'));
		symList.add(new Character('f'));
		symList.add(new Character('g'));
		symList.add(new Character('h'));
		symList.add(new Character('i'));
		symList.add(new Character('j'));
		LinkedList <Organism> org = new LinkedList<Organism>();
		Random r = new Random();
		for(int i = 0; i < 5; i++) {
			LinkedList<Gene> genes = new LinkedList<Gene>();
			for (int j = 0; j < 3; j++){
				LinkedList<Character> symList1 = new LinkedList<Character>();
				for(int q = 0; q < 3; q++) {
					symList1.add(new Character(symList.get(r.nextInt(symList.size()))));
				}
				genes.add(new Gene(symList1));
			}
			Chromosome chrom = new Chromosome(genes);
			org.add(new Organism((double)(r.nextDouble()*100.0), chrom));
		}
		Character mut = symList.get(r.nextInt(symList.size()));
		 @SuppressWarnings("unused")
		GEP gep = new GEP(org, 1.00, true, 1.00, 1.00, 1.00, 1.00, mut);
		// org = gep.getOrgList();
	}
	
	//Used for debugging. Prints the line number.
	public static int getLineNumber() {
	    return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
}