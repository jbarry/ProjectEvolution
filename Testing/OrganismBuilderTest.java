package Testing;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import Interactive.Organism;

public class OrganismBuilderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void organismBuilderTest() {
		Organism org = Organism.organismBuilder()
					.withNumberOfGenes(4)
					.withFitness(200)
					.withMatterId(10)
					.withHealth(100.00)
					.build();
		
		Assert.assertEquals(10, org.getId());
		System.out.println(org.getHealth());
		System.out.println(org.getId());
		Assert.assertEquals(100.00, org.getHealth());
		Assert.assertEquals('o', org.getType());
	}
}