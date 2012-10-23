package Interactive;

import Frame.GridPanel;

public class OrganismBuilder extends MatterBuilder<OrganismBuilder> {
	
	public Organism build() {
		return new Organism(this);
	}
	
	public OrganismBuilder withGridPanel(GridPanel gridPanel) {
		this.gridPanel = gridPanel;
		return this;
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
	
	public OrganismBuilder withNumberOfGenes(int chromosomeSize) {
		this.numberOfGenes = chromosomeSize;
		return this;
	}
	
	@Override
	protected OrganismBuilder getThis() {
		return this;
	}

	String action;
	double fitness;
	OrgData orgData;
	int numberOfGenes;
	GridPanel gridPanel;
}