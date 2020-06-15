package data.territory.building;

import java.util.HashMap;

import data.localisation.Localisation;
import data.player.Player;
import data.territory.resource.Resource;
import data.troop.Troop;
import data.troop.soldier.Soldier;
import engine.exception.TroopNotBuildException;

/**
 * Capital building data
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class CapitalBuilding extends Building implements ResourceIncrease, DefenseIncrease, TroopIncrease {

	private static final long serialVersionUID = 1L;
	private static final Integer defenseIncrease = 20;
	private HashMap<Integer, Integer> resourceIncrease;
	private Troop troopInConstruction;

	/**
	 * 
	 */
	public CapitalBuilding() {
		super();
		resourceIncrease = new HashMap<Integer, Integer>();
		resourceIncrease.put(Resource.WOOD, 20);
		super.addRessourceCost(Resource.WOOD, 0);
	}
	
	/**
	 * 
	 * @param cB The building to copy
	 */
	public CapitalBuilding(CapitalBuilding cB){
		super(cB);
		resourceIncrease = new HashMap<Integer, Integer>();
		for(int i = 0; i < Resource.nbResource; i++){
			this.resourceIncrease.put(i, cB.getResourceIncrease(i));
		}
		if(cB.getTroopInConstruction() == null){
			this.troopInConstruction = null;
		}
		else{
			this.troopInConstruction = cB.getTroopInConstruction().clone();
		}
	}

	@Override
	public int getDefenseIncrease() {
		return defenseIncrease;
	}

	@Override
	public int getResourceIncrease(Integer resource) {
		return resourceIncrease.get(resource);
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
	public Building clone(){
		return new CapitalBuilding(this);
	}
	
	@Override
	public Troop getTroopInConstruction(){
		return troopInConstruction;
	}

	@Override
	public Troop checkTroop() {
		if (troopInConstruction != null) {
			if (troopInConstruction.checkCurrentTimeProduct() == 0) {
				Troop newTroop = troopInConstruction;
				troopInConstruction = null;
				return newTroop;
			}
		}
		return null;
	}

	@Override
	public boolean haveTroopInConstruction() {
		return (troopInConstruction != null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((resourceIncrease == null) ? 0 : resourceIncrease.hashCode());
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
		CapitalBuilding other = (CapitalBuilding) obj;
		if (resourceIncrease == null) {
			if (other.resourceIncrease != null)
				return false;
		} else if (!resourceIncrease.equals(other.resourceIncrease))
			return false;
		if (troopInConstruction == null) {
			if (other.troopInConstruction != null)
				return false;
		} else if (!troopInConstruction.equals(other.troopInConstruction))
			return false;
		return super.equals(obj);
	}
	
	
}
