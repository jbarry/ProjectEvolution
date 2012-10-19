package Interactive;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import Frame.Coordinate;

public class HealthyFood extends Food {

	// ------------------------------------------------------------------------------------
	// --constructors--
	// ------------------------------------------------------------------------------------
	public HealthyFood(double aMxHlth, int anId, int aScanRange) {
		super(aMxHlth, anId, 'h');
		icon = new ImageIcon(getClass().getResource("sprites/flower.gif"))
				.getImage();
	}

	public HealthyFood(int x, int y) {
		super(x, y);
		icon = new ImageIcon(getClass().getResource("sprites/flower.gif"))
				.getImage();
	}

	public HealthyFood(Coordinate aCoord) {
		super(aCoord);
		icon = new ImageIcon(getClass().getResource("sprites/flower.gif"))
				.getImage();
	}

	@Override
	public String toString() {
		String str = "";
		str += " I am food. Eat me." + "\n Location: " + getLocation()
				+ "\n Remaining Food: " + getHealth() + "\n ID: "
				+ this.getMatterID();
		return str;
	}

	@Override
	public void paint(Graphics g, boolean isDepleted) {
		if (Matter.graphicsEnabled) {
			super.paint(g, isDepleted);
		} else {
			g.setColor(Color.BLUE);

			if (!isDepleted) {
				g.fillRect((int) this.getLocation().getX() - (getWidth() / 2),
						(int) this.getLocation().getY() - (getHeight() / 2),
						getWidth(), getHeight());
			}
		}
	}

	@Override
	public char getType() {
		return 'h';
	}

	public Double getFoodType() {
		return 1.0;
	}

	@Override
	public int getMatterID() {
		return matterId;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}