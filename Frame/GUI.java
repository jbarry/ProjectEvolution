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
import java.awt.FileDialog;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

import Interactive.*;

/**
 * This class will host and initialize all JPanels and JMenuItems. It will NOT
 * perform any game logic.
 */
@SuppressWarnings("all")
public class GUI {
	// ------------------------------------------------------------------------------------
	// --globals--
	// ------------------------------------------------------------------------------------
	public final static int WIDTH = 800;
	public final static int HEIGHT = 600;

	private JFrame jframe;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu helpMenu;

	private JMenuItem newSimulation;
	private JMenuItem saveGenes;
	private JMenuItem loadGenes;
	private JMenuItem pause;
	private JMenuItem exitApplication;
	private JMenuItem about;

	private OptionsPanel optionsPanel;
	private Chromosome chromosome;
	private Organism organism;
	private GridPanel simulation;
	private MonitorPanel monitorPanel;
	public static GenerationPanel genPanel;

	// ------------------------------------------------------------------------------------
	// --constructors--
	// ------------------------------------------------------------------------------------
	public GUI() {
		/** Create and set jframe attributes */
		jframe = new JFrame();
		jframe.setLayout(null);
		jframe.setAlwaysOnTop(false);
		jframe.setSize(GUI.WIDTH, GUI.HEIGHT);
		jframe.setVisible(true);
		jframe.setFocusable(true);
		jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		jframe.setTitle("Project Evolution");

		KeyListener frameKeyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				int key = keyEvent.getKeyCode();
				switch (key) {
				// p-key
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

		MouseListener frameMouseListener = new MouseListener() {
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

		/** Create Game Grid Panel */
		simulation = new GridPanel(this);
		jframe.add(simulation);

		/** Create Options Panel */
		optionsPanel = new OptionsPanel(this, simulation);
		jframe.add(optionsPanel);

		/** Create Monitor Panel */
		monitorPanel = new MonitorPanel(simulation);
		jframe.add(monitorPanel);

		/** Create Generation Panel */
		genPanel = new GenerationPanel(simulation, this);
		jframe.add(genPanel);

		/** Create Menu */
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");

		menuBar.add(fileMenu);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(helpMenu);

		jframe.setJMenuBar(menuBar);

		// new simulation option
		newSimulation = new JMenuItem("New Simulation");
		newSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// initialize GridPanel.
				if (!getGridPanelData(e)) {
					pause.setEnabled(true);
					genPanel.enableStopButton();
					saveGenes.setEnabled(true);
					loadGenes.setEnabled(true);
					simulation.initialize();
					simulation.start();
				}
			}
		});
		fileMenu.add(newSimulation);

