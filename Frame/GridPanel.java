package Frame;

import Evaluation.Eval;
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
	protected static final int sightRange = 100;

	public static boolean[][] isValidLocation;

	private LinkedList<Organism> organisms;
	private LinkedList<HealthyFood> healthyFoodSources;
	private LinkedList<PoisonousFood> poisonousFoodSources;
	private int lengthTimeStep=100;
	private int lengthGeneration=lengthTimeStep*100;
	private int timePassed=0;
	private int trialsPerGen=1;
	public int trialNum=1;
	public int generationNum=1;
	public int lastAvg=0;
	private GEP g;
	private int numFoodSources;
	private Timer t;

	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	/**
	 * This constructor will handle all of the movements/interactions of
	 * all objects in the current game state.
	 */
	public GridPanel(final GUI gui){
		Runnable r = new Runnable(){
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
						try{
							//get and display current mouse location.
							Coordinate mouseLocation = new Coordinate(arg0.getX(), arg0.getY());
							MonitorPanel.currMouseLoc.setText(mouseLocation.toString());

							//used to manage checks
							boolean isOrg = false;
							boolean isPFood = false;
							boolean isHFood = false;

							//check mouse location vs. all organism's locations.
							for(Organism o: organisms){
								if(mouseLocation.approxEquals(o.getLocation(), Organism.width/2)){
									//organism found
									isOrg = true;
									MonitorPanel.simObjInfo.setText(o.toString());
									//break to prevent any more updating from occuring and loop overhead.
									break;
								}
								else{
									isOrg = false;
								}
							}

							for(HealthyFood r: healthyFoodSources){
								if(mouseLocation.approxEquals(r.getLocation(),Food.width/2)){
									//food found
									isHFood = true;
									MonitorPanel.simObjInfo.setText(r.toString());
									//break to prevent any more updating from occuring and loop overhead.
									break;
								}
								else{
									isHFood = false;
								}
							}
							for(PoisonousFood r: poisonousFoodSources){
								if(mouseLocation.approxEquals(r.getLocation(),Food.width/2)){
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
				healthyFoodSources = new LinkedList<HealthyFood>();
				poisonousFoodSources = new LinkedList<PoisonousFood>();

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
							// * Should work pretty well, needs to be tested. EDIT: Mad slow.
							// * Genes are set as N-S-E-W-NE-NW-SE-SW-Eat.
							// * Each gene gets checked for however many food sources are in their sight range.
							// * TODO: Fitness function needs reworking.
							Collections.shuffle(organisms);
							for(Organism org: organisms){
								org.depleteHealth();
								//Take sample of organism health for fitness.
								org.updateAvgHealth(); //TODO: added (03.13) justin.
								if(org.getHealth() > 0){
									Chromosome chrom = org.getChromosome();
									ArrayList<Food> sight = new ArrayList<Food>();
									ArrayList<Double> decisions = new ArrayList<Double>();
									sight = getSight(org);
									double orgX = org.getLocation().getX();
									double orgY = org.getLocation().getY();
									double health = org.getHealth();
									for (int i = 0; i < chrom.size(); i++) {
										Gene workingGene = chrom.getGene(i);
										if (sight.size() > 0) { //if there is something in org's field of vision.
											double max =0;
											for(int j = 0;j < sight.size(); j++){
												HashMap<String, Double> environment = new HashMap<String, Double>();
												Food f = sight.get(j);
												double foodX=f.getLocation().getX();
												double foodY=f.getLocation().getY();
												double orgNearFood = f.numSurroundingObjects(5);
												Expr result = Eval.evaluation(workingGene.makeStringArray());
												environment.put("a", foodX-orgX);
												environment.put("b", orgY-foodY);
												environment.put("c", orgNearFood);
												environment.put("d", health);
												if(result.evaluate(environment) > max){
													max = result.evaluate(environment);
												}
											}
											decisions.add(max);
										}
										//TODO: if there isn't anything in org's field of vision. 
										//These numbers need to be worked out.
										else { 
											Expr result = Eval.evaluation(workingGene.makeStringArray());
											HashMap<String,Double> environment = new HashMap<String, Double>();
											environment.put("a", 10.0); // not sure what to pass
											environment.put("b", 10.0); // not sure what to pass
											environment.put("c", 0.0);
											environment.put("d", health);
											decisions.add(result.evaluate(environment));
										}
									}
									int decision = 0;
									double best=decisions.get(0);
									for(int i = 1 ; i<decisions.size();i++){
										if(decisions.get(i) > best){
											decision = i;
											best=decisions.get(i);
										}
									}
									//System.out.println(decision);
									switch (decision) {
									case 0: 
										org.moveNorth(organisms);
										org.countStep();
										break;
									case 1: 
										org.moveSouth(organisms);
										org.countStep();
										break;
									case 2: 
										org.moveEast(organisms); 
										org.countStep();
										break;
									case 3: 
										org.moveWest(organisms);
										org.countStep();
										break;
									case 4: 
										org.moveNorthEast(organisms);
										org.countStep();
										break;
									case 5: 
										org.moveNorthWest(organisms);
										org.countStep();
										break;
									case 6: 
										org.moveSouthEast(organisms);
										org.countStep();
										break;
									case 7: 
										org.moveSouthWest(organisms);
										org.countStep();
										break;
									case 8: if(organismIsNextToHealthyFood(org)
											|| organismIsNextToPoisonousFood(org));
									}
								}
							}
							//End AI LOGIC
							repaint();


						} else if (trialNum < trialsPerGen) {
							t.stop();
							for(Organism o: organisms){
								o.newLocation();
								o.setHealth(7500);
								//TODO: added (03.13) justin.
								o.calcAvgHealth();
							}
							trialNum++;
							healthyFoodSources.clear();
							poisonousFoodSources.clear();
							for(int i=0; i<OptionsPanel.numOrganisms/2; i++){
								HealthyFood h = new HealthyFood();
								PoisonousFood f = new PoisonousFood();
								healthyFoodSources.add(h);
								poisonousFoodSources.add(f);
							}

							timePassed=0;
							if(!GUI.genPanel.resumeHasNotBeenClicked() && !GUI.genPanel.genIsSelected()){
								GUI.genPanel.enableResumeSimulation();
								gui.disableAllPauses();
							}
							else{
								t.start();
								GUI.genPanel.newTrial();
							}
						} else {
							t.stop();
							timePassed=0;
							int sum = 0;
							for(Organism o: organisms){
								sum+=g.fitness(o);
								o.newLocation();
							}
							lastAvg=sum/OptionsPanel.numOrganisms;
							g.setOrgList(organisms);
							organisms=g.newGeneration();
							healthyFoodSources.clear();
							poisonousFoodSources.clear();
							for(Organism o: organisms){
								//TODO: added (03.13) justin.
								o.calcAvgHealth();
								o.setHealth(7500);
							}
							for(int i=0; i<OptionsPanel.numOrganisms/2; i++){
								HealthyFood h = new HealthyFood();
								PoisonousFood f = new PoisonousFood();
								healthyFoodSources.add(h);
								poisonousFoodSources.add(f);
							}
							trialNum=1;
							generationNum++;

							GUI.genPanel.addGeneration();
							if(!GUI.genPanel.resumeHasNotBeenClicked()){
								GUI.genPanel.enableResumeSimulation();
								gui.disableAllPauses();
							}
							else{
								t.start();
								GUI.genPanel.newGeneration();
								repaint();
							}

						}
					}

					private ArrayList<Food> getSight(Organism org) {
						ArrayList<Food> toReturn = new ArrayList<Food>();
						Coordinate orgCoord = org.getLocation();
						int orgX= orgCoord.getX();
						int orgY= orgCoord.getY();
						for(Food f : healthyFoodSources){
							if(Math.abs(orgX-f.getLocation().getX()) <= sightRange ||
									Math.abs(orgY-f.getLocation().getY()) <= sightRange ){
								toReturn.add(f);
							}
						}
						for(Food f : poisonousFoodSources){
							if(Math.abs(orgX-f.getLocation().getX()) <= sightRange ||
									Math.abs(orgY-f.getLocation().getY()) <= sightRange ){
								toReturn.add(f);
							}
						}

						return toReturn;
					}


					//TODO: Can organism differentiate bw pois and non
					//pois?
					private Pair<Food, Double> findClosestFood(Organism org) {
						for(PoisonousFood p: poisonousFoodSources) {
							p.getLocation().getX();
							p.getLocation().getY();
						}
						for(HealthyFood h: healthyFoodSources) {
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
						//				if() {
						//					
						//				}
					}
				});
			}

		};
		r.run();
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
	 * Sets the initial game state of the GridPanel
	 */
	public void initialize(){
		//reset all generation info from previous simulations.
		generationNum = 1;
		trialNum = 1;
		GUI.genPanel.resetGenInformation();
		
		timePassed=0;
		numFoodSources = 0;
		isValidLocation = new boolean[GridPanel.WIDTH][GridPanel.HEIGHT];
		for(int i=0; i<isValidLocation.length; i++){
			for(int j=0; j<isValidLocation[i].length; j++){
				isValidLocation[i][j] = true;
			}
		}

		organisms.clear();
		for(int i=0; i<OptionsPanel.numOrganisms; i++){
			Organism o = new Organism();
			organisms.add(o);
		}
		healthyFoodSources.clear();
		for(int i=0; i<OptionsPanel.numOrganisms/2; i++){
			HealthyFood h = new HealthyFood();
			healthyFoodSources.add(h);
			numFoodSources++;
		}
		poisonousFoodSources.clear();
		for(int i=0; i<OptionsPanel.numOrganisms/2; i++){
			PoisonousFood p = new PoisonousFood();
			poisonousFoodSources.add(p);
		}
		g= new GEP(organisms, 1,1,1,1,1);
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
		for(HealthyFood food: healthyFoodSources){
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
				org.eatFood(food);
				if(food.getFoodRemaining() <= 0){ //TODO: may not need this. GridPanel does this.
					//Delete food source if it is depleted
					healthyFoodSources.remove(food);
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
		for(PoisonousFood foodList: poisonousFoodSources){
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
				org.eatFood(foodList);
				if(foodList.getFoodRemaining() <= 0){
					//Delete food source if it is depleted
					poisonousFoodSources.remove(foodList);
				}
				isNextToFood = true;
				break;
			}
		}

		return isNextToFood;
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
		for(HealthyFood h: healthyFoodSources){
			if(h.getFoodRemaining()>0){
				h.paint(g, false);
			}
			else{
				h.paint(g, true);
			}
		}
		for(PoisonousFood p: poisonousFoodSources){
			if(p.getFoodRemaining()>0){
				p.paint(g, false);
			}
			else{
				p.paint(g, true);
			}
		}
	}

}
