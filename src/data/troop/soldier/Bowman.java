package data.troop.soldier;

import data.territory.resource.Resource;

/**
 * The data of "Bowman" class
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Bowman extends Soldier {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Bowman() {
		super();
		super.setAttack(15);
		super.setDefense(10);
		super.setProductTime(2);
		super.addRessourceCost(Resource.WOOD, 25);
	}
	
	/**
	 * 
	 * @param b The soldier to copy
	 */
	public Bowman(Bowman b){
		super(b);
	}

	@Override
	public Soldier clone(){
		return new Bowman(this);
	}

}
