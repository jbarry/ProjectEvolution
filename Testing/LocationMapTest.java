package Testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Frame.Coordinate;
import Frame.LocationMap;
import Interactive.Food;
import Interactive.HealthyFood;
import Interactive.Organism;
import Interactive.Pair;
import Interactive.PoisonousFood;

public class LocationMapTest {

	Random r;
	
	/*// FOR TESTING IN THE LOCATION MAP CLASS.
	public HealthyFood healthyFoodCtor(double aMxHlth, int anId, int aScanRange){
		HealthyFood hf = new HealthyFood(aMxHlth, anId, 'h');
		
	}*/
	
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
	public void placeOrganismsPositionsArePast590() {
		LocationMap map = LocationMap.getInstance();
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		for (int i = 0; i < 400; i++)
			orgList.add(new Organism(true, 4, r.nextInt(20), i));
		map.placeOrganisms(orgList);
		int count = 0;
		for(Organism org : orgList)
			if (org.getLocation().getX() > 580) {
				System.out.println("x: " + org.getLocation().getX());
				System.out.println("y: " + org.getLocation().getY());
				count++;
			}
		Assert.assertTrue(count > 0);
		map.clearLocations();
	}
	
	//	@Test
	public void searchTest() {
		LocationMap map = LocationMap.getInstance();
		LinkedList<Organism> orgList = new LinkedList<Organism>();
		for (int i = 0; i < 300; i++)
			orgList.add(new Organism(true, 4, r.nextInt(20), i));
		map.placeOrganisms(orgList);
		fail("not yet implemented");
	}

	@Test
	public void placeFoodPositionsNotEqualToZero() {
		LocationMap map = LocationMap.getInstance();
		LinkedList<Food> foodList = new LinkedList<Food>();
		for (int i = 0; i < 300; i++) {
			if (r.nextBoolean())
				foodList.add(new HealthyFood(100.0, i, 'h'));
			else
				foodList.add(new PoisonousFood(100.0, i, 'p'));
		}
		map.placeFoods(foodList);
		for(Food f : foodList)
			Assert.assertTrue(f.getLocation().getX() != 0 && f.getLocation().getY() != 0);
		map.clearLocations();
	}

	//	@Test
	public void placeFoodPositionsAreUnique() {
		LocationMap map = LocationMap.getInstance();
		LinkedList<Food> foodList = new LinkedList<Food>();
		for (int i = 0; i < 300; i++) {
			if (r.nextBoolean())
				foodList.add(new HealthyFood(100.0, i, 'h'));
			else
				foodList.add(new PoisonousFood(100.0, i, 'p'));
		}
		map.placeFoods(foodList);
		for(Food f : foodList)
			Assert.assertTrue(f.getLocation().getX() != 0 && f.getLocation().getY() != 0);
		map.clearLocations();
	}

	@Test
	public void placeFoodGivesUniquePositions() {
		LocationMap map = LocationMap.getInstance();
		LinkedList<Food> foodList = new LinkedList<Food>();
		for (int i = 0; i < 300; i++) {
			if (r.nextBoolean())
				foodList.add(new HealthyFood(100.0, i, 'h'));
			else
				foodList.add(new PoisonousFood(100.0, i, 'p'));
		}
		map.placeFoods(foodList);
		while (!foodList.isEmpty()) {
			Food currentFood = foodList.remove();
			int x = currentFood.getLocation().getX();
			int y = currentFood.getLocation().getY();
			for (int i = 0; i < foodList.size(); i++) {
				Food aFood = foodList.remove();
				int x2 = aFood.getLocation().getX();
				int y2 = aFood.getLocation().getY();
				if (x == x2)
					Assert.assertTrue(y != y2);
				if (y == y2)
					Assert.assertTrue(x != x2);
			}
		}
		map.clearLocations();
	}

	@Test
	public void distance() {
		fail("not yet implemented");
	}

