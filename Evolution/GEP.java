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
			boolean aHandicap) {

		orgList = anOrgList;
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
		/*double fitness = avgHealth/org.getSamples() +
		 *  org.getHealthyFoodSize()*20 + org.getNumAttacked() +
		 *   org.getNumPushed() - org.getPoisonFoodSize()*10;*/
		org.setFitness(fitness);
		return fitness;
	}

	public LinkedList<Organism> newGeneration() {
		LinkedList<Pair<Organism, Organism>> partners = partnerSelect(orgList);
		LinkedList<Organism> organismList = tournament(partners);
		LinkedList<Chromosome> chromList = makeChromList(organismList);
		rotation(chromList);
		mutation(chromList);
		LinkedList<Pair<Chromosome, Chromosome>> pairList =
			mateSelect(chromList);
		onePointCrossOver(pairList);
		chromList = makeChrmListFrmPair(pairList);
		pairList = mateSelect(chromList);
		onePointCrossOver(pairList);
		onePointCrossOver(pairList);
		for(Chromosome chrom: chromList)
			for(Gene gene: chrom.subListGene(0, chrom.size()))
				gene.updateEvaledList();
		for(int i = 0; i < orgList.size(); i++)
			orgList.get(i).setChromosome(chromList.get(i));
		return orgList;
	}

	/**
	 *Pairs up indiv from the Organism Pair list parameter and
	 *makes them into Pair objects. Puts Pairs into a LinkedList.
	 */
	private LinkedList<Pair<Organism, Organism>> partnerSelect(
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
	private LinkedList<Organism> tournament(
			LinkedList<Pair<Organism, Organism>> aPartners) {
		
		LinkedList<Organism> newPop = new LinkedList<Organism>();
		//Iter through partners list.
		for(int i = 0; i < aPartners.size(); i++) {
			Organism org1 = aPartners.get(i).getFst();
			Organism org2 = aPartners.get(i).getSnd();
			//TODO: change the fitness call when OrgData is used.
			if(org1.getFitness() <= org2.getFitness())
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
	private LinkedList<Organism> tournamentHandicap(
			LinkedList<Pair<Organism, Organism>> partners) {
		
		LinkedList<Organism> newPop = new LinkedList<Organism>();
		//Iter through partners list.
		for (int i = 0; i < partners.size(); i++) {
			Organism org1 = partners.get(i).getFst();
			Organism org2 = partners.get(i).getSnd();

			//TODO: change the fitness call when OrgData is used.
			if(org1.getFitness() <= org2.getFitness())
				if(!handicap)
					newPop.add(org2);
				else if(ran.nextDouble() <= tournProb)
					newPop.add(org1);
				else
					newPop.add(org2);
			else
				newPop.add(org2);
		}
		return newPop;
	}

	private LinkedList<Organism> tournamentWithPrint(
			LinkedList<Pair<Organism, Organism>> partners) {

		System.out.println("Reg tourn");
		LinkedList<Organism> newPop = new LinkedList<Organism>();
		// Iter through partners list.
		for (int i = 0; i < partners.size(); i++) {
			Organism org1 = partners.get(i).getFst();
			Organism org2 = partners.get(i).getSnd();
			double fit1 = org1.getFitness();
			double fit2 = org2.getFitness();
			// TODO: change the fitness call when OrgData is used.
			if (fit1 <= fit2) { // Org1 is less fit.
				System.out.println("org1Fit: " + fit1 + " <= org2Fit: " + fit2);
				newPop.add(org2);
			} else { // Org2 is less fit.
				System.out.println("org1Fit: " + fit1 + " > org2Fit: " + fit2);
				newPop.add(org1);
			}
		}
		return newPop;
	}

	private LinkedList<Organism> tournamentHandicapWithPrint(
			LinkedList<Pair<Organism, Organism>> partners) {

		LinkedList<Organism> newPop = new LinkedList<Organism>();
		int countWon = 0;
		//Iter through partners list.
		for (int i = 0; i < partners.size(); i++) {
			Organism org1 = partners.get(i).getFst();
			Organism org2 = partners.get(i).getSnd();
			double fit1 = org1.getFitness();
			double fit2 = org2.getFitness();
			double rand = ran.nextDouble();
			//TODO: change the fitness call when OrgData is used.
			if (fit1 <= fit2)
				if (!handicap) { // Handicap off.
					System.out.println("Fair game");
					System.out.println("org1Fit: " + fit1 + " <= org2Fit: "
							+ fit2);
					newPop.add(org2);
				} else if (rand <= tournProb) { // Handicap on and probability
												// is won.
					System.out.println("Won!! Handicap on: " + rand + " <= "
							+ tournProb);
					System.out.println("org1Fit: " + fit1 + " <= org2Fit: "
							+ fit2);
					newPop.add(org1);
					countWon++;
				} else { // Handicap is on and probability is lost.
					System.out.println("Handicap on But!: " + rand + " > "
							+ tournProb);
					System.out.println("org1Fit: " + fit1 + " < org2Fit: "
							+ fit2);
					newPop.add(org2);
				}
			else
				newPop.add(org2);
		}
		System.out.println();
		System.out.println("Number won: " + countWon);
		return newPop;
	}

	//Pairs up indiv from the chromosome list parameter and 
	//makes them into Pair objects. Puts Pairs into a LinkedList.
	private LinkedList <Pair<Chromosome, Chromosome>> mateSelect(
			LinkedList<Chromosome> aChromList) {
		
		LinkedList<Pair<Chromosome, Chromosome>> pairList =
			new LinkedList<Pair<Chromosome, Chromosome>>();
		LinkedList<Chromosome> competitors = 
			(LinkedList<Chromosome>) aChromList.clone();
		while(!competitors.isEmpty()) {
			Chromosome chrom = competitors.remove(0);
			int mate;
			Chromosome partner;
			if(competitors.size() == 0) {
				mate = ran.nextInt(aChromList.size());
				aChromList.remove(chrom);
				partner = aChromList.get(mate);
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
	private void rotation(LinkedList<Chromosome> aChromList) {
		for(Chromosome chrom: aChromList)
			if(ran.nextDouble() <= rotProb)
				chrom.rotate(ran.nextInt(chrom.size()));
	}

	/**
	 * Iterate through chromList parameter and call each
	 * chromosomes mutate function.
	 * @param generation
	 * @param prob
	 * @param mutation
	 */
	private void mutation(LinkedList<Chromosome> aChromList) {
		for(Chromosome chrom: aChromList)
			if(ran.nextDouble() < mutProb)
				chrom.mutate();
	}

	/**
	 * Iterates through the aPairList parameter 
	 * of paired chromosomes to mate, and the left chromosome
	 * calls crossover on the right.
	 * @param aPairList
	 */
	private void onePointCrossOver(
			LinkedList<Pair<Chromosome, Chromosome>> aPairList) {
		
		for(int i = 0; i < aPairList.size(); i++)
			if(ran.nextDouble() < onePtProb)
					aPairList.get(i).left().crossOver(aPairList.get(i).right());
	}

	public LinkedList<Organism> getOrgList(){
		return orgList;
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
	private LinkedList<Chromosome> makeChromList(LinkedList<Organism> partners) {
		LinkedList<Chromosome> chromList = new LinkedList<Chromosome>();
		for(Organism org: partners)
			chromList.add(org.getChromosome());
		return chromList;
	}

	private LinkedList<Chromosome> makeChrmListFrmPair(
			LinkedList<Pair<Chromosome, Chromosome>> chromPair) {
		
		LinkedList<Chromosome> chromList = new LinkedList<Chromosome>(); 
		for (int i = 0; i < chromPair.size(); i++) {
			chromList.add(chromPair.get(i).left());
			chromList.add(chromPair.get(i).right());			
		}
		return chromList;
	}

	/**
	 * Prints the id's of the orgs in the partner
	 * list that is passed as a param.
	 * @param partners
	 */
	private void printPartnerListIds(
			LinkedList <Pair<Organism, Organism>> partners) {
		out.println("ParterList with Ids");
		out.println();
		for (Pair<Organism, Organism> partner: partners) {
			Organism o1 = partner.getFst();
			Organism o2 = partner.getSnd();
			out.println(o1.getId() +
					" <=> " + o2.getId());
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
	private void printOrgListChromes() {
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
	private void printOrgListIds() {
		out.println("Printing orgListIds");
		System.out.println();
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
	private void printChromList(LinkedList<Chromosome> aChromList) {
		for(int i = 0; i < aChromList.size(); i++) {
			Chromosome chrom = aChromList.get(i);
			out.println("Chromosome" + i);
			for(int j = 0; j < chrom.size(); j++) {
				for(int k = 0; k < chrom.getGene(j).size(); k++) {
					out.print(chrom.getGene(j).getSym(k).charValue() + " ");
				}
				out.println();
			}
		}
	}
	
	private void printOrgListIdsAndFitness(LinkedList<Organism> anOrgList) {
		out.println("Printing orgList Ids and Fitness");
		out.println();
		for(int i = 0; i < anOrgList.size(); i++) {
			Organism org = anOrgList.get(i);
			out.println("orgId: " + org.getId() +
					" Fitness: " + org.getFitness());
		}
		out.println();		
	}

	public static void main(String[] args) {
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		Random r = new Random();
		for(int i = 0; i < 41; i++)
			orgList.add(new Organism(true, 4, r.nextInt(20), i));
		
		/*GEP gep = new GEP(orgList, 0.75, 0.01, 0.01, 0.75, 0.75, true);*/
		GEP gep = new GEP(orgList, 0.10, 0.01, 0.01, 0.75, 0.75, true);
		
		//print original orgList.
		/*System.out.println("Original orgList");
		gep.printOrgListIdsAndFitness(gep.getOrgList());
		
		// Test partnerSelect.
		System.out.println("Testing Partner Select");
		System.out.println();
		out.println("orgListSize" + gep.getOrgList().size());
		out.println();*/
		LinkedList<Pair<Organism, Organism>> partners =
			gep.partnerSelect(gep.getOrgList());
		//print the ids of the pair of orgs after
		//partnerSelect is called.
		/*System.out.println("After partner select:");*/
		/*gep.printPartnerListIds(partners);*/
		
		//test that tournament with handicap works properly.
		/*LinkedList<Organism> afterTournOrgs = gep.tournament(partners);*/
		/*LinkedList<Organism> afterTournOrgs = gep.tournamentWithPrint(partners);*/
		LinkedList<Organism> afterTournOrgs = gep.tournamentHandicapWithPrint(partners);
		/*System.out.println("After the tournament");*/
		/*gep.printOrgListIdsAndFitness(afterTournOrgs);*/
	}
}