package Interactive;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;

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
	private TreeSet<Integer> healthyFood;
	private TreeSet<Integer> poisonFood;
	private ArrayList<ArrayList<String>> ActionList;
	private ArrayList<Coordinate> StartingLocation;
	private ArrayList<Chromosome> chromosomeHistory;
	private int eatFail=0;
	private int healthyEatSuccess=0;
	private int poisonEatSuccess=0;
	private int numScans=0;
	private int numAttacked=0;
	private int numPushed=0;
	private String action = "";
	public static int width = 11;
	public static int height = 11;
	//for images/actions
    private Image ninja_walk1;
    private Image ninja_walk1_inv;
    private Image ninja_walk2;
    private Image ninja_walk2_inv;
    private Image ninja_eat;
    private Image ninja_eat_inv;
    private Image ninja_attack;
    private Image ninja_attack_inv;
    private Image ninja_push;
    private Image ninja_push_inv;
    private Image ninja_dead;
    private Image ninja_dead_inv;
    private boolean swapImage;
    private boolean facingRight;
	private char currentAction;
	
	//------------------------------------------------------------------------------------
	//--constructors--
	//------------------------------------------------------------------------------------
	public Organism(double aHealth, int chromSize, int anId, int aScanRange) {
		super(aHealth, anId, 'o');
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
		ninja_walk1 = new ImageIcon(getClass().getResource("sprites/ninja_walk1.gif")).getImage();
		ninja_walk1_inv = new ImageIcon(getClass().getResource("sprites/ninja_walk1_inv.gif")).getImage();
		ninja_walk2 = new ImageIcon(getClass().getResource("sprites/ninja_walk2.gif")).getImage();
		ninja_walk2_inv = new ImageIcon(getClass().getResource("sprites/ninja_walk2_inv.gif")).getImage();
		ninja_eat = new ImageIcon(getClass().getResource("sprites/ninja_eat.gif")).getImage();
		ninja_eat_inv = new ImageIcon(getClass().getResource("sprites/ninja_eat_inv.gif")).getImage();
		ninja_attack = new ImageIcon(getClass().getResource("sprites/ninja_attack.gif")).getImage();
		ninja_attack_inv = new ImageIcon(getClass().getResource("sprites/ninja_attack_inv.gif")).getImage();
		ninja_push = new ImageIcon(getClass().getResource("sprites/ninja_push.gif")).getImage();
		ninja_push_inv = new ImageIcon(getClass().getResource("sprites/ninja_push_inv.gif")).getImage();
		ninja_dead = new ImageIcon(getClass().getResource("sprites/ninja_dead.gif")).getImage();
		ninja_dead_inv = new ImageIcon(getClass().getResource("sprites/ninja_dead_inv.gif")).getImage();
		//create behavior tracking boolean variables
		swapImage = true;
		facingRight = true;
		currentAction = ' ';
	}
	
	public void newLocation() {
		setRange(width, height, 'w');
		int x = r.nextInt(GridPanel.WIDTH);
		int y = r.nextInt(GridPanel.HEIGHT);
		while(!canSpawn(x, y)){
			x = r.nextInt(GridPanel.WIDTH);
			y = r.nextInt(GridPanel.HEIGHT);
		}
		location = new Coordinate(x, y);
		//set boundaries
		setWrapAround(width, height);
		setRange(width, height, 'o');
	}	
	
	public void eatFood(Food f, double fdVal){
		currentAction = 'e';
		f.deplete(fdVal);
		if(f instanceof HealthyFood) {
//			System.out.println("orgId: " + id);
//			System.out.println("hlthy");
//			System.out.println("orgHealth: " + hlth);
//			System.out.println("FoodId: " + f.getId());
//			System.out.println("OrgLoc: " + this.getLocation().getX() + ", " + this.getLocation().getY());
//			System.out.println();
			incHlth(fdVal);
			setAction("Eating health food");
			healthyEatSuccess++;
			eatFail--;
			healthyFood.add(f.getId());
		}
		else if(f instanceof PoisonousFood){
//			System.out.println("orgId: " + id);
//			System.out.println("pois");
// 		    System.out.println("orgHealth: " + hlth);
//			System.out.println("FoodId: " + f.getId());
//			System.out.println("OrgLoc: " + this.getLocation().getX() + ", " + this.getLocation().getY());
//			System.out.println();
			poisonFood.add(f.getId());
			setAction("Eating poisonous food");
			poisonEatSuccess++;
			eatFail--;
			deplete(fdVal);
		}
	}
	
	public void moveNorth(LinkedList<Organism> organisms, boolean wasPushed) {
		//System.out.println("org " + this.getId() + " is moving north");
		boolean successfullyMoved = false;
		
		//make old location available.
		setRange(width, height, 'w');
		
		//if the next move is available.
		if(isAtTheTop(height)){
			if(canSpawn(location.getX(), GridPanel.HEIGHT - (((int)Math.ceil((double)height/2)) + 1))){
				location.setY(GridPanel.HEIGHT - (((int)Math.ceil((double)height/2)) + 1));
				successfullyMoved = true;
			}
		}
		else{
			try{
				if(canSpawn(location.getX(), location.getY() - 1)){
					//move there.
					location.setY(location.getY() - 1);
					successfullyMoved = true;
				}
			}
			catch(ArrayIndexOutOfBoundsException e){}
		}
		
		if(!wasPushed){
			currentAction = 'm';
			if(successfullyMoved){
				setAction("Traveling North");
			}
			else{
				setAction("Attempting to go North");
			}
		}
		
		//make current location unavailable
		setRange(width, height, 'o');
	}

	public void moveNorthEast(LinkedList<Organism> organisms, boolean wasPushed) {
		//System.out.println("org " + this.getId() + " is moving northeast");
		facingRight = true;
		boolean successfullyMoved = false;
		
		setRange(width, height, 'w');

		if(isAtTheTop(height) && isAtTheRight(width)){
			if(canSpawn(((int)Math.ceil((double)width/2)) + 1, GridPanel.HEIGHT - (((int)Math.ceil((double)height/2)) + 1))){
				location.setX(((int)Math.ceil((double)width/2)) + 1);
				location.setY(GridPanel.HEIGHT - (((int)Math.ceil((double)height/2)) + 1));
				successfullyMoved = true;
			}
		}
		else if(isAtTheTop(height)){
			if(canSpawn(location.getX() + 1, GridPanel.HEIGHT - (((int)Math.ceil((double)height/2)) + 1))){
				location.setX(location.getX() + 1);
				location.setY(GridPanel.HEIGHT - (((int)Math.ceil((double)height/2)) + 1));
				successfullyMoved = true;
			}
		}
		else if(isAtTheRight(width)){
			if(canSpawn(((int)Math.ceil((double)width/2)) + 1, location.getY() - 1)){
				location.setX(((int)Math.ceil((double)width/2)) + 1);
				location.setY(location.getY() - 1);
				successfullyMoved = true;
			}
		}
		else{
			try{
				if(canSpawn(location.getX() + 1, location.getY() - 1)){
					location.setX(location.getX() + 1);
					location.setY(location.getY() - 1);
					successfullyMoved = true;
				}
			}
			catch(ArrayIndexOutOfBoundsException e){}
		}
		
		if(!wasPushed){
			currentAction = 'm';
			if(successfullyMoved){
				setAction("Traveling Northeast");
			}
			else{
				setAction("Attempting to go Northeast");
			}
		}
		setRange(width, height, 'o');
	}

	public void moveEast(LinkedList<Organism> organisms, boolean wasPushed) {
		//System.out.println("org " + this.getId() + " is moving east");
		facingRight = true;
		boolean successfullyMoved = false;
		
		setRange(width, height, 'w');
		
		if(isAtTheRight(width)){
			if(canSpawn(((int)Math.ceil((double)width/2))+1, location.getY())){
				location.setX(((int)Math.ceil((double)width/2)) + 1);
				successfullyMoved = true;
			}
		}
		else{
			try{
				if(canSpawn(location.getX() + 1, location.getY())){
					location.setX(location.getX() + 1);
					successfullyMoved = true;
				}
			}
			catch(ArrayIndexOutOfBoundsException e){}
		}	
		
		if(!wasPushed){
			currentAction = 'm';
			if(successfullyMoved){
				setAction("Traveling East");
			}
			else{
				setAction("Attempting to go East");
			}
		}
		setRange(width, height, 'o');
	}

	public void moveSouthEast(LinkedList<Organism> organisms, boolean wasPushed) {
		//System.out.println("org " + this.getId() + " is moving southeast");
		facingRight = true;
		boolean successfullyMoved = false; 
		
		setRange(width, height, 'w');
		
		if(isAtTheBottom(height) && isAtTheRight(width)){
			if(canSpawn(((int)Math.ceil((double)width/2))+1, ((int)Math.ceil((double)height/2))+1)){
				location.setX(((int)Math.ceil((double)width/2))+1);
				location.setY(((int)Math.ceil((double)height/2))+1);
				successfullyMoved = true;
			}
		}
		else if(isAtTheBottom(height)){
			if(canSpawn(location.getX() + 1, ((int)Math.ceil((double)height/2))+1)){
				location.setX(location.getX() + 1);
				location.setY(((int)Math.ceil((double)height/2))+1);
				successfullyMoved = true;
			}
		}
		else if(isAtTheRight(width)){
			if(canSpawn(((int)Math.ceil((double)width/2))+1, location.getY()+1)){
				location.setX(((int)Math.ceil((double)width/2))+1);
				location.setY(location.getY()+1);
				successfullyMoved = true;
			}
		}
		else{
			try{
				if(canSpawn(location.getX() + 1, location.getY() + 1)){
					location.setX(location.getX() + 1);
					location.setY(location.getY() + 1);
					successfullyMoved = true;
				}
			}
			catch(ArrayIndexOutOfBoundsException e){}
		}
		
		if(!wasPushed){
			currentAction = 'm';
			if(successfullyMoved){
				setAction("Traveling Southeast");
			}
			else{
				setAction("Attempting to go Southeast");
			}
		}
		setRange(width, height, 'o');
	}

	public void moveSouth(LinkedList<Organism> organisms, boolean wasPushed) {
		//System.out.println("org " + this.getId() + " is moving south");
		boolean successfullyMoved = false; 
		
		setRange(width, height, 'w');
		
		if(isAtTheBottom(height)){
			if(canSpawn(location.getX(), ((int)Math.ceil((double)height/2)) + 1)){
				location.setY(((int)Math.ceil((double)height/2)) + 1);
				successfullyMoved = true;
			}
		}
		else{
			try{
				if(canSpawn(location.getX(), location.getY() + 1)){
					location.setY(location.getY() + 1);
					successfullyMoved = true;
				}
			}
			catch(ArrayIndexOutOfBoundsException e){}
		}
		
		if(!wasPushed){
			currentAction = 'm';
			if(successfullyMoved){
				setAction("Traveling South");
			}
			else{
				setAction("Attempting to go South");
			}
		}
		setRange(width, height, 'o');

	}

	public void moveSouthWest(LinkedList<Organism> organisms, boolean wasPushed) {
		//System.out.println("org " + this.getId() + " is moving southwest");
		facingRight = false;
		boolean successfullyMoved = false; 
		
		setRange(width, height, 'w');
		if(isAtTheBottom(height) && isAtTheLeft(width)){
			if(canSpawn(GridPanel.WIDTH - (((int)Math.ceil((double)width/2)) + 1), ((int)Math.ceil((double)height/2)) + 1)){
				location.setX(GridPanel.WIDTH - (((int)Math.ceil((double)width/2)) + 1));
				location.setY(((int)Math.ceil((double)height/2)) + 1);
				successfullyMoved = true;
			}
		}
		else if(isAtTheBottom(height)){
			if(canSpawn(location.getX() - 1, ((int)Math.ceil((double)height/2)) + 1)){
				location.setX(location.getX() - 1);
				location.setY(((int)Math.ceil((double)height/2)) + 1);
				successfullyMoved = true;
			}
		}
		else if(isAtTheLeft(width)){
			if(canSpawn(GridPanel.WIDTH - (((int)Math.ceil((double)width/2)) + 1), location.getY() + 1)){
				location.setX(GridPanel.WIDTH - (((int)Math.ceil((double)width/2)) + 1));
				location.setY(location.getY() + 1);
				successfullyMoved = true;
			}
		}
		else{
			try{
				if(canSpawn(location.getX() - 1, location.getY() + 1)){
					location.setX(location.getX() - 1);
					location.setY(location.getY() + 1);
					successfullyMoved = true;
				}
			}
			catch(ArrayIndexOutOfBoundsException e){}
		}
		
		if(!wasPushed){
			currentAction = 'm';
			if(successfullyMoved){
				setAction("Traveling Southwest");
			}
			else{
				setAction("Attempting to go Southwest");
			}
		}

		setRange(width, height, 'o');
	}

	public void moveWest(LinkedList<Organism> organisms, boolean wasPushed) {
		//System.out.println("org " + this.getId() + " is moving west");
		facingRight = false;
		boolean successfullyMoved = false;
		
		setRange(width, height, 'w');
		if(isAtTheLeft(width)){
			if(canSpawn(GridPanel.WIDTH - (((int)Math.ceil((double)width/2)) + 1), location.getY())){
				location.setX(GridPanel.WIDTH - (((int)Math.ceil((double)width/2)) + 1));
				successfullyMoved = true;
			}
		}
		else{
			try{
				if(canSpawn(location.getX() - 1, location.getY())){
					location.setX(location.getX() - 1);
					successfullyMoved = true;
				}
			}
			catch(ArrayIndexOutOfBoundsException e){}
		}
		
		if(!wasPushed){
			currentAction = 'm';
			if(successfullyMoved){
				setAction("Traveling West");
			}
			else{
				setAction("Attempting to go West");
			}
		}
		setRange(width, height, 'o');
	}

	public void moveNorthWest(LinkedList<Organism> organisms, boolean wasPushed) {
		//System.out.println("org " + this.getId() + " is moving northwest");
		facingRight = false;
		boolean successfullyMoved = false;
		
		setRange(width, height, 'w');
		if(isAtTheTop(height) && isAtTheLeft(width)){
			if(canSpawn(GridPanel.WIDTH - (((int)Math.ceil((double)width/2)) + 1), GridPanel.HEIGHT - (((int)Math.ceil((double)height/2)) + 1))){
				location.setX(GridPanel.WIDTH - (((int)Math.ceil((double)width/2)) + 1));
				location.setY(GridPanel.HEIGHT - (((int)Math.ceil((double)height/2)) + 1));
				successfullyMoved = true;
			}
		}
		else if(isAtTheTop(height)){
			if(canSpawn(location.getX() - 1, GridPanel.HEIGHT - (((int)Math.ceil((double)height/2)) + 1))){
				location.setX(location.getX() - 1);
				location.setY(GridPanel.HEIGHT - (((int)Math.ceil((double)height/2)) + 1));
				successfullyMoved = true;
			}
		}
		else if(isAtTheLeft(width)){
			if(canSpawn(GridPanel.WIDTH - (((int)Math.ceil((double)width/2)) + 1), location.getY() - 1)){
				location.setX(GridPanel.WIDTH - (((int)Math.ceil((double)width/2)) + 1));
				location.setY(location.getY() - 1);
				successfullyMoved = true;
			}
		}
		else{
			try{
				if(canSpawn(location.getX() - 1, location.getY() - 1)){
					location.setX(location.getX() - 1);
					location.setY(location.getY() - 1);
					successfullyMoved = true;
				}
			}
			catch(ArrayIndexOutOfBoundsException e){}
		}
		
		if(!wasPushed){
			currentAction = 'm';
			if(successfullyMoved){
				setAction("Traveling Northwest");
			}
			else{
				setAction("Attempting to go Northwest");
			}
		}
		setRange(width, height, 'o');
	}
	
		public void attack(int orgIndex, LinkedList<Organism> organisms){
			currentAction = 'a';
//			System.out.print("Attacking org " + orgIndex + "(" + organisms.get(orgIndex).getLocation().getX() + " " + organisms.get(orgIndex).getLocation().getY() + "). Health: " + organisms.get(orgIndex).getHealth());
			organisms.get(orgIndex).deplete(.5);
			numAttacked++;
			organisms.get(this.getId()).setAction("Attacking org " + orgIndex);
//			System.out.print(". Health: " + organisms.get(orgIndex).getHealth());
//			System.out.println(". Attacked by org " + this.id);
	}
	
		public void pushOrg(int orgIndex, LinkedList<Organism> organisms){
			currentAction = 'p';
			
			int xPushing = this.getLocation().getX();
			int yPushing = this.getLocation().getY();
			int xGettingPushed = organisms.get(orgIndex).getLocation().getX();
			int yGettingPushed = organisms.get(orgIndex).getLocation().getY();
			
//			System.out.print("Pushing org " + orgIndex + "(" + organisms.get(orgIndex).getLocation().getX() + " " + organisms.get(orgIndex).getLocation().getY() + ")");
			
			if(xGettingPushed < xPushing){
				organisms.get(orgIndex).moveWest(organisms, true);
			}
			else if(xGettingPushed > xPushing){
				organisms.get(orgIndex).moveEast(organisms,true);
			}
			
			if(yGettingPushed < yPushing){
				organisms.get(orgIndex).moveNorth(organisms,true);
			}
			else if(yGettingPushed > yPushing){
				organisms.get(orgIndex).moveSouth(organisms,true);
			}
			numPushed++;
			organisms.get(this.getId()).setAction("Pushing org " + orgIndex);
//			System.out.print("(" + organisms.get(orgIndex).getLocation().getX() + " " + organisms.get(orgIndex).getLocation().getY() + ")");
//			System.out.println(". Pushed by org " + getId());
		
	}
	
		public boolean currOrgIsNextToSpecifiedOrg(int orgIndex){
		ArrayList<Integer> surroundingOrgs = new ArrayList<Integer>();
		surroundingOrgs = this.getSurroundingObjects('o', 1);
		boolean orgIsNextToOrg = false;
		for(Integer o: surroundingOrgs){
			if(orgIndex == o) orgIsNextToOrg = true;
		}
		
		return orgIsNextToOrg;
	}
	
	//------------------------------------------------------------------------------------
	//--overloaded functions--
	//------------------------------------------------------------------------------------

	/**
	 * @return a String representation of the Object.
	 */
	public String toString(){
		String str = "";
		str += " I am an Organism " + this.getId()+ ". Fear me."
			+  "\n Location: " + location
			+  "\n Health: " + hlth
			+  "\n Status: " + getAction();
		return str;
	}
	
	//------------------------------------------------------------------------------------
	//--getters/setters--
	//------------------------------------------------------------------------------------
	public String getAction(){
		return action;
	}
	
	public void setAction(String s){
		action += s + "\n               ";
	}
	
	public void clearAction(){
		action = "";
	}
	
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
	
	public void setHlthTot(double val) {
		hlthTot = val;
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
	
	public void addAction(String action,int index){
		ActionList.get(ActionList.size()-1).add(action + " " + index);
	}
	
	public void addGeneration(){
		ActionList.add(new ArrayList<String>());
		addStartingLocation();
		addChromosome();
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
	
	public int getHealthyFoodSize(){
		return healthyFood.size();
	}
	
	public int getPoisonFoodSize(){
		return poisonFood.size();
	}

	public int getEatFail(){
		return eatFail;
	}
	
	public void setEatFail(int val) {
		eatFail = val;
	}
	
	public void addEatFail(){
		eatFail++;
	}
	
	public int getHealthEat(){
		return healthyEatSuccess;
	}
	
	public void setHealthyEat(int val) {
		healthyEatSuccess = val;
	}
	
	public int getPoisonEat(){
		return poisonEatSuccess;
	}
	
	public void setPoisonEat(int val) {
		poisonEatSuccess = val;
	}

	public int getTotalScans() {
		return numScans;
	}
	
	public void addScan(int scans){
		numScans += scans;
	}
	
	public void setTotalScans(int val) {
		numScans = val;
	}
	
	public int getScanRange(){
		return scanRange;
	}
	
	@Override
	
	public int getHeight() {
		return height;
	}
	@Override
	public int getWidth(){
		return width;
	}

	public int getNumPushed() {
		return numPushed;
	}
	
	public void setNumPushed(int val) {
		numPushed = val;
	}

	public int getNumAttacked() {
		return numAttacked;
	}
	
	public void setNumAttacked(int val) {
		numAttacked = val;
	}
	
	public void clear(){
		eatFail = 0;
		numScans = 0;
		steps = 0;
		samples = 0;
		hlthTot = 0;
		poisonFood.clear();
		healthyFood.clear();
		poisonEatSuccess = 0;
		healthyEatSuccess = 0;
		numAttacked=0;
		numPushed=0;
		
	}
	
	public void goBack(int generation){
		newLocation();
		chromosome = chromosomeHistory.get(generation-1);
		for(int i=generation;i<chromosomeHistory.size();i++){
			chromosomeHistory.remove(i);
		}
		clear();
	}
	
	public void paint(Graphics g) {
		if(facingRight){
			if(getHealth() <= 0){
				g.drawImage(ninja_dead, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
			}
			else{
				if(currentAction == 'e'){
					g.drawImage(ninja_eat, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
				}
				else if(currentAction == 'a'){
					g.drawImage(ninja_attack, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
				}
				else if(currentAction == 'p'){
					g.drawImage(ninja_push, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
				}
				else{
					if(swapImage){
						g.drawImage(ninja_walk1, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
					}
					else{
						g.drawImage(ninja_walk2, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
					}
				}
			}
		}
		else{
			if(getHealth() <= 0){
				g.drawImage(ninja_dead_inv, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
			}
			else{
				if(currentAction == 'e'){
					g.drawImage(ninja_eat_inv, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
				}
				else if(currentAction == 'a'){
					g.drawImage(ninja_attack_inv, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
				}
				else if(currentAction == 'p'){
					g.drawImage(ninja_push_inv, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
				}
				else{
					if(swapImage){
						g.drawImage(ninja_walk1_inv, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
					}
					else{
						g.drawImage(ninja_walk2_inv, location.getX()-2*Organism.width/2, location.getY()-2*Organism.height/2, 2*Organism.width, 2*Organism.height, null);
					}
				}
			}
		}
		swapImage = !swapImage;
	}
}	
