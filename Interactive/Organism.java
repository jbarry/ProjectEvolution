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
	public static int width = 5;
	public static int height = 5;
//	private Integer instanceCount;
	private double avgHealth;
	private double hlthTot;
	private int samples;
	private int steps;
	private Chromosome chromosome;
	// Organism should not have knowledge of its own fitness
	private double fitness;
	private Random r;
	private ArrayList<ArrayList<String>> ActionList;
	private ArrayList<Coordinate> StartingLocation;
	private ArrayList<Chromosome> chromosomeHistory;

	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public Organism() {
		super(7500.0);
		samples = 0;
		avgHealth = 0;
		hlthTot = 0;
		steps = 0;
		//location (x, y) is random between (0-width,0-height) exclusive
		r = new Random();
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);
		//check for collisions
		while(!canSpawn(x, y)){
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		location = new Coordinate(x, y);
		
		//set boundaries
		setWrapAround(width, height);
		setRange(width, height, 'o');
		
		chromosome = new Chromosome(9);
		fitness=0.0;
		ActionList= new ArrayList<ArrayList<String>>();
		ActionList.add(new ArrayList<String>());
		StartingLocation=new ArrayList<Coordinate>();
		chromosomeHistory= new ArrayList<Chromosome>();
	}

	public Organism(double aHealth, int chromSize,
			int anId, int aScnRng) {
		super(aHealth, anId, aScnRng);
		chromosome = new Chromosome(chromSize);
		samples = 0;
		avgHealth = 0;
		hlthTot = 0;
		steps = 0;
		fitness = 0.0;
		//location (x, y) is random between (0-width,0-height) exclusive
		r = new Random();
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);
		//check for collisions
		while(!canSpawn(x, y)){
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		location = new Coordinate(x, y);
		//set boundaries
		setWrapAround(width, height);
		setRange(width, height, 'o');
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
	
	public void newLocation(){
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

	
	@Override
	public double numSurroundingObjects(int scanRange) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void eatFood(Food f, double fdVal){
		f.deplete(fdVal);
		if(f instanceof HealthyFood) {
			System.out.println("orgId: " + id);
			System.out.println("hlthy");
			System.out.println("orgHealth: " + hlth);
			System.out.println("FoodId: " + f.getId());
			if(hlth + fdVal > mxHlth)
				hlth = mxHlth;
			else hlth+=fdVal;
		}
		else if(f instanceof PoisonousFood){
			System.out.println("orgId: " + id);
			System.out.println("pois");
			System.out.println("orgHealth: " + hlth);
			System.out.println("FoodId: " + f.getId());
			deplete(fdVal);
		}
	}
	
	public ArrayList<Food> look(LinkedList<HealthyFood> healthFdSrc,
			LinkedList<PoisonousFood> poisFoodSrc) {
		ArrayList<Food> toReturn = new ArrayList<Food>();
		Coordinate orgCoord = getLocation();
		int orgX= orgCoord.getX();
		int orgY= orgCoord.getY();
		for(Food f : healthFdSrc) {
			if(Math.abs(orgX - f.getLocation().getX()) <= scnRng ||
					Math.abs(orgY - f.getLocation().getY()) <= scnRng ){
				toReturn.add(f);
			}
		}
		for(Food f : poisFoodSrc) {
			if(Math.abs(orgX - f.getLocation().getX()) <= scnRng ||
					Math.abs(orgY - f.getLocation().getY()) <= scnRng ){
				toReturn.add(f);
			}
		}
		return toReturn;
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
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect((int)this.location.getX()-(width/2), 
				   (int)this.location.getY()-(height/2), 
				   width, height);
	}
	
	/**
	 * @param x - current x location if valid.
	 * @param y - current y location if valid.
	 * @return true if organism can spawn at given location.
	 */
	private boolean canSpawn(int x, int y){
		for(int i=x-width/2; i<=x+width/2; i++){
			for(int j=y-height/2; j<=y+height/2; j++){
				try{
					if(GridPanel.locationMap[i][j].snd != 'w'){
						return false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					
				}
			}
		}
		return true;
	}
	
	/**
	 * This method will modify the boolean location map and account for wrapping.
	 * 
	 * @param x        x-size for rectangle
	 * @param y        y-size for rectangle
	 * @param validity the value to mark the location map.
	 */
	public void setRange(int x, int y, Character value){
		//check to see if a valid entry was given.
		if(value != 'o' && value != 'w'){
			value = 'w';
		}
		else{
			for(int i=(location.getX()-(x/2)); i<=(location.getX()+(x/2)); i++){
				for(int j=(location.getY()-(y/2)); j<=(location.getY()+(y/2)); j++){
					try{
						GridPanel.locationMap[i][j].fst = id;
						GridPanel.locationMap[i][j].snd = value;
					}
					catch(ArrayIndexOutOfBoundsException e){
						
					}
				}
			}
		}
	}
	
	/**
	 * Handles objects that stray off of the GridPanel and wraps their location.
	 * @param rightLeftBound   - right and left boundary to trigger wrap
	 * @param topBottomBound   - top and bottom boundary to trigger wrap
	 */
	private void setWrapAround(int rightLeftBound, int topBottomBound){
		if(location.getX() + (rightLeftBound/2) >= GridPanel.WIDTH){
			//right
			if(canSpawn(width/2+1, location.getY()))
				location.setX((width/2)+1);
		}
		if(location.getX() - (rightLeftBound/2) <= 0){
			//left
			if(canSpawn(GridPanel.WIDTH - (width/2), location.getY()))
				location.setX(GridPanel.WIDTH - (width/2));
		}
		if(location.getY() + (topBottomBound/2) >= GridPanel.HEIGHT){
			//bottom
			if(canSpawn(location.getX(), height/2 + 1))
				location.setY(height/2 + 1);
		}
		if(location.getY() - (topBottomBound/2) <= 0){
			//top
			if(canSpawn(location.getX(), GridPanel.HEIGHT - (height/2)))
				location.setY(GridPanel.HEIGHT - (height/2));
		}
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
	
	public Coordinate getLocation() {
		return location;
	}

	public Chromosome getChromosome() {
		return chromosome;
	}
	
	public void setChromosome(Chromosome aChrom){
		chromosome = aChrom;
	}
	
	public double getFitness() {
		return fitness;
	}

	public void setFitness(double aFit) {
		fitness = aFit;
	}

	public double getHealth() {
		return hlth;
	}
	
	public double getMaxHealth() {
		return mxHlth;
	}
	
	public void setMxHlth(double aMxHlth) {
		mxHlth = aMxHlth;
	}
	
	public void setHealth(double aHealth) {
		hlth = aHealth;
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
	
	public int getScnRng() {
		return scnRng;
	}
	
	public void setScnRng(int aScnRng) {
		scnRng = aScnRng;
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
