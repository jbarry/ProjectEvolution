package Frame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
@SuppressWarnings("all")
public class OptionsPanel extends JPanel implements Runnable{
	public final static int WIDTH  = 200;
	public final static int HEIGHT = 400;
	
	public static int numOrganisms = 0;
	
	private JTextField numOrgsTxtBox;
	private JLabel     numOrgsLbl;
	private JButton    pause;
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public OptionsPanel(final GridPanel simulation){
		run();
		
		//initial JPanel settings
		setLayout(null);
		setLocation(0,0);
		setSize(OptionsPanel.WIDTH, OptionsPanel.HEIGHT);
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		/** # Organisms TextField */
		numOrgsTxtBox = new JTextField(4);
		numOrgsTxtBox.setLayout(null);
		numOrgsTxtBox.setSize(130,20);
		numOrgsTxtBox.setLocation(OptionsPanel.WIDTH/10, OptionsPanel.HEIGHT/10);
		numOrgsTxtBox.setFocusable(true);
		add(numOrgsTxtBox);
		
		/** # Organisms Label */
		numOrgsLbl = new JLabel();
		numOrgsLbl.setLayout(null);
		numOrgsLbl.setText("# organisms (0-1000):");
		numOrgsLbl.setSize(130,20);
		numOrgsLbl.setLocation(OptionsPanel.WIDTH/10, (OptionsPanel.HEIGHT/10)-20);
		add(numOrgsLbl);
		
	    KeyListener nOrgKeyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
		        int key = keyEvent.getKeyCode();
		        
		        if (key == KeyEvent.VK_ENTER) {
					try {
						int x = Integer.parseInt(numOrgsTxtBox.getText());
						if(x < 0){
							numOrgsLbl.setText("# < 0");
						}
						else if(x > 1000){
							numOrgsLbl.setText("# > 1000");
						}
						else{
							numOrgsLbl.setText("# organisms (0-1000):");
							//the number of organisms given via user-input.
							OptionsPanel.numOrganisms = x;
							simulation.initialize();
							simulation.start();
						}
					} catch (NumberFormatException a) {
						numOrgsLbl.setText("Invalid Entry!");
					}	
		        }
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
	    };
	    numOrgsTxtBox.addKeyListener(nOrgKeyListener);
	    
		/** Pause/Resume Button */
		pause = new JButton();
		pause.setLayout(null);
		pause.setText("Pause");
		pause.setSize(90,20);
		pause.setLocation(OptionsPanel.WIDTH/10, (OptionsPanel.HEIGHT/20) + 60);
		add(pause);
		
		ActionListener p = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventPause(simulation);
			}	
		};
		pause.addActionListener(p);
	}
	
	//------------------------------------------------------------------------------------
	//--accessors/mutators--
	//------------------------------------------------------------------------------------
	public void eventPause(GridPanel simulation){
		if(simulation.isPaused()){
			simulation.start();
			pause.setText("Pause");
		}
		else{
			simulation.stop();
			pause.setText("Resume");
		}
	}
	
	//------------------------------------------------------------------------------------
	//--overloaded functions--
	//------------------------------------------------------------------------------------
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
