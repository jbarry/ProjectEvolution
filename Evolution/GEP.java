package Evolution;

import Interactive.Chromosome;
import Interactive.Crossable;
import Interactive.Gene;
import Interactive.Organism;
import Interactive.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Arrays;
import java.util.LinkedList;
import java.lang.Double;
import java.awt.List;

import static java.lang.System.out;
import static java.lang.System.err;
import java.util.HashMap;
import java.util.Collection;
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
	private LinkedList<Chromosome> chromList;
	private Random ran;

	public static double tournProb;
	public static double mutProb;
	public static double rotProb;
	public static double onePtProb;
	public static double twoPtProb;

	//TODO: will have another way of inserting random mutations.
	public GEP(LinkedList <Organism> anOrgList,
			double aTournProb,
			double aMutProb, 
			double aRotProb,
			double aOnePtProb,
			double aTwoPtProb) {

		orgList = anOrgList;
		chromList = new LinkedList<Chromosome>();
		tournProb = aTournProb;
		mutProb = aMutProb;
		rotProb = aRotProb;
		onePtProb = aOnePtProb;
		twoPtProb = aTwoPtProb;
//		TODO: handicap = aHandicap;
		ran = new Random();

//		testing.
//		makeChromList();
//		onePointCrossOver();
//		printChromList();
	}

	/**
	 * For testing
	 * @param population
	 */
	public void makeChromList() {
		chromList = new LinkedList<Chromosome>();
		for(Organism org: orgList) {
			chromList.add(org.getChromosome());
		}
	}

	public LinkedList<Organism> newGeneration() {
		tournament(partnerSelect(orgList));
		rotation();
		mutation();
		onePointCrossOver();
//		out.println("orgList size: " + orgList.size() + 
//				"\nchromList size: " + chromList.size() + "\n");
		for(int i = 0; i < orgList.size(); i++)
			orgList.get(i).setChromosome(chromList.get(i));
		return orgList;
	}

	/**
	 * This method assigns a double representing fitness of each organism.
	 * @param org - a single organism to be assessed.
	 * @return a double representing the evaluated fitness of the organism.
	 */
	//TODO: fitness function idea
	//average health throughout the generation. ie., take
	//samples of an org's health at time periods. Then take
	//average.
	//number of steps travelled. Essentially, org's that 
	//have higher health with travelling short distances are more fit.
	//
	public double fitness(Organism org) {
		if (org.getNumSteps() == 0) return 0;
		double avgHealth = org.getHlthTot()/org.getSamples();
		double fitness = avgHealth/org.getNumSteps();
//		out.println(fitness);
		org.setFitness(fitness);
		return fitness;
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
	public void tournament(
			LinkedList<Pair<Organism, Organism>> partners) {
		LinkedList<Chromosome> newPop = new LinkedList<Chromosome>();
		for(int i = 0; i < partners.size(); i++) {
			if(fitness(partners.get(i).left()) < fitness(partners.get(i).right())) {
				newPop.add(partners.get(i).right().getChromosome());
			} else {
				newPop.add(partners.get(i).left().getChromosome());
			}
		}
		chromList = newPop;
	}

	/**
	 * Iterates through the chromList
	 * @param aChromList
	 * @param prob
	 */
	public void rotation() {
		for(Chromosome chrom: chromList) {
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
	public void mutation() {
		for(Chromosome chrom: chromList) {
			if(ran.nextDouble() < mutProb)
				chrom.mutate();
		}
	}

	public void onePointCrossOver() {
		LinkedList<Pair<Chromosome, Chromosome>> pairList =
			mateSelect();
//		LinkedList<Chromosome> printList = 
//			new LinkedList<Chromosome>();
//		for (int i = 0; i < pairList.size(); i ++) {
//			printList.add(pairList.get(i).left());
//			printList.add(pairList.get(i).right());
//		}
//		for (int i = 0; i < printList.size(); i ++) {
//			Chromosome aChrom = printList.get(i);
//			out.println("Chromosome " + i);
//			for (int j = 0; j < aChrom.size(); j++) {
//				Gene aGene = aChrom.getGene(j);
//				for (int k = 0; k < aGene.size(); k++) {
//					out.print(aGene.getSym(k) + " ");
//				}
//				out.println();
//			}
//		}
		chromList.clear();
		for(int i = 0; i < pairList.size(); i++) {
			if(ran.nextDouble() < onePtProb) {
				Pair<Chromosome, Chromosome> crossed = 
					pairList.get(i).left().crossOver(pairList.get(i).right());
			}
		}
		makeChrmListFrmPair(pairList);
	}

	private void makeChrmListFrmPair(
			LinkedList<Pair<Chromosome, Chromosome>> chromPair
			) {
		chromList.clear();
		for (int i = 0; i < chromPair.size(); i++) {
			chromList.add(chromPair.get(i).left());
			chromList.add(chromPair.get(i).right());			
		}
	}
	
	//TODO: remove print statements. Only for testing.
	//Pairs up indiv from the chromosome list parameter and 
	//makes them into Pair objects. Puts Pairs into a LinkedList.
	private LinkedList <Pair<Chromosome, Chromosome>> mateSelect() {
		LinkedList<Pair<Chromosome, Chromosome>> pairList =
			new LinkedList<Pair<Chromosome, Chromosome>>();
		LinkedList<Chromosome> competitors = 
			(LinkedList<Chromosome>) chromList.clone();
		while(!competitors.isEmpty()) {
			Chromosome chrom = competitors.remove(0);
			int mate;
			Chromosome partner;
			if(competitors.size() == 0) {
				mate = ran.nextInt(chromList.size());
				chromList.remove(chrom);
				partner = chromList.get(mate);
			} else {
				mate = ran.nextInt(competitors.size());
				partner = competitors.remove(mate);
			}
			Pair<Chromosome, Chromosome> mates = 
				new Pair<Chromosome, Chromosome>(chrom, partner);
			pairList.add(mates);
		}
		return pairList; 
	}

	/**
	 *Pairs up indiv from the Organism Pair list parameter and 
	 *makes them into Pair objects. Puts Pairs into a LinkedList.
	 */
	private LinkedList <Pair<Organism, Organism>> partnerSelect(
			LinkedList<Organism> population) {
		LinkedList<Pair<Organism, Organism>> pairList =
			new LinkedList<Pair<Organism, Organism>>();
		HashMap<Organism, LinkedList<Organism>> notSeenMap =
			new HashMap<Organism, LinkedList<Organism>>();
		for(int i = 0; i < population.size(); i++) {
			Organism org = population.get(i);
			notSeenMap.put(org, 
					(LinkedList<Organism>) population.clone());
			notSeenMap.get(org).remove(i);
		}
		for(int i = 0; i < population.size(); i++) {
			Organism partner1 = population.get(i);
			LinkedList<Organism> selection = notSeenMap.get(partner1);
			int mate = ran.nextInt(selection.size());
			Organism partner2 = selection.get(mate);
			selection.remove(partner2);
			Pair<Organism, Organism> mates =
				new Pair<Organism, Organism>(partner1, partner2);
			pairList.add(mates);
		}
		return pairList;
	}

	/**
	 * Used for testing the GEP class. Simply prints a organism list
	 * that is passed to it.
	 * @param list
	 */
	public void printOrgList() {
		for(int i = 0; i < orgList.size(); i++) {
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
	 */
	public void printChromList() {
		for(int i = 0; i < chromList.size(); i++) {
			Chromosome chrom = chromList.get(i);
			out.println("Chromosome" + i);
			for(int j = 0; j < chrom.size(); j++) {
				for(int k = 0; k < chrom.getGene(j).size(); k++) {
					out.print(chrom.getGene(j).getSym(k).charValue() + " ");
				}
				out.println();
			}
		}
	}

	public LinkedList<Organism> getOrgList(){
		return orgList;
	}

	public LinkedList<Chromosome> getChromList(){
		return chromList;
	}

	public double getTournProb(){
		return tournProb;
	}
	
	public void setTournProb(double x){
		tournProb = x;
	}
	
	public double getMutProb(){
		return mutProb;
	}
	
	public void setMutProb(double x){
		mutProb = x;
	}

	public double getRotProb(){
		return rotProb;
	}
	
	public void setRotProb(double x){
		rotProb = x;
	}

	public double getOnePtProb(){
		return onePtProb;
	}
	
	public void setOnePtProb(double x){
		onePtProb = x;
	}

	public void setTwoPtProb(double x){
		twoPtProb = x;
	}

	public void setChromList(LinkedList<Chromosome> aChromList){
		chromList = aChromList;
	}

	public void setOrgList(LinkedList<Organism> anOrgList){
		orgList = anOrgList;
	}

	//Used for debugging. Prints the line number.
	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
}