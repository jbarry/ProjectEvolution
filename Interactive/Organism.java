package Interactive;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import Frame.Coordinate;
import Frame.GridPanel;
import Frame.LocationMap;

public class Organism extends Matter implements Cloneable {
	
	// ---------------------
	// Builder for Organism
	// ---------------------
	
	public static OrganismBuilder organismBuilder() {
		return new OrganismBuilder();
	}
	
	public Organism(OrganismBuilder organismBuilder) {
		width = 5;
		height = 5;
		action = organismBuilder.action;
		fitness = organismBuilder.fitness;
		orgData = organismBuilder.orgData;
		numActions = organismBuilder.numActions;
		health = organismBuilder.health;
		fitness = organismBuilder.fitness;
		chromosome = new Chromosome(organismBuilder.numberOfGenes);
		type = 'o';
		matterId = organismBuilder.matterId;
		location = new Coordinate();
		gridPanel = organismBuilder.gridPanel; 
	}
	
	public boolean eatFood(double val) {
		health += val;
		if (health <= 0) {
			health = 0;
			return true;
		} else if (health > mxHlth)
			health = mxHlth;
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
	 * Returns a boolean indicating whether or not the specified Matter id with
	 * type is in the scanRange of the organism.
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
		LocationMap.getInstance().newLocation(location, width, height, matterId, 'o');
	}

	public void moveTo(Coordinate c) {
		LocationMap map = LocationMap.getInstance();
		// Set current location available.
		map.setRangeToBlank(width, height, location.getX(), location.getY());
		location.setX(c.getX());
		location.setY(c.getY());
		map.setWrapAround(location, width, height);
		// Make current location unavailable.
		map.setRange(location, width, height, 'o', matterId);
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

		organisms.get(orgIndex).decreaseHealth(5);
		organisms.get(this.getMatterID()).setAction("Attacking org " + orgIndex);
		/*
		 * System.out.print(". Health: " + organisms.get(orgIndex).getHealth());
		 * System.out.println(". Attacked by org " + this.id);
		 */
	}

	/*
	 * public void pushOrg(int orgIndex, LinkedList<Organism> organisms) {
	 * currentAction = 'p'; int xPushing = this.getLocation().getX(); int
	 * yPushing = this.getLocation().getY(); int xGettingPushed =
	 * organisms.get(orgIndex).getLocation().getX(); int yGettingPushed =
	 * organisms.get(orgIndex).getLocation().getY();
	 * 
	 * System.out.print("Pushing org " + orgIndex + "(" +
	 * organisms.get(orgIndex).getLocation().getX() + " " +
	 * organisms.get(orgIndex).getLocation().getY() + ")");
	 * 
	 * if (xGettingPushed < xPushing) {
	 * organisms.get(orgIndex).moveWest(organisms, true); } else if
	 * (xGettingPushed > xPushing) { organisms.get(orgIndex).moveEast(organisms,
	 * true); }
	 * 
	 * if (yGettingPushed < yPushing) {
	 * organisms.get(orgIndex).moveNorth(organisms, true); } else if
	 * (yGettingPushed > yPushing) {
	 * organisms.get(orgIndex).moveSouth(organisms, true); } numPushed++;
	 * organisms.get(this.getId()).setAction("Pushing org " + orgIndex);
	 * System.out.print("(" + organisms.get(orgIndex).getLocation().getX() + " "
	 * + organisms.get(orgIndex).getLocation().getY() + ")");
	 * System.out.println(". Pushed by org " + getId());
	 * 
	 * }
	 */

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
		str += " I am an Organism " + this.getMatterID() + ". Fear me."
				+ "\n Location: " + location + "\n Health: " + health
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
		/*
		 * System.out.print("Attacking org " + orgIndex + "(" +
		 * organisms.get(orgIndex).getLocation().getX() + " " +
		 * organisms.get(orgIndex).getLocation().getY() + "). Health: " +
		 * organisms.get(orgIndex).getHealth());
		 */
		Organism org = anOrgList.get(orgIndex);
		org.decreaseHealth(5);

		/* numAttacked++; */
		anOrgList.get(this.getMatterID()).setAction("Attacking org " + orgIndex);
		/*
		 * System.out.print(". Health: " + organisms.get(orgIndex).getHealth());
		 * System.out.println(". Attacked by org " + this.id);
		 */
	}

	public void printInfo() {
		System.out.println("Organism: ");
		System.out.println("Id: " + matterId);
		System.out.println("Health: " + health);
		System.out.println("Position: (" + location.getX() + ", "
				+ location.getY() + ")");
		System.out.println();
	}

	public void printId() {
		System.out.println("Id: " + matterId);
		System.out.println();
	}

	@Override
	public char getType() {
		return 'o';
	}

	@Override
	public int getMatterID() {
		return matterId;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		/*
		 * if (location == null)
		 * System.out.println("loc at paint time is null"); else
		 * System.out.println("org location at paint: " + location.getX() + ", "
		 * + location.getY());
		 */
		g.fillRect((int) this.location.getX() - (width / 2),
				(int) this.location.getY() - (height / 2), width, height);
	}

	@Override
	public void run() {
		gridPanel.simulateStepAstarClosedListOrgDataLoopGenes(1, 40, this);
		
	}
	
	public Chromosome chromosome;
	public static int width = 5;
	public static int height = 5;
	public String action;
	public double fitness;
	public OrgData orgData;
	public int numActions;
	public GridPanel gridPanel;
}