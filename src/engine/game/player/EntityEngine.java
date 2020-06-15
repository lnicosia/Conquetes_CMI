package engine.game.player;

import java.io.Serializable;

import data.player.Player;
import engine.game.phase.GestionPhase;

/**
 * Super class of entity (player or AI) representation 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public abstract class EntityEngine implements Serializable{

	private static final long serialVersionUID = 1L;
	private Player dataEntity;
	private GestionPhase gp;
	
	/**
	 * 
	 * @param dataEntity The data of this entity
	 */
	public EntityEngine(Player dataEntity){
		this.dataEntity = dataEntity;
		gp = null;
	}
	
	/**
	 * Give the end of turn to game engine
	 */
	public void endOfGestionTurn() {
		if (gp != null) {
			gp.endOfTurn();
		}
	}
	
	public Player getDataEntity(){
		return dataEntity;
	}
	
	public void setGestionPhase(GestionPhase gp){
		this.gp = gp;
	}
	
	public GestionPhase getGestionPhase(){
		return gp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataEntity == null) ? 0 : dataEntity.hashCode());
		result = prime * result + ((gp == null) ? 0 : gp.hashCode());
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
		EntityEngine other = (EntityEngine) obj;
		if (dataEntity == null) {
			if (other.dataEntity != null)
				return false;
		} else if (!dataEntity.equals(other.dataEntity))
			return false;
		if (gp == null) {
			if (other.gp != null)
				return false;
		} else if (!gp.equals(other.gp))
			return false;
		return true;
	}

	
	
	

}
