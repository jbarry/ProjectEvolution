package Frame;
import Evolution.GEP;
import Interactive.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalSliderUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;

@SuppressWarnings("all")
public class OptionsPanel extends JPanel implements Runnable{
	public final static int WIDTH = 200;
	public final static int HEIGHT = 400;

	public static int numOrganisms = 0;

	private JLabel numOrgsLbl;
	private JLabel orgSizeLbl;
	private JLabel mutationRateLbl;
	private JLabel rotationRateLbl;
	private JLabel selectionRateLbl;
	private JLabel crossoverRateLbl;
	private JLabel timeStepSelectionLbl;

	private JTextField numOrgsTxtBox;
	private JTextField orgWidthTxtBox;
	private JTextField orgHeightTxtBox;
	private JTextField mutationRateTxtBox;
	private JTextField rotationRateTxtBox;
	private JTextField selectionRateTxtBox;
	private JTextField crossoverRateTxtBox;

	private JSlider timeStepSelectionSldr;

	private JButton start;
	private JButton pause;

	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public OptionsPanel(final GUI gui, final GridPanel simulation){
		run();

		/** initial JPanel settings */
		setLayout(null);
		setLocation(0,0);
		setSize(OptionsPanel.WIDTH, OptionsPanel.HEIGHT);
		setBorder(BorderFactory.createLineBorder(Color.black));

		//allow for text limitations in JTextFields
		class JTextFieldLimit extends PlainDocument {
			private int limit;
			// optional uppercase conversion
			private boolean toUppercase = false;

			JTextFieldLimit(int limit) {
				super();
				this.limit = limit;
			}

			JTextFieldLimit(int limit, boolean upper) {
				super();
				this.limit = limit;
				toUppercase = upper;
			}

			public void insertString
			(int offset, String str, AttributeSet attr)
			throws BadLocationException {
				if (str == null) return;

				if ((getLength() + str.length()) <= limit) {
					if (toUppercase) str = str.toUpperCase();
					super.insertString(offset, str, attr);
				}
			}
		}

		/** Header */
		JLabel header = new JLabel();
		header.setLayout(null);
		header.setText("Options");
		header.setSize(80,30);
		header.setFont(new Font("sansserif", Font.BOLD, 20));
		header.setLocation(10, 0);
		add(header);

		/** Start Button */
		start = new JButton();
		start.setLayout(null);
		start.setText("Start");
		start.setSize(90,20);
		start.setLocation(100, 35);
		add(start);

		ActionListener s = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//reset all labels
				numOrgsLbl.setText("Population Size:");
				orgSizeLbl.setText("Organism Size: w x h");
				mutationRateLbl.setText("Mutation Rate (%): ");
				rotationRateLbl.setText("Rotation Rate (%): ");
				selectionRateLbl.setText("Selection Rate (%): ");
				crossoverRateLbl.setText("Crossover Rate (%): ");

				//handle and check all parameters specified via user-input.
				if(!orgWidthTxtBox.getText().equals("")
						|| !orgHeightTxtBox.getText().equals("")){
					try{
						//if something was entered for width at least.
						if(!orgWidthTxtBox.getText().equals("")){
							//if input is not too large
							if(Integer.parseInt(orgWidthTxtBox.getText()) <= 20
									&& Integer.parseInt(orgWidthTxtBox.getText()) > 0){
								//if simulation is currently running.
								if(simulation.getTimer().isRunning()){
									//make organisms able to move if they are shrunk.
									for(Organism o: simulation.getOrganisms()){
										o.setRange(Organism.width, Organism.height, true);
									}
								}

								Organism.width = Integer.parseInt(orgWidthTxtBox.getText());

								//if enter was pressed, but height was not filled in.
								if(!orgWidthTxtBox.getText().equals("") && orgHeightTxtBox.getText().equals("")){
									//copy the width's input.
									Organism.height = Integer.parseInt(orgWidthTxtBox.getText());
								}
								else{
									//height was filled in
									if(Integer.parseInt(orgHeightTxtBox.getText()) <= 20
											&& Integer.parseInt(orgHeightTxtBox.getText()) > 0){
										Organism.height = Integer.parseInt(orgHeightTxtBox.getText());
									}
									else{
										orgSizeLbl.setText("Number not 1 <= x <= 20");
										return;
									}
								}
							}
							else{
								orgSizeLbl.setText("Number not 1 <= x <= 20");
								return;
							}
						}
						else{
							orgSizeLbl.setText("Enter a Width!");
							return;
						}
					}
					catch(NumberFormatException e){
						//update label and prevent further action.
						orgSizeLbl.setText("Not a number!");
						return;
					}
					catch(Exception e){
						orgSizeLbl.setText("Unknown Error!");
						return;
					}
				}
				if(!mutationRateTxtBox.getText().equals("")){
					try{
						if(Integer.parseInt(mutationRateTxtBox.getText()) >= 0
								&& Integer.parseInt(mutationRateTxtBox.getText()) <= 100){
							GEP.mutProb =
								(double) Integer.parseInt(mutationRateTxtBox.getText())/100;
						}
						else{
							mutationRateLbl.setText("Number not 0 <= x <= 100");
							return;
						}
					}
					catch(NumberFormatException e){
						mutationRateLbl.setText("Not a number!");
						return;
					}
					catch(Exception e){
						mutationRateLbl.setText("Unknown Error!");
						return;
					}
				}
				if(!rotationRateTxtBox.getText().equals("")){
					try{
						if(Integer.parseInt(rotationRateTxtBox.getText()) >= 0
								&& Integer.parseInt(rotationRateTxtBox.getText()) <= 100){
							GEP.rotProb =
								(double) Integer.parseInt(rotationRateTxtBox.getText())/100;
						}
						else{
							rotationRateLbl.setText("Number not 0 <= x <= 100");
							return;
						}
					}
					catch(NumberFormatException e){
						rotationRateLbl.setText("Not a number!");
						return;
					}
					catch(Exception e){
						rotationRateLbl.setText("Unknown Error!");
						return;
					}
				}
				if(!selectionRateTxtBox.getText().equals("")){
					try{
						if(Integer.parseInt(selectionRateTxtBox.getText()) >= 0
								&& Integer.parseInt(selectionRateTxtBox.getText()) <= 100){
							GEP.tournProb =
								(double) Integer.parseInt(selectionRateTxtBox.getText())/100;
						}
						else{
							selectionRateLbl.setText("Number not 0 <= x <= 100");
							return;
						}
					}
					catch(NumberFormatException e){
						selectionRateLbl.setText("Not a number!");
						return;
					}
					catch(Exception e){
						selectionRateLbl.setText("Unknown Error!");
						return;
					}
				}
				if(!crossoverRateTxtBox.getText().equals("")){
					try{
						if(Integer.parseInt(crossoverRateTxtBox.getText()) >= 0
								&& Integer.parseInt(crossoverRateTxtBox.getText()) <= 100){
							GEP.onePtProb =
								(double) Integer.parseInt(crossoverRateTxtBox.getText())/100;
							GEP.twoPtProb =
								(double) Integer.parseInt(crossoverRateTxtBox.getText())/100;
						}
						else{
							crossoverRateLbl.setText("Number not 0 <= x <= 100");
							return;
						}
					}
					catch(NumberFormatException e){
						crossoverRateLbl.setText("Not a number!");
						return;
					}
					catch(Exception e){
						crossoverRateLbl.setText("Unknown Error!");
						return;
					}
				}
				//Since this field starts the simulation, it should be handled last.
				if(!numOrgsTxtBox.getText().equals("")){
					try{
						if(Integer.parseInt(numOrgsTxtBox.getText()) <= 500
								&& Integer.parseInt(numOrgsTxtBox.getText()) > 0){
							//display confirm dialog.
							int confirmed =
								JOptionPane.showConfirmDialog(gui.getContainer(),
										"Begin a new simulation?", "Confirm",
										JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							//if user confirmed, or no other existing simulation.
							if (confirmed == JOptionPane.YES_OPTION)
							{
								//everything checks out, start simulation.
								OptionsPanel.numOrganisms = Integer.parseInt(numOrgsTxtBox.getText());
								start.setText("New");
								toggleEnabled(true);
								gui.enableStopGenButton();
								gui.enableJMenuItemPause();
								simulation.initialize();
								simulation.start();
							}
						}
						else{
							numOrgsLbl.setText("Number not 2 <= x <= 500");
							return;
						}
					}
					catch(NumberFormatException e){
						numOrgsLbl.setText("Invalid Entry");
						return;
					}
					catch(Exception e){
						numOrgsLbl.setText("Unknown Error");
						return;
					}
				}
				else{
					JOptionPane.showMessageDialog(gui.getContainer(),
							"No population size was specified!", "Message",
							JOptionPane.OK_OPTION);
				}
			}
		};
		start.addActionListener(s);