	@Test
	public void objectsInSpaceTest() {
		fail("not yet implemented");
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
			int id = org.getMatterID();
			System.out.println("orgid: " + id);
			System.out.println("orgType: " + 'o');
			Pair<Integer, Character> space = map.get(x, y);
			int spaceId = space.getLeft();
			Character spaceType = space.getRight();
			System.out.println("spaceid: " + spaceId);
			System.out.println("spaceType: " + spaceType);
			// Assert that the space is filled with the organism's id.
			Assert.assertEquals(id, spaceId);
			// Assert that the space is filled with the appropriate type.
			Assert.assertEquals("o", spaceType.toString());
		}
		System.out.println("In the random pair: " + map.get(20, 237).getLeft()
				+ ", " + map.get(20, 237).getRight());
		map.clearLocations();
	}
	
	@Test
	public void hasObstacleNorthTest() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(10);
		org2.getLocation().setY(5);
		map.setOrganism(org1);
		map.setOrganism(org2);
		Assert.assertTrue(map.hasObstacle(10, 9, 0));
		map.clearLocations();
	}
	
	@Test
	public void hasObstacleNorthEastTest() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(15);
		org2.getLocation().setY(5);
		map.setOrganism(org1);
		map.setOrganism(org2);
		Assert.assertTrue(map.hasObstacle(11, 9, 0));
		map.clearLocations();
	}
	
	@Test
	public void hasObstacleSouthEastTest() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(15);
		org2.getLocation().setY(15);
		map.setOrganism(org1);
		map.setOrganism(org2);
		Assert.assertTrue(map.hasObstacle(11, 11, 0));
		map.clearLocations();
	}

	@Test
	public void hasObstacleSouthTest() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(10);
		org2.getLocation().setY(15);
		map.setOrganism(org1);
		map.setOrganism(org2);
		Assert.assertTrue(map.hasObstacle(10, 11, 0));
		map.clearLocations();
	}
	
	/*@Test
	public void hasObstacleSouthWestTest() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(5);
		org2.getLocation().setY(15);
		map.setOrganism(org1, org1.getLocation());
		map.setOrganism(org2, org2.getLocation());
		Assert.assertTrue(LocationMap.hasObstacle(9, 11, 0));
		map.clearLocations();
	}*/
	
	@Test
	public void hasObstacleWestTest() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(5);
		org2.getLocation().setY(10);
		map.setOrganism(org1);
		map.setOrganism(org2);
		Assert.assertTrue(map.hasObstacle(9, 10, 0));
		map.clearLocations();
	}
	
	@Test
	public void hasObstacleNorthWestTest() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(5);
		org2.getLocation().setY(5);
		map.setOrganism(org1);
		map.setOrganism(org2);
		Assert.assertTrue(map.hasObstacle(9, 9, 0));
		map.clearLocations();
	}
	
	@Test
	public void doesNothaveObstacleNorthWestTest() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(4);
		org2.getLocation().setY(4);
		map.setOrganism(org1);
		map.setOrganism(org2);
		Assert.assertTrue(!map.hasObstacle(9, 9, 0));
		map.clearLocations();
	}
	
	@Test
	public void shouldOnlyHaveSouthMovementInStarQueue() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		map.setOrganism(org1);
		
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(5);
		org2.getLocation().setY(5);
		map.setOrganism(org2);
		
		Organism org3 = new Organism(true, 4, 0.0, 1);
		org3.getLocation().setX(10);
		org3.getLocation().setY(5);
		map.setOrganism(org3);
		
		Organism org4 = new Organism(true, 4, 0.0, 1);
		org4.getLocation().setX(15);
		org4.getLocation().setY(5);
		map.setOrganism(org4);
		
		Organism org5 = new Organism(true, 4, 0.0, 1);
		org5.getLocation().setX(15);
		org5.getLocation().setY(10);
		map.setOrganism(org5);
		
		Organism org6 = new Organism(true, 4, 0.0, 1);
		org6.getLocation().setX(15);
		org6.getLocation().setY(15);
		map.setOrganism(org6);
		
		Organism org7 = new Organism(true, 4, 0.0, 1);
		org7.getLocation().setX(5);
		org7.getLocation().setY(15);
		map.setOrganism(org7);
		
		Organism org8 = new Organism(true, 4, 0.0, 1);
		org8.getLocation().setX(5);
		org8.getLocation().setY(10);
		map.setOrganism(org8);
		
		Coordinate end = new Coordinate(34, 9);
		PriorityQueue<Coordinate> sq = map.adjacentCoordinates(10, 10, 1,
				end, org1.getMatterID());
		System.out.println("queue size: " + sq.size());
		Coordinate coord = sq.remove();
		System.out.println("To move to: " + coord.getX() + ", " + coord.getY());
		map.clearLocations();
	}
	
	@Test
	public void shouldOnlyHaveSouthandWestMovementInStarQueueAndSouthShouldBeFirst() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		map.setOrganism(org1);
		
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(5);
		org2.getLocation().setY(5);
		map.setOrganism(org2);
		
		Organism org3 = new Organism(true, 4, 0.0, 1);
		org3.getLocation().setX(10);
		org3.getLocation().setY(5);
		map.setOrganism(org3);
		
		Organism org4 = new Organism(true, 4, 0.0, 1);
		org4.getLocation().setX(15);
		org4.getLocation().setY(5);
		map.setOrganism(org4);
		
		Organism org5 = new Organism(true, 4, 0.0, 1);
		org5.getLocation().setX(15);
		org5.getLocation().setY(10);
		map.setOrganism(org5);
		
		Organism org6 = new Organism(true, 4, 0.0, 1);
		org6.getLocation().setX(15);
		org6.getLocation().setY(15);
		map.setOrganism(org6);
		
		Organism org7 = new Organism(true, 4, 0.0, 1);
		org7.getLocation().setX(5);
		org7.getLocation().setY(15);
		map.setOrganism(org7);
		
		Coordinate end = new Coordinate(34, 9);
		PriorityQueue<Coordinate> sq = map.adjacentCoordinates(10, 10, 1,
				end, org1.getMatterID());
		Coordinate coord = sq.remove();
		System.out.println("To move to: " + coord.getX() + ", " + coord.getY());
		assertEquals(10, coord.getX());
		assertEquals(11, coord.getY());
		map.clearLocations();
	}
	
	@Test
	public void shouldOnlyHaveNorthMovementInStarQueue() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		map.setOrganism(org1);
		
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(5);
		org2.getLocation().setY(5);
		map.setOrganism(org2);
		
		/*Organism org3 = new Organism(true, 4, 0.0, 1);
		org3.getLocation().setX(10);
		org3.getLocation().setY(5);
		map.setOrganism(org3);*/
		
		Organism org4 = new Organism(true, 4, 0.0, 1);
		org4.getLocation().setX(15);
		org4.getLocation().setY(5);
		map.setOrganism(org4);
		
		Organism org5 = new Organism(true, 4, 0.0, 1);
		org5.getLocation().setX(15);
		org5.getLocation().setY(10);
		map.setOrganism(org5);
		
		Organism org6 = new Organism(true, 4, 0.0, 1);
		org6.getLocation().setX(15);
		org6.getLocation().setY(15);
		map.setOrganism(org6);
		
		Organism org7 = new Organism(true, 4, 0.0, 1);
		org7.getLocation().setX(10);
		org7.getLocation().setY(15);
		map.setOrganism(org7);
		
		Organism org8 = new Organism(true, 4, 0.0, 1);
		org8.getLocation().setX(5);
		org8.getLocation().setY(15);
		map.setOrganism(org8);
		
		Organism org9 = new Organism(true, 4, 0.0, 1);
		org9.getLocation().setX(5);
		org9.getLocation().setY(10);
		map.setOrganism(org9);
		
		Coordinate end = new Coordinate(34, 9);
		PriorityQueue<Coordinate> sq = map.adjacentCoordinates(10, 10, 1,
				end, org1.getMatterID());
		System.out.println("queue size: " + sq.size());
		Coordinate coord = sq.remove();
		System.out.println("To move to: " + coord.getX() + ", " + coord.getY());
		assertEquals(10, coord.getX());
		assertEquals(9, coord.getY());
		map.clearLocations();
	}
	
	@Test
	public void shouldOnlyHaveWestMovementInStarQueue() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		map.setOrganism(org1);
		
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(5);
		org2.getLocation().setY(5);
		map.setOrganism(org2);
		
		Organism org3 = new Organism(true, 4, 0.0, 1);
		org3.getLocation().setX(10);
		org3.getLocation().setY(5);
		map.setOrganism(org3);
		
		Organism org4 = new Organism(true, 4, 0.0, 1);
		org4.getLocation().setX(15);
		org4.getLocation().setY(5);
		map.setOrganism(org4);
		
		Organism org5 = new Organism(true, 4, 0.0, 1);
		org5.getLocation().setX(15);
		org5.getLocation().setY(10);
		map.setOrganism(org5);
		
		Organism org6 = new Organism(true, 4, 0.0, 1);
		org6.getLocation().setX(15);
		org6.getLocation().setY(15);
		map.setOrganism(org6);
		
		Organism org7 = new Organism(true, 4, 0.0, 1);
		org7.getLocation().setX(10);
		org7.getLocation().setY(15);
		map.setOrganism(org7);
		
		Organism org8 = new Organism(true, 4, 0.0, 1);
		org8.getLocation().setX(5);
		org8.getLocation().setY(15);
		map.setOrganism(org8);
		
		/*Organism org9 = new Organism(true, 4, 0.0, 1);
		org9.getLocation().setX(5);
		org9.getLocation().setY(10);
		map.setOrganism(org9);*/
		
		Coordinate end = new Coordinate(26, 13);
		PriorityQueue<Coordinate> sq = map.adjacentCoordinates(10, 10, 1,
				end, org1.getMatterID());
		System.out.println("queue size: " + sq.size());
		Coordinate coord = sq.remove();
		System.out.println("To move to: " + coord.getX() + ", " + coord.getY());
		assertEquals(9, coord.getX());
		assertEquals(10, coord.getY());
		map.clearLocations();
	}
	
	@Test
	public void shouldOnlyHaveWestMovementInStarQueueAfterFirstMoveThenMoveAgainMoveTwice() {
		LocationMap map = LocationMap.getInstance();
		Organism org1 = new Organism(true, 4, 0.0, 0);
		org1.getLocation().setX(10);
		org1.getLocation().setY(10);
		map.setOrganism(org1);
		
		Organism org2 = new Organism(true, 4, 0.0, 1);
		org2.getLocation().setX(5);
		org2.getLocation().setY(5);
		map.setOrganism(org2);
		
		Organism org3 = new Organism(true, 4, 0.0, 1);
		org3.getLocation().setX(10);
		org3.getLocation().setY(5);
		map.setOrganism(org3);
		
		Organism org4 = new Organism(true, 4, 0.0, 1);
		org4.getLocation().setX(15);
		org4.getLocation().setY(5);
		map.setOrganism(org4);
		
		Organism org5 = new Organism(true, 4, 0.0, 1);
		org5.getLocation().setX(15);
		org5.getLocation().setY(10);
		map.setOrganism(org5);
		
		Organism org6 = new Organism(true, 4, 0.0, 1);
		org6.getLocation().setX(15);
		org6.getLocation().setY(15);
		map.setOrganism(org6);
		
		Organism org7 = new Organism(true, 4, 0.0, 1);
		org7.getLocation().setX(10);
		org7.getLocation().setY(15);
		map.setOrganism(org7);
		
		Organism org8 = new Organism(true, 4, 0.0, 1);
		org8.getLocation().setX(5);
		org8.getLocation().setY(15);
		map.setOrganism(org8);
		
		/*Organism org9 = new Organism(true, 4, 0.0, 1);
		org9.getLocation().setX(5);
		org9.getLocation().setY(10);
		map.setOrganism(org9);*/
		
		Coordinate end = new Coordinate(26, 13);
		PriorityQueue<Coordinate> sq = map.adjacentCoordinates(10, 10, 1,
				end, org1.getMatterID());
		System.out.println("queue size: " + sq.size());
		Coordinate coord = sq.remove();
		System.out.println("To move to: " + coord.getX() + ", " + coord.getY());
		assertEquals(9, coord.getX());
		assertEquals(10, coord.getY());
		map.clearLocations();
	}
}
