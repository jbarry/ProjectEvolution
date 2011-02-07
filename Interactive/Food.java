package Interactive;
import java.awt.Color;
import java.awt.Graphics;
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

	public void paint(Graphics g, boolean isDepleted)
	{
		g.setColor(Color.BLUE);
		
		if(isDepleted){
			g.drawRect((int)this.getLocation().getX()-3, (int)this.getLocation().getY()-3, 6, 6);
		}
		else{
			g.fillRect((int)this.getLocation().getX()-3, (int)this.getLocation().getY()-3, 6, 6);
		}
	}


}