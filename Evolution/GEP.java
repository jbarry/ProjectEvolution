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
import Interactive.OrgData;
import Interactive.Organism;
import Interactive.Pair;

/**
 * @author justin
 * 
 */
@SuppressWarnings("all")
public class GEP {

	 // Class variables.
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
	  *              The number of elite organisms that we want to transcend
	  *              each generation.
	  * @param aHandicap
	  * @param aDoElitism
	  *              If true then elitism occurs. Otherwise elitism is not
	  *              practiced.
	  */
	 public GEP(double aTournProb, double aMutProb, double aRotProb,
			   double aOnePtProb, double aTwoPtProb, int aNumElitists,
			   boolean aHandicap, boolean aDoElitism) {

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
	  * @param orgData
	  *              - a single organism to be assessed.
	  * @return a double representing the evaluated fitness of the organism.
	  */
	 public double fitnessIan(OrgData orgData) {
		  double avgHealth = orgData.getHlthTot() / orgData.getSamples();
		  double activity = (double) orgData.getNumSteps();
		  double goodEating = (double) orgData.getHealthEat()
				    * (orgData.getHealthEat() + orgData.getPoisonEat() + orgData
							 .getTotalScans())
				    / (GridPanel.numFoodSources);
		  double assertion = (double) (orgData.getNumSteps()
				    + orgData.getNumAttacked() + orgData.getNumPushed())
				    / (orgData.getHealthEat() + 1);
		  double badEating = (double) orgData.getPoisonEat() + 1;
		  double fitness = (avgHealth * (activity + goodEating + assertion))
				    / badEating;
		  orgData.setFitness(fitness);
		  return fitness;
	 }

	 /**
	  * This is texting Ian's fitness function with print statements.
	  * 
	  * @param orgData
	  * @return
	  */
	 public double fitnessIanTest(OrgData orgData) {
		  double avgHealth = orgData.getHlthTot() / orgData.getSamples();
		  System.out.println("avgHealth: " + avgHealth);
		  double activity = (double) orgData.getNumSteps();
		  System.out.println("activity: " + activity);
		  double goodEating = (double) orgData.getHealthEat()
				    * (orgData.getHealthEat() + orgData.getPoisonEat() + orgData
							 .getTotalScans())
				    / (GridPanel.numFoodSources);
		  System.out.println("goodEating: " + goodEating);
		  double assertion = (double) (orgData.getNumSteps()
				    + orgData.getNumAttacked() + orgData.getNumPushed())
				    / (orgData.getHealthEat() + 1);
		  System.out.println("assertion: " + assertion);
		  double badEating = (double) orgData.getPoisonEat() + 1;
		  System.out.println("badEating: " + badEating);
		  double fitness = (avgHealth * (activity + goodEating + assertion))
				    / badEating;
		  orgData.setFitness(fitness);
		  System.out.println();
		  return fitness;
	 }

	 public double fitnessDistanceTraveled(OrgData orgData) {
		  System.out.println(orgData.getSteps());
		  return orgData.getSteps();
	 }

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
	  * @param orgData
	  * @return
	  */
	 public double fitnessAvgHealthPerSteps(OrgData orgData) {
		  int numSteps = orgData.getNumSteps();
		  if (numSteps > 0)
			   return 0.0;
		  return (orgData.getHlthTot() / orgData.getSamples()) / orgData.getNumSteps();
	 }
	 
	 /**
	  * This fitness function evaluates the fitness of an Organism based on its
	  * average health per the number of steps that it had taken over a
	  * generation.
	  * 
	  * @param org
	  * @return
	  */
	 public double fitnessAverageHealthTimeOfDeathNumSteps(OrgData orgData) {
		  int numSteps = orgData.getNumSteps();
		  System.out.println("numsteps: " + orgData.getNumSteps());
		  System.out.println("timeofdeath: " + orgData.getTimeOfDeath());
		  System.out.println("healthtotal: " + orgData.getHlthTot());
		  orgData.setAverageHealth(orgData.getHlthTot()
				    / orgData.getTimeOfDeath());
		  System.out.println("avghealth: " + orgData.getAverageHealth());
		  double fitness = (orgData.getAverageHealth() / numSteps)
				    + orgData.getTimeOfDeath();
		  System.out.println("fit: " + fitness);
		  System.out.println();
		  orgData.setFitness(fitness);
		  return fitness;
	 }

	 /**
	  * Called by grid panel to perform all of the necessary actions that it
	  * takes to produce a new generation. An organism list is passed to the
	  * ctor and when this method is called, the population goes through
	  * 
	  * - tournament selection - mutation - rotation - 1-point cross over. -
	  * 2-point cross over.
	  * 
	  * @return
	  */
	 
	 public LinkedList<Organism> newGeneration(LinkedList<Organism> anOrgList) {

		  // List to hold the Elite individuals.
		  LinkedList<Chromosome> eliteList = new LinkedList<Chromosome>();
		  // Get the most elite indiv or individuals.
		  if (doElitism) {
			   // Called on a clone of the orgList because
			   eliteList = (LinkedList<Chromosome>) assembleElites(
						(LinkedList<Organism>) anOrgList.clone());
		  }
		  // ordering the original list may be a
		  // detriment to randomization.
		  LinkedList<Chromosome> chromList = 
			   makeChromList(tournament(partnerSelect(anOrgList)));
		  rotation(chromList);
		  mutation(chromList);
		  // Pair up Chromosomes in preparation
		  // for 1-point cross over.
		  LinkedList<Pair<Chromosome, Chromosome>> pairList =
			   mateSelect(chromList);
		  onePointCrossOver(pairList);
		  // Pair up Chromosomes again in preparation
		  // for 2-point cross over.
		  pairList = mateSelect(makeChrmListFrmPair(pairList));
		  // 2-point cross over.
		  onePointCrossOver(pairList);
		  onePointCrossOver(pairList);
		  LinkedList<Chromosome> finalChromes =
			   makeChrmListFrmPair(pairList);
		  // Proceed with elitism if true.
		  if (doElitism)
			   transferElites(finalChromes, eliteList);
		  // Update the evaluations of the genes.
		  for (Chromosome chrom : chromList)
			   for (Gene gene : chrom.subListGene(0, chrom.size()))
				    gene.updateEvaledList();
		  for (int i = 0; i < anOrgList.size(); i++)
			   anOrgList.get(i).setChromosome(chromList.get(i));
		  return anOrgList;
	 }

	 /**
	  * The same as newGeneration except with print lines for testing.
	  * 
	  * @return
	  */
	public LinkedList<Organism> newGenerationTest(
			LinkedList<Organism> anOrgList) {
		
		  printOrgListIdsAndFitness(anOrgList);
		  System.out.println("The chromosomes in the original gene list.");
		  printGenes(makeChromList(anOrgList));
		  // List to hold the Elite individuals.
		  LinkedList<Chromosome> eliteList = new LinkedList<Chromosome>();
		  // Get the most elite indiv or individuals.
		  if (doElitism) {
			// Called on a clone of the
				 // orgList because
			// Took out the call to assembleElitesTest. Replaced with
			// assembleElites.
			   eliteList = (LinkedList<Chromosome>) assembleElites(
						(LinkedList<Organism>) anOrgList.clone());
			   System.out.println("The elites are: ");
			   printChromeListIds(eliteList);
			   System.out.println("The elites genes are: ");
			   printChromGenes(eliteList);
		  }

		  // PRINT ORIGINAL ORGLIST.
		  System.out.println("Original orgList");
		  printOrgListIdsAndFitness(anOrgList);

		  // TEST PARTNERSELECT.
		  System.out.println("Testing Partner Select");
		  System.out.println();
		  out.println("orgListSize" + anOrgList.size());
		  out.println();
		  LinkedList<Pair<Organism, Organism>> partners =
			   partnerSelect(anOrgList);

		  // PRINT THE IDS OF THE PAIR OF ORGS AFTER
		  // PARTNERSELECT IS CALLED.
		  System.out.println("After partner select:");
		  printPartnerListIds(partners);

		  // TEST TOURNAMENT.
		  LinkedList<Organism> afterTournOrgs = tournament(partners);
		  System.out.println("afterTournOrgs size: " +
				    afterTournOrgs.size());

		  // TOURNAMENT WITH PRINT.
		  /*
		   * LinkedList<Organism> afterTournOrgs =
		   * gep.tournamentWithPrint(partners); System.out.println();
		   * System.out.println("afterTournOrgs size: " + afterTournOrgs.size());
		   */

		  // HANDICAP TOURNAMENT WITH PRINT.
		  /*
		   * LinkedList<Organism> afterTournOrgs =
		   * gep.tournamentHandicapWithPrint(partners);
		   * System.out.println("After the tournament");
		   * gep.printOrgListIdsAndFitness(afterTournOrgs);
		   */

		  // TEST MATESELECT
		  // MAKE A NEW CHROMLIST.
		  LinkedList<Chromosome> chromList = makeChromList(afterTournOrgs);

		  // PRINT THE IDS OF THE CHROMOSOMES IN THE
		  // CHROME LIST AFTER TOURNAMENT OCCURS.
		  System.out.println("Chromosome ids in list after tournament:");
		  printChromeListIds(chromList);

		  // MUTATION AND ROTATION.
		  System.out.println("Before rotation");
		  printGenes(chromList);
		  rotation(chromList);
		  System.out.println("After rotation");
		  printGenes(chromList);
		  mutation(chromList);
		  System.out.println("After mutation");
		  printGenes(chromList);

		  // Pair up Chromosomes in preparation
		  // for 1-point cross over.
		  LinkedList<Pair<Chromosome, Chromosome>> pairList =
			   mateSelect(chromList);
		  printMateListIds(pairList);

		  // ONE POINT CROSS OVER.
		  System.out.println("Genes before cross");
		  printChromGenes(makeChrmListFrmPair(pairList));
		  onePointCrossOver(pairList);
		  System.out.println("After 1st onePoint cross over");
		  printChromGenes(makeChrmListFrmPair(pairList));

		  // Pair up Chromosomes again in preparation
		  // for 2-point cross over.
		  pairList = mateSelect(makeChrmListFrmPair(pairList));
		  /*
		   * System.out.println("ids after 1 point");
		   * printChromeListIds(makeChrmListFrmPair(pairList));
		   */

		  // 2-POINT CROSS OVER.
		  onePointCrossOver(pairList);
		  onePointCrossOver(pairList);
		  LinkedList<Chromosome> finalChromes =
			   makeChrmListFrmPair(pairList);
		  System.out.println("After 2-point");
		  printGenes(finalChromes);
		  /* printChromeListIds(finalChromes); */

		  // PROCEED WITH ELITISM IF TRUE.
		  if (doElitism) {
			   transferElites(finalChromes, eliteList);
			   printChromeListIds(finalChromes);
			   System.out.println("After tranferring the elites.");
			   printChromGenes(finalChromes);
		  }
		  // UPDATE THE EVALUATIONS OF THE GENES.
		  for (Chromosome chrom : chromList)
			   for (Gene gene : chrom.subListGene(0, chrom.size()))
				    gene.updateEvaledList();

		  System.out.println("without set: ");
		  printChromGenes(makeChromList(anOrgList));

		  // TODO: Shouldn't have to reset the orgLists chromosomes. The
		  // objects
		  // themselves should be changed already.
		  for (int i = 0; i < anOrgList.size(); i++)
			   anOrgList.get(i).setChromosome(chromList.get(i));

		  System.out.println("with set");
		  printChromGenes(makeChromList(anOrgList));
		  return anOrgList;
	 }
	 
	 /**
	  * This method retreives the most elite members of the organism list. The
	  * number of individuals depends on what the numElites feild is set to.
	  * 
	  * @param anOrgList
	  *              An organism list to find the most fit individuals from.
	  */
	 public List<Chromosome> assembleElites(LinkedList<Organism> anOrgList) {
		  LinkedList<Organism> tempOrgList = (LinkedList<Organism>) anOrgList
				    .clone();
		  Collections.sort(tempOrgList);
		  System.out.println("In assembleelites method");
		  for (Organism organism : tempOrgList) {
			  System.out.println("Id: " + organism.getId() + " fitness: " + organism.getFitness());
		}
		  List<Chromosome> eliteList = new LinkedList<Chromosome>();
		  for (int i = 0; i < numElites; i++)
			   eliteList.add(tempOrgList.pollLast().getChromosome());
		  return eliteList;
	 }

	 /**
	  * The same as assembleElites except for with print lines.
	  * 
	  * @param anOrgList
	  * @return
	  */
	 /*public List<Chromosome> assembleElitesTest(LinkedList<Organism> anOrgList) {
		  System.out.println("before ordering: ");
		  printOrgListIdsAndFitness(anOrgList);
		  Collections.sort(anOrgList);
		  System.out.println("after ordering: ");
		  printOrgListIdsAndFitness(anOrgList);
		  List<Chromosome> eliteList = new LinkedList<Chromosome>();

		  for (int i = 0; i < numElites; i++)
			   eliteList.add(anOrgList.pollLast().getChromosome());
		  return eliteList;
	 }*/

	 /**
	  * Takes in a chromosome list, aFinalChromes, and a list of the most elite
	  * member's chromosomes of the population, and transfers the chromosomes
	  * of the elite members into aFinalChromes. The number of chromosomes in
	  * the elite list is the number of chromosomes that will be randomly
	  * replaced aFinalChromes list.
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
		  while (!indexList.isEmpty())
			   aFinalChromes.set(indexList.pop(), anEliteList.pop());
	 }

	 /**
	  * Pairs up indiv from the Organism Pair list parameter and makes them
	  * into Pair objects. Puts Pairs into a LinkedList.
	  * 
	  * @param population
	  *              - A list of organisms from which the pairs are to be
	  *              selected.
	  * @return - A list of the pairs of organisms.
	  * 
	  */
	 public LinkedList<Pair<Organism, Organism>> partnerSelect(
			   LinkedList<Organism> population) {
		  // pairList: list that will recieve the pairs
		  // of partners.
		  LinkedList<Pair<Organism, Organism>> pairList =
			   new LinkedList<Pair<Organism, Organism>>();
		  // notSeenMap:
		  // key: Each org from population.
		  // val: A list of organisms that they have not
		  // seen yet.
		  HashMap<Organism, LinkedList<Organism>> notSeenMap =
			   new HashMap<Organism, LinkedList<Organism>>();
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
			   if (selection.isEmpty())
				    selection = (LinkedList<Organism>) population.clone();
			   // Select a random individual from the notSeenList
			   // Then remove that individual.
			   Organism partner2 = selection.remove(ran.nextInt(selection
						.size()));
			   // Remove the partner1 from partner2's notSeenList.
			   notSeenMap.get(partner2).remove(partner1);
			   pairList.add(new Pair<Organism, Organism>(partner1, partner2));
		  }
		  return pairList;
	 }

