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
		//location (x, y) is random between (0-width,0-height) inclusive
		r = new Random();
		location = new Coordinate(r.nextInt(GridPanel.WIDTH + 1),
				r.nextInt(GridPanel.HEIGHT + 1));
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
	public Coordinate getLocation() {
		return location;
	}

	public Chromosome getChromosome() {
		return chromosome;
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
	
	public void setChromosome(Chromosome aChrom){
		chromosome=aChrom;
	}
	//------------------------------------------------------------------------------------
	//--accessors/mutators--
	//------------------------------------------------------------------------------------
	public void moveNorth(LinkedList<Organism> organisms) {
		location.setY(location.getY() - 1);
		setWrapAround(this);
		if(organismConflictsWithAnotherOrganism(this, organisms)){
			location.setY(location.getY() + 1);
			setWrapAround(this);
		}
		
	}

	public void moveNorthEast(LinkedList<Organism> organisms) {
		location.setX(location.getX() + 1);
		location.setY(location.getY() - 1);
		setWrapAround(this);
		if(organismConflictsWithAnotherOrganism(this, organisms)){
			location.setX(location.getX() - 1);
			location.setY(location.getY() + 1);
			setWrapAround(this);
		}
	}

	public void moveEast(LinkedList<Organism> organisms) {
		location.setX(location.getX() + 1);
		setWrapAround(this);
		if(organismConflictsWithAnotherOrganism(this, organisms)){
			location.setX(location.getX() - 1);
			setWrapAround(this);
		}
	}

	public void moveSouthEast(LinkedList<Organism> organisms) {
		location.setX(location.getX() + 1);
		location.setY(location.getY() + 1);
		setWrapAround(this);
		if(organismConflictsWithAnotherOrganism(this, organisms)){
			location.setX(location.getX() - 1);
			location.setY(location.getY() - 1);
			setWrapAround(this);
		}
	}

	public void moveSouth(LinkedList<Organism> organisms) {
		location.setY(location.getY() + 1);
		setWrapAround(this);
		if(organismConflictsWithAnotherOrganism(this, organisms)){
			location.setY(location.getY() - 1);
			setWrapAround(this);
		}
	}

	public void moveSouthWest(LinkedList<Organism> organisms) {
		location.setX(location.getX() - 1);
		location.setY(location.getY() + 1);
		setWrapAround(this);
		if(organismConflictsWithAnotherOrganism(this, organisms)){
			location.setX(location.getX() + 1);
			location.setY(location.getY() - 1);
			setWrapAround(this);
		}
	}

	public void moveWest(LinkedList<Organism> organisms) {
		location.setX(location.getX() - 1);
		setWrapAround(this);
		if(organismConflictsWithAnotherOrganism(this, organisms)){
			location.setX(location.getX() + 1);
			setWrapAround(this);
		}
	}

	public void moveNorthWest(LinkedList<Organism> organisms) {
		location.setX(location.getX() - 1);
		location.setY(location.getY() - 1);
		setWrapAround(this);
		if(organismConflictsWithAnotherOrganism(this, organisms)){
			location.setX(location.getX() + 1);
			location.setY(location.getY() + 1);
			setWrapAround(this);
		}
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
		g.fillRect((int)this.getLocation().getX()-2, (int)this.getLocation().getY()-2, 5, 5);
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
	 * Determines whether or not an organism conflicts with another organisms' location
	 *
	 * @param o The Organism that is being compared to the list of organisms.
	 * @return (true/false) whether or not the organism conflicts with another organism.
	 */
	private boolean organismConflictsWithAnotherOrganism(Organism o, LinkedList<Organism> organisms) {
		int leftBoundary = o.getLocation().getX() - 2;
		int rightBoundary = o.getLocation().getX() + 2;
		int lowerBoundary = o.getLocation().getY() + 2;
		int upperBoundary = o.getLocation().getY() - 2;

		boolean conflictsWithOrganism = false;
		for(Organism org: organisms){
			if(!org.equals(o)){
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
					 * Organism conflicts with another organism's location
					 */
					conflictsWithOrganism = true;
					break;
				}
			}
		}
		return conflictsWithOrganism;
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

	public void setHealth(int aHealth) {
		health=aHealth;
		
	}
	}	

