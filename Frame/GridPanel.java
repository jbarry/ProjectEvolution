package Frame;

import static java.lang.System.out;
import Evaluation.Eval;
import Evaluation.Normalizer;

import java.util.HashMap;
import Evolution.GEP;
import Interactive.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import Interactive.Pair;
import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Evaluation.Expr;
/**
 * This class will handle all of the simulation's display.
 */
@SuppressWarnings("all")
public class GridPanel extends JPanel
{
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	public final static int WIDTH = 600;
	public final static int HEIGHT = 400;


	//	public static boolean[][] isValidLocation;
	public static Pair<Integer, Character>[][] locationMap;

	private LinkedList<Organism> organisms;
	private LinkedList<HealthyFood> healthFd;
	private LinkedList<PoisonousFood> poisFood;
	private ArrayList<Integer> shuffleIds;
	private int lengthTimeStep = 100;
	private int lengthGeneration = lengthTimeStep*300;
	private int timePassed = 0;
	private int trialsPerGen = 1;
	public int trialNum = 1;
	public int generationNum = 1;
	public double lastAvg = 0;
	private GEP g;
	private int numFoodSources;
	private Timer t;
	private Normalizer norm;
	private int numPreProcessedGenerations = 0;
	private Random ran;
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	/**
	 * This constructor will handle all of the movements/interactions of
	 * all objects in the current game state.
	 */
	public GridPanel(final GUI gui) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				//initial JPanel settings
				setLayout(null);
				setLocation(GUI.WIDTH - GridPanel.WIDTH, 0);
				setSize(GridPanel.WIDTH, GridPanel.HEIGHT);
				setBorder(BorderFactory.createLineBorder(Color.black));

				/**Add Listeners*/
				//track user mouse movement.
				MouseMotionListener simMouseMotion = new MouseMotionListener(){
					@Override
					public void mouseDragged(MouseEvent arg0) {
					}
					@Override
					public void mouseMoved(MouseEvent arg0) {
						try {
							//get and display current mouse location.
							Coordinate mouseLocation = new Coordinate(arg0.getX(), arg0.getY());
							MonitorPanel.currMouseLoc.setText(mouseLocation.toString());

							//used to manage checks
							boolean isOrg = false;
							boolean isPFood = false;
							boolean isHFood = false;

							//check mouse location vs. all organism's locations.
							for(Organism o: organisms) {
								if(mouseLocation.approxEquals(o.getLocation(), Organism.width/2)) {
									//organism found
									isOrg = true;
									MonitorPanel.simObjInfo.setText(o.toString());
									//break to prevent any more updating from occuring and loop overhead.
									break;
								} else {
									isOrg = false;
								}
							}

							for(HealthyFood r: healthFd) {
								if(mouseLocation.approxEquals(r.getLocation(), Food.width/2)){
									//food found
									isHFood = true;
									MonitorPanel.simObjInfo.setText(r.toString());
									//break to prevent any more updating from occuring and loop overhead.
									break;
								} else {
									isHFood = false;
								}
							}
							for(PoisonousFood r: poisFood) {
								if(mouseLocation.approxEquals(r.getLocation(), Food.width/2)){
									//food found
									isPFood = true;
									MonitorPanel.simObjInfo.setText(r.toString());
									//break to prevent any more updating from occuring and loop overhead.
									break;
								}
								else{
									isPFood = false;
								}
							}

							if(!isHFood & !isPFood & !isOrg)
								MonitorPanel.simObjInfo.setText("No Object Selected");
						}
						catch(NullPointerException e){

						}
					}
				};
				addMouseMotionListener(simMouseMotion);

				//handle other mouse events
				MouseListener simMouseListener = new MouseListener(){
					@Override
					public void mouseClicked(MouseEvent arg0) {
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						try{
							//get and display current mouse location.
							Coordinate mouseLocation = new Coordinate(arg0.getX(), arg0.getY());
							MonitorPanel.currMouseLoc.setText(mouseLocation.toString());
						}
						catch(NullPointerException e){

						}
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						MonitorPanel.currMouseLoc.setText("");
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
					}

				};
				addMouseListener(simMouseListener);

				//initial program settings
				organisms = new LinkedList<Organism>();
				healthFd = new LinkedList<HealthyFood>();
				poisFood = new LinkedList<PoisonousFood>();

