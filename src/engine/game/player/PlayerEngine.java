package engine.game.player;

import java.util.ArrayList;

import data.player.Player;
import engine.exception.ActionNotPossibleException;
import engine.game.action.Action;
import engine.map.Map;

/**
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class PlayerEngine extends EntityEngine{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param dataEntity The data of this entity
	 */
	public PlayerEngine(Player dataEntity) {
		super(dataEntity);
	}
	
	/**
	 * Give an action to game engine
	 * @param action The action given
	 * @param map The map of the game
	 * @param players The list of players
	 * @throws ActionNotPossibleException Throws if the action could not be executing
	 */
	public String makeAction(Action action, Map map, ArrayList<Player> players) throws ActionNotPossibleException {
		if (getGestionPhase() != null) {
			return getGestionPhase().action(action, getDataEntity(), map, players);
		}
		else{
			throw new ActionNotPossibleException("Action not possible");
		}
		
	}
}
