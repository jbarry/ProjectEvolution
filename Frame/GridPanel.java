package Frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import Evaluation.Expr;
import Evaluation.Normalizer;
import Evolution.GEP;
import Interactive.Chromosome;
import Interactive.Food;
import Interactive.Gene;
import Interactive.HealthyFood;
import Interactive.Matter;
import Interactive.OrgData;
import Interactive.Organism;
import Interactive.Pair;
import Interactive.PoisonousFood;

/**
 * This class will handle all of the simulation's display.
 */
@SuppressWarnings("all")
public class GridPanel extends JPanel {
	// ------------------------------------------------------------------------------------
	// --globals--
	// ------------------------------------------------------------------------------------
	public final static int WIDTH = 3 * GUI.WIDTH / 4;
	public final static int HEIGHT = 2 * GUI.HEIGHT / 3;

	/*public static Pair<Integer, Character>[][] locationMap;*/
	private LocationMap locationMap;
	private LinkedList<Organism> organisms;
	private LinkedList<HealthyFood> healthFd;
	private LinkedList<PoisonousFood> poisFood;
	private LinkedList<Food> foodList;
	private LinkedList<Organism> orgsUsed;
	private ArrayList<OrgData> orgDataList;
	private ArrayList<Integer> shuffleIds;
	private ArrayList<String> shuffleStringIds;
	private int lengthTimeStep = 100;
	private int lengthGeneration = 500;
	private int timePassed = 0;
	private int trialsPerGen = 1;
	public int trialNum = 1;
	public int generationNum = 1;
	public double lastAvg = 0;
	private GEP g;
	public static int numFoodSources = 0;
	private Timer t;
	private Normalizer norm;
	private int numPreProcessedGenerations = 0;
	private Random ran;
	private GUI gui;