	 /**
	  * @param partnerList
	  *              - A LinkedList of organism Pairs. Each member of a pair
	  *              will compete with the other member.
	  * @param tournProb
	  *              -the probability at which either the more fit, or the less
	  *              fit individual will be chosen.
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
	  *              - A LinkedList of organism Pairs. Each member of a pair
	  *              will compete with the another organism from the
	  *              population. There will be no duplicate pairs in the
	  *              returned list.
	  * @param tournProb
	  *              -the probability at which either the more fit, or the less
	  *              fit individual will be chosen.
	  * @param handicap
	  *              - Handicap to false means that the tournProb probability
	  *              will be in favor of the most fit individuals. Handicap to
	  *              true means that the prob will favor the less fit
	  *              individuals.
	  * @return returns each winner of a match, as a Chromosome, in a
	  *         LinkedList.
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
						System.out.println("org1Fit: " + fit1
								  + " <= org2Fit: " + fit2);
						newPop.add(org2);
				    } else if (rand <= tournProb) { // Handicap on and
											 // probability
						// is won.
						System.out.println("Won!! Handicap on: " + rand
								  + " <= " + tournProb);
						System.out.println("org1Fit: " + fit1
								  + " <= org2Fit: " + fit2);
						newPop.add(org1);
						countWon++;
				    } else { // Handicap is on and probability is lost.
						System.out.println("Handicap on But!: " + rand
								  + " > " + tournProb);
						System.out.println("org1Fit: " + fit1
								  + " < org2Fit: " + fit2);
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
		  LinkedList<Chromosome> competitors =
			   (LinkedList<Chromosome>) aChromList.clone();
		  // If Competitors has an odd cardinality, then just append a random
		  // member to the end.
		  if (competitors.size() % 2 == 1)
			   competitors.add(competitors.get(ran.nextInt(competitors
						.size())));
		  while (!competitors.isEmpty()) {
			   Chromosome chrom1 = competitors.remove(ran
						.nextInt(competitors.size()));
			   Chromosome chrom2 = competitors.remove(ran
						.nextInt(competitors.size()));
			   /* System.out.println(chrom1.getId() + " <=> " + chrom2.getId()); */
			   pairList.add(new Pair<Chromosome, Chromosome>(
						chrom1, chrom2));
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
				    aPairList.get(i).getFst()
							 .crossOver(aPairList.get(i).getSnd());
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

