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
	

	public static boolean[][] isValidLocation;

	private LinkedList<Organism> organisms;
	private LinkedList<HealthyFood> healthFd;
	private LinkedList<PoisonousFood> poisFood;
	private int lengthTimeStep = 100;
	private int lengthGeneration = lengthTimeStep*10;
	private int timePassed = 0;
	private int trialsPerGen = 1;
	public int trialNum = 1;
	public int generationNum = 1;
	public double lastAvg = 0;
	private GEP g;
	private int numFoodSources;
	private Timer t;
	private Normalizer norm;
	private int numPreProcessedGenerations = 0;
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	/**
	 * This constructor will handle all of the movements/interactions of
	 * all objects in the current game state.
	 */
	public GridPanel(final GUI gui) {
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

				//TODO: deplete health
				//fill in if not in sight range info.
				//
				//a timer and it's action event to call at every time t.
				t = new javax.swing.Timer(lengthTimeStep, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(timePassed < lengthGeneration) {
							/*begin game logic here:*/
							timePassed+=lengthTimeStep;
							/*
							for(HealthyFood h: healthyFoodSources){
								h.deplete();
							}

							for(PoisonousFood p: poisonousFoodSources){
								p.deplete();
							}
							 */

							//Dwight's AI LOGIC
							// * Should work pretty well, needs to be tested.
							// * Each gene gets checked for however many food sources are in their sight range.
							// * TODO: Fitness function needs reworking.
							Collections.shuffle(organisms);
							int orgIndex = 0;
							for(Organism org: organisms){
								org.deplete(1.5);
								//Take sample of organism health for fitness.
								org.incHlthTot();
								if(org.getHealth() > 0){
									ArrayList<Food> sight = new ArrayList<Food>();
									ArrayList<Double> chromResults = new ArrayList<Double>();
									sight = org.look(healthFd, poisFood);
									double orgX = norm.normalize(
											org.getLocation().getX());
									double orgY = norm.normalize(
											org.getLocation().getY());
									double health = org.getHealth();
									Chromosome chrom = org.getChromosome();
									Pair<Integer, Double> bestEval =
										new Pair<Integer, Double> (0, 0.0);
									for (int i = 0; i < chrom.size(); i++) {
										Gene workingGene = chrom.getGene(i);
										if (sight.size() > 0) { //if there is something in org's field of vision.
											for(int j = 0; j < sight.size(); j++) {
												HashMap<String, Double> environment =
													new HashMap<String, Double>();
												Food f = sight.get(j);
												double foodX = norm.normalize(
														f.getLocation().getX());
												double foodY = norm.normalize(
														f.getLocation().getY());
												double orgNearFood = norm.normalize(
														f.numSurroundingObjects(2));
												Expr result = workingGene.getEvaledList();
												environment.put("a", foodX-orgX);
												environment.put("b", orgY-foodY);
												environment.put("c", orgNearFood);
												environment.put("d", norm.normalize(health));
												double geneEval = result.evaluate(environment);
												if(geneEval > bestEval.right())
													bestEval = new Pair<Integer, Double> (i, geneEval);
											}
										}
										//TODO: if there isn't anything in org's field of vision. 
										//These numbers need to be worked out.
										else {
											Expr result = Eval.evaluation(workingGene.makeStringArray());
											HashMap<String,Double> environment = new HashMap<String, Double>();
											environment.put("a", norm.normalize(10.0)); // not sure what to pass
											environment.put("b", norm.normalize(10.0)); // not sure what to pass
											environment.put("c", norm.normalize(0.0));
											environment.put("d", norm.normalize(health));
											double geneEval = result.evaluate(environment);
											if(geneEval > bestEval.right())
												bestEval = new Pair<Integer, Double> (i, geneEval);
										}
									}
									// Genes are set as N-S-E-W-NE-NW-SE-SW-Eat.
									switch (bestEval.left()) {
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
									case 8: if(organismIsNextToHealthyFood(org)||
											organismIsNextToPoisonousFood(org)){};
											//org.addAction("F", orgIndex);
									}
								}
								orgIndex++;
							}
							//End AI LOGIC
							repaint();


						} else if (trialNum < trialsPerGen) {
							t.stop();
							for(Organism o: organisms){
								o.newLocation();
								o.setHealth(o.getMaxHealth());
								//TODO: added (03.13) justin.
							}
							trialNum++;
							healthFd.clear();
							poisFood.clear();
							for(int i=0; i<OptionsPanel.numOrganisms/5; i++){
								HealthyFood h = new HealthyFood(100.0, i);
								PoisonousFood f = new PoisonousFood(100.0, i);
								healthFd.add(h);
								poisFood.add(f);
							}

							timePassed=0;
							if(!GUI.genPanel.resumeHasNotBeenClicked() &&
									!GUI.genPanel.genIsSelected()){
								GUI.genPanel.enableResumeSimulation();
								gui.toggleAllPauses(false);
							}
							else{
								t.start();
								GUI.genPanel.newTrial();
							}
						} else {
							t.stop();
							timePassed=0;
							double sum = 0;
							for(Organism o: organisms) {
								sum+=g.fitness(o);
								o.newLocation();
								out.println(o.getFitness());
							}
							lastAvg = sum/OptionsPanel.numOrganisms;
							g.setOrgList(organisms);
							organisms=g.newGeneration();
							healthFd.clear();
							poisFood.clear();
							for(Organism o: organisms){
								//TODO: added (03.13) justin.
								o.setHealth(o.getMaxHealth());
								//o.addGeneration();
							}
							for(int i = 0; i < OptionsPanel.numOrganisms/5; i++){
								HealthyFood h = new HealthyFood(100.0, i);
								PoisonousFood f = new PoisonousFood(100.0, i);
								healthFd.add(h);
								poisFood.add(f);
							}
							trialNum=1;
							generationNum++;

							GUI.genPanel.addGeneration();
							if(!GUI.genPanel.resumeHasNotBeenClicked()){
								GUI.genPanel.enableResumeSimulation();
								gui.toggleAllPauses(false);
							}
							else{
								t.start();
								GUI.genPanel.newGeneration();
								repaint();
							}
						}
					}

					//TODO: Can organism differentiate bw pois and non
					//pois?
					private Pair<Food, Double> findClosestFood(Organism org) {
						for(PoisonousFood p: poisFood) {
							p.getLocation().getX();
							p.getLocation().getY();
						}
						for(HealthyFood h: healthFd) {
							h.getLocation().getX();
							h.getLocation().getY();
						}
						return null;
					}
					//TODO: make all objects on grid inherit from a
					//gamePiece or whatever. Then make this method 
					//available for any piece.
					private void numSurrounding(Food h) {
						// TODO Auto-generated method stub
						int xPos = h.getLocation().getX();
						int yPos = h.getLocation().getY();
					}
				});
			}

		};
		r.run();
	}

	/**
	 * Sets the initial game state of the GridPanel
	 */
	public void initialize(){
		//reset all generation info from previous simulations.
		generationNum = 1;
		trialNum = 1;
		GUI.genPanel.resetGenInformation();

		timePassed=0;
		numFoodSources = 0;
		norm = new Normalizer(
				new Pair<Double, Double> (1.0, 10000.0),
				new Pair<Double, Double> (1.0, 50.0));
		isValidLocation = new boolean[GridPanel.WIDTH][GridPanel.HEIGHT];
		for(int i = 0; i < isValidLocation.length; i++){
			for(int j = 0; j < isValidLocation[i].length; j++)
				isValidLocation[i][j] = true;
		}

		organisms.clear();
		for(int i=0; i<OptionsPanel.numOrganisms; i++){
			Organism o = new Organism(500.00, 9, i, 100); //justin b (03.15).
			organisms.add(o);
			o.addStartingLocation();
			o.addChromosome();
		}
		healthFd.clear();
		for(int i = 0; i < OptionsPanel.numOrganisms/5; i++){
			HealthyFood h = new HealthyFood(100.0, i);
			healthFd.add(h);
			numFoodSources++;
		}
		poisFood.clear();
		for(int i = 0; i < OptionsPanel.numOrganisms/5; i++){
			PoisonousFood p = new PoisonousFood(100.0, i);
			poisFood.add(p);
		}
		g = new GEP(organisms, 0.75, 0.01, 0.01, 0.75, 0.75);
		preProcess(10000);
	}
	
	//------------------------------------------------------------------------------------
	//--accessors and mutators--
	//------------------------------------------------------------------------------------
	/**For the timer*/
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
				if(food.getFoodRemaining() <= 0){ //TODO: may not need this. GridPanel may handle this already.
					//Delete food source if it is depleted
					healthFd.remove(food);
				}
				isNextToFood = true;
				break;
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
				if(foodList.getFoodRemaining() <= 0){
					//Delete food source if it is depleted
					poisFood.remove(foodList);
				}
				isNextToFood = true;
				break;
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
			if(timePassed < lengthGeneration) {
				/*begin game logic here:*/
				timePassed+=lengthTimeStep;
				Collections.shuffle(organisms);
				int orgIndex = 0;
				for(Organism org: organisms){
					org.deplete(5.0);
					//Take sample of organism health for fitness.
					org.incHlthTot(); //TODO: added (03.13) justin.
					if(org.getHealth() > 0){
						ArrayList<Food> sight = new ArrayList<Food>();
						sight = org.look(healthFd, poisFood);
						double orgX = norm.normalize(
								org.getLocation().getX());
						double orgY = norm.normalize(
								org.getLocation().getY());
						double health = org.getHealth();
						Chromosome chrom = org.getChromosome();
						Pair<Integer, Double> bestEval =
							new Pair<Integer, Double> (0, 0.0);
						for (int i = 0; i < chrom.size(); i++) {
							Gene workingGene = chrom.getGene(i);
							if (sight.size() > 0) { //if there is something in org's field of vision.
								for(int j = 0; j < sight.size(); j++) {
									HashMap<String, Double> environment =
										new HashMap<String, Double>();
									Food f = sight.get(j);
									double foodX = norm.normalize(
											f.getLocation().getX());
									double foodY = norm.normalize(
											f.getLocation().getY());
									double orgNearFood = norm.normalize(
											f.numSurroundingObjects(2));
									Expr result = workingGene.getEvaledList();
									environment.put("a", foodX-orgX);
									environment.put("b", orgY-foodY);
									environment.put("c", orgNearFood);
									environment.put("d", norm.normalize(health));
									double geneEval = result.evaluate(environment);
									if(geneEval > bestEval.right())
										bestEval = new Pair<Integer, Double> (i, geneEval);
								}
								sight.clear();
							}
							//TODO: if there isn't anything in org's field of vision. 
							//These numbers need to be worked out.
							else {
								Expr result = Eval.evaluation(workingGene.makeStringArray());
								HashMap<String,Double> environment = new HashMap<String, Double>();
								environment.put("a", norm.normalize(10.0)); // not sure what to pass
								environment.put("b", norm.normalize(10.0)); // not sure what to pass
								environment.put("c", norm.normalize(0.0));
								environment.put("d", norm.normalize(health));
								double geneEval = result.evaluate(environment);
								if(geneEval > bestEval.right())
									bestEval = new Pair<Integer, Double> (i, geneEval);
							}
						}
						// Genes are set as N-S-E-W-NE-NW-SE-SW-Eat.
						switch (bestEval.left()) {
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
						case 8: if(organismIsNextToHealthyFood(org)||
								organismIsNextToPoisonousFood(org)){};
								//org.addAction("F", orgIndex);
						}
					}
					orgIndex++;
				}
			} else if (trialNum < trialsPerGen) {
				for(Organism o: organisms){
					o.newLocation();
					o.setHealth(o.getMaxHealth());
					//TODO: added (03.13) justin.
				}
				trialNum++;
				healthFd.clear();
				poisFood.clear();
				for(int i=0; i<OptionsPanel.numOrganisms/5; i++){
					HealthyFood h = new HealthyFood(100.0, i);
					PoisonousFood f = new PoisonousFood(100.0, i);
					healthFd.add(h);
					poisFood.add(f);
				}
				timePassed=0;
				//GUI.genPanel.newTrial();
			} else {
				timePassed=0;
				int sum = 0;
				for(Organism o: organisms) {
					sum+=g.fitness(o);
					o.newLocation();
				}
				lastAvg = sum/OptionsPanel.numOrganisms;
				g.setOrgList(organisms);
				organisms=g.newGeneration();
				healthFd.clear();
				poisFood.clear();
				for(Organism o: organisms){
					o.setHealth(o.getMaxHealth());
					//o.addGeneration();
				}
				for(int i = 0; i < OptionsPanel.numOrganisms/5; i++){
					HealthyFood h = new HealthyFood(100.0, i);
					PoisonousFood f = new PoisonousFood(100.0, i);
					healthFd.add(h);
					poisFood.add(f);
				}
				trialNum=1;
				generationNum++;
				//GUI.genPanel.addGeneration();
				//GUI.genPanel.newGeneration();
			}
		}
		generationNum=1;
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
			if(h.getFoodRemaining()>0){
				h.paint(g, false);
			}
			else{
				h.paint(g, true);
			}
		}
		for(PoisonousFood p: poisFood){
			if(p.getFoodRemaining()>0){
				p.paint(g, false);
			}
			else{
				p.paint(g, true);
			}
		}
	}

}
