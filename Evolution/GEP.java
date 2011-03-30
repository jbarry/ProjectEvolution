package Evolution;

import Frame.GridPanel;
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
	private boolean handicap;
	
	public static double tournProb;
	public static double mutProb;
	public static double rotProb;
	public static double onePtProb;
	public static double twoPtProb;

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
		handicap = false;
		ran = new Random();
	}
	
	public GEP(LinkedList <Organism> anOrgList,
			double aTournProb,
			double aMutProb, 
			double aRotProb,
			double aOnePtProb,
			double aTwoPtProb,
			boolean aHandicap,
			double aHandicapProb) {

		orgList = anOrgList;
		chromList = new LinkedList<Chromosome>();
		tournProb = aTournProb;
		mutProb = aMutProb;
		rotProb = aRotProb;
		onePtProb = aOnePtProb;
		twoPtProb = aTwoPtProb;
		handicap = aHandicap;
		ran = new Random();
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
		double avgHealth = org.getHlthTot()/org.getSamples();
		double activity = (double) org.getNumSteps();
		double goodEating = (double) org.getHealthEat()*(org.getHealthEat()+org.getPoisonEat()+org.getTotalScans())/(GridPanel.numFoodSources);
		double assertion = 	(double) (org.getNumSteps()+org.getNumAttacked()+org.getNumPushed())/(org.getHealthEat()+1);
		double badEating =	(double) org.getPoisonEat()+1;
		double fitness = (avgHealth*(activity + goodEating + assertion))/badEating;
//		double fitness = avgHealth/org.getSamples() + org.getHealthyFoodSize()*20 + org.getNumAttacked() + org.getNumPushed() - org.getPoisonFoodSize()*10;
		org.setFitness(fitness);
		return fitness;
	}

	public LinkedList<Organism> newGeneration() {
		LinkedList<Pair<Organism, Organism>> partners = partnerSelect(orgList);
		LinkedList<Organism> organismList = tournament(partners);
		LinkedList<Chromosome> chromList = makeChromList(organismList);
		LinkedList<Chromosome> rotated = rotation(chromList);
		mutation();
		onePointCrossOver();
		onePointCrossOver();
		for(Chromosome chrom: chromList){
			for(Gene gene: chrom.subListGene(0, chrom.size())){
				gene.updateEvaledList();
			}
		}
		for(int i = 0; i < orgList.size(); i++)
			orgList.get(i).setChromosome(chromList.get(i));
		return orgList;
	}

	/**
	 *Pairs up indiv from the Organism Pair list parameter and
	 *makes them into Pair objects. Puts Pairs into a LinkedList.
	 */
	public LinkedList<Pair<Organism, Organism>> partnerSelect(
			LinkedList<Organism> population) {
		// pairList: list that will recieve the pairs
		//of partners.
		LinkedList<Pair<Organism, Organism>> pairList =
			new LinkedList<Pair<Organism, Organism>>();
		//notSeenMap:
		//key: Each org from population.
		//val: A list of organisms that they have not
		// seen yet.
		HashMap<Organism, LinkedList<Organism>> notSeenMap =
			new HashMap<Organism, LinkedList<Organism>>();
		// Iterate through population, initializing the
		// notSeenMap with keys, and making all the values
		// a clone of the original population with
		// the current organism removed from the list.
		for(int i = 0; i < population.size(); i++) {
			Organism org = population.get(i);
			notSeenMap.put(org, (LinkedList<Organism>) population.clone());
			notSeenMap.get(org).remove(i);
		}
		// Iterate through population list again.
		for(int i = 0; i < population.size(); i++) {
			Organism partner1 = population.get(i);
			//For the current org from the population list,
			//get his notSeenList from the notSeenMap.
			LinkedList<Organism> selection = notSeenMap.get(partner1);
			//Select a random individual from the notSeenList
			//Then remove that individual.
			Organism partner2 =
				selection.remove(ran.nextInt(selection.size()));
			//Remove the partner1 from partner2's notSeenList.
			notSeenMap.get(partner2).remove(partner1);
			pairList.add(new Pair<Organism, Organism>(partner1, partner2));
		}
		return pairList;
	}
	
	/**
	 * @param partnerList - A LinkedList of organism Pairs.
	 * Each member of a pair will compete with the other member.
	 * @param tournProb-the probability at which either the 
	 * more fit, or the less fit individual will be chosen.
	 */
	//TODO: make org unaware of its own fitness.
	public LinkedList<Organism> tournament(
			LinkedList<Pair<Organism, Organism>> partners) {
		
		LinkedList<Organism> newPop = new LinkedList<Organism>();
		//Iter through partners list.
		for(int i = 0; i < partners.size(); i++) {
			Organism org1 = partners.get(i).getFst();
			Organism org2 = partners.get(i).getSnd();
			//TODO: change the fitness call when OrgData is used.
			if(org1.getFitness() < org2.getFitness())
				newPop.add(org1);
			else
				newPop.add(org2);
		}
		return newPop;
	}

	/**
	 * @param partnerList - A LinkedList of organism Pairs.
	 * Each member of a pair will compete with the other member.
	 * @param tournProb-the probability at which either the 
	 * more fit, or the less fit individual will be chosen.
	 * @param handicap- Handicap to false means that the tournProb
	 * probability will be in favor of the most fit individuals.
	 * Handicap to true means that the prob will favor the less fit individuals.
	 * @return returns each winner of a match, as a Chromosome, in a LinkedList.
	 */
	//TODO: make org unaware of its own fitness.
	public LinkedList<Organism> tournamentHandicap(
			LinkedList<Pair<Organism, Organism>> partners) {
		
		LinkedList<Organism> newPop = new LinkedList<Organism>();
		//Iter through partners list.
		for(int i = 0; i < partners.size(); i++) {
			Organism org1 = partners.get(i).getFst();
			Organism org2 = partners.get(i).getSnd();
			//TODO: change the fitness call when OrgData is used.
			if(org1.getFitness() < org2.getFitness()) {
				if(handicap)
					if(ran.nextDouble() < tournProb)
						newPop.add(org1);
					else
						newPop.add(org2);
			}
			else
				newPop.add(org2);
		}
		return newPop;
	}

	//Pairs up indiv from the chromosome list parameter and 
	//makes them into Pair objects. Puts Pairs into a LinkedList.
	public LinkedList <Pair<Chromosome, Chromosome>> mateSelect() {
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
	 * Iterates through the chromList
	 * @param aChromList
	 * @param prob
	 */
	public LinkedList<Chromosome> rotation(LinkedList<Chromosome> chromList) {
		for(Chromosome chrom: chromList)
			if(ran.nextDouble() <= rotProb) {
				int x = ran.nextInt(chrom.size());
				chrom.rotate(x);
			}
		return chromList;
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
		chromList.clear();
		for(int i = 0; i < pairList.size(); i++) {
			if(ran.nextDouble() < onePtProb) {
				Pair<Chromosome, Chromosome> crossed = 
					pairList.get(i).left().crossOver(pairList.get(i).right());
			}
		}
		makeChrmListFrmPair(pairList);
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
	public int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

	/**
	 * For testing
	 * @param population
	 */
	public LinkedList<Chromosome> makeChromList(LinkedList<Organism> partners) {
		LinkedList<Chromosome> chromList = new LinkedList<Chromosome>();
		for(Organism org: partners)
			chromList.add(org.getChromosome());
		return chromList;
	}

	public void makeChrmListFrmPair(
			LinkedList<Pair<Chromosome, Chromosome>> chromPair) {
		chromList.clear();
		for (int i = 0; i < chromPair.size(); i++) {
			chromList.add(chromPair.get(i).left());
			chromList.add(chromPair.get(i).right());			
		}
	}

	/**
	 * Prints the id's of the orgs in the partner
	 * list that is passed as a param.
	 * @param partners
	 */
	public void printPartnerListIds(
			LinkedList <Pair<Organism, Organism>> partners) {
		for (Pair<Organism, Organism> partner: partners) {
			Organism o1 = partner.getFst();
			Organism o2 = partner.getSnd();
			out.println("part1: " + o1.getId() +
					" part2: " + o2.getId());
		}
		out.println();
		out.println("partnerList size: " + partners.size());
		out.println();
	}
	
	/**
	 * Used for testing the GEP class. Simply prints the
	 * chromosomes of the organism list
	 * that is passed to it.
	 * @param list
	 */
	public void printOrgListChromes() {
		out.println("Printing orgList's Chromosomes");
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
		out.println();
	}
	
	/**
	 * Used for testing the GEP class. Simply prints the
	 * ids of the organism list
	 * that is passed to it.
	 * @param list
	 */
	public void printOrgListIds() {
		out.println("Printing orgListIds");
		for(int i = 0; i < orgList.size(); i++) {
			Organism org = orgList.get(i);
			out.println("orgId: " + org.getId());
		}
		out.println();
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
	
	public static void main(String[] args) {
		LinkedList <Organism> orgList = new LinkedList<Organism>();
		Random r = new Random();
		for(int i = 0; i < 41; i++)
			orgList.add(new Organism(true, 4, r.nextDouble(), i));
		GEP gep = new GEP(orgList, 0.75, 0.01, 0.01, 0.75, 0.75);
//		GEP gep = new GEP(orgList, 0.10, 0.01, 0.01, 0.75, 0.75);
		//print original orgList.
		gep.printOrgListIds();
		// Test partnerSelect.
		out.println("orgListSize" + gep.getOrgList().size());
		out.println();
		LinkedList<Pair<Organism, Organism>> partners =
			gep.partnerSelect(gep.getOrgList());
		//print the ids of the pair of orgs after
		//partnerSelect is called.
		gep.printPartnerListIds(partners);
		
	}
}