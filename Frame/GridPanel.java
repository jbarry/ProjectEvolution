package Frame;

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

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

	private LinkedList<Organism> organisms;
	private LinkedList<HealthyFood> healthyFoodSources;
	private LinkedList<PoisonousFood> poisonousFoodSources;
	private int lengthTimeStep=50;
	private int lengthGeneration=lengthTimeStep*200;
	private int timePassed=0;
	private GEP g;
	private javax.swing.Timer t;
	private boolean[][] validLocationMap;

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
		organisms.clear();
		healthyFoodSources.clear();
		poisonousFoodSources.clear();
		g= new GEP(organisms, 1,1,1,1,1);
		
		boolean validOrganismLocation = false;
		for(int i=0; i<OptionsPanel.numOrganisms; i++){
			while(!validOrganismLocation){
				Organism o = new Organism();
				boolean conflicts = organismConflictsWithAnotherOrganism(o);
				if(!conflicts){
					organisms.add(o);
					validOrganismLocation = true;
				}
			}
			validOrganismLocation = false;
			
		}
		initHealthyFoodLocations();
		initPoisonousFoodLocations();
	}

	/**
	 * Handles objects that stray off of the GridPanel and wraps their location.
	 *
	 * @param o The Organism object to apply the wrap-setting to.
	 */
	public void setWrapAround(Organism o){
		if(o.getLocation().getX() > GridPanel.WIDTH){
			o.getLocation().setX(o.getLocation().getX() - GridPanel.WIDTH);
		}
		if(o.getLocation().getX() <= 0){
			o.getLocation().setX(o.getLocation().getX() + GridPanel.WIDTH);
		}
		if(o.getLocation().getY() > GridPanel.HEIGHT){
			o.getLocation().setY(o.getLocation().getY() - GridPanel.HEIGHT);
		}
		if(o.getLocation().getY() <= 0){
			o.getLocation().setY(o.getLocation().getY() + GridPanel.HEIGHT);
		}
	}
	
	/**
	 * Dwight will mess with this
	 */
	public void initNextGeneration(){
		boolean validOrganismLocation = false;
		for(Organism o: organisms){
			o.newLocation();
		}
		validOrganismLocation=false;
		for(int i=0; i<OptionsPanel.numOrganisms; i++){
			while(!validOrganismLocation){
				Organism o = organisms.get(i);
				o.newLocation();
				boolean conflicts = organismConflictsWithAnotherOrganism(o);
				System.out.println(conflicts);
				if(!conflicts){
					validOrganismLocation = true;
				}
			}
			validOrganismLocation = false;
			
		}
		
		initHealthyFoodLocations();
		initPoisonousFoodLocations();
	}
	
	/**
	 * Adds valid healthy food locations to the grid
	 */
	public void initHealthyFoodLocations(){
		healthyFoodSources.clear();
		boolean validHealthyFoodLocation = false;
		for(int i=0;i<OptionsPanel.numOrganisms/2;i++){
			while(!validHealthyFoodLocation){
				HealthyFood h = new HealthyFood();
				//Get the boundaries of the potential food source
				int leftBoundary = h.getLocation().getX() - 2;
				int rightBoundary = h.getLocation().getX() + 2;
				int lowerBoundary = h.getLocation().getY() + 2;
				int upperBoundary = h.getLocation().getY() - 2;
				
				/*
				 * Check to see if the potential food source conflicts
				 * with already created foods.
				 */
				boolean conflict = false;
				for(HealthyFood foodList: healthyFoodSources){
					//Get the boundaries of the already created food sources
					int leftBoundary2 = foodList.getLocation().getX() - 2;
					int rightBoundary2 = foodList.getLocation().getX() + 2;
					int lowerBoundary2 = foodList.getLocation().getY() + 2;
					int upperBoundary2 = foodList.getLocation().getY() - 2;
					
					if((leftBoundary >= leftBoundary2 && leftBoundary <= rightBoundary2 &&
						((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
						 (lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2))) ||
						 (rightBoundary >= leftBoundary2 && rightBoundary <= rightBoundary2 &&
							((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
							(lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2)))){
						//There is a conflict
						conflict = true;
						break;
					}
					else{
						//There is not a conflict
					}
				}
				
				if(!conflict){
					//No conflicts yet, so check if food conflicts with any organism
					conflict = foodConflictsWithOrganisms(h);
				}
				
				if(!conflict){
					//No conflicts, so add food to foodSource list
					healthyFoodSources.add(h);
					validHealthyFoodLocation = true;
				}
			}
			validHealthyFoodLocation = false;
		}
	}
	
	/**
	 * Adds valid poisonous food locations to the grid
	 */
	public void initPoisonousFoodLocations(){
		poisonousFoodSources.clear();
		boolean validPoisonousFoodLocation = false;
		for(int i=0;i<OptionsPanel.numOrganisms/2;i++){
			while(!validPoisonousFoodLocation){
				PoisonousFood p = new PoisonousFood();
				//Get the boundaries of the potential food source
				int leftBoundary = p.getLocation().getX() - 2;
				int rightBoundary = p.getLocation().getX() + 2;
				int lowerBoundary = p.getLocation().getY() + 2;
				int upperBoundary = p.getLocation().getY() - 2;
				
				/*
				 * Check to see if the potential food source conflicts
				 * with already created foods.
				 */
				boolean conflict = false;
				for(PoisonousFood foodList: poisonousFoodSources){
					//Get the boundaries of the already created food sources
					int leftBoundary2 = foodList.getLocation().getX() - 2;
					int rightBoundary2 = foodList.getLocation().getX() + 2;
					int lowerBoundary2 = foodList.getLocation().getY() + 2;
					int upperBoundary2 = foodList.getLocation().getY() - 2;
					
					if((leftBoundary >= leftBoundary2 && leftBoundary <= rightBoundary2 &&
						((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
						 (lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2))) ||
						 (rightBoundary >= leftBoundary2 && rightBoundary <= rightBoundary2 &&
							((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
							(lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2)))){
						//There is a conflict
						conflict = true;
						break;
					}
					else{
						//There is not a conflict
					}
				}
				
				if(!conflict){
					//No conflicts yet, so check if food conflicts with any organism
					conflict = foodConflictsWithOrganisms(p);
				}
				
				if(!conflict){
					//No conflicts yet, so check if poisonous food conflicts with any healthy food
					conflict = poisonousFoodConflictsWithHealthyFood(p);
				}
				
				if(!conflict){
					//No conflicts, so add food to foodSource list
					poisonousFoodSources.add(p);
					validPoisonousFoodLocation = true;
				}
			}
			validPoisonousFoodLocation = false;
		}
	}
	
	/**
	 * Determines whether or not an organism conflicts with another organisms' location
	 * 
	 * @param o The Organism that is being compared to the list of organisms.
	 * @return (true/false) whether or not the organism conflicts with another organism.
	 */
	private boolean organismConflictsWithAnotherOrganism(Organism o) {
		int leftBoundary = o.getLocation().getX() - 2;
		int rightBoundary = o.getLocation().getX() + 2;
		int lowerBoundary = o.getLocation().getY() + 2;
		int upperBoundary = o.getLocation().getY() - 2;
		
		boolean conflictsWithOrganism = false;
		for(Organism org: organisms){
			int leftBoundary2 = org.getLocation().getX() - 2;
			int rightBoundary2 = org.getLocation().getX() + 2;
			int lowerBoundary2 = org.getLocation().getY() + 2;
			int upperBoundary2 = org.getLocation().getY() - 2;
			
			if((leftBoundary >= leftBoundary2 && leftBoundary <= rightBoundary2 &&
				((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
				 (lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2))) ||
				 (rightBoundary >= leftBoundary2 && rightBoundary <= rightBoundary2 &&
					((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
					(lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2)))){
				/*
				 * Potential organism conflicts with another organism's location
				 */
				conflictsWithOrganism = true;
				break;
			}
		}
		return conflictsWithOrganism;
	}
	
	/**
	 * Determines whether of not the passed Organism is next to a food source.
	 * 
	 * @param org The Organism that is being compared to the food sources.
	 * @return (true/false) whether or not the organism is next to food.
	 */
	private boolean organismIsNextToHealthyFood(Organism org) {
		int leftBoundary = org.getLocation().getX() - 2;
		int rightBoundary = org.getLocation().getX() + 2;
		int lowerBoundary = org.getLocation().getY() + 2;
		int upperBoundary = org.getLocation().getY() - 2;
		
		boolean isNextToFood = false;
		for(HealthyFood foodList: healthyFoodSources){
			int leftBoundary2 = foodList.getLocation().getX() - 2;
			int rightBoundary2 = foodList.getLocation().getX() + 2;
			int lowerBoundary2 = foodList.getLocation().getY() + 2;
			int upperBoundary2 = foodList.getLocation().getY() - 2;
			
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
		int leftBoundary = org.getLocation().getX() - 2;
		int rightBoundary = org.getLocation().getX() + 2;
		int lowerBoundary = org.getLocation().getY() + 2;
		int upperBoundary = org.getLocation().getY() - 2;
		
		boolean isNextToFood = false;
		for(PoisonousFood foodList: poisonousFoodSources){
			int leftBoundary2 = foodList.getLocation().getX() - 2;
			int rightBoundary2 = foodList.getLocation().getX() + 2;
			int lowerBoundary2 = foodList.getLocation().getY() + 2;
			int upperBoundary2 = foodList.getLocation().getY() - 2;
			
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
	
	/**
	 * Checks if the potential food location conflicts with any organism.
	 * 
	 * @param f The food source being compared to the list of organisms.
	 * @return (true/false) whether or not the food is spawned on organism.
	 */
	private boolean foodConflictsWithOrganisms(Food f) {
		int leftBoundary = f.getLocation().getX() - 2;
		int rightBoundary = f.getLocation().getX() + 2;
		int lowerBoundary = f.getLocation().getY() + 2;
		int upperBoundary = f.getLocation().getY() - 2;
		
		boolean conflictsWithOrganism = false;
		for(Organism org: organisms){
			int leftBoundary2 = org.getLocation().getX() - 2;
			int rightBoundary2 = org.getLocation().getX() + 2;
			int lowerBoundary2 = org.getLocation().getY() + 2;
			int upperBoundary2 = org.getLocation().getY() - 2;
			
			if((leftBoundary >= leftBoundary2 && leftBoundary <= rightBoundary2 &&
				((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
				 (lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2))) ||
				 (rightBoundary >= leftBoundary2 && rightBoundary <= rightBoundary2 &&
					((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
					(lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2)))){
				/*
				 * Food conflicts with organism location
				 */
				conflictsWithOrganism = true;
				break;
			}
		}
		return conflictsWithOrganism;
	}
	
	/**
	 * Checks if the potential poisonous food location conflicts with 
	 * any healthy food location.
	 * 
	 * @param p The poisonous food source being compared to the list of
	 *  healthy food sources.
	 * @return (true/false) whether or not the poisonous food is spawned
	 *  on a healthy food source.
	 */
	private boolean poisonousFoodConflictsWithHealthyFood(PoisonousFood p) {
		int leftBoundary = p.getLocation().getX() - 2;
		int rightBoundary = p.getLocation().getX() + 2;
		int lowerBoundary = p.getLocation().getY() + 2;
		int upperBoundary = p.getLocation().getY() - 2;
		
		boolean conflictsWithHealthyFood = false;
		for(HealthyFood h: healthyFoodSources){
			int leftBoundary2 = h.getLocation().getX() - 2;
			int rightBoundary2 = h.getLocation().getX() + 2;
			int lowerBoundary2 = h.getLocation().getY() + 2;
			int upperBoundary2 = h.getLocation().getY() - 2;
			
			if((leftBoundary >= leftBoundary2 && leftBoundary <= rightBoundary2 &&
				((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
				 (lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2))) ||
				 (rightBoundary >= leftBoundary2 && rightBoundary <= rightBoundary2 &&
					((upperBoundary >= upperBoundary2 && upperBoundary <= lowerBoundary2) ||
					(lowerBoundary >= upperBoundary2 && lowerBoundary <= lowerBoundary2)))){
				/*
				 * Poisonous food conflicts with healthy food location
				 */
				conflictsWithHealthyFood = true;
				break;
			}
		}
		return conflictsWithHealthyFood;
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
			setWrapAround(org);
			//System.out.println(org.getLocation());
			org.paint(g);
		}
		for(HealthyFood h: healthyFoodSources){
			if(h.getFoodRemaining()>0){
				//System.out.println(h.getFoodRemaining());
				h.paint(g, false);
			}
			else{
				//System.out.println(h.getFoodRemaining());
				h.paint(g, true);
			}
		}
		for(PoisonousFood p: poisonousFoodSources){
			if(p.getFoodRemaining()>0){
				//System.out.println(p.getFoodRemaining());
				p.paint(g, false);
			}
			else{
				//System.out.println(p.getFoodRemaining());
				p.paint(g, true);
			}
		}
	}

	@Override
	public void run() {
		//initial JPanel settings
		setLayout(null);
		setLocation(GUI.WIDTH - GridPanel.WIDTH,0);
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
						if(mouseLocation.approxEquals(o.getLocation(),5)){
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
						if(mouseLocation.approxEquals(r.getLocation(),5)){
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
						if(mouseLocation.approxEquals(r.getLocation(),5)){
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
					
					if(!isHFood & !isPFood & !isOrg){
						MonitorPanel.simObjInfo.setText("No Object Selected");
					}
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
				// TODO Auto-generated method stub
				
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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		};
		addMouseListener(simMouseListener);

		//initial program settings
		organisms = new LinkedList<Organism>();
		healthyFoodSources = new LinkedList<HealthyFood>();
		poisonousFoodSources = new LinkedList<PoisonousFood>();
		validLocationMap= new boolean[GridPanel.WIDTH][GridPanel.HEIGHT];
		for(int i=0;i<validLocationMap.length;i++){
			for(int j=0;j<validLocationMap[i].length;j++){
				validLocationMap[i][j]=true;
			}
		}

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
						else{
							//create new, random number 0-7 representing a movement.
							Random r = new Random();
							int movement = r.nextInt(8);
							
							//perform movement
							switch(movement){
							case 0: org.moveNorth(); break;
							case 1: org.moveNorthEast(); break;
							case 2: org.moveEast(); break;
							case 3: org.moveSouthEast(); break;
							case 4: org.moveSouth(); break;
							case 5: org.moveSouthWest(); break;
							case 6: org.moveWest(); break;
							case 7: org.moveNorthWest(); break;
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
					for(Organism org: organisms) {
						org.getChromosome().getGene(Chromosome.MOVEFOOD).setSym('x', findClosestFood(org));
					}
					for(HealthyFood h: healthyFoodSources) {
						numSurrounding(h);
					}
					for(PoisonousFood p: poisonousFoodSources) {
						numSurrounding(p);
					}
					}
				else{
					g.setOrgList(organisms);
					organisms=g.newGeneration();
					initNextGeneration();
					System.out.println("after init");
					timePassed=0;
					repaint();
				}
			}

			//TODO: Can organism differentiate bw pois and non
			//pois?
			private double findClosestFood(Organism org) {
				for(PoisonousFood p: poisonousFoodSources) {
					p.getLocation().getX();
					p.getLocation().getY();
				}
				for(HealthyFood h: healthyFoodSources) {
					h.getLocation().getX();
					h.getLocation().getY();
				}
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