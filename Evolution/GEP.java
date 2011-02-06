package Evolution;

import Interactive.Chromosome;
import Interactive.Organism;
import java.util.Random;
import java.util.Arrays;

import static java.lang.System.out;
import java.util.ArrayList;
public class GEP {

	// Class variables.
	private Organism[] orgList;
	private Chromosome[] chromList;
	private Random ran;

	public GEP(Organism[] orgList, double prob, double mutProb, 
			double rotProb, double onePtProb, double twoPtProb) {
		this.orgList = orgList;
		ran = new Random();
		
		//Assess the fitness of each organism
		//Case1: Fitness will be assessed based on the 
		//organism with the most amount of health left
		//receives the highest fitness.
		for(int i = 0; i < orgList.length; i++) {
			orgList[i].setFitness(fitness(orgList[i]));
		}
		
		tournament(orgList, 2, prob);
		rotation(chromList, rotProb);
		mutation(chromList, mutProb);
		chromList = twoPointCross(onePointCross(chromList, onePtProb), twoPtProb);
		
		//TODO: should be no need for this. If organisms handle themselves.
		//redistribute the chromosomes into the same organisms.
//		for(int i = 0; i < orgList.length; i++) {
//			orgList[i].setChromosome(chromList[i]);
//		}
	}

	/**
	 * This method assigns a double representing fitness of each organism.
	 * @param org - a single organism to be assessed.
	 * @return a double representing the evaluated fitness of the organism.
	 */
	private double fitness(Organism org) {
		return org.getHealth();
	}
	
	private Chromosome[] tournament(Organism [] generation, int tournSize, double prob) {
		Chromosome[] aChromList = new Chromosome[generation.length];
		int size = 0;
		Organism cur = generation[0];
		while(size != aChromList.length) {
			int i = 0, num = 0;
			while(num != tournSize) {
				if(ran.nextDouble() < prob) {
					if(cur.getFitness() < generation[i].getFitness()) {
						if(generation[i] != null) {
							cur = generation[i];
						}
					}
					num++;
				}
				i++;
			}
			aChromList[size] = cur.getChromosome();
			orgList[i] = null;
			size++;
		}
		return aChromList;
	}

	private void mutation(Chromosome[] generation, double prob) {
		for(Chromosome chrom: generation) {
			if(ran.nextDouble() < prob) {
				int gene = ran.nextInt((chrom.getChrom()[0].length));
				chrom.mutate(gene);
			}
		}
	}

	private Chromosome[] onePointCross(Chromosome[] generation, double prob) {
		Chromosome[][] pairList = mateSelect(generation);
		for(Chromosome[] gene: pairList) {
			if(ran.nextDouble() < prob) {
				
			}
		}
		return null;
	}
	
	private Chromosome[] twoPointCross(Chromosome[] generation, double prob) {
		Chromosome[][] pairList = mateSelect(generation);
		for(Chromosome[] gene: pairList) {
			if(ran.nextDouble() < prob) {
				
			}
		}
		return null;
	}
	
	private Chromosome[][] mateSelect(Chromosome[] generation) {
		ArrayList<Chromosome> chromArList = 
			new ArrayList<Chromosome>(Arrays.asList(generation));
		Chromosome[][] pairList = new Chromosome[generation.length/2][2];
		for(int i = 0; i < chromArList.size(); i++) {
				int mate = ran.nextInt(chromArList.size());
				Chromosome[] pair = {chromArList.get(i), chromArList.get(mate)};
				pairList[i] = pair;
				chromArList.remove(i);
				chromArList.remove(mate);
		}
		return pairList;
	}
	
	private void rotation(Chromosome[] generation, double prob) {
		for(Chromosome chrom: generation) {
			if(ran.nextDouble() <= prob) {
				int x = ran.nextInt(chrom.getChrom().length);
				chrom.rotate(x);
			}
		}
	}
	
	public Organism[] getOrgList() {
		return orgList;
	}
	
	public static void main(String[] args) {
		Organism[] org = new Organism[10];
		Arrays.fill(org, new Organism());
//		GEP gep = new GEP(org);
//		org = gep.getOrgList();
	}
}
