package Interactive;

import java.awt.List;
import java.util.Arrays;
import java.util.Random;
import java.util.Queue;	
import java.util.LinkedList;

public class Chromosome implements Crossable{

	private char[] terminals;
	private char[] nonTerminals;
	private char[][] chromosome;
	private Random ran;

	private List geneticmaterial;
	public Chromosome() {
		ran = new Random();
	}
	
	public char[][] getChrom() {
		return chromosome;
	}
	
	public void rotate(int gene) {
		char[] theGene = chromosome[gene];
		int geneSize = theGene.length;
		int rotNum = ran.nextInt(theGene.length);
		char[] temp = theGene;
		for(int i = rotNum , j = 0; i < geneSize; i++, j++) {
				theGene[j] = temp[i];
		}
		for(int i = 0; i < geneSize; i++, rotNum++) {
			theGene[rotNum] = temp[i];
		}
	}
	
	public void mutate(int gene) {
		char[] aGene = chromosome[gene];
		int changeGene = ran.nextInt(aGene.length);
		if(ran.nextBoolean()) {
			aGene[changeGene] = terminals[ran.nextInt(terminals.length)];
		} else {
			aGene[changeGene] = nonTerminals[ran.nextInt(terminals.length)];
		}
	}

	@Override
	public Pair crossover(Crossable other) {
		// TODO Auto-generated method stub
		return null;
	}
}
