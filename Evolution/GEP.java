package Evolution;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import Frame.GridPanel;
import Interactive.Chromosome;
import Interactive.Gene;
import Interactive.Organism;
import Interactive.Pair;

//TODO: implement in such a way that so many probability
//variables do not need to be passed to the ctor.
/**
 * @author justin
 *
 */
@SuppressWarnings("all")
public class GEP {

	// Class variables.
	private LinkedList<Organism> orgList;
	private Random ran;

	private boolean handicap;
	private boolean elitist;
	private int numElites;
	private boolean doElitism;

	public static double tournProb;
	public static double mutProb;
	public static double rotProb;
	public static double onePtProb;
	public static double twoPtProb;

	/**
	 * @param anOrgList
	 * @param aTournProb
	 * @param aMutProb
	 * @param aRotProb
	 * @param aOnePtProb
	 * @param aTwoPtProb
	 */
	public GEP(LinkedList<Organism> anOrgList, double aTournProb,
			double aMutProb, double aRotProb, double aOnePtProb,
			double aTwoPtProb) {

		orgList = anOrgList;
		tournProb = aTournProb;
		mutProb = aMutProb;
		rotProb = aRotProb;
		onePtProb = aOnePtProb;
		twoPtProb = aTwoPtProb;
		handicap = false;
		ran = new Random();
		doElitism = false;
		numElites = 0;
	}

	/**
	 * @param anOrgList
	 * @param aTournProb
	 * @param aMutProb
	 * @param aRotProb
	 * @param aOnePtProb
	 * @param aTwoPtProb
	 * @param aHandicap
	 */
	public GEP(LinkedList<Organism> anOrgList, double aTournProb,
			double aMutProb, double aRotProb, double aOnePtProb,
			double aTwoPtProb, boolean aHandicap) {

		orgList = anOrgList;
		tournProb = aTournProb;
		mutProb = aMutProb;
		rotProb = aRotProb;
		onePtProb = aOnePtProb;
		twoPtProb = aTwoPtProb;
		handicap = aHandicap;
		ran = new Random();
		doElitism = false;
		numElites = 0;
	}

	/**
	 *
	 * Similar to the previous ctor except that elitism occurs in this form of
	 * GEP.
	 *
	 * @param anOrgList
	 * @param aTournProb
	 * @param aMutProb
	 * @param aRotProb
	 * @param aOnePtProb
	 * @param aTwoPtProb
	 * @param aNumElitists
	 * The number of elite organisms that we want to transcend each
	 * generation.
	 * @param aHandicap
	 * @param aDoElitism
	 * If true then elitism occurs. Otherwise elitism is not
	 * practiced.
	 */
	public GEP(LinkedList<Organism> anOrgList, double aTournProb,
			double aMutProb, double aRotProb, double aOnePtProb,
			double aTwoPtProb, int aNumElitists, boolean aHandicap,
			boolean aDoElitism) {

		orgList = anOrgList;
		tournProb = aTournProb;
		mutProb = aMutProb;
		rotProb = aRotProb;
		onePtProb = aOnePtProb;
		twoPtProb = aTwoPtProb;
		handicap = aHandicap;
		doElitism = aDoElitism;
		numElites = aNumElitists;

		ran = new Random();
	}

	/**
	 * This method assigns a double representing fitness of each organism.
	 *
	 * @param org
	 * - a single organism to be assessed.
	 * @return a double representing the evaluated fitness of the organism.
	 */
	public double fitness(Organism org) {
		double avgHealth = org.getHlthTot() / org.getSamples();
		double activity = (double) org.getNumSteps();
		double goodEating = (double) org.getHealthEat()
		* (org.getHealthEat() + org.getPoisonEat() + org
				.getTotalScans()) / (GridPanel.numFoodSources);
		double assertion = (double) (org.getNumSteps() + org.getNumAttacked() + org
				.getNumPushed()) / (org.getHealthEat() + 1);
		double badEating = (double) org.getPoisonEat() + 1;
		double fitness = (avgHealth * (activity + goodEating + assertion))
		/ badEating;
		org.setFitness(fitness);
		return fitness;
	}

