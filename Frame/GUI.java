/**
 * Project Evolution 
 * 
 * Created in 2011
 * 
 * @author Chris Jackey
 * @author Devin Lam
 * @author Dwight Bell
 * @author Ian Gardea
 * @author Justin Barry
 * 
 * @version 0.1 Revision 4
 */

package Frame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *	This class will host and initialize all JPanels and JMenuItems. It will
 *	NOT perform any game logic.
 */
@SuppressWarnings("all")
public class GUI {
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	public final static int WIDTH  = 800;
	public final static int HEIGHT = 600;
	
	private JFrame   jframe;
	private JMenuBar menuBar;
	private JMenu    fileMenu;
	private JMenu    helpMenu;

	private JMenuItem newSimulation;
	private JMenuItem pause;
	private JMenuItem exitApplication;
	private JMenuItem about;
	
	private OptionsPanel optionsPanel;
	private GridPanel    simulation;
	private MonitorPanel monitorPanel;
	public static GenerationPanel genPanel;
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public GUI(){
		/**Create and set jframe attributes*/
		jframe = new JFrame();
		jframe.setLayout(null);
		jframe.setAlwaysOnTop(true);
		jframe.setSize(GUI.WIDTH, GUI.HEIGHT);
		jframe.setVisible(true);
		jframe.setFocusable(true);
		jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		jframe.setTitle("Project Evolution");
		
		KeyListener frameKeyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
		        int key = keyEvent.getKeyCode();
		        switch(key){	        
			        //p-key
			        case KeyEvent.VK_P: {
			        	optionsPanel.eventPause(simulation);
			        }
			        break;
		        }
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		};
		jframe.addKeyListener(frameKeyListener);
		
		MouseListener frameMouseListener = new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				jframe.requestFocusInWindow();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {	
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		};
		jframe.addMouseListener(frameMouseListener);
		
		/**Create Game Grid Panel*/
		simulation = new GridPanel();
		jframe.add(simulation);
		
		/**Create Options Panel*/
		optionsPanel = new OptionsPanel(this, simulation);
		jframe.add(optionsPanel);
		
		/**Create Monitor Panel*/
		monitorPanel = new MonitorPanel(simulation);
		jframe.add(monitorPanel);
		
		/**Create Generation Panel*/
		genPanel = new GenerationPanel(simulation);
		jframe.add(genPanel);
		
		/**Create Menu*/
		menuBar  = new JMenuBar();
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");

		menuBar.add(fileMenu);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(helpMenu);

		jframe.setJMenuBar(menuBar);
		
		//new simulation option
		newSimulation = new JMenuItem("New Simulation");
		newSimulation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//initialize GridPanel.
				if(!getGridPanelData(e)){
					pause.setEnabled(true);
					
					simulation.initialize();
					simulation.start();
				}
			}
		});
		fileMenu.add(newSimulation);
		
		//pause/resume option
		pause = new JMenuItem("Pause/Resume");
		pause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				optionsPanel.eventPause(simulation);
			}
		});
		fileMenu.add(pause);
		pause.setEnabled(false);
		
		//exit option
		exitApplication = new JMenuItem("Exit");
		exitApplication.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				confirmExit();
			}

		});
		fileMenu.add(exitApplication);
		
		//about option
		about = new JMenuItem("About...");
		about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(jframe, 
						"Project Evolution v. 1.0\n\n"
						+ "Written By:\n"
						+ "Justin Barry, Dwight Bell, Ian Gardea, Devin Lam, and Chris Jackey\n\n"
						+ "This program will simulate basic Social Darwinism amongst \n"
						+ "organisms with the same initial conditions. Intelligence is \n"
						+ "represented through a Robust Gene Expression Program.", 
						"About...", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(about);

		/**set additional frame attributes*/
		jframe.setLocationRelativeTo(null);
		jframe.setResizable(false);
		jframe.addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						confirmExit();
					}
				}
		);
	
	}
	
	//------------------------------------------------------------------------------------
	//--Getters/Setters--
	//------------------------------------------------------------------------------------
	public JFrame getContainer(){
		return jframe;
	}
	
	//------------------------------------------------------------------------------------
	//--accessors/mutators--
	//------------------------------------------------------------------------------------
	/**
	 * Sets initial game state of the Panel.
	 * Note: This does NOT handle game state changes.
	 * 
	 * @param e an ActionEvent instance
	 */
	private boolean getGridPanelData(ActionEvent e){
		//textbox for # organisms inquiry
		boolean userCancel = true;
		jframe.setAlwaysOnTop(false);
		JTextField numOrganisms = new JTextField();
		JPanel jP = new JPanel();
		jP.setLayout(new GridLayout(2,2,5,5));
		jP.add(new JLabel("Number of Organisms (0-1000):"));
		jP.add(numOrganisms);
		
		Object[] msg = {jP};

		JOptionPane op = new JOptionPane(
				msg,
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION,
				null,
				null);

		JDialog dialog = op.createDialog("New Simulation");
		dialog.setVisible(true);

		int result = 1;
		try{
			result = ((Integer)op.getValue()).intValue();
		}
		catch(NullPointerException q){

		}

		if(result == JOptionPane.OK_OPTION){
			userCancel = false;
			try {
				int x = Integer.parseInt(numOrganisms.getText());
				if(x < 0){
					JOptionPane.showMessageDialog(jframe, 
							"Enter a positive integer (0-1000)", 
							"Error", JOptionPane.INFORMATION_MESSAGE);
					//try again
					getGridPanelData(e);
				}
				else if(x > 1000){
					JOptionPane.showMessageDialog(jframe, 
							"Enter an integer less than 1000", 
							"Error", JOptionPane.INFORMATION_MESSAGE);
					//try again
					getGridPanelData(e);
				}
				else{
					//the number of organisms given via user-input.
					OptionsPanel.numOrganisms = x;
					optionsPanel.toggleEnabled(true);
					pause.setEnabled(false);
				}
			} catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(jframe, 
						"Enter a valid integer", 
						"Error", JOptionPane.INFORMATION_MESSAGE);
				//try again
				getGridPanelData(e);
			}	
		}
		jframe.setAlwaysOnTop(true);
		dialog.dispose();
		return userCancel;
	}
	
	public void enableJMenuItemPause(){
		pause.setEnabled(true);
	}
	
	private void confirmExit() {
		//Display confirm dialog
		int confirmed =
			JOptionPane.showConfirmDialog(jframe,
					"Exit Graph?", "Confirm",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		//Close if user confirmed
		if (confirmed == JOptionPane.YES_OPTION)
		{                           
			//Close frame and exit program
			jframe.dispose();
			System.exit(0);
		}
	}
	
	//------------------------------------------------------------------------------------
	//--MAIN--
	//------------------------------------------------------------------------------------
	/**
	 * Defines main entry point for program.
	 */
	public static void main(String[] args){
		new GUI();
	}
}
