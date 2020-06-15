package data.troop.soldier;

import data.territory.resource.Resource;

/**
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Spearman extends Soldier {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Spearman() {
		super();
		super.setAttack(10);
		super.setDefense(15);
		super.setProductTime(2);
		super.addRessourceCost(Resource.WOOD, 25);
	}
	
	/**
	 * 
	 * @param s The soldier to copy
	 */ 
	public Spearman(Spearman s){
		super(s);
	}
	
	@Override
	public Soldier clone(){
		return new Spearman(this);
	}

}
