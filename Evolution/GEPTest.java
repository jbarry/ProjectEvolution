package Evolution;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Interactive.Organism;

public class GEPTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Random ran = new Random();
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		for (int i = 0; i < 7; i++)
			orgList.add(new Organism(true, 7, ran.nextInt(20), i));
		GEP gep = new GEP(orgList, 1.0, 1.0, 1.0, 1.0, 1.0, 3, true, true);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

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
		fail("Not yet implemented"); // TODO
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
