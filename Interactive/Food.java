package Interactive;
import java.util.Random;

import Frame.*;
import Evolution.*;

public class Food {
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	private Coordinate location;
	private double foodRemaining;
	private Random r;
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public Food(){
		this.r = new Random();
		this.location=new Coordinate(r.nextInt(),r.nextInt());
		this.foodRemaining=100.0;	
	}
	
	public Food(int x, int y){
		this.r = new Random();
		this.location=new Coordinate(x,y);
		this.foodRemaining=100.0;	
	}
	
	public Food(Coordinate aCoord){
		this.r = new Random();
		this.location=aCoord;
		this.foodRemaining=100.0;	
	}
	
	//------------------------------------------------------------------------------------
	//--getters/setters--
	//------------------------------------------------------------------------------------
	public double getFoodRemaining(){
		return foodRemaining;
	}
	
	public Coordinate getCoordinate(){
		return location;
	}
	
	//------------------------------------------------------------------------------------
	//--accessors/mutators--
	//------------------------------------------------------------------------------------
	public void deplete(){
		foodRemaining--;
	}
	
	
}
