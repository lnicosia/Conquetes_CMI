package engine.game.action;

import java.io.Serializable;
import java.util.ArrayList;

import data.localisation.Localisation;
import data.player.Player;
import engine.exception.AllianceNotAcceptException;
import engine.map.Map;

/**
 * Action of accept an alliance
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class AcceptAllianceAction implements Action, Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer target;
	
	/**
	 * 
	 * @param target The player who asked an alliance
	 */
	public AcceptAllianceAction(Integer target) {
		this.target = target;
	}

	@Override
	public String run(Player player, Map map, ArrayList<Player> players) throws AllianceNotAcceptException{
		try{
			if((target == null) || !player.getAskAlliance().contains(players.get(target))){
				throw new AllianceNotAcceptException("Wrong Target");
			}
			if(player.getAllies().contains(players.get(target))){
				players.get(target).getAskAlliance().remove(player);
				player.getAskAlliance().remove(players.get(target));
				throw new AllianceNotAcceptException("Already Allied");
			}
			players.get(target).getAskAlliance().remove(player);
			player.getAskAlliance().remove(players.get(target));
			player.getAllies().add(players.get(target));
			players.get(target).getAllies().add(player);
			return "Alliance Accepted";
		} catch (ArrayIndexOutOfBoundsException e){
			throw new AllianceNotAcceptException("Wrong Target");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + target;;
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
		AcceptAllianceAction other = (AcceptAllianceAction) obj;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
	
	
}
