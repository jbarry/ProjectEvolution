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

import java.applet.Applet;
import java.awt.Color;
import java.awt.Container;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.media.Manager;
import javax.media.Player;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import Interactive.*;

/**
 * This class will host and initialize all JPanels and JMenuItems. It will NOT
 * perform any game logic.
 */
@SuppressWarnings("all")
public class GUI extends Container {
	// ------------------------------------------------------------------------------------
	// --globals--
	// ------------------------------------------------------------------------------------
	public final static int WIDTH = 1024;
	public final static int HEIGHT = 768;

	private JFrame jframe;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu controlMenu;
	private JMenu helpMenu;

	private JMenuItem newSimulation;
	private JMenuItem saveGenes;
	private JMenuItem saveConfig;
	private JMenuItem loadGenes;
	private JMenuItem loadConfig;
	private JMenuItem pause;
	private JMenuItem exitApplication;
	private JMenuItem about;

	private JMenu selectMapMenu;
	private JMenuItem defaultMap;
	private JMenuItem grassMap;
	private JMenuItem sandMap;
	private JMenuItem waterMap;
	private JMenuItem moonMap;
	private JMenuItem middleEarthMap;
	private JMenuItem sawMap;
	private JMenuItem dexterMap;

	private OptionsPanel optionsPanel;
	private Chromosome chromosome;
	private Organism organism;
	private Map map;
	private Audio audio;
	private GridPanel simulation;
	private MonitorPanel monitorPanel;
	public static GenerationPanel genPanel;
	private PercentagePanel percentagePanel;

	private boolean emptySimulation = true;

