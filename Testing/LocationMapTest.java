package Testing;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Random;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Frame.LocationMap;
import Interactive.Organism;
import Interactive.Pair;

public class LocationMapTest {

	Random r;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		r = new Random();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void placeOrganismsPositionsNotEqualToZero() {
		LocationMap map = LocationMap.getInstance();
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		for (int i = 0; i < 300; i++)
			orgList.add(new Organism(true, 4, r.nextInt(20), i));
		map.placeOrganisms(orgList);
		for(Organism org : orgList)
			Assert.assertTrue(org.getLocation().getX() != 0 && org.getLocation().getY() != 0);
		map.clearLocations();
	}
	
	@Test
	public void placeOrganismsGivesUniquePositions() {
		LocationMap map = LocationMap.getInstance();
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		for (int i = 0; i < 300; i++)
			orgList.add(new Organism(true, 4, r.nextInt(20), i));
		map.placeOrganisms(orgList);
		while (!orgList.isEmpty()) {
			Organism currentOrg = orgList.remove();
			int x = currentOrg.getLocation().getX();
			int y = currentOrg.getLocation().getY();
			for (int i = 0; i < orgList.size(); i++) {
				Organism anOrg = orgList.remove();
				int x2 = anOrg.getLocation().getX();
				int y2 = anOrg.getLocation().getY();
				if (x == x2)
					Assert.assertTrue(y != y2);
				if (y == y2)
					Assert.assertTrue(x != x2);
			}
		}
		map.clearLocations();
	}
	
	@Test
	public void setRangeFillsInIndecesWithAppropriateLocations() {
		LocationMap map = LocationMap.getInstance();
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		for (int i = 0; i < 300; i++)
			orgList.add(new Organism(true, 4, r.nextInt(20), i));
		map.placeOrganisms(orgList);
		for (Organism org : orgList) {
			int x = org.getLocation().getX();
			int y = org.getLocation().getY();
			int id = org.getId();
			System.out.println("orgid: " + id);
			System.out.println("orgType: " + 'o');
			Pair<Integer, Character> space = map.get(x, y);
			int spaceId = space.getFst();
			Character spaceType = space.getSnd();
			System.out.println("spaceid: " + spaceId);
			System.out.println("spaceType: " + spaceType);
			// Assert that the space is filled with the organism's id.
			Assert.assertEquals(id, spaceId);
			// Assert that the space is filled with the appropriate type.
			Assert.assertEquals("o", spaceType.toString());
		}
		System.out.println("In the random pair: " + map.get(20, 237).getFst() + ", " + map.get(20, 237).getSnd());
		map.clearLocations();
	}
	
	@Test
	public void adjacentCoordinatesTest() {
		LocationMap map = LocationMap.getInstance();
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		for (int i = 0; i < 300; i++)
			orgList.add(new Organism(true, 4, r.nextInt(20), i));
		map.placeOrganisms(orgList);
	}

	@Test
	public void searchTest() {
	}

	@Test
	public void distance() {
	}

	@Test
	public void objectsInSpaceTest() {
	}

	@Test
	public void hasObstacleTest() {
	}
}
