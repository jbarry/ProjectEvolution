package Frame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import Interactive.Food;
import Interactive.HealthyFood;
import Interactive.Organism;
import Interactive.PoisonousFood;

public class MouseMotionListenerClass implements MouseMotionListener {
	
	private GridPanel gp;
	
	public MouseMotionListenerClass(GridPanel aGrid) {
		gp = aGrid;
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		try {
			//get and display current mouse location.
			Coordinate mouseLocation = new Coordinate(arg0.getX(), arg0.getY());
			MonitorPanel.currMouseLoc.setText(mouseLocation.toString());

			//used to manage checks
			boolean isOrg = false;
			boolean isPFood = false;
			boolean isHFood = false;

			//check mouse location vs. all organism's locations.
			for(Organism o: gp.getOrganisms()) {
				if(mouseLocation.approxEquals(o.getLocation(), Organism.width/2)) {
					//organism found
					isOrg = true;
					MonitorPanel.simObjInfo.setText(o.toString());
					//break to prevent any more updating from occuring and loop overhead.
					break;
				} else {
					isOrg = false;
				}
			}

			for(HealthyFood r: gp.getHealthyFoodList()) {
				if(mouseLocation.approxEquals(r.getLocation(), Food.width/2)){
					//food found
					isHFood = true;
					MonitorPanel.simObjInfo.setText(r.toString());
					//break to prevent any more updating from occuring and loop overhead.
					break;
				} else {
					isHFood = false;
				}
			}
			for(PoisonousFood r: gp.getPoisonousFoodList()) {
				if(mouseLocation.approxEquals(r.getLocation(), Food.width/2)){
					//food found
					isPFood = true;
					MonitorPanel.simObjInfo.setText(r.toString());
					//break to prevent any more updating from occuring and loop overhead.
					break;
				}
				else{
					isPFood = false;
				}
			}

			if(!isHFood & !isPFood & !isOrg)
				MonitorPanel.simObjInfo.setText("No Object Selected");
		}
		catch(NullPointerException e){

		}
	}
}