				t = new javax.swing.Timer(lengthTimeStep, new TimerListener(gui));
			}

		};
		r.run();
	}

	/**
	 * Sets the initial game state of the GridPanel
	 */
	public void initialize(){
		//reset all generation info from previous simulations.
		generationNum = 1;
		trialNum = 1;
		GUI.genPanel.resetGenInformation();
		ran = new Random();
		timePassed = 0;
		numFoodSources = 0;
		shuffleIds = new ArrayList<Integer>();
		/*
		 * location map will consist of:
		 * 	key: current instance number of object
		 *  value:
		 * 		'w' for white space or available.
		 * 		'o' for organism.
		 * 		'h' for healthy food.
		 * 		'p' for poisonous food.
		 */
		locationMap = new Pair[GridPanel.WIDTH][GridPanel.HEIGHT];
		for(int i = 0; i < locationMap.length; i++){
			for(int j=0; j<locationMap[i].length; j++){
				//mark available
				locationMap[i][j] = new Pair<Integer, Character>(0, 'w');
			}
		}

		norm = new Normalizer(
				new Pair<Double, Double> (1.0, 10000.0),
				new Pair<Double, Double> (1.0, 50.0));

		organisms.clear();
		for(int i = 0; i < OptionsPanel.numOrganisms; i++){
			Organism o = new Organism(500.00, 9, i, 100); //justin b (03.15).
			shuffleIds.add(i);
			organisms.add(o);
			o.addStartingLocation();
			o.addChromosome();
		}
		healthFd.clear();
		for(int i = 0; i < OptionsPanel.numOrganisms/5; i++){
			HealthyFood h = new HealthyFood(100.0, i, 2);
			healthFd.add(h);
			numFoodSources++;
		}
		poisFood.clear();
		for(int i = 0; i < OptionsPanel.numOrganisms/5; i++){
			PoisonousFood p = new PoisonousFood(100.0, i, 2);
			poisFood.add(p);
		}
		g = new GEP(organisms, 0.75, 0.01, 0.01, 0.75, 0.75);
		//		preProcess(20);
	}
	
	/**
	 * Sets the initial game state of the GridPanel
	 */
	public void initialize2(){
		//reset all generation info from previous simulations.
		generationNum = 1;
		trialNum = 1;
		GUI.genPanel.resetGenInformation();
		ran = new Random();
		timePassed = 0;
		numFoodSources = 0;
		shuffleIds = new ArrayList<Integer>();
		/*
		 * location map will consist of:
		 * 	key: current instance number of object
		 *  value:
		 * 		'w' for white space or available.
		 * 		'o' for organism.
		 * 		'h' for healthy food.
		 * 		'p' for poisonous food.
		 */
		locationMap = new Pair[GridPanel.WIDTH][GridPanel.HEIGHT];
		for(int i = 0; i < locationMap.length; i++){
			for(int j=0; j<locationMap[i].length; j++){
				//mark available
				locationMap[i][j] = new Pair<Integer, Character>(0, 'w');
			}
		}

		norm = new Normalizer(
				new Pair<Double, Double> (1.0, 10000.0),
				new Pair<Double, Double> (1.0, 50.0));

		organisms.clear();
		organisms.add(new Organism(new Coordinate(200, 300), 20, 1, true));
		healthFd.add(new HealthyFood(new Coordinate(210, 310), 5, true));
		healthFd.add(new HealthyFood(new Coordinate(210, 300), 6, true));
		healthFd.add(new HealthyFood(new Coordinate(200, 290), 7, true));
		healthFd.add(new HealthyFood(new Coordinate(190, 310), 8, true));
		healthFd.add(new HealthyFood(new Coordinate(190, 290), 9, true));
		numFoodSources++;
		numFoodSources++;
		numFoodSources++;
		numFoodSources++;
		numFoodSources++;
		Organism o = organisms.get(0);
		o.addStartingLocation();
		o.addChromosome();
		List<Integer> ids = o.getSurroundingObjects('h', 40);
		for(int i = 0; i < ids.size(); i++) {
			out.println(ids.get(i));
		}
//		g = new GEP(organisms, 0.75, 0.01, 0.01, 0.75, 0.75);
	}
	
	class TimerListener implements ActionListener {

		GUI gui;
		public TimerListener(final GUI aGui) {
			gui = aGui;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(timePassed < lengthGeneration) {
				timePassed+=lengthTimeStep;
				Collections.shuffle(shuffleIds);
				int orgIndex = 0;
				for(Integer integ: shuffleIds){
					Organism org = organisms.get(integ);
					org.deplete(1.5);
					//Take sample of organism health for fitness.
					org.incHlthTot();
					if(org.getHealth() > 0){
						ArrayList<Integer> sightHlthy =
							org.getSurroundingObjects('h', 60);
						ArrayList<Integer> sightPois =
							org.getSurroundingObjects('p', 60);
						sightHlthy.addAll(sightPois);
						double orgX = norm.normalize(
								org.getLocation().getX());
						double orgY = norm.normalize(
								org.getLocation().getY());
						double health = org.getHealth();
						Chromosome chrom = org.getChromosome();
						Pair<Integer, Double> bestEval =
							new Pair<Integer, Double> (0, 0.0);
						for (int i = 0; i < chrom.size(); i++) {
							Gene workingGene = chrom.getGene(i);
							//if there is something in org's field of vision.
							if (sight.size() > 0) {
								for(int j = 0; j < sight.size(); j++) {
									HashMap<String, Double> environment =
										new HashMap<String, Double>();
									Food f = sight.get(j);
									double foodX = norm.normalize(
											f.getLocation().getX());
									double foodY = norm.normalize(
											f.getLocation().getY());
									double orgNearFood = norm.normalize(
											f.numSurroundingObjects(5));
									Expr result = workingGene.getEvaledList();
									environment.put("a", foodX-orgX);
									environment.put("b", orgY-foodY);
									environment.put("c", orgNearFood);
									environment.put("d", norm.normalize(health));
									double geneEval = result.evaluate(environment);
									if(geneEval > bestEval.right())
										bestEval = new Pair<Integer, Double> (i, geneEval);
								}
							}
							//TODO: if there isn't anything in org's field of vision. 
							//These numbers need to be worked out.
							else {
								Expr result = workingGene.getEvaledList();
								HashMap<String,Double> environment = new HashMap<String, Double>();
								environment.put("a", norm.normalize(10.0)); // not sure what to pass
								environment.put("b", norm.normalize(10.0)); // not sure what to pass
								environment.put("c", norm.normalize(0.0));
								environment.put("d", norm.normalize(health));
								double geneEval = result.evaluate(environment);
								if(geneEval > bestEval.right())
									bestEval = new Pair<Integer, Double> (i, geneEval);
							}
						}
						// Genes are set as N-S-E-W-NE-NW-SE-SW-Eat.
						switch (bestEval.left()) {
						case 0: 
							org.moveNorth(organisms);
							//org.addAction("N", orgIndex);
							org.countStep();
							break;
						case 1: 
							org.moveSouth(organisms);
							//org.addAction("S", orgIndex);
							org.countStep();
							break;
						case 2: 
							org.moveEast(organisms); 
							//org.addAction("E", orgIndex);
							org.countStep();
							break;
						case 3: 
							org.moveWest(organisms);
							//org.addAction("W", orgIndex);
							org.countStep();
							break;
						case 4: 
							org.moveNorthEast(organisms);
							//org.addAction("NE", orgIndex);
							org.countStep();
							break;
						case 5: 
							org.moveNorthWest(organisms);
							//org.addAction("NW", orgIndex);
							org.countStep();
							break;
						case 6: 
							org.moveSouthEast(organisms);
							//org.addAction("SE", orgIndex);
							org.countStep();
							break;
						case 7: 
							org.moveSouthWest(organisms);
							//org.addAction("SW", orgIndex);
							org.countStep();
							break;
						case 8: 
							ArrayList<Integer> surrndngHlthyFd =
								org.getSurroundingObjects('h', 5);
							ArrayList<Integer> surrndngPoisFd = 
								org.getSurroundingObjects('p', 5);
							if (surrndngHlthyFd.size() != 0) {
								out.println("Id: "  + org.getId());
								Coordinate pos = org.getLocation();
								out.println("Pos: ("  + pos.getX() +
										", " + pos.getY()  + ")");
//								for (int i = 0; i < surrndngHlthyFd.size(); i++) {
//									healthFd.get(
//									out.print
//								}
								org.eatFood(healthFd.get(
										ran.nextInt(surrndngHlthyFd.size())), 5.0);
							} else if (surrndngPoisFd.size() != 0) {
								for (int i = 0; i < surrndngPoisFd.size(); i++) {
									
								}
								org.eatFood(poisFood.get(
										ran.nextInt(surrndngPoisFd.size())), 5.0);
							}
						}
					}
					orgIndex++;
				}
				//End AI LOGIC
				repaint();
			} else if (trialNum < trialsPerGen) {
				t.stop();
				for(Organism o: organisms){
					o.newLocation();
					o.setHealth(o.getMaxHealth());
				}
				trialNum++;
				healthFd.clear();
				poisFood.clear();
				for(int i = 0; i < OptionsPanel.numOrganisms/5; i++){
					HealthyFood h = new HealthyFood(100.0, i, 2);
					PoisonousFood f = new PoisonousFood(100.0, i, 2);
					healthFd.add(h);
					poisFood.add(f);
				}
				timePassed = 0;
				if(!GUI.genPanel.resumeHasNotBeenClicked() &&
						!GUI.genPanel.genIsSelected()){
					GUI.genPanel.enableResumeSimulation();
					gui.toggleAllPauses(false);
				}
				else{
					t.start();
					GUI.genPanel.newTrial();
				}
			} else {
				t.stop();
				timePassed = 0;
				double sum = 0;	
				for(Organism o: organisms) {
					sum+=g.fitness(o);
					o.newLocation();
					out.println(o.getFitness());
				}
				lastAvg = sum/OptionsPanel.numOrganisms;
				g.setOrgList(organisms);
				organisms = g.newGeneration();
				healthFd.clear();
				poisFood.clear();
				for(Organism o: organisms)
					o.setHealth(o.getMaxHealth());
				for(int i = 0; i < OptionsPanel.numOrganisms/5; i++) {
					HealthyFood h = new HealthyFood(100.0, i, 2);
					PoisonousFood f = new PoisonousFood(100.0, i, 2);
					healthFd.add(h);
					poisFood.add(f);
				}
				trialNum = 1;
				generationNum++;

				GUI.genPanel.addGeneration();
				if(!GUI.genPanel.resumeHasNotBeenClicked()) {
					GUI.genPanel.enableResumeSimulation();
					gui.toggleAllPauses(false);
				} else {
					t.start();
					GUI.genPanel.newGeneration();
					repaint();
				}
			}
		}
	}

	/**
	 * Preprocess generations
	 * Essentially run the simulation without updating the graphics.
	 */

	public void preProcess(int generations){
		for(numPreProcessedGenerations=0; numPreProcessedGenerations < generations; numPreProcessedGenerations++){
			System.out.println("Processing Generation " + numPreProcessedGenerations);
			while(timePassed < lengthGeneration) {
				/*begin game logic here:*/
				timePassed+=lengthTimeStep;
				LinkedList<Organism> orgCopy = new LinkedList<Organism>();
				orgCopy=(LinkedList<Organism>) organisms.clone();
				Collections.shuffle(orgCopy);
				int orgIndex = 0;
				for(Organism org: orgCopy){
					org.deplete(5.0);
					//Take sample of organism health for fitness.
					org.incHlthTot();
					if(org.getHealth() > 0){
						ArrayList<Food> sight = new ArrayList<Food>();
						sight = org.look(healthFd, poisFood);
						double orgX = norm.normalize(
								org.getLocation().getX());
						double orgY = norm.normalize(
								org.getLocation().getY());
						double health = org.getHealth();
						Chromosome chrom = org.getChromosome();
						Pair<Integer, Double> bestEval1 =
							new Pair<Integer, Double> (0, 0.0);
						Pair<Integer, Double> bestEval2 =
							new Pair<Integer, Double> (1, 0.0);
						for (int i = 2; i < chrom.size(); i++) {
							Gene workingGene = chrom.getGene(i);
							//if there is something in org's field of vision.
							if (sight.size() > 0) {
								for(int j = 0; j < sight.size(); j++) {
									HashMap<String, Double> environment =
										new HashMap<String, Double>();
									Food f = sight.get(j);
									double foodX = norm.normalize(
											f.getLocation().getX());
									double foodY = norm.normalize(
											f.getLocation().getY());
									double orgNearFood = norm.normalize(
											f.numSurroundingObjects(5));
									Expr result = workingGene.getEvaledList();
									environment.put("a", foodX-orgX);
									environment.put("b", orgY-foodY);
									environment.put("c", orgNearFood);
									environment.put("d", norm.normalize(health));
									double geneEval = result.evaluate(environment);
									if(geneEval > bestEval1.right() && bestEval1.right() < bestEval2.right()){
										bestEval1.setLeft(i);
										bestEval1.setRight(geneEval);
									}
									else if(geneEval>bestEval2.right()){
										bestEval2.setLeft(i);
										bestEval2.setRight(geneEval);
									}
								}
								sight.clear();
							}
							//TODO: if there isn't anything in org's field of vision.
							//These numbers need to be worked out.
							else {
								Expr result = workingGene.getEvaledList();
								HashMap<String,Double> environment = new HashMap<String, Double>();
								environment.put("a", norm.normalize(10.0)); // not sure what to pass
								environment.put("b", norm.normalize(10.0)); // not sure what to pass
								environment.put("c", norm.normalize(0.0));
								environment.put("d", norm.normalize(health));
								double geneEval = result.evaluate(environment);
								if(geneEval > bestEval1.right() && bestEval1.right() < bestEval2.right()){
									bestEval1.setLeft(i);
									bestEval1.setRight(geneEval);
								}
								else if(geneEval > bestEval2.right()){
									bestEval2.setLeft(i);
									bestEval2.setRight(geneEval);
								}
							}
						}
						// Genes are set as N-S-E-W-NE-NW-SE-SW-Eat.
						switch (bestEval1.left()) {
						case 0:
							org.moveNorth(organisms);
							//org.addAction("N", orgIndex);
							org.countStep();
							break;
						case 1:
							org.moveSouth(organisms);
							//org.addAction("S", orgIndex);
							org.countStep();
							break;
						case 2:
							org.moveEast(organisms);
							//org.addAction("E", orgIndex);
							org.countStep();
							break;
						case 3:
							org.moveWest(organisms);
							//org.addAction("W", orgIndex);
							org.countStep();
							break;
						case 4:
							org.moveNorthEast(organisms);
							//org.addAction("NE", orgIndex);
							org.countStep();
							break;
						case 5:
							org.moveNorthWest(organisms);
							//org.addAction("NW", orgIndex);
							org.countStep();
							break;
						case 6:
							org.moveSouthEast(organisms);
							//org.addAction("SE", orgIndex);
							org.countStep();
							break;
						case 7:
							org.moveSouthWest(organisms);
							//org.addAction("SW", orgIndex);
							org.countStep();
							break;
						case 8:
							ArrayList<Integer> surrndngHlthyFd =
								org.getSurroundingObjects('h', 5);
							ArrayList<Integer> surrndngPoisFd =
								org.getSurroundingObjects('p', 5);
							if (surrndngHlthyFd.size() != 0) {
								org.eatFood(healthFd.get(
										ran.nextInt(surrndngHlthyFd.size())), 5.0);
							} else if (surrndngPoisFd.size() != 0) {
								org.eatFood(poisFood.get(
										ran.nextInt(surrndngPoisFd.size())), 5.0);
							}
						}
					}
					orgIndex++;
				}
			}
			if (trialNum < trialsPerGen) {
				for(Organism o: organisms){
					o.newLocation();
					o.setHealth(o.getMaxHealth());
				}
				trialNum++;
				healthFd.clear();
				poisFood.clear();
				for(int i = 0; i < OptionsPanel.numOrganisms/5; i++){
					HealthyFood h = new HealthyFood(100.0, i, 2);
					PoisonousFood f = new PoisonousFood(100.0, i, 2);
					healthFd.add(h);
					poisFood.add(f);
				}
				timePassed = 0;
			}
			else {
				timePassed = 0;
				int sum = 0;
				for(Organism o: organisms) {
					sum+=g.fitness(o);
					o.newLocation();
				}
				lastAvg = sum/OptionsPanel.numOrganisms;
				g.setOrgList(organisms);
				organisms = g.newGeneration();
				healthFd.clear();
				poisFood.clear();
				for(Organism o: organisms)
					o.setHealth(o.getMaxHealth());
				for(int i = 0; i < OptionsPanel.numOrganisms/5; i++) {
					HealthyFood h = new HealthyFood(100.0, i, 2);
					PoisonousFood f = new PoisonousFood(100.0, i, 2);
					healthFd.add(h);
					poisFood.add(f);
				}
				trialNum = 1;
				generationNum++;
			}
		}
		generationNum = 1;
	}

	//------------------------------------------------------------------------------------
	//--Getters/Setters--
	//------------------------------------------------------------------------------------
	public LinkedList<Organism> getOrganisms(){
		return organisms;
	}

	public GEP getGEP(){
		return g;
	}

	public Timer getTimer(){
		return t;
	}

	public int getCurrTimeStep(){
		return lengthTimeStep;
	}

	public void setTimeStep(int step){
		lengthTimeStep = step;
		t.setDelay(step);
	}

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
	//------------------------------------------------------------------------------------
	//--Override Functions--
	//------------------------------------------------------------------------------------
	/**
	 * A function that will be called to update the
	 * current state of the panel
	 *
	 * @param g the Graphics object used
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		//handle each new organism created.
		for(Organism org : organisms){
			org.paint(g);
		}
		for(HealthyFood h: healthFd){
			if(h.getHealth() > 0){
				h.paint(g, false);
			}
			else{
				h.paint(g, true);
			}
		}
		for(PoisonousFood p: poisFood){
			if(p.getHealth()>0){
				p.paint(g, false);
			}
			else{
				p.paint(g, true);
			}
		}
	}

}