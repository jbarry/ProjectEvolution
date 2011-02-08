package Interactive;

import java.awt.List;
import java.util.Arrays;
import java.util.Random;
import java.util.Queue;	
import java.util.LinkedList;
@SuppressWarnings("all")
public class Chromosome implements Crossable{
	
	public int numGenes;
	public int lenGenes;
	private char[] terminals = {'/','+','-','*'};
	private char[] nonTerminals = {'a','b','c','d'};
	private char[][] chromosome;
	private Random r;

	private List geneticmaterial;
	
	public Chromosome() {
		r = new Random();
		numGenes=10;
		lenGenes=10;
		chromosome = new char[numGenes][lenGenes];
		for(int i=0;i<numGenes;i++){
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
	
	public Chromosome(int aNumGenes,int aLenGenes){
		r = new Random();
		numGenes=aNumGenes;
		lenGenes=aLenGenes;
		chromosome = new char[numGenes][lenGenes];
		for(int i=0;i<numGenes;i++){
			for(int j=0;j<chromosome[i].length;j++){
				if(r.nextBoolean()){
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
	
	public void printChromosome(){
		for(int i=0;i<numGenes;i++){
			for(int j=0;j<chromosome[i].length;j++){
				System.out.print(chromosome[i][j]);
			}
			System.out.print("  ");
		}
	}
	
	public void rotate(int gene) {
		char[] theGene = chromosome[gene];
		int geneSize = theGene.length;
		int rotNum=0;
		while(rotNum==0){
		rotNum = r.nextInt(theGene.length);
		}
		char[] temp = new char[geneSize];
		for(int i = 0; i<geneSize;i++){
			temp[i]=theGene[i];
		}
		
		/*for(int i = rotNum , j = 0; i < geneSize; i++, j++) {
				theGene[j] = temp[i];
		}
		*/
		
		for(int j = 0; j<geneSize;j++){
			if(rotNum+j<geneSize){
				theGene[j+rotNum]=temp[j];
			}
			else{
				theGene[(j+rotNum)%geneSize]=temp[j];
			}
		
		}
		chromosome[gene]=theGene;
		
	}
	
	public void mutate(int gene) {
		char[] aGene = chromosome[gene];
		int changeGene = r.nextInt(aGene.length);
		if(r.nextBoolean()) {
			aGene[changeGene] = terminals[r.nextInt(terminals.length)];
		} else {
			aGene[changeGene] = nonTerminals[r.nextInt(nonTerminals.length)];
		}
		chromosome[gene]=aGene;
	}

	@Override
	public Pair crossover(Crossable other) {
		// TODO Auto-generated method stub
		return null;
	}
}
