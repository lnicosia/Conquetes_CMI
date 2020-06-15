package testunit;

import java.util.ArrayList;

import junit.framework.TestCase;
import data.territory.Territory;
import engine.game.GameInitializator;
import engine.game.player.EntityEngine;
import engine.game.player.IAEngine;
import engine.map.Map;
import engine.map.MapProcedural;

public class TestEngine extends TestCase{

	private Map map;
	private GameInitializator gi;
	
	public TestEngine(String arg0){
		super(arg0);
		map = null;
		gi = null;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    map = new MapProcedural(10, 10, 4, 8);
	    ArrayList<String> ai = new ArrayList<String>();
	    ai.add("Agressive");
	    gi = new GameInitializator("normal", 2, 2, ai);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		map = null;
		gi = null;
	}
	
	public void testInitializator(){
		ArrayList<EntityEngine> players = gi.getPlayers();
		assertTrue(gi.getMap().getClass().equals(MapProcedural.class));
		assertTrue(players.size() == 2);
		int nbIa = 0;
		for(EntityEngine ee : players){
			if(ee.getClass().equals(IAEngine.class)){
				nbIa++;
			}
		}
		assertTrue(nbIa == 1);
	}
	
	public void testMap(){
		assertTrue(map.getTerritoryNb() == (map.getNbLine()*map.getNbColumn()));
		for(Territory t : map){
			assertTrue(t.getClass() == map.getTableMap()[map.getPositionX(t.getId())][map.getPositionY(t.getId())]);
		}
	}
}