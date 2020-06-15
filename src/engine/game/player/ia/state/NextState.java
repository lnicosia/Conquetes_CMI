package engine.game.player.ia.state;

import java.io.Serializable;
import java.util.ArrayList;

import engine.game.action.Action;

/**
 * The data of a next state from a state
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class NextState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private State state;
	private Integer length;
	private ArrayList<Action> actions;
	
	public NextState(State state, Integer length, ArrayList<Action> actions){
		this.state = state;
		this.length = length;
		this.actions = actions;
	}
	
	public State getState(){
		return state;
	}
	
	public Integer getLength(){
		return length;
	}
	
	public ArrayList<Action> getActions(){
		return actions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actions == null) ? 0 : actions.hashCode());
		result = prime * result + ((length == null) ? 0 : length.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		NextState other = (NextState) obj;
		if (actions == null) {
			if (other.actions != null)
				return false;
		} else if (!actions.equals(other.actions))
			return false;
		if (length == null) {
			if (other.length != null)
				return false;
		} else if (!length.equals(other.length))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	
	
	
	

}
