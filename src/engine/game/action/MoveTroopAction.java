package engine.game.action;

import java.io.Serializable;
import java.util.ArrayList;

import data.localisation.Localisation;
import data.player.Player;
import data.territory.Territory;
import engine.exception.ActionNotPossibleException;
import engine.map.Map;

/**
 * Action of move a troop
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class MoveTroopAction implements Action, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer territoryId;
	private Integer targetId;
	private Integer troop;

	/**
	 * 
	 * @param territoryId The origin territory of the troop
	 * @param troop The troop in moving
	 * @param target The targeted territory
	 */
	public MoveTroopAction(Integer territoryId, int troop, Integer target) {
		this.territoryId = territoryId;
		this.targetId = target;
		this.troop = troop;
	}

	@Override
	public String run(Player player, Map map, ArrayList<Player> players) throws ActionNotPossibleException {
		Territory target = map.getTerritory(targetId);
		Territory territory = map.getTerritory(territoryId);
		if (territory.getOwner() == null) {
			throw new ActionNotPossibleException("No Troop In Territory");
		}
		if (target == null) {
			throw new ActionNotPossibleException("Bad Territory Target");
		}
		if (target.equals(territory)) {
			throw new ActionNotPossibleException("Bad Territory Target");
		}
		if (troop == null) {
			throw new ActionNotPossibleException("No Troop Selected");
		}
		if (territory.getTroops().size() < troop + 1) {
			throw new ActionNotPossibleException("No Troop Selected");
		}
		if (!territory.getTroops().get(troop).getOwner().equals(player)){
			throw new ActionNotPossibleException("Not Your Troop");
		}
		if (!territory.getNeighbours().containsValue(target)) {
			throw new ActionNotPossibleException("Bad Territory Target");
		}
		if(!target.isCrossable()){
			throw new ActionNotPossibleException("Bad Territory Target");
		}
		territory.addTroopInMoving(troop, target);
		return "Troop Moved";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((targetId == null) ? 0 : targetId.hashCode());
		result = prime * result + ((territoryId == null) ? 0 : territoryId.hashCode());
		result = prime * result + ((troop == null) ? 0 : troop.hashCode());
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
		MoveTroopAction other = (MoveTroopAction) obj;
		if (targetId == null) {
			if (other.targetId != null)
				return false;
		} else if (!targetId.equals(other.targetId))
			return false;
		if (territoryId == null) {
			if (other.territoryId != null)
				return false;
		} else if (!territoryId.equals(other.territoryId))
			return false;
		if (troop == null) {
			if (other.troop != null)
				return false;
		} else if (!troop.equals(other.troop))
			return false;
		return true;
	}
	
	

}
