package Interactive;

import Frame.Coordinate;

public abstract class Matter {
	protected Coordinate location;
	protected double health;
	protected double mxHlth;
	protected int id;
	
	public Matter() {
		
	}
	
	public Matter(double aMxHlth) {
		health = mxHlth = aMxHlth;
	}
	
	public Matter(double aMxHlth, int anId) {
		health = mxHlth = aMxHlth;
		id = anId;
	}
	
	public abstract double numSurroundingObjects(int scanRange);
}
