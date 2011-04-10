package Evolution;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import Interactive.Chromosome;
import Interactive.Organism;

@RunWith(value = Parameterized.class)
public class GEPTest {

	
	private List<Organism> orgList;
	/*private int number;*/
	
	public GEPTest(List<Organism> anOrgList) {
		this.orgList = anOrgList;
	}
	
	/*public GEPTest(int number) {
		this.number = number;
	}*/

	@Before
	public void setUp() throws Exception {
			/*Random ran = new Random();
			LinkedList<Organism> orgList = new LinkedList<Organism>();
			for (int i = 0; i < 7; i++)
				orgList.add(new Organism(true, 7, ran.nextInt(20), i));
			GEP gep = new GEP(orgList, 1.0, 1.0, 1.0, 1.0, 1.0, 3, true, true);*/
	}

	@After
	public void tearDown() throws Exception {
		/*orgList.clear();*/
	}
	
	/*// Types of orgLists:
	// - even num
	// - odd num
	// - all 0 <= fitness < 1.
	 @Parameters
	 public static Object[][] data() {
		Random ran = new Random();
		LinkedList<Organism> orgList1 = new LinkedList<Organism>();
		for (int i = 0; i < 7; i++) // Odd num.
			orgList1.add(new Organism(true, 7, ran.nextInt(20), i));
		LinkedList<Organism> orgList2 = new LinkedList<Organism>();
		for (int i = 0; i < 6; i++) // Even num.
			orgList2.add(new Organism(true, 7, ran.nextInt(20), i));
		
		LinkedList<Organism> orgList3 = new LinkedList<Organism>();
		for (int i = 0; i < 4; i++) // All have fitness of zero.
			orgList3.add(new Organism(true, 7, 0.0, i));
		LinkedList<Organism> orgList4 = new LinkedList<Organism>();
		for (int i = 0; i < 4; i++) // All have same fitness not equal to zero.
			orgList4.add(new Organism(true, 7, 0.0, i));
		LinkedList<Organism> orgList5 = new LinkedList<Organism>();
		LinkedList<Organism> orgList6 = new LinkedList<Organism>();
		LinkedList<Organism> orgList7 = new LinkedList<Organism>();
		LinkedList<Organism> orgList8 = new LinkedList<Organism>();
		
		LinkedList<Organism>[][] data = new Collection<Object[]> { { orgList1 }, { orgList2 } };
		return data;
	 }*/

	 /*@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { 1 }, { 2 }, { 3 }, { 4 } };
		return Arrays.asList(data);
	}*/

	/*@Test
	public void pushTest() {
		System.out.println("number: " + number);
	}*/
	 
 	@Test
	public final void pushTest2() {
 		for (int i = 0; i < orgList.size(); i++) {
 			Organism org = orgList.get(i);
 			System.out.println("orgID: " + org.getId());
 			/*Chromosome chrom = org.getChromosome();*/
		}
 		System.out.println();
 	}
}