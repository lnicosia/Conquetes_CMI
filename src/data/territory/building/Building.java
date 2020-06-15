package data.territory.building;

import java.io.Serializable;
import java.util.HashMap;

import data.territory.resource.Resource;

/**
 * The super class of buildings Data
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public abstract class Building implements Serializable {

	private static final long serialVersionUID = 1L;
	private int buildTime;
	private HashMap<Integer, Integer> buildCost;

	/**
	 * 
	 */
	public Building() {
		buildCost = new HashMap<Integer, Integer>();
	}
	
	/**
	 * 
	 * @param b The building to copy
	 */
	public Building(Building b){
		buildTime = b.getBuildTime();
		buildCost = new HashMap<Integer, Integer>();
		for(int i = 0; i < Resource.nbResource; i++){
			buildCost.put(i, b.getRessourceCost(i));
		}
	}

	/**
	 * Create a building if it is possible
	 * @param resources Resource used to create the building
	 * @return If the building have been created
	 */
	public boolean buy(HashMap<Integer, Integer> resources) {
		for (Integer i : buildCost.keySet()) {
			if (!(buildCost.get(i) <= resources.get(i))) {
				return false;
			}
		}
		for (Integer i : buildCost.keySet()) {
			resources.put(i, resources.get(i) - buildCost.get(i));
		}
		return true;
	}
	
	/**
	 * Clone the building to a new instance
	 */
	public abstract Building clone();

	public int getBuildTime() {
		return buildTime;
	}

	public int getRessourceCost(Integer resource) {
		return buildCost.get(resource);
	}

	public int decreaseBuildTime() {
		if (buildTime != 0) {
			buildTime--;
		}
		return buildTime;
	}

	public void setBuildTime(int buildTime) {
		this.buildTime = buildTime;
	}

	/**
	 * Add resource to the cost of this building
	 * @param ressource Type of {@link data.territory.resource.Resource resource}
	 * @param value Amount of this cost
	 */
	public void addRessourceCost(Integer ressource, int value) {
		buildCost.put(ressource, value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buildCost == null) ? 0 : buildCost.hashCode());
		result = prime * result + buildTime;
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
		Building other = (Building) obj;
		if (buildCost == null) {
			if (other.buildCost != null)
				return false;
		} else if (!buildCost.equals(other.buildCost))
			return false;
		if (buildTime != other.buildTime)
			return false;
		return true;
	}
	
	

}
