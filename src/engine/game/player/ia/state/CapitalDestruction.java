package engine.game.player.ia.state;

import java.util.ArrayList;
import java.util.HashMap;

import engine.game.player.EntityEngine;
import engine.map.Map;

/**
 * The State of Capital Destruction (final state)
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class CapitalDestruction extends State{
	
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
	public CapitalDestruction(Map map, Integer territory, EntityEngine engine, HashMap<Integer, Integer> resources){
		super(map, territory, engine, resources);
	}
	
	@Override
	public ArrayList<NextState> getNextStates(){
		return new ArrayList<NextState>();
	}
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + getClass().hashCode();
		result = prime * result + getTerritory().hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if(getTerritory() != other.getTerritory())
			return false;
		return true;
	}

}
