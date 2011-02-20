package Frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Interactive.Food;
import Interactive.Organism;

@SuppressWarnings("all")
public class GenerationPanel extends JPanel{
	public final static int WIDTH = OptionsPanel.WIDTH;
	public final static int HEIGHT = GUI.HEIGHT - OptionsPanel.HEIGHT;
	private JLabel genTitle;
	private JTextArea currentGeneration;
	private JTextArea pastGenerations;
	private GridPanel sim;
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public GenerationPanel(final GridPanel simulation){
		//initial JPanel settings
		sim = simulation;
		setLayout(null);
		setFocusable(true);
		setLocation(MonitorPanel.WIDTH, OptionsPanel.HEIGHT);
		setSize(GenerationPanel.WIDTH*2, GenerationPanel.HEIGHT);
		setBorder(BorderFactory.createLineBorder(Color.black));
		genTitle = new JLabel();
		genTitle.setLayout(null);
		genTitle.setOpaque(false);
		genTitle.setLocation(0, 0); //upper right
		genTitle.setText(" Generation Information");
		genTitle.setSize(MonitorPanel.WIDTH, 20);
		
		
		currentGeneration= new JTextArea(100,100);
		currentGeneration.setOpaque(false);
		currentGeneration.setLayout(null);
		currentGeneration.setEditable(false);
		currentGeneration.setSize(GenerationPanel.WIDTH, 40);
		currentGeneration.setText(" Current Generation: " + sim.generationNum);
		currentGeneration.append("\n" + " Current Trial: " + sim.trialNum);
		currentGeneration.setLocation(0,21);
		pastGenerations= new JTextArea(100,100);
		
		pastGenerations.setOpaque(false);
		pastGenerations.setLayout(null);
		pastGenerations.setEditable(false);
		pastGenerations.setSize(GenerationPanel.WIDTH*2, GenerationPanel.HEIGHT-40);
		pastGenerations.setText("\n" + " Past Generation Number-Average Fitness ");
		pastGenerations.setLocation(0,42);
		JScrollPane scrollPane = new JScrollPane(pastGenerations);
		add(genTitle);
		add(currentGeneration);
		add(pastGenerations);
	}
	
	public void newTrial(){
		currentGeneration.setText(" Current Generation: " + sim.generationNum);
		currentGeneration.append("\n" + " Current Trial: " + sim.trialNum);
		currentGeneration.setLocation(0,21);
	}
	
	public void newGeneration(){
		currentGeneration.setText(" Current Generation: " + sim.generationNum);
		currentGeneration.append("\n" + " Current Trial: " + sim.trialNum);
		currentGeneration.setLocation(0,21);
		addGeneration();
	}
	
	public void addGeneration(){
		if(sim.generationNum<60)
		pastGenerations.append("\n" + " Generation " + (sim.generationNum-1) + ": " + sim.lastAvg);
		
	}
}
