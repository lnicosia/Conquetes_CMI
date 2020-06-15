package engine.game.player.ia.state;

import java.util.ArrayList;
import java.util.HashMap;

import data.territory.Territory;
import data.territory.building.DefenseIncrease;
import data.territory.resource.Resource;
import data.territory.types.Plain;
import data.troop.Troop;
import engine.game.action.Action;
import engine.game.action.MoveTroopAction;
import engine.game.phase.FightPhase;
import engine.game.player.EntityEngine;
import engine.map.Map;

/**
 * The state of troops in territory
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class TroopsInTerritory extends State{
	
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
	public TroopsInTerritory(Map map, Integer territory, EntityEngine engine, HashMap<Integer, Integer> resources){
		super(map, territory, engine, resources);
	}

	@Override
	public ArrayList<NextState> getNextStates() {
		ArrayList<NextState> nextState = super.getNextStates();
		for(int i = 1; i <= 6; i++){
			Map map = getMap().clone();
			Territory territory = map.getTerritory(getTerritory());
			Territory neigh = territory.getNeigh(i);
			if((neigh != null) && neigh.isCrossable()){
				Territory test = new Plain(0, 0, neigh.getId());
				test.cloneData(neigh);
				moveTroops(territory, test, false);
				new FightPhase(test);
				if((test.getOwner() != null) && (test.getOwner().equals(getEngine().getDataEntity()) || getEngine().getDataEntity().getAllies().contains(test.getOwner()))){
					//System.err.println(neigh.getId());
					ArrayList<Action> actions = moveTroops(territory, neigh, true);
					if(neigh.isCapital() && !neigh.getOwner().equals(getEngine().getDataEntity())){
						HashMap<Integer, Integer> resources = Resource.cloneResource(getResource());
						nextTurnAction(map, getEngine().getDataEntity(), resources);
						nextState.add(new NextState(new CapitalDestruction(map, neigh.getId(), getEngine(), resources), 1, actions));
					}
					else{
						if((neigh.getOwner() == null) || !neigh.getOwner().equals(getEngine().getDataEntity()) && ((neigh.getBuilding() == null) || (neigh.getBuilding() instanceof DefenseIncrease))){
							HashMap<Integer, Integer> resources = Resource.cloneResource(getResource());
							nextTurnAction(map, getEngine().getDataEntity(), resources);
							nextState.add(new NextState(new TerritoryWithoutBuilding(getMap().clone(), getTerritory(), getEngine(), resources), 1, actions));
						}
						HashMap<Integer, Integer> resources = Resource.cloneResource(getResource());
						nextTurnAction(map, getEngine().getDataEntity(), resources);
						nextState.add(new NextState(new TroopsInTerritory(map, neigh.getId(), getEngine(), resources), 1, actions));
					}
				}
			}
		}
		
		
		return nextState;
	}
	
	private ArrayList<Action> moveTroops(Territory territory, Territory target, boolean clear){
		ArrayList<Action> actions = new ArrayList<Action>();
		for(Troop t : territory.getTroops()){
			if(t.getOwner().equals(getEngine().getDataEntity())){
				target.addTroop(t);
				actions.add(new MoveTroopAction(getTerritory(), territory.getTroops().indexOf(t), target.getId()));
			}
		}
		if(clear){
			territory.getTroops().clear();
		}
		return actions;
	}

}
