package Frame;

import Interactive.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class will handle all of the simulation's display.
 */
@SuppressWarnings("all")
public class GridPanel extends JPanel implements Runnable
{
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	public final static int WIDTH = 600;
	public final static int HEIGHT = 400;

	private LinkedList<Organism> organisms;
	private LinkedList<Food> foodSources;
	
	private javax.swing.Timer t;

	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	/**
	 * This constructor will handle all of the movements/interactions of
	 * all objects in the current game state.
	 */
	public GridPanel()
	{
		run();
	}

	//------------------------------------------------------------------------------------
	//--accessors and mutators--
	//------------------------------------------------------------------------------------
	/**For the timer*/
	public void start(){
		t.start();
	}
	public void stop(){
		t.stop();
	}
	public boolean isPaused(){
		if(t.isRunning())
			return false;
		return true;
	}
	
	/**
	 * Sets the initial game state of the GridPanel
	 */
	public void initialize(){
		organisms.clear();
		foodSources.clear();
		for(int i=0; i<OptionsPanel.numOrganisms; i++){
			organisms.add(new Organism());
		}
		for(int i=0;i<OptionsPanel.numOrganisms/2;i++){
			foodSources.add(new Food());
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
			//System.out.println(org.getLocation());
			org.paint(g);
		}
		for(Food f: foodSources){
			if(f.getFoodRemaining()>0){
				System.out.println(f.getFoodRemaining());
				f.paint(g, false);
			}
			else{
				System.out.println(f.getFoodRemaining());
				f.paint(g, true);
			}
		}
	}

	@Override
	public void run() {
		//initial JPanel settings
		setLayout(null);
		setLocation(GUI.WIDTH - GridPanel.WIDTH,0);
		setSize(GridPanel.WIDTH, GridPanel.HEIGHT);
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		/**Add Listeners*/
		//track user mouse movement.
		MouseMotionListener simMouseMotion = new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent arg0) {
			}
			@Override
			public void mouseMoved(MouseEvent arg0) {
				try{
					//get and display current mouse location.
					Coordinate mouseLocation = new Coordinate(arg0.getX(), arg0.getY());
					MonitorPanel.currMouseLoc.setText(mouseLocation.toString());
					
					//used to manage checks
					boolean isOrg = false;
					boolean isFood = false;
					
					//check mouse location vs. all organism's locations.
					for(Organism o: organisms){	
						if(mouseLocation.approxEquals(o.getLocation(),5)){
							//organism found
							isOrg = true;
							MonitorPanel.simObjInfo.setText(o.toString());
							//break to prevent any more updating from occuring and loop overhead.
							break;
						}
						else{
							isOrg = false;
						}
					}
					
					for(Food f: foodSources){					
						if(mouseLocation.approxEquals(f.getLocation(),5)){
							//food found
							isFood = true;
							MonitorPanel.simObjInfo.setText(f.toString());
							//break to prevent any more updating from occuring and loop overhead.
							break;
						}
						else{
							isFood = false;
						}
					}
					
					if(!isFood & !isOrg){
						MonitorPanel.simObjInfo.setText("No Object Selected");
					}
				}
				catch(NullPointerException e){
					
				}
			}	
		};
		addMouseMotionListener(simMouseMotion);
		
		//handle other mouse events
		MouseListener simMouseListener = new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				try{
					//get and display current mouse location.
					Coordinate mouseLocation = new Coordinate(arg0.getX(), arg0.getY());
					MonitorPanel.currMouseLoc.setText(mouseLocation.toString());
				}
				catch(NullPointerException e){
					
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				MonitorPanel.currMouseLoc.setText("");
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		};
		addMouseListener(simMouseListener);

		//initial program settings
		organisms = new LinkedList<Organism>();
		foodSources = new LinkedList<Food>();

		//a timer and it's action event to call at every time t.
		t = new javax.swing.Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*begin game logic here:*/
				for(Organism org: organisms){
					//create new, random number 0-7 representing a movement.
					Random r = new Random();
					int movement = r.nextInt(8);
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
				for(Food f: foodSources){
					f.deplete();
				}
				repaint();
			}
		});

		t.start();
	}
}