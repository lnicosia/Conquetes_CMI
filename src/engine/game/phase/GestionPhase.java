package engine.game.phase;

import java.io.Serializable;
import java.util.ArrayList;

import data.player.Player;
import engine.exception.ActionNotPossibleException;
import engine.game.action.Action;
import engine.map.Map;

/**
 * The game phase of gestion
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GestionPhase extends Thread implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean endOfTurn;
	private final String keyEndOfTurn = new String();
	private final String loopSuspended;
	private final String keyIsLoopSuspended = new String();

	/**
	 * 
	 * @param map The map of game
	 * @param loopSuspended The key for suspend the thread
	 */
	public GestionPhase(Map map, String loopSuspended) {
		endOfTurn = false;
		this.loopSuspended = loopSuspended;
	}

	public synchronized String action(Action action, Player player, Map map, ArrayList<Player> players) throws ActionNotPossibleException {
		synchronized (loopSuspended) {
			return action.run(player, map, players);
		}
	}
	
	public boolean getEndOfTurn(){
		synchronized (loopSuspended) {
			synchronized (keyEndOfTurn) {
				return endOfTurn;
			}
		}
	}
	
	public void endOfTurn() {
		synchronized (loopSuspended) {
			synchronized (keyEndOfTurn) {
				endOfTurn = true;
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (endOfTurn ? 1231 : 1237);
		result = prime * result + ((keyEndOfTurn == null) ? 0 : keyEndOfTurn.hashCode());
		result = prime * result + ((keyIsLoopSuspended == null) ? 0 : keyIsLoopSuspended.hashCode());
		result = prime * result + ((loopSuspended == null) ? 0 : loopSuspended.hashCode());
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
		GestionPhase other = (GestionPhase) obj;
		if (endOfTurn != other.endOfTurn)
			return false;
		if (keyEndOfTurn == null) {
			if (other.keyEndOfTurn != null)
				return false;
		} else if (!keyEndOfTurn.equals(other.keyEndOfTurn))
			return false;
		if (keyIsLoopSuspended == null) {
			if (other.keyIsLoopSuspended != null)
				return false;
		} else if (!keyIsLoopSuspended.equals(other.keyIsLoopSuspended))
			return false;
		if (loopSuspended == null) {
			if (other.loopSuspended != null)
				return false;
		} else if (!loopSuspended.equals(other.loopSuspended))
			return false;
		return true;
	}

	
	
	

}
