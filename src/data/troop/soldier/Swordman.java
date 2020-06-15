package data.troop.soldier;

import data.territory.resource.Resource;

/**
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Swordman extends Soldier {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Swordman() {
		super();
		super.setAttack(15);
		super.setDefense(15);
		super.setProductTime(2);
		super.addRessourceCost(Resource.WOOD, 25);
	}
	
	/**
	 * 
	 * @param s The soldier to copy
	 */
	public Swordman(Swordman s){
		super(s);
	}
	
	@Override
	public Soldier clone(){
		return new Swordman(this);
	}

}
