package Testing;

import java.util.LinkedList;
import Interactive.Organism;
import Evolution.GEP;
import Interactive.*;

@SuppressWarnings("all")
public class GEPTest {

	private static LinkedList <Organism> orgList;
	private static LinkedList <Chromosome> chromList;
	
	public static void main(String[] args){
		orgList= new LinkedList<Organism>();
		for(int i=0;i<10;i++){
			orgList.add(new Organism());
		}
		for(Organism o : orgList){
			chromList.add(o.getChromosome());
		}
		
		GEP g = new GEP(orgList,.25,.05,.05,.05,.05);
	}
}
