package data.territory.building;

/**
 * Resource buildings interface
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public interface ResourceIncrease {

	/**
	 * Get the resource inscrease by the building
	 * @param i The type of {@link data.territory.resource.Resource resource}
	 * @return the value of resources
	 */
	public int getResourceIncrease(Integer i);

}
