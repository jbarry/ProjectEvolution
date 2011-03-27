package Interactive;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.ImageIcon;

import Frame.*;

public class Organism extends Matter{
	//------------------------------------------------------------------------------------
	//--globals--
	//------------------------------------------------------------------------------------
	private double avgHealth;
	private double hlthTot;
	private int samples;
	private int steps;
	private Chromosome chromosome;
	private double fitness;
	private int scanRange;
	private ArrayList<ArrayList<String>> ActionList;
	private ArrayList<Coordinate> StartingLocation;
	private ArrayList<Chromosome> chromosomeHistory;
	public static int WIDTH = 5;
	public static int HEIGHT = 5;
	//TODO: what is this?
	private TreeSet<Integer> healthyFood;
	private TreeSet<Integer> poisonFood;
	
	//Ian's
	//for images/actions
    private Image ninja_walk1;
    private Image ninja_walk1_inv;
    private Image ninja_walk2;
    private Image ninja_walk2_inv;
    private Image ninja_eat;
    private Image ninja_eat_inv;
    private Image ninja_attack;
    private Image ninja_attack_inv;
    private Image ninja_dead;
    private Image ninja_dead_inv;
    private boolean swapImage;
    private boolean facingRight;
    private char currentAction; // 'a' for attack, 'e' for eating, 'm' for movement
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------

	public Organism(double aHealth, int chromSize, int anId, int aScanRange) {
		super(aHealth, anId, 'o');
		//load initial images
		ninja_walk1 = new ImageIcon(getClass().getResource("sprites/ninja_walk1.gif")).getImage();
		ninja_walk1_inv = new ImageIcon(getClass().getResource("sprites/ninja_walk1_inv.gif")).getImage();
		ninja_walk2 = new ImageIcon(getClass().getResource("sprites/ninja_walk2.gif")).getImage();
		ninja_walk2_inv = new ImageIcon(getClass().getResource("sprites/ninja_walk2_inv.gif")).getImage();
		ninja_eat = new ImageIcon(getClass().getResource("sprites/ninja_eat.gif")).getImage();
		ninja_eat_inv = new ImageIcon(getClass().getResource("sprites/ninja_eat_inv.gif")).getImage();
		ninja_attack = new ImageIcon(getClass().getResource("sprites/ninja_attack.gif")).getImage();
		ninja_attack_inv = new ImageIcon(getClass().getResource("sprites/ninja_attack_inv.gif")).getImage();
		ninja_dead = new ImageIcon(getClass().getResource("sprites/ninja_dead.gif")).getImage();
		ninja_dead_inv = new ImageIcon(getClass().getResource("sprites/ninja_dead_inv.gif")).getImage();
		//create behavior tracking boolean variables
		swapImage = true;
		facingRight = true;
		currentAction = ' ';
		chromosome = new Chromosome(chromSize);
		samples = 0;
		avgHealth = 0;
		hlthTot = 0;
		scanRange = aScanRange;
		steps = 0;
		fitness = 0.0;
		ActionList= new ArrayList<ArrayList<String>>();
		ActionList.add(new ArrayList<String>());
		StartingLocation=new ArrayList<Coordinate>();
		chromosomeHistory= new ArrayList<Chromosome>();
		healthyFood = new TreeSet<Integer>();
		poisonFood = new TreeSet<Integer>(); 
	}
	
	//for testing purposes only.
	//just removing the GridPanel call.
	public Organism(boolean boo, int aChromSize) {
		hlth = 7500.00;
		r = new Random();
		chromosome = new Chromosome(aChromSize);
		fitness = 0.0;
	}
	
	//This ctor is for testing purposes.
	public Organism(double ahealth, Chromosome aChromosome) {
		hlth = ahealth;
		chromosome = aChromosome;
	}

	public Organism(Coordinate aLocation, Chromosome aChromosome) {
		hlth = 7500.0;
		location = aLocation;
		chromosome = aChromosome;
	}
	
	public void newLocation() {
		setRange(WIDTH, HEIGHT, 'w');
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);
		while(!canSpawn(x, y)){
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		location = new Coordinate(x, y);
		//set boundaries
		setWrapAround(WIDTH, HEIGHT);
		setRange(WIDTH, HEIGHT, 'o');
	}
	
