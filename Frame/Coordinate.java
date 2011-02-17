package Frame;
@SuppressWarnings("all")
public class Coordinate {
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	private int x;
	private int y;
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public Coordinate(){
		x = 0;
		y = 0;
	}
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	//------------------------------------------------------------------------------------
	//--getters/setters--
	//------------------------------------------------------------------------------------
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setX(int x){
		if(x >= GridPanel.WIDTH){
			this.x = GridPanel.WIDTH -1;
		}
		else if(x < 0){
			this.x = 0;
		}
		else{
			this.x = x;
		}
	}
	public void setY(int y){
		if(y >= GridPanel.HEIGHT){
			this.y = GridPanel.HEIGHT - 1;
		}
		else if(y < 0){
			this.y = 0;
		}
		else{
			this.y = y;
		}
	}
	
	/** Checks to see if a coordinate is roughly equal to another given a step var.
	 * 
	 * @param o the object to be compared
	 * @param step the varience by which somethin may be considered "equal"
	 * @return true if equal, false otherwise
	 */
	public boolean approxEquals(Object o, int step){
	    if (o instanceof Coordinate) {
	        Coordinate c = (Coordinate) o;
	        if (this.getX() < c.getX() + step &
	        		this.getX() > c.getX() - step){
	        	if(this.getY() < c.getY() + step &
	        		this.getY() > c.getY() - step){
	        		return true;
	        	}
	        }
	    }
	    return false;
	}
	//------------------------------------------------------------------------------------
	//--overloaded functions--
	//------------------------------------------------------------------------------------
	@Override 
	public boolean equals(Object o){
	    if (o instanceof Coordinate) {
	        Coordinate c = (Coordinate) o;
	        if (this.getX() == c.getX()
	        		& this.getY() == c.getY()){
	        	return true;
	        }
	    }
	    return false;
	}
	/**
	 * @return a String representation of the Object.
	 */
	public String toString(){
		return "(" + x + ", " + y + ")";
	}
}
