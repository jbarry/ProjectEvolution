package Interactive;

import Frame.Coordinate;


public abstract class MatterBase extends Matter {
	
	@Override
	public void setLocation(Coordinate newLocation) {
		location = newLocation;
	}
	
	@Override
	public Coordinate getLocation() {
		return location;
	}

	@Override
	public int getMatterID() {
		return id;
	}

	@Override
	public void run() {
	}

	@Override
	public char getType() {
		return type;
	}
	
	@Override
	public double getHealth() {
		return health;
	}
	
	// Maybe need to be implemented
	
	/*@Override
	public int compareTo(Matter arg0) {
		return 0;
	}*/

	/*@Override
	public int getHeight() {
		return 0;
	}*/

	/*@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}*/
}
