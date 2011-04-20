package Interactive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
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
	private HashSet<Integer> healthyFood;
	private HashSet<Integer> poisonFood;
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
		actionList = new ArrayList<ArrayList<String>>();
		actionList.add(new ArrayList<String>());
		startingLocation = new ArrayList<Coordinate>();
		chromosomeHistory = new ArrayList<Chromosome>();
		healthyFood = new HashSet<Integer>();
		poisonFood = new HashSet<Integer>();
		
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

	public int getHealthEatSize() {
		return healthyFood.size();
	}

	public int getPoisonEatSize() {
		return poisonFood.size();
	}

	public int getTotalScans() {
		return numScans;
	}

	public void addScan(int scans) {
		numScans += scans;
	}

	public void reinitializeVariables() {
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
	}
	
	public void clearActionList() {
		actionList.clear();
	}
	
	public void clearStartingLocation() {
		startingLocation.clear();
	}
	
	public void clearClosedList() {
		closedList.clear();
	}
	
	public void clearEatFail() {
		eatFail = 1;
	}

	public void clearFoodList() {
		poisonFood.clear();
		healthyFood.clear();
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
	
	public void addHealthSuccess() {
		healthyEatSuccess++;
	}

	public int getPoisonEatSuccess() {
		return poisonEatSuccess;
	}

	public void setPoisonEatSuccess(int poisonEatSuccess) {
		this.poisonEatSuccess = poisonEatSuccess;
	}
	
	public void addPoisSuccess() {
		poisonEatSuccess++;
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
		LinkedList<Gene> listOfGenes = new LinkedList<Gene>();
		for (int i = 0; i < chrom.size(); i++) {
			LinkedList<Character> symList = (LinkedList<Character>) chrom
					.getGene(i).getSymList();
			LinkedList<Character> symListCopy = new LinkedList<Character>();
			for (int j = 0; j < symList.size(); j++)
				symListCopy.add(symList.get(j));
			listOfGenes.add(new Gene(symListCopy));
		}
		chromosomeHistory.add(new Chromosome(listOfGenes));
	}

	public void addGeneration(Chromosome chrom, Coordinate c) {
		actionList.add(new ArrayList<String>());
		startingLocation.add(c);
		addChromosome(chrom);
	}

	public void addStartingLocation(Coordinate c) {
		startingLocation.add(c);
	}

	public void setHealthyFood(HashSet<Integer> healthyFood) {
		this.healthyFood = healthyFood;
	}

	public HashSet<Integer> getHealthyFood() {
		return healthyFood;
	}
	
	public void addHealthyFood(Integer i) {
		healthyFood.add(i);
	}

	public void setPoisonFood(HashSet<Integer> poisonFood) {
		this.poisonFood = poisonFood;
	}

	public HashSet<Integer> getPoisonFood() {
		return poisonFood;
	}
	
	public void addPoisFood(Integer i ) {
		poisonFood.add(i);
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

	public void subEatFail() {
		eatFail--;
	}
	
	public void addAction(String action, int index) {
		actionList.get(actionList.size() - 1).add(action + " " + index);
	}
	
	public Chromosome goBack(int generation) {
		Chromosome toReturnChrom = chromosomeHistory.get(generation - 1);
		for (int i = generation; i < chromosomeHistory.size(); i++)
			chromosomeHistory.remove(i);
		return toReturnChrom;
	}

	public void addEatFoodData(Character type, int id) {
		if (type == 'h') {
			System.out.println("Ate healthy Food!!");
			addHealthyFood(id);
			addHealthSuccess();
		} else {
			System.out.println("Ate Pois Food!!");
			addPoisFood(id);
			addPoisSuccess();
		}
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
