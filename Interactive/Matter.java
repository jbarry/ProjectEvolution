package Interactive;

import Frame.Coordinate;

public abstract class Matter {
	protected Coordinate location;
	protected double health;
	protected double maxHealth;
	
	public abstract double numSurroundingObjects(int scanRange);
}
