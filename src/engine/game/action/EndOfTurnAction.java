package engine.game.action;

import java.io.Serializable;
import java.util.ArrayList;

import data.player.Player;
import engine.exception.ActionNotPossibleException;
import engine.exception.EndOfTurnException;
import engine.map.Map;

/**
 * Action of end the turn
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class EndOfTurnAction implements Action, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String run(Player player, Map map, ArrayList<Player> players) throws ActionNotPossibleException {
			throw new EndOfTurnException("");
	}
	
	
}