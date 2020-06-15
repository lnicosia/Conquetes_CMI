package engine.game.player.ia;

import java.util.ArrayList;
import java.util.HashMap;

import data.territory.Territory;
import data.territory.building.TroopIncrease;
import data.troop.Troop;
import engine.exception.ActionNotPossibleException;
import engine.game.action.CreateBuildingAction;
import engine.game.action.CreateTroopAction;
import engine.game.action.MoveTroopAction;
import engine.game.player.EntityEngine;
import engine.map.Map;
import engine.map.MapIterator;
import engine.path.PathFindingTerritory;

/**
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class IAAgressiveAncient extends IAScript implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<Troop, PathFindingTerritory> troops;

	/**
	 * 
	 * @param player The engine of AI
	 * @param map The current map of the game
	 * @param players The list of all players
	 */
	public IAAgressiveAncient(EntityEngine player, Map map, ArrayList<EntityEngine> players) {
		super(player, map, players);
		troops = new HashMap<Troop, PathFindingTerritory>();
	}

	@Override
	public void run() {
		
		for(EntityEngine ee : getPlayers()){
			getPlayersData().add(ee.getDataEntity());
		}
		setEnnemiesPosition();
		
		MapIterator it = (MapIterator) getMap().iterator();
		
		while(it.hasNext()){
			Territory currentTerritory = it.next();
			if(currentTerritory.getOwner() != null){
				if(currentTerritory.getOwner().equals(getPlayer().getDataEntity())){
					if(currentTerritory.getBuilding() == null){
						String buildingName;
						if(((int)(Math.random())) == 1){
							buildingName = "Barracks";
						}
						else{
							buildingName = "Factory";
						}
						try {
							makeAction(new CreateBuildingAction(currentTerritory.getId(), buildingName));
						} catch (ActionNotPossibleException e) {
							
						}
					}
					else {
						if(currentTerritory.getBuilding() instanceof TroopIncrease){
							try {
								makeAction(new CreateTroopAction(currentTerritory.getId(), "Barbarian"));
							} catch (ActionNotPossibleException e) {
								
							}
						}
					}
					if(!currentTerritory.getTroops().isEmpty()){
						for(Troop troop : currentTerritory.getTroops()){
							if(!troops.containsKey(troop)){
								PathFindingTerritory newPath = new PathFindingTerritory(getMap(), currentTerritory, getMap().getTerritory(getEnnemiesPosition().get(0)));
								troops.put(troop, newPath);
							}
							try {
								Territory target = getMap().getTerritory(troops.get(troop).next());
								makeAction(new MoveTroopAction(currentTerritory.getId(), currentTerritory.getTroops().indexOf(troop), target.getId()));
							} catch (ActionNotPossibleException e) {

							}
						}
					}
				}
			}
		}
		
		getGestionphase().endOfTurn();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((troops == null) ? 0 : troops.hashCode()) + super.hashCode();
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
		IAAgressiveAncient other = (IAAgressiveAncient) obj;
		if (troops == null) {
			if (other.troops != null)
				return false;
		} else if (!troops.equals(other.troops))
			return false;
		return true;
	}
	
	

}
