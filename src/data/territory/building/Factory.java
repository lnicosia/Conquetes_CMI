package data.territory.building;

import java.util.HashMap;

import data.territory.resource.Resource;

/**
 * Simply resource building data
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Factory extends Building implements ResourceIncrease {

	private static final long serialVersionUID = 1L;
	private HashMap<Integer, Integer> resourceIncrease;

	/**
	 * 
	 */
	public Factory() {
		super();
		super.setBuildTime(1);
		super.addRessourceCost(Resource.WOOD, 10);
		resourceIncrease = new HashMap<Integer, Integer>();
		resourceIncrease.put(Resource.WOOD, 20);
	}
	
	/**
	 * 
	 * @param f The building to copy
	 */
	public Factory(Factory f) {
		super(f);
		for(int i = 0; i < Resource.nbResource; i++){
			this.resourceIncrease.put(i, this.getResourceIncrease(i));
		}
	}
	
	@Override
	public Building clone(){
		return new Factory(this);
	}

	@Override
	public int getResourceIncrease(Integer resource) {
		return resourceIncrease.get(resource);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((resourceIncrease == null) ? 0 : resourceIncrease.hashCode());
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
		Factory other = (Factory) obj;
		if (resourceIncrease == null) {
			if (other.resourceIncrease != null)
				return false;
		} else if (!resourceIncrease.equals(other.resourceIncrease))
			return false;
		return super.equals(obj);
	}
	
	
}
