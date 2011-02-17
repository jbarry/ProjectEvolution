package Interactive;
import java.awt.Color;
import java.awt.Graphics;
import Frame.Coordinate;

public class HealthyFood extends Food {
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public HealthyFood(){}

	public HealthyFood(int x, int y){
		super(x,y);
	}

	public HealthyFood(Coordinate aCoord){
		super(aCoord);
	}

	@Override
	public String toString() {
		String str = "";
		str += " I am food. Eat me."
			+ "\n Location: " + getLocation()
			+ "\n Remaining Food: " + getFoodRemaining();
		return str;		
	}

	@Override
	public void paint(Graphics g, boolean isDepleted) {
		g.setColor(Color.BLUE);
		
		if(!isDepleted){
			g.fillRect((int)this.getLocation().getX()-(getWidth()/2), 
					   (int)this.getLocation().getY()-(getHeight()/2), 
					   getWidth(), getHeight());
		}
	}
}