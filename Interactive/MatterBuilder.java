package Interactive;

import Frame.Coordinate;

public abstract class MatterBuilder <T extends MatterBuilder<T>> {
	
	T withLocation(Coordinate c) {
		getParent().setLocation(c);
		return getThis();
	}
	
	protected abstract T getThis();
	
	protected abstract MatterBase getParent();
}
