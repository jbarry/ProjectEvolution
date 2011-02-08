package Testing;

import java.util.LinkedList;
import Interactive.Organism;
import Evolution.GEP;
import Interactive.*;

@SuppressWarnings("all")
public class GEPTest {

	private static LinkedList<Organism> orgList;
	private static LinkedList<Chromosome> chromListBefore;
	private static LinkedList<Chromosome> chromListAfter;
	private static GEP g;
	
	private static LinkedList<Chromosome> testMutation_Rotation_Selection(){
		g.mutation(chromListBefore);
		g.rotation(chromListBefore);
		LinkedList<Chromosome> toReturn=new LinkedList<Chromosome>();
		toReturn=g.getChromList();
		System.out.println("--Chromosomes before mutation/rotation--");
		for(int i=0; i<chromListBefore.size();i++){
			if(i!=0){
				System.out.println();
				System.out.println();
			}
			System.out.print("Organism " + i + ": ");
			chromListBefore.get(i).printChromosome();
		}
		
		toReturn=g.getChromList();
		System.out.println();
		System.out.println();
		System.out.println("--Chromosomes after mutation/rotation--");
		for(int i=0; i<toReturn.size();i++){
			if(i!=0){
				System.out.println();
				System.out.println();
			}
			System.out.print("Organism " + i + ": ");
			toReturn.get(i).printChromosome();
		}
			System.out.println();
			System.out.println();
		return toReturn;
		
	}
	
	
	
	public static void main(String[] args){
		orgList= new LinkedList<Organism>();
		chromListBefore= new LinkedList<Chromosome>();
		chromListAfter= new LinkedList<Chromosome>();
		for(int i=0;i<10;i++){
			orgList.add(new Organism());
		}
		for(Organism o : orgList){
			chromListBefore.add(o.getChromosome());
		}
		
		g = new GEP(orgList,.25,1.0,1.0,1.0,.05);
		chromListAfter=testMutation_Rotation_Selection();
		
	}
}
