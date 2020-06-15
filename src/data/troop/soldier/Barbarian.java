package data.troop.soldier;

import data.territory.resource.Resource;

/**
 * The data of "Barbarian" soldier
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Barbarian extends Soldier {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Barbarian() {
		super();
		super.setAttack(10);
		super.setDefense(10);
		super.setProductTime(1);
		super.addRessourceCost(Resource.WOOD, 20);
	}
	
	/**
	 * 
	 * @param b The soldier to copy
	 */
	public Barbarian(Barbarian b){
		super(b);
	}
	
	@Override
	public Soldier clone(){
		return new Barbarian(this);
	}

}
