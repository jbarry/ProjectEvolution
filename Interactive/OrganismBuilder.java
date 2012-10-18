package Interactive;

public class OrganismBuilder extends MatterBuilder<OrganismBuilder> {
	
	public Organism build() {
		return new Organism(this);
	}
	
	public OrganismBuilder withWidth(int width) {
		this.width = width;
		return this;
	}
	
	public OrganismBuilder withHeight(int height) {
		this.height  = height;
		return this;
	}
	
	public OrganismBuilder withAction(String action) {
		this.action = action;
		return this;
	}
	
	public OrganismBuilder withFitness(double fitness) {
		this.fitness = fitness;
		return this;
	}
	
	public OrganismBuilder withOrgData(OrgData orgData) {
		this.orgData = orgData;
		return this;
	}
	
	public OrganismBuilder withNumActions(int numActions) {
		this.numActions = numActions;
		return this;
	}
	
	public OrganismBuilder withNumberOfGenes(int chromosomeSize) {
		this.numberOfGenes = chromosomeSize;
		return this;
	}
	
	@Override
	protected OrganismBuilder getThis() {
		// TODO Auto-generated method stub
		return this;
	}

	private Organism org;
	
	int width = 5;
	int height = 5;
	String action;
	double fitness;
	OrgData orgData;
	int numActions;
	int numberOfGenes;
	int id;
	int health;
}
