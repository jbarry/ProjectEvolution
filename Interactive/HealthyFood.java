package Interactive;
import java.awt.Color;
import java.awt.Graphics;
import Frame.Coordinate;

public class HealthyFood extends Food {
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
//	public HealthyFood(double aMxHlth, int anId){
//		super(aMxHlth, anId);
//	}

	public HealthyFood(double aMxHlth, int anId, int aScanRange){
		super(aMxHlth, anId, 'h');
	}
	
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
			+ "\n Remaining Food: " + getHealth()
			+ "\n ID: " + this.getId();
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

	@Override
	public Double getType() {
		return 3.0;
	}
}