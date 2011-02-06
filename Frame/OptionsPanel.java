package Frame;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OptionsPanel extends JPanel{
	public final static int WIDTH  = 200;
	public final static int HEIGHT = 400;
	
	public static int numOrgamisms = 0;
	
	private JTextField numOrgsTxtBox;
	private JLabel     numOrgsLbl;
	
	public OptionsPanel(final GridPanel theGrid){
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
							OptionsPanel.numOrgamisms = x;
							theGrid.initialize();
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
	}
}