	// ------------------------------------------------------------------------------------
	// --constructors--
	// ------------------------------------------------------------------------------------
	public GUI() {
		/** Handle UI override for Frame */
		UIManager ui = new UIManager();

		try {
			// Set System L&F.
			ui.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

			// allow for fancy frame and dialog boxes.
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
		} catch (UnsupportedLookAndFeelException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}

		// Gradients
		List<Object> gradients = new ArrayList<Object>(5);
		gradients.add(0.00f);
		gradients.add(0.00f);
		gradients.add(new Color(0xC1C1C1));
		gradients.add(new Color(0xFFFFFF));
		gradients.add(new Color(0x5C5D5C));

		// UI HashMap Overrides
		ui.put("Button.background", new Color(255, 255, 255));
		ui.put("Button.foreground", new Color(0, 0, 0));
		ui.put("Button.gradient", gradients);
		ui.put("Button.select", new Color(175, 175, 175));

		ui.put("Label.foreground", new Color(0, 0, 0));

		ui.put("Menu.selectionBackground", new Color(175, 175, 175));
		ui.put("MenuBar.background", new Color(220, 220, 220));
		ui.put("MenuBar.foreground", new Color(0, 0, 0));
		ui.put("MenuItem.selectionBackground", new Color(175, 175, 175));

		ui.put("OptionPane.background", new Color(175, 175, 175));
		ui.put("OptionPane.errorDialog.titlePane.background", new Color(255,
				153, 153)); // redish
		ui.put("OptionPane.questionDialog.titlePane.background", new Color(153,
				204, 153)); // greenish
		ui.put("OptionPane.warningDialog.titlePane.background", new Color(255,
				204, 103)); // yellowish

		ui.put("Panel.background", new Color(175, 175, 175));

		ui.put("RadioButton.background", new Color(175, 175, 175));
		ui.put("RadioButton.foreground", new Color(0, 0, 0));

		ui.put("RootPane.frameBorder",
				BorderFactory.createLineBorder(Color.BLACK, 2));
		ui.put("RootPane.plainDialogBorder",
				BorderFactory.createLineBorder(Color.BLACK, 2));
		ui.put("RootPane.questionDialogBorder",
				BorderFactory.createLineBorder(Color.BLACK, 2));
		ui.put("RootPane.warningDialogBorder",
				BorderFactory.createLineBorder(Color.BLACK, 2));

		ui.put("Slider.background", new Color(175, 175, 175));

		ui.put("TextArea.foreground", new Color(0, 0, 0));

		/** Create and set jframe attributes */
		jframe = new JFrame();
		jframe.setLayout(null);
		jframe.setAlwaysOnTop(true);
		jframe.setSize(GUI.WIDTH, GUI.HEIGHT);
		jframe.setVisible(true);
		jframe.setFocusable(true);
		jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		jframe.setTitle("Project Evolution");

		Container pane = jframe.getContentPane();

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
		pane.addKeyListener(frameKeyListener);

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
		pane.addMouseListener(frameMouseListener);

		map = new Map();
		audio = new Audio();
		
		simulation = new GridPanel(this);
		simulation.setBackground(new Color(230, 230, 230));
		pane.add(simulation);

		/** Create Options Panel */
		optionsPanel = new OptionsPanel(this, simulation);
		pane.add(optionsPanel);

		/** Create Monitor Panel */
		monitorPanel = new MonitorPanel(simulation);
		pane.add(monitorPanel);

		/** Create Generation Panel */
		genPanel = new GenerationPanel(simulation, this);
		pane.add(genPanel);

		/** Create Percentage Panel */
		percentagePanel = new PercentagePanel(simulation);
		pane.add(percentagePanel);

		/** Create Menu */
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		controlMenu = new JMenu("Control");
		controlMenu.setMnemonic(KeyEvent.VK_C);
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		menuBar.add(fileMenu);
		menuBar.add(controlMenu);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(helpMenu);

		jframe.setJMenuBar(menuBar);

		/** New Simulation Option */
		newSimulation = new JMenuItem("New Simulation", KeyEvent.VK_N);
		KeyStroke ctrlNKeyStroke = KeyStroke.getKeyStroke("control N");
		newSimulation.setAccelerator(ctrlNKeyStroke);
		newSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// initialize GridPanel.
				if (!getGridPanelData(e)) {
					emptySimulation = false;
					pause.setEnabled(true);
					genPanel.enableStopButton();
					saveGenes.setEnabled(true);
					saveConfig.setEnabled(true);
					loadGenes.setEnabled(true);
					loadConfig.setEnabled(true);
					simulation.initialize();
					simulation.start();
				}
			}
		});
		fileMenu.add(newSimulation);

		/** Select Custom Map */
		selectMapMenu = new JMenu("Select Map");
		selectMapMenu.setMnemonic(KeyEvent.VK_M);
		controlMenu.add(selectMapMenu);

		// Default Map
		defaultMap = new JMenuItem("Default");
		defaultMap.setText("Default");
		defaultMap.setEnabled(false);
		defaultMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableMapItems();
				map.setCurrMap(defaultMap.getText());		
				audio.stopAudio();
				defaultMap.setEnabled(false);
			}
		});
		selectMapMenu.add(defaultMap);

		// Grass Map
		grassMap = new JMenuItem("Grass");
		grassMap.setText("Grass");
		grassMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableMapItems();
				map.setCurrMap(grassMap.getText());
				audio.stopAudio();
				audio = new Audio("grass.wav");			
				audio.playAudio();
				grassMap.setEnabled(false);

			}
		});
		selectMapMenu.add(grassMap);

		// Sand Map
		sandMap = new JMenuItem("Sand");
		sandMap.setText("Sand");
		sandMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableMapItems();
				map.setCurrMap(sandMap.getText());
				audio.stopAudio();
				audio = new Audio("sand.wav");			
				audio.playAudio();
				sandMap.setEnabled(false);

			}
		});
		selectMapMenu.add(sandMap);

		// Water Map
		waterMap = new JMenuItem("Water");
		waterMap.setText("Water");
		waterMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableMapItems();
				map.setCurrMap(waterMap.getText());
				audio.stopAudio();
				audio = new Audio("water.wav");			
				audio.playAudio();
				waterMap.setEnabled(false);

			}
		});
		selectMapMenu.add(waterMap);

		// Moon Map
		moonMap = new JMenuItem("Moon");
		moonMap.setText("Moon");
		moonMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableMapItems();
				map.setCurrMap(moonMap.getText());
				audio.stopAudio();
				audio = new Audio("moon.wav");			
				audio.playAudio();
				moonMap.setEnabled(false);
			}
		});
		selectMapMenu.add(moonMap);

		// Middle Earth Map
		middleEarthMap = new JMenuItem("Middle Earth");
		middleEarthMap.setText("Middle Earth");
		middleEarthMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableMapItems();
				map.setCurrMap(middleEarthMap.getText());
				audio.stopAudio();
				audio = new Audio("middleEarth.wav");			
				audio.playAudio();
				middleEarthMap.setEnabled(false);		
			}
		});
		selectMapMenu.add(middleEarthMap);

		// Saw Map
		sawMap = new JMenuItem("Saw");
		sawMap.setText("Saw");
		sawMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableMapItems();
				map.setCurrMap(sawMap.getText());
				audio.stopAudio();
				audio = new Audio("saw.wav");			
				audio.playAudio();
				sawMap.setEnabled(false);

			}
		});
		selectMapMenu.add(sawMap);
		
		// Dexter Map
		dexterMap = new JMenuItem("Dexter");
		dexterMap.setText("Dexter");
		dexterMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableMapItems();
				map.setCurrMap(dexterMap.getText());
				audio.stopAudio();
				audio = new Audio("dexter.wav");			
				audio.playAudio();
				dexterMap.setEnabled(false);

			}
		});
		selectMapMenu.add(dexterMap);
		
		fileMenu.addSeparator();

		/** Save Genes Option */
		saveGenes = new JMenuItem("Save Genes", KeyEvent.VK_S);
		KeyStroke ctrlSKeyStroke = KeyStroke.getKeyStroke("control S");
		saveGenes.setAccelerator(ctrlSKeyStroke);
		saveGenes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!simulation.isPaused()) {
					optionsPanel.eventPause(simulation);
				}
				saveGenes();
			}
		});
		fileMenu.add(saveGenes);
		saveGenes.setEnabled(false);

		/** Save Configuration Option */
		saveConfig = new JMenuItem("Save Configuration");
		saveConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!simulation.isPaused()) {
					optionsPanel.eventPause(simulation);
				}
				saveConfig();
			}
		});
		fileMenu.add(saveConfig);
		saveConfig.setEnabled(false);

		// separator between groups of JMenuItems
		fileMenu.addSeparator();

		/** Load Genes Option */
		loadGenes = new JMenuItem("Load Genes", KeyEvent.VK_L);
		KeyStroke ctrlLKeyStroke = KeyStroke.getKeyStroke("control L");
		loadGenes.setAccelerator(ctrlLKeyStroke);
		loadGenes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!simulation.isPaused() && !emptySimulation) {
					optionsPanel.eventPause(simulation);
				}
				loadGenes();
			}
		});
		fileMenu.add(loadGenes);
		loadGenes.setEnabled(true);

		/** Load Configuration Option */
		loadConfig = new JMenuItem("Load Configuration");
		loadConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!simulation.isPaused() && !emptySimulation) {
					optionsPanel.eventPause(simulation);
				}
				loadConfig();
				simulation.start();
			}
		});
		fileMenu.add(loadConfig);
		loadConfig.setEnabled(true);

		// separator between groups of JMenuItems
		fileMenu.addSeparator();

		/** Pause/Resume Option */
		pause = new JMenuItem("Pause/Resume", KeyEvent.VK_P);
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionsPanel.eventPause(simulation);
			}
		});
		controlMenu.add(pause);
		pause.setEnabled(false);

		/** Exit Option */
		exitApplication = new JMenuItem("Exit", KeyEvent.VK_X);
		exitApplication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmExit();
			}

		});
		fileMenu.add(exitApplication);

		/** About Option */
		about = new JMenuItem("About...", KeyEvent.VK_A);
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(
								jframe,
								"Project Evolution v. 1.0\n\n"
										+ "Written By:\n"
										+ "Justin Barry, Dwight Bell, Ian Gardea, Devin Lam, and Chris Jackey\n\n"
										+ "This program will simulate basic Darwinism amongst \n"
										+ "organisms with the same initial conditions. Intelligence is \n"
										+ "represented through a Robust Gene Expression Program.",
								"About...", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(about);

		/** Set Additional Frame Attributes */
		jframe.setLocationRelativeTo(null);
		jframe.setResizable(false);
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				confirmExit();
			}
		});

		/** Apply LnF (Look and Feel) */
		SwingUtilities.updateComponentTreeUI(jframe);
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
		jP.add(new JLabel("Number of Organisms (0-500):"));
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
							"Enter a positive integer (0-500)", "Error",
							JOptionPane.INFORMATION_MESSAGE);
					// try again
					getGridPanelData(e);
				} else if (x > 500) {
					JOptionPane.showMessageDialog(jframe,
							"Enter an integer less than 500", "Error",
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

	// ------------------------------------------------------------------------------------
	// Enablers used in Options Panel
	// ------------------------------------------------------------------------------------

	public void enableJMenuItemPause() {
		pause.setEnabled(true);
	}

	public void enableJMenuItemSaveGenes() {
		saveGenes.setEnabled(true);
	}

	public void enableJMenuItemSaveConfig() {
		saveConfig.setEnabled(true);
	}

	public void toggleAllPauses(boolean b) {
		pause.setEnabled(b);
		optionsPanel.togglePause(b);
	}

	public void enableStopGenButton() {
		genPanel.enableButtons();
	}

	public void toggleEmptySimulation(boolean b) {
		if (b) {
			emptySimulation = true;
		} else {
			emptySimulation = false;
		}
	}

	public boolean isSimulationEmpty() {
		if (emptySimulation) {
			return true;
		}
		return false;
	}

	public void updatePercentage(double d) {
		percentagePanel.updatePercentage(d);
	}

	public Map getMap() {
		return map;
	}

	public void enableMapItems() {
		defaultMap.setEnabled(true);
		grassMap.setEnabled(true);
		sandMap.setEnabled(true);
		waterMap.setEnabled(true);
		moonMap.setEnabled(true);
		middleEarthMap.setEnabled(true);
	}

	private void confirmExit() {
		// Display confirm dialog
		int confirmed = JOptionPane.showConfirmDialog(jframe,
				"Exit Simulation?", "Confirm", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);

		// Close if user confirmed
		if (confirmed == JOptionPane.YES_OPTION) {
			// Close frame and exit program
			jframe.dispose();
			System.exit(0);
		}
	}

	private void saveGenes() {

		LinkedList<Organism> tempOrgs;
		tempOrgs = simulation.getOrganisms();
		int geneLength = tempOrgs.get(0).getChromosome().getGene(0).size();
		int numGenes = tempOrgs.get(0).getChromosome().size();

		String fileContents = "VALID_GEP_GENE_FILE" + " " + geneLength + " "
				+ numGenes + " ";

		for (int i = 0; i < tempOrgs.size(); i++) {
			for (int k = 0; k < tempOrgs.get(i).getChromosome().size(); k++) {
				ArrayList<String> tempArray = tempOrgs.get(i).getChromosome()
						.getGene(k).makeStringArray();
				for (int j = 0; j < tempArray.size(); j++) {
					fileContents += tempArray.get(j);
				}
			}
			fileContents += " ";
		}

		Writer writer = null;

		try {
			FileDialog sd = new FileDialog(new JFrame(), "Save Genes",
					FileDialog.SAVE);
			sd.setVisible(true);
			sd.setAlwaysOnTop(true);
			sd.setFocusable(true);
			File file = new File(sd.getDirectory(), sd.getFile() + ".txt");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(fileContents);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
					optionsPanel.eventPause(simulation);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void saveConfig() {
		LinkedList<Organism> tempOrgs;
		tempOrgs = simulation.getOrganisms();
		int geneLength = tempOrgs.get(0).getChromosome().getGene(0).size();
		int numGenes = tempOrgs.get(0).getChromosome().size();

		String fileContents = "";

		fileContents += "VALID_GEP_CONFIG_FILE" + " " + geneLength + " "
				+ numGenes + " " + tempOrgs.get(0).getMaxHealth() + "&";

		for (int i = 0; i < tempOrgs.size(); i++) {
			for (int k = 0; k < tempOrgs.get(i).getChromosome().size(); k++) {
				ArrayList<String> tempArray = tempOrgs.get(i).getChromosome()
						.getGene(k).makeStringArray();
				for (int j = 0; j < tempArray.size(); j++) {
					fileContents += tempArray.get(j);
				}
			}
			fileContents += "@" + simulation.getOrganismData(i) + "&";
		}

		Writer writer = null;

		try {
			FileDialog sd = new FileDialog(new JFrame(), "Save Configuration",
					FileDialog.SAVE);
			sd.setVisible(true);
			sd.setAlwaysOnTop(true);
			sd.setFocusable(true);
			File file = new File(sd.getDirectory(), sd.getFile() + ".txt");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(fileContents);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
					optionsPanel.eventPause(simulation);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadGenes() {
		BufferedReader reader = null;
		String tempChars;
		String[] fileContents;
		LinkedList<Organism> population = new LinkedList<Organism>();
		int geneLength = 0;
		int numGenes = 0;

		try {
			// Create load dialog box
			FileDialog ld = new FileDialog(new JFrame(), "Load Genes",
					FileDialog.LOAD);
			ld.setVisible(true);
			ld.setAlwaysOnTop(true);
			ld.setFocusable(true);
			if (ld.getFile() != null) {
				File file = new File(ld.getDirectory(), ld.getFile());
				reader = new BufferedReader(new FileReader(file));
				tempChars = reader.readLine();
				// Split file that was read in using spaces as a delimiter
				fileContents = tempChars.split(" ");
				// Confirm that the file loaded was a valid gep gene file
				if (fileContents[0].equals("VALID_GEP_GENE_FILE")) {
					emptySimulation = false;
					// traverse list of Chromosomes in file, parse Gene
					// info/assign to organisms, create population
					for (int i = 1; i < fileContents.length; i++) {
						if (i <= 2) {
							if (i == 1) {
								// Set gene length equal to the first element
								// after validation
								geneLength = Integer.parseInt(fileContents[i]);
							}
							if (i == 2) {
								// Set the number of genes equal to the second
								// element after validation
								numGenes = Integer.parseInt(fileContents[i]);
							}
						} else {
							Organism individOrganism = new Organism(500.0,
									numGenes, i, 100);
							LinkedList<Gene> tempGeneList = new LinkedList<Gene>();
							// traverse individual Chromosome to split genes at
							// geneLength
							for (int j = 0; j < numGenes; j++) {
								// Traverse organism's chromosome and split into
								// individual genes at intervals of gene length
								String tempGene = fileContents[i].substring(
										(j * geneLength),
										((j * geneLength) + (geneLength)))
										.trim();
								// Create a temporary array of characters that
								// the
								// temporary gene will be converted and stored
								// into
								char[] tempCharGenes = tempGene.toCharArray();
								// Create a temporary Linked List of type
								// Character
								// to store the individual character arrays
								LinkedList<Character> tempGeneArray = new LinkedList<Character>();
								// For loop to add each character array to the
								// Linked List of type Character
								for (int h = 0; h < tempGene.length(); h++) {
									tempGeneArray.add(tempCharGenes[h]);
								}
								Gene g = new Gene(tempGeneArray);
								tempGeneList.add(g);
							}
							// Constructor to create a new Gene with the Linked
							// List of type Character
							individOrganism.setChromosome(new Chromosome(
									tempGeneList));
							// Finally add the new organism to the Linked List
							// of
							// type sOrganism: population.
							population.add(individOrganism);
						}
					}
					/*
					 * Uncomment the following code to print the chromosomes of
					 * each organism
					 */
					// for(int h = 0 ; h < population.size(); h++){
					// System.out.println("Organism " + h);
					// for (int o = 0 ; o <
					// population.get(h).getChromosome().size() ; o++){
					// System.out.println(population.get(h).getChromosome().getGene(o).getList());
					// }
					// }
					simulation.initializeFromGeneFile(population);
				} else {
					JOptionPane.showMessageDialog(jframe,
							"Please select a valid saved genes file.", "Error",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (simulation.isPaused() && !emptySimulation) {
			optionsPanel.eventPause(simulation);
		}
	}

	public void loadConfig() {
		BufferedReader reader = null;
		String tempChars;
		String[] entireFileContent;
		String[] reqInfo;
		String[] organismData;
		LinkedList<Organism> population = new LinkedList<Organism>();
		int geneLength = 0;
		int numGenes = 0;
		double maxHealth = 0;

		try {
			// Create load dialog box
			FileDialog ld = new FileDialog(new JFrame(), "Load Configuration",
					FileDialog.LOAD);
			ld.setVisible(true);
			ld.setAlwaysOnTop(true);
			ld.setFocusable(true);
			if (ld.getFile() != null) {
				File file = new File(ld.getDirectory(), ld.getFile());
				reader = new BufferedReader(new FileReader(file));
				tempChars = reader.readLine();

				// Split file contents into required info and individual
				// organisms and their accompanying data
				entireFileContent = tempChars.split("&");
				// Split the first index of file contents at every space
				reqInfo = entireFileContent[0].split(" ");

				// Confirm that the file loaded was a valid gep configuration
				// file
				if (reqInfo[0].equals("VALID_GEP_CONFIG_FILE")) {
					emptySimulation = false;
					// Gene length for organisms
					geneLength = Integer.parseInt(reqInfo[1]);
					// Number of genes in each organisms' chromosome
					numGenes = Integer.parseInt(reqInfo[2]);
					// Max health for all organisms
					maxHealth = Double.parseDouble(reqInfo[3]);

					// Traverse entire contents of file
					for (int i = 1; i < entireFileContent.length; i++) {
						// Split contents of file into individual organism
						// contents
						String totalOrganismContents = entireFileContent[i];
						// Split individual organism contents into two parts
						// (Chromosome and Data)
						String[] tempChrom = totalOrganismContents.split("@");
						// Set organism chromosome equal to the first index of
						// the tempChrom array
						String organismChrom = tempChrom[0];
						// Split the second index of the tempChrom array into
						// the organism's individual data pieces
						organismData = tempChrom[1].split(" ");
						Organism individOrganism = new Organism(500.0,
								numGenes, i, 100);
						// Create temporary Linked List of type Gene to store
						// each gene in a given organisms' Chromosome
						LinkedList<Gene> tempGeneList = new LinkedList<Gene>();
						// Traverse organism's chromosome and split into
						// individual genes at intervals of gene length
						for (int l = 0; l < numGenes; l++) {
							String tempGene = organismChrom.substring(
									(l * geneLength),
									((l * geneLength) + (geneLength))).trim();
							// Create a temporary array of characters that the
							// temporary gene will be converted and stored into
							char[] tempCharGenes = tempGene.toCharArray();
							// Create a temporary Linked List of type Character
							// to store the individual character arrays
							LinkedList<Character> tempGeneArray = new LinkedList<Character>();
							// For loop to add each character array to the
							// Linked List of type Character
							for (int h = 0; h < tempGene.length(); h++) {
								tempGeneArray.add(tempCharGenes[h]);
							}
							// Constructor to create a new Gene with the Linked
							// List of type Character
							Gene g = new Gene(tempGeneArray);
							// Adds the newly created Gene to the Linked List of
							// type Gene
							tempGeneList.add(g);
						}
						// Sets the Chromosome of the given organism to be the
						// Linked List of type Gene
						individOrganism.setChromosome(new Chromosome(
								tempGeneList));

						// Finally add the new organism to the Linked List of
						// type sOrganism: population.
						population.add(individOrganism);

						// Set individual organisms' data including Id, Health,
						// Fitness, X-Location, Y-Location, Healthy Eats, Poison
						// Eats, Eat Fails, Num Attacked, Num Pushed, and Total
						// Scans
						for (int m = 0; m < organismData.length; m++) {
							if (m == 0) {
								individOrganism.setId(Integer
										.parseInt(organismData[m]));
							}
							if (m == 1) {
								individOrganism.setHealth(Double
										.parseDouble(organismData[m]));
							}
							if (m == 2) {
								individOrganism.setFitness(Double
										.parseDouble(organismData[m]));
							}
							if (m == 3) {
								individOrganism.getLocation().setX(
										Integer.parseInt(organismData[m]));
							}
							if (m == 4) {
								individOrganism.getLocation().setY(
										Integer.parseInt(organismData[m]));
							}
							if (m == 5) {
								individOrganism.setHealthyEat(Integer
										.parseInt(organismData[m]));
							}
							if (m == 6) {
								individOrganism.setPoisonEat(Integer
										.parseInt(organismData[m]));
							}
							if (m == 7) {
								individOrganism.setEatFail(Integer
										.parseInt(organismData[m]));
							}
							if (m == 8) {
								individOrganism.setNumAttacked(Integer
										.parseInt(organismData[m]));
							}
							if (m == 9) {
								individOrganism.setNumPushed(Integer
										.parseInt(organismData[m]));
							}
							if (m == 10) {
								individOrganism.setTotalScans(Integer
										.parseInt(organismData[m]));
							}
						}
					}
					/*
					 * Uncomment the following lines of code to print the
					 * chromosomes and data of all of the organisms that have
					 * been loaded from the configuration file
					 */
					// for (int h = 0; h < population.size(); h++) {
					// System.out.println("Organism " + (h + 1));
					// for (int o = 0; o < population.get(h).getChromosome()
					// .size(); o++) {
					// System.out.println(population.get(h).getChromosome()
					// .getGene(o).getList());
					//
					// }
					// System.out.println("Id : " + population.get(h).getId());
					// System.out.println("Health: "
					// + population.get(h).getHealth());
					// System.out.println("Fitness: "
					// + population.get(h).getFitness());
					// System.out.println("X location: "
					// + population.get(h).getLocation().getX());
					// System.out.println("Y Location: "
					// + population.get(h).getLocation().getY());
					// System.out.println("Healthy Eats: "
					// + population.get(h).getHealthEat());
					// System.out.println("Poison Eats: "
					// + population.get(h).getPoisonEat());
					// System.out.println("Eat Fails: "
					// + population.get(h).getEatFail());
					// System.out.println("Num Attacked: "
					// + population.get(h).getNumAttacked());
					// System.out.println("Num Pushed: "
					// + population.get(h).getNumPushed());
					// System.out.println("Total Scans: "
					// + population.get(h).getTotalScans());
					// System.out.println();
					// }
					// simulation.initializeFromConfigFile(population);
				} else {
					JOptionPane.showMessageDialog(jframe,
							"Please select a valid saved configuration file.",
							"Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (simulation.isPaused() && !emptySimulation) {
			optionsPanel.eventPause(simulation);
		}
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