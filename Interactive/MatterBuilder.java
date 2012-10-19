package Interactive;

import java.util.Random;

import Frame.Coordinate;

public abstract class MatterBuilder <T extends MatterBuilder<T>> {
	
	public T withLocation(Coordinate c) {
		location = c;
		return getThis();
	}
		
	public T withMatterId(int matterId) {
		this.matterId = matterId;
		return getThis();
	}
	
	public T withHealth(double health) {
		this.health = health;
		return getThis();
	}
	
	public T withMxHlth(double mxHlth) {
		this.mxHlth = mxHlth;
		return getThis();
	}
	
	public T withType(char type) {
		this.type = type;
		return getThis();
	}
	
	public T withHeight(int height) {
		this.height = height;
		return getThis();
	}
	
	public T withWidth(int Width) {
		this.width = Width;
		return getThis();
	}
	
	protected abstract T getThis();
		
	protected Coordinate location;
	protected double health;
	protected double mxHlth;
	protected int matterId;
	protected Random r;
	protected char type;
	protected int height;
	protected int width;
	public static boolean graphicsEnabled = false;
}