	 public void setNumElites(int numElites) {
	       this.numElites = numElites;
	 }

	 public int getNumElites() {
	       return numElites;
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
	 public void printOrgListIds(List<Organism> anOrgList) {
		  out.println("Printing orgListIds");
		  System.out.println();
		  for (int i = 0; i < anOrgList.size(); i++) {
			   Organism org = anOrgList.get(i);
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
						out.print(chrom.getGene(j).getSym(k).charValue()
								  + " ");
				    }
				    out.println();
			   }
		  }
		  System.out.println();
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
	 public void printChromeListIds(LinkedList<Chromosome> chromeList) {

		  System.out.println("Size: " + chromeList.size());
		  System.out.println("Printing chromosome ids: ");
		  for (Chromosome chrom : chromeList)
			   System.out.println(chrom.getId());
		  System.out.println();
	 }

	 /**
	  * @param mates
	  */
	 public void printMateListIds(
			   LinkedList<Pair<Chromosome, Chromosome>> mates) {
		  out.println("chrom pair list size: " + mates.size());
		  out.println("chrom pair list with Ids:");
		  for (Pair<Chromosome, Chromosome> partners : mates) {
			   Chromosome c1 = partners.getFst();
			   Chromosome c2 = partners.getSnd();
			   out.println(c1.getId() + " <=> " + c2.getId());
		  }
		  out.println();
	 }

