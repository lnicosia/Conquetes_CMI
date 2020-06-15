package data.territory.types;

import data.territory.Territory;

/**
 * Territory of plain type
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Plain extends Territory {
	
	public static float coeffResource = 1;
	
	/**
	 * 
	 * @param production The value of production of this territory
	 * @param stock The value of stock of this territory
	 * @param idHashCode id of this territory
	 */
	public Plain(Integer production, Integer stock, Integer idHashCode){
		super(production, stock, idHashCode);
	}
	
	/**
	 * 
	 * @param t The territory to copy
	 */
	public Plain(Territory t){
		super(t);
	}
	
	@Override
	public float getCoeffResource() {
		return coeffResource;
	}
	
	@Override
	public boolean isCrossable() {
		return true;
	}
}
