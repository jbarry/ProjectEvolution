package Interactive;

import java.util.TreeSet;

public class OrgData{
	
	private int eatFail;
	private int healthyEatSuccess;
	private int poisonEatSuccess;
	private int numScans;
	private int numAttacked;
	private int numPushed;
	private String action;
	private double fitness;
	private double hlthTot;
	private int samples;
	private int steps;
	private double health;
	private int id;
	
	public OrgData(double aHealth, int anId) {
		health = aHealth;
		id = anId;
		
		eatFail = 0;
		healthyEatSuccess = 0;
		poisonEatSuccess = 0;
		numScans = 0;
		numAttacked = 0;
		numPushed = 0;
		String action = "";
		fitness = 0;
		hlthTot = 0;
		samples = 0;
		steps = 0;
		
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
	public int getEatFail(){
		return eatFail;
	}
	
	public void addEatFail(){
		eatFail++;
	}
	public int getHealthEat(){
		return healthyEatSuccess;
	}
	
	public int getPoisonEat(){
		return poisonEatSuccess;
	}
	

	public int getTotalScans() {
		return numScans;
	}
	
	public void addScan(int scans){
		numScans += scans;
	}
	public void clear(){
		eatFail = 0;
		numScans = 0;
		steps = 0;
		samples = 0;
		hlthTot = 0;
		poisonEatSuccess = 0;
		healthyEatSuccess = 0;
		numAttacked=0;
		numPushed=0;
		
	}
	public void incHlthTot() {
		hlthTot+=health;
		samples++;
	}
	
	public double getHlthTot() {
		return hlthTot;
	}
}
