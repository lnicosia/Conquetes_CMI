package testunit;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import data.player.Player;
import data.territory.building.Barracks;
import data.territory.building.Building;
import data.territory.resource.Resource;
import data.troop.soldier.Barbarian;
import data.troop.soldier.Soldier;

public class TestData extends TestCase {
	
	private Player player;
	private Building building;
	private Soldier soldier;
	
	public TestData(String arg0){
		super(arg0);
		player = null;
		building = null;
		soldier = null;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    player = new Player("Test", 1);
	    player.increaseResource(Resource.WOOD, 50);
	    building = new Barracks();
	    soldier = new Barbarian();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	      player = null;
	      building = null;
	      soldier = null;
	}
	
	public void testBuyBuilding() {
		assertTrue(building.buy(player.getStock()));
		assertTrue(soldier.buy(player.getStock()));
	}
}
