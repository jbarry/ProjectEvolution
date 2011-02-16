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
public class GenerationPanel extends JPanel{
	public final static int WIDTH = OptionsPanel.WIDTH;
	public final static int HEIGHT = GUI.HEIGHT - OptionsPanel.HEIGHT;
	private JLabel genTitle;
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public GenerationPanel(final GridPanel simulation){
		
		//initial JPanel settings
		setLayout(null);
		setFocusable(true);
		setLocation(MonitorPanel.WIDTH, OptionsPanel.HEIGHT);
		setSize(GenerationPanel.WIDTH, GenerationPanel.HEIGHT);
		setBorder(BorderFactory.createLineBorder(Color.black));
		genTitle = new JLabel();
		genTitle.setLayout(null);
		genTitle.setOpaque(false);
		genTitle.setLocation(0, 0); //upper right
		genTitle.setText(" Generation Information");
		genTitle.setSize(MonitorPanel.WIDTH, 20);
		add(genTitle);
		
	
	}
	
	public void addNewGeneration(){
		JLabel toAdd= new JLabel();
		toAdd.setLayout(null);
		toAdd.setOpaque(false);
		
	}
}
