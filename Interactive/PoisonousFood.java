package Interactive;
import java.awt.Color;
import java.awt.Graphics;
import Frame.Coordinate;

public class PoisonousFood extends Food{
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public PoisonousFood(){}

	public PoisonousFood(int x, int y){
		super(x,y);
	}

	public PoisonousFood(Coordinate aCoord){
		super(aCoord);
	}

	@Override
	public String toString() {
		String str = "";
		str += " I am poisonous food. Don't eat me."
			+ "\n Location: " + getLocation()
			+ "\n Remaining Poison: " + getFoodRemaining();
		return str;		
	}

	@Override
	public void paint(Graphics g, boolean isDepleted) {
		g.setColor(Color.RED);

		if(!isDepleted){
			g.fillRect((int)this.getLocation().getX()-(getWidth()/2), 
					   (int)this.getLocation().getY()-(getHeight()/2), 
					   getWidth(), getHeight());
		}

	}


}