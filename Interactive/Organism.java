package Interactive;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;

import Frame.Coordinate;
import Frame.LocationMap;

public class Organism extends Matter implements Cloneable{

	// ------------------------------------------------------------------------------------
	// --globals--
	// ------------------------------------------------------------------------------------
	private Chromosome chromosome;
	private int scanRange;
	public static int width = 5;
	public static int height = 5;
	private String action;
	private double fitness;
	// For images/actions
	private Image ninja_walk1;
	private Image ninja_walk1_inv;
	private Image ninja_walk2;
	private Image ninja_walk2_inv;
	private Image ninja_eat;
	private Image ninja_eat_inv;
	private Image ninja_attack;
	private Image ninja_attack_inv;
	private Image ninja_push;
	private Image ninja_push_inv;
	private Image ninja_dead;
	private Image ninja_dead_inv;
	private boolean swapImage;
	private boolean facingRight;
	private char currentAction;

	// ------------------------------------------------------------------------------------
	// --constructors--
	// ------------------------------------------------------------------------------------
	public Organism(double aHealth, int chromSize, int anId, int aScanRange) {
		super(aHealth, anId, 'o');
		chromosome = new Chromosome(chromSize);
		fitness = 0.0;
		/*scanRange = aScanRange;*/
		// Image initialization.
		ninja_walk1 = new ImageIcon(getClass().getResource(
				"sprites/ninja_walk1.gif")).getImage();
		ninja_walk1_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_walk1_inv.gif")).getImage();
		ninja_walk2 = new ImageIcon(getClass().getResource(
				"sprites/ninja_walk2.gif")).getImage();
		ninja_walk2_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_walk2_inv.gif")).getImage();
		ninja_eat = new ImageIcon(getClass().getResource(
				"sprites/ninja_eat.gif")).getImage();
		ninja_eat_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_eat_inv.gif")).getImage();
		ninja_attack = new ImageIcon(getClass().getResource(
				"sprites/ninja_attack.gif")).getImage();
		ninja_attack_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_attack_inv.gif")).getImage();
		ninja_push = new ImageIcon(getClass().getResource(
				"sprites/ninja_push.gif")).getImage();
		ninja_push_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_push_inv.gif")).getImage();
		ninja_dead = new ImageIcon(getClass().getResource(
				"sprites/ninja_dead.gif")).getImage();
		ninja_dead_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_dead_inv.gif")).getImage();
		// create behavior tracking boolean variables
		swapImage = true;
		facingRight = true;
		currentAction = ' ';
	}

	public Organism(double aHealth, int chromSize, int anId) {
		super(aHealth, anId, 'o');
		chromosome = new Chromosome(chromSize);
		/*scanRange = aScanRange;*/
		// Image initialization.
		ninja_walk1 = new ImageIcon(getClass().getResource(
				"sprites/ninja_walk1.gif")).getImage();
		ninja_walk1_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_walk1_inv.gif")).getImage();
		ninja_walk2 = new ImageIcon(getClass().getResource(
				"sprites/ninja_walk2.gif")).getImage();
		ninja_walk2_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_walk2_inv.gif")).getImage();
		ninja_eat = new ImageIcon(getClass().getResource(
				"sprites/ninja_eat.gif")).getImage();
		ninja_eat_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_eat_inv.gif")).getImage();
		ninja_attack = new ImageIcon(getClass().getResource(
				"sprites/ninja_attack.gif")).getImage();
		ninja_attack_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_attack_inv.gif")).getImage();
		ninja_push = new ImageIcon(getClass().getResource(
				"sprites/ninja_push.gif")).getImage();
		ninja_push_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_push_inv.gif")).getImage();
		ninja_dead = new ImageIcon(getClass().getResource(
				"sprites/ninja_dead.gif")).getImage();
		ninja_dead_inv = new ImageIcon(getClass().getResource(
				"sprites/ninja_dead_inv.gif")).getImage();
		// create behavior tracking boolean variables
		swapImage = true;
		facingRight = true;
		currentAction = ' ';
	}

	// FOR TESTING PURPOSES ONLY.
	// JUST REMOVING THE GRIDPANEL CALL.
	public Organism(boolean boo, int numGenes, double aFitness, int anId) {
		super(100.00, anId, 'o');
		r = new Random();
		chromosome = new Chromosome(numGenes, anId, true);
		fitness = aFitness;
		id = anId;
	}

