package Interactive;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
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
	private double avgHealth;
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
	private int eatFail=0;
	private int healthyEatSuccess=0;
	private int poisonEatSuccess=0;
	private int numScans=0;
	private int numAttacked=0;
	private int numPushed=0;
	public static int width = 5;
	public static int height = 5;
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
//	public Organism() {
//		super(7500.0);
//		samples = 0;
//		avgHealth = 0;
//		hlthTot = 0;
//		steps = 0;
//		chromosome = new Chromosome(9);
//		fitness = 0.0;
//		ActionList = new ArrayList<ArrayList<String>>();
//		ActionList.add(new ArrayList<String>());
//		StartingLocation = new ArrayList<Coordinate>();
//		chromosomeHistory = new ArrayList<Chromosome>();
//	}

	public Organism(double aHealth, int chromSize, int anId, int aScanRange) {
		super(aHealth, anId, 'o');
		chromosome = new Chromosome(chromSize);
		samples = 0;
		avgHealth = 0;
		hlthTot = 0;
		scanRange = aScanRange;
		steps = 0;
		fitness = 0.0;
		ActionList= new ArrayList<ArrayList<String>>();
		ActionList.add(new ArrayList<String>());
		StartingLocation=new ArrayList<Coordinate>();
		chromosomeHistory= new ArrayList<Chromosome>();
		healthyFood = new TreeSet<Integer>();
		poisonFood = new TreeSet<Integer>(); 
	}
	
	//for testing purposes only.
	//just removing the GridPanel call.
	public Organism(boolean boo, int aChromSize) {
		hlth = 7500.00;
		r = new Random();
		chromosome = new Chromosome(aChromSize);
		fitness = 0.0;
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
		f.deplete(fdVal);
		if(f instanceof HealthyFood) {
//			System.out.println("orgId: " + id);
//			System.out.println("hlthy");
//			System.out.println("orgHealth: " + hlth);
//			System.out.println("FoodId: " + f.getId());
//			System.out.println("OrgLoc: " + this.getLocation().getX() + ", " + this.getLocation().getY());
//			System.out.println();
			incHlth(fdVal);
			healthyEatSuccess++;
			eatFail--;
			healthyFood.add(f.getId());
		}
		else if(f instanceof PoisonousFood){
//			System.out.println("orgId: " + id);
//			System.out.println("pois");
// 		    System.out.println("orgHealth: " + hlth);
//			System.out.println("FoodId: " + f.getId());
//			System.out.println("OrgLoc: " + this.getLocation().getX() + ", " + this.getLocation().getY());
//			System.out.println();
			poisonFood.add(f.getId());
			poisonEatSuccess++;
			eatFail--;
			deplete(fdVal);
		}
	}
	
	public void moveNorth(LinkedList<Organism> organisms) {
		//make old location available.
		setRange(width, height, 'w');
		setWrapAround(width, height);
		
		//if the next move is available.
		try{
			if(canSpawn(location.getX(), location.getY() - 1)){
				//move there.
				location.setY(location.getY() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		//make current location unavailable
		setRange(width, height, 'o');
	}

	public void moveNorthEast(LinkedList<Organism> organisms) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() + 1, location.getY() - 1)){
				location.setX(location.getX() + 1);
				location.setY(location.getY() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		
		}
		setRange(width, height, 'o');
	}

	public void moveEast(LinkedList<Organism> organisms) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(location.getX() + 1 + width/2 >= GridPanel.WIDTH){
				if(canSpawn(width/2, location.getY()))
					location.setX(width/2);
			}
			if(canSpawn(location.getX() + 1, location.getY())){
				location.setX(location.getX() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		
		}	
		setRange(width, height, 'o');
	}

	public void moveSouthEast(LinkedList<Organism> organisms) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(location.getY() + 1 + height/2>= GridPanel.HEIGHT){
				if(canSpawn(location.getX(), height/2))
					location.setY(height/2);
			}
			if(canSpawn(location.getX() + 1, location.getY() + 1)){
				location.setX(location.getX() + 1);
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, 'o');
	}

	public void moveSouth(LinkedList<Organism> organisms) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX(), location.getY() + 1)){
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, 'o');

	}

	public void moveSouthWest(LinkedList<Organism> organisms) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() - 1, location.getY() + 1)){
				location.setX(location.getX() - 1);
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, 'o');
	}

	public void moveWest(LinkedList<Organism> organisms) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() - 1, location.getY())){
				location.setX(location.getX() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, 'o');
	}

	public void moveNorthWest(LinkedList<Organism> organisms) {
		setRange(width, height, 'w');
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() - 1, location.getY() - 1)){
				location.setX(location.getX() - 1);
				location.setY(location.getY() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){}
		setRange(width, height, 'o');
	}
	
		public void attack(int orgIndex, LinkedList<Organism> organisms){
//			System.out.print("Attacking org " + orgIndex + "(" + organisms.get(orgIndex).getLocation().getX() + " " + organisms.get(orgIndex).getLocation().getY() + "). Health: " + organisms.get(orgIndex).getHealth());
			organisms.get(orgIndex).deplete(5);
			numAttacked++;
//			System.out.print(". Health: " + organisms.get(orgIndex).getHealth());
//			System.out.println(". Attacked by org " + this.id);
	}
	
		public void pushOrg(int orgIndex, LinkedList<Organism> organisms){
			int xPushing = this.getLocation().getX();
			int yPushing = this.getLocation().getY();
			int xGettingPushed = organisms.get(orgIndex).getLocation().getX();
			int yGettingPushed = organisms.get(orgIndex).getLocation().getY();
			
//			System.out.print("Pushing org " + orgIndex + "(" + organisms.get(orgIndex).getLocation().getX() + " " + organisms.get(orgIndex).getLocation().getY() + ")");
			
			if(xGettingPushed < xPushing){
				organisms.get(orgIndex).moveWest(organisms);
			}
			else if(xGettingPushed > xPushing){
				organisms.get(orgIndex).moveEast(organisms);
			}
			
			if(yGettingPushed < yPushing){
				organisms.get(orgIndex).moveNorth(organisms);
			}
			else if(yGettingPushed > yPushing){
				organisms.get(orgIndex).moveSouth(organisms);
			}
			numPushed++;
//			System.out.print("(" + organisms.get(orgIndex).getLocation().getX() + " " + organisms.get(orgIndex).getLocation().getY() + ")");
//			System.out.println(". Pushed by org " + getId());
		
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
		str += " I am an Organism. Fear me."
			+  "\n Location: " + location
			+  "\n Health: " + hlth
			+  "\n ID: " + this.getId();
		return str;
	}
	
	//------------------------------------------------------------------------------------
	//--getters/setters--
	//------------------------------------------------------------------------------------
	
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
	
	public double getHlthTot() {
		return hlthTot;
	}
	
	public void countStep() {
		steps++;
	}
	
	public int getNumSteps() {
		return steps;
	}
	
	public int getSamples() {
		return samples;
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
	
	public ArrayList<String> getActions(int generation){
		return ActionList.get(generation);
	}
	
	public int getHealthyFoodSize(){
		return healthyFood.size();
	}
	
	public int getPoisonFoodSize(){
		return poisonFood.size();
	}

	public int getEatFail(){
		return eatFail;
	}
	
	public void addEatFail(){
		eatFail++;
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

	@Override
	public int getHeight() {
		return height;
	}
	@Override
	public int getWidth(){
		return width;
	}

	public int getNumPushed() {
		return numPushed;
	}

	public int getNumAttacked() {
		return numAttacked;
	}
}	
