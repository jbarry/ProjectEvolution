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
		LinkedList <Organism> org = new LinkedList<Organism>();
		Random r = new Random();
		for(int i = 0; i < 4; i++) {
			org.add(new Organism(true, 4));
		}
		GEP gep = new GEP(org, 0.00, 1.00, 1.00, 1.00, 1.00);
		org = gep.getOrgList();
	}
}