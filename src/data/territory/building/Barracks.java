package data.territory.building;

import data.localisation.Localisation;
import data.player.Player;
import data.territory.resource.Resource;
import data.troop.Troop;
import data.troop.soldier.*;
import engine.exception.TroopNotBuildException;

/**
 * Simple military building data
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Barracks extends Building implements TroopIncrease {

	private static final long serialVersionUID = 1L;
	private Troop troopInConstruction;

	/**
	 * 
	 */
	public Barracks() {
		super();
		super.setBuildTime(1);
		super.addRessourceCost(Resource.WOOD, 10);
		troopInConstruction = null;
	}
	
	/**
	 * 
	 * @param b The building to copy
	 */
	public Barracks(Barracks b){
		super(b);
		if(b.getTroopInConstruction() != null){
			this.troopInConstruction = b.getTroopInConstruction().clone();
		}
	}

	@Override
	public void createTroop(Player owner, String soldierString) throws TroopNotBuildException {
		if (troopInConstruction != null) {
			throw new TroopNotBuildException(Localisation.getInstance().getMessage("Already Troop In Construction"));
		}

		try {
			Soldier soldier = (Soldier) Class.forName("data.troop.soldier." + soldierString).newInstance();
			if (soldier.buy(owner.getStock())) {
				troopInConstruction = new Troop(owner, soldier);
			} else {
				throw new TroopNotBuildException(Localisation.getInstance().getMessage("Not Enough Resource"));
			}
		} catch (ClassNotFoundException e) {
			throw new TroopNotBuildException(Localisation.getInstance().getMessage("The Troop") + " " + soldierString + " " + Localisation.getInstance().getMessage("Not Exist"));
		} catch (InstantiationException e) {
			throw new TroopNotBuildException(Localisation.getInstance().getMessage("The Class") + " " + soldierString + " " + Localisation.getInstance().getMessage("Not Instanciate"));
		} catch (IllegalAccessException e) {
			throw new TroopNotBuildException(Localisation.getInstance().getMessage("The Class") + " " + soldierString + " " + Localisation.getInstance().getMessage("Not Instanciate"));
		}
	}

	@Override
	public Troop checkTroop() {
		if(troopInConstruction != null){
			if (troopInConstruction.checkCurrentTimeProduct() == 0) {
				Troop newTroop = troopInConstruction;
				troopInConstruction = null;
				return newTroop;
			}
		}
		return null;
	}
	
	@Override
	public Building clone(){
		return new Barracks(this);
	}
	
	@Override
	public Troop getTroopInConstruction(){
		return troopInConstruction;
	}

	@Override
	public boolean haveTroopInConstruction() {
		return (troopInConstruction != null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((troopInConstruction == null) ? 0 : troopInConstruction.hashCode());
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
		Barracks other = (Barracks) obj;
		if (troopInConstruction == null) {
			if (other.troopInConstruction != null)
				return false;
		} else if (!troopInConstruction.equals(other.troopInConstruction))
			return false;
		return super.equals(obj);
	}
	
	
}