	/**
	 * This is texting Ian's fitness function with print statements.
	 * @param org
	 * @return
	 */
	public double fitnessTest(Organism org) {
		double avgHealth = org.getHlthTot() / org.getSamples();
		System.out.println("avgHealth: " + avgHealth);
		double activity = (double) org.getNumSteps();
		System.out.println("activity: " + activity);
		double goodEating = (double) org.getHealthEat()
		* (org.getHealthEat() + org.getPoisonEat() + org
				.getTotalScans()) / (GridPanel.numFoodSources);
		System.out.println("goodEating: " + goodEating);
		double assertion = (double) (org.getNumSteps() + org.getNumAttacked() + org
				.getNumPushed()) / (org.getHealthEat() + 1);
		System.out.println("assertion: " + assertion);
		double badEating = (double) org.getPoisonEat() + 1;
		System.out.println("badEating: " + badEating);
		double fitness = (avgHealth * (activity + goodEating + assertion))
		/ badEating;
		org.setFitness(fitness);
		System.out.println();
		return fitness;
	}
	
	public double fitnessDistanceTraveled(Organism org) {
		System.out.println(org.getSteps());
		return org.getSteps();
	}
	/**
	 * This method assigns a double representing fitness of each organism.
	 *
	 * @param org
	 * - a single organism to be assessed.
	 * @return a double representing the evaluated fitness of the organism.
	 */
	public double fitnessIan(Organism org) {
		double avgHealth = org.getHlthTot() / org.getSamples();
		double activity = (double) org.getNumSteps();
		double goodEating = (double) org.getHealthEat()
		* (org.getHealthEat() + org.getPoisonEat() + org
				.getTotalScans()) / (GridPanel.numFoodSources);
		double assertion = (double) (org.getNumSteps() + org.getNumAttacked() + org
				.getNumPushed()) / (org.getHealthEat() + 1);
		double badEating = (double) org.getPoisonEat() + 1;
		double fitness = (avgHealth * (activity + goodEating + assertion))
		/ badEating;
		org.setFitness(fitness);
		return fitness;
	}

	/**
	 * @param org
	 * @return
	 */
	/*public double fitnessAvgHealth(Organism org) {
}*/

	/**
	 * @param org
	 * @return
	 */
	public double fitnessDwight(Organism org) {
		/*
		 * double avgHealth = org.getHlthTot() / org.getSamples(); double
		 * fitness = avgHealth / org.getSamples() + org.getHealthyFoodSize() *
		 * 20 + org.getNumAttacked() + org.getNumPushed() -
		 * org.getPoisonFoodSize() * 10; return fitness;
		 */
		return 0.0;
	}

	/**
	 * This fitness function evaluates the fitness of an Organism based on its
	 * average health per the number of steps that it had taken over a
	 * generation.
	 *
	 * @param org
	 * @return
	 */
	public double fitnessAvgHealthPerSteps(Organism org) {
		int numSteps = org.getNumSteps();
		if (numSteps > 0)
			return 0.0;
		return (org.getHlthTot() / org.getSamples()) / org.getNumSteps();
	}

	/**
	 * Called by grid panel to perform all of the necessary actions that it
	 * takes to produce a new generation. An organism list is passed to the ctor
	 * and when this method is called, the population goes through
	 *
	 * - tournament selection
	 * - mutation
	 * - rotation
	 * - 1-point cross over.
	 * - 2-point cross over.
	 *
	 * @return
	 */
	public LinkedList<Organism> newGeneration() {

		// List to hold the Elite individuals.
		LinkedList<Chromosome> eliteList = new LinkedList<Chromosome>();
		// Get the most elite indiv or individuals.
		if (doElitism)
			eliteList = (LinkedList<Chromosome>) assembleElites((LinkedList<Organism>) orgList
					.clone()); // Called on a clone of the orgList because
		// ordering the original list may be a
		// detriment to randomization.
		LinkedList<Chromosome> chromList = makeChromList(tournament(partnerSelect(orgList)));
		rotation(chromList);
		mutation(chromList);
		// Pair up Chromosomes in preparation
		// for 1-point cross over.
		LinkedList<Pair<Chromosome, Chromosome>> pairList = mateSelect(chromList);
		onePointCrossOver(pairList);
		// Pair up Chromosomes again in preparation
		// for 2-point cross over.
		pairList = mateSelect(makeChrmListFrmPair(pairList));
		// 2-point cross over.
		onePointCrossOver(pairList);
		onePointCrossOver(pairList);
		LinkedList<Chromosome> finalChromes = makeChrmListFrmPair(pairList);
		// Proceed with elitism if true.
		if (eliteList.size() != 0)
			transferElites(finalChromes, eliteList);
		// Update the evaluations of the genes.
		for (Chromosome chrom : chromList)
			for (Gene gene : chrom.subListGene(0, chrom.size()))
				gene.updateEvaledList();
		// TODO: May have to put org id's and chrom id's back together.
		for (int i = 0; i < orgList.size(); i++)
			orgList.get(i).setChromosome(chromList.get(i));
		return orgList;
	}