		/** Pause/Resume Button */
		pause = new JButton();
		pause.setLayout(null);
		pause.setEnabled(false);
		pause.setText("Pause");
		pause.setSize(90,20);
		pause.setLocation(10, 35);
		add(pause);

		ActionListener p = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventPause(simulation);
			}
		};
		pause.addActionListener(p);

		/** # Organisms */
		numOrgsLbl = new JLabel();
		numOrgsLbl.setLayout(null);
		numOrgsLbl.setText("Population Size:");
		numOrgsLbl.setSize(150,20);
		numOrgsLbl.setLocation(10, 60);
		add(numOrgsLbl);

		numOrgsTxtBox = new JTextField();
		numOrgsTxtBox.setLayout(null);
		numOrgsTxtBox.setSize(50,20);
		numOrgsTxtBox.setLocation(10, 80);
		numOrgsTxtBox.setFocusable(true);
		numOrgsTxtBox.setDocument(new JTextFieldLimit(3));
		add(numOrgsTxtBox);

		/** Organism Size */
		orgSizeLbl = new JLabel();
		orgSizeLbl.setLayout(null);
		orgSizeLbl.setText("Organism Size: w x h");
		orgSizeLbl.setSize(150,20);
		orgSizeLbl.setLocation(10, 100);
		add(orgSizeLbl);

		orgWidthTxtBox = new JTextField();
		orgWidthTxtBox.setLayout(null);
		orgWidthTxtBox.setSize(30,20);
		orgWidthTxtBox.setLocation(10, 120);
		orgWidthTxtBox.setFocusable(true);
		orgWidthTxtBox.setDocument(new JTextFieldLimit(3));
		add(orgWidthTxtBox);

		JLabel times = new JLabel();
		times.setLayout(null);
		times.setText("x");
		times.setSize(10,20);
		times.setLocation(40, 120);
		add(times);

		orgHeightTxtBox = new JTextField();
		orgHeightTxtBox.setLayout(null);
		orgHeightTxtBox.setSize(30,20);
		orgHeightTxtBox.setLocation(50, 120);
		orgHeightTxtBox.setFocusable(true);
		orgHeightTxtBox.setDocument(new JTextFieldLimit(3));
		add(orgHeightTxtBox);

		/** Mutation Rate */
		mutationRateLbl = new JLabel();
		mutationRateLbl.setLayout(null);
		mutationRateLbl.setText("Mutation Rate (%): ");
		mutationRateLbl.setSize(150,20);
		mutationRateLbl.setLocation(10, 140);
		add(mutationRateLbl);

		mutationRateTxtBox = new JTextField();
		mutationRateTxtBox.setLayout(null);
		mutationRateTxtBox.setSize(30,20);
		mutationRateTxtBox.setLocation(10, 160);
		mutationRateTxtBox.setFocusable(true);
		mutationRateTxtBox.setDocument(new JTextFieldLimit(3));
		add(mutationRateTxtBox);

		/** Rotation Rate */
		rotationRateLbl = new JLabel();
		rotationRateLbl.setLayout(null);
		rotationRateLbl.setText("Rotation Rate (%): ");
		rotationRateLbl.setSize(150,20);
		rotationRateLbl.setLocation(10, 180);
		add(rotationRateLbl);

		rotationRateTxtBox = new JTextField();
		rotationRateTxtBox.setLayout(null);
		rotationRateTxtBox.setSize(30,20);
		rotationRateTxtBox.setLocation(10, 200);
		rotationRateTxtBox.setFocusable(true);
		rotationRateTxtBox.setDocument(new JTextFieldLimit(3));
		add(rotationRateTxtBox);

		/** Selection Rate */
		selectionRateLbl = new JLabel();
		selectionRateLbl.setLayout(null);
		selectionRateLbl.setText("Selection Rate (%): ");
		selectionRateLbl.setSize(150,20);
		selectionRateLbl.setLocation(10, 220);
		add(selectionRateLbl);

		selectionRateTxtBox = new JTextField();
		selectionRateTxtBox.setLayout(null);
		selectionRateTxtBox.setSize(30,20);
		selectionRateTxtBox.setLocation(10, 240);
		selectionRateTxtBox.setFocusable(true);
		selectionRateTxtBox.setDocument(new JTextFieldLimit(3));
		add(selectionRateTxtBox);

		/** Crossover Rate */
		crossoverRateLbl = new JLabel();
		crossoverRateLbl.setLayout(null);
		crossoverRateLbl.setText("Crossover Rate (%): ");
		crossoverRateLbl.setSize(150,20);
		crossoverRateLbl.setLocation(10, 260);
		add(crossoverRateLbl);

		crossoverRateTxtBox = new JTextField();
		crossoverRateTxtBox.setLayout(null);
		crossoverRateTxtBox.setSize(30,20);
		crossoverRateTxtBox.setLocation(10, 280);
		crossoverRateTxtBox.setFocusable(true);
		crossoverRateTxtBox.setDocument(new JTextFieldLimit(3));
		add(crossoverRateTxtBox);

		/**
		 * MouseListener, handles ALL of the above textfields
		 * will restore default label when field is clicked.
		 */
		MouseListener inputMouseListener = new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(numOrgsTxtBox.isFocusOwner()){
					numOrgsLbl.setText("Population Size:");
				}
				else if(orgWidthTxtBox.isFocusOwner()
						|| orgHeightTxtBox.isFocusOwner()){
					orgSizeLbl.setText("Organism Size: w x h");
				}
				else if(mutationRateTxtBox.isFocusOwner()){
					mutationRateLbl.setText("Mutation Rate (%):");
				}
				else if(rotationRateTxtBox.isFocusOwner()){
					rotationRateLbl.setText("Rotation Rate (%):");
				}
				else if(selectionRateTxtBox.isFocusOwner()){
					selectionRateLbl.setText("Selection Rate (%):");
				}
				else if(crossoverRateTxtBox.isFocusOwner()){
					crossoverRateLbl.setText("Crossover Rate (%):");
				}
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
		numOrgsTxtBox.addMouseListener(inputMouseListener);
		orgWidthTxtBox.addMouseListener(inputMouseListener);
		orgHeightTxtBox.addMouseListener(inputMouseListener);
		mutationRateTxtBox.addMouseListener(inputMouseListener);
		rotationRateTxtBox.addMouseListener(inputMouseListener);
		selectionRateTxtBox.addMouseListener(inputMouseListener);
		crossoverRateTxtBox.addMouseListener(inputMouseListener);

		/** KeyListener, handles ALL of the above textfields */
		KeyListener inputKeyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				int key = keyEvent.getKeyCode();

				if (key == KeyEvent.VK_ENTER) {
					//reset all labels
					numOrgsLbl.setText("Population Size:");
					orgSizeLbl.setText("Organism Size: w x h");
					mutationRateLbl.setText("Mutation Rate (%): ");
					rotationRateLbl.setText("Rotation Rate (%): ");
					selectionRateLbl.setText("Selection Rate (%): ");
					crossoverRateLbl.setText("Crossover Rate (%): ");

					if(numOrgsTxtBox.isFocusOwner()){
						//force click start button.
						start.doClick();
					}
					if(simulation.getTimer().isRunning()){
						if(orgWidthTxtBox.isFocusOwner()
								|| orgHeightTxtBox.isFocusOwner()){
							try{
								//if something was entered for width at least.
								if(!orgWidthTxtBox.getText().equals("")){
									//if input is not far too large
									if(Integer.parseInt(orgWidthTxtBox.getText()) <= 20
											&& Integer.parseInt(orgWidthTxtBox.getText()) > 0){
										//make organisms able to move if they are shrunk.
										//assumes simulation is running.
										for(Organism o: simulation.getOrganisms()){
											o.setRange(Organism.width, Organism.height, true);
										}

										Organism.width = Integer.parseInt(orgWidthTxtBox.getText());

										//if enter was pressed, but height was not filled in.
										if(!orgWidthTxtBox.getText().equals("") && orgHeightTxtBox.getText().equals("")){
											//copy the width's input.
											Organism.height = Integer.parseInt(orgWidthTxtBox.getText());
										}
										else{
											//height was filled in
											if(Integer.parseInt(orgHeightTxtBox.getText()) <= 20
													&& Integer.parseInt(orgHeightTxtBox.getText()) > 0){
												Organism.height = Integer.parseInt(orgHeightTxtBox.getText());
											}
											else{
												orgSizeLbl.setText("Number not 1 <= x <= 20");
												return;
											}
										}
									}
									else{
										orgSizeLbl.setText("Number not 1 <= x <= 20");
									}
								}
								else{
									orgSizeLbl.setText("Enter a Width!");
								}
							}
							catch(NumberFormatException e){
								orgSizeLbl.setText("Not a number!");
							}
							catch(Exception e){
								orgSizeLbl.setText("Unknown Error!");
							}
						}
						else if(mutationRateTxtBox.isFocusOwner()){
							try{
								if(Integer.parseInt(mutationRateTxtBox.getText()) >= 0
										&& Integer.parseInt(mutationRateTxtBox.getText()) <= 100){
									simulation.getGEP().setMutProb(
											(double) Integer.parseInt(mutationRateTxtBox.getText())/100);
								}
								else{
									mutationRateLbl.setText("Number not 0 <= x <= 100");
								}
							}
							catch(NumberFormatException e){
								mutationRateLbl.setText("Not a number!");
							}
							catch(Exception e){
								mutationRateLbl.setText("Unknown Error!");
							}
						}
						else if(rotationRateTxtBox.isFocusOwner()){
							try{
								if(Integer.parseInt(rotationRateTxtBox.getText()) >= 0
										&& Integer.parseInt(rotationRateTxtBox.getText()) <= 100){
									simulation.getGEP().setRotProb(
											(double) Integer.parseInt(rotationRateTxtBox.getText())/100);
								}
								else{
									rotationRateLbl.setText("Number not 0 <= x <= 100");
								}
							}
							catch(NumberFormatException e){
								rotationRateLbl.setText("Not a number!");
							}
							catch(Exception e){
								rotationRateLbl.setText("Unknown Error!");
							}
						}
						else if(selectionRateTxtBox.isFocusOwner()){
							try{
								if(Integer.parseInt(selectionRateTxtBox.getText()) >= 0
										&& Integer.parseInt(selectionRateTxtBox.getText()) <= 100){
									simulation.getGEP().setTournProb(
											(double) Integer.parseInt(selectionRateTxtBox.getText())/100);
								}
								else{
									selectionRateLbl.setText("Number not 0 <= x <= 100");
								}
							}
							catch(NumberFormatException e){
								selectionRateLbl.setText("Not a number!");
							}
							catch(Exception e){
								selectionRateLbl.setText("Unknown Error!");
							}
						}
						else if(crossoverRateTxtBox.isFocusOwner()){
							try{
								if(Integer.parseInt(crossoverRateTxtBox.getText()) >= 0
										&& Integer.parseInt(crossoverRateTxtBox.getText()) <= 100){
									simulation.getGEP().setOnePtProb(
											(double) Integer.parseInt(crossoverRateTxtBox.getText())/100);
									simulation.getGEP().setTwoPtProb(
											(double) Integer.parseInt(crossoverRateTxtBox.getText())/100);
								}
								else{
									crossoverRateLbl.setText("Number not 0 <= x <= 100");
								}
							}
							catch(NumberFormatException e){
								crossoverRateLbl.setText("Not a number!");
							}
							catch(Exception e){
								crossoverRateLbl.setText("Unknown Error!");
							}
						}
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
		numOrgsTxtBox.addKeyListener(inputKeyListener);
		orgWidthTxtBox.addKeyListener(inputKeyListener);
		orgHeightTxtBox.addKeyListener(inputKeyListener);
		mutationRateTxtBox.addKeyListener(inputKeyListener);
		rotationRateTxtBox.addKeyListener(inputKeyListener);
		selectionRateTxtBox.addKeyListener(inputKeyListener);
		crossoverRateTxtBox.addKeyListener(inputKeyListener);

		/** Time Selection Slider */
		timeStepSelectionLbl = new JLabel();
		timeStepSelectionLbl.setLayout(null);
		timeStepSelectionLbl.setText("Current Time Step: "
				+ simulation.getCurrTimeStep()
				+ "ms");
		timeStepSelectionLbl.setSize(150,20);
		timeStepSelectionLbl.setLocation(10, 300);
		add(timeStepSelectionLbl);

		timeStepSelectionSldr = new JSlider(JSlider.HORIZONTAL,
				1, 600, simulation.getCurrTimeStep());
		timeStepSelectionSldr.setLayout(null);
		timeStepSelectionSldr.setEnabled(false);
		timeStepSelectionSldr.setSize(180,50);
		timeStepSelectionSldr.setLocation(1, 320);

		//Create the label table
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer( 100 ), new JLabel("Faster") );
		labelTable.put( new Integer( 500 ), new JLabel("Slower") );
		timeStepSelectionSldr.setLabelTable( labelTable );
		timeStepSelectionSldr.setPaintLabels(true);

		add(timeStepSelectionSldr);

		//adjust slider for fine/coarse adjustment
		class MySliderUI extends MetalSliderUI
		{
			public void scrollByUnit(int direction)
			{
				synchronized(timeStepSelectionSldr)
				{
					int oldValue = timeStepSelectionSldr.getValue();
					int delta = (direction > 0) ? 10 : -5;
					timeStepSelectionSldr.setValue(oldValue + delta);
				}
			}
		}
		final MySliderUI myUI = new MySliderUI();
		timeStepSelectionSldr.setUI( myUI );
		myUI.scrollByUnit(1);

		MouseListener sldrMouseListener = new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				timeStepSelectionSldr.setValue(myUI.valueForXPosition(
						timeStepSelectionSldr.getMousePosition().x));
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
		timeStepSelectionSldr.addMouseListener(sldrMouseListener);

		ChangeListener sldrChangeListener = new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				simulation.setTimeStep(timeStepSelectionSldr.getValue());
				timeStepSelectionLbl.setText("Current Time Step: "
						+ timeStepSelectionSldr.getValue()
						+ "ms");
			}
		};
		timeStepSelectionSldr.addChangeListener(sldrChangeListener);
	}

	//------------------------------------------------------------------------------------
	//--method called in GUI to handle pause button visibility--
	//------------------------------------------------------------------------------------
	public void toggleEnabled(boolean toggle){
		if(toggle == true){
			pause.setEnabled(true);
			timeStepSelectionSldr.setEnabled(true);
		}else{
			pause.setEnabled(false);
			timeStepSelectionSldr.setEnabled(false);
		}
	}
	
	//------------------------------------------------------------------------------------
	//--method called in GUI to disable pause button visibility--
	//------------------------------------------------------------------------------------
	public void disablePause(){
		pause.setEnabled(false);
	}
	
	//------------------------------------------------------------------------------------
	//--method called in GUI to enable pause button visibility--
	//------------------------------------------------------------------------------------
	public void enablePause(){
		pause.setEnabled(true);
	}
	
	//------------------------------------------------------------------------------------
	//--accessors/mutators--
	//------------------------------------------------------------------------------------
	public void eventPause(GridPanel simulation){
		if(simulation.isPaused()){
			simulation.start();
			pause.setText("Pause");
			MonitorPanel.simStatus.setText("Running");
		}
		else{
			simulation.stop();
			pause.setText("Resume");
			MonitorPanel.simStatus.setText("Paused");
		}
	}

	//------------------------------------------------------------------------------------
	//--overloaded functions--
	//------------------------------------------------------------------------------------
	@Override
	public void run() {
	}
}