	 /*public static void main(String[] args) {
		  LinkedList<Organism> orgList = new LinkedList<Organism>();
		  Random r = new Random();

		  // CASE: EVEN NUMBER OF ORGANISMS.
		  for (int i = 0; i < 4; i++)
		     orgList.add(new Organism(true, 4, r.nextInt(20), i));
		  GEP gep = new GEP(1.00, 1.00, 1.00, 1.00, 1.00, 2, false,
		  	    true);
		  gep.newGenerationTest(orgList);
		  
		  // CASE: ODD NUMBER OF ORGANISMS.
		  LinkedList<Organism> orgList2 = new LinkedList<Organism>();
		  for (int i = 0; i < 5; i++)
			   orgList2.add(new Organism(true, 4, r.nextInt(20), i));
		  GEP gep2 = new GEP(1.00, 1.00, 1.00, 1.00, 1.00, 2,
				    false, false); // Elitism ctor.
		  gep2.newGenerationTest(orgList2);
		  
		  // CASE USING NEW PARAMETERIZED NEW GENERATION.
		  LinkedList<Organism> orgList3 = new LinkedList<Organism>();
		  for (int i = 0; i < 5; i++)
			   orgList3.add(new Organism(true, 4, r.nextInt(20), i));
		  GEP gep3 = new GEP(1.00, 1.00, 1.00, 1.00, 1.00, 2,
				    false, false); // Elitism ctor.
		  gep3.newGenerationTest(orgList3);
	 }*/
}