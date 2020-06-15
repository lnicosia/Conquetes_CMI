package engine.game.player.ia.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import data.player.Player;
import data.territory.Territory;
import data.territory.building.ResourceIncrease;
import data.territory.resource.Resource;
import engine.game.player.EntityEngine;
import engine.map.Map;

/**
 * The super class of ai algorithm states
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public abstract class State implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityEngine engine;
	private ArrayList<NextState> nextStates;
	private Integer territory;
	private Map map;
	private HashMap<Integer, Integer> resources;

	/**
	 * 
	 * @param map The simulate map of the game
	 * @param territory The id of territory concerned
	 * @param engine The engine which make this algorithm
	 * @param resources The simulate resources for this action
	 */
	public State(Map map, Integer territory, EntityEngine engine, HashMap<Integer, Integer> resources){
		this.engine = engine;
		this.territory = territory;
		this.map = map;
		this.resources = resources;
		nextStates = new ArrayList<NextState>();
	}
	
	protected void nextTurnAction(Map map, Player owner, HashMap<Integer, Integer> resources){
		for(Territory t : map){
			if (t.getBuilding() instanceof ResourceIncrease) {
				ResourceIncrease currentBuilding = (ResourceIncrease) t.getBuilding();
				for (int i = 0; i < Resource.nbResource; i++) {
					if ((t.getOwner() != null && t.getOwner().equals(owner))) {
						resources.put(i, resources.get(i) + t.getResources().get(i).product(currentBuilding.getResourceIncrease(i)));
					}
				}
			}
		}
	}
	
	public ArrayList<NextState> getNextStates(){
		return nextStates;
	}
	
	public Integer getTerritory(){
		return territory;
	}
	
	public HashMap<Integer, Integer> getResource(){
		return resources;
	}
	
	public Map getMap(){
		return map;
	}
	
	public EntityEngine getEngine(){
		return engine;
	}
	
	public String toString(){
		return getClass().getSimpleName() + ": Territory " + territory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getClass().hashCode();
		result = prime * result + ((engine == null) ? 0 : engine.hashCode());
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + ((resources == null) ? 0 : resources.hashCode());
		result = prime * result + ((territory == null) ? 0 : territory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (engine == null) {
			if (other.engine != null)
				return false;
		} else if (!engine.equals(other.engine))
			return false;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		if (territory == null) {
			if (other.territory != null)
				return false;
		} else if (!territory.equals(other.territory))
			return false;
		return true;
	}
	
	
	
}
