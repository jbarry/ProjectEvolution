package Testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.LinkedList;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Evolution.GEP;
import Interactive.Chromosome;
import Interactive.Organism;
import Interactive.Pair;

public class GEPTest {

	/* private LinkedList<Organism> orgList; */
	GEP gep;
	Random r;

	/* private int number; */

	/*
	 * public GEPTest(int number) { this.number = number; }
	 */

	@Before
	public void setUp() throws Exception {
		r = new Random();
		gep = new GEP(0.75, 0.01, 0.01, 0.75, 0.75, 3, false, true);
		//
		/*
		 * LinkedList<Organism> orgList = new LinkedList<Organism>(); for (int i
		 * = 0; i < 7; i++) orgList.add(new Organism(true, 7, ran.nextInt(20),
		 * i)); GEP gep = new GEP(orgList, 1.0, 1.0, 1.0, 1.0, 1.0, 3, true,
		 * true);
		 */

		// CASE: ODD NUMBER OF ORGANISMS.
		/*
		 * for (int i = 0; i < 13; i++) orgList.add(new Organism(true, 4,
		 * r.nextInt(20), i));
		 */

		// CASE: EVEN NUMBER OF ORGANISMS.
		/*
		 * for (int i = 0; i < 4; i++) orgList.add(new Organism(true, 4,
		 * r.nextInt(20), i));
		 */

		// CASE: ORG LIST SIZE 1.
		// CASE: ORG LIST SIZE 0.
		// CASE: ORG ALL SAME FITNESS.
		// CASE: ORG LIST ALL SAME IDS.
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void partnerSelectTestOddPopulation() {
		// CASE: ODD NUMBER OF ORGANISMS.
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(0)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(04.0)
				.withMatterId(1)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(11.0)
				.withMatterId(2)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(03.0)
				.withMatterId(3)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(4)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(13.0)
				.withMatterId(5)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(12.0)
				.withMatterId(6)
				.build());
		
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(7)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(8)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(9)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(10)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(11)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(12)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(0)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(13)
				.build());
		LinkedList<Pair<Organism, Organism>> partnerList = gep
				.partnerSelect(orgList);
		while (!partnerList.isEmpty()) {
			Pair<Organism, Organism> partners1 = partnerList.poll();
			for (int j = 0; j < partnerList.size(); j++) {
				Pair<Organism, Organism> partners2 = partnerList.get(j);
				Integer id1P1 = partners1.getLeft().getMatterID();
				Integer id2P1 = partners1.getRight().getMatterID();
				Integer id1P2 = partners2.getLeft().getMatterID();
				Integer id2P2 = partners2.getRight().getMatterID();
				assertFalse(id1P1 == id2P1 && id1P1 == id2P2);
				if (id1P1 == id1P2)
					assertFalse(id2P1 == id2P2);
				if (id2P1 == id2P2)
					assertFalse(id1P1 == id1P2);
			}
		}
	}

	@Test
	public void partnerSelectTestEvenPopulation() {
		// CASE: ODD NUMBER OF ORGANISMS.
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(0)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(04.0)
				.withMatterId(1)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(11.0)
				.withMatterId(2)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(03.0)
				.withMatterId(3)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(4)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(13.0)
				.withMatterId(5)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(12.0)
				.withMatterId(6)
				.build());
		
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(7)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(8)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(9)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(10)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(11)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(12)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(0)
				.build());
		orgList.add(Organism.organismBuilder()
				.withNumberOfGenes(7)
				.withFitness(10.0)
				.withMatterId(13)
				.build());
		LinkedList<Pair<Organism, Organism>> partnerList = gep
				.partnerSelect(orgList);
		while (!partnerList.isEmpty()) {
			Pair<Organism, Organism> partners1 = partnerList.poll();
			for (int j = 0; j < partnerList.size(); j++) {
				Pair<Organism, Organism> partners2 = partnerList.get(j);
				Integer id1P1 = partners1.getLeft().getMatterID();
				Integer id2P1 = partners1.getRight().getMatterID();
				Integer id1P2 = partners2.getLeft().getMatterID();
				Integer id2P2 = partners2.getRight().getMatterID();
				assertFalse(id1P1 == id2P1 && id1P1 == id2P2);
				if (id1P1 == id1P2)
					assertFalse(id2P1 == id2P2);
				if (id2P1 == id2P2)
					assertFalse(id1P1 == id1P2);
			}
		}
	}

	// TODO EVEN CASE.
	// TODO
	@Test
	public void tournamentTestOddPopulation() {
		// CASE: ODD NUMBER OF ORGANISMS.
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		for (int i = 0; i < 13; i++)
			orgList.add(Organism.organismBuilder()
					.withNumberOfGenes(4)
					.withFitness(r.nextInt(20))
					.withMatterId(i)
					.build());
		LinkedList<Pair<Organism, Organism>> partnerList = gep
				.partnerSelect(orgList);
		LinkedList<Organism> tournOrgList = gep.tournament(partnerList);
		for (int i = 0; i < partnerList.size(); i++) {
			Organism firstOrg = partnerList.get(i).getLeft();
			Organism secOrg = partnerList.get(i).getRight();
			Organism tournListOrg = tournOrgList.get(i);
			Double firstFitness = firstOrg.getFitness();
			Double secFitness = secOrg.getFitness();
			if (firstFitness <= secFitness) {
				assertEquals(tournListOrg, secOrg);
			} else if (firstFitness > secFitness) {
				assertEquals(tournListOrg, firstOrg);
			}
		}
	}

	@Test
	public void tournamentTestEvenPopulation() {
		// CASE: ODD NUMBER OF ORGANISMS.
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		for (int i = 0; i < 14; i++)
			orgList.add(Organism.organismBuilder()
					.withNumberOfGenes(4)
					.withFitness(r.nextInt(20))
					.withMatterId(i)
					.build());
		LinkedList<Pair<Organism, Organism>> partnerList = gep
				.partnerSelect(orgList);
		LinkedList<Organism> tournOrgList = gep.tournament(partnerList);
		for (int i = 0; i < partnerList.size(); i++) {
			Organism firstOrg = partnerList.get(i).getLeft();
			Organism secOrg = partnerList.get(i).getRight();
			Organism tournListOrg = tournOrgList.get(i);
			Double tournListOrgFitness = tournListOrg.getFitness();
			Double firstFitness = firstOrg.getFitness();
			Double secFitness = secOrg.getFitness();
			if (firstFitness <= secFitness) {
				assertEquals(tournListOrg, secOrg);
			} else if (firstFitness > secFitness) {
				assertEquals(tournListOrg, firstOrg);
			}
		}
	}

	/*
	 * @Test public void rotationTest() { }
	 */

	/*
	 * @Test public void mutationTest() { }
	 */

	/* @Test */
	public void mateSelectEvenPopulationTest() {
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		for (int i = 0; i < 14; i++)
			orgList.add(Organism.organismBuilder()
					.withNumberOfGenes(4)
					.withFitness(r.nextInt(20))
					.withMatterId(i)
					.build());
		LinkedList<Chromosome> chromList = gep.makeChromList(gep.tournament(gep
				.partnerSelect(orgList)));
		/* gep.printChromeListIds(chromList); */
		LinkedList<Pair<Chromosome, Chromosome>> matePairList = gep
				.mateSelect(chromList);
		System.out.println("AfterMateSelect");
		/* gep.printMateListIds(matePairList); */
		/*
		 * System.out.println("Printing contents of testing the mate list");
		 * System.out.println(); System.out.println("matePairList size" +
		 * matePairList.size());
		 */
		int i = 0;
		while (!matePairList.isEmpty()) {
			/* System.out.println(i); */
			System.out.println(i);
			Pair<Chromosome, Chromosome> partners1 = matePairList.poll();
			for (int j = 0; j < matePairList.size(); j++) {
				Pair<Chromosome, Chromosome> partners2 = matePairList.get(j);
				Chromosome chrom11 = partners1.getLeft();
				Chromosome chrom12 = partners1.getRight();
				Chromosome chrom21 = partners2.getLeft();
				Chromosome chrom22 = partners2.getRight();
				Integer id1P1 = chrom11.getId();
				Integer id2P1 = chrom12.getId();
				Integer id1P2 = chrom21.getId();
				Integer id2P2 = chrom22.getId();
				System.out.println("<" + id1P1 + ", " + id2P1 + ">");
				System.out.println("<" + id1P2 + ", " + id2P2 + ">");
				assertNotSame(chrom11, chrom12);
				assertNotSame(chrom11, chrom21);
				assertNotSame(chrom11, chrom22);
				assertNotSame(chrom12, chrom21);
				assertNotSame(chrom12, chrom22);
				assertNotSame(chrom21, chrom22);
			}
			i++;
		}
	}

}