	// ------------------------------------------------------------------------------------
	// --constructors--
	// ------------------------------------------------------------------------------------
	/**
	 * This constructor will handle all of the movements/interactions of all
	 * objects in the current game state.
	 */
	public GridPanel(final GUI aGui) {
		gui = aGui;
		Runnable r = new Runnable() {
			@Override
			public void run() {
				// initial JPanel settings
				setLayout(null);
				setLocation(GUI.WIDTH - GridPanel.WIDTH, 0);
				setSize(GridPanel.WIDTH, GridPanel.HEIGHT);
				setBorder(BorderFactory.createLineBorder(Color.black));

				organisms = new LinkedList<Organism>();
				healthFd = new LinkedList<HealthyFood>();
				poisFood = new LinkedList<PoisonousFood>();
				foodList = new LinkedList<Food>();
				// track user mouse movement.
				addMouseMotionListener(new MouseMotionListenerClass(
						GridPanel.this));
				// handle other mouse events
				addMouseListener(new MouseListenerClass());
				t = new javax.swing.Timer(lengthTimeStep, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (timePassed < lengthGeneration) {
							gui.updatePercentage((double) timePassed
									/ lengthGeneration);
							simStep(1, 2);
							repaint();
							timePassed++;
						} else if (trialNum < trialsPerGen)
							newTrial();
						else {
							newGenerationAstar();
						}
						repaint();
					}
				});
			}
		};
		r.run();
	}

	public int getGenerationNum() {
		return generationNum;
	}

	public void revert(int generation) {
		t.stop();
		generationNum = generation;
		timePassed = 0;
		locationMap.clearLocations();
		for (int i = 0; i < organisms.size(); i++) {
			Organism org = organisms.get(i);
			OrgData orgData = orgDataList.get(i);
			org.setChromosome(orgData.goBack(generation));
			orgData.reinitializeVariables();
			locationMap.newLocation(org.getLocation(), org.getWidth(),
					org.getHeight(), org.getId(), 'o');
		}
		GUI.genPanel.removeGenerations(generation);
		foodList.clear();

		for (int i = 0; i < numFoodSources * 2; i++) {
			if (ran.nextBoolean())
				foodList.add(new HealthyFood(100.00, i, 2));
			else
				foodList.add(new PoisonousFood(100.00, i, 2));
			locationMap.placeFood(foodList.get(i));
		}
	}

	/**
	 * @param org
	 * @param aOrgsUsed
	 */
	public void attack2(Organism org, ArrayList<Organism> aOrgsUsed) {
		ArrayList<Integer> surroundingOrganisms = org.getSurroundingObjects(
				'o', 1);
		surroundingOrganisms.remove((Integer) org.getId());
		if (surroundingOrganisms.size() > 0) {
			int index = surroundingOrganisms.get(ran
					.nextInt(surroundingOrganisms.size()));
			org.attack2(index, aOrgsUsed);
		} else {
			org.setAction("Attempted to attack");
		}
	}

	/**
	 * Checks to see if a specific food is in range and returns whether or not
	 * the organism died while eating, if the food is in range.
	 * 
	 * @param org
	 * @param aFood
	 * @return
	 */
	public boolean eatFood(Organism org, OrgData orgData, int fdId) {
		Food aFood = foodList.get(fdId);
		if (org.matterInRange(aFood.getId(), aFood.getType(), 5)) {
			orgData.addEatFoodData(aFood.getType(), aFood.getId());
			if (deplete(foodList.get(fdId), 5))
				foodDead(foodList.get(fdId), "food died on eat.");
			return org.eatFood(5 * aFood.getTypeInt());
		}
		return false;
	}

	/**
	 * @param org
	 */
	public void attack(Organism org) {
		ArrayList<Integer> surroundingOrganisms = org.getSurroundingObjects(
				'o', 1);
		surroundingOrganisms.remove((Integer) org.getId());
		if (surroundingOrganisms.size() > 0) {
			int index = surroundingOrganisms.get(ran
					.nextInt(surroundingOrganisms.size()));
			org.attack(index, organisms);
		} else
			org.setAction("Attempted to attack");
	}

	public void push(Organism org) {
		ArrayList<Integer> surroundingOrganismsPush = org
				.getSurroundingObjects('o', 1);
		surroundingOrganismsPush.remove((Integer) org.getId());
		if (surroundingOrganismsPush.size() > 0) {
			int index = surroundingOrganismsPush.get(ran
					.nextInt(surroundingOrganismsPush.size()));
			// System.out.println(org.getId() + " " + index);
			/*org.pushOrg(index, organisms);*/
		} else
			org.setAction("Attempted to push");
	}

	public void newTrial() {
		t.stop();
		shuffleIds.clear();
		/*for (Organism o : organisms) {
			o.newLocation();
			o.setHealth(o.getMaxHealth());
			o.clear();
			shuffleIds.add(o.getId());
		}*/
		for (int i = 0; i < organisms.size(); i++) {
			Organism org = organisms.get(i);
			OrgData orgData = orgDataList.get(i);
			org.setFitness(g.fitness(orgData));
			orgData.clearEatFail();
			orgData.clearFoodList();
			orgData.reinitializeVariables();
			org.newLocation();
			org.setHealth(orgData.getMaxHealth());
			shuffleIds.add(org.getId());
		}
		trialNum++;
		healthFd.clear();
		poisFood.clear();
		for (int i = 0; i < numFoodSources; i++) {
			HealthyFood h = new HealthyFood(100.0, i, 2);
			PoisonousFood f = new PoisonousFood(100.0, i, 2);
			healthFd.add(h);
			poisFood.add(f);
		}
		timePassed = 0;
		if (!GUI.genPanel.resumeHasNotBeenClicked()
				&& !GUI.genPanel.genIsSelected()) {
			GUI.genPanel.enableResumeSimulation();
			gui.toggleAllPauses(false);
		} else {
			t.start();
			GUI.genPanel.newTrial();
		}
	}

	public void preProcess(int generations) {
		System.out.println("process");
		numPreProcessedGenerations = 0;
		while (numPreProcessedGenerations < generations) {
			System.out.println("Processing Generation " + generationNum);
			while (timePassed < lengthGeneration) {
				simStep(1, 2);
				timePassed++;
			}
			newGenerationAstar();
			numPreProcessedGenerations++;
		}
	}

	// ------------------------------------------------------------------------------------
	// --Getters/Setters--
	// ------------------------------------------------------------------------------------
	public LinkedList<Organism> getOrganisms() {
		return organisms;
	}

	public LinkedList<HealthyFood> getHealthyFoodList() {
		return healthFd;
	}

	public LinkedList<PoisonousFood> getPoisonousFoodList() {
		return poisFood;
	}

	public GEP getGEP() {
		return g;
	}

	public Timer getTimer() {
		return t;
	}

	public int getCurrTimeStep() {
		return lengthTimeStep;
	}

	public void setTimeStep(int step) {
		lengthTimeStep = step;
		t.setDelay(step);
	}

	public void start() {
		t.start();
	}

	public void stop() {
		t.stop();
	}

	public boolean isPaused() {
		if (t.isRunning())
			return false;
		return true;
	}

	// TODO: Organize the logic in this method. By logic I mean logical
	// progression.
	public void newGenerationAstar() {
		timePassed = 0;
		trialNum = 1;
		generationNum++;
		// Clear the location map.
		locationMap.clearLocations();
		shuffleIds.clear();
		// sum is used for displaying data to generation panel.
		double sum = 0;
		// Assign organism fitness and reinitialize organisms.
		for (int i = 0; i < organisms.size(); i++) {
			Organism org = organisms.get(i);
			// Organisms corresponding data object.
			OrgData orgData = orgDataList.get(i);
			// Asses fitness of each organism.
			org.setFitness(g.fitness(orgData));
			sum += org.getFitness();
		}
		// Perform the evolutionary process on the organism's in the organism
		// list.
		organisms = g.newGeneration(organisms);
		// Reinitialize food list.
		foodList.clear();
		for (int i = 0; i < numFoodSources * 2; i++) {
			if (ran.nextBoolean())
				foodList.add(new HealthyFood(100.00, i, 2));
			else
				foodList.add(new PoisonousFood(100.00, i, 2));
			// Place food on locationMap.
			locationMap.placeFood(foodList.get(i));
		}
		// Re-initialization of the organisms and the OrgData objects are done
		// in this loop. New positions for organisms and food objects are done
		// as well.
		for (int i = 0; i < organisms.size(); i++) {
			// An organism.
			Organism org = organisms.get(i);
			// organisms corresponding data object.
			OrgData orgData = orgDataList.get(i);
			Chromosome chrom = org.getChromosome();
			// Update the new symLists to an evaluated expression.
			// TODO: Make Gene and Chromosome implement iterable.
			for (int j = 0; j < chrom.size(); j++)
				chrom.getGene(j).updateEvaledList();
			// Re-initialize all of the organism's data.
			orgData.reinitializeVariables();
			org.reinitialize();
			orgData.addChromosome(org.getChromosome());
			// Place the organisms on the locationMap.
			locationMap.placeOrganism(org);
			org.setHealth(orgData.getMaxHealth());
			shuffleIds.add(org.getId());
		}
		// Set the most elite Individuals.
		ArrayList<Integer> eliteIdList = (ArrayList<Integer>) g
				.getEliteIdList();
		for (int i = 0; i < eliteIdList.size(); i++)
			organisms.get(eliteIdList.get(i)).setElite(true);
		// Calculate lastAvg for the Generation Panel.
		lastAvg = sum / OptionsPanel.numOrganisms;
		// Set the generation panel data information.
		GUI.genPanel.addGeneration();
		if (!GUI.genPanel.resumeHasNotBeenClicked()) {
			GUI.genPanel.enableResumeSimulation();
			gui.toggleAllPauses(false);
		} else {
			t.start();
			GUI.genPanel.newGeneration();
			System.out.println("restarting");
		}
	}

	/**
	 * Sets the initial game state of the GridPanel making use of OrgData
	 * objects.
	 */
	public void initialize() {
		generationNum = 1;
		trialNum = 1;
		GUI.genPanel.resetGenInformation();
		ran = new Random();
		timePassed = 0;
		shuffleIds = new ArrayList<Integer>();
		orgDataList = new ArrayList<OrgData>();
		organisms.clear();
		foodList.clear();
		norm = new Normalizer(new Pair<Double, Double>(-600.0, 600.0),
				new Pair<Double, Double>(-50.0, 50.0));
		locationMap = LocationMap.getInstance();
		numFoodSources = OptionsPanel.numOrganisms / 5;
		// Initialize the organisms in the organisms list and have the
		// locationMap singleton object place that organism on the map.
		// Initialize the OrgData objects with an id corresponding to an
		// organism.
		for (int i = 0; i < OptionsPanel.numOrganisms; i++) {
			Organism org = new Organism(100.00, 2, i);
			OrgData orgData = new OrgData(org.getMaxHealth(), i);
			orgData.addChromosome(org.getChromosome());
			orgDataList.add(orgData);
			organisms.add(org);
			locationMap.placeOrganism(org);
			shuffleIds.add(i);
		}
		// Initialize the food sources and allow locationMap to find a starting
		// position for them.
		for (int i = 0; i < numFoodSources * 2; i++) {
			if (ran.nextBoolean())
				foodList.add(new HealthyFood(100.00, i, 2));
			else
				foodList.add(new PoisonousFood(100.00, i, 2));
			locationMap.placeFood(foodList.get(i));
		}
		g = new GEP(0.75, 0.01, 0.01, 0.82, 0.82, 2, false, true);
//		preProcess(2);
		t.start();
	}

	public void initializeFromGeneFile(LinkedList<Organism> orgList) {
		generationNum = 1;
		trialNum = 1;
		
		ran = new Random();
		timePassed = 0;
		shuffleIds = new ArrayList<Integer>();
		orgDataList = new ArrayList<OrgData>();
		organisms.clear();
		foodList.clear();
		norm = new Normalizer(new Pair<Double, Double>(-600.0, 600.0),
				new Pair<Double, Double>(-50.0, 50.0));
		locationMap = LocationMap.getInstance();
		locationMap.clearLocations();
		// Initialize the organisms in the organisms list and have the
		// locationMap singleton object place that organism on the map.
		// Initialize the OrgData objects with an id corresponding to an
		// organism.
		organisms = orgList;
		numFoodSources = organisms.size() / 5;
		GUI.genPanel.resetGenInformation();
		for (int i = 0; i < organisms.size(); i++) {
			Organism org = organisms.get(i);
			orgDataList.add(new OrgData(org.getMaxHealth(), org.getId()));
			locationMap.placeOrganism(org);
			shuffleIds.add(i);
		}
		// Initialize the food sources and allow locationMap to find a starting
		// position for them.
		for (int i = 0; i < numFoodSources * 2; i++) {
			if (ran.nextBoolean())
				foodList.add(new HealthyFood(100.00, i, 2));
			else
				foodList.add(new PoisonousFood(100.00, i, 2));
			locationMap.placeFood(foodList.get(i));
		}
		g = new GEP(0.75, 0.01, 0.01, 0.82, 0.82, 2, false, true);
		/*preProcess(2);*/
		t.start();
	}

	/**
	 * Uses: Astar, there is no eating, and the closed list is in the OrgData
	 * object.
	 * 
	 * @param numActions
	 * @param healthDepletion
	 */
	private void simStep(int numActions, double healthDepletion) {
		Collections.shuffle(shuffleIds);
		for (int i = 0; i < foodList.size(); i++) {
			Food f = foodList.get(i);
			// Deplete food.
			if (deplete(f, (ran.nextInt(20) / 10)))
				foodDead(f, "food died! Replacing.");
		}
		// Loop through Organisms.
		mainLoop: for (int orgIndex = 0; orgIndex < shuffleIds.size(); orgIndex++) { // mainLoop.
			// If all the orgs are dead, then start a new generation.
			if (shuffleIds.isEmpty()) {
				timePassed = lengthGeneration;
				System.out.println("All dead.");
				break;
			}
			// Get an Organism corresponding the the ids in the shuffleIds
			// list.
			Organism org = organisms.get(shuffleIds.get(orgIndex));
			// The orgData object holds all of the data for this specific
			// organism.
			OrgData orgData = orgDataList.get(org.getId());
			orgData.incrementSumHealth(org.getHealth());
			// Closed list.
			ArrayList<Coordinate> closedList = (ArrayList<Coordinate>) orgData
					.getClosedList();
			closedList.add(org.getLocation());
			// lastFoodSourceIndex holds the index of the last food source
			// that was visited.
			orgData.setLastFoodSourceIndex(0);
			for (int numActionIndex = 0; numActionIndex < numActions; numActionIndex++) { // numActions
																						  // loop.
				// Sample for fitness function.
				orgData.incHlthTot();
				// depleteValue is the value to decrease the Organism's health
				// by at each time step.
				double depleteValue = orgData.getMaxHealth()
						/ (lengthGeneration / healthDepletion);
				// Check to see if the Organism is dead, if so remove that org
				// from the shuffleIds list.
				if (deplete(org, depleteValue)) {
					orgDead(org, orgData, "remove on deplete.");
					continue mainLoop;
				}
				Chromosome chrome = org.getChromosome();
				// Get the first food.
				int foodDestination = 0;
				// bestEval is used to hold the value of the evaluation
				// and the food source that the evaluation corresponds
				// to.
				// TODO: 1st gene is only evaled once.
				Pair<Integer, Double> bestEval = null;
				for (int geneIndex = 0; geneIndex < chrome.size(); geneIndex++) { // loopGenes.
					// First Gene in Chromosome.
					Gene currentGene = chrome.getGene(geneIndex);
					/*System.out.println("onGene: " + j);*/
					for (int foodIndex = 0; foodIndex < foodList.size(); foodIndex++) { // loopFood.
						// If the geneIndex and the food index 0, then set the
						// values of bestEval. bestEval is initialized in this
						// fashion to prevent the occurrence of the case where,
						// if bestEval is initialized outside of the geneLoop to
						// have left and right values of 0, 0, then when
						// comparing resulting evaluations of food sources to
						// each other, all of the evaluations, except the
						// evaluation of the first food source and the first
						// gene, turn out to be less than 0, but greater than
						// the evaluation of the first food source and the first
						// gene.
						if (geneIndex == 0 && foodIndex == 0)
							bestEval = new Pair<Integer, Double>(0,
									evaluateGeneFoodInRangeAstar(org,
											currentGene.getEvaledList(),
											foodList.get(0)));
						else {
							double aResult = evaluateGeneFoodInRangeAstar(org,
									currentGene.getEvaledList(),
									foodList.get(foodIndex));
							if (aResult > bestEval.getRight()) {
								foodDestination = foodIndex;
								/*System.out.println("replacedId: " + k);*/
								bestEval.setLeft(geneIndex);
								bestEval.setRight(aResult);
							}
						}
					} // End loopFood.
				} // End loopGenes.

				// This mechanism decides whether or not to clear the
				// closed list. The closed list should clear if the
				// organism has decided to make its way towards another
				// food source. If it decides to stay on the path to the
				// same food source, then it needs to keep its list of
				// previously visited nodes.
				if (orgData.getLastFoodSourceDestination() != foodDestination) {
					closedList.clear();
					orgData.setLastFoodSourceIndex(foodDestination);
				}
				// TODO: Later on replace foodDestination with
				// objectDestination.
				if (doActionAstar(org, orgData, bestEval,
						orgData.getLastFoodSourceDestination())) {
					shuffleIds.remove(new Integer(org.getId()));
					orgData.setTimeOfDeath(timePassed);
					continue mainLoop;
				}
			} // End NumAction Loop.
		} // End mainLoop.
	}

	/**
	 * @param org
	 * @param anOrgData
	 * @param bestEval
	 * @param aFoodDestination
	 * @return boolean indicating whether or not the organism died while
	 *         performing an action.
	 */
	public boolean doActionAstar(Organism org, OrgData anOrgData,
			Pair<Integer, Double> bestEval, int aFoodDestination) {
		switch (bestEval.getLeft()) {
			case 0:
				if (moveAstarUsingContains(org, aFoodDestination,
						(ArrayList<Coordinate>) anOrgData.getClosedList()))
					anOrgData.countStep();
				break;
			case 1:
				return eatFood(org, anOrgData, aFoodDestination);
		}
		return false;
	}

	/**
	 * @param org
	 * @param orgData
	 * @param aFoodDestination
	 * @return boolean indicating whether or not the organism moved.
	 */
	private boolean moveAstarUsingContains(Organism org, int aFoodDestination,
			List<Coordinate> closedList) {
		
		Food fd = foodList.get(aFoodDestination);
		PriorityQueue<Coordinate> sq = LocationMap.getInstance()
				.searchWithList(org.getLocation(), fd.getLocation(),
						org.getId());
		if (sq.peek().equals(org.getLocation()))
			return false;
		do {
			Coordinate move = sq.poll();
			// If queue is emtpy.
			if (move == null)
				return false;
			else {
				// Check to see if the move is on the closed list.
				if (!closedList.contains(move)) {
					closedList.add(move);
					org.moveTo(move);
					return true;
				} // Otherwise, get the next move.Hey 
			}
		} while (!sq.isEmpty());
		return false;
	}
	
	private double evaluateGene(Organism org, Gene currentGene) {

		HashMap<String, Double> environment = new HashMap<String, Double>();
		double orgX = norm.normalize(org.getLocation().getX());
		double orgY = norm.normalize(org.getLocation().getY());
		double health = norm.normalize(org.getHealth());
		double numSurroundingOrgs = norm.normalize(org.getSurroundingObjects(
				'o', 5).size() - 1);
		double foodX = norm.normalize(ran.nextDouble() * 100);
		double foodY = norm.normalize(ran.nextDouble() * 100);
		double orgNearFood = norm.normalize(0.0);
		double foodRemaining = norm.normalize(0.0);
		Expr result = currentGene.getEvaledList();
		environment.put("a", foodX - orgX);
		environment.put("b", orgY - foodY);
		environment.put("c", orgNearFood);
		environment.put("d", health);
		environment.put("e", foodRemaining);
		environment.put("f", norm.normalize(0.0));
		environment.put("g", numSurroundingOrgs);
		return result.evaluate(environment);
	}

	private double evaluateGeneFoodInRangeAstar(Organism org, Expr result,
			Food aFood) {
		LocationMap map = LocationMap.getInstance();
		HashMap<String, Double> environment = new HashMap<String, Double>();
		double orgX = norm.normalize(org.getLocation().getX());
		double orgY = norm.normalize(org.getLocation().getY());
		double foodX = norm.normalize(aFood.getLocation().getX());
		double foodY = norm.normalize(aFood.getLocation().getY());
		double distanceToFood = norm.normalize(map.distance(orgX, orgY, foodX,
				foodY));
		double health = norm.normalize(org.getHealth());
		double numSurroundingOrgs = norm.normalize(map.numSurroundingObjects(
				org.getLocation(), org.getWidth(), org.getHeight(), 10));
		double orgNearFood = norm.normalize(map.numSurroundingObjects(
				aFood.getLocation(), aFood.getWidth(), aFood.getHeight(), 10));
		double foodRemaining = norm.normalize(aFood.getHealth());
		environment.put("a", distanceToFood); // Represents distance to
		// food.
		environment.put("c", orgNearFood);
		environment.put("d", health);
		environment.put("e", foodRemaining);
		environment.put("f", aFood.getTypeInt());
		environment.put("g", numSurroundingOrgs);
		return result.evaluate(environment);
	}

	private double evaluateGeneFoodInRangeAstarTest(Organism org,
			Gene currentGene, Food aFood) {

		LocationMap map = LocationMap.getInstance();
		System.out.println("Inside evaluateGene");
		HashMap<String, Double> environment = new HashMap<String, Double>();
		double orgX = norm.normalize(org.getLocation().getX());
		System.out.println("orgX: " + orgX);
		double orgY = norm.normalize(org.getLocation().getY());
		System.out.println("orgY: " + orgY);
		double foodX = norm.normalize(aFood.getLocation().getX());
		System.out.println("foodX: " + foodX);
		double foodY = norm.normalize(aFood.getLocation().getY());
		System.out.println("foodY: " + foodY);
		double distanceToFood = norm.normalize(map.distance(orgX, orgY, foodX,
				foodY));
		System.out.println("distanceToFood: " + distanceToFood);
		double health = norm.normalize(org.getHealth());
		System.out.println("health: " + health);

		double numSurroundingOrgs = norm.normalize(map.numSurroundingObjects(
				org.getLocation(), org.getWidth(), org.getHeight(), 10));
		System.out.println("aroundOrg wo norm: "
				+ map.numSurroundingObjects(org.getLocation(), org.getWidth(),
						org.getHeight(), 10));
		/*System.out.println("numSurroundOrg: " + org.getId() + " is: "
				+ numSurroundingOrgs);*/
		double orgNearFood = norm.normalize(map.numSurroundingObjects(
				aFood.getLocation(), aFood.getWidth(), aFood.getHeight(), 10));
		/*System.out.println("orgNearFood: " + aFood.getId() + " is: "
				+ orgNearFood);*/
		System.out.println("aroundfood wo norm: "
				+ map.numSurroundingObjects(aFood.getLocation(),
						aFood.getWidth(), aFood.getHeight(), 10));
		double foodRemaining = norm.normalize(aFood.getHealth());
		System.out.println("foodRemaining: " + foodRemaining);
		Expr result = currentGene.getEvaledList();
		environment.put("a", distanceToFood); // Represents distance to
		// food.
		environment.put("c", orgNearFood);
		environment.put("d", health);
		environment.put("e", foodRemaining);
		environment.put("f", aFood.getTypeInt());
		environment.put("g", numSurroundingOrgs);
		double resultDoub = result.evaluate(environment);
		System.out.println("ResultDoub: " + resultDoub);
		System.out.println();
		System.out.println();
		return result.evaluate(environment);
	}

	private double evaluateGeneFoodInRangeAstarNoNorm(Organism org,
			Gene currentGene, Food aFood) {

		LocationMap map = LocationMap.getInstance();
		HashMap<String, Double> environment = new HashMap<String, Double>();
		double orgX = org.getLocation().getX();
		double orgY = org.getLocation().getY();
		double foodX = aFood.getLocation().getX();
		double foodY = aFood.getLocation().getY();
		double distanceToFood = map.distance(orgX, orgY, foodX, foodY);
		double health = org.getHealth();
		double numSurroundingOrgs = map.numSurroundingObjects(
				org.getLocation(), org.getWidth(), org.getHeight(), 10);
		double orgNearFood = map.numSurroundingObjects(aFood.getLocation(),
				aFood.getWidth(), aFood.getHeight(), 10);
		double foodRemaining = aFood.getHealth();
		Expr result = currentGene.getEvaledList();
		environment.put("a", distanceToFood); // Represents distance to
		// food.
		environment.put("c", orgNearFood);
		environment.put("d", health);
		environment.put("e", foodRemaining);
		environment.put("f", aFood.getTypeInt());
		environment.put("g", numSurroundingOrgs);
		return result.evaluate(environment);
	}

	/**
	 * This method gets all returns a list of all the Food objects that are in
	 * the org's sight range.
	 * 
	 * @param org
	 * @return
	 */
	// Uses abstract Food class instead of HealthyFood and Poisonous Food.
	private List<Food> collectFoodInRange(Organism org, int aRange) {
		// List of Ids of the food in range.
		ArrayList<Integer> foodInRangeIds = (ArrayList<Integer>) org
				.getFoodInRange(aRange);
		// List to be filled with actual food objects.
		LinkedList<Food> foodInRange = new LinkedList<Food>();
		// Fill the healthyFoodInRange list with food objects obtained from
		// ids.
		if (!foodInRangeIds.isEmpty())
			for (Integer integer : foodInRangeIds)
				foodInRange.add(foodList.get(integer));
		return foodInRange;
	}

	/**
	 * Deplete the health of any matter to the appropriate value.
	 * 
	 * @param m
	 * @param val
	 * @return
	 */
	public boolean deplete(Matter m, double val) {
		if (m.getHealth() - val < 0) {
			m.setHealth(0);
			return true;
		} else
			m.setHealth(m.getHealth() - val);
		return false;
	}
	
	public void orgDead(Organism org, OrgData orgData, String message) {
		// TODO: Decorate with dead org class.
		orgData.setTimeOfDeath(timePassed);
		System.out.println(message);
		shuffleIds.remove(new Integer(org.getId()));
		Coordinate orgLocation = org.getLocation();
		locationMap.setRangeToBlank(orgLocation.getX(), orgLocation.getY(),
				org.getWidth(), org.getHeight());
	}
	
	public void foodDead(Food food, String message) {
		// TODO: Decorate with dead food class.
		System.out.println(message);
		food.setHealth(food.getMaxHealth());
		Coordinate foodLocation = food.getLocation();
		locationMap.setRangeToBlank(foodLocation.getX(), foodLocation.getY(),
				food.getWidth(), food.getHeight());
		locationMap.placeFood(food);
	}

	public String getOrganismData(int index) {
		OrgData orgData = orgDataList.get(index);
		Organism org = organisms.get(index);
		return orgData.getId() + " " + org.getHealth() + " " + org.getFitness()
				+ " " + org.getLocation().getX() + " "
				+ org.getLocation().getY() + " " + orgData.getHealthEatSize() + " "
				+ orgData.getPoisonEatSize() + " " + orgData.getEatFail() + " "
				+ orgData.getNumAttacked() + " " + orgData.getNumPushed() + " "
				+ orgData.getTotalScans();
	}
	
	// ------------------------------------------------------------------------------------
	// --Override Functions--
	// ------------------------------------------------------------------------------------
	/**
	 * A function that will be called to update the current state of the panel
	 * 
	 * @param g
	 *            the Graphics object used
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		gui.getMap().paint(g);
		// handle each new organism created.
		for (Organism org : organisms)
			org.paint(g);
		/*for (Integer i : shuffleIds)
			organisms.get(i).paint(g);*/
		for (Food f : foodList)
			if (f.getHealth() > 0)
				f.paint(g, false);
	}
}