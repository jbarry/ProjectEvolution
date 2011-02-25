package Frame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Interactive.Food;
import Interactive.Organism;

@SuppressWarnings("all")
public class MonitorPanel extends JPanel implements Runnable{
	public final static int WIDTH = OptionsPanel.WIDTH;
	public final static int HEIGHT = GUI.HEIGHT - OptionsPanel.HEIGHT;
	
	public static JTextArea simObjInfo;
	public static JLabel currMouseLoc;
	public static JLabel simStatus;
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public MonitorPanel(final GridPanel simulation){
		run();
		
		//initial JPanel settings
		setLayout(null);
		setFocusable(true);
		setLocation(0, OptionsPanel.HEIGHT);
		setSize(MonitorPanel.WIDTH, MonitorPanel.HEIGHT);
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		/**Pause Notification*/
		simStatus = new JLabel();
		simStatus.setLayout(null);
		simStatus.setOpaque(false);
		simStatus.setLocation(0, 0); //upper right
		simStatus.setText("Running");
		simStatus.setSize(MonitorPanel.WIDTH, 20);
		add(simStatus);
		
		/**Organism Info Notification*/
		simObjInfo = new JTextArea();
		simObjInfo.setLayout(null);
		simObjInfo.setOpaque(false);
		simObjInfo.setLocation(0, simStatus.getHeight());
		simObjInfo.setText("No Object Selected");
		simObjInfo.setSize(MonitorPanel.WIDTH, 60);
		add(simObjInfo);
		
		/**Current Mouse Location Relative to Panel*/
		currMouseLoc = new JLabel();
		currMouseLoc.setLayout(null);
		currMouseLoc.setOpaque(false);
		currMouseLoc.setSize(100, 20);
		currMouseLoc.setLocation(0, MonitorPanel.HEIGHT-75);
		currMouseLoc.setText("");
		add(currMouseLoc);
	}
	
	//------------------------------------------------------------------------------------
	//--overrides--
	//------------------------------------------------------------------------------------
	@Override
	public void run() {
	}
}
