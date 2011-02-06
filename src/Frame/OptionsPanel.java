package Frame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OptionsPanel extends JPanel{
	public final static int WIDTH  = 200;
	public final static int HEIGHT = 400;
	
	public static int numOrgamisms = 0;
	
	private JTextField numOrgs;
	
	public OptionsPanel(){
		/** # Organisms TextField */
		numOrgs = new JTextField(4);
		numOrgs.setLayout(null);
		numOrgs.setSize(100,20);
		numOrgs.setLocation(OptionsPanel.WIDTH/10, OptionsPanel.HEIGHT/10);
		numOrgs.setFocusable(true);
		add(numOrgs);
		
	    KeyListener nOrgKeyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
		        int key = keyEvent.getKeyCode();
		        
		        if (key == KeyEvent.VK_ENTER) {
					try {
						int x = Integer.parseInt(numOrgs.getText());
						if(x < 0){
							JOptionPane.showMessageDialog(null, 
									"Enter a positive integer (0-1000)", 
									"Error", JOptionPane.INFORMATION_MESSAGE);
						}
						if(x > 1000){
							JOptionPane.showMessageDialog(null, 
									"Enter an integer less than 1000", 
									"Error", JOptionPane.INFORMATION_MESSAGE);
						}
						else{
							//the number of organisms given via user-input.
							OptionsPanel.numOrgamisms = x;
						}
					} catch (NumberFormatException a) {
						JOptionPane.showMessageDialog(null, 
								"Enter a valid integer", 
								"Error", JOptionPane.INFORMATION_MESSAGE);
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
	    numOrgs.addKeyListener(nOrgKeyListener);
	}
}
