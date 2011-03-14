package Interactive;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

import Frame.*;

public class Organism {
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	public static int width = 5;
	public static int height = 5;
	
	private double health;
	private double avgHealth;
	private double healthTot;
	private int samples;
	private int steps;
	private Coordinate location;
	private Chromosome chromosome;
	//TODO: fitness instance variable.
	// Organism should not have knowledge of its own fitness
	private double fitness;
	private Random r;

	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public Organism() {
		health = 7500.00;
		samples = 0;
		avgHealth = 0;
		healthTot = 0;
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
		setRange(width, height, false);
		
		chromosome = new Chromosome(9);
		fitness=0.0;
	}

	//for testing purposes only.
	//just removing the GridPanel call.
	public Organism(boolean boo, int aChromSize) {
		health = 7500.00;
		r = new Random();
		chromosome = new Chromosome(aChromSize);
		fitness = 0.0;
	}
	
	//This ctor is for testing purposes.
	public Organism(double ahealth, Chromosome aChromosome) {
		health = ahealth;
		chromosome = aChromosome;
	}

	public Organism(Coordinate aLocation, Chromosome aChromosome) {
		health = 7500.0;
		location = aLocation;
		chromosome = aChromosome;
	}
	
	public void newLocation(){
		setRange(width, height, true);
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);
		while(!canSpawn(x, y)){
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		location = new Coordinate(x, y);
		//set boundaries
		setWrapAround(width, height);
		setRange(width, height, false);

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
		return health;
	}
	
	public void setHealth(int aHealth) {
		health = aHealth;
	}
	
	public void depleteHealth() {
		health--;
	}
	
	public void setAvgHealth(double aAvg) {
		avgHealth = aAvg;
	}
	
	public void updateAvgHealth() {
		healthTot+=health;
		samples++;
	}
	
	public double getAvgHealth() {
		return avgHealth;
	}
	
	public void calcAvgHealth() {
		avgHealth = healthTot/samples;
	}
	
	public void countStep() {
		steps++;
	}
	//------------------------------------------------------------------------------------
	//--accessors/mutators--
	//------------------------------------------------------------------------------------
	public void moveNorth(LinkedList<Organism> organisms) {
		//make old location available.
		setRange(width, height, true);
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
		setRange(width, height, false);
	}

	public void moveNorthEast(LinkedList<Organism> organisms) {
		setRange(width, height, true);
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() + 1, location.getY() - 1)){
				location.setX(location.getX() + 1);
				location.setY(location.getY() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		
		}
		setRange(width, height, false);
	}

	public void moveEast(LinkedList<Organism> organisms) {
		setRange(width, height, true);
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
		setRange(width, height, false);
	}

	public void moveSouthEast(LinkedList<Organism> organisms) {
		setRange(width, height, true);
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
		setRange(width, height, false);
	}

	public void moveSouth(LinkedList<Organism> organisms) {
		setRange(width, height, true);
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX(), location.getY() + 1)){
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, false);

	}

	public void moveSouthWest(LinkedList<Organism> organisms) {
		setRange(width, height, true);
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() - 1, location.getY() + 1)){
				location.setX(location.getX() - 1);
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, false);
	}

	public void moveWest(LinkedList<Organism> organisms) {
		setRange(width, height, true);
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() - 1, location.getY())){
				location.setX(location.getX() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, false);
	}

	public void moveNorthWest(LinkedList<Organism> organisms) {
		setRange(width, height, true);
		setWrapAround(width, height);
		try{
			if(canSpawn(location.getX() - 1, location.getY() - 1)){
				location.setX(location.getX() - 1);
				location.setY(location.getY() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){

		}
		setRange(width, height, false);
	}
	
	public void eatFood(Food f){
		f.deplete();
		if(f instanceof HealthyFood){
			if(health<7500 && health>7499){
				health = 7500;
			}
			else if(health<=99){
				health += 1;
			}
		}
		else if(f instanceof PoisonousFood){
			if(health>0 && health<1){
				health = 0;
			}
			else if(health>=1){
				health -= 1;
			}
		}
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
					if(!GridPanel.isValidLocation[i][j]){
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
	public void setRange(int x, int y, boolean validity){
		for(int i=(location.getX()-(x/2)); i<=(location.getX()+(x/2)); i++){
			for(int j=(location.getY()-(y/2)); j<=(location.getY()+(y/2)); j++){
				try{
					GridPanel.isValidLocation[i][j] = validity;
				}
				catch(ArrayIndexOutOfBoundsException e){
					
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
			+  "\n Health: " + health;
		return str;
	}
}	

