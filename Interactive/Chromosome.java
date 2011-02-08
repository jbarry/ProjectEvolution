package Interactive;

import java.awt.List;
import java.util.Arrays;
import java.util.Random;
import java.util.Queue;	
import java.util.LinkedList;
@SuppressWarnings("all")
public class Chromosome implements Crossable{

	private char[] terminals = {'/','+','-','*'};
	private char[] nonTerminals = {'a','b','c','d'};
	private char[][] chromosome = new char[10][10];
	private Random r;

	private List geneticmaterial;
	
	public Chromosome() {
		r = new Random();
		for(int i=0;i<chromosome.length;i++){
			for(int j=0;j<chromosome[i].length;j++){
				if(r.nextInt(2)==0){
					chromosome[i][j]=terminals[r.nextInt(terminals.length)];
				}
				else{
					chromosome[i][j]=nonTerminals[r.nextInt(nonTerminals.length)];
				}
			}
		}
	}
	
	public char[][] getChromomsome() {
		return chromosome;
	}
	
	public void rotate(int gene) {
		char[] theGene = chromosome[gene];
		int geneSize = theGene.length;
		int rotNum = r.nextInt(theGene.length);
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
		int changeGene = r.nextInt(aGene.length);
		if(r.nextBoolean()) {
			aGene[changeGene] = terminals[r.nextInt(terminals.length)];
		} else {
			aGene[changeGene] = nonTerminals[r.nextInt(nonTerminals.length)];
		}
	}

	@Override
	public Pair crossover(Crossable other) {
		// TODO Auto-generated method stub
		return null;
	}
}
