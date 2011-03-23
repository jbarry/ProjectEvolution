package Interactive;

import java.awt.Color;
import java.awt.Graphics;
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
	private ArrayList<ArrayList<String>> ActionList;
	private ArrayList<Coordinate> StartingLocation;
	private ArrayList<Chromosome> chromosomeHistory;
	public static int WIDTH = 5;
	public static int HEIGHT = 5;
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------

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
		setRange(WIDTH, HEIGHT, 'w');
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);
		while(!canSpawn(x, y)){
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		location = new Coordinate(x, y);
		//set boundaries
		setWrapAround(WIDTH, HEIGHT);
		setRange(WIDTH, HEIGHT, 'o');
	}
	
	public void eatFood(Food f, double fdVal){
		f.deplete(fdVal);
		if(f instanceof HealthyFood) {
//			System.out.println("orgId: " + id);
//			System.out.println("hlthy");
//			System.out.println("orgHealth: " + hlth);
//			System.out.println("FoodId: " + f.getId());
			incHlth(fdVal);
		}
		else if(f instanceof PoisonousFood){
//			System.out.println("orgId: " + id);
//			System.out.println("pois");
//			System.out.println("orgHealth: " + hlth);
//			System.out.println("FoodId: " + f.getId());
			deplete(fdVal);
		}
	}
	
	public ArrayList<Food> look(LinkedList<HealthyFood> healthFdSrc,
			LinkedList<PoisonousFood> poisFoodSrc) {
		ArrayList<Food> toReturn = new ArrayList<Food>();
		Coordinate orgCoord = getLocation();
		int orgX = orgCoord.getX();
		int orgY = orgCoord.getY();
		for(Food f : healthFdSrc) {
			if(Math.abs(orgX - f.getLocation().getX()) <= scanRange ||
					Math.abs(orgY - f.getLocation().getY()) <= scanRange ){
				toReturn.add(f);
			}
		}
		for(Food f : poisFoodSrc) {
			if(Math.abs(orgX - f.getLocation().getX()) <= scanRange ||
					Math.abs(orgY - f.getLocation().getY()) <= scanRange ){
				toReturn.add(f);
			}
		}
		return toReturn;
	}
	
	public void moveNorth(LinkedList<Organism> organisms) {
		//make old location available.
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		
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
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveNorthEast(LinkedList<Organism> organisms) {
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(canSpawn(location.getX() + 1, location.getY() - 1)){
				location.setX(location.getX() + 1);
				location.setY(location.getY() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		
		}
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveEast(LinkedList<Organism> organisms) {
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(location.getX() + 1 + WIDTH/2 >= GridPanel.WIDTH){
				if(canSpawn(WIDTH/2, location.getY()))
					location.setX(WIDTH/2);
			}
			if(canSpawn(location.getX() + 1, location.getY())){
				location.setX(location.getX() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		
		}	
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveSouthEast(LinkedList<Organism> organisms) {
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(location.getY() + 1 + HEIGHT/2>= GridPanel.HEIGHT){
				if(canSpawn(location.getX(), HEIGHT/2))
					location.setY(HEIGHT/2);
			}
			if(canSpawn(location.getX() + 1, location.getY() + 1)){
				location.setX(location.getX() + 1);
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveSouth(LinkedList<Organism> organisms) {
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(canSpawn(location.getX(), location.getY() + 1)){
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(WIDTH, HEIGHT, 'o');

	}

	public void moveSouthWest(LinkedList<Organism> organisms) {
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(canSpawn(location.getX() - 1, location.getY() + 1)){
				location.setX(location.getX() - 1);
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveWest(LinkedList<Organism> organisms) {
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(canSpawn(location.getX() - 1, location.getY())){
				location.setX(location.getX() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveNorthWest(LinkedList<Organism> organisms) {
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(canSpawn(location.getX() - 1, location.getY() - 1)){
				location.setX(location.getX() - 1);
				location.setY(location.getY() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){}
		setRange(WIDTH, HEIGHT, 'o');
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect((int)this.location.getX()-(WIDTH/2), 
				   (int)this.location.getY()-(HEIGHT/2), 
				   WIDTH, HEIGHT);
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
			+  "\n Health: " + hlth;
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
	
	@Override
	public int getHeight() {
		return Organism.HEIGHT;
	}
	
	@Override
	public int getWidth() {
		return Organism.WIDTH;
	}
	
	public void addAction(String action,int index){
		ActionList.get(ActionList.size()-1).add(action + " " + index);
	}
	
	public void addGeneration(){
		ActionList.add(new ArrayList<String>());
		addStartingLocation();
		chromosomeHistory.add(chromosome);
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
}	