	// THIS CTOR IS FOR TESTING PURPOSES.
	public Organism(double ahealth, Chromosome aChromosome) {
		hlth = ahealth;
		chromosome = aChromosome;
		fitness = 0.0;
	}

	public Organism(Coordinate aLocation, Chromosome aChromosome) {
		hlth = 7500.0;
		location = aLocation;
		chromosome = aChromosome;
		fitness = 0.0;
	}

	public boolean eatFood(double val) {
		currentAction = 'e';
		hlth += val;
		if (hlth <= 0) {
			hlth = 0;
			return true;
		} else if (hlth > mxHlth)
			hlth = mxHlth;
		return false;
	}

	/**
	 * @param scanRange
	 * @return
	 */
	public List<Integer> getFoodInRange(int scanRange) {
		Set<Integer> objectIds = new HashSet<Integer>();
		// create a square from cornerTop to cornerBottom of
		// dimension scanRange+getWidth/2 X scanRange+getHeight/2 to be
		// scanned.
		int widthSub = location.getX() - (getWidth() / 2);
		int widthPlus = location.getX() + (getWidth() / 2);
		int heightSub = location.getY() - (getHeight() / 2);
		int heightPlus = location.getY() + (getHeight() / 2);
		// cornerTop and cornerBottom will be values for the loop
		// conditional.
		Coordinate cornerTop = new Coordinate(widthSub - scanRange, heightSub
				- scanRange);
		Coordinate cornerBottom = new Coordinate(widthPlus + scanRange,
				heightPlus + scanRange);
		// Instance of locationMap.
		LocationMap locationMap = LocationMap.getInstance();
		for (int i = cornerTop.getX(); i <= cornerBottom.getX(); i++) {
			for (int j = cornerTop.getY(); j <= cornerBottom.getY(); j++) {
				try {
					// Count all occurrences of objects in location
					// map
					Pair<Integer, Character> space = locationMap.get(i, j);
					if (space.getRight() == 'h' || space.getRight() == 'p')
						objectIds.add(space.getLeft());
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		return new ArrayList<Integer>(objectIds);
	}

	/**
	 * Returns a boolean indicating whether or not the specified Matter id with type is in
	 * the scanRange of the organism.
	 * 
	 * @param scanRange
	 * @return
	 */
	public boolean matterInRange(int anId, Character type, int scanRange) {
		int widthSub = location.getX() - (getWidth() / 2);
		int widthPlus = location.getX() + (getWidth() / 2);
		int heightSub = location.getY() - (getHeight() / 2);
		int heightPlus = location.getY() + (getHeight() / 2);
		// cornerTop and cornerBottom will be values for the loop
		// conditional.
		Coordinate cornerTop = new Coordinate(widthSub - scanRange, heightSub
				- scanRange);
		Coordinate cornerBottom = new Coordinate(widthPlus + scanRange,
				heightPlus + scanRange);
		// Instance of locationMap.
		LocationMap locationMap = LocationMap.getInstance();
		for (int i = cornerTop.getX(); i <= cornerBottom.getX(); i++) {
			for (int j = cornerTop.getY(); j <= cornerBottom.getY(); j++) {
				try {
					// Count all occurrences of objects in location map.
					Pair<Integer, Character> space = locationMap.get(i, j);
					if (space.getRight() == type && space.getLeft() == anId)
						return true;
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		return false;
	}

	public void newLocation() {
		LocationMap.getInstance().newLocation(location, width, height, id, 'o');
	}

	public void moveTo(Coordinate c) {
		LocationMap map = LocationMap.getInstance();
		// Set current location available.
		map.setRangeToBlank(width, height, location.getX(), location.getY());
		location.setX(c.getX());
		location.setY(c.getY());
		map.setWrapAround(location, width, height);
		// Make current location unavailable.
		map.setRange(location, width, height, 'o', id);
	}

	/*
	 * public void moveNorthEast(LinkedList<Organism> organisms, boolean
	 * wasPushed) { setRange(width, height, 'w'); setWrapAround(width, height);
	 * try{ if(canSpawn(location.getX() + 1, location.getY() - 1)){
	 * location.setX(location.getX() + 1); location.setY(location.getY() - 1);
	 * if(!wasPushed)setAction("Traveling Northeast"); } else{
	 * if(!wasPushed)setAction("Attempting to travel Northeast"); } }
	 * catch(ArrayIndexOutOfBoundsException e){
	 * 
	 * } setRange(width, height, 'o'); }
	 * 
	 * public void moveEast(LinkedList<Organism> organisms, boolean wasPushed) {
	 * setRange(width, height, 'w'); setWrapAround(width, height); try{
	 * if(location.getX() + 1 + width/2 >= GridPanel.WIDTH){
	 * if(canSpawn(width/2, location.getY())){ location.setX(width/2);
	 * if(!wasPushed)setAction("Traveling East"); } }
	 * if(canSpawn(location.getX() + 1, location.getY())){
	 * location.setX(location.getX() + 1);
	 * if(!wasPushed)setAction("Traveling East"); } else{
	 * if(!wasPushed)setAction("Attempting to travel East"); } }
	 * catch(ArrayIndexOutOfBoundsException e){
	 * 
	 * } setRange(width, height, 'o'); }
	 * 
	 * public void moveSouthEast(LinkedList<Organism> organisms, boolean
	 * wasPushed) { setRange(width, height, 'w'); setWrapAround(width, height);
	 * try{ if(location.getY() + 1 + height/2>= GridPanel.HEIGHT){
	 * if(canSpawn(location.getX(), height/2)){ location.setY(height/2);
	 * if(!wasPushed)setAction("Traveling Southeast"); } }
	 * if(canSpawn(location.getX() + 1, location.getY() + 1)){
	 * location.setX(location.getX() + 1); location.setY(location.getY() + 1);
	 * if(!wasPushed)setAction("Traveling Southeast"); } else{
	 * if(!wasPushed)setAction("Attempting to travel Southeast"); } }
	 * catch(ArrayIndexOutOfBoundsException e){
	 * 
	 * } setRange(width, height, 'o'); }
	 * 
	 * public void moveSouth(LinkedList<Organism> organisms, boolean wasPushed)
	 * { setRange(width, height, 'w'); setWrapAround(width, height); try{
	 * if(canSpawn(location.getX(), location.getY() + 1)){
	 * location.setY(location.getY() + 1);
	 * if(!wasPushed)setAction("Traveling South"); } else{
	 * if(!wasPushed)setAction("Attempting to travel South"); } }
	 * catch(ArrayIndexOutOfBoundsException e){
	 * 
	 * } setRange(width, height, 'o');
	 * 
	 * }
	 * 
	 * public void moveSouthWest(LinkedList<Organism> organisms, boolean
	 * wasPushed) { setRange(width, height, 'w'); setWrapAround(width, height);
	 * try{ if(canSpawn(location.getX() - 1, location.getY() + 1)){
	 * location.setX(location.getX() - 1); location.setY(location.getY() + 1);
	 * if(!wasPushed)setAction("Traveling Southwest");
	 * 
	 * } else{ if(!wasPushed)setAction("Attempting to travel Southwest"); } }
	 * catch(ArrayIndexOutOfBoundsException e){
	 * 
	 * } setRange(width, height, 'o'); }
	 * 
	 * public void moveWest(LinkedList<Organism> organisms, boolean wasPushed) {
	 * setRange(width, height, 'w'); setWrapAround(width, height); try{
	 * if(canSpawn(location.getX() - 1, location.getY())){
	 * location.setX(location.getX() - 1);
	 * if(!wasPushed)setAction("Traveling West"); } else{
	 * if(!wasPushed)setAction("Attempting to travel West"); } }
	 * catch(ArrayIndexOutOfBoundsException e){
	 * 
	 * } setRange(width, height, 'o'); }
	 * 
	 * public void moveNorthWest(LinkedList<Organism> organisms, boolean
	 * wasPushed) { setRange(width, height, 'w'); setWrapAround(width, height);
	 * try{ if(canSpawn(location.getX() - 1, location.getY() - 1)){
	 * location.setX(location.getX() - 1); location.setY(location.getY() - 1);
	 * if(!wasPushed)setAction("Traveling Northwest"); } else{
	 * if(!wasPushed)setAction("Attempting to travel Northwest"); } }
	 * catch(ArrayIndexOutOfBoundsException e){} setRange(width, height, 'o'); }
	 */

	public void attack(int orgIndex, LinkedList<Organism> organisms) {
		/*
		 * System.out.print("Attacking org " + orgIndex + "(" +
		 * organisms.get(orgIndex).getLocation().getX() + " " +
		 * organisms.get(orgIndex).getLocation().getY() + "). Health: " +
		 * organisms.get(orgIndex).getHealth());
		 */
		currentAction = 'a';

		organisms.get(orgIndex).decreaseHealth(5);
		organisms.get(this.getId()).setAction("Attacking org " + orgIndex);
		/*
		 * System.out.print(". Health: " + organisms.get(orgIndex).getHealth());
		 * System.out.println(". Attacked by org " + this.id);
		 */
	}

	/*public void pushOrg(int orgIndex, LinkedList<Organism> organisms) {
	  currentAction = 'p';
	  int xPushing = this.getLocation().getX();
	  int yPushing = this.getLocation().getY();
	  int xGettingPushed = organisms.get(orgIndex).getLocation().getX();
	  int yGettingPushed = organisms.get(orgIndex).getLocation().getY();

	  System.out.print("Pushing org " + orgIndex + "("
			    + organisms.get(orgIndex).getLocation().getX() + " "
			    + organisms.get(orgIndex).getLocation().getY() + ")");

	  if (xGettingPushed < xPushing) {
		   organisms.get(orgIndex).moveWest(organisms, true);
	  } else if (xGettingPushed > xPushing) {
		   organisms.get(orgIndex).moveEast(organisms, true);
	  }

	  if (yGettingPushed < yPushing) {
		   organisms.get(orgIndex).moveNorth(organisms, true);
	  } else if (yGettingPushed > yPushing) {
		   organisms.get(orgIndex).moveSouth(organisms, true);
	  }
	  numPushed++;
	  organisms.get(this.getId()).setAction("Pushing org " + orgIndex);
	  System.out.print("(" + organisms.get(orgIndex).getLocation().getX()
			    + " " + organisms.get(orgIndex).getLocation().getY()
			    + ")");
	  System.out.println(". Pushed by org " + getId());

	}*/

	public boolean currOrgIsNextToSpecifiedOrg(int orgIndex) {
		ArrayList<Integer> surroundingOrgs = new ArrayList<Integer>();
		surroundingOrgs = this.getSurroundingObjects('o', 1);
		boolean orgIsNextToOrg = false;
		for (Integer o : surroundingOrgs) {
			if (orgIndex == o)
				orgIsNextToOrg = true;
		}

		return orgIsNextToOrg;
	}

	// ------------------------------------------------------------------------------------
	// --overloaded functions--
	// ------------------------------------------------------------------------------------

	/**
	 * @return a String representation of the Object.
	 */
	public String toString() {
		String str = "";
		str += " I am an Organism " + this.getId() + ". Fear me."
				+ "\n Location: " + location + "\n Health: " + hlth
				+ "\n Status: " + getAction();
		return str;
	}

	// ------------------------------------------------------------------------------------
	// --getters/setters--
	// ------------------------------------------------------------------------------------
	public String getAction() {
		return action;
	}

	public void setAction(String s) {
		action += s + "\n               ";
	}

	public void clearAction() {
		action = "";
	}

	public Chromosome getChromosome() {
		return chromosome;
	}

	public void setChromosome(Chromosome aChrom) {
		chromosome = aChrom;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int compareTo(Matter o) {
		Organism org = (Organism) o;
		if (fitness < org.getFitness())
			return -1;
		else if (fitness == org.getFitness())
			return 0;
		return 1;
	}

	/**
	 * Takes an organism index and a list of Organisms and decreases the health
	 * of the organism that is attacked.
	 * 
	 * @param orgIndex
	 * @param anOrgList
	 */
	public void attack2(int orgIndex, ArrayList<Organism> anOrgList) {
		/*System.out.print("Attacking org " + orgIndex + "("
				+ organisms.get(orgIndex).getLocation().getX() + " "
				+ organisms.get(orgIndex).getLocation().getY() + "). Health: "
				+ organisms.get(orgIndex).getHealth());*/
		Organism org = anOrgList.get(orgIndex);
		org.decreaseHealth(5);

		/*numAttacked++;*/
		anOrgList.get(this.getId()).setAction("Attacking org " + orgIndex);
		/*System.out.print(". Health: " + organisms.get(orgIndex).getHealth());
		System.out.println(". Attacked by org " + this.id);*/
	}

	public void printInfo() {
		System.out.println("Organism: ");
		System.out.println("Id: " + id);
		System.out.println("Health: " + hlth);
		System.out.println("Position: (" + location.getX() + ", "
				+ location.getY() + ")");
		System.out.println();
	}

	public void printId() {
		System.out.println("Id: " + id);
		System.out.println();
	}

	@Override
	public char getType() {
		return 'o';
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Organism org = (Organism) super.clone();
		org.chromosome = (Chromosome) org.chromosome.clone();
		return super.clone();
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		/*if (location == null)
		System.out.println("loc at paint time is null");
		else
		System.out.println("org location at paint: " + location.getX()
		+ ", " + location.getY());*/
		g.fillRect((int) this.location.getX() - (width / 2),
				(int) this.location.getY() - (height / 2), width, height);
	}

	/*public void paint(Graphics g) {
	  if (facingRight) {
		   if (getHealth() <= 0) {
			    g.drawImage(ninja_dead, location.getX() - 4
						 * Organism.width / 2, location.getY() - 4
						 * Organism.height / 2, 4 * Organism.width,
						  4 * Organism.height, null);
		   } else {
			    if (currentAction == 'e') {
					g.drawImage(ninja_eat, location.getX() - 4
							  * Organism.width / 2, location.getY()
							  - 4 * Organism.height / 2,
							  4 * Organism.width,
							  4 * Organism.height, null);
			    } else if (currentAction == 'a') {
					g.drawImage(ninja_attack, location.getX() - 4
							  * Organism.width / 2, location.getY()
							  - 4 * Organism.height / 2,
							  4 * Organism.width,
							  4 * Organism.height, null);
			    } else if (currentAction == 'p') {
					g.drawImage(ninja_push, location.getX() - 4
							  * Organism.width / 2, location.getY()
							  - 4 * Organism.height / 2,
							  4 * Organism.width,
							  4 * Organism.height, null);
			    } else {
					if (swapImage) {
						 g.drawImage(ninja_walk1, location.getX()
								   - 4 * Organism.width / 2,
								   location.getY() - 4
											* Organism.height
											/ 2,
								   4 * Organism.width,
								   4 * Organism.height, null);
					} else {
						 g.drawImage(ninja_walk2, location.getX()
								   - 4 * Organism.width / 2,
								   location.getY() - 4
											* Organism.height
											/ 2,
								   4 * Organism.width,
								   4 * Organism.height, null);
					}
			    }
		   }
	  } else {
		   if (getHealth() <= 0) {
			    g.drawImage(ninja_dead_inv, location.getX() - 4
						 * Organism.width / 2, location.getY() - 4
						 * Organism.height / 2, 4 * Organism.width,
						 4 * Organism.height, null);
		   } else {
			    if (currentAction == 'e') {
					g.drawImage(ninja_eat_inv, location.getX() - 4
							  * Organism.width / 2, location.getY()
							  - 4 * Organism.height / 2,
							  4 * Organism.width,
							  4 * Organism.height, null);
			    } else if (currentAction == 'a') {
					g.drawImage(ninja_attack_inv, location.getX() - 4
							  * Organism.width / 2, location.getY()
							  - 4 * Organism.height / 2,
							  4 * Organism.width,
							  4 * Organism.height, null);
			    } else if (currentAction == 'p') {
					g.drawImage(ninja_push_inv, location.getX() - 4
							  * Organism.width / 2, location.getY()
							  - 4 * Organism.height / 2,
							  4 * Organism.width,
							  4 * Organism.height, null);
			    } else {
					if (swapImage) {
						 g.drawImage(ninja_walk1_inv,
								   location.getX() - 4
											* Organism.width
											/ 2,
								   location.getY() - 4
											* Organism.height
											/ 2,
								   4 * Organism.width,
								   4 * Organism.height, null);
					} else {
						 g.drawImage(ninja_walk2_inv,
								   location.getX() - 4
											* Organism.width
											/ 2,
								   location.getY() - 4
											* Organism.height
											/ 2,
								   4 * Organism.width,
								   4 * Organism.height, null);
					}
			    }
		   }
	  }
	  swapImage = !swapImage;
	}*/

	/*public void addAction(String action, int index) {
	actionList.get(actionList.size() - 1).add(action + " " + index);
	}*/

	/*public ArrayList<String> getActions(int generation) {
		return ActionList.get(generation);
	}*/

	/*public int getHealthyFoodSize() {
		return healthyFood.size();
	}*/

	/*public int getPoisonFoodSize() {
		return poisonFood.size();
	}*/

	/*public void addEatFail() {
		eatFail++;
	}*/
}