		// save genes to text file
		saveGenes = new JMenuItem("Save Genes");
		saveGenes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jframe.setAlwaysOnTop(false);
				optionsPanel.toggleEnabled(false);
				optionsPanel.eventPause(simulation);				
				saveGene(jframe);				
				pause.setEnabled(false);
				optionsPanel.toggleEnabled(true);
			}
		});
		fileMenu.add(saveGenes);
		saveGenes.setEnabled(false);
		
		// load genes from text file
		loadGenes = new JMenuItem("Load Genes");
		loadGenes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jframe.setAlwaysOnTop(false);
				optionsPanel.toggleEnabled(false);
				optionsPanel.eventPause(simulation);				
				loadGene(jframe);				
				pause.setEnabled(false);
				optionsPanel.toggleEnabled(true);
			}
		});
		fileMenu.add(loadGenes);
		loadGenes.setEnabled(false);
		
		// pause/resume option
		pause = new JMenuItem("Pause/Resume");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionsPanel.eventPause(simulation);
			}
		});
		fileMenu.add(pause);
		pause.setEnabled(false);

		// exit option
		exitApplication = new JMenuItem("Exit");
		exitApplication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmExit();
			}

		});
		fileMenu.add(exitApplication);

		// about option
		about = new JMenuItem("About...");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane
				.showMessageDialog(
						jframe,
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

		/** set additional frame attributes */
		jframe.setLocationRelativeTo(null);
		jframe.setResizable(false);
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				confirmExit();
			}
		});

	}

	// ------------------------------------------------------------------------------------
	// --Getters/Setters--
	// ------------------------------------------------------------------------------------
	public JFrame getContainer() {
		return jframe;
	}

	// ------------------------------------------------------------------------------------
	// --accessors/mutators--
	// ------------------------------------------------------------------------------------
	/**
	 * Sets initial game state of the Panel. Note: This does NOT handle game
	 * state changes.
	 * 
	 * @param e
	 *            an ActionEvent instance
	 */
	private boolean getGridPanelData(ActionEvent e) {
		// textbox for # organisms inquiry
		boolean userCancel = true;
		jframe.setAlwaysOnTop(false);
		JTextField numOrganisms = new JTextField();
		JPanel jP = new JPanel();
		jP.setLayout(new GridLayout(2, 2, 5, 5));
		jP.add(new JLabel("Number of Organisms (0-1000):"));
		jP.add(numOrganisms);

		Object[] msg = { jP };

		JOptionPane op = new JOptionPane(msg, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION, null, null);

		JDialog dialog = op.createDialog("New Simulation");
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
				if (x < 0) {
					JOptionPane.showMessageDialog(jframe,
							"Enter a positive integer (0-1000)", "Error",
							JOptionPane.INFORMATION_MESSAGE);
					// try again
					getGridPanelData(e);
				} else if (x > 1000) {
					JOptionPane.showMessageDialog(jframe,
							"Enter an integer less than 1000", "Error",
							JOptionPane.INFORMATION_MESSAGE);
					// try again
					getGridPanelData(e);
				} else {
					// the number of organisms given via user-input.
					OptionsPanel.numOrganisms = x;
					optionsPanel.toggleEnabled(true);
					pause.setEnabled(false);
				}
			} catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(jframe, "Enter a valid integer",
						"Error", JOptionPane.INFORMATION_MESSAGE);
				// try again
				getGridPanelData(e);
			}
		}
		jframe.setAlwaysOnTop(true);
		dialog.dispose();
		return userCancel;
	}

	public void enableJMenuItemPause() {
		pause.setEnabled(true);
	}
	
	public void enableJMenuItemSaveGenes(){
		saveGenes.setEnabled(true);
	}
	
	public void toggleAllPauses(boolean b){
		pause.setEnabled(b);
		optionsPanel.togglePause(b);
	}

	
	public void enableStopGenButton(){
		genPanel.enableStopButton();
	}

	private void confirmExit() {
		// Display confirm dialog
		int confirmed = JOptionPane.showConfirmDialog(jframe, "Exit Graph?",
				"Confirm", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);

		// Close if user confirmed
		if (confirmed == JOptionPane.YES_OPTION) {
			// Close frame and exit program
			jframe.dispose();
			System.exit(0);
		}
	}

	private void saveGene(JFrame f) {

		LinkedList<Organism> tempOrgs;
		tempOrgs = simulation.getOrganisms();
		String tempChrom = "";

		for (int i = 0; i < tempOrgs.size(); i++) {
			for (int k = 0; k < tempOrgs.get(i).getChromosome().size(); k++) {
				ArrayList<String> tempArray = tempOrgs.get(i).getChromosome()
				.getGene(k).makeStringArray();
				for (int j = 0; j < tempArray.size(); j++) {
					tempChrom += tempArray.get(j);
				}
			}
			tempChrom += " ";
		}

		Writer writer = null;

		try {
			FileDialog sd = new FileDialog(f);
			sd.setMode(FileDialog.SAVE);
			sd.setVisible(true);
			sd.setAlwaysOnTop(true);
			sd.setFocusable(true);
			File file = new File(sd.getFile()+".txt");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(tempChrom);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		jframe.setAlwaysOnTop(true);	
	}
	
	public void loadGene(JFrame f){
		Reader reader = null;

		try {
			FileDialog ld = new FileDialog(f);
			ld.setMode(FileDialog.LOAD);
			ld.setVisible(true);
			ld.setAlwaysOnTop(true);
			ld.setFocusable(true);
			File file = new File(ld.getFile());
			reader = new BufferedReader(new FileReader(file));
			reader.read();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		jframe.setAlwaysOnTop(true);
	}

	// ------------------------------------------------------------------------------------
	// --MAIN--
	// ------------------------------------------------------------------------------------
	/**
	 * Defines main entry point for program.
	 */
	public static void main(String[] args) {
		new GUI();
	}
}