package engine.game.action;

import java.io.Serializable;
import java.util.ArrayList;

import data.localisation.Localisation;
import data.player.Player;
import engine.exception.AllianceNotPossibleException;
import engine.map.Map;

/**
 * Action of ask an alliance
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class AskAllianceAction implements Action, Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer target;
	
	/**
	 * 
	 * @param target The player to you ask the alliance
	 */
	public AskAllianceAction(Integer target) {
		this.target = target;
	}

	@Override
	public String run(Player player, Map map, ArrayList<Player> players) throws AllianceNotPossibleException {
		try{
			if(target == null){
				throw new AllianceNotPossibleException("Wrong Target");
			}
			if(player.getAllies().contains(players.get(target))){
				throw new AllianceNotPossibleException("Already Allied");
			}
			if(players.get(target).getAskAlliance().contains(player)){
				throw new AllianceNotPossibleException("Already Asked");
			}
			players.get(target).getAskAlliance().add(player);
			return "Alliance Asked";
		} catch (ArrayIndexOutOfBoundsException e){
			throw new AllianceNotPossibleException("Wrong Target");
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
		AskAllianceAction other = (AskAllianceAction) obj;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
	
	

}
