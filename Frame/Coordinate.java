package Frame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("all")
public class Coordinate implements getSurroundingObjects{
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	protected int x;
	protected int y;
	
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
	public Coordinate(Coordinate c) {
		x = c.getX();
		y = c.getY();
	}
	
	//------------------------------------------------------------------------------------
	//--getters/setters--
	//------------------------------------------------------------------------------------
	public int getX() {
		return x;
	}
	public int getY() {
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
	
	public Node spawnNode(double aPriority) {
		return new Node(aPriority, this);
	}
	
	public boolean hasObstacle() {
		if(getSurroundingObjects().size() != 0) {
			return true;
		}
		return false;
	}
	@Override
	public ArrayList<Integer> getSurroundingObjects(char type, int scanRange) {
		Set<Integer> objectIds = new HashSet<Integer>();
		for(int i=location.getX()-width/2-scanRange; i<=location.getX()+width/2+scanRange; i++){
			for(int j=location.getY()-height/2-scanRange; j<=location.getY()+height/2+scanRange; j++){
				try{	
					//count all occurrences of objects in location map
					if(GridPanel.locationMap[i][j].snd == type){
						objectIds.add(GridPanel.locationMap[i][j].fst);
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
				}
			}
		}
		//make sure that scanning object was not included in scan.
		//TODO: will do this outside of class. In GridPanel probably.
		//		if(numObj >= width*height){
		//			numObj -= width*height; 
		//		}
		return new ArrayList<Integer>(objectIds);
	}
}