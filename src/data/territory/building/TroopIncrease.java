package data.territory.building;

import data.player.Player;
import data.troop.Troop;
import engine.exception.TroopNotBuildException;

/**
 * Military buildings interface
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public interface TroopIncrease {

	/**
	 * Create a troop in this territory if it is possible
	 * @param owner Owner of the troop created
	 * @param soldierString String of the soldier created type
	 * 
	 * @throws TroopNotBuildException The troop could not be created
	 */
	public void createTroop(Player owner, String soldierString) throws TroopNotBuildException;

	/**
	 * Check if the troop is in construction. If this occurs, continue the construction
	 * @return Return the troop created if this occurs, null otherwise
	 */
	public Troop checkTroop();

	/**
	 * Check if a troop are building
	 * @return If a troop are building
	 */
	public boolean haveTroopInConstruction();
	
	public Troop getTroopInConstruction();

}
