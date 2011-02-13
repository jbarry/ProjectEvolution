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
@SuppressWarnings("all")
public class GEP {

	// Class variables.
	private LinkedList<Organism> orgList;
	private LinkedList<Chromosome> aChromList;
	private Random ran;

	double tournProb;
	boolean level;
	double mutProb;
	double rotProb;
	double onePtProb;
	double twoPtProb;
	//TODO: will have another way of inserting random mutations.
	public GEP(LinkedList <Organism>orgList,
			double aTournProb,
			boolean aLevel,
			double aMutProb, 
			double aRotProb,
			double aOnePtProb,
			double aTwoPtProb,
			Character mut) {

		this.orgList = orgList;
		tournProb = aTournProb;
		level = aLevel;
		mutProb = aMutProb;
		rotProb = aRotProb;
		onePtProb = aOnePtProb;
		twoPtProb = aTwoPtProb;
//		TODO: handicap = aHandicap;
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
//		LinkedList<Pair<Organism, Double>> orgFitPairList =
//			new LinkedList<Pair<Organism, Double>>();
//		for(int i = 0; i < orgList.size(); i++) {
//			orgFitPairList.add(new Pair<Organism,
//					Double>(orgList.get(i), fitness(orgList.get(i))));
//		}
//		printOrgList(orgList);
		aChromList = tournament(partnerSelect(orgList));
//		printChromList(aChromList);
		rotation(aChromList);
//		printChromList(aChromList);
		mutation(aChromList, mut);
		printChromList(aChromList);
		twoPointCrossOver(onePointCrossOver(aChromList, onePtProb), twoPtProb);
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
			LinkedList<Pair<Organism, Organism>> partners) {
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
	 * Iterates through the chromList
	 * @param aChromList
	 * @param prob
	 */
	private void rotation(LinkedList<Chromosome> aChromList) {
		for(Chromosome chrom: aChromList) {
			if(ran.nextDouble() <= rotProb) {
				int x = ran.nextInt(chrom.size());
				chrom.rotate(x);
			}
		}
	}

	/**
	 * 
	 * @param generation
	 * @param prob
	 * @param mutation
	 */
	private void mutation(LinkedList<Chromosome> generation, Character mutation) {
		for(Chromosome chrom: generation) {
			if(ran.nextDouble() < mutProb) {
				int gene = ran.nextInt((chrom.size()));
				chrom.mutate(gene, mutation);
			}
		}
	}


	
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

		LinkedList<Chromosome> selection;
		LinkedList<Pair<Chromosome, Chromosome>> pairList =
			new LinkedList<Pair<Chromosome, Chromosome>>();

		for(int i = 0; i < generation.size(); i++) {
			selection = (LinkedList<Chromosome>) generation.clone();
			selection.remove(i);
			int mate = ran.nextInt(selection.size());
			Pair<Chromosome, Chromosome> mates =
				new Pair<Chromosome, Chromosome>(
						generation.get(i), selection.get(mate));
			pairList.add(mates);
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
	 * Used for testing the GEP class. Simply prints a organism list
	 * that is passed to it.
	 * @param list
	 */
	private void printOrgList(LinkedList<Organism> list) {
		for(int i = 0; i < list.size(); i++) {
			out.println("Chromosome " + i);
			Chromosome chromOne = orgList.get(i).getChromosome();
			for(int j = 0; j < chromOne.size(); j++) {
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

	//Used for debugging. Prints the line number.
	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
}