	public void eatFood(Food f, double fdVal){
		currentAction = 'e';
		f.deplete(fdVal);
		if(f instanceof HealthyFood) {
			//System.out.println("orgId: " + id);
			//System.out.println("hlthy");
			//System.out.println("orgHealth: " + hlth);
			//System.out.println("FoodId: " + f.getId());
			incHlth(fdVal);
		}
		else if(f instanceof PoisonousFood){
			//System.out.println("orgId: " + id);
			//System.out.println("pois");
			//System.out.println("orgHealth: " + hlth);
			//System.out.println("FoodId: " + f.getId());
			deplete(fdVal);
		}
	}
	
	public void attackOrganism(){
		currentAction = 'a';
	}
	
	public void moveNorth(LinkedList<Organism> organisms) {
		currentAction = 'm';
		
		//make old location available.
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		
		//if the next move is available.
		try{
			if(canSpawn(location.getX(), location.getY() - 1)){
				//move there.
				location.setY(location.getY() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		//make current location unavailable
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveNorthEast(LinkedList<Organism> organisms) {
		currentAction = 'm';
		facingRight = true;
		
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(canSpawn(location.getX() + 1, location.getY() - 1)){
				location.setX(location.getX() + 1);
				location.setY(location.getY() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		
		}
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveEast(LinkedList<Organism> organisms) {
		currentAction = 'm';
		facingRight = true;
		
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(location.getX() + 1 + WIDTH/2 >= GridPanel.WIDTH){
				if(canSpawn(WIDTH/2, location.getY()))
					location.setX(WIDTH/2);
			}
			if(canSpawn(location.getX() + 1, location.getY())){
				location.setX(location.getX() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		
		}	
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveSouthEast(LinkedList<Organism> organisms) {
		currentAction = 'm';
		facingRight = true;
		
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(location.getY() + 1 + HEIGHT/2>= GridPanel.HEIGHT){
				if(canSpawn(location.getX(), HEIGHT/2))
					location.setY(HEIGHT/2);
			}
			if(canSpawn(location.getX() + 1, location.getY() + 1)){
				location.setX(location.getX() + 1);
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveSouth(LinkedList<Organism> organisms) {
		currentAction = 'm';

		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(canSpawn(location.getX(), location.getY() + 1)){
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(WIDTH, HEIGHT, 'o');

	}

	public void moveSouthWest(LinkedList<Organism> organisms) {
		currentAction = 'm';
		facingRight = false;
		
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(canSpawn(location.getX() - 1, location.getY() + 1)){
				location.setX(location.getX() - 1);
				location.setY(location.getY() + 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveWest(LinkedList<Organism> organisms) {
		currentAction = 'm';
		facingRight = false;
		
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(canSpawn(location.getX() - 1, location.getY())){
				location.setX(location.getX() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		setRange(WIDTH, HEIGHT, 'o');
	}

	public void moveNorthWest(LinkedList<Organism> organisms) {
		currentAction = 'm';
		facingRight = true;
		
		setRange(WIDTH, HEIGHT, 'w');
		setWrapAround(WIDTH, HEIGHT);
		try{
			if(canSpawn(location.getX() - 1, location.getY() - 1)){
				location.setX(location.getX() - 1);
				location.setY(location.getY() - 1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){}
		setRange(WIDTH, HEIGHT, 'o');
	}
	
	//------------------------------------------------------------------------------------
	//--overloaded functions--
	//------------------------------------------------------------------------------------

	/**
	 * @return a String representation of the Object.
	 */
	public String toString(){
		String str = "";
		str += " I am an Organism. Fear me."
			+  "\n Location: " + location
			+  "\n Health: " + hlth;
		return str;
	}
	
	//------------------------------------------------------------------------------------
	//--getters/setters--
	//------------------------------------------------------------------------------------
	
	public Chromosome getChromosome() {
		return chromosome;
	}
	
	public void setChromosome(Chromosome aChrom){
		chromosome = aChrom;
	}
	
	public void incHlthTot() {
		hlthTot+=hlth;
		samples++;
	}
	
	public double getHlthTot() {
		return hlthTot;
	}
	
	public void countStep() {
		steps++;
	}
	
	public int getNumSteps() {
		return steps;
	}
	
	public int getSamples() {
		return samples;
	}
	
	@Override
	public int getHeight() {
		return Organism.HEIGHT;
	}
	
	@Override
	public int getWidth() {
		return Organism.WIDTH;
	}
	
	public void addAction(String action,int index){
		ActionList.get(ActionList.size()-1).add(action + " " + index);
	}
	
	public void addGeneration(){
		ActionList.add(new ArrayList<String>());
		addStartingLocation();
		chromosomeHistory.add(chromosome);
	}
	
	public void addStartingLocation(){
		StartingLocation.add(getLocation());
	}
	
	public void addChromosome(){
		chromosomeHistory.add(chromosome);
	}
	
	public ArrayList<String> getActions(int generation){
		return ActionList.get(generation);
	}
	
	//TODO: add dwight.
	public int getHealthyFoodSize(){
		return healthyFood.size();
	}
	
	public int getPoisonFoodSize(){
		return poisonFood.size();
	}
	
	//Painting.
//	public void paint(Graphics g) {
//		g.setColor(Color.BLACK);
//		g.fillRect((int)this.location.getX()-(WIDTH/2), 
//				   (int)this.location.getY()-(HEIGHT/2), 
//				   WIDTH, HEIGHT);
//	}
	
	public void paint(Graphics g) {
		if(facingRight){
			if(getHealth() <= 0){
				g.drawImage(ninja_dead, location.getX()-2*Organism.WIDTH/2,
						location.getY()-2*Organism.HEIGHT/2, 2*Organism.WIDTH,
						2*Organism.HEIGHT, null);
			}
			else{
				if(currentAction == 'e'){
					g.drawImage(ninja_eat, location.getX()-2*Organism.WIDTH/2,
							location.getY()-2*Organism.HEIGHT/2, 2*Organism.WIDTH,
							2*Organism.HEIGHT, null);
				}
				else if(currentAction == 'a'){
					g.drawImage(ninja_attack, location.getX()-2*Organism.WIDTH/2,
							location.getY()-2*Organism.HEIGHT/2, 2*Organism.WIDTH,
							2*Organism.HEIGHT, null);
				}
				else{
					if(swapImage){
						g.drawImage(ninja_walk1, location.getX()-2*Organism.WIDTH/2,
								location.getY()-2*Organism.HEIGHT/2, 2*Organism.WIDTH,
								2*Organism.HEIGHT, null);
					}
					else{
						g.drawImage(ninja_walk2, location.getX()-2*Organism.WIDTH/2,
								location.getY()-2*Organism.HEIGHT/2, 2*Organism.WIDTH,
								2*Organism.HEIGHT, null);
					}
				}
			}
		}
		else{
			if(getHealth() <= 0){
				g.drawImage(ninja_dead_inv, location.getX()-2*Organism.WIDTH/2,
						location.getY()-2*Organism.HEIGHT/2, 2*Organism.WIDTH,
						2*Organism.HEIGHT, null);
			}
			else{
				if(currentAction == 'e'){
					g.drawImage(ninja_eat_inv, location.getX()-2*Organism.WIDTH/2,
							location.getY()-2*Organism.HEIGHT/2, 2*Organism.WIDTH,
							2*Organism.HEIGHT, null);
				}
				else if(currentAction == 'a'){
					g.drawImage(ninja_attack_inv, location.getX()-2*Organism.WIDTH/2,
							location.getY()-2*Organism.HEIGHT/2, 2*Organism.WIDTH,
							2*Organism.HEIGHT, null);
				}
				else{
					if(swapImage){
						g.drawImage(ninja_walk1_inv, location.getX()-2*Organism.WIDTH/2,
								location.getY()-2*Organism.HEIGHT/2, 2*Organism.WIDTH,
								2*Organism.HEIGHT, null);
					}
					else{
						g.drawImage(ninja_walk2_inv, location.getX()-2*Organism.WIDTH/2,
								location.getY()-2*Organism.HEIGHT/2, 2*Organism.WIDTH,
								2*Organism.HEIGHT, null);
					}
				}
			}
		}
		swapImage = !swapImage;
	}
}	
