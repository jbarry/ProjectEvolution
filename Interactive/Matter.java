package Interactive;

import Frame.Coordinate;

public abstract class Matter {
	protected Coordinate location;
	protected double hlth;
	protected double mxHlth;
	protected int id;
	
	public Matter() {
		
	}
	
	public Matter(double aMxHlth) {
		hlth = mxHlth = aMxHlth;
	}
	
	public Matter(double aMxHlth, int anId) {
		hlth = mxHlth = aMxHlth;
		id = anId;
	}
	
	public void deplete(double val) {
		if (hlth - val < 0) hlth = 0;
		else hlth-=val;
	}
	
	public abstract double numSurroundingObjects(int scanRange);
}
