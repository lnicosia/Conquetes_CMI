package engine.game.action;

import java.io.Serializable;
import java.util.ArrayList;

import data.localisation.Localisation;
import data.player.Player;
import data.territory.Territory;
import data.territory.building.TroopIncrease;
import engine.exception.ActionNotPossibleException;
import engine.exception.TroopNotBuildException;
import engine.map.Map;

/**
 * Action of create troop
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class CreateTroopAction implements Action, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String troop;
	private Integer territoryId;
	
	/**
	 * 
	 * @param territoryId The id of territory containing the troop
	 * @param troop The type of soldier
	 */
	public CreateTroopAction(Integer territoryId, String troop) {
		this.troop = troop;
		this.territoryId = territoryId;
	}

	@Override
	public String run(Player player, Map map, ArrayList<Player> players) throws ActionNotPossibleException {
		Territory territory = map.getTerritory(territoryId);
		if (territory.getOwner() == null) {
			throw new TroopNotBuildException("Not Your Territory");
		}
		if (!(territory.getOwner().equals(player))) {
			throw new TroopNotBuildException("Not Your Territory");
		}
		if (territory.getBuilding() == null) {
			throw new TroopNotBuildException("Not Military Building");
		}
		if (!(territory.getBuilding() instanceof TroopIncrease)) {
			throw new TroopNotBuildException("Not Military Building");
		}
		TroopIncrease building = (TroopIncrease) territory.getBuilding();
		building.createTroop(player, troop);
		return "Troop Created";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		CreateTroopAction other = (CreateTroopAction) obj;
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
