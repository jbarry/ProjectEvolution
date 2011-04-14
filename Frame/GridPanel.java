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
							timePassed++;
							/* simulateStep(); */
							/*simulateStep2(2);*/
							/*simulateStep2Test(4);*/
							/*simulateStepAstar(1);*/
							/*simulateStepAstarWithoutEat(4);*/
							gui.updatePercentage((double) timePassed
									/ lengthGeneration);
							/*simulateStepAstarWithoutEatWithClosedList(7);*/
							/*simulateStepAstarWithoutEatWithClosedListOrgData(5);*/
							simulateStepAstarClosedListOrgDataLoopGenes(1);
							/*simulateStepAstarClosedListOrgDataTest(1);*/
							repaint();
						} else if (trialNum < trialsPerGen)
							newTrial();
						else {
							/*newGeneration();*/
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
		for (Organism org : organisms) {
			/*org.goBack(generation)*/;
		}
		GUI.genPanel.removeGenerations(generation);
		healthFd.clear();
		poisFood.clear();
		foodList.clear();

		for (int i = 0; i < numFoodSources; i++) {
			HealthyFood h = new HealthyFood(100.0, i, 2);
			PoisonousFood f = new PoisonousFood(100.0, i, 2);
			healthFd.add(h);
			poisFood.add(f);
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
	 * @param org
	 * @return
	 */
	public boolean eatFoodInRange(Organism org) {
		LinkedList<Food> foodToEatList = (LinkedList<Food>) collectFoodInRange(
				org, 3);
		Food foodToEat;
		if (!foodToEatList.isEmpty()) {
			foodToEat = foodToEatList.get(0);
			/*org.printInfo();
			System.out.println("Ate food");
			foodToEat.printInfo();*/
			return org.eatFood(5 * foodToEat.getFoodType());
		} else
			return false;
	}

	/**
	 * Checks to see if a specific food is in range and returns whether or not
	 * the organism died while eating, if the food is in range.
	 * 
	 * @param org
	 * @param aFood
	 * @return
	 */
	public boolean eatFood(Organism org, Food aFood) {
		if (org.matterInRange(aFood.getId(), aFood.getType(), 5)) {
			System.out.println("Ate Food");
			return org.eatFood(5 * aFood.getFoodType());
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
			org.setFitness(g.fitnessAverageHealthTimeOfDeathNumSteps(orgData));
			orgData.clear();
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
			// An organism.
			Organism org = organisms.get(i);
			// organisms corresponding data object.
			OrgData orgData = orgDataList.get(i);
			// Asses fitness of each organism.
			org.setFitness(g.fitnessAverageHealthTimeOfDeathNumSteps(orgData));
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
		// in
		// this loop. New positions for organisms and food objects are done as
		// well.
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
			orgData.clear();
			orgData.addChromosome(org.getChromosome());
			// Place the organisms on the locationMap.
			locationMap.placeOrganism(org);
			/*locationMap.newLocation(org.getLocation(), Organism.width,
					Organism.height, org.getId(), 'o');*/
			org.setHealth(orgData.getMaxHealth());
			shuffleIds.add(org.getId());
		}

		/*locationMap.placeOrganisms(organisms);*/
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
			repaint();
			System.out.println("restarting");
		}
	}

	/**
	 * Preprocess generations Essentially run the simulation without updating
	 * the graphics.
	 */

	public void preProcess(int generations) {
		for (numPreProcessedGenerations = 0; numPreProcessedGenerations < generations; numPreProcessedGenerations++) {

			System.out.println("Processing Generation " + generationNum);
			while (timePassed < lengthGeneration) {
				/* begin game logic here: */
				for (int i = shuffleIds.size() - 1; i >= 0; i--)
					if (organisms.get(i).getHealth() <= 0)
						shuffleIds.remove(i);

				Collections.shuffle(shuffleIds);
				int orgIndex = 0;
				for (Integer orgNum : shuffleIds) {
					Organism org = organisms.get(orgNum);
					org.deplete(org.getMaxHealth() / lengthGeneration);
					org.clearAction();
					// Take sample of organism health for fitness.
					/*org.incHlthTot();*/
					if (org.getHealth() > 0) {
						ArrayList<Food> sight = new ArrayList<Food>();
						ArrayList<Integer> sightIDs = new ArrayList<Integer>();
						sightIDs = org.getSurroundingObjects('h', 20);
						for (Integer i : sightIDs) {
							sight.add(healthFd.get(i));
						}
						sightIDs.clear();
						sightIDs = org.getSurroundingObjects('p', 20);
						for (Integer i : sightIDs)
							sight.add(poisFood.get(i));
						/*org.addScan(sight.size())*/;
						double orgX = norm.normalize(org.getLocation().getX());
						double orgY = norm.normalize(org.getLocation().getY());
						double health = norm.normalize(org.getHealth());
						double numSurroundingOrgs = norm.normalize(org
								.getSurroundingObjects('o', 5).size() - 1);
						Chromosome chrom = org.getChromosome();
						Pair<Integer, Double> bestEval1 = new Pair<Integer, Double>(
								0, 0.0);
						Pair<Integer, Double> bestEval2 = new Pair<Integer, Double>(
								1, 0.0);
						for (int i = 0; i < chrom.size(); i++) {
							Gene workingGene = chrom.getGene(i);
							// if there is something in org's
							// field of vision.
							if (sight.size() > 0) {
								for (int j = 0; j < sight.size(); j++) {
									HashMap<String, Double> environment = new HashMap<String, Double>();
									Food f = sight.get(j);
									double foodX;
									double foodY;
									double orgNearFood;
									double foodRemaining;
									double isPoison = norm.normalize(1.0);
									if (f != null) {
										foodX = norm.normalize(f.getLocation()
												.getX());
										foodY = norm.normalize(f.getLocation()
												.getY());
										orgNearFood = norm.normalize(f
												.numSurroundingObjects(5));
										foodRemaining = norm.normalize(f
												.getHealth());

										if (f instanceof PoisonousFood)
											isPoison = -isPoison;
									} else {
										foodX = norm
												.normalize(ran.nextDouble() * 100);
										foodY = norm
												.normalize(ran.nextDouble() * 100);
										orgNearFood = norm.normalize(0.0);
										foodRemaining = norm.normalize(0.0);
									}
									Expr result = workingGene.getEvaledList();
									environment.put("a", foodX - orgX);
									environment.put("b", orgY - foodY);
									environment.put("c", orgNearFood);
									environment.put("d", health);
									environment.put("e", foodRemaining);
									environment.put("f", isPoison);
									environment.put("g", numSurroundingOrgs);
									double geneEval = result
											.evaluate(environment);
									if (geneEval > bestEval1.right()
											&& bestEval1.right() < bestEval2
													.right()) {
										bestEval1.setLeft(i);
										bestEval1.setRight(geneEval);
									} else if (geneEval > bestEval2.right()
											&& i != bestEval1.left()) {
										bestEval2.setLeft(i);
										bestEval2.setRight(geneEval);
									}
								}
								sight.clear();
							}
							// TODO: if there isn't anything in
							// org's field of
							// vision.
							// These numbers need to be worked
							// out.
							else {
								Expr result = workingGene.getEvaledList();
								HashMap<String, Double> environment = new HashMap<String, Double>();
								environment.put("a",
										norm.normalize(ran.nextDouble() * 100)); // not
								// sure
								// what
								// to
								// pass
								environment.put("b",
										norm.normalize(ran.nextDouble() * 100)); // not
								// sure
								// what
								// to
								// pass
								environment.put("c", norm.normalize(0.0));
								environment.put("d", health);
								environment.put("e", norm.normalize(0.0));
								environment.put("f", norm.normalize(1.0));
								environment.put("g", numSurroundingOrgs);
								double geneEval = result.evaluate(environment);
								if (geneEval > bestEval1.right()
										&& bestEval1.right() < bestEval2
												.right()) {
									bestEval1.setLeft(i);
									bestEval1.setRight(geneEval);
								} else if (geneEval > bestEval2.right()
										&& i != bestEval1.left()) {
									bestEval2.setLeft(i);
									bestEval2.setRight(geneEval);
								}
							}
						}
						/*doAction(org, bestEval1);
						doAction(org, bestEval2);*/
					}
					orgIndex++;
				}
				timePassed++;
			}
			if (trialNum < trialsPerGen) {
				shuffleIds.clear();
				for (Organism o : organisms) {
					o.newLocation();
					o.setHealth(o.getMaxHealth());
					/*o.clear();*/
					shuffleIds.add(o.getId());
				}
				trialNum++;
				healthFd.clear();
				poisFood.clear();
				for (int i = 0; i < OptionsPanel.numOrganisms / 2; i++) {
					HealthyFood h = new HealthyFood(100.0, i, 2);
					PoisonousFood f = new PoisonousFood(100.0, i, 2);
					healthFd.add(h);
					poisFood.add(f);
				}
				timePassed = 0;
				GUI.genPanel.newTrial();
			} else {
				timePassed = 0;
				double sum = 0;
				/*g.setOrgList(organisms)*/;
				organisms = g.newGeneration(organisms);
				healthFd.clear();
				poisFood.clear();
				locationMap.clearLocations();
				shuffleIds.clear();
				for (Organism o : organisms) {
					/*sum += g.fitnessIan(o);*/
					o.newLocation();
					/*o.addChromosome();*/
					o.setHealth(o.getMaxHealth());
					/*o.clear();*/
					shuffleIds.add(o.getId());
				}
				lastAvg = sum / OptionsPanel.numOrganisms;
				System.out.println(lastAvg);
				for (int i = 0; i < OptionsPanel.numOrganisms / 2; i++) {
					HealthyFood h = new HealthyFood(100.0, i, 2);
					PoisonousFood f = new PoisonousFood(100.0, i, 2);
					healthFd.add(h);
					poisFood.add(f);
				}
				trialNum = 1;
				generationNum++;
				GUI.genPanel.addGeneration();
				GUI.genPanel.newGeneration();
			}
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
		for (Food f : foodList)
			if (f.getHealth() > 0)
				f.paint(g, false);
		/*for (HealthyFood h : healthFd)
			if (h != null)
				if (h.getHealth() > 0)
					h.paint(g, false);
				else
					h.paint(g, true);
		for (PoisonousFood p : poisFood)
			if (p != null)
				if (p.getHealth() > 0)
					p.paint(g, false);
				else
					p.paint(g, true);*/
	}

	/**
	 * Sets the initial game state of the GridPanel.
	 */
	public void initializeAstar() {
		// Reset all generation info from previous simulations.
		generationNum = 1;
		trialNum = 1;
		GUI.genPanel.resetGenInformation();
		ran = new Random();
		timePassed = 0;
		shuffleIds = new ArrayList<Integer>();
		// Location map will consist of: key: current instance number of
		// object
		// value: 'w' for white space or available. 'o' for organism. 'h'
		// for
		// healthy food. 'p' for poisonous food.
		locationMap = LocationMap.getInstance();
		locationMap.clearLocations();
		norm = new Normalizer(new Pair<Double, Double>(-600.0, 600.0),
				new Pair<Double, Double>(-50.0, 50.0));
		organisms.clear();
		shuffleIds.clear();
		numFoodSources = OptionsPanel.numOrganisms / 5;
		for (int i = 0; i < OptionsPanel.numOrganisms; i++) {
			/*Organism o = new Organism(100.00, 2, i);*/
			Organism o = new Organism(100.00, 1, i);
			organisms.add(o);
			shuffleIds.add(i);
			/*o.addStartingLocation();*/
			/*o.addChromosome();*/
		}
		locationMap.placeOrganisms(organisms);
		for (int i = 0; i < numFoodSources * 2; i++)
			if (ran.nextBoolean())
				foodList.add(new HealthyFood(100.00, i, 2));
			else
				foodList.add(new PoisonousFood(100.00, i, 2));
		locationMap.placeFoods(foodList);
		g = new GEP(0.75, 0.01, 0.01, 0.75, 0.75, 1, false, false);
	}

	/**
	 * Sets the initial game state of the GridPanel making use of OrgData
	 * objects.
	 */
	public void initializeAstarWithOrgData() {
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
			orgDataList.add(new OrgData(org.getMaxHealth(), i));
			organisms.add(org);
			locationMap.placeOrganism(org);
			shuffleIds.add(i);
		}
		// Initialize the food sources and allow locationMap to find a starting
		// position for them.
		for (int i = 0; i < numFoodSources * 3; i++) {
			if (ran.nextBoolean())
				foodList.add(new HealthyFood(100.00, i, 2));
			else
				foodList.add(new PoisonousFood(100.00, i, 2));
			locationMap.placeFood(foodList.get(i));
		}
		g = new GEP(0.75, 0.01, 0.01, 0.75, 0.75, 2, false, true);
	}

	/**
	 * Uses: Astar, there is no eating, and the closed list is in the OrgData
	 * object.
	 * 
	 * @param numActions
	 *            The number of actions that an organism will do at each time
	 *            step.
	 */
	private void simulateStepAstarWithoutEatWithClosedListOrgData(int numActions) {

		Collections.shuffle(shuffleIds);
		// Loop through Organisms.
		mainLoop: for (int i = 0; i < shuffleIds.size(); i++) { // mainLoop.
			// Get an Organism corresponding the the ids in the shuffleIds
			// list.
			Organism org = organisms.get(shuffleIds.get(i));
			// The orgData object holds all of the data for this specific
			// organism.
			OrgData orgData = orgDataList.get(shuffleIds.get(i));
			// Closed list.
			ArrayList<Coordinate> closedList = (ArrayList<Coordinate>) orgData
					.getClosedList();
			closedList.add(org.getLocation());
			// lastFoodSourceIndex holds the index of the last food source
			// that was visited.
			Integer lastFoodSourceIndex = 0;
			for (int l = 0; l < numActions; l++) { // numActions loop.
				// Sample for fitness function.
				orgData.incHlthTot();
				// depleteValue is the value to decrease the Organism's
				// health
				// by at each time step.
				double depleteValue = org.getMaxHealth()
						/ ((lengthGeneration - 1) * numActions);
				// Check to see if the Organism is dead, if so remove
				// that org
				// from the shuffleIds list.
				if (deplete(org, depleteValue)) {
					System.out.println("removing on deplete");
					shuffleIds.remove(new Integer(org.getId()));
					continue mainLoop;
				}
				Chromosome chrome = org.getChromosome();
				// First Gene in Chromosome.
				Gene currentGene = chrome.getGene(0);
				// Get the first food.
				Food foodDestination = foodList.get(0);
				// bestEval is used to hold the value of the evaluation
				// and the food source that the evaluation corresponds
				// to.
				Pair<Integer, Double> bestEval = new Pair<Integer, Double>(0,
						evaluateGeneFoodInRangeAstar(org, currentGene,
								foodDestination));
				for (int k = 1; k < foodList.size(); k++) { // loopFood.
					double aResult = evaluateGeneFoodInRangeAstar(org,
							currentGene, foodList.get(k));
					/*System.out.println("on food" + k);
					System.out.println("Result for eval: " + aResult);*/
					if (aResult > bestEval.getSnd()) {
						foodDestination = foodList.get(k);
						/*System.out.println("Replaced!! result");*/
						bestEval.setLeft(k);
						bestEval.setRight(aResult);
					}
				} // End loopFood.
				/*System.out.println("foodToMoveto id: "
					    + foodDestination.getId());
				System.out.println();
				moveAstar(org, bestEval, foodDestination);*/
				// This mechanism decides whether or not to clear the
				// closed
				// list. The closed list should clear if the organism
				// has
				// decided to make its way towards another food source.
				// If it
				// decides to stay on the path to the same food source,
				// then it
				// needs to keep its list of previously visited
				// nodes.
				if (lastFoodSourceIndex != bestEval.getFst()) {
					closedList.clear();
					lastFoodSourceIndex = bestEval.getFst();
				}
				moveAstarClosedList(org, orgData, foodDestination, closedList);
			} // End NumAction Loop.
		} // End mainLoop.
	}

	/**
	 * Uses: Astar, there is no eating, and the closed list is in the OrgData
	 * object.
	 * 
	 * @param numActions
	 *            The number of actions that an organism will do at each time
	 *            step.
	 */
	private void simulateStepAstarClosedListOrgDataLoopGenes(int numActions) {

		Collections.shuffle(shuffleIds);
		// Loop through Organisms.
		mainLoop: for (int i = 0; i < shuffleIds.size(); i++) { // mainLoop.
			// Get an Organism corresponding the the ids in the shuffleIds
			// list.
			Organism org = organisms.get(shuffleIds.get(i));
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
			for (int l = 0; l < numActions; l++) { // numActions loop.
				// Sample for fitness function.
				orgData.incHlthTot();
				// depleteValue is the value to decrease the Organism's
				// health
				// by at each time step.
				double depleteValue = orgData.getMaxHealth()
						/ ((lengthGeneration - 100) * numActions);
				// Check to see if the Organism is dead, if so remove that org
				// from the shuffleIds list.
				if (deplete(org, depleteValue)) {
					orgData.setTimeOfDeath(timePassed);
					shuffleIds.remove(new Integer(org.getId()));
					continue mainLoop;
				}
				Chromosome chrome = org.getChromosome();
				// First Gene in Chromosome.
				Gene currentGene = chrome.getGene(0);
				// Get the first food.
				Food foodDestination = foodList.get(0);
				// bestEval is used to hold the value of the evaluation
				// and the food source that the evaluation corresponds
				// to.
				Pair<Integer, Double> bestEval = new Pair<Integer, Double>(0,
						evaluateGeneFoodInRangeAstar(org, currentGene,
								foodDestination));

				for (int j = 1; j < chrome.size(); j++) { // loopGenes.
					currentGene = chrome.getGene(j);
					System.out.println("onGene: " + j);
					for (int k = 1; k < foodList.size(); k++) { // loopFood.
						double aResult = evaluateGeneFoodInRangeAstar(org,
								currentGene, foodList.get(k));
						if (aResult > bestEval.getSnd()) {
							foodDestination = foodList.get(k);
							bestEval.setLeft(k);
							bestEval.setRight(aResult);
						}
					} // End loopFood.
				} // End loopGenes.

				// This mechanism decides whether or not to clear the
				// closed list. The closed list should clear if the
				// organism has decided to make its way towards another
				// food source. If it decides to stay on the path to the
				// same food source, then it needs to keep its list of
				// previously visited nodes.
				if (orgData.getLastFoodSourceIndex() != bestEval.getFst()) {
					closedList.clear();
					orgData.setLastFoodSourceIndex(bestEval.getFst());
				}
				// TODO: Later on replace foodDestination with
				// objectDestination.
				if (doActionAstar(org, orgData, bestEval, foodDestination)) {
					System.out.println("removing on action");
					shuffleIds.remove(new Integer(org.getId()));
					orgData.setTimeOfDeath(timePassed);
					continue mainLoop;
				}
			} // End NumAction Loop.
		} // End mainLoop.
	}

	/**
	 * Uses: Astar, there is no eating, and the closed list is in the OrgData
	 * object.
	 * 
	 * @param numActions
	 *            The number of actions that an organism will do at each time
	 *            step.
	 */
	private void simulateStepAstarClosedListOrgDataTest(int numActions) {
		System.out.println("simStep");
		Collections.shuffle(shuffleIds);
		// Loop through Organisms.
		mainLoop: for (int i = 0; i < shuffleIds.size(); i++) { // mainLoop.
			// Get an Organism corresponding the the ids in the shuffleIds
			// list.
			Organism org = organisms.get(shuffleIds.get(i));
			/*System.out.println("simStep for org id num: " + org.getId());*/
			// The orgData object holds all of the data for this specific
			// organism.
			OrgData orgData = orgDataList.get(org.getId());
			/*System.out.println("orgData id num: " + orgData.getId());*/
			orgData.incrementSumHealth(org.getHealth());
			// Closed list.
			ArrayList<Coordinate> closedList = (ArrayList<Coordinate>) orgData
					.getClosedList();
			closedList.add(org.getLocation());
			// lastFoodSourceIndex holds the index of the last food source
			// that was visited.
			orgData.setLastFoodSourceIndex(0);
			for (int l = 0; l < numActions; l++) { // numActions loop.
				// Sample for fitness function.
				orgData.incHlthTot();
				// depleteValue is the value to decrease the Organism's health
				// by at each time step.
				double depleteValue = orgData.getMaxHealth()
						/ ((lengthGeneration - 100) * numActions);
				// Check to see if the Organism is dead, if so remove that org
				// from the shuffleIds list.
				if (deplete(org, depleteValue)) {
					/*System.out.println("removing on deplete");*/
					orgData.setTimeOfDeath(timePassed);
					shuffleIds.remove(new Integer(org.getId()));
					continue mainLoop;
				}
				Chromosome chrome = org.getChromosome();
				// First Gene in Chromosome.
				Gene currentGene = chrome.getGene(0);
				// Get the first food.
				Food foodDestination = foodList.get(0);
				// bestEval is used to hold the value of the evaluation
				// and the food source that the evaluation corresponds
				// to.
				Pair<Integer, Double> bestEval = new Pair<Integer, Double>(0,
						evaluateGeneFoodInRangeAstar(org, currentGene,
								foodDestination));
				for (int k = 1; k < foodList.size(); k++) { // loopFood.
					double aResult = evaluateGeneFoodInRangeAstar(org,
							currentGene, foodList.get(k));
					/*System.out.println("aResult: " + aResult);
					System.out.println("prevResult: " + bestEval.getSnd());
					System.out.println();*/
					if (aResult > bestEval.getSnd()) {
						/*System.out.println("aResult better than prev.");*/
						foodDestination = foodList.get(k);
						/*System.out.println("Going to food source: " + k);*/
						bestEval.setLeft(k);
						bestEval.setRight(aResult);
					}
				} // End loopFood.

				// This mechanism decides whether or not to clear the
				// closed list. The closed list should clear if the
				// organism has decided to make its way towards another
				// food source. If it decides to stay on the path to the
				// same food source, then it needs to keep its list of
				// previously visited nodes.
				if (orgData.getLastFoodSourceIndex() != bestEval.getFst()) {
					closedList.clear();
					orgData.setLastFoodSourceIndex(bestEval.getFst());
				}
				if (doActionAstar(org, orgData, bestEval, foodDestination)) {
					/*System.out.println("removing on action");*/
					shuffleIds.remove(new Integer(org.getId()));
					orgData.setTimeOfDeath(timePassed);
					continue mainLoop;
				}
			} // End NumAction Loop.
			/*System.out.println("Performed num limit of actions");*/
		} // End mainLoop.
		/*System.out.println();*/
	}

	/**
	 * Moves the organism using Astar.
	 * 
	 * @param org
	 * @param bestEval
	 * @param aFoodDestination
	 */
	private void moveAstarClosedList(Organism org, OrgData od,
			Food aFoodDestination, ArrayList<Coordinate> aClosedList) {
		PriorityQueue<Coordinate> sq = LocationMap.searchWithList(
				org.getLocation(), aFoodDestination.getLocation(), org.getId());
		Coordinate nextMove;
		/*org.printId();
		org.printLocation();
		System.out.println("Next move: " + nextMove.getX() + ", "
			    + nextMove.getY());
		System.out.println("closedList size(): " + aClosedList.size());*/
		boolean canMove;
		getNextMove: do {
			canMove = true;
			nextMove = sq.remove();
			int nextX = nextMove.getX();
			int nextY = nextMove.getY();
			/*System.out.print("nextMove: ");
			nextMove.printLocation();*/
			if (sq.isEmpty()) {
				canMove = false;
				aClosedList.clear();
				break;
			}
			for (int i = 0; i < aClosedList.size(); i++) {
				Coordinate closed = aClosedList.get(i);
				int closedX = closed.getX();
				int closedY = closed.getY();
				/*System.out.println("On closed list: " + closedX + ", "
						 + closedY);*/
				if (nextX == closedX && nextY == closedY) {
					canMove = false;
					continue getNextMove;
				}
			}
			if (canMove)
				break;
		} while (!sq.isEmpty());
		if (canMove) {
			org.moveTo(nextMove);
			od.countStep();
			aClosedList.add(nextMove);
		}
		/*System.out.println("Added to closedList: " + aClosedList.get(0));*/
		repaint();
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
			Pair<Integer, Double> bestEval, Food aFoodDestination) {

		switch (bestEval.left()) {
			case 0:
				moveAstarClosedList(org, anOrgData, aFoodDestination,
						(ArrayList<Coordinate>) anOrgData.getClosedList());
				break;
			case 1:
				return eatFood(org, aFoodDestination);
				/*return eatFoodInRange(org);*/
		}
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

	private double evaluateGeneFoodInRange(Organism org, Gene currentGene,
			Food aFood) {
		HashMap<String, Double> environment = new HashMap<String, Double>();
		double orgX = norm.normalize(org.getLocation().getX());
		double orgY = norm.normalize(org.getLocation().getY());
		double health = norm.normalize(org.getHealth());
		double numSurroundingOrgs = norm.normalize(org.getSurroundingObjects(
				'o', 5).size() - 1);
		double foodX = norm.normalize(aFood.getLocation().getX());
		double foodY = norm.normalize(aFood.getLocation().getY());
		double orgNearFood = norm.normalize(aFood.numSurroundingObjects(5));
		double foodRemaining = norm.normalize(aFood.getHealth());
		Expr result = currentGene.getEvaledList();
		environment.put("a", foodX - orgX);
		environment.put("b", orgY - foodY);
		environment.put("c", orgNearFood);
		environment.put("d", health);
		environment.put("e", foodRemaining);
		environment.put("f", aFood.getFoodType());
		environment.put("g", numSurroundingOrgs);
		return result.evaluate(environment);
	}

	private double evaluateGeneFoodInRangeAstar(Organism org, Gene currentGene,
			Food aFood) {
		HashMap<String, Double> environment = new HashMap<String, Double>();
		double orgX = org.getLocation().getX();
		double orgY = org.getLocation().getY();
		double foodX = aFood.getLocation().getX();
		double foodY = aFood.getLocation().getY();
		double distanceToFood = norm.normalize(LocationMap.distance(orgX, orgY,
				foodX, foodY));
		double health = norm.normalize(org.getHealth());
		double numSurroundingOrgs = norm.normalize(org.getSurroundingObjects(
				'o', 5).size() - 1);
		double orgNearFood = norm.normalize(aFood.numSurroundingObjects(5));
		double foodRemaining = norm.normalize(aFood.getHealth());
		Expr result = currentGene.getEvaledList();
		environment.put("a", distanceToFood); // Represents distance to
		// food.
		environment.put("c", orgNearFood);
		environment.put("d", health);
		environment.put("e", foodRemaining);
		environment.put("f", aFood.getFoodType());
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
	 * This method gets all returns a list of all the Food objects that are in
	 * the org's sight range.
	 * 
	 * @param org
	 * @return
	 */
	/*FOR TESTING.*/
	// Uses abstract Food class instead of HealthyFood and Poisonous Food.
	private List<Food> collectFoodInRange2Test(Organism org) {
		int sightRange = 30;
		// List of Ids of the food in range.
		ArrayList<Integer> foodInRangeIds = (ArrayList<Integer>) org
				.getFoodInRange(sightRange);
		// List to be filled with actual food objects.
		LinkedList<Food> foodInRange = new LinkedList<Food>();
		// Fill the healthyFoodInRange list with food objects obtained from
		// ids.
		if (!foodInRangeIds.isEmpty())
			for (Integer integer : foodInRangeIds)
				foodInRange.add(foodList.get(integer));
		for (Food f : foodInRange) {
			Coordinate c = f.getLocation();
			int fX = c.getX();
			int fY = c.getY();
			Coordinate cOrg = org.getLocation();
			int oX = cOrg.getX();
			int oY = cOrg.getY();
			int xRange = Math.abs(oX - fX);
			int yRange = Math.abs(oY - fY);
			if (xRange > sightRange + 4) {
				System.out.println("out of range x");
				System.out.println("xRange: " + xRange);
			}
			if (yRange > sightRange + 4) {
				System.out.println("out of range y");
				System.out.println("yRange: " + yRange);
			}
		}
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
			Coordinate c = m.getLocation();
			int x = c.getX();
			int y = c.getY();
			locationMap.setRangeToBlank(x, y, m.getWidth(), m.getHeight());
			return true;
		} else
			m.setHealth(m.getHealth() - val);
		return false;
	}

	public void initializeFromGeneFile(LinkedList<Organism> population) {
		// reset all generation info from previous simulations.
		generationNum = 1;
		trialNum = 1;
		GUI.genPanel.resetGenInformation();
		ran = new Random();
		timePassed = 0;
		shuffleIds = new ArrayList<Integer>();

		/*
		 * location map will consist of: key: current instance number of object
		 * value: 'w' for white space or available. 'o' for organism. 'h' for
		 * healthy food. 'p' for poisonous food.
		 */
		locationMap.getInstance().clearLocations();

		norm = new Normalizer(new Pair<Double, Double>(-600.0, 600.0),
				new Pair<Double, Double>(-50.0, 50.0));

		// clear any remaining organisms, food, or poison in simulation.
		organisms.clear();
		healthFd.clear();
		poisFood.clear();

		for (int i = 0; i < population.size(); i++) {
			organisms.add(population.get(i));
			shuffleIds.add(i);
			organisms.get(i).newLocation();
		}
	}

	public String getOrganismData(int index) {
		OrgData orgData = orgDataList.get(index);
		Organism org = organisms.get(index);
		return orgData.getId() + " " + org.getHealth() + " " + org.getFitness()
				+ " " + org.getLocation().getX() + " "
				+ org.getLocation().getY() + " " + orgData.getHealthEat() + " "
				+ orgData.getPoisonEat() + " " + orgData.getEatFail() + " "
				+ orgData.getNumAttacked() + " " + orgData.getNumPushed() + " "
				+ orgData.getTotalScans();
	}
}