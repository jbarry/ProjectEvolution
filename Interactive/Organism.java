package Interactive;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import Frame.*;

public class Organism extends Matter{
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	private double hlthTot;
	private int samples;
	private int steps;
	private Chromosome chromosome;
	private double fitness;
	private int scanRange;
	private TreeSet<Integer> healthyFood;
	private TreeSet<Integer> poisonFood;
	private ArrayList<ArrayList<String>> ActionList;
	private ArrayList<Coordinate> StartingLocation;
	private ArrayList<Chromosome> chromosomeHistory;
	private int eatFail = 0;
	private int healthyEatSuccess = 0;
	private int poisonEatSuccess = 0;
	private int numScans = 0;
	private int numAttacked = 0;
	private int numPushed = 0;
	private String action = "";
	public static int width = 5;
	public static int height = 5;
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	
	/*
	public Organism() {
		super(7500.0);
		samples = 0;
		avgHealth = 0;
		hlthTot = 0;
		steps = 0;
		chromosome = new Chromosome(9);
		fitness = 0.0;
		ActionList = new ArrayList<ArrayList<String>>();
		ActionList.add(new ArrayList<String>());
		StartingLocation = new ArrayList<Coordinate>();
		chromosomeHistory = new ArrayList<Chromosome>();
	}
	*/
	
	public Organism(double aHealth, int chromSize, int anId) {
		super(aHealth, anId, 'o');
		chromosome = new Chromosome(chromSize);
		samples = 0;
		hlthTot = 0;
		//scanRange = aScanRange;
		steps = 0;
		fitness = 0.0;
		ActionList = new ArrayList<ArrayList<String>>();
		ActionList.add(new ArrayList<String>());
		StartingLocation=new ArrayList<Coordinate>();
		chromosomeHistory= new ArrayList<Chromosome>();
		healthyFood = new TreeSet<Integer>();
		poisonFood = new TreeSet<Integer>(); 
	}
	
	// For testing purposes only.
	// Just removing the GridPanel call.
	public Organism(boolean boo, int numGenes, double aFitness,
			int anId) {
		hlth = 100.00;
		r = new Random();
		chromosome = new Chromosome(numGenes, anId, true);
		fitness = aFitness;
		id = anId;
	}
	
	//This ctor is for testing purposes.
	public Organism(double ahealth, Chromosome aChromosome) {
		hlth = ahealth;
		chromosome = aChromosome;
	}

	public Organism(Coordinate aLocation, Chromosome aChromosome) {
		hlth = 7500.0;
		location = aLocation;
		chromosome = aChromosome;
	}

	
	public void newLocation() {
		setRange(width, height, 'w');
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);
		while(!canSpawn(x, y)){
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		location = new Coordinate(x, y);
		//set boundaries
		setWrapAround(width, height);
		setRange(width, height, 'o');
	}	
	
	public void eatFood(Food f, double fdVal){
		f.decreaseHealth(fdVal);
		if(f instanceof HealthyFood) {
			/*System.out.println("orgId: " + id);
			System.out.println("hlthy");
			System.out.println("orgHealth: " + hlth);
			System.out.println("FoodId: " + f.getId());
			System.out.println("OrgLoc: " + this.getLocation().getX() + ", " + this.getLocation().getY());
			System.out.println();*/
			incHlth(fdVal);
			setAction("Eating health food");
			healthyEatSuccess++;
			eatFail--;
			healthyFood.add(f.getId());
		}
		else if(f instanceof PoisonousFood){
			/*System.out.println("orgId: " + id);
			System.out.println("pois");
 		    System.out.println("orgHealth: " + hlth);
			System.out.println("FoodId: " + f.getId());
			System.out.println("OrgLoc: " + this.getLocation().getX() + ", " + this.getLocation().getY());
			System.out.println();*/
			poisonFood.add(f.getId());
			setAction("Eating poisonous food");
			poisonEatSuccess++;
			eatFail--;
			decreaseHealth(fdVal);
		}
	}
	
