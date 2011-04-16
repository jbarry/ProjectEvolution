package Interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import Frame.Coordinate;

public class OrgData {

	private int eatFail;
	private int healthyEatSuccess;
	private int poisonEatSuccess;
	private int numScans;
	private int numAttacked;
	private int numPushed;
	private double hlthTot;
	private int samples;
	private int steps;
	private double maxHealth;
	private int id;
	private int timeOfDeath;
	private double averageHealth;
	private List<Coordinate> closedList;
	private Coordinate previousPosition;
	private TreeSet<Integer> healthyFood;
	private TreeSet<Integer> poisonFood;
	private ArrayList<ArrayList<String>> actionList;
	private ArrayList<Coordinate> startingLocation;
	private ArrayList<Chromosome> chromosomeHistory;
	private int lastFoodSourceDestination;

	public OrgData(double aMaxHealth, int anId) {
		maxHealth = aMaxHealth;
		id = anId;
		eatFail = 1;
		healthyEatSuccess = 1;
		poisonEatSuccess = 1;
		numScans = 1;
		numAttacked = 1;
		numPushed = 1;
		String action = "";
		hlthTot = 1;
		samples = 1;
		steps = 1;
		timeOfDeath = 1;
		closedList = new ArrayList<Coordinate>();
		lastFoodSourceDestination = 0;
		averageHealth = 1;
		previousPosition = new Coordinate(0, 0);
		healthyFood = new TreeSet<Integer>();
		poisonFood = new TreeSet<Integer>();
		actionList = new ArrayList<ArrayList<String>>();
		startingLocation = new ArrayList<Coordinate>();
		chromosomeHistory = new ArrayList<Chromosome>();
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

	public int getNumPushed() {
		return numPushed;
	}

	public int getNumAttacked() {
		return numAttacked;
	}

	public int getEatFail() {
		return eatFail;
	}

	public void addEatFail() {
		eatFail++;
	}

	public int getHealthEat() {
		return healthyEatSuccess;
	}

	public int getPoisonEat() {
		return poisonEatSuccess;
	}

	public int getTotalScans() {
		return numScans;
	}

	public void addScan(int scans) {
		numScans += scans;
	}

	public void clear() {
		eatFail = 1;
		healthyEatSuccess = 1;
		poisonEatSuccess = 1;
		numScans = 1;
		numAttacked = 1;
		numPushed = 1;
		String action = "";
		hlthTot = 1;
		samples = 1;
		steps = 1;
		timeOfDeath = 1;
		closedList.clear();
		lastFoodSourceDestination = 0;
		averageHealth = 1;
		previousPosition = new Coordinate(0, 0);
		healthyFood.clear();
		poisonFood.clear();
		actionList.clear();
		startingLocation.clear();
		chromosomeHistory.clear();
	}

	public void incHlthTot() {
		hlthTot += maxHealth;
		samples++;
	}

	public double getHlthTot() {
		return hlthTot;
	}

	public int getHealthyEatSuccess() {
		return healthyEatSuccess;
	}

	public void setHealthyEatSuccess(int healthyEatSuccess) {
		this.healthyEatSuccess = healthyEatSuccess;
	}

	public int getPoisonEatSuccess() {
		return poisonEatSuccess;
	}

	public void setPoisonEatSuccess(int poisonEatSuccess) {
		this.poisonEatSuccess = poisonEatSuccess;
	}

	public int getNumScans() {
		return numScans;
	}

	public void setNumScans(int numScans) {
		this.numScans = numScans;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(double aMaxHealth) {
		maxHealth = aMaxHealth;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEatFail(int eatFail) {
		this.eatFail = eatFail;
	}

	public void setNumAttacked(int numAttacked) {
		this.numAttacked = numAttacked;
	}

	public void setNumPushed(int numPushed) {
		this.numPushed = numPushed;
	}

	public void setHlthTot(double hlthTot) {
		this.hlthTot = hlthTot;
	}

	public void setSamples(int samples) {
		this.samples = samples;
	}

	public void setClosedList(List<Coordinate> closedList) {
		this.closedList = closedList;
	}

	public List<Coordinate> getClosedList() {
		return closedList;
	}

	public void clearClosedList() {
		closedList.clear();
	}

	public Coordinate getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(int x, int y) {
		previousPosition.setX(x);
		previousPosition.setY(y);
	}

	public int getTimeOfDeath() {
		return timeOfDeath;
	}

	public void setTimeOfDeath(int timeOfDeath) {
		this.timeOfDeath = timeOfDeath;
	}

	public void setAverageHealth(double averageHealth) {
		this.averageHealth = averageHealth;
	}

	public double getAverageHealth() {
		return averageHealth;
	}

	public void incrementSumHealth(double currentHealth) {
		hlthTot += currentHealth;
	}

	public ArrayList<String> getActions(int generation) {
		return actionList.get(generation);
	}

	public int getHealthyFoodSize() {
		return healthyFood.size();
	}

	public int getPoisonFoodSize() {
		return poisonFood.size();
	}

	public void addChromosome(Chromosome chrom) {
		chromosomeHistory.add(chrom);
	}

	public void addGeneration(Chromosome chrom, Coordinate c) {
		actionList.add(new ArrayList<String>());
		startingLocation.add(c);
		addChromosome(chrom);
	}

	public void addStartingLocation(Coordinate c) {
		startingLocation.add(c);
	}

	public void setHealthyFood(TreeSet<Integer> healthyFood) {
		this.healthyFood = healthyFood;
	}

	public TreeSet<Integer> getHealthyFood() {
		return healthyFood;
	}

	public void setPoisonFood(TreeSet<Integer> poisonFood) {
		this.poisonFood = poisonFood;
	}

	public TreeSet<Integer> getPoisonFood() {
		return poisonFood;
	}

	public void setActionList(ArrayList<ArrayList<String>> actionList) {
		this.actionList = actionList;
	}

	public ArrayList<ArrayList<String>> getActionList() {
		return actionList;
	}

	public int getLastFoodSourceDestination() {
		// TODO Auto-generated method stub
		return lastFoodSourceDestination;
	}
	
	public void setLastFoodSourceIndex(int aLastFoodSourceIndex) {
		lastFoodSourceDestination = aLastFoodSourceIndex;
	}
	
	/*public void goBack(int generation) {
		int x = location.getX();
		int y = location.getY();
		LocationMap.getInstance().newLocation(location, width, height, id, 'o');
		chromosome = chromosomeHistory.get(generation - 1);
		for (int i = generation; i < chromosomeHistory.size(); i++) {
			chromosomeHistory.remove(i);
		}
		clear();
	}*/
}
