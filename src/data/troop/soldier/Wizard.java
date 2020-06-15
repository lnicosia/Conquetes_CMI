package data.troop.soldier;

import data.territory.resource.Resource;

/**
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Wizard extends Soldier {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Wizard() {
		super();
		super.setAttack(25);
		super.setDefense(5);
		super.setProductTime(3);
		super.addRessourceCost(Resource.WOOD, 30);
	}
	
	/**
	 * 
	 * @param w The soldier to copy
	 */
	public Wizard(Wizard w){
		super(w);
	}
	
	@Override
	public Soldier clone(){
		return new Wizard(this);
	}

}
