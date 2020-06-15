package engine.game.player.ia;

import java.util.ArrayList;
import java.util.HashMap;

import data.path.PathFindingRepresentation;
import data.territory.Territory;
import data.territory.building.TroopIncrease;
import data.territory.resource.Resource;
import engine.exception.ActionNotPossibleException;
import engine.game.action.Action;
import engine.game.player.EntityEngine;
import engine.game.player.ia.state.CapitalDestruction;
import engine.game.player.ia.state.MilitaryInWait;
import engine.game.player.ia.state.NextState;
import engine.game.player.ia.state.State;
import engine.game.player.ia.state.TerritoryWithoutBuilding;
import engine.game.player.ia.state.TroopsInTerritory;
import engine.map.Map;

/**
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class IAAgressive extends IAScript{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param player The engine of AI
	 * @param map The current map of the game
	 * @param players The list of all players
	 */
	public IAAgressive(EntityEngine player, Map map, ArrayList<EntityEngine> players) {
		super(player, map, players);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		
		setEnnemiesPosition();		
		HashMap<State, PathFindingRepresentation<State>> states = new HashMap<State, PathFindingRepresentation<State>>();
		HashMap<State, PathFindingRepresentation<State>> allStates;
		
		for(Territory t : getMap()){
			if((t.getOwner() != null) && (t.getOwner().equals(getPlayer().getDataEntity()))){
				if(t.getBuilding() == null){
					states.put(new TerritoryWithoutBuilding(getMap(), t.getId(), getPlayer(), Resource.cloneResource(getPlayer().getDataEntity().getStock())), new PathFindingRepresentation<State>(0));
				}
				else if(t.getBuilding() instanceof TroopIncrease){
					states.put(new MilitaryInWait(getMap(), t.getId(), getPlayer(), Resource.cloneResource(getPlayer().getDataEntity().getStock())), new PathFindingRepresentation<State>(0));
				}
			}
			
			if(!t.getTroops().isEmpty()){
				states.put(new TroopsInTerritory(getMap(), t.getId(), getPlayer(), Resource.cloneResource(getPlayer().getDataEntity().getStock())), new PathFindingRepresentation<State>(0));
			}
		}	
		
		boolean finish = false;
		boolean nothing = true;
		State finalState = new CapitalDestruction(getMap(), getEnnemiesPosition().get(0), getPlayer(), Resource.cloneResource(getPlayer().getDataEntity().getStock()));
		
		allStates = (HashMap<State, PathFindingRepresentation<State>>) states.clone();
		
		while(!finish){			
			
			int length = -1;
			nothing = true;
			for(State state : states.keySet()){
				if(state.equals(finalState)){
					finish = true;
				}
				else if((states.get(state).getWeight() < length) || (length == -1)){
					length = states.get(state).getWeight();
					nothing = false;
				}
			}
			if(!finish){
			
				if(!nothing){
					HashMap<State, PathFindingRepresentation<State>> tempNewStates = new HashMap<State, PathFindingRepresentation<State>>();
					ArrayList<State> tempRemoveStates = new ArrayList<State>();
					for(State state : states.keySet()){
						if(states.get(state).getWeight() == length){
							ArrayList<NextState> nextStates = state.getNextStates();
							if(!nextStates.isEmpty()){
								for(NextState next: nextStates){
									PathFindingRepresentation<State> ptr = new PathFindingRepresentation<State>(length + next.getLength());
									ptr.setPredecessor(state, nextStates.indexOf(next));
									if(!(allStates.containsKey(next.getState()) && (allStates.get(next.getState()).getWeight() <= (length + 1)))){
										tempNewStates.put(next.getState(), ptr);
										allStates.put(next.getState(), ptr);
									}
								}
							}
							else{
								allStates.remove(state);
							}
							tempRemoveStates.add(state);
						}
					}
					
					for(State s : tempRemoveStates){
						states.remove(s);
					}
					
					states.putAll(tempNewStates);
					
				}
				else{
					System.err.println("nothing");
					break;
				}
			}
		}
		
		if(!nothing){
			PathFindingRepresentation<State> currentStateRepresentation = allStates.get(finalState);
			State currentState = finalState;
			int direction = -1;
			while(currentStateRepresentation.getPredecessor() != null){
				direction = currentStateRepresentation.getDirection();
				currentState = currentStateRepresentation.getPredecessor();
				currentStateRepresentation = allStates.get(currentState);
			}
			//System.err.println(currentState.getClass().getSimpleName());
			for(Action action : currentState.getNextStates().get(direction).getActions()){
				//System.err.println(action.getClass().getSimpleName());
				try {
					makeAction(action);
				} catch (ActionNotPossibleException e) {
				}
			}
		}
		getGestionphase().endOfTurn();
	}

}
