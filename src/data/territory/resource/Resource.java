package data.territory.resource;

import java.io.Serializable;
import java.util.HashMap;

import data.localisation.Localisation;

/**
 * Territory resources data
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Resource implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration for Wood resource
	 */
	public static final int WOOD = 0;
	
	public static final int nbResource = 1;

	private Production production;
	private Stock stock;

	/**
	 * 
	 * @param production The value of production of this resource
	 * @param stock The value of stock of this resource
	 */
	public Resource(int production, int stock) {
		this.production = new Production(production);
		this.stock = new Stock(stock);
	}
	
	/**
	 * Clone this resource to a new instance
	 * @return The new instance
	 */
	public Resource clone(){
		return new Resource(production.getValue(), stock.getValue());
	}

	/**
	 * Product resources depending production of the territory
	 * @param bonusProduction Pourcentage of bonus production
	 * @return The value of resources producted
	 */
	public int product(int bonusProduction) {
		return stock.decrease(production.getValue() + (production.getValue() * bonusProduction / 100));
	}

	public Production getProduction() {
		return production;
	}

	public Stock getStock() {
		return stock;
	}
	
	/**
	 * Clone all resources to new instances
	 * @param resources The map of all resources
	 * @return The new instances
	 */
	public static HashMap<Integer, Integer> cloneResource(HashMap<Integer, Integer> resources){
		HashMap<Integer, Integer> cloneResource = new HashMap<Integer, Integer>();
		for(int i = 0; i < Resource.nbResource; i++){
			cloneResource.put(i, resources.get(i));
		}
		return cloneResource;
	}

	@Override
	public String toString() {
		return Localisation.getInstance().getMessage("Production") + " : " + production.getValue() + "; " + Localisation.getInstance().getMessage("Stock") + " : " + stock.getValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((production == null) ? 0 : production.hashCode());
		result = prime * result + ((stock == null) ? 0 : stock.hashCode());
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
		Resource other = (Resource) obj;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		if (stock == null) {
			if (other.stock != null)
				return false;
		} else if (!stock.equals(other.stock))
			return false;
		return true;
	}

	

}