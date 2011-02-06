package Frame;

import Interactive.Organism;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

/**
 *	This class will handle all of the simulation's display.
 */
public class GridPanel extends JPanel 
{
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	public final static int WIDTH  = 600;
	public final static int HEIGHT = 400;
	
	private List<Organism> organisms;

	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	/**
	 * This constructor will handle all of the movements/interactions of
	 * all objects in the current game state.
	 */
	public GridPanel()
	{		
		//initial program settings
		organisms = new ArrayList<Organism>();
		
		//a timer and it's action event to call at every time t.
		javax.swing.Timer t = new javax.swing.Timer(50, new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	        	  /*begin game logic here:*/
	        	  for(Organism org: organisms){
	        		  //create new, random number 0-7 representing a movement.
		        	  Random r = new Random();
		        	  int movement = r.nextInt(8);
		        	  //int movement = 2;
		        	  //perform movement
	        		  switch(movement){
		        		  case 0: org.moveNorth(); break;
		        		  case 1: org.moveNorthEast(); break;
		        		  case 2: org.moveEast(); break;
		        		  case 3: org.moveSouthEast(); break;
		        		  case 4: org.moveSouth(); break;
		        		  case 5: org.moveSouthWest(); break;
		        		  case 6: org.moveWest(); break;
		        		  case 7: org.moveNorthWest(); break; 
	        		  }
	        	  }
	              repaint();
	          }
	       });
		
		t.start();
	}
	
	//------------------------------------------------------------------------------------
	//--accessors and mutators--
	//------------------------------------------------------------------------------------
	/**
	 * Sets the initial game state of the GridPanel
	 */
	public void initialize(){
		organisms.clear();
		for(int i=0; i<OptionsPanel.numOrgamisms; i++){
			organisms.add(new Organism());
		}
	}
	
	/** 
	 * Handles objects that stray off of the GridPanel and wraps their location.
	 * 
	 * @param o The Organism object to apply the wrap-setting to.
	 */
	public void setWrapAround(Organism o){		
		if(o.getLocation().getX() > GridPanel.WIDTH){
			o.getLocation().setX(o.getLocation().getX() - GridPanel.WIDTH);
		}
		if(o.getLocation().getX() <= 0){
			o.getLocation().setX(o.getLocation().getX() + GridPanel.WIDTH);
		}
		if(o.getLocation().getY() > GridPanel.HEIGHT){
			o.getLocation().setY(o.getLocation().getY() - GridPanel.HEIGHT);
		}
		if(o.getLocation().getY() <= 0){
			o.getLocation().setY(o.getLocation().getY() + GridPanel.HEIGHT);
		}
	}
	
	//------------------------------------------------------------------------------------
	//--Override Functions--
	//------------------------------------------------------------------------------------
	/**
	 * A function that will be called to update the
	 * current state of the panel
	 * 
	 * @param g the Graphics object used
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
			
		//handle each new organism created.
		for(Organism org : organisms){
			setWrapAround(org);
			System.out.println(org.getLocation());
			org.paint(g);
		}
	}
}