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
	public static final int width = 5;
	public static final int height = 5;
	
	private double health;
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
		health = 100.00;
		
		//location (x, y) is random between (0-width,0-height) exclusive
		r = new Random();
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);
		//check for collisions
		try{
			while(!GridPanel.isValidLocation[x+width/2][y+height/2] 
			   || !GridPanel.isValidLocation[x+width/2][y-height/2]
			   || !GridPanel.isValidLocation[x-width/2][y+height/2]
			   || !GridPanel.isValidLocation[x-width/2][y-height/2]
			   || !GridPanel.isValidLocation[x][y-height/2]
			   || !GridPanel.isValidLocation[x][y+height/2]
			   || !GridPanel.isValidLocation[x+width/2][y]
			   || !GridPanel.isValidLocation[x-width/2][y]
			){
				x = r.nextInt(GridPanel.WIDTH);
			    y = r.nextInt(GridPanel.HEIGHT);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		location = new Coordinate(x, y);
		
		//set boundaries
		setWrapAround(width, height);
		setRange(width, height, false);
		
		chromosome = new Chromosome();
		fitness=0.0;
	}

	//This ctor is for testing purposes.
	public Organism(double ahealth, Chromosome aChromosome) {
		health = ahealth;
		chromosome = aChromosome;
	}

	public Organism(Coordinate aLocation, Chromosome aChromosome) {
		health = 100.0;
		location = aLocation;
		chromosome = aChromosome;
	}
	
	public void newLocation(){
		location = new Coordinate(r.nextInt(GridPanel.WIDTH + 1),
				r.nextInt(GridPanel.HEIGHT + 1)); 
	}

	//------------------------------------------------------------------------------------
	//--getters/setters--
	//------------------------------------------------------------------------------------
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Coordinate getLocation() {
		return location;
	}

	public Chromosome getChromosome() {
		return chromosome;
	}
	
	public void setChromosome(Chromosome aChrom){
		chromosome=aChrom;
	}
	
	public double getFitness() {
		return fitness;
	}

	public void setFitness(double aFit) {
		fitness=aFit;
	}

	public double getHealth() {
		return health;
	}
	
	public void setHealth(int aHealth) {
		health=aHealth;
		
	}
	
	//------------------------------------------------------------------------------------
	//--accessors/mutators--
	//------------------------------------------------------------------------------------
	public void moveNorth(LinkedList<Organism> organisms) {
		setWrapAround(width, height);
		//make old location available.
		setRange(width, height, true);
		//if the next move is available.
		try{
			if(GridPanel.isValidLocation[location.getX()][location.getY() - 1 - height/2]){
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
		setWrapAround(width, height);
		setRange(width, height, true);
		try{
			if(GridPanel.isValidLocation[location.getX() + 1 + width/2][location.getY() - 1 - height/2]){
				location.setX(location.getX() + 1);
				location.setY(location.getY() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		
		}
		setRange(width, height, false);
	}

	public void moveEast(LinkedList<Organism> organisms) {
		setWrapAround(width, height);
		setRange(width, height, true);
		try{
			if(location.getX() + 1 + width/2 >= GridPanel.WIDTH){
				location.setX(width/2);
			}
			if(GridPanel.isValidLocation[location.getX() + 1 + width/2][location.getY()]){
				location.setX(location.getX() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		
		}	
		setRange(width, height, false);
	}

	public void moveSouthEast(LinkedList<Organism> organisms) {
		setWrapAround(width, height);
		setRange(width, height, true);
		try{
			if(location.getY() + 1 + height/2>= GridPanel.HEIGHT){
				location.setY(height/2);
			}
			if(GridPanel.isValidLocation[location.getX() + 1 + width/2][location.getY() + 1 + height/2]){
				location.setX(location.getX() + 1);
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, false);
	}

	public void moveSouth(LinkedList<Organism> organisms) {
		setWrapAround(width, height);
		setRange(width, height, true);
		try{
			if(GridPanel.isValidLocation[location.getX()][location.getY() + 1 + height/2]){
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, false);

	}

	public void moveSouthWest(LinkedList<Organism> organisms) {
		setWrapAround(width, height);
		setRange(width, height, true);
		try{
			if(GridPanel.isValidLocation[location.getX() - 1 - width/2][location.getY() + 1 + height/2]){
				location.setX(location.getX() - 1);
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, false);
	}

	public void moveWest(LinkedList<Organism> organisms) {
		setWrapAround(width, height);
		setRange(width, height, true);
		try{
			if(GridPanel.isValidLocation[location.getX() - 1 - width/2][location.getY()]){
				location.setX(location.getX() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(width, height, false);
	}

	public void moveNorthWest(LinkedList<Organism> organisms) {
		setWrapAround(width, height);
		setRange(width, height, true);
		try{
			if(GridPanel.isValidLocation[location.getX() - 1 - width/2][location.getY() - 1 - height/2]){
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
			if(health<100 && health>99){
				health = 100;
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
		g.fillRect((int)this.getLocation().getX()-(width/2), 
				   (int)this.getLocation().getY()-(height/2), 
				   width, height);
	}
	
	/**
	 * This method will modify the boolean location map and account for wrapping.
	 * 
	 * @param x        x-size for rectangle
	 * @param y        y-size for rectangle
	 * @param validity the value to mark the location map.
	 */
	public void setRange(int x, int y, boolean validity){
		for(int i=(getLocation().getX()-(x/2)); i<=(getLocation().getX()+(x/2)); i++){
			//adjust coordinates for wrapping
			for(int j=(getLocation().getY()-(y/2)); j<=(getLocation().getY()+(y/2)); j++){
				//no conflicts
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
	public void setWrapAround(int rightLeftBound, int topBottomBound){
		if(getLocation().getX() + (rightLeftBound/2) >= GridPanel.WIDTH){
			//right
			location.setX((width/2)+1);
		}
		if(getLocation().getX() - (rightLeftBound/2) <= 0){
			//left
			location.setX(GridPanel.WIDTH - (width/2));
		}
		if(getLocation().getY() + (topBottomBound/2) >= GridPanel.HEIGHT){
			//bottom
			location.setY(height/2 + 1);
		}
		if(getLocation().getY() - (topBottomBound/2) <= 0){
			//top
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
			+  "\n Location: " + getLocation()
			+  "\n Health: " + getHealth();
		return str;
	}
}	

