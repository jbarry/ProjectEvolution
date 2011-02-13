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
	
	private JLabel simPause;
	
	javax.swing.Timer t;
	
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
		simPause = new JLabel();
		simPause.setLayout(null);
		simPause.setOpaque(false);
		simPause.setLocation(0, 0); //upper right
		simPause.setText("Running");
		simPause.setSize(MonitorPanel.WIDTH, 20);
		add(simPause);
		
		/**Organism Info Notification*/
		simObjInfo = new JTextArea();
		simObjInfo.setLayout(null);
		simObjInfo.setOpaque(false);
		simObjInfo.setLocation(0, simPause.getHeight());
		simObjInfo.setText(" No Object Selected");
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
		
		t = new javax.swing.Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(simulation.isPaused()){
					simPause.setText(" Paused");
				}
				else{
					simPause.setText(" Running");
				}
				repaint();
			}
		});
		
		start();
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

	@Override
	public void run() {
	}
}