	/**
	 * The same as newGeneration except with print lines for testing.
	 *
	 * @return
	 */
	public LinkedList<Organism> newGenerationTest() {
		printOrgListIdsAndFitness(orgList);
		// List to hold the Elite individuals.
		LinkedList<Chromosome> eliteList = new LinkedList<Chromosome>();
		// Get the most elite indiv or individuals.
		if (doElitism)
			eliteList = (LinkedList<Chromosome>) assembleElitesTest((LinkedList<Organism>) orgList
					.clone()); // Called on a clone of the orgList because
		// ordering the original list may be a
		// detriment to randomization.

		System.out.println("The elites are: ");
		printChromeListIds(eliteList);
		System.out.println("The elites genes are: ");
		printChromGenes(eliteList);

		// PRINT ORIGINAL ORGLIST.
		System.out.println("Original orgList");
		printOrgListIdsAndFitness(getOrgList());

		// TEST PARTNERSELECT.
		System.out.println("Testing Partner Select");
		System.out.println();
		out.println("orgListSize" + getOrgList().size()); out.println();
		LinkedList<Pair<Organism, Organism>> partners = partnerSelect(getOrgList());

		// PRINT THE IDS OF THE PAIR OF ORGS AFTER
		// PARTNERSELECT IS CALLED.
		System.out.println("After partner select:");
		printPartnerListIds(partners);

		// TEST TOURNAMENT.
		LinkedList<Organism> afterTournOrgs = tournament(partners);
		System.out.println("afterTournOrgs size: " + afterTournOrgs.size());

		// TOURNAMENT WITH PRINT.
		/*LinkedList<Organism> afterTournOrgs = gep.tournamentWithPrint(partners);
System.out.println();
System.out.println("afterTournOrgs size: " + afterTournOrgs.size());*/

		// HANDICAP TOURNAMENT WITH PRINT.
		/*LinkedList<Organism> afterTournOrgs =
gep.tournamentHandicapWithPrint(partners);
System.out.println("After the tournament");
gep.printOrgListIdsAndFitness(afterTournOrgs);*/

		// TEST MATESELECT
		// MAKE A NEW CHROMLIST.
		LinkedList<Chromosome> chromList = makeChromList(afterTournOrgs);

		// PRINT THE IDS OF THE CHROMOSOMES IN THE
		// CHROME LIST AFTER TOURNAMENT OCCURS.
		System.out.println("Chromosome ids in list after tournament:");
		printChromeListIds(chromList);

		// MUTATION AND ROTATION.
		rotation(chromList);
		printChromeListIds(chromList);
		mutation(chromList);
		System.out.println("After rot and mut:");
		printChromeListIds(chromList);


		// Pair up Chromosomes in preparation
		// for 1-point cross over.
		LinkedList<Pair<Chromosome, Chromosome>> pairList = mateSelect(chromList);
		printMateListIds(pairList);

		// ONE POINT CROSS OVER.
		System.out.println("Genes before cross");
		printChromGenes(makeChrmListFrmPair(pairList));
		onePointCrossOver(pairList);
		System.out.println("Genes after cross");
		printChromGenes(makeChrmListFrmPair(pairList));

		// Pair up Chromosomes again in preparation
		// for 2-point cross over.
		pairList = mateSelect(makeChrmListFrmPair(pairList));
		System.out.println("After 1-point");
		printChromeListIds(makeChrmListFrmPair(pairList));

		// 2-POINT CROSS OVER.
		System.out.println("After 2-point");
		onePointCrossOver(pairList);
		onePointCrossOver(pairList);
		LinkedList<Chromosome> finalChromes = makeChrmListFrmPair(pairList);

		printChromeListIds(finalChromes);

		// PROCEED WITH ELITISM IF TRUE.
		if (eliteList.size() != 0)
			transferElites(finalChromes, eliteList);
		printChromeListIds(finalChromes);
		System.out.println("and the genes are");
		printChromGenes(finalChromes);

		// UPDATE THE EVALUATIONS OF THE GENES.
		for (Chromosome chrom : chromList)
			for (Gene gene : chrom.subListGene(0, chrom.size()))
				gene.updateEvaledList();

		System.out.println("without set: ");
		printChromGenes(makeChromList(orgList));

		// TODO: Shouldn't have to reset the orgLists chromosomes. The objects
		// themselves should be changed already.
		for (int i = 0; i < orgList.size(); i++)
			orgList.get(i).setChromosome(chromList.get(i));

		System.out.println("with set");
		printChromGenes(makeChromList(orgList));
		return orgList;
	}

