package Testing;

import java.util.LinkedList;

import Evolution.GEP;
import Interactive.Organism;

public class GEPTest {
	public static void main(String[] args) {
		LinkedList <Organism> org = new LinkedList<Organism>();
		for(int i = 0; i < 4; i++) {
			org.add(new Organism(true, 4));
		}
		GEP gep = new GEP(org, 1.00, 1.00, 1.00, 1.00, 1.00);
		org = gep.getOrgList();
	}
}