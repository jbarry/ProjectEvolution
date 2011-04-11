package Interactive;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

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
		icon = new ImageIcon(getClass().getResource("sprites/volcano.gif")).getImage();
	}

	public PoisonousFood(int x, int y){
		super(x,y);
		icon = new ImageIcon(getClass().getResource("sprites/volcano.gif")).getImage();
	}

	public PoisonousFood(Coordinate aCoord){
		super(aCoord);
		icon = new ImageIcon(getClass().getResource("sprites/volcano.gif")).getImage();
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

}