	/**
	 * This method retreives the most elite members of the organism list. The
	 * number of individuals depends on what the numElites feild is set to.
	 *
	 * @param anOrgList
	 * An organism list to find the most fit individuals from.
	 */
	public List<Chromosome> assembleElites(LinkedList<Organism> anOrgList) {
		LinkedList<Organism> tempOrgList = (LinkedList<Organism>) anOrgList.clone();
		Collections.sort(tempOrgList);
		List<Chromosome> eliteList = new LinkedList<Chromosome>();
		for (int i = 0; i < numElites; i++)
			eliteList.add(tempOrgList.pollLast().getChromosome());
		return eliteList;
	}

	/**
	 * The same as assembleElites except for with print lines.
	 * @param anOrgList
	 * @return
	 */
	public List<Chromosome> assembleElitesTest(LinkedList<Organism> anOrgList) {
		System.out.println("before ordering: ");
		printOrgListIdsAndFitness(anOrgList);
		Collections.sort(anOrgList);
		System.out.println("after ordering: ");
		printOrgListIdsAndFitness(anOrgList);
		List<Chromosome> eliteList = new LinkedList<Chromosome>();

		for (int i = 0; i < numElites; i++)
			eliteList.add(anOrgList.pollLast().getChromosome());
		return eliteList;
	}

	/**
	 * Takes in a chromosome list, aFinalChromes, and a list of the most elite
	 * member's chromosomes of the population, and transfers the chromosomes of
	 * the elite members into aFinalChromes. The number of chromosomes in the
	 * elite list is the number of chromosomes that will be randomly replaced
	 * aFinalChromes list.
	 *
	 * @param aFinalChromes
	 * @param anEliteList
	 */
	public void transferElites(LinkedList<Chromosome> aFinalChromes,
			LinkedList<Chromosome> anEliteList) {

		HashSet<Integer> indeces = new HashSet<Integer>();
		indeces.add(ran.nextInt(aFinalChromes.size()));
		while (indeces.size() < numElites)
			indeces.add(ran.nextInt(aFinalChromes.size()));
		LinkedList<Integer> indexList = new LinkedList<Integer>(indeces);
		while(!indexList.isEmpty())
			aFinalChromes.set(indexList.pop(), anEliteList.pop());
	}

	/**
	 * Pairs up indiv from the Organism Pair list parameter and makes them into
	 * Pair objects. Puts Pairs into a LinkedList.
	 *
	 * @param population
	 * - A list of organisms from which the pairs are to be selected.
	 * @return - A list of the pairs of organisms.
	 *
	 */
	public LinkedList<Pair<Organism, Organism>> partnerSelect(
			LinkedList<Organism> population) {
		// pairList: list that will recieve the pairs
		// of partners.
		LinkedList<Pair<Organism, Organism>> pairList = new LinkedList<Pair<Organism, Organism>>();
		// notSeenMap:
		// key: Each org from population.
		// val: A list of organisms that they have not
		// seen yet.
		HashMap<Organism, LinkedList<Organism>> notSeenMap = new HashMap<Organism, LinkedList<Organism>>();
		// Iterate through population, initializing the
		// notSeenMap with keys, and making all the values
		// a clone of the original population with
		// the current organism removed from the list.
		for (int i = 0; i < population.size(); i++) {
			Organism org = population.get(i);
			notSeenMap.put(org, (LinkedList<Organism>) population.clone());
			notSeenMap.get(org).remove(i);
		}
		// Iterate through population list again.
		for (int i = 0; i < population.size(); i++) {
			Organism partner1 = population.get(i);
			// For the current org from the population list,
			// get his notSeenList from the notSeenMap.
			LinkedList<Organism> selection = notSeenMap.get(partner1);
			// Select a random individual from the notSeenList
			// Then remove that individual.
			Organism partner2 = selection.remove(ran.nextInt(selection.size()));
			// Remove the partner1 from partner2's notSeenList.
			notSeenMap.get(partner2).remove(partner1);
			pairList.add(new Pair<Organism, Organism>(partner1, partner2));
		}
		return pairList;
	}

	/**
	 * @param partnerList
	 * - A LinkedList of organism Pairs. Each member of a pair will
	 * compete with the other member.
	 * @param tournProb
	 * -the probability at which either the more fit, or the less fit
	 * individual will be chosen.
	 */
	// TODO: make org unaware of its own fitness.
	public LinkedList<Organism> tournament(
			LinkedList<Pair<Organism, Organism>> aPartners) {

		LinkedList<Organism> newPop = new LinkedList<Organism>();
		// Iter through partners list.
		for (int i = 0; i < aPartners.size(); i++) {
			Organism org1 = aPartners.get(i).getFst();
			Organism org2 = aPartners.get(i).getSnd();
			// TODO: change the fitness call when OrgData is used.
			if (org1.getFitness() <= org2.getFitness())
				newPop.add(org2);
			else
				newPop.add(org1);
		}
		return newPop;
	}

	/**
	 * @param partnerList
	 * - A LinkedList of organism Pairs. Each member of a pair will
	 * compete with the another organism from the population. There
	 * will be no duplicate pairs in the returned list.
	 * @param tournProb
	 * -the probability at which either the more fit, or the less fit
	 * individual will be chosen.
	 * @param handicap
	 * - Handicap to false means that the tournProb probability will
	 * be in favor of the most fit individuals. Handicap to true
	 * means that the prob will favor the less fit individuals.
	 * @return returns each winner of a match, as a Chromosome, in a LinkedList.
	 */
	// TODO: make org unaware of its own fitness.
	public LinkedList<Organism> tournamentHandicap(
			LinkedList<Pair<Organism, Organism>> partners) {

		LinkedList<Organism> newPop = new LinkedList<Organism>();
		// Iter through partners list.
		for (int i = 0; i < partners.size(); i++) {
			Organism org1 = partners.get(i).getFst();
			Organism org2 = partners.get(i).getSnd();

			// TODO: change the fitness call when OrgData is used.
			if (org1.getFitness() <= org2.getFitness())
				if (!handicap)
					newPop.add(org2);
				else if (ran.nextDouble() <= tournProb)
					newPop.add(org1);
				else
					newPop.add(org2);
			else
				newPop.add(org2);
		}
		return newPop;
	}

	public LinkedList<Organism> tournamentHandicapWithPrint(
			LinkedList<Pair<Organism, Organism>> partners) {

		LinkedList<Organism> newPop = new LinkedList<Organism>();
		int countWon = 0;
		// Iter through partners list.
		for (int i = 0; i < partners.size(); i++) {
			Organism org1 = partners.get(i).getFst();
			Organism org2 = partners.get(i).getSnd();
			double fit1 = org1.getFitness();
			double fit2 = org2.getFitness();
			double rand = ran.nextDouble();
			// TODO: change the fitness call when OrgData is used.
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

	/**
	 * Pairs up indiv from the chromosome list parameter and makes them into
	 * Pair objects. Puts Pairs into a LinkedList.
	 *
	 * @param aChromList
	 * @return
	 */
	public LinkedList<Pair<Chromosome, Chromosome>> mateSelect(
			LinkedList<Chromosome> aChromList) {
		// This is the list of pairs to return.
		LinkedList<Pair<Chromosome, Chromosome>> pairList =
			new LinkedList<Pair<Chromosome, Chromosome>>();
		// This is the clone of the chromList to be decreased.
		LinkedList<Chromosome> competitors = (LinkedList<Chromosome>) aChromList
		.clone();
		// If Competitors has an odd cardinality, then just append a random
		// member to the end.
		if (competitors.size()%2 == 1)
			competitors.add(competitors.get(ran.nextInt(competitors.size())));
		while (!competitors.isEmpty()) {
			Chromosome chrom1 = competitors.remove(ran.nextInt(competitors.size()));
			Chromosome chrom2 = competitors.remove(ran.nextInt(competitors.size()));
			/*System.out.println(chrom1.getId() + " <=> " + chrom2.getId());*/
			pairList.add(new Pair<Chromosome, Chromosome>(chrom1, chrom2));
		}
		return pairList;
	}

	/**
	 * Iterates through the chromList
	 *
	 * @param aChromList
	 * @param prob
	 */
	private void rotation(LinkedList<Chromosome> aChromList) {
		for (Chromosome chrom : aChromList)
			if (ran.nextDouble() <= rotProb)
				chrom.rotate(ran.nextInt(chrom.size()));
	}

	/**
	 * Iterate through chromList parameter and call each chromosomes mutate
	 * function.
	 *
	 * @param generation
	 * @param prob
	 * @param mutation
	 */
	public void mutation(LinkedList<Chromosome> aChromList) {
		for (Chromosome chrom : aChromList)
			if (ran.nextDouble() < mutProb)
				chrom.mutate();
	}

	/**
	 * Iterates through the aPairList parameter of paired chromosomes to mate,
	 * and the left chromosome calls crossover on the right.
	 *
	 * @param aPairList
	 */
	public void onePointCrossOver(
			LinkedList<Pair<Chromosome, Chromosome>> aPairList) {
		for (int i = 0; i < aPairList.size(); i++)
			if (ran.nextDouble() < onePtProb)
				aPairList.get(i).getFst().crossOver(aPairList.get(i).getSnd());
	}

	/*
	 * private void assembleElites(LinkedList<Organism> orgList) {
	 * Collections.sort(orgList); List<Chromosome> eliteList = new
	 * ArrayList<Chromosome>(); for (int i = 0; i < numElites; i++)
	 * orgList.add(orgList.pop().getChromosome()); }
	 */

	public LinkedList<Organism> getOrgList() {
		return orgList;
	}

	public double getTournProb() {
		return tournProb;
	}

	public void setTournProb(double x) {
		tournProb = x;
	}

	public double getMutProb() {
		return mutProb;
	}

	public void setMutProb(double x) {
		mutProb = x;
	}

	public double getRotProb() {
		return rotProb;
	}

	public void setRotProb(double x) {
		rotProb = x;
	}

	public double getOnePtProb() {
		return onePtProb;
	}

	public void setOnePtProb(double x) {
		onePtProb = x;
	}

	public void setTwoPtProb(double x) {
		twoPtProb = x;
	}

	/**
	 * @param anOrgList
	 */
	public void setOrgList(LinkedList<Organism> anOrgList) {
		orgList = anOrgList;
	}

	public void enableElitism(boolean aDoElitism, int aNumElites) {
		doElitism = aDoElitism;
		numElites = aNumElites;
	}

	public void setElitism(boolean aDoElitism) {
		doElitism = aDoElitism;
	}

	public void setElitismNumber(int aNumElites) {
		numElites = aNumElites;
	}

	// Used for debugging. Prints the line number.
	public int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

	/**
	 * For testing
	 *
	 * @param population
	 */
	public LinkedList<Chromosome> makeChromList(LinkedList<Organism> partners) {
		LinkedList<Chromosome> chromList = new LinkedList<Chromosome>();
		for (Organism org : partners)
			chromList.add(org.getChromosome());
		return chromList;
	}

	public LinkedList<Chromosome> makeChrmListFrmPair(
			LinkedList<Pair<Chromosome, Chromosome>> chromPair) {

		LinkedList<Chromosome> chromList = new LinkedList<Chromosome>();
		for (int i = 0; i < chromPair.size(); i++) {
			chromList.add(chromPair.get(i).left());
			chromList.add(chromPair.get(i).right());
		}
		return chromList;
	}

	/**
	 * Prints the id's of the orgs in the partner list that is passed as a
	 * param.
	 *
	 * @param partners
	 */
	public void printPartnerListIds(
			LinkedList<Pair<Organism, Organism>> partners) {

		out.println("ParterList with Ids");
		out.println();
		for (Pair<Organism, Organism> partner : partners) {
			Organism o1 = partner.getFst();
			Organism o2 = partner.getSnd();
			out.println(o1.getId() + " <=> " + o2.getId());
		}
		out.println();
		out.println("partnerList size: " + partners.size());
		out.println();
	}

	/**
	 * Used for testing the GEP class. Simply prints the chromosomes of the
	 * organism list that is passed to it.
	 *
	 * @param list
	 */
	public void printChromGenes(LinkedList<Chromosome> aChromList) {
		out.println("Printing genes in chromosomes:");
		for (int i = 0; i < aChromList.size(); i++) {
			Chromosome chrom = aChromList.get(i);
			System.out.println("Chromosome: " + i);
			for (int j = 0; j < chrom.size(); j++) {
				Gene aGene = chrom.getGene(j);
				for (int k = 0; k < aGene.size(); k++)
					out.print(aGene.getSym(k).charValue() + " ");
				out.println();
			}
			out.println();
		}
	}

	/**
	 * Used for testing the GEP class. Simply prints the ids of the organism
	 * list that is passed to it.
	 *
	 * @param list
	 */
	public void printOrgListIds() {
		out.println("Printing orgListIds");
		System.out.println();
		for (int i = 0; i < orgList.size(); i++) {
			Organism org = orgList.get(i);
			out.println("orgId: " + org.getId());
		}
		out.println();
	}

	/**
	 * Used for testing the GEP class. Simply prints a chromosome list that is
	 * passed to it.
	 */
	public void printGenes(LinkedList<Chromosome> aChromList) {
		for (int i = 0; i < aChromList.size(); i++) {
			Chromosome chrom = aChromList.get(i);
			out.println("Chromosome" + i);
			for (int j = 0; j < chrom.size(); j++) {
				for (int k = 0; k < chrom.getGene(j).size(); k++) {
					out.print(chrom.getGene(j).getSym(k).charValue() + " ");
				}
				out.println();
			}
		}
	}

	/**
	 * @param anOrgList
	 */
	public void printOrgListIdsAndFitness(LinkedList<Organism> anOrgList) {
		out.println("Printing orgList Ids and Fitness");
		out.println();
		for (int i = 0; i < anOrgList.size(); i++) {
			Organism org = anOrgList.get(i);
			out.println("orgId: " + org.getId() + " Fitness: "
					+ org.getFitness());
		}
		out.println();
	}

	/**
	 * @param chromeList
	 */
	public void printChromeListIds(
			LinkedList<Chromosome> chromeList) {

		System.out.println("Size: " + chromeList.size());
		System.out.println("Printing chromosome ids: ");
		for (Chromosome chrom : chromeList)
			System.out.println(chrom.getId());
		System.out.println();
	}

	/**
	 * @param mates
	 */
	public void printMateListIds(LinkedList<Pair<Chromosome, Chromosome>> mates) {
		out.println("chrom pair list size: " + mates.size());
		out.println("chrom pair list with Ids:");
		for (Pair<Chromosome, Chromosome> partners : mates) {
			Chromosome c1 = partners.getFst();
			Chromosome c2 = partners.getSnd();
			out.println(c1.getId() + " <=> " + c2.getId());
		}
		out.println();
	}

	public int getNumElites() {
		return numElites;
	}

	public void setNumElites(int numElites) {
		this.numElites = numElites;
	}

	public static void main(String[] args) {
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		Random r = new Random();

		// CASE: ODD NUMBER OF ORGANISMS.
		/*for (int i = 0; i < 13; i++)
		orgList.add(new Organism(true, 4, r.nextInt(20), i));*/

		// CASE: EVEN NUMBER OF ORGANISMS.
		for (int i = 0; i < 4; i++)
			orgList.add(new Organism(true, 4, r.nextInt(20), i));

		/* GEP gep = new GEP(orgList, 0.75, 0.01, 0.01, 0.75, 0.75, true); */
		/* GEP gep = new GEP(orgList, 0.10, 0.01, 0.01, 0.75, 0.75, true); */
		/*GEP gep = new GEP(orgList, 0.10, 0.01, 0.01, 0.75, 0.75, true);*/
		GEP gep = new GEP(orgList, 0.75, 0.01, 0.01, 0.75, 0.75, 2, false, true); // Elitism ctor.
		gep.newGenerationTest();
		/*
		// PRINT ORIGINAL ORGLIST.
		System.out.println("Original orgList");
		gep.printOrgListIdsAndFitness(gep.getOrgList());
		// TEST PARTNERSELECT.
		System.out.println("Testing Partner Select");
		System.out.println();
		out.println("orgListSize" + gep.getOrgList().size()); out.println();
		LinkedList<Pair<Organism, Organism>> partners = gep.partnerSelect(gep
		.getOrgList());
		// PRINT THE IDS OF THE PAIR OF ORGS AFTER
		// PARTNERSELECT IS CALLED.
		System.out.println("After partner select:");
		gep.printPartnerListIds(partners);
		
		// TEST TOURNAMENT.
		LinkedList<Organism> afterTournOrgs = gep.tournament(partners);
		System.out.println("afterTournOrgs size: " + afterTournOrgs.size());
		// TOURNAMENT WITH PRINT.
		LinkedList<Organism> afterTournOrgs = gep.tournamentWithPrint(partners);
		System.out.println();
		System.out.println("afterTournOrgs size: " + afterTournOrgs.size());
		
		// HANDICAP TOURNAMENT WITH PRINT.
		LinkedList<Organism> afterTournOrgs =
		gep.tournamentHandicapWithPrint(partners);
		System.out.println("After the tournament");
		gep.printOrgListIdsAndFitness(afterTournOrgs);
		
		// TEST MATESELECT
		// MAKE A NEW CHROMLIST.
		LinkedList<Chromosome> afterTournChromList = gep
		.makeChromList(afterTournOrgs);
		// PRINT THE IDS OF THE CHROMOSOMES IN THE
		// CHROME LIST AFTER TOURNAMENT OCCURS.
		System.out.println("Chromosome ids in list after tournament:");
		gep.printChromeListIds(afterTournChromList);
		// CALL MATESELECT.
		LinkedList<Pair<Chromosome, Chromosome>> afterMateSelect = gep
		.mateSelect2(afterTournChromList);
		// PRINT REPRESENTATION OF CHROM'S IDS IN PAIRS
		System.out.println("Pair representation of chromosome mates:");
		gep.printMateListIds(afterMateSelect);
		
		// PRINT THE SYMBOLS STORED IN THE GENES OF EACH CHROM
		// IN THE CHROMLIST.
		System.out.println("Before crossover: ");
		System.out.println();
		gep.printChromGenes(gep.makeChrmListFrmPair(afterMateSelect));
		
		// ONE POINT CROSSOVER TESTING.
		gep.onePointCrossOver(afterMateSelect);
		
		// PRINT THE RESULT OF CROSSOVER.
		System.out.println("After crossOver: ");
		System.out.println();
		// MAKE CHROMLIST OF THE CROSSEDOVER PAIRS.
		LinkedList<Chromosome> nextGo = gep
		.makeChrmListFrmPair(afterMateSelect);
		gep.printChromGenes(nextGo);
		// PAIR UP CHROMS AGAIN.
		LinkedList<Pair<Chromosome, Chromosome>> afterSecondGo = gep.mateSelect2(nextGo);
		// PERFORM 2-POINT CROSS OVER.
		gep.onePointCrossOver(afterSecondGo);
		gep.onePointCrossOver(afterSecondGo);*/
	}
}