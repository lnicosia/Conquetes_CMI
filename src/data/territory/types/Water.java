package data.territory.types;

import data.territory.Territory;

/**
 * Territory of water type
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Water extends Territory {
	
	public static float coeffResource = 0;
	
	public Water(Integer production, Integer stock, Integer idHashCode){
		super(production, stock, idHashCode);
	}
	
	public Water(Territory t){
		super(t);
	}

	@Override
	public float getCoeffResource() {
		return coeffResource;
	}
	
	@Override
	public boolean isCrossable() {
		return false;
	}
}
