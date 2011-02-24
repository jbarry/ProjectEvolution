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
import java.util.LinkedList;
import java.util.Random;
import Interactive.Pair;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Evaluation.Expr;
/**
 * This class will handle all of the simulation's display.
 */
@SuppressWarnings("all")
public class GridPanel extends JPanel implements Runnable
{
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	public final static int WIDTH = 600;
	public final static int HEIGHT = 400;

	public static boolean[][] isValidLocation;

	private LinkedList<Organism> organisms;
	private LinkedList<HealthyFood> healthyFoodSources;
	private LinkedList<PoisonousFood> poisonousFoodSources;
	private int lengthTimeStep=50;
	private int lengthGeneration=lengthTimeStep*2;
	private int timePassed=0;
	private int trialsPerGen=2;
	public int trialNum=1;
	public int generationNum=1;
	public int lastAvg=0;
	private GEP g;
	private javax.swing.Timer t;
	private int numFoodSources;
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	/**
	 * This constructor will handle all of the movements/interactions of
	 * all objects in the current game state.
	 */
	public GridPanel()
	{
		run();
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
		for(HealthyFood foodList: healthyFoodSources){
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
					healthyFoodSources.remove(foodList);
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

		//a timer and it's action event to call at every time t.
		t = new javax.swing.Timer(lengthTimeStep, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(timePassed < lengthGeneration) {
					/*begin game logic here:*/
					timePassed+=lengthTimeStep;
					System.out.println(timePassed);
					for(Organism org: organisms) {

						if(organismIsNextToHealthyFood(org) || organismIsNextToPoisonousFood(org)){
							//organism eats food
						}
						//otherwise keep moving.
						else {
							//create new, random number 0-7 representing a movement.
							Random r = new Random();
							int movement = r.nextInt(8);
							//perform movement
							switch(movement){
							case 0: org.moveNorth(organisms); break;
							case 1: org.moveNorthEast(organisms); break;
							case 2: org.moveEast(organisms); break;
							case 3: org.moveSouthEast(organisms); break;
							case 4: org.moveSouth(organisms); break;
							case 5: org.moveSouthWest(organisms); break;
							case 6: org.moveWest(organisms); break;
							case 7: org.moveNorthWest(organisms); break;
							}
						}
					}

					for(HealthyFood h: healthyFoodSources){
						//h.deplete();
					}

					for(PoisonousFood p: poisonousFoodSources){
						//p.deplete();
					}

					repaint();

					//Begin AI logic. ROUGH
					//TODO: make org find its way to closest food source.
					//variables in gene:
					//distance to closest food(maybe put in a certain range)
					//#opponents around food in food
					//amount left in food
					//amount of health left
					for (Organism org: organisms) {
						Chromosome chrom = org.getChromosome();
						//The first loop is for the food genes.
						//ie deciding which food source to go to.
						for(int i = 0; i < chrom.size(); i++) {
							Expr result = Eval.evaluation(
									chrom.getGene(i).makeStringArray());
							HashMap<String, Double> environment =
								new HashMap<String, Double>();
							Pair<Food, Double> foodDistPair =
								findClosestFood(org);
							environment.put("x", foodDistPair.right());
							environment.put("y", foodDistPair.left().
									numSurroundingObjects(5));
							environment.put("z", org.getHealth());
							environment.put("w", foodDistPair.
									left().getFoodRemaining());
							result.evaluate(environment);
						}
					}
				} else if (trialNum < trialsPerGen) {
					t.stop();
					for(Organism o: organisms){
						o.newLocation();
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
					GUI.genPanel.newTrial();
					timePassed=0;
					t.start();
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
					
					for(int i=0; i<OptionsPanel.numOrganisms/2; i++){
						HealthyFood h = new HealthyFood();
						PoisonousFood f = new PoisonousFood();
						healthyFoodSources.add(h);
						poisonousFoodSources.add(f);
					}
					trialNum=1;
					generationNum++;
					GUI.genPanel.newGeneration();
					t.start();
					repaint();
				}
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
}
