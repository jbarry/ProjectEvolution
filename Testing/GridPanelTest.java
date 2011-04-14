package Testing;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import Frame.LocationMap;

@SuppressWarnings("unused")
public class GridPanelTest {

	LocationMap locationMap;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		locationMap = LocationMap.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		locationMap.clearLocations();
	}

	public void simulateStepTest() {
		
	}
}
