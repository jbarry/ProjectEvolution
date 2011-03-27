package Frame;

import static java.lang.System.out;
import Evaluation.Eval;
import Evaluation.Normalizer;

import java.util.HashMap;
import Evolution.GEP;
import Interactive.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import Interactive.Pair;
import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Evaluation.Expr;
/**
 * This class will handle all of the simulation's display.
 */
@SuppressWarnings("all")
public class GridPanel extends JPanel
{
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	public final static int WIDTH = 600;
	public final static int HEIGHT = 400;
	

//	public static boolean[][] isValidLocation;
	public static Pair<Integer, Character>[][] locationMap;

	private LinkedList<Organism> organisms;
	private LinkedList<HealthyFood> healthFd;
	private LinkedList<PoisonousFood> poisFood;
	private ArrayList<Integer> shuffleIds;
	private int lengthTimeStep = 100;
	private int lengthGeneration = 100000;
	private int timePassed = 0;
	private int trialsPerGen = 1;
	public int trialNum = 1;
	public int generationNum = 1;
	public double lastAvg = 0;
	private GEP g;
	public static int numFoodSources=OptionsPanel.numOrganisms/5;
	private Timer t;
	private Normalizer norm;
	private int numPreProcessedGenerations = 0;
	private Random ran;
	private GUI gui;
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	/**
	 * This constructor will handle all of the movements/interactions of
	 * all objects in the current game state.
	 */
	public GridPanel(final GUI aGui) {
		gui=aGui;
		Runnable r = new Runnable() {
			@Override
			public void run() {
				//initial JPanel settings
				setLayout(null);
				setLocation(GUI.WIDTH - GridPanel.WIDTH, 0);
				setSize(GridPanel.WIDTH, GridPanel.HEIGHT);
				setBorder(BorderFactory.createLineBorder(Color.black));

				/**Add Listeners*/
				//track user mouse movement.
				MouseMotionListener simMouseMotion = new MouseMotionListener(){
					@Override
					public void mouseDragged(MouseEvent arg0) {
					}
					@Override
					public void mouseMoved(MouseEvent arg0) {
						try {
							//get and display current mouse location.
							Coordinate mouseLocation = new Coordinate(arg0.getX(), arg0.getY());
							MonitorPanel.currMouseLoc.setText(mouseLocation.toString());

							//used to manage checks
							boolean isOrg = false;
							boolean isPFood = false;
							boolean isHFood = false;

							//check mouse location vs. all organism's locations.
							for(Organism o: organisms) {
								if(mouseLocation.approxEquals(o.getLocation(), Organism.width/2)) {
									//organism found
									isOrg = true;
									MonitorPanel.simObjInfo.setText(o.toString());
									//break to prevent any more updating from occuring and loop overhead.
									break;
								} else {
									isOrg = false;
								}
							}

							for(HealthyFood r: healthFd) {
								if(mouseLocation.approxEquals(r.getLocation(), Food.width/2)){
									//food found
									isHFood = true;
									MonitorPanel.simObjInfo.setText(r.toString());
									//break to prevent any more updating from occuring and loop overhead.
									break;
								} else {
									isHFood = false;
								}
							}
							for(PoisonousFood r: poisFood) {
								if(mouseLocation.approxEquals(r.getLocation(), Food.width/2)){
									//food found
									isPFood = true;
									MonitorPanel.simObjInfo.setText(r.toString());
									//break to prevent any more updating from occuring and loop overhead.
									break;
								}
								else{
									isPFood = false;
								}
							}

							if(!isHFood & !isPFood & !isOrg)
								MonitorPanel.simObjInfo.setText("No Object Selected");
						}
						catch(NullPointerException e){

						}
					}
				};
				addMouseMotionListener(simMouseMotion);

				//handle other mouse events
				MouseListener simMouseListener = new MouseListener(){
					@Override
					public void mouseClicked(MouseEvent arg0) {
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						try{
							//get and display current mouse location.
							Coordinate mouseLocation = new Coordinate(arg0.getX(), arg0.getY());
							MonitorPanel.currMouseLoc.setText(mouseLocation.toString());
						}
						catch(NullPointerException e){

						}
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						MonitorPanel.currMouseLoc.setText("");
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
					}

				};
				addMouseListener(simMouseListener);

				//initial program settings
				organisms = new LinkedList<Organism>();
				healthFd = new LinkedList<HealthyFood>();
				poisFood = new LinkedList<PoisonousFood>();

				t = new javax.swing.Timer(lengthTimeStep, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(timePassed < lengthGeneration) {
							t.stop();
							//out.println();
							timePassed+=100;
							simulateStep();
							repaint();
							t.start();
						} else if (trialNum < trialsPerGen) {
							newTrial();
						} else {
							newGeneration();
						}
					}
				});
			}

		};
		r.run();
	}

	public void clearLocations(){
		for(int i = 0; i < locationMap.length; i++){
			for(int j=0; j<locationMap[i].length; j++){
				//mark available
				locationMap[i][j] = new Pair<Integer, Character>(0, 'w');
			}
		}
	}
	
	private void simulateStep(){
		Collections.shuffle(shuffleIds);
		int orgIndex = 0;
		for(Integer orgNum: shuffleIds){
			Organism org = organisms.get(orgNum);
			org.deplete(.5);
			//Take sample of organism health for fitness.
			org.incHlthTot();
			if(org.getHealth() > 0){
				ArrayList<Food> sight = new ArrayList<Food>();
				ArrayList<Integer> sightIDs = new ArrayList<Integer>();
				sightIDs=org.getSurroundingObjects('h', 20);
				for(Integer i: sightIDs){
					sight.add(healthFd.get(i));
				}
				sightIDs.clear();
				sightIDs=org.getSurroundingObjects('p', 20);
				for(Integer i: sightIDs){
					sight.add(poisFood.get(i));
				}
				org.addScan(sight.size());
				double orgX = norm.normalize(
						org.getLocation().getX());
				double orgY = norm.normalize(
						org.getLocation().getY());
				double health = norm.normalize(org.getHealth());
				double numSurroundingOrgs = norm.normalize(org.getSurroundingObjects('o', 5).size()-1);
				Chromosome chrom = org.getChromosome();
				Pair<Integer, Double> bestEval1 =
					new Pair<Integer, Double> (0, 0.0);
				Pair<Integer, Double> bestEval2 =
					new Pair<Integer, Double> (1, 0.0);
				for (int i = 0; i < chrom.size(); i++) {
					Gene workingGene = chrom.getGene(i);
					//if there is something in org's field of vision.
					if (sight.size() > 0) {
						for(int j = 0; j < sight.size(); j++) {
							HashMap<String, Double> environment =
								new HashMap<String, Double>();
							Food f = sight.get(j);
							double foodX;
							double foodY;
							double orgNearFood;
							double foodRemaining;
							double isPoison = norm.normalize(1.0);
							if(f!=null){
							foodX = norm.normalize(
									f.getLocation().getX());
							foodY = norm.normalize(
									f.getLocation().getY());
							orgNearFood = norm.normalize(
									f.numSurroundingObjects(5));
							foodRemaining = norm.normalize(f.getHealth());
								if(f instanceof PoisonousFood){
									isPoison = -isPoison;
								}
							}
							else{
							foodX=norm.normalize(ran.nextDouble()*100);
							foodY=norm.normalize(ran.nextDouble()*100);
							orgNearFood = norm.normalize(0.0);
							foodRemaining = norm.normalize(0.0);
							}
							Expr result = workingGene.getEvaledList();
							environment.put("a", foodX-orgX);
							environment.put("b", orgY-foodY);
							environment.put("c", orgNearFood);
							environment.put("d", health);
							environment.put("e", foodRemaining);
							environment.put("f", isPoison);
							environment.put("g", numSurroundingOrgs);
							double geneEval = result.evaluate(environment);
							if(geneEval > bestEval1.right() && bestEval1.right() < bestEval2.right()){
								bestEval1.setLeft(i);
								bestEval1.setRight(geneEval);
							}
							else if(geneEval>bestEval2.right() && i!=bestEval1.left()){
								bestEval2.setLeft(i);
								bestEval2.setRight(geneEval);
							}
						}
						sight.clear();
					}
					//TODO: if there isn't anything in org's field of vision. 
					//These numbers need to be worked out.
					else {
						Expr result = workingGene.getEvaledList();
						HashMap<String,Double> environment = new HashMap<String, Double>();
						environment.put("a", norm.normalize(ran.nextDouble()*100)); // not sure what to pass
						environment.put("b", norm.normalize(ran.nextDouble()*100)); // not sure what to pass
						environment.put("c", norm.normalize(0.0));
						environment.put("d", health);
						environment.put("e",norm.normalize(0.0));
						environment.put("f", norm.normalize(1.0));
						environment.put("g", numSurroundingOrgs);
						double geneEval = result.evaluate(environment);
						if(geneEval > bestEval1.right() && bestEval1.right() < bestEval2.right()){
							bestEval1.setLeft(i);
							bestEval1.setRight(geneEval);
						}
						else if(geneEval > bestEval2.right() && i!=bestEval1.left()){
							bestEval2.setLeft(i);
							bestEval2.setRight(geneEval);
						}
					}
				}
				// Genes are set as N-S-E-W-NE-NW-SE-SW-Eat-Attack-PushOrg.
				System.out.println("Org ID: " + org.getId() + " Action 1: " + bestEval1.left() + " Action 2: " + bestEval2.left() + " numObj in Sight:" + sight.size());
				switch (bestEval1.left()) {
				case 0: 
					org.moveNorth(organisms);
					//org.addAction("N", orgIndex);
					org.countStep();
					break;
				case 1: 
					org.moveSouth(organisms);
					//org.addAction("S", orgIndex);
					org.countStep();
					break;
				case 2: 
					org.moveEast(organisms); 
					//org.addAction("E", orgIndex);
					org.countStep();
					break;
				case 3: 
					org.moveWest(organisms);
					//org.addAction("W", orgIndex);
					org.countStep();
					break;
				case 4: 
					org.moveNorthEast(organisms);
					//org.addAction("NE", orgIndex);
					org.countStep();
					break;
				case 5: 
					org.moveNorthWest(organisms);
					//org.addAction("NW", orgIndex);
					org.countStep();
					break;
				case 6: 
					org.moveSouthEast(organisms);
					//org.addAction("SE", orgIndex);
					org.countStep();
					break;
				case 7: 
					org.moveSouthWest(organisms);
					//org.addAction("SW", orgIndex);
					org.countStep();
					break;
				case 8: 
					if(organismIsNextToHealthyFood(org) || organismIsNextToPoisonousFood(org)){}else{org.addEatFail();};
					break;
				case 9:
					ArrayList<Integer> surroundingOrganisms = org.getSurroundingObjects('o', 1);
					if(surroundingOrganisms.size() > 0){
					int index=surroundingOrganisms.get(ran.nextInt(surroundingOrganisms.size()));
					if(index!=org.getId()){
					org.attack(index, organisms);
					}
					}
					break;
				case 10:
					ArrayList<Integer> surroundingOrganismsPush = org.getSurroundingObjects('o', 1);
					if(surroundingOrganismsPush.size() > 0){
					int index=surroundingOrganismsPush.get(ran.nextInt(surroundingOrganismsPush.size()));
					if(index!=org.getId()){
//					System.out.println(org.getId() + " " + index);
					org.pushOrg(index, organisms);
					}
					}
				}
				
				switch (bestEval2.left()) {
				case 0: 
					org.moveNorth(organisms);
					//org.addAction("N", orgIndex);
					org.countStep();
					break;
				case 1: 
					org.moveSouth(organisms);
					//org.addAction("S", orgIndex);
					org.countStep();
					break;
				case 2: 
					org.moveEast(organisms); 
					//org.addAction("E", orgIndex);
					org.countStep();
					break;
				case 3: 
					org.moveWest(organisms);
					//org.addAction("W", orgIndex);
					org.countStep();
					break;
				case 4: 
					org.moveNorthEast(organisms);
					//org.addAction("NE", orgIndex);
					org.countStep();
					break;
				case 5: 
					org.moveNorthWest(organisms);
					//org.addAction("NW", orgIndex);
					org.countStep();
					break;
				case 6: 
					org.moveSouthEast(organisms);
					//org.addAction("SE", orgIndex);
					org.countStep();
					break;
				case 7: 
					org.moveSouthWest(organisms);
					//org.addAction("SW", orgIndex);
					org.countStep();
					break;
				case 8: 
					if(organismIsNextToHealthyFood(org) || organismIsNextToPoisonousFood(org)){}else{org.addEatFail();};
					break;
				case 9:
					ArrayList<Integer> surroundingOrganisms = org.getSurroundingObjects('o', 1);
					if(surroundingOrganisms.size() > 0){
					int index=surroundingOrganisms.get(ran.nextInt(surroundingOrganisms.size()));
					if(index!=org.getId()){
					org.attack(index, organisms);
					}
					}
					break;
				case 10:
					ArrayList<Integer> surroundingOrganismsPush = org.getSurroundingObjects('o', 1);
					if(surroundingOrganismsPush.size() > 0){
					int index=surroundingOrganismsPush.get(ran.nextInt(surroundingOrganismsPush.size()));
					if(index!=org.getId()){
//					System.out.println(org.getId() + " " + index);
					org.pushOrg(index, organisms);
					}
					}
				}
			}
			orgIndex++;
		}
		//End AI LOGIC
	}
	
	private void newTrial(){
		t.stop();
		for(Organism o: organisms){
			o.newLocation();
			o.setHealth(o.getMaxHealth());
			o.clear();
		}
		trialNum++;
		healthFd.clear();
		poisFood.clear();
		for(int i = 0; i < numFoodSources; i++){
			HealthyFood h = new HealthyFood(100.0, i, 2);
			PoisonousFood f = new PoisonousFood(100.0, i, 2);
			healthFd.add(h);
			poisFood.add(f);
		}
		timePassed = 0;
		if(!GUI.genPanel.resumeHasNotBeenClicked() &&
				!GUI.genPanel.genIsSelected()){
			GUI.genPanel.enableResumeSimulation();
			gui.toggleAllPauses(false);
		}
		else{
			t.start();
			GUI.genPanel.newTrial();
		}
	}
	
	private void newGeneration(){
		timePassed = 0;
		double sum = 0;
		g.setOrgList(organisms);
		organisms = g.newGeneration();
		healthFd.clear();
		poisFood.clear();
		clearLocations();
		for(Organism o: organisms) {
			sum+=g.fitness(o);
			o.newLocation();
			o.setHealth(o.getMaxHealth());
			o.clear();
		}
		lastAvg = sum/OptionsPanel.numOrganisms;	
		for(int i = 0; i < numFoodSources; i++) {
			HealthyFood h = new HealthyFood(100.0, i, 2);
			PoisonousFood f = new PoisonousFood(100.0, i, 2);
			healthFd.add(h);
			poisFood.add(f);
		}
		trialNum = 1;
		generationNum++;

		GUI.genPanel.addGeneration();
		if(!GUI.genPanel.resumeHasNotBeenClicked()) {
			GUI.genPanel.enableResumeSimulation();
			gui.toggleAllPauses(false);
		} else {
			t.start();
			GUI.genPanel.newGeneration();
			repaint();
		}
	}
	
	/**
	 * Sets the initial game state of the GridPanel
	 */
	public void initialize(){
		//reset all generation info from previous simulations.
		generationNum = 1;
		trialNum = 1;
		GUI.genPanel.resetGenInformation();
		ran = new Random();
		timePassed=0;
		shuffleIds=new ArrayList<Integer>();
		/*
		 * location map will consist of:
		 * 	key: current instance number of object
		 *  value:
		 * 		'w' for white space or available.
		 * 		'o' for organism.
		 * 		'h' for healthy food.
		 * 		'p' for poisonous food.
		 */
		locationMap = new Pair[GridPanel.WIDTH][GridPanel.HEIGHT];
		clearLocations();
		
		norm = new Normalizer(
				new Pair<Double, Double> (-1000.0, 1000.0),
				new Pair<Double, Double> (-50.0, 50.0));
		
		organisms.clear();
		for(int i = 0; i < OptionsPanel.numOrganisms; i++){
			Organism o = new Organism(600.00, 11, i, 100); //justin b (03.15).
			organisms.add(o);
			shuffleIds.add(i);
			o.addStartingLocation();
			o.addChromosome();
		}
		healthFd.clear();
		poisFood.clear();
		for(int i = 0; i < numFoodSources; i++) {
			HealthyFood h = new HealthyFood(100.0, i, 2);
			PoisonousFood f = new PoisonousFood(100.0, i, 2);
			healthFd.add(h);
			poisFood.add(f);
		}
		g = new GEP(organisms, 0.75, 0.01, 0.01, 0.75, 0.75);
		preProcess(0);
		System.out.println("Preprocess Finished");
	}
	
	/**
	 * Determines whether of not the passed Organism is next to a food source.
	 *
	 * @param org The Organism that is being compared to the food sources.
	 * @return (true/false) whether or not the organism is next to food.
	 */
	private boolean organismIsNextToHealthyFood(Organism org) {
		int leftBoundary = org.getLocation().getX() - Food.width/2;
		int rightBoundary = org.getLocation().getX() + Food.width/2;
		int lowerBoundary = org.getLocation().getY() + Food.height/2;
		int upperBoundary = org.getLocation().getY() - Food.height/2;

		boolean isNextToFood = false;
		for(HealthyFood food: healthFd){
			if(food!=null){
			int leftBoundary2 = food.getLocation().getX() - Organism.width/2 - 1;
			int rightBoundary2 = food.getLocation().getX() + Organism.width/2 + 1;
			int lowerBoundary2 = food.getLocation().getY() + Organism.height/2 + 1;
			int upperBoundary2 = food.getLocation().getY() - Organism.height/2 - 1;

			if((leftBoundary >= leftBoundary2 && leftBoundary <= rightBoundary2 &&
					((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
							(lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2))) ||
							(rightBoundary >= leftBoundary2 && rightBoundary <= rightBoundary2 &&
									((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
											(lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2)))){
				/*
				 * Organism is next to food
				 */
				org.eatFood(food, 5);
				if(food.getHealth() <= 0){ 
					//Delete food source if it is depleted
					food.setRange(food.getWidth(),food.getWidth(), 'w');
					healthFd.set(food.getId(), null);
				}
				isNextToFood = true;
				break;
			}
			}
		}
		return isNextToFood;
	}

	/**
	 * Determines whether of not the passed Organism is next to a food source.
	 *
	 * @param org The Organism that is being compared to the food sources.
	 * @return (true/false) whether or not the organism is next to food.
	 */
	private boolean organismIsNextToPoisonousFood(Organism org) {
		int leftBoundary = org.getLocation().getX() - Food.width/2;
		int rightBoundary = org.getLocation().getX() + Food.width/2;
		int lowerBoundary = org.getLocation().getY() + Food.height/2;
		int upperBoundary = org.getLocation().getY() - Food.height/2;

		boolean isNextToFood = false;
		for(PoisonousFood foodList: poisFood){
			if(foodList!=null){
			int leftBoundary2 = foodList.getLocation().getX() - Organism.width/2 - 1;
			int rightBoundary2 = foodList.getLocation().getX() + Organism.width/2 + 1;
			int lowerBoundary2 = foodList.getLocation().getY() + Organism.height/2 + 1;
			int upperBoundary2 = foodList.getLocation().getY() - Organism.height/2 - 1;

			if((leftBoundary >= leftBoundary2 && leftBoundary <= rightBoundary2 &&
					((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
							(lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2))) ||
							(rightBoundary >= leftBoundary2 && rightBoundary <= rightBoundary2 &&
									((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
											(lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2)))){
				/*
				 * Organism is next to food
				 */
				org.eatFood(foodList, 2);
				if(foodList.getHealth() <= 0){
					//Delete food source if it is depleted
					foodList.setRange(foodList.getWidth(),foodList.getHeight(), 'w');
					poisFood.set(foodList.getId(),null);
				}
				isNextToFood = true;
				break;
			}
			}
		}

		return isNextToFood;
	}

	/**
	 * Preprocess generations
	 * Essentially run the simulation without updating the graphics.
	 */

	public void preProcess(int generations){
		for(numPreProcessedGenerations=0; numPreProcessedGenerations < generations; numPreProcessedGenerations++){
			System.out.println("Processing Generation " + numPreProcessedGenerations);
			while(timePassed < lengthGeneration) {
				/*begin game logic here:*/
				Collections.shuffle(shuffleIds);
				int orgIndex = 0;
				for(Integer orgNum: shuffleIds){
					Organism org=organisms.get(orgNum);
					org.deplete(.5);
					//Take sample of organism health for fitness.
					org.incHlthTot();
					if(org.getHealth() > 0){
						ArrayList<Food> sight = new ArrayList<Food>();
						ArrayList<Integer> sightIDs = new ArrayList<Integer>();
						sightIDs=org.getSurroundingObjects('h', 20);
						for(Integer i: sightIDs){
							sight.add(healthFd.get(i));
						}
						sightIDs.clear();
						sightIDs=org.getSurroundingObjects('p', 20);
						for(Integer i: sightIDs){
							sight.add(poisFood.get(i));
						}
						org.addScan(sight.size());
						double orgX = norm.normalize(
								org.getLocation().getX());
						double orgY = norm.normalize(
								org.getLocation().getY());
						double health = norm.normalize(org.getHealth());
						double numSurroundingOrgs = norm.normalize(org.getSurroundingObjects('o', 5).size()-1);
						Chromosome chrom = org.getChromosome();
						Pair<Integer, Double> bestEval1 =
							new Pair<Integer, Double> (0, 0.0);
						Pair<Integer, Double> bestEval2 =
							new Pair<Integer, Double> (1, 0.0);
						for (int i = 0; i < chrom.size(); i++) {
							Gene workingGene = chrom.getGene(i);
							//if there is something in org's field of vision.
							if (sight.size() > 0) {
								for(int j = 0; j < sight.size(); j++) {
									HashMap<String, Double> environment =
										new HashMap<String, Double>();
									Food f = sight.get(j);
									double foodX;
									double foodY;
									double orgNearFood;
									double foodRemaining;
									double isPoison = norm.normalize(1.0);
									if(f!=null){
									foodX = norm.normalize(
											f.getLocation().getX());
									foodY = norm.normalize(
											f.getLocation().getY());
									orgNearFood = norm.normalize(
											f.numSurroundingObjects(5));
									foodRemaining = norm.normalize(f.getHealth());
										if(f instanceof PoisonousFood){
											isPoison = -isPoison;
										}
									}
									else{
									foodX=norm.normalize(ran.nextDouble()*100);
									foodY=norm.normalize(ran.nextDouble()*100);
									orgNearFood = norm.normalize(0.0);
									foodRemaining = norm.normalize(0.0);
									}
									Expr result = workingGene.getEvaledList();
									environment.put("a", foodX-orgX);
									environment.put("b", orgY-foodY);
									environment.put("c", orgNearFood);
									environment.put("d", health);
									environment.put("e", foodRemaining);
									environment.put("f", isPoison);
									environment.put("g", numSurroundingOrgs);
									double geneEval = result.evaluate(environment);
									if(geneEval > bestEval1.right() && bestEval1.right() < bestEval2.right()){
										bestEval1.setLeft(i);
										bestEval1.setRight(geneEval);
									}
									else if(geneEval>bestEval2.right() && i!=bestEval1.left()){
										bestEval2.setLeft(i);
										bestEval2.setRight(geneEval);
									}
								}
								sight.clear();
							}
							//TODO: if there isn't anything in org's field of vision. 
							//These numbers need to be worked out.
							else {
								Expr result = workingGene.getEvaledList();
								HashMap<String,Double> environment = new HashMap<String, Double>();
								environment.put("a", norm.normalize(ran.nextDouble()*100)); // not sure what to pass
								environment.put("b", norm.normalize(ran.nextDouble()*100)); // not sure what to pass
								environment.put("c", norm.normalize(0.0));
								environment.put("d", health);
								environment.put("e",norm.normalize(0.0));
								environment.put("f", norm.normalize(1.0));
								environment.put("g", numSurroundingOrgs);
								double geneEval = result.evaluate(environment);
								if(geneEval > bestEval1.right() && bestEval1.right() < bestEval2.right()){
									bestEval1.setLeft(i);
									bestEval1.setRight(geneEval);
								}
								else if(geneEval > bestEval2.right() && i!=bestEval1.left()){
									bestEval2.setLeft(i);
									bestEval2.setRight(geneEval);
								}
							}
						}
						// Genes are set as N-S-E-W-NE-NW-SE-SW-Eat-Attack-PushOrg.
						switch (bestEval1.left()) {
						case 0: 
							org.moveNorth(organisms);
							//org.addAction("N", orgIndex);
							org.countStep();
							break;
						case 1: 
							org.moveSouth(organisms);
							//org.addAction("S", orgIndex);
							org.countStep();
							break;
						case 2: 
							org.moveEast(organisms); 
							//org.addAction("E", orgIndex);
							org.countStep();
							break;
						case 3: 
							org.moveWest(organisms);
							//org.addAction("W", orgIndex);
							org.countStep();
							break;
						case 4: 
							org.moveNorthEast(organisms);
							//org.addAction("NE", orgIndex);
							org.countStep();
							break;
						case 5: 
							org.moveNorthWest(organisms);
							//org.addAction("NW", orgIndex);
							org.countStep();
							break;
						case 6: 
							org.moveSouthEast(organisms);
							//org.addAction("SE", orgIndex);
							org.countStep();
							break;
						case 7: 
							org.moveSouthWest(organisms);
							//org.addAction("SW", orgIndex);
							org.countStep();
							break;
						case 8: 
							if(organismIsNextToPoisonousFood(org) || organismIsNextToHealthyFood(org)){}else{org.addEatFail();};
							break;
						case 9:
							ArrayList<Integer> surroundingOrganisms = org.getSurroundingObjects('o', 1);
							if(surroundingOrganisms.size() > 0){
							int index=surroundingOrganisms.get(ran.nextInt(surroundingOrganisms.size()));
							if(index!=org.getId()){
							org.attack(index, organisms);
							}
							}
							break;
						case 10:
							ArrayList<Integer> surroundingOrganismsPush = org.getSurroundingObjects('o', 1);
							if(surroundingOrganismsPush.size() > 0){
							int index=surroundingOrganismsPush.get(ran.nextInt(surroundingOrganismsPush.size()));
							if(index!=org.getId()){
//							System.out.println(org.getId() + " " + index);
							org.pushOrg(index, organisms);
							}
							}
							
						}
						switch (bestEval2.left()) {
						case 0: 
							org.moveNorth(organisms);
							//org.addAction("N", orgIndex);
							org.countStep();
							break;
						case 1: 
							org.moveSouth(organisms);
							//org.addAction("S", orgIndex);
							org.countStep();
							break;
						case 2: 
							org.moveEast(organisms); 
							//org.addAction("E", orgIndex);
							org.countStep();
							break;
						case 3: 
							org.moveWest(organisms);
							//org.addAction("W", orgIndex);
							org.countStep();
							break;
						case 4: 
							org.moveNorthEast(organisms);
							//org.addAction("NE", orgIndex);
							org.countStep();
							break;
						case 5: 
							org.moveNorthWest(organisms);
							//org.addAction("NW", orgIndex);
							org.countStep();
							break;
						case 6: 
							org.moveSouthEast(organisms);
							//org.addAction("SE", orgIndex);
							org.countStep();
							break;
						case 7: 
							org.moveSouthWest(organisms);
							//org.addAction("SW", orgIndex);
							org.countStep();
							break;
						case 8: 
							if(organismIsNextToHealthyFood(org) || organismIsNextToPoisonousFood(org)){}else{org.addEatFail();};
							break;
						case 9:
							ArrayList<Integer> surroundingOrganisms = org.getSurroundingObjects('o', 1);
							if(surroundingOrganisms.size() > 0){
							int index=surroundingOrganisms.get(ran.nextInt(surroundingOrganisms.size()));
							if(index!=org.getId()){
							org.attack(index, organisms);
							}
							}
							break;
						case 10:
							ArrayList<Integer> surroundingOrganismsPush = org.getSurroundingObjects('o', 1);
							if(surroundingOrganismsPush.size() > 0){
							int index=surroundingOrganismsPush.get(ran.nextInt(surroundingOrganismsPush.size()));
							if(index!=org.getId()){
//							System.out.println(org.getId() + " " + index);
							org.pushOrg(index, organisms);
							}
							}
							
						}
					}
					orgIndex++;
				}
				timePassed+=lengthTimeStep;
			} 
			if (trialNum < trialsPerGen) {
				for(Organism o: organisms){
					o.newLocation();
					o.setHealth(o.getMaxHealth());
					o.clear();
				}
				trialNum++;
				healthFd.clear();
				poisFood.clear();
				for(int i = 0; i < OptionsPanel.numOrganisms/2; i++){
					HealthyFood h = new HealthyFood(100.0, i, 2);
					PoisonousFood f = new PoisonousFood(100.0, i, 2);
					healthFd.add(h);
					poisFood.add(f);
				}
				timePassed = 0;
				GUI.genPanel.newTrial();
			} 
			else {
				timePassed = 0;
				double sum = 0;
				g.setOrgList(organisms);
				organisms = g.newGeneration();
				healthFd.clear();
				poisFood.clear();
				clearLocations();
				for(Organism o: organisms) {
					sum+=g.fitness(o);
					o.newLocation();
					o.setHealth(o.getMaxHealth());
					o.clear();
				}
				lastAvg = sum/OptionsPanel.numOrganisms;
				System.out.println(lastAvg);
				
				for(int i = 0; i < OptionsPanel.numOrganisms/2; i++) {
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
		//generationNum = 1;
	}

	//------------------------------------------------------------------------------------
	//--Getters/Setters--
	//------------------------------------------------------------------------------------
	public LinkedList<Organism> getOrganisms(){
		return organisms;
	}

	public GEP getGEP(){
		return g;
	}

	public Timer getTimer(){
		return t;
	}

	public int getCurrTimeStep(){
		return lengthTimeStep;
	}

	public void setTimeStep(int step){
		lengthTimeStep = step;
		t.setDelay(step);
	}
	
	public void start(){
		t.start();
	}
	public void stop(){
		t.stop();
	}
	public boolean isPaused(){
		if(t.isRunning())
			return false;
		return true;
	}
	//------------------------------------------------------------------------------------
	//--Override Functions--
	//------------------------------------------------------------------------------------
	/**
	 * A function that will be called to update the
	 * current state of the panel
	 *
	 * @param g the Graphics object used
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		//handle each new organism created.
		for(Organism org : organisms){
			org.paint(g);
		}
		
		
		for(HealthyFood h: healthFd){
			if(h!=null){
			if(h.getHealth() > 0){
				h.paint(g, false);
			}
			else{
				h.paint(g, true);
			}
			}
		}
		
		for(PoisonousFood p: poisFood){
			if(p!=null){
			if(p.getHealth()>0){
				p.paint(g, false);
			}
			else{
				p.paint(g, true);
			}
			}
		}
	}

}