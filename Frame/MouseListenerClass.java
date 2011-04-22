package Frame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseListenerClass implements MouseListener{
	
	public MouseListenerClass(){
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		try{
			//get and display current mouse location.
			Coordinate mouseLocation = new Coordinate(arg0.getX(), arg0.getY());
			MonitorPanel.currMouseLoc.setText(mouseLocation.toString());
		}
		catch(NullPointerException e){}
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		MonitorPanel.currMouseLoc.setText("");
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
