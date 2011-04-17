package Interactive;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import Frame.Coordinate;

public class PoisonousFood extends Food {

	// ------------------------------------------------------------------------------------
	// --constructors--
	// ------------------------------------------------------------------------------------
	public PoisonousFood(double aMxHlth, int anId, int aScanRange) {
		super(aMxHlth, anId, 'p');
		icon = new ImageIcon(getClass().getResource("sprites/volcano.gif"))
		.getImage();
	}

	public PoisonousFood(int x, int y) {
		super(x, y);
		icon = new ImageIcon(getClass().getResource("sprites/volcano.gif"))
		.getImage();
	}

	public PoisonousFood(Coordinate aCoord) {
		super(aCoord);icon = new ImageIcon(getClass().getResource("sprites/volcano.gif"))
		.getImage();
	}

	@Override
	public String toString() {
		String str = "";
		str += " I am poisonous food. Don't eat me." + "\n Location: "
				+ getLocation() + "\n Remaining Poison: " + getHealth()
				+ "\n ID: " + this.getId();
		return str;
	}

	@Override
	public void paint(Graphics g, boolean isDepleted) {
		if (Matter.graphicsEnabled) {
			super.paint(g, isDepleted);
		} else {
			g.setColor(Color.RED);

			if (!isDepleted) {
				g.fillRect((int) this.getLocation().getX() - (getWidth() / 2),
						(int) this.getLocation().getY() - (getHeight() / 2),
						getWidth(), getHeight());
			}
		}
	}

	@Override
	public char getType() {
		return 'p';
	}

	public Double getFoodType() {
		return -1.0;
	}

	@Override
	public int getId() {
		return id;
	}
}