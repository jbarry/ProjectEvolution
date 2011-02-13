package Interactive;
import java.awt.Graphics;
import java.util.Random;
import Frame.*;

public abstract class Food {
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
		this.location=new Coordinate(r.nextInt(GridPanel.WIDTH + 1),
				r.nextInt(GridPanel.HEIGHT + 1));
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

	public Coordinate getLocation(){
		return location;
	}

	//------------------------------------------------------------------------------------
	//--accessors/mutators--
	//------------------------------------------------------------------------------------
	public void deplete(){
		if(foodRemaining>=0){
			foodRemaining--;
		}
	}

	public abstract String toString();

	public abstract void paint(Graphics g, boolean isDepleted);

}