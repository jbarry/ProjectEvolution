package Interactive;

import java.awt.Color;
import java.awt.Graphics;
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

	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public Organism() {
		health = 100.00;
		//location (x, y) is random between (0-width,0-height) inclusive
		Random r = new Random();
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
	//------------------------------------------------------------------------------------
	//--accessors/mutators--
	//------------------------------------------------------------------------------------
	public void moveNorth() {
		location.setY(location.getY() - 1);
	}

	public void moveNorthEast() {
		location.setX(location.getX() + 1);
		location.setY(location.getY() - 1);
	}

	public void moveEast() {
		location.setX(location.getX() + 1);
	}

	public void moveSouthEast() {
		location.setX(location.getX() + 1);
		location.setY(location.getY() + 1);
	}

	public void moveSouth() {
		location.setY(location.getY() + 1);
	}

	public void moveSouthWest() {
		location.setX(location.getX() - 1);
		location.setY(location.getY() + 1);
	}

	public void moveWest() {
		location.setX(location.getX() - 1);
	}

	public void moveNorthWest() {
		location.setX(location.getX() - 1);
		location.setY(location.getY() - 1);
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect((int)this.getLocation().getX(), (int)this.getLocation().getY(), 5, 5);
	}

	//------------------------------------------------------------------------------------
	//--overloaded functions--
	//------------------------------------------------------------------------------------

	/**
	 * @return a String representation of the Object.
	 */
	public String toString(){
		String str = "";
		str += "I am an Organism. Fear me."
			+  "\nLocation: " + getLocation();
		return str;
	}
}


