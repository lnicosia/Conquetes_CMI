package engine.game.player.ia.state;

import java.util.ArrayList;
import java.util.HashMap;

import data.territory.building.Barracks;
import data.territory.resource.Resource;
import engine.game.action.Action;
import engine.game.action.CreateBuildingAction;
import engine.game.player.EntityEngine;
import engine.map.Map;

/**
 * The state of a territory without building
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class TerritoryWithoutBuilding extends State{
	
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
	public TerritoryWithoutBuilding(Map map, Integer territory, EntityEngine engine, HashMap<Integer, Integer> resources){
		super(map, territory, engine, resources);
	}

	@Override
	public ArrayList<NextState> getNextStates() {
		
		ArrayList<NextState> nextState = super.getNextStates();
		
		Barracks building = new Barracks();
		HashMap<Integer, Integer> resource = Resource.cloneResource(getResource());
		Map map = getMap().clone();
		map.getTerritory(getTerritory()).setBuilding(building);
		
		if(building.buy(resource)){
			ArrayList<Action> militaryAction = new ArrayList<Action>();
			militaryAction.add(new CreateBuildingAction(getTerritory(), "Barracks"));
			//ArrayList<Action> factoryAction = new ArrayList<Action>();
			//ArrayList<Action> defenseAction = new ArrayList<Action>();
			nextTurnAction(map, getEngine().getDataEntity(), resource);
			nextState.add(new NextState(new MilitaryInWait(map, getTerritory(), getEngine(), resource), building.getBuildTime(), militaryAction));
		}
		
		return nextState;
	}

}
