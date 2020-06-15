package data.territory.types;

import data.territory.Territory;

/**
 * Territory of desert type
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Desert extends Territory {
	
	public static float coeffResource = (float) 0.5;

	/**
	 * 
	 * @param production The value of production of this territory
	 * @param stock The value of stock of this territory
	 * @param idHashCode id of this territory
	 */
	public Desert(Integer production, Integer stock, Integer idHashCode){
		super(production, stock, idHashCode);
	}
	
	/**
	 * 
	 * @param t The terriory to copy
	 */
	public Desert(Territory t){
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
