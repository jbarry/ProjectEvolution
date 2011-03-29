package Frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Interactive.Food;
import Interactive.Organism;

@SuppressWarnings("all")
public class GenerationPanel extends JPanel{
	public final static int WIDTH = OptionsPanel.WIDTH;
	public final static int HEIGHT = GUI.HEIGHT - OptionsPanel.HEIGHT;
	
	private JLabel genTitle;
	
	public static JTextArea currentGeneration;
	
	private JTextArea pastGenerations;
	private JTextArea pastGenStats;
	
	private JButton stopGenerationOrTrial;
	private JButton preprocess;
	private JButton jumpBack;
	
	private ButtonGroup stopSelection;
	private JRadioButton stopGenButton;
	private JRadioButton stopTrialButton;
	
	private GridPanel sim;
	
	private boolean canStopTheSim;
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public GenerationPanel(final GridPanel simulation, final GUI gui){
		//initial JPanel settings
		sim = simulation;
		canStopTheSim = true;
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
		
		//Current Generation number
		currentGeneration= new JTextArea(100,100);
		currentGeneration.setOpaque(false);
		currentGeneration.setLayout(null);
		currentGeneration.setEditable(false);
		currentGeneration.setSize(MonitorPanel.WIDTH, 40);
		currentGeneration.setText("  Current Generation: " + sim.generationNum);
		currentGeneration.append("\n" + "  Current Trial: " + sim.trialNum);
		currentGeneration.setLocation(0,21);
		
		//Current Trial Number
		pastGenerations= new JTextArea(3, 100);
		pastGenerations.setOpaque(false);
		pastGenerations.setLayout(null);
		pastGenerations.setEditable(false);
		pastGenerations.setSize(MonitorPanel.WIDTH, GenerationPanel.HEIGHT-40);
		pastGenerations.setText("\n" + "  Past Generation's Average Fitness:");
		pastGenerations.setLocation(0,42);
		
		//Text area for past generation information
		pastGenStats = new JTextArea();
		
		//Make the text area for past generation information scroll-able
		JScrollPane scrollPane = new JScrollPane(pastGenStats);
		JPanel pastGenPanel = new JPanel();
		pastGenPanel.setOpaque(false);
		pastGenPanel.setLayout(new GridLayout());
		pastGenPanel.setFocusable(true);
		pastGenPanel.setSize(200, 70);
		pastGenPanel.setLocation(5,75);
		
		pastGenPanel.add(scrollPane);
		
		/**
		 * This button stops either the current generation or trial,
		 * depending on what the user chooses. It also resumes the
		 * simulation after stoppage.
		 */
		stopGenerationOrTrial = new JButton();
		stopGenerationOrTrial.setLayout(null);
		stopGenerationOrTrial.setEnabled(false);
		stopGenerationOrTrial.setText("Stop");
		

		
		//Need to add nested components before parent components.
		add(pastGenPanel);
		pastGenPanel.add(scrollPane);
		
		//Add the label and text areas.
		add(genTitle);
		add(currentGeneration);
		add(pastGenerations);
		
		//ActionListener for the stopGenerationOrTrial button
		ActionListener sG = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!canStopTheSim){
					gui.toggleAllPauses(true);
				}
				eventStopGenerationOrTrial(simulation);
			}
		};
		stopGenerationOrTrial.addActionListener(sG);
		
		/**
		 * Button for deciding to preprocess.
		 * Simulation must be paused.
		 */
		
		preprocess = new JButton();
		preprocess.setLayout(null);
		preprocess.setEnabled(false);
		preprocess.setText("Preprocess");
		
		//Listener for preprocess button
		ActionListener PP = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				if(simulation.isPaused()){
				simPreprocess(arg0,simulation);
				}
				else{
					JOptionPane.showMessageDialog(simulation, "Simulation must be paused", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		
		preprocess.addActionListener(PP);
		
		/**
		 * Button for jumping back generations
		 * Simulation must be paused
		 */
		jumpBack = new JButton();
		jumpBack.setLayout(null);
		jumpBack.setEnabled(false);
		jumpBack.setText("Go Back Generations");
		ActionListener JB= new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				if(simulation.getGenerationNum()<2){
					JOptionPane.showMessageDialog(simulation, "One generation must have passed before reverting", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(simulation.isPaused()){
					JOptionPane.showMessageDialog(simulation, "Any unsaved information will be lost. " +
							"\nThis will revert back to the organisms of the generation you specify. " +
							"\nAll generations after this will be discarded.", "Warning!", JOptionPane.WARNING_MESSAGE);
					goBack(simulation,arg0);
					}
					else{
						JOptionPane.showMessageDialog(simulation, "Simulation must be paused", "Error", JOptionPane.ERROR_MESSAGE);
					}
			}
		};
		
		jumpBack.addActionListener(JB);
		
		
		//Add radio button for stopping either trial or generation
		stopGenButton = new JRadioButton("Generation");
		stopGenButton.setMnemonic(KeyEvent.VK_G);
		stopGenButton.setActionCommand("Generation");
		stopGenButton.setSelected(true);

	    stopTrialButton = new JRadioButton("Trial");
	    stopTrialButton.setMnemonic(KeyEvent.VK_T);
	    stopTrialButton.setActionCommand("Trial");
	    
	    //Create button group to contain radio buttons
	    stopSelection = new ButtonGroup();
	    stopSelection.add(stopGenButton);
	    stopSelection.add(stopTrialButton);
	    
	    /**
	     * Put radio buttons and the "stopGenerationOrTrial" button
	     * in JPanel to look nicer.
	     */
	    JPanel radioPanel = new JPanel();
	    radioPanel.setOpaque(false);
	    radioPanel.setFocusable(true);
	    radioPanel.setSize(180, 130);
	    radioPanel.setLocation(210,20);
	    radioPanel.setLayout(new GridLayout(5, 1));
	    radioPanel.add(stopGenButton);
	    radioPanel.add(stopTrialButton);
	    radioPanel.add(stopGenerationOrTrial);
	    radioPanel.add(preprocess);
	    radioPanel.add(jumpBack);
	    radioPanel.setBorder(BorderFactory.createTitledBorder(
	            BorderFactory.createEtchedBorder(), "Select Option to Stop"));
	    
	    add(radioPanel);
	}
	
	private boolean goBack(GridPanel simulation, ActionEvent e) {
		boolean userCancel = true;
		JTextField numOrganisms = new JTextField();
		JPanel jP = new JPanel();
		jP.setLayout(new GridLayout(2, 2, 5, 5));
		jP.add(new JLabel("What Generation would you like to revert to?"));
		jP.add(numOrganisms);

		Object[] msg = { jP };

		JOptionPane op = new JOptionPane(msg, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION, null, null);

		JDialog dialog = op.createDialog("Reverting");
		dialog.setVisible(true);
		
		int result = 1;
		try {
			result = ((Integer) op.getValue()).intValue();
		} catch (NullPointerException q) {

		}

		if (result == JOptionPane.OK_OPTION) {
			userCancel = false;
			try {
				int x = Integer.parseInt(numOrganisms.getText());
				if (x <= 0) {
					JOptionPane.showMessageDialog(simulation,
							"Enter a positive integer", "Error",
							JOptionPane.INFORMATION_MESSAGE);
					// try again
					goBack(simulation,e);
				}
				else if(x > simulation.getGenerationNum()-1){
					JOptionPane.showMessageDialog(simulation,
							"Enter a number less than " + (simulation.getGenerationNum()-1), "Error",
							JOptionPane.INFORMATION_MESSAGE);
					// try again
					goBack(simulation,e);
				}
				else {
					// the generation to go back to.
					simulation.revert(x);
					JOptionPane.showMessageDialog(simulation,
							"Reverting complete, resume the simulation to continue.", "Success!",
							JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(simulation, "Enter a valid integer",
						"Error", JOptionPane.INFORMATION_MESSAGE);
				// try again
				simPreprocess(e,simulation);
			}
		}
		
		return userCancel;
		
	}

	private boolean simPreprocess(ActionEvent e, GridPanel simulation){
		boolean userCancel = true;
		JTextField numOrganisms = new JTextField();
		JPanel jP = new JPanel();
		jP.setLayout(new GridLayout(2, 2, 5, 5));
		jP.add(new JLabel("Number of generation to pre-process(Will take take time, can not be stopped):"));
		jP.add(numOrganisms);

		Object[] msg = { jP };

		JOptionPane op = new JOptionPane(msg, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION, null, null);

		JDialog dialog = op.createDialog("Pre-Process");
		dialog.setVisible(true);
		
		int result = 1;
		try {
			result = ((Integer) op.getValue()).intValue();
		} catch (NullPointerException q) {

		}

		if (result == JOptionPane.OK_OPTION) {
			userCancel = false;
			try {
				int x = Integer.parseInt(numOrganisms.getText());
				if (x <= 0) {
					JOptionPane.showMessageDialog(simulation,
							"Enter a positive integer", "Error",
							JOptionPane.INFORMATION_MESSAGE);
					// try again
					simPreprocess(e,simulation);
				} else {
					// the number of generations.
					simulation.preProcess(x);
					JOptionPane.showMessageDialog(simulation,
							"Pre-Processing complete, resume the simulation to continue.", "Success!",
							JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(simulation, "Enter a valid integer",
						"Error", JOptionPane.INFORMATION_MESSAGE);
				// try again
				simPreprocess(e,simulation);
			}
		}
		
		return userCancel;
	}
	
	private void eventStopGenerationOrTrial(GridPanel simulation) {
		if(canStopTheSim){
			stopGenerationOrTrial.setEnabled(false);
			stopGenButton.setEnabled(false);
			stopTrialButton.setEnabled(false);
			if(genIsSelected()){
				stopGenerationOrTrial.setText("Wait for gen. to end");
			}
			else{
				stopGenerationOrTrial.setText("Wait for trial to end");
			}
			canStopTheSim = false;
		}
		else{
			stopGenerationOrTrial.setText("Stop");
			canStopTheSim = true;
			stopGenButton.setEnabled(true);
			stopTrialButton.setEnabled(true);
			newTrial();
			simulation.getTimer().start();
			simulation.repaint();
		}
	}
	
	public void enableButtons(){
		stopGenerationOrTrial.setEnabled(true);
		jumpBack.setEnabled(true);
		preprocess.setEnabled(true);
	}
	
	public void enableResumeSimulation(){
		stopGenerationOrTrial.setEnabled(true);
		stopGenerationOrTrial.setText("Resume Simulation");
	}
	
	public boolean resumeHasNotBeenClicked(){
		return canStopTheSim;
	}
	
	public boolean genIsSelected(){
		if(stopGenButton.isSelected()){
			return true;
		}
		return false;
	}
	
	public void resetGenInformation(){
		pastGenStats.setText("");
		canStopTheSim = true;
		stopGenButton.setEnabled(true);
		stopTrialButton.setEnabled(true);
		stopGenerationOrTrial.setText("Stop");
		stopGenButton.setSelected(true);
		
		currentGeneration.setText("  Current Generation: " + sim.generationNum);
		currentGeneration.append("\n" + "  Current Trial: " + sim.trialNum);
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
	}
	
	public void addGeneration(){
		if(pastGenStats.getText().equals("")){
			pastGenStats.append(" Generation " + (sim.generationNum-1) + ": " + sim.lastAvg);
		}
		else{
			pastGenStats.append("\n" + " Generation " + (sim.generationNum-1) + ": " + sim.lastAvg);
		}
		
	}
	
	public void removeGenerations(int generation){
		pastGenStats.replaceRange("", generation*34-35 , pastGenStats.getText().length());

	}

}
