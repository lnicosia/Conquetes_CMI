package engine.game.action;

import java.io.Serializable;
import java.util.ArrayList;

import data.localisation.Localisation;
import data.player.Player;
import data.territory.Territory;
import engine.exception.ActionNotPossibleException;
import engine.exception.BuildingNotBuildException;
import engine.map.Map;

/**
 * Action of create a building
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class CreateBuildingAction implements Action, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String building;
	private Integer territoryId;

	/**
	 * 
	 * @param territoryId The id of territory containing the building
	 * @param building the type of building
	 */
	public CreateBuildingAction(Integer territoryId, String building) {
		this.territoryId = territoryId;
		this.building = building;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String run(Player player, Map map, ArrayList<Player> players) throws ActionNotPossibleException {
		Territory territory = map.getTerritory(territoryId);
		if(territory.getOwner() == null){
			throw new BuildingNotBuildException("Not Your Territory");
		}
		if (!(territory.getOwner().equals(player))) {
			throw new BuildingNotBuildException("Not Your Territory");
		}
		if (territory.getBuilding() != null) {
			throw new BuildingNotBuildException("Already Building In Territory");
		}
		territory.createBuilding(player.getStock(), building);
		return "Building Created";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((building == null) ? 0 : building.hashCode());
		result = prime * result + ((territoryId == null) ? 0 : territoryId.hashCode());
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
		CreateBuildingAction other = (CreateBuildingAction) obj;
		if (building == null) {
			if (other.building != null)
				return false;
		} else if (!building.equals(other.building))
			return false;
		if (territoryId == null) {
			if (other.territoryId != null)
				return false;
		} else if (!territoryId.equals(other.territoryId))
			return false;
		return true;
	}
	
	

}
