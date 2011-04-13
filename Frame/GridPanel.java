package Frame;

import static java.lang.System.out;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
/* import Searching.AStar; */
import Searching.StarQueue;

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
	 private LinkedList<Food> food;
	 private LinkedList<Organism> orgsUsed;
	 private ArrayList<OrgData> orgDataList;
	 private ArrayList<Integer> shuffleIds;
	 private ArrayList<String> shuffleStringIds;
	 private int lengthTimeStep = 100;
	 private int lengthGeneration = 200;
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
				    food = new LinkedList<Food>();
				    // track user mouse movement.
				    addMouseMotionListener(new MouseMotionListenerClass(
							 GridPanel.this));
				    // handle other mouse events
				    addMouseListener(new MouseListenerClass());
				    t = new javax.swing.Timer(lengthTimeStep,
							 new ActionListener() {
								  public void actionPerformed(
										    ActionEvent e) {
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
										    simulateStepAstarWithoutEatWithClosedListOrgData(5);
										    /*simulateStepAstarWithoutEatWithPreviousPosition(2);*/
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
			   org.goBack(generation);
		  }
		  GUI.genPanel.removeGenerations(generation);
		  healthFd.clear();
		  poisFood.clear();
		  food.clear();

		  for (int i = 0; i < numFoodSources; i++) {
			   HealthyFood h = new HealthyFood(100.0, i, 2);
			   PoisonousFood f = new PoisonousFood(100.0, i, 2);
			   healthFd.add(h);
			   poisFood.add(f);
		  }

	 }

	 public boolean doAction(Organism org, Pair<Integer, Double> bestEval) {

		  switch (bestEval.left()) {
			   case 0:
				    org.moveNorth(organisms, false);
				    // org.addAction("N", orgIndex);
				    org.countStep();
				    break;
			   /*case 1:
			   	org.moveSouth(organisms, false);
			   	// org.addAction("S", orgIndex);
			   	org.countStep();
			   	break;
			   case 2:
			   	org.moveEast(organisms, false);
			   	// org.addAction("E", orgIndex);
			   	org.countStep();
			   	break;
			   case 3:
			   	org.moveWest(organisms, false);
			   	// org.addAction("W", orgIndex);
			   	org.countStep();
			   	break;
			   case 4:
			   	org.moveNorthEast(organisms, false);
			   	// org.addAction("NE", orgIndex);
			   	org.countStep();
			   	break;
			   case 5:
			   	org.moveNorthWest(organisms, false);
			   	// org.addAction("NW", orgIndex);
			   	org.countStep();
			   	break;
			   case 6:
			   	org.moveSouthEast(organisms, false);
			   	// org.addAction("SE", orgIndex);
			   	org.countStep();
			   	break;
			   case 7:
			   	org.moveSouthWest(organisms, false);
			   	// org.addAction("SW", orgIndex);
			   	org.countStep();
			   	break;*/
			   case 8:
				    // doEat(org);
				    return eat(org);
			   case 9:
				    attack(org);
				    break;
			   case 10:
				    push(org);
		  }
		  return false;
	 }

	 public void attack2(Organism org, ArrayList<Organism> aOrgsUsed) {
		  ArrayList<Integer> surroundingOrganisms = org
				    .getSurroundingObjects('o', 1);
		  surroundingOrganisms.remove((Integer) org.getId());
		  if (surroundingOrganisms.size() > 0) {
			   int index = surroundingOrganisms.get(ran
						.nextInt(surroundingOrganisms.size()));
			   org.attack2(index, aOrgsUsed);
		  } else {
			   org.setAction("Attempted to attack");
		  }
	 }

	 public boolean eat(Organism org) {
		  LinkedList<Food> foodToEatList = (LinkedList<Food>) collectFoodInRange2(
				    org, 2);
		  Food foodToEat;
		  if (!foodToEatList.isEmpty())
			   foodToEat = foodToEatList.get(ran.nextInt(foodToEatList
						.size()));
		  else
			   /* System.out.println("Fired Gene without any food in range."); */
			   return false;
		  org.printInfo();
		  /* System.out.println("Ate food"); */
		  foodToEat.printInfo();
		  return org.changeHealth(5 * foodToEat.getType());
	 }

	 public void attack(Organism org) {
		  ArrayList<Integer> surroundingOrganisms = org
				    .getSurroundingObjects('o', 1);
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
		  for (Organism o : organisms) {
			   o.newLocation();
			   o.setHealth(o.getMaxHealth());
			   o.clear();
			   shuffleIds.add(o.getId());
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

	 public void newGenerationAstar() {
		  timePassed = 0;
		  double sum = 0;
		  trialNum = 1;
		  generationNum++;
		  g.setOrgList(organisms);
		  organisms = g.newGeneration();
		  locationMap.clearLocations();
		  shuffleIds.clear();
		  // Assign organism fitness and reinitialize organisms.
		  for (Organism o : organisms) {
			   o.setFitness(g.fitnessDistanceTraveled(o)); // TODO: New
			   // fitness travel
			   // distance.
			   sum += o.getFitness();
			   // TODO: change the gene checking.
			   /*o.newLocation();*/
			   /*o.addChromosome();*/
			   o.setHealth(o.getMaxHealth());
			   o.clear();
			   shuffleIds.add(o.getId());
		  }
		  for (OrgData od : orgDataList)
			   od.clear();
		  // Place the organisms on the locationMap.
		  locationMap.placeOrganisms(organisms);
		  lastAvg = sum / OptionsPanel.numOrganisms;
		  // Reinitialize food list.
		  for (int i = 0; i < numFoodSources * 2; i++)
			   if (ran.nextBoolean())
				    food.add(new HealthyFood(100.00, i, 2));
			   else
				    food.add(new PoisonousFood(100.00, i, 2));
		  // Place food on locationMap.
		  locationMap.placeFoods(food);
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
						org.incHlthTot();
						if (org.getHealth() > 0) {
							 ArrayList<Food> sight = new ArrayList<Food>();
							 ArrayList<Integer> sightIDs = new ArrayList<Integer>();
							 sightIDs = org.getSurroundingObjects('h',
									   20);
							 for (Integer i : sightIDs) {
								  sight.add(healthFd.get(i));
							 }
							 sightIDs.clear();
							 sightIDs = org.getSurroundingObjects('p',
									   20);
							 for (Integer i : sightIDs)
								  sight.add(poisFood.get(i));
							 org.addScan(sight.size());
							 double orgX = norm.normalize(org
									   .getLocation().getX());
							 double orgY = norm.normalize(org
									   .getLocation().getY());
							 double health = norm.normalize(org
									   .getHealth());
							 double numSurroundingOrgs = norm
									   .normalize(org
												.getSurroundingObjects(
														  'o', 5)
												.size() - 1);
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
									   for (int j = 0; j < sight
												.size(); j++) {
										    HashMap<String, Double> environment = new HashMap<String, Double>();
										    Food f = sight.get(j);
										    double foodX;
										    double foodY;
										    double orgNearFood;
										    double foodRemaining;
										    double isPoison = norm
													 .normalize(1.0);
										    if (f != null) {
												foodX = norm.normalize(f
														  .getLocation()
														  .getX());
												foodY = norm.normalize(f
														  .getLocation()
														  .getY());
												orgNearFood = norm
														  .normalize(f.numSurroundingObjects(5));
												foodRemaining = norm
														  .normalize(f.getHealth());

												if (f instanceof PoisonousFood)
													 isPoison = -isPoison;
										    } else {
												foodX = norm.normalize(ran
														  .nextDouble() * 100);
												foodY = norm.normalize(ran
														  .nextDouble() * 100);
												orgNearFood = norm
														  .normalize(0.0);
												foodRemaining = norm
														  .normalize(0.0);
										    }
										    Expr result = workingGene
													 .getEvaledList();
										    environment.put("a",
													 foodX - orgX);
										    environment.put("b", orgY
													 - foodY);
										    environment.put("c",
													 orgNearFood);
										    environment.put("d",
													 health);
										    environment.put("e",
													 foodRemaining);
										    environment.put("f",
													 isPoison);
										    environment.put("g",
													 numSurroundingOrgs);
										    double geneEval = result
													 .evaluate(environment);
										    if (geneEval > bestEval1
													 .right()
													 && bestEval1.right() < bestEval2
															   .right()) {
												bestEval1.setLeft(i);
												bestEval1.setRight(geneEval);
										    } else if (geneEval > bestEval2
													 .right()
													 && i != bestEval1
															   .left()) {
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
									   Expr result = workingGene
												.getEvaledList();
									   HashMap<String, Double> environment = new HashMap<String, Double>();
									   environment.put(
												"a",
												norm.normalize(ran
														  .nextDouble() * 100)); // not
									   // sure
									   // what
									   // to
									   // pass
									   environment.put(
												"b",
												norm.normalize(ran
														  .nextDouble() * 100)); // not
									   // sure
									   // what
									   // to
									   // pass
									   environment.put("c",
												norm.normalize(0.0));
									   environment.put("d", health);
									   environment.put("e",
												norm.normalize(0.0));
									   environment.put("f",
												norm.normalize(1.0));
									   environment.put("g",
												numSurroundingOrgs);
									   double geneEval = result
												.evaluate(environment);
									   if (geneEval > bestEval1
												.right()
												&& bestEval1.right() < bestEval2
														  .right()) {
										    bestEval1.setLeft(i);
										    bestEval1.setRight(geneEval);
									   } else if (geneEval > bestEval2
												.right()
												&& i != bestEval1
														  .left()) {
										    bestEval2.setLeft(i);
										    bestEval2.setRight(geneEval);
									   }
								  }
							 }
							 doAction(org, bestEval1);
							 doAction(org, bestEval2);
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
						o.clear();
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
				    g.setOrgList(organisms);
				    organisms = g.newGeneration();
				    healthFd.clear();
				    poisFood.clear();
				    locationMap.clearLocations();
				    shuffleIds.clear();
				    for (Organism o : organisms) {
						sum += g.fitness(o);
						o.newLocation();
						o.addChromosome();
						o.setHealth(o.getMaxHealth());
						o.clear();
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
	  *              the Graphics object used
	  */
	 @Override
	 public void paintComponent(Graphics g) {
		  super.paintComponent(g);
		  gui.getMap().paint(g);
		  // handle each new organism created.
		  for (Organism org : organisms)
			   org.paint(g);
		  for (Food f : food)
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

		  System.out.println("got to intiastar");
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
				    food.add(new HealthyFood(100.00, i, 2));
			   else
				    food.add(new PoisonousFood(100.00, i, 2));
		  locationMap.placeFoods(food);
		  g = new GEP(organisms, 0.75, 0.01, 0.01, 0.75, 0.75, 1, false,
				    false);
	 }

	 /**
	  * Sets the initial game state of the GridPanel. Trying to implement the
	  * OrgData class.
	  */
	 public void initializeAstarWithOrgData() {
		  System.out.println("got to intiastar");
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
		  orgDataList = new ArrayList<OrgData>();
		  norm = new Normalizer(new Pair<Double, Double>(-600.0, 600.0),
				    new Pair<Double, Double>(-50.0, 50.0));
		  organisms.clear();
		  shuffleIds.clear();
		  numFoodSources = OptionsPanel.numOrganisms / 5;
		  for (int i = 0; i < OptionsPanel.numOrganisms; i++) {
			   /*Organism o = new Organism(100.00, 2, i);*/
			   Organism o = new Organism(100.00, 1, i);
			   orgDataList.add(new OrgData(o.getMaxHealth(), i));
			   organisms.add(o);
			   shuffleIds.add(i);
			   /*o.addStartingLocation();*/
			   /*o.addChromosome();*/
		  }
		  locationMap.placeOrganisms(organisms);
		  for (int i = 0; i < numFoodSources * 2; i++)
			   if (ran.nextBoolean())
				    food.add(new HealthyFood(100.00, i, 2));
			   else
				    food.add(new PoisonousFood(100.00, i, 2));
		  locationMap.placeFoods(food);
		  g = new GEP(organisms, 0.75, 0.01, 0.01, 0.75, 0.75, 1, false,
				    false);
	 }

	 private void simulateStepAstar(int numActions) {
		  Collections.shuffle(shuffleIds);
		  // Loop through Organisms.
		  mainLoop: for (int i = 0; i < shuffleIds.size(); i++) { // mainLoop.
			   // Get an Organism corresponding the the ids in the shuffleIds
			   // list.
			   Organism org = organisms.get(shuffleIds.get(i));
			   for (int l = 0; l < numActions; l++) {
				    org.incHlthTot(); // sample for fitness function.
				    // depleteValue is the value to decrease the Organism's
				    // health
				    // by at each time step.
				    double depleteValue = org.getMaxHealth()
							 / ((lengthGeneration - 2) * numActions);
				    // Check to see if the Organism is dead, if so remove
				    // that org
				    // from the shuffleIds list.
				    if (deplete(org, depleteValue)) {
						System.out.println("removing on deplete");
						shuffleIds.remove(new Integer(org.getId()));
						continue mainLoop;
				    }
				    // Get the food in the org's sight range.
				    /*LinkedList<Food> foodInRange =
				    	(LinkedList<Food>) collectFoodInRange2Test(org);*/
				    /*LinkedList<Food> foodInRange =
				    	(LinkedList<Food>) collectFoodInRange2(org, 300);*/
				    Chromosome chrome = org.getChromosome();
				    Gene currentGene = chrome.getGene(0); // First Gene in
				    // Chromosome.
				    // The food source that will be the final destination of
				    // the Astar search.
				    // TODO: Removed the idea of a vision range for now.
				    /* Food foodDestination = foodInRange.get(0);*/
				    // Get the first food.
				    Food foodDestination = food.get(0);
				    Pair<Integer, Double> bestEval = null;
				    // If the organism has something in its sight range.
				    /*if (!foodInRange.isEmpty()) {*/
				    // Initialize the variable that decides the resulting
				    // action.
				    bestEval = new Pair<Integer, Double>(0,
							 evaluateGeneFoodInRange(org, currentGene,
									   foodDestination));
				    // Loop through Genes in Chromosome.
				    loopGenes: for (int j = 1; j < chrome.size(); j++) {
						currentGene = chrome.getGene(j);
						loopFood: for (int k = 1; k < food.size(); k++) { // loopFood.
							 foodDestination = food.get(k);
							 double aResult = evaluateGeneFoodInRange(
									   org, currentGene,
									   foodDestination);
							 if (aResult > bestEval.getSnd()) {
								  bestEval.setLeft(k);
								  bestEval.setRight(aResult);
							 }
						} // End loopFood.
				    } // End loopGenes.
				    /*} else { // If the organism has nothing in its sight range.
				    	bestEval = new Pair<Integer, Double>(0, evaluateGene(org, currentGene));
				    	loopGenes: for (int j = 1; j < chrome.size(); j++) { // loopGenes.
				    		currentGene = chrome.getGene(j);
				    		double aResult = evaluateGene(org, currentGene);
				    		if (aResult > bestEval.getSnd()) {
				    			bestEval.setLeft(j);
				    			bestEval.setRight(aResult);
				    		} // end if.
				    	} // End loopGenes.
				    }*/// end Else.
				    if (doActionAstar(org, bestEval, foodDestination)) {
						System.out.println("removing on action");
						shuffleIds.remove(new Integer(org.getId()));
						continue mainLoop;
				    }
			   }
		  } // End mainLoop.
	 }

	 /**
	  * Just doing the movement of astar and no eat genes firing.
	  * 
	  * @param numActions
	  */
	 // FOR TESTING.
	 private void simulateStepAstarWithoutEat(int numActions) {
		  Collections.shuffle(shuffleIds);
		  // Loop through Organisms.
		  mainLoop: for (int i = 0; i < shuffleIds.size(); i++) { // mainLoop.
			   // Get an Organism corresponding the the ids in the shuffleIds
			   // list.
			   Organism org = organisms.get(shuffleIds.get(i));
			   for (int l = 0; l < numActions; l++) { // numActions loop.
				    // Sample for fitness function.
				    org.incHlthTot();
				    // depleteValue is the value to decrease the Organism's
				    // health
				    // by at each time step.
				    double depleteValue = org.getMaxHealth()
							 / ((lengthGeneration - 2) * numActions);
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
				    Food foodDestination = food.get(0);
				    // bestEval is used to hold the value of the evaluation
				    // and the food source that the evaluation corresponds
				    // to.
				    Pair<Integer, Double> bestEval = new Pair<Integer, Double>(
							 0, evaluateGeneFoodInRangeAstar(org,
									   currentGene, foodDestination));
				    for (int k = 1; k < food.size(); k++) { // loopFood.
						double aResult = evaluateGeneFoodInRangeAstar(
								  org, currentGene, food.get(k));
						/*System.out.println("on food" + k);
						System.out.println("Result for eval: " + aResult);*/
						if (aResult > bestEval.getSnd()) {
							 foodDestination = food.get(k);
							 /*System.out.println("Replaced!! result");*/
							 bestEval.setLeft(k);
							 bestEval.setRight(aResult);
						}
				    } // End loopFood.
				    /*System.out.println("foodToMoveto id: " + foodDestination.getId());
				    System.out.println();*/
				    /*moveAstar(org, bestEval, foodDestination);*/
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
				    moveAstar(org, bestEval, foodDestination);
			   } // End NumAction Loop.
		  } // End mainLoop.
	 }

	 private void simulateStepAstarWithoutEatWithClosedList(int numActions) {
		  Collections.shuffle(shuffleIds);
		  // Loop through Organisms.
		  mainLoop: for (int i = 0; i < shuffleIds.size(); i++) { // mainLoop.
			   // Get an Organism corresponding the the ids in the shuffleIds
			   // list.
			   Organism org = organisms.get(shuffleIds.get(i));
			   // Closed list.
			   // TODO: Put closedList in the OrgData class.
			   ArrayList<Coordinate> closedList = new ArrayList<Coordinate>();
			   closedList.add(org.getLocation());
			   // lastFoodSourceIndex holds the index of the last food source
			   // that was visited.
			   Integer lastFoodSourceIndex = 0;
			   for (int l = 0; l < numActions; l++) { // numActions loop.
				    // Sample for fitness function.
				    org.incHlthTot();
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
				    Food foodDestination = food.get(0);
				    // bestEval is used to hold the value of the evaluation
				    // and the food source that the evaluation corresponds
				    // to.
				    Pair<Integer, Double> bestEval = new Pair<Integer, Double>(
							 0, evaluateGeneFoodInRangeAstar(org,
									   currentGene, foodDestination));
				    for (int k = 1; k < food.size(); k++) { // loopFood.
						double aResult = evaluateGeneFoodInRangeAstar(
								  org, currentGene, food.get(k));
						/*System.out.println("on food" + k);
						System.out.println("Result for eval: " + aResult);*/
						if (aResult > bestEval.getSnd()) {
							 foodDestination = food.get(k);
							 /*System.out.println("Replaced!! result");*/
							 bestEval.setLeft(k);
							 bestEval.setRight(aResult);
						}
				    } // End loopFood.
				    /*System.out.println("foodToMoveto id: " + foodDestination.getId());
				    System.out.println();*/
				    /*moveAstar(org, bestEval, foodDestination);*/
				    // This mechanism decides whether or not to clear the
				    // closed list. The closed list should clear if the
				    // organism has decided to make its way towards another
				    // food source. If it decides to stay on the path to the
				    // same food source, then it needs to keep its list of
				    // previously visited nodes.
				    if (lastFoodSourceIndex != bestEval.getFst()) {
						closedList.clear();
						lastFoodSourceIndex = bestEval.getFst();
				    }
				    moveAstarClosedList(org, bestEval, foodDestination,
							 closedList);
			   } // End NumAction Loop.
		  } // End mainLoop.
	 }

	 /**
	  * Uses: Astar, there is no eating, and the closed list is in the OrgData
	  * object.
	  * 
	  * @param numActions
	  *              The number of actions that an organism will do at each
	  *              time step.
	  */
	 private void simulateStepAstarWithoutEatWithClosedListOrgData(
			   int numActions) {

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
				    org.incHlthTot();
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
				    Food foodDestination = food.get(0);
				    // bestEval is used to hold the value of the evaluation
				    // and the food source that the evaluation corresponds
				    // to.
				    Pair<Integer, Double> bestEval = new Pair<Integer, Double>(
							 0, evaluateGeneFoodInRangeAstar(org,
									   currentGene, foodDestination));
				    for (int k = 1; k < food.size(); k++) { // loopFood.
						double aResult = evaluateGeneFoodInRangeAstar(
								  org, currentGene, food.get(k));
						/*System.out.println("on food" + k);
						System.out.println("Result for eval: " + aResult);*/
						if (aResult > bestEval.getSnd()) {
							 foodDestination = food.get(k);
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
				    moveAstarClosedList(org, bestEval, foodDestination,
							 closedList);
			   } // End NumAction Loop.
		  } // End mainLoop.
	 }
	 
	 /**
	  * Uses: Astar, there is no eating, and the closed list is in the OrgData
	  * object.
	  * 
	  * @param numActions
	  *              The number of actions that an organism will do at each
	  *              time step.
	  */
	 private void simulateStepAstarWithoutEatWithPreviousPosition(
			   int numActions) {

		  Collections.shuffle(shuffleIds);
		  // Loop through Organisms.
		  mainLoop: for (int i = 0; i < shuffleIds.size(); i++) { // mainLoop.
			   // Get an Organism corresponding the the ids in the shuffleIds
			   // list.
			   Organism org = organisms.get(shuffleIds.get(i));
			   // The orgData object holds all of the data for this specific
			   // organism.
			   OrgData orgData = orgDataList.get(shuffleIds.get(i));
			   // lastFoodSourceIndex holds the index of the last food source
			   // that was visited.
			   Integer lastFoodSourceIndex = 0;
			   for (int l = 0; l < numActions; l++) { // numActions loop.
				    // Sample for fitness function.
				    org.incHlthTot();
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
				    Food foodDestination = food.get(0);
				    // bestEval is used to hold the value of the evaluation
				    // and the food source that the evaluation corresponds
				    // to.
				    Pair<Integer, Double> bestEval = new Pair<Integer, Double>(
							 0, evaluateGeneFoodInRangeAstar(org,
									   currentGene, foodDestination));
				    for (int k = 1; k < food.size(); k++) { // loopFood.
						double aResult = evaluateGeneFoodInRangeAstar(
								  org, currentGene, food.get(k));
						/*System.out.println("on food" + k);
						System.out.println("Result for eval: " + aResult);*/
						if (aResult > bestEval.getSnd()) {
							 foodDestination = food.get(k);
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
				    Coordinate prevPos = orgData.getPreviousPosition();
				    if (lastFoodSourceIndex != bestEval.getFst()) {
						prevPos.setX(-1);
						prevPos.setY(-1);
						lastFoodSourceIndex = bestEval.getFst();
				    }
				    moveAstarPreviousPosition(org, bestEval, foodDestination,
							 prevPos);
			   } // End NumAction Loop.
		  } // End mainLoop.
	 }

	 /**
	  * Moves the organism using Astar.
	  * 
	  * @param org
	  * @param bestEval
	  * @param aFoodDestination
	  */
	 private void moveAstar(Organism org, Pair<Integer, Double> bestEval,
			   Food aFoodDestination) {
		  Coordinate nextMove = LocationMap.search(org.getLocation(),
				    aFoodDestination.getLocation(), org.getId());
		  /*org.printId();
		  org.printLocation();
		  System.out.println("Next move: " + nextMove.getX() + ", " + nextMove.getY());*/
		  org.moveTo(nextMove);
		  org.countStep();
	 }

	 /**
	  * Moves the organism using Astar.
	  * 
	  * @param org
	  * @param bestEval
	  * @param aFoodDestination
	  */
	 private void moveAstarClosedList(Organism org,
			   Pair<Integer, Double> bestEval, Food aFoodDestination,
			   ArrayList<Coordinate> aClosedList) {

		  StarQueue<Coordinate> sq = LocationMap.searchWithList(
				    org.getLocation(), aFoodDestination.getLocation(),
				    org.getId());
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
			   org.countStep();
			   aClosedList.add(nextMove);
		  }
		  /*System.out.println("Added to closedList: " + aClosedList.get(0));*/
		  repaint();
	 }
	 
	 /**
	  * Moves the organism using previousPosition.
	  * 
	  * @param org
	  * @param bestEval
	  * @param aFoodDestination
	  */
	 private void moveAstarPreviousPosition(Organism org,
			   Pair<Integer, Double> bestEval, Food aFoodDestination,
			   Coordinate aPreviousPosition) {

		  StarQueue<Coordinate> sq = LocationMap.searchWithList(
				    org.getLocation(), aFoodDestination.getLocation(),
				    org.getId());
		  int prevX = aPreviousPosition.getX();
		  int prevY = aPreviousPosition.getY();
		  Coordinate nextMove;
		  boolean canMove;
		  getNextMove: do {
			   int nextX;
			   int nextY;
			   canMove = true;
			   if (!sq.isEmpty()) {
				    nextMove = sq.remove();
				    nextX = nextMove.getX();
				    nextY = nextMove.getY();
			   } else {
				    nextMove = org.getLocation();
				    canMove = false;
				    break;
			   }
			   /*System.out.print("nextMove: ");
			   nextMove.printLocation();*/
			   if (nextX == prevX && nextY == prevY) {
				    org.moveTo(nextMove);
				    org.countStep();
				    aPreviousPosition.setX(nextX);
				    aPreviousPosition.setY(nextY);
				    canMove = false;
				    continue getNextMove;
			   }
			   if (canMove)
				    break;
		  } while (!sq.isEmpty());
		  if (canMove) {
			   org.moveTo(nextMove);
			   org.countStep();
			   aPreviousPosition.setX(nextMove.getX());
			   aPreviousPosition.setY(nextMove.getY());
		  }
	 }

	 private boolean doActionAstar(Organism org,
			   Pair<Integer, Double> bestEval, Food aFoodDestination) {

		  switch (bestEval.left()) {
			   case 0:
				    Coordinate nextMove = LocationMap
							 .search(org.getLocation(),
									   aFoodDestination.getLocation(),
									   org.getId());
				    org.moveTo(nextMove);
				    org.countStep();
				    break;
			   case 1:
				    // doEat(org);
				    return eat(org);
		  }
		  return false;
	 }

	 private double evaluateGene(Organism org, Gene currentGene) {

		  HashMap<String, Double> environment = new HashMap<String, Double>();
		  double orgX = norm.normalize(org.getLocation().getX());
		  double orgY = norm.normalize(org.getLocation().getY());
		  double health = norm.normalize(org.getHealth());
		  double numSurroundingOrgs = norm.normalize(org
				    .getSurroundingObjects('o', 5).size() - 1);
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
		  double numSurroundingOrgs = norm.normalize(org
				    .getSurroundingObjects('o', 5).size() - 1);
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

	 private double evaluateGeneFoodInRangeAstar(Organism org,
			   Gene currentGene, Food aFood) {
		  HashMap<String, Double> environment = new HashMap<String, Double>();
		  double orgX = org.getLocation().getX();
		  double orgY = org.getLocation().getY();
		  double foodX = aFood.getLocation().getX();
		  double foodY = aFood.getLocation().getY();
		  double distanceToFood = norm.normalize(LocationMap.distance(orgX,
				    orgY, foodX, foodY));
		  double health = norm.normalize(org.getHealth());
		  double numSurroundingOrgs = norm.normalize(org
				    .getSurroundingObjects('o', 5).size() - 1);
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
	 private List<Food> collectFoodInRange2(Organism org, int aRange) {
		  // List of Ids of the food in range.
		  ArrayList<Integer> foodInRangeIds = (ArrayList<Integer>) org
				    .getFoodInRange(aRange);
		  // List to be filled with actual food objects.
		  LinkedList<Food> foodInRange = new LinkedList<Food>();
		  // Fill the healthyFoodInRange list with food objects obtained from
		  // ids.
		  if (!foodInRangeIds.isEmpty())
			   for (Integer integer : foodInRangeIds)
				    foodInRange.add(food.get(integer));
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
				    foodInRange.add(food.get(integer));
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
		  Organism o = organisms.get(index);
		  return o.getId() + " " + o.getHealth() + " " + o.getFitness() + " "
				    + o.getLocation().getX() + " " + o.getLocation().getY()
				    + " " + o.getHealthEat() + " " + o.getPoisonEat() + " "
				    + o.getEatFail() + " " + o.getNumAttacked() + " "
				    + o.getNumPushed() + " " + o.getTotalScans();
	 }
}