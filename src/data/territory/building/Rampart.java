package data.territory.building;

import data.territory.resource.Resource;

/**
 * Simply defense building data
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Rampart extends Building implements DefenseIncrease {

	private static final long serialVersionUID = 1L;
	private static Integer defenseIncrease = 20;

	/**
	 * 
	 */
	public Rampart() {
		super();
		super.setBuildTime(1);
		super.addRessourceCost(Resource.WOOD, 10);
	}
	
	/**
	 * 
	 * @param r The building to copy
	 */
	public Rampart(Rampart r){
		super(r);
		defenseIncrease = r.getDefenseIncrease();
	}
	
	@Override
	public Building clone(){
		return new Rampart(this);
	}

	@Override
	public int getDefenseIncrease() {
		return defenseIncrease;
	}
	
}
