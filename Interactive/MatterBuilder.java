package Interactive;

import java.util.Random;

import Frame.Coordinate;

public abstract class MatterBuilder <T extends MatterBuilder<T>> {
	
	T withLocation(Coordinate c) {
		location = c;
		return getThis();
	}
		
	T withId(int id) {
		this.id = id;
		return getThis();
	}
	
	T withHealth(double health) {
		this.health = health;
		return getThis();
	}
	
	T withMxHlth(double mxHlth) {
		this.mxHlth = mxHlth;
		return getThis();
	}
	
	T withType(char type) {
		this.type = type;
		return getThis();
	}
	
	protected abstract T getThis();
		
	protected Coordinate location;
	protected double health;
	protected double mxHlth;
	protected int id;
	protected Random r;
	protected char type;
	public static boolean graphicsEnabled = false;
}