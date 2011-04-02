package Evolution;

import static org.junit.Assert.*;

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

import Interactive.Organism;

@RunWith(value = Parameterized.class)
public class GEPTest {

	
	private List<Organism> orgList;

	@Before
	public void setUp() throws Exception {
			Random ran = new Random();
			LinkedList<Organism> orgList = new LinkedList<Organism>();
			for (int i = 0; i < 7; i++)
				orgList.add(new Organism(true, 7, ran.nextInt(20), i));
			GEP gep = new GEP(orgList, 1.0, 1.0, 1.0, 1.0, 1.0, 3, true, true);
	}

	@After
	public void tearDown() throws Exception {
		orgList.clear();
	}

	// Types of orgLists:
	// - even num
	// - odd num
	// - all 0 <= fitness < 1.
	 /*@Parameters
	 public static List<Organism>[][] data() {
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
//		LinkedList<Organism>[][] data = new LinkedList<Organism>[][];
		return data;
	 }*/

 	@Test
	public final void testFitness() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFitnessIan() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFitnessAvgHealth() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFitnessDwight() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFitnessAvgHealthPerSteps() {
		
	}

	@Test
	public final void testNewGeneration() {
		
	}

	@Test
	public final void testMain() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetLineNumber() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testObject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetClass() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testHashCode() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testEquals() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testClone() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testNotify() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testNotifyAll() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWaitLong() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWaitLongInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWait() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFinalize() {
		fail("Not yet implemented"); // TODO
	}

}
