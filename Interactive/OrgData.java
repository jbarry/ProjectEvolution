package Interactive;

import java.util.ArrayList;
import java.util.List;

import Frame.Coordinate;

public class OrgData {

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
	 private double maxHealth;
	 private int id;
	 private List<Coordinate> closedList;
	 private Coordinate previousPosition;

	 public OrgData(double aMaxHealth, int anId) {
		  maxHealth = aMaxHealth;
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
		  closedList = new ArrayList<Coordinate>();
		  previousPosition = new Coordinate(-1, -1);
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
		  eatFail = 0;
		  numScans = 0;
		  steps = 0;
		  samples = 0;
		  hlthTot = 0;
		  poisonEatSuccess = 0;
		  healthyEatSuccess = 0;
		  numAttacked = 0;
		  numPushed = 0;
		  closedList.clear();
		  previousPosition = new Coordinate(-1, -1);
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

	 public String getAction() {
		  return action;
	 }

	 public void setAction(String action) {
		  this.action = action;
	 }

	 public double getFitness() {
		  return fitness;
	 }

	 public void setFitness(double fitness) {
		  this.fitness = fitness;
	 }

	 public int getSteps() {
		  return steps;
	 }

	 public void setSteps(int steps) {
		  this.steps = steps;
	 }

	 public double getHealth() {
		  return maxHealth;
	 }

	 public void setHealth(double health) {
		  this.maxHealth = health;
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

}
