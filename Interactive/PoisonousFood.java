package Interactive;
import java.awt.Color;
import java.awt.Graphics;
import Frame.Coordinate;

public class PoisonousFood extends Food{
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
//	public PoisonousFood(double aMxHlth, int anId){
//		super(aMxHlth, anId);
//	}
	
	public PoisonousFood(double aMxHlth, int anId, int aScanRange){
		super(aMxHlth, anId, 'p');
	}

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
			+ "\n Remaining Poison: " + getHealth()
			+ "\n ID: " + this.getId();
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