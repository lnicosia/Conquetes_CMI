package engine.game.action;

import java.io.Serializable;
import java.util.ArrayList;

import data.localisation.Localisation;
import data.player.Player;
import engine.exception.ActionNotPossibleException;
import engine.exception.AllianceNotDestroyException;
import engine.map.Map;

/**
 * Action of detroy alliance
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class DestroyAllianceAction implements Action, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Player target;
	
	/**
	 * 
	 * @param target The player who asked an alliance
	 */
	public DestroyAllianceAction(Player target) {
		this.target = target;
	}

	@Override
	public String run(Player player, Map map, ArrayList<Player> players) throws ActionNotPossibleException {
		
		if(target == null){
			throw new AllianceNotDestroyException("Wrong Target");
		}
		if(!player.getAllies().contains(target)){
			throw new AllianceNotDestroyException("Not Allied");
		}
		player.getAllies().remove(target);
		target.getAllies().remove(player);
		return "Alliance Destroy";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		DestroyAllianceAction other = (DestroyAllianceAction) obj;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
	
	

}
