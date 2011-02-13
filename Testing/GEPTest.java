<<<<<<< HEAD
package Testing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import Evolution.GEP;
import Interactive.Chromosome;
import Interactive.Gene;
import Interactive.Organism;

@SuppressWarnings("all")
public class GEPTest {
	
	public static void main(String[] args) {
		ArrayList<Character> symList = new ArrayList<Character>();
		symList.add(new Character('a'));
		symList.add(new Character('b'));
		symList.add(new Character('c'));
		symList.add(new Character('d'));
		symList.add(new Character('e'));
		symList.add(new Character('f'));
		symList.add(new Character('g'));
		symList.add(new Character('h'));
		symList.add(new Character('i'));
		symList.add(new Character('j'));
		LinkedList <Organism> org = new LinkedList<Organism>();
		Random r = new Random();
		for(int i = 0; i < 5; i++) {
			LinkedList<Gene> genes = new LinkedList<Gene>();
			for (int j = 0; j < 3; j++){
				LinkedList<Character> symList1 = new LinkedList<Character>();
				for(int q = 0; q < 3; q++) {
					symList1.add(new Character(symList.get(r.nextInt(symList.size()))));
				}
				genes.add(new Gene(symList1));
			}
			Chromosome chrom = new Chromosome(genes);
			org.add(new Organism((double)(r.nextDouble()*100.0), chrom));
		}
		Character mut = symList.get(r.nextInt(symList.size()));
		GEP g= new GEP(org, 1, 1, 1, 1, 1);
		// org = gep.getOrgList();
	}
=======
package Testing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import Evolution.GEP;
import Interactive.Chromosome;
import Interactive.Gene;
import Interactive.Organism;

public class GEPTest {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		ArrayList<Character> symList = new ArrayList<Character>();
		symList.add(new Character('a'));
		symList.add(new Character('b'));
		symList.add(new Character('c'));
		symList.add(new Character('d'));
		symList.add(new Character('e'));
		symList.add(new Character('f'));
		symList.add(new Character('g'));
		symList.add(new Character('h'));
		symList.add(new Character('i'));
		symList.add(new Character('j'));
		LinkedList <Organism> org = new LinkedList<Organism>();
		Random r = new Random();
		for(int i = 0; i < 5; i++) {
			LinkedList<Gene> genes = new LinkedList<Gene>();
			for (int j = 0; j < 3; j++){
				LinkedList<Character> symList1 = new LinkedList<Character>();
				for(int q = 0; q < 3; q++) {
					symList1.add(new Character(symList.get(r.nextInt(symList.size()))));
				}
				genes.add(new Gene(symList1));
			}
			Chromosome chrom = new Chromosome(genes);
			org.add(new Organism((double)(r.nextDouble()*100.0), chrom));
		}
		Character mut = symList.get(r.nextInt(symList.size()));
		@SuppressWarnings("unused")
		GEP gep = new GEP(org, 1.00, true, 1.00, 1.00, 1.00, 1.00, mut);
		// org = gep.getOrgList();
	}
>>>>>>> 9fe0fb76a9111c87e78158b3935a10a8dc8c820d
}