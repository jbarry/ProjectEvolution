package Interactive;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

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
		icon = new ImageIcon(getClass().getResource("sprites/flower.gif")).getImage();
	}
	
	public HealthyFood(int x, int y){
		super(x,y);
		icon = new ImageIcon(getClass().getResource("sprites/flower.gif")).getImage();
	}

	public HealthyFood(Coordinate aCoord){
		super(aCoord);
		icon = new ImageIcon(getClass().getResource("sprites/flower.gif")).getImage();
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
}