package Frame;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("all")
public class PercentagePanel extends JPanel{
	public final static int WIDTH = OptionsPanel.WIDTH;
	public final static int HEIGHT = GUI.HEIGHT - OptionsPanel.HEIGHT;
	
	private JLabel header1;
	private JLabel header2;
	private JLabel percentage;
	
	public PercentagePanel(GridPanel simulation) {
		
		//initial JPanel settings
		setLayout(null);
		setFocusable(true);
		setLocation(MonitorPanel.WIDTH + GenerationPanel.WIDTH*2, OptionsPanel.HEIGHT);
		setSize(PercentagePanel.WIDTH, PercentagePanel.HEIGHT);
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		/** Header */
		header1 = new JLabel();
		header1.setLayout(null);
		header1.setText("Percentage of the generation");
		header1.setSize(200, 15);
		header1.setLocation(10, 5);
		add(header1);
		header2 = new JLabel();
		header2.setLayout(null);
		header2.setText("that is simulated");
		header2.setSize(200, 15);
		header2.setLocation(10, 20);
		add(header2);
		
		/** Percentage */
		percentage = new JLabel();
		percentage.setLayout(null);
		percentage.setText("0.0%");
		percentage.setSize(200,50);
		percentage.setFont(new Font("sansserif", Font.BOLD, 50));
		percentage.setLocation(10, 45);
		add(percentage);
	}
	
	public void updatePercentage(double perc){
		perc *= 100;
		long x = (long)(perc*10);
	    double y = (double)x/10;
		percentage.setText(y + "%");
	}
}
