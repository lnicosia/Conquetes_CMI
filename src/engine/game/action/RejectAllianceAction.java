package engine.game.action;

import java.io.Serializable;
import java.util.ArrayList;

import data.localisation.Localisation;
import data.player.Player;
import engine.exception.AllianceNotRejectException;
import engine.map.Map;

/**
 * Action of reject an alliance
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class RejectAllianceAction implements Action, Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer target;
	
	/**
	 * 
	 * @param target The player to reject
	 */
	public RejectAllianceAction(Integer target) {
		this.target = target;
	}

	@Override
	public String run(Player player, Map map, ArrayList<Player> players) throws AllianceNotRejectException{
		try{
			if((target == null) || !player.getAskAlliance().contains(players.get(target))){
				throw new AllianceNotRejectException("Wrong Target");
			}
			players.get(target).getAskAlliance().remove(player);
			player.getAskAlliance().remove(players.get(target));
			return "Alliance Rejected";
		} catch (ArrayIndexOutOfBoundsException e){
			throw new AllianceNotRejectException("Wrong Target");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + target;
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
		RejectAllianceAction other = (RejectAllianceAction) obj;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
	
	
}
