package Interactive;

import Frame.Coordinate;

public abstract class Matter {
	protected Coordinate location;
	protected double hlth;
	protected double mxHlth;
	protected int id;
	protected int scnRng;
	
	public Matter() {
		
	}
	
	public Matter(double aMxHlth) {
		hlth = mxHlth = aMxHlth;
		scnRng = 10;
	}
	
	public Matter(double aMxHlth, int anId) {
		hlth = mxHlth = aMxHlth;
		id = anId;
		scnRng = 10;
	}
	
	public Matter(double aMxHlth, int anId, int aScnRng) {
		hlth = mxHlth = aMxHlth;
		id = anId;
		scnRng = aScnRng;
	}
	
	public void deplete(double val) {
		if (hlth - val < 0) hlth = 0;
		else hlth-=val;
	}
	
	public abstract double numSurroundingObjects(int scanRange);
}
