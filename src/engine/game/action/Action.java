package engine.game.action;

import java.util.ArrayList;

import data.player.Player;
import engine.exception.ActionNotPossibleException;
import engine.map.Map;

/**
 * Interface of actions asked by a player on engine
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public interface Action {

	/**
	 * 
	 * @param player The data player which asked this action
	 * @param map The actual map of game
	 * @param players The list of players in game
	 * @throws ActionNotPossibleException
	 */
	public abstract String run(Player player, Map map, ArrayList<Player> players) throws ActionNotPossibleException;

}