	/**
	 * @param scanRange
	 * @return
	 */
	public List<Integer> getFoodInRange(int scanRange) {
		Set<Integer> objectIds = new HashSet<Integer>();
		//create a square from cornerTop to cornerBottom of 
		//dimension scanRange+getWidth/2 X scanRange+getHeight/2 to be scanned.
		int widthSub = location.getX() - (getWidth()/2);
		int widthPlus = location.getX() + (getWidth()/2);
		int heightSub = location.getY() - (getHeight()/2);
		int heightPlus = location.getY() + (getHeight()/2);
		
		Coordinate cornerTop =
			new Coordinate(widthSub - scanRange, heightSub - scanRange);
		Coordinate cornerBottom =
			new Coordinate(widthPlus + scanRange, heightPlus + scanRange);
		
		for (int i = cornerTop.getX(); i <= cornerBottom.getX(); i++) {
			for (int j = cornerTop.getY(); j <= cornerBottom.getY(); j++) {
				try {
					// count all occurrences of objects in location map
					Pair<Integer, Character> space = GridPanel.locationMap[i][j];
					if (space.getSnd() == 'h' || space.getSnd() == 'p')
						objectIds.add(space.getFst());
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		/*Test prints
		System.out.println("Organism " + this.getId() + " is scanning from" + location.getX() + ", " + location.getY());
		System.out.println("The scan range is " + scanRange + " and the square is from " + cornerTop.getX() + ", " + cornerBottom.getY() 
				+ "to " + cornerBottom.getX() + ", " + cornerBottom.getY());*/
		return new ArrayList<Integer>(objectIds);
	}

	public boolean moveTo(int x, int y) {
		try {
			if (canSpawn(x, y)) { // If the next move is available.
				setRange(width, height, 'w');
				setWrapAround(width, height);
				location.setY(y);
				location.setX(x);
				// make current location unavailable
				setRange(width, height, 'o');
				return true;
			} else
				return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public void moveNorth(LinkedList<Organism> organisms, boolean wasPushed) {
		//make old location available.
		setRange(width, height, 'w');
		setWrapAround(width, height);
		
		//if the next move is available.
		try{
			if(canSpawn(location.getX(), location.getY() - 1)){
				//move there.
				location.setY(location.getY() - 1);
				if(!wasPushed)setAction("Traveling North");
			}
			else{
				if(!wasPushed)setAction("Attempting to travel North");
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		//make current location unavailable
		setRange(width, height, 'o');
	}

	public void moveNorthEast(LinkedList<Organism> organisms, boolean wasPushed) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() + 1, location.getY() - 1)){
				location.setX(location.getX() + 1);
				location.setY(location.getY() - 1);
				if(!wasPushed)setAction("Traveling Northeast");
			}
			else{
				if(!wasPushed)setAction("Attempting to travel Northeast");
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		
		}
		setRange(width, height, 'o');
	}

	public void moveEast(LinkedList<Organism> organisms, boolean wasPushed) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(location.getX() + 1 + width/2 >= GridPanel.WIDTH){
				if(canSpawn(width/2, location.getY())){
					location.setX(width/2);
				if(!wasPushed)setAction("Traveling East");
				}
			}
			if(canSpawn(location.getX() + 1, location.getY())){
				location.setX(location.getX() + 1);
				if(!wasPushed)setAction("Traveling East");
			}
			else{
				if(!wasPushed)setAction("Attempting to travel East");
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		
		}	
		setRange(width, height, 'o');
	}

	public void moveSouthEast(LinkedList<Organism> organisms, boolean wasPushed) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(location.getY() + 1 + height/2>= GridPanel.HEIGHT){
				if(canSpawn(location.getX(), height/2)){
					location.setY(height/2);
				if(!wasPushed)setAction("Traveling Southeast");
				}
			}
			if(canSpawn(location.getX() + 1, location.getY() + 1)){
				location.setX(location.getX() + 1);
				location.setY(location.getY() + 1);
				if(!wasPushed)setAction("Traveling Southeast");
			}
			else{
				if(!wasPushed)setAction("Attempting to travel Southeast");
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, 'o');
	}

	public void moveSouth(LinkedList<Organism> organisms, boolean wasPushed) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX(), location.getY() + 1)){
				location.setY(location.getY() + 1);
				if(!wasPushed)setAction("Traveling South");
			}
			else{
				if(!wasPushed)setAction("Attempting to travel South");
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, 'o');

	}

	public void moveSouthWest(LinkedList<Organism> organisms, boolean wasPushed) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() - 1, location.getY() + 1)){
				location.setX(location.getX() - 1);
				location.setY(location.getY() + 1);
				if(!wasPushed)setAction("Traveling Southwest");
				
			}
			else{
				if(!wasPushed)setAction("Attempting to travel Southwest");
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, 'o');
	}

	public void moveWest(LinkedList<Organism> organisms, boolean wasPushed) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() - 1, location.getY())){
				location.setX(location.getX() - 1);
				if(!wasPushed)setAction("Traveling West");
			}
			else{
				if(!wasPushed)setAction("Attempting to travel West");
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, 'o');
	}

	public void moveNorthWest(LinkedList<Organism> organisms, boolean wasPushed) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() - 1, location.getY() - 1)){
				location.setX(location.getX() - 1);
				location.setY(location.getY() - 1);
				if(!wasPushed)setAction("Traveling Northwest");
			}
			else{
				if(!wasPushed)setAction("Attempting to travel Northwest");
			}
		}
		catch(ArrayIndexOutOfBoundsException e){}
		setRange(width, height, 'o');
	}
	
		public void attack(int orgIndex, LinkedList<Organism> organisms){
			/*System.out.print("Attacking org " +
					orgIndex + "(" +
					organisms.get(orgIndex).getLocation().getX() +
					" " + organisms.get(orgIndex).getLocation().getY() +
					"). Health: " +
					organisms.get(orgIndex).getHealth());*/
			organisms.get(orgIndex).decreaseHealth(5);
			numAttacked++;
			organisms.get(this.getId()).setAction("Attacking org " + orgIndex);
			/*System.out.print(". Health: " + organisms.get(orgIndex).getHealth());
			System.out.println(". Attacked by org " + this.id);*/
	}
	
		public void pushOrg(int orgIndex, LinkedList<Organism> organisms){
			int xPushing = this.getLocation().getX();
			int yPushing = this.getLocation().getY();
			int xGettingPushed = organisms.get(orgIndex).getLocation().getX();
			int yGettingPushed = organisms.get(orgIndex).getLocation().getY();
			
			/*System.out.print("Pushing org " +
					orgIndex + "(" +
					organisms.get(orgIndex).getLocation().getX() +
					" " + organisms.get(orgIndex).getLocation().getY() + ")");*/
			
			if(xGettingPushed < xPushing){
				organisms.get(orgIndex).moveWest(organisms, true);
			}
			else if(xGettingPushed > xPushing){
				organisms.get(orgIndex).moveEast(organisms,true);
			}
			
			if(yGettingPushed < yPushing){
				organisms.get(orgIndex).moveNorth(organisms,true);
			}
			else if(yGettingPushed > yPushing){
				organisms.get(orgIndex).moveSouth(organisms,true);
			}
			numPushed++;
			organisms.get(this.getId()).setAction("Pushing org " + orgIndex);
			/*System.out.print("(" +
					organisms.get(orgIndex).getLocation().getX() +
					" " + organisms.get(orgIndex).getLocation().getY() + ")");
			System.out.println(". Pushed by org " +
					getId());*/
		
	}
	
		public boolean currOrgIsNextToSpecifiedOrg(int orgIndex){
		ArrayList<Integer> surroundingOrgs = new ArrayList<Integer>();
		surroundingOrgs = this.getSurroundingObjects('o', 1);
		boolean orgIsNextToOrg = false;
		for(Integer o: surroundingOrgs){
			if(orgIndex == o) orgIsNextToOrg = true;
		}
		
		return orgIsNextToOrg;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect((int)this.location.getX()-(width/2), 
				   (int)this.location.getY()-(height/2), 
				   width, height);
	}
	
	//------------------------------------------------------------------------------------
	//--overloaded functions--
	//------------------------------------------------------------------------------------

	/**
	 * @return a String representation of the Object.
	 */
	public String toString(){
		String str = "";
		str += " I am an Organism " + this.getId()+ ". Fear me."
			+  "\n Location: " + location
			+  "\n Health: " + hlth
			+  "\n Status: " + getAction();
		return str;
	}
	
	//------------------------------------------------------------------------------------
	//--getters/setters--
	//------------------------------------------------------------------------------------
	public String getAction(){
		return action;
	}
	
	public void setAction(String s){
		action += s + "\n               ";
	}
	
	public void clearAction(){
		action = "";
	}
	
	public Chromosome getChromosome() {
		return chromosome;
	}
	
	public void setChromosome(Chromosome aChrom){
		chromosome = aChrom;
	}
	
	public void incHlthTot() {
		hlthTot+=hlth;
		samples++;
	}
	
	public void countStep() {
		steps++;
	}
	
	public void addAction(String action,int index){
		ActionList.get(ActionList.size()-1).add(action + " " + index);
	}
	
	public void addGeneration(){
		ActionList.add(new ArrayList<String>());
		addStartingLocation();
		addChromosome();
	}
	
	public void addStartingLocation(){
		StartingLocation.add(getLocation());
	}
	
	public void addChromosome(){
		chromosomeHistory.add(chromosome);
	}
	
	/*public ArrayList<String> getActions(int generation){
		return ActionList.get(generation);
	}
	
	public int getHealthyFoodSize(){
		return healthyFood.size();
	}
	
	public int getPoisonFoodSize(){
		return poisonFood.size();
	}*/

	public void addEatFail(){
		eatFail++;
	}

	/*public ArrayList<String> getActions(int generation){
		return ActionList.get(generation);
	}

	public int getHealthyFoodSize(){
		return healthyFood.size();
	}

	public int getPoisonFoodSize(){
		return poisonFood.size();
	}*/

	public void addScan(int scans){
		numScans += scans;
	}

	public void clear(){
		eatFail = 0;
		numScans = 0;
		steps = 0;
		samples = 0;
		hlthTot = 0;
		poisonFood.clear();
		healthyFood.clear();
		poisonEatSuccess = 0;
		healthyEatSuccess = 0;
		numAttacked=0;
		numPushed=0;
		
	}
	
	public void goBack(int generation){
		newLocation();
		chromosome = chromosomeHistory.get(generation-1);
		for(int i=generation;i<chromosomeHistory.size();i++){
			chromosomeHistory.remove(i);
		}
		clear();
	}

	@Override
	public int getHeight() {
		return height;
	}
	@Override
	public int getWidth(){
		return width;
	}

	/*
	public ArrayList<String> getActions(int generation){
		return ActionList.get(generation);
	}

	public int getHealthyFoodSize(){
		return healthyFood.size();
	}

	public int getPoisonFoodSize(){
		return poisonFood.size();
	}
	*/
	
	public double getFitness() {
		return fitness;
	}

	public void setFitness(double aFit) {
		fitness = aFit;
	}
	
	
	public double getHlthTot() {
		return hlthTot;
	}

	public int getNumSteps() {
		return steps;
	}

	public int getSamples() {
		return samples;
	}

	public int getEatFail(){
		return eatFail;
	}

	public int getHealthEat(){
		return healthyEatSuccess;
	}

	public int getPoisonEat(){
		return poisonEatSuccess;
	}

	public int getTotalScans() {
		return numScans;
	}

	public int getNumPushed() {
		return numPushed;
	}

	public int getNumAttacked() {
		return numAttacked;
	}

	@Override
	public int compareTo(Matter o) {
		Organism org = (Organism)o;
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
		
		numAttacked++;
		anOrgList.get(this.getId()).setAction("Attacking org " + orgIndex);
		/*
		 * System.out.print(". Health: " + organisms.get(orgIndex).getHealth());
		 * System.out.println(". Attacked by org " + this.id);
		 */
	}
	
	public void printInfo() {
		System.out.println("Organism: ");
		System.out.println("Id: " + id);
		System.out.println("Health: " + hlth);
		System.out.println("Position: (" + location.getX() + ", "
				+ location.getY() + ")");
		System.out.println();
	}
}
