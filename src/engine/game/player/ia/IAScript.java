package engine.game.player.ia;

import java.io.Serializable;
import java.util.ArrayList;

import data.player.Player;
import data.territory.Territory;
import engine.exception.ActionNotPossibleException;
import engine.game.action.Action;
import engine.game.phase.GestionPhase;
import engine.game.player.EntityEngine;
import engine.map.Map;
import engine.map.MapIterator;

/**
 * The super class of algorithm representations of AI
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public abstract class IAScript implements Runnable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityEngine player;
	private GestionPhase gp;
	private Map map;
	private ArrayList<Player> playersData;
	private ArrayList<EntityEngine> players;
	private ArrayList<Integer> ennemiesPosition;
	
	/**
	 * 
	 * @param player The engine of AI
	 * @param map The current map of the game
	 * @param players The list of all players
	 */
	public IAScript(EntityEngine player, Map map, ArrayList<EntityEngine> players){
		this.player = player;
		this.players = players;
		gp = null;
		this.map = map;
		ennemiesPosition = null;
		playersData = new ArrayList<Player>();
	}
	
	/**
	 * Make a action given by AI
	 * @param action The action given
	 * @throws ActionNotPossibleException The exception thrown if the action could not be executing
	 */
	protected void makeAction(Action action) throws ActionNotPossibleException {
		if (gp != null) {
			gp.action(action, player.getDataEntity(), map, playersData);
		}
		
	}
	
	public EntityEngine getPlayer(){
		return player;
	}
	
	public ArrayList<EntityEngine> getPlayers(){
		return players;
	}
	
	public ArrayList<Player> getPlayersData(){
		return playersData;
	}
	
	public GestionPhase getGestionphase(){
		return gp;
	}
	
	public void setGestionPhase(GestionPhase gp){
		this.gp = gp;
	}
	
	public Map getMap(){
		return map;
	}
	
	public ArrayList<Integer> getEnnemiesPosition(){
		return ennemiesPosition;
	}
	
	/**
	 * Fill ennemiesPosition with the current position of enemy capitals
	 */
	protected void setEnnemiesPosition(){
        
        ennemiesPosition = new ArrayList<Integer>();
       
        MapIterator it = (MapIterator) getMap().iterator();
       
        while(it.hasNext()){
            Territory currentTerritory = it.next();
            if(currentTerritory.isCapital()){
                if(!currentTerritory.getOwner().equals(getPlayer().getDataEntity())){
                        ennemiesPosition.add(currentTerritory.getId());
                }
            }
               
        }
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ennemiesPosition == null) ? 0 : ennemiesPosition.hashCode());
		result = prime * result + ((gp == null) ? 0 : gp.hashCode());
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + ((player == null) ? 0 : player.hashCode());
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
		IAScript other = (IAScript) obj;
		if (ennemiesPosition == null) {
			if (other.ennemiesPosition != null)
				return false;
		} else if (!ennemiesPosition.equals(other.ennemiesPosition))
			return false;
		if (gp == null) {
			if (other.gp != null)
				return false;
		} else if (!gp.equals(other.gp))
			return false;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}
	
	
	
}
