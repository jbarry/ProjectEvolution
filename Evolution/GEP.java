package Evolution;

import Interactive.Chromosome;
import Interactive.Organism;
import Interactive.Pair;

import java.util.Random;
import java.util.Arrays;
import java.util.LinkedList;
import java.lang.Double;
import java.awt.List;

import static java.lang.System.out;
@SuppressWarnings("all")
public class GEP {

	// Class variables.
	private LinkedList<Organism> orgList;
	private LinkedList<Chromosome> chromList = new LinkedList<Chromosome>();
	private Random ran;
	private double tournProb;
	private double mutProb;
	private double rotProb;
	private double onePtProb;
	private double twoPtProb;

	public GEP(LinkedList <Organism> anOrgList, double aTournProb, double aMutProb, 
			double aRotProb, double aOnePtProb, double aTwoPtProb) {
		orgList = anOrgList;
		ran = new Random();
		tournProb=aTournProb;
		mutProb=aMutProb;
		rotProb=aRotProb;
		onePtProb=aOnePtProb;
		twoPtProb=aTwoPtProb;
		for(Organism o: orgList){
			chromList.add(o.getChromosome());
		}
		
		//Assess the fitness of each organism
		//Case1: Fitness will be assessed based on the 
		//organism with the most amount of health left
		//receives the highest fitness.
		for(int i = 0; i < orgList.size(); i++) {
			orgList.get(i).setFitness(fitness(orgList.get(i)));
		}
		
		//TODO: another way of carrying fitness info for organisms. They have no knowledge of
		//their own fitness.
		LinkedList<Pair<Organism, Double>> orgFitPairList =
			new LinkedList<Pair<Organism, Double>>();
		for(int i = 0; i < orgList.size(); i++) {
			orgFitPairList.add(new Pair<Organism,
					Double>(orgList.get(i), fitness(orgList.get(i))));
		}
		//TODO:tournament method with orgFitListPair
		
		/*rotation(chromList);
		mutation(chromList);
		chromList = tournament(orgList, 2);
		chromList = twoPointCross(onePointCross(chromList, onePtProb), twoPtProb);
		*/
		
		//TODO: should be no need for this. If organisms handle themselves.
		//redistribute the chromosomes into the same organisms.
//		for(int i = 0; i < orgList.length; i++) {
//			orgList.get(i).setChromosome(chromList[i]);
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
	
	//TODO: redo with pairs
	public LinkedList<Chromosome> tournament(LinkedList<Organism> generation, int tournSize) {
		LinkedList<Chromosome> aChromList = new LinkedList<Chromosome>();
		int size = 0;
		Organism cur = generation.get(0);
		while(size != aChromList.size()) {
			int i = 0, num = 0;
			while(num != tournSize) {
				if(ran.nextDouble() < tournProb) {
					if(cur.getFitness() < generation.get(i).getFitness()) {
						if(generation.get(i) != null) {
							cur = generation.get(i);
						}
					}
					num++;
				}
				i++;
			}
			aChromList.set(size, cur.getChromosome());
			orgList.set(i,null);
			size++;
		}
		return aChromList;
	}
	
//	TODO: TournSize always 2.
	private LinkedList<Pair<Pair<Organism, Double>, Pair<Organism,Double>>> tournament2(
			LinkedList<Pair<Organism, Double>> generation, double prob) {
		LinkedList<Pair<Pair<Organism, Double>, Pair<Organism, Double>>> pairList =
			new LinkedList<Pair<Pair<Organism, Double>, Pair<Organism, Double>>>();
		for(Pair<Organism, Double> org: generation) {
			int partner = ran.nextInt(generation.size());
			pairList.add(new Pair< Pair<Organism, Double> , Pair<Organism,Double> >(org, generation.get(partner)));
		}
		return pairList;
	}
	
	public void mutation(LinkedList<Chromosome> aChromList) {
		for(Chromosome chrom: aChromList) {
			if(ran.nextDouble() < mutProb) {
				int gene = ran.nextInt((chrom.getChromomsome()[0].length));
				chrom.mutate(gene);
			}
		}
		chromList=aChromList;
	}
	
	public void rotation(LinkedList<Chromosome> aChromList) {
		for(Chromosome chrom: aChromList) {
			if(ran.nextDouble() <= rotProb) {
				int rotAmt = ran.nextInt(chrom.getChromomsome().length);
				chrom.rotate(rotAmt);
			}
		}
		chromList=aChromList;
	}

	private LinkedList<Chromosome> onePointCross(LinkedList<Chromosome> chromList2, double prob) {
		Chromosome[][] pairList = mateSelect(chromList2);
		for(Chromosome[] gene: pairList) {
			if(ran.nextDouble() < prob) {
				
			}
		}
		return null;
	}
	
	private LinkedList<Chromosome> twoPointCross(LinkedList<Chromosome> generation, double prob) {
		Chromosome[][] pairList = mateSelect(generation);
		for(Chromosome[] gene: pairList) {
			if(ran.nextDouble() < prob) {
				
			}
		}
		return null;
	}
	
	private Chromosome[][] mateSelect(LinkedList<Chromosome> generation) {
		Chromosome[][] pairList = new Chromosome[generation.size()/2][2];
		for(int i = 0; i < generation.size(); i++) {
				int mate = ran.nextInt(generation.size());
				Chromosome[] pair = {generation.get(i), generation.get(mate)};
				pairList[i] = pair;
				generation.remove(i);
				generation.remove(mate);
		}
		return pairList;
	}
	
	
	public LinkedList<Organism> getOrgList() {
		return orgList;
	}
	
	public LinkedList<Chromosome> getChromList(){
		return chromList;
	}
	
}
