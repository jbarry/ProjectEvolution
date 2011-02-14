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
	private LinkedList<Chromosome> chromList;
	private Random ran;

	private double tournProb;
	private double mutProb;
	private double rotProb;
	private double onePtProb;
	private double twoPtProb;
	
	//TODO: will have another way of inserting random mutations.
	public GEP(LinkedList <Organism> anOrgList,
			double aTournProb,
			double aMutProb, 
			double aRotProb,
			double aOnePtProb,
			double aTwoPtProb) {

		orgList = anOrgList;
		tournProb = aTournProb;
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
//		printOrgList(orgList);
		chromList = tournament(partnerSelect(orgList));
		printChromList(chromList);
//		rotation();
//		printChromList(chromList);
//		mutation();
//		printChromList(chromList);
		onePointCrossOver(chromList, onePtProb);
		printChromList(chromList);
	}
	
	public LinkedList<Organism> newGeneration(){
		chromList = tournament(partnerSelect(orgList));
		rotation();
		mutation();
		for(int i=0; i<orgList.size(); i++){
			orgList.get(i).setChromosome(chromList.get(i));
			orgList.get(i).setHealth(100);
		}
		
		return orgList;
	}
	
	public LinkedList<Organism> getOrgList(){
		return orgList;
	}
	
	public LinkedList<Chromosome> getChromList(){
		return chromList;
	}
	
	public void setTournProb(double x){
		tournProb=x;
	}
	
	public void setMutProb(double x){
		mutProb=x;
	}
	
	public void setRotProb(double x){
		rotProb=x;
	}
	
	public void setOnePtProb(double x){
		onePtProb=x;
	}
	
	public void setTwoPtProb(double x){
		twoPtProb=x;
	}
	
	public void setChromList(LinkedList<Chromosome> aChromList){
		chromList=aChromList;
	}
	
	public void setOrgList(LinkedList<Organism> anOrgList){
		orgList=anOrgList;
	}

	/**
	 * This method assigns a double representing fitness of each organism.
	 * @param org - a single organism to be assessed.
	 * @return a double representing the evaluated fitness of the organism.
	 */
	public double fitness(Organism org) {
		return org.getHealth();
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
	public LinkedList<Chromosome> tournament(
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
		Character mutation = new Character('a'); 
		for(Chromosome chrom: chromList) {
			if(ran.nextDouble() < mutProb) {
				int gene = ran.nextInt((chrom.size()));
				chrom.mutate(gene, mutation);
			}
		}
	}


	
	public LinkedList <Pair<Chromosome, Chromosome>> onePointCrossOver(
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


	
	public void twoPointCrossOver(
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
	public LinkedList <Pair<Chromosome, Chromosome>> mateSelect(
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
	public LinkedList <Pair<Organism, Organism>> partnerSelect(
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
	public void printOrgList(LinkedList<Organism> list) {
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
	public void printChromList(LinkedList<Chromosome> list) {
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