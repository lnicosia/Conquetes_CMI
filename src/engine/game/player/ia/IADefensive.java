package engine.game.player.ia;

import java.io.Serializable;
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

/**
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class IADefensive extends IAScript implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<Troop, Integer> troops;
	private HashMap<Integer, Integer> territoryValue;
	private int currentNeigh;

	/**
	 * 
	 * @param player The engine of AI
	 * @param map The current map of the game
	 * @param players The list of all players
	 */
	public IADefensive(EntityEngine player, Map map, ArrayList<EntityEngine> players) {
		super(player, map, players);
		territoryValue = new HashMap<Integer, Integer>();
		troops = new HashMap<Troop, Integer>();
		currentNeigh = 0;
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
						try {
							makeAction(new CreateBuildingAction(currentTerritory.getId(), "Rampart"));
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
						if(currentTerritory.getTroops().size() > 3){
							for(int i = 3; i < currentTerritory.getTroops().size(); i++){
								int smallNeigh = 1;
								boolean move = true;
								if(territoryValue.containsKey(currentTerritory)){
									int value = territoryValue.get(currentTerritory.getId());
									int nextValue;
									if(value != 6){
										nextValue = value + 1;
									}
									else{
										nextValue = 1;
									}
									if(!territoryValue.containsKey(currentTerritory.getNeigh(value))){
										territoryValue.put(currentTerritory.getNeigh(value).getId(), value);
									}
									if(!territoryValue.containsKey(currentTerritory.getNeigh(nextValue))){
										territoryValue.put(currentTerritory.getNeigh(nextValue).getId(), nextValue);
									}
									if((currentTerritory.getNeigh(value) == null) || !currentTerritory.getNeigh(value).isCrossable()){
										if((currentTerritory.getNeigh(nextValue) == null) || !currentTerritory.getNeigh(nextValue).isCrossable()){
											move = false;
										}
										else{
											smallNeigh = nextValue;
										}
									}
									else{
										if((currentTerritory.getNeigh(nextValue) == null) || !currentTerritory.getNeigh(nextValue).isCrossable()){
											smallNeigh = value;
										}
										else{
											//int size = currentTerritory.getNeigh(value).getTroops().size() + currentTerritory.getNeigh(value).getTroopsInMoving().size();
											int size = currentTerritory.getNeigh(value).getTroops().size();
											//int nextSize = currentTerritory.getNeigh(nextValue).getTroops().size() + currentTerritory.getNeigh(nextValue).getTroopsInMoving().size();
											int nextSize = currentTerritory.getNeigh(nextValue).getTroops().size();
											if((nextSize > size) && (territoryValue.get(currentTerritory.getNeigh(value).getId()) == value)){
												smallNeigh = value;
											}
											else{
												smallNeigh = nextValue;
											}
										}
									}
								}
								else{
									do{
										currentNeigh++;
										if(currentNeigh > 6){
											currentNeigh = 1;
										}
									}while(!((currentTerritory.getNeigh(currentNeigh) != null) && (currentTerritory.getNeigh(currentNeigh).isCrossable())));
									if(!territoryValue.containsKey(currentTerritory.getNeigh(currentNeigh))){
										territoryValue.put(currentTerritory.getNeigh(currentNeigh).getId(), currentNeigh);
									}
									smallNeigh = currentNeigh;
								}
								
								if(move){
									troops.put(currentTerritory.getTroops().get(i), smallNeigh);
								}
								else{
									break;
								}
							}
						}
						for(Troop troop : currentTerritory.getTroops()){
							try {
								if(troops.get(troop) != null){
									makeAction(new MoveTroopAction(currentTerritory.getId(), currentTerritory.getTroops().indexOf(troop), currentTerritory.getNeigh(troops.get(troop)).getId()));
									troops.put(troop, null);
								}
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
		int result = super.hashCode();
		result = prime * result + ((troops == null) ? 0 : troops.hashCode()) + super.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		IADefensive other = (IADefensive) obj;
		if (troops == null) {
			if (other.troops != null)
				return false;
		} else if (!troops.equals(other.troops))
			return false;
		return true;
	}
	
	
	
}