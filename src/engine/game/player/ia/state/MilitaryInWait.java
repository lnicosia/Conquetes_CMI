package engine.game.player.ia.state;

import java.util.ArrayList;
import java.util.HashMap;

import data.territory.Territory;
import data.territory.resource.Resource;
import data.troop.Troop;
import data.troop.soldier.Barbarian;
import data.troop.soldier.Soldier;
import engine.game.action.Action;
import engine.game.action.CreateTroopAction;
import engine.game.player.EntityEngine;
import engine.map.Map;
/**
 * The state of a military building without action
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class MilitaryInWait extends State{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param map The simulate map of the game
	 * @param territory The id of territory concerned
	 * @param engine The engine which make this algorithm
	 * @param resources The simulate resources for this action
	 */
	public MilitaryInWait(Map map, Integer territory, EntityEngine engine, HashMap<Integer, Integer> resources){
		super(map, territory, engine, resources);
	}

	@Override
	public ArrayList<NextState> getNextStates() {
		ArrayList<NextState> nextState = super.getNextStates();
		
		Soldier s = new Barbarian();
		HashMap<Integer, Integer> resource = Resource.cloneResource(getResource());
		if(s.buy(resource)){
			Map map = getMap().clone();
			Territory territory = map.getTerritory(getTerritory());
			territory.getTroops().add(new Troop(getEngine().getDataEntity(), s));
			ArrayList<Action> action = new ArrayList<Action>();
			action.add(new CreateTroopAction(territory.getId(), "Barbarian"));
			nextState.add(new NextState(new TroopsInTerritory(map, getTerritory(), getEngine(), resource),  s.getProductTime(), action));
			nextTurnAction(map, getEngine().getDataEntity(), resource);
			nextState.add(new NextState(new MilitaryInWait(map, getTerritory(), getEngine(), resource),  s.getProductTime(), new ArrayList<Action>()));
			
		}

		return nextState;
	}

}
