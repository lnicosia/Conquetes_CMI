package data.territory;

import java.util.ArrayList;
import java.util.HashMap;

import data.localisation.Localisation;
import data.player.Player;
import data.territory.building.Building;
import data.territory.building.CapitalBuilding;
import data.territory.resource.Resource;
import data.troop.Troop;
import engine.exception.BuildingNotBuildException;

/**
 * Represent informations about a territory
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public abstract class Territory {

	public static int CONST_SIDES = 6;
	private final int id;
	
	private boolean isCapital;
	private HashMap<Integer, Territory> neighbours;
	private Player owner;
	private HashMap<Integer, Resource> resources;
	private Building building;
	private Building buildingInConstruction;
	private ArrayList<Troop> troops;
	private HashMap<Integer, Territory> troopsInMoving;

	/**
	 * Create a new territory
	 * @param production Resource's production by turn of the territory
	 * @param stock Resource's stock maximum of the territory
	 */
	public Territory(Integer production, Integer stock, Integer id) {
		initialiseNeighbours();
		owner = null;
		resources = new HashMap<Integer, Resource>();
		for(int i = 0; i < Resource.nbResource; i++){
			resources.put(i, new Resource((int) ((float)production * getCoeffResource()), (int) ((float)stock * getCoeffResource())));
		}
		buildingInConstruction = null;
		setBuilding(null);
		troops = new ArrayList<Troop>();
		troopsInMoving = new HashMap<Integer, Territory>();
		isCapital = false;
		
		this.id = id;
	}

	/**
	 * Copy data of a territory to remplace this
	 * @param t The territory to copy
	 */
	public Territory(Territory t) {
		neighbours = t.getNeighbours();
		for(Integer neigh : t.getNeighbours().keySet()){
			int neighTerr = 0;
			switch(neigh){
				case 1:
					neighTerr = 5;
					break;
				case 2:
					neighTerr = 4;
					break;
				case 3:
					neighTerr = 6;
					break;
				case 4:
					neighTerr = 2;
					break;
				case 5:
					neighTerr = 1;
					break;
				case 6:
					neighTerr = 3;
					break;
			}
			Territory tNeigh = t.getNeighbours().get(neigh);
			tNeigh.getNeighbours().put(neighTerr, this);
		}
		owner = t.getOwner();
		resources = t.getResources();
		buildingInConstruction = t.getBuildingInConstruction();
		building = t.getBuilding();
		troops = t.getTroops();
		troopsInMoving = new HashMap<Integer, Territory>();
		id = t.getId();
		isCapital = t.isCapital();
	}
	
	/**
	 * Copie data from an other territory
	 * @param t The territory to copy
	 */
	public void cloneData(Territory t){
		if(t.getOwner() == null){
			owner = null;
		}
		else{
			owner = t.getOwner();
		}
		resources = new HashMap<Integer, Resource>();
		for(Integer resource : t.getResources().keySet()){
			resources.put(resource, t.getResources().get(resource));
		}
		if(t.getBuildingInConstruction() == null){
			buildingInConstruction = null;
		}
		else{
			buildingInConstruction = t.getBuildingInConstruction().clone();
		}
		if(t.getBuilding() == null){
			building = null;
		}
		else{
			building = t.getBuilding().clone();
		}
		troops = new ArrayList<Troop>();
		troopsInMoving = new HashMap<Integer, Territory>();
		for(Troop troop : t.getTroops()){
			troops.add(troop.clone());
		}
		for(Integer i : t.getTroopsInMoving().keySet()){
			troopsInMoving.put(i, t.getTroopsInMoving().get(i));
		}
		isCapital = t.isCapital();
	}
	
	/**
	 * Define this territory like a Capital
	 */
	public void defineCapital(){
		isCapital = true;
		setBuilding(new CapitalBuilding());
		for(Integer resource : getResources().keySet()){
			getResources().get(resource).getStock().setValue(10000000);
		}
	}

	public void undefineCapital(){
		isCapital = false;
		setBuilding(null);
		for(Integer resource : getResources().keySet()){
			getResources().get(resource).getStock().setValue(0);
		}
	}
	/**
	 * Create a territory in the territory if enough resources and not a building already build
	 * @param resources The resource owned
	 * @param buildingString The type of building
	 * @throws BuildingNotBuildException Return if the creation has not occurred
	 */
	public void createBuilding(HashMap<Integer, Integer> resources, String buildingString) throws BuildingNotBuildException {
		if (buildingInConstruction != null) {
			throw new BuildingNotBuildException("Il y a d�ja un batiment en construction dans ce territoire");
		}
		try {
			Building building = (Building) Class.forName("data.territory.building." + buildingString).newInstance();
			if (building.buy(resources)) {
				buildingInConstruction = building;
			}
			else{
				throw new BuildingNotBuildException(Localisation.getInstance().getMessage("Not Enough Resource"));
			}
		} catch (ClassNotFoundException e) {
			throw new BuildingNotBuildException(Localisation.getInstance().getMessage("The Building") + buildingString + Localisation.getInstance().getMessage("Not Exist"));
		} catch (InstantiationException e) {
			throw new BuildingNotBuildException(Localisation.getInstance().getMessage("The Class") + buildingString + "Not Instanciate");
		} catch (IllegalAccessException e) {
			throw new BuildingNotBuildException(Localisation.getInstance().getMessage("The Class") + buildingString + "Not Instanciate");
		}
	}

	/**
	 * Initialise the ArrayList of neighbours of territory
	 */
	public void initialiseNeighbours() {
		neighbours = new HashMap<Integer, Territory>();
		for (int i = 1; i <= Territory.CONST_SIDES; i++) {
			neighbours.put(i, null);
		}
	}

	/**
	 * Decrease the build time left for the building in Construction
	 */
	public void checkBuildingInConstruction() {
		if (buildingInConstruction != null) {
			if (buildingInConstruction.decreaseBuildTime() == 0) {
				setBuilding(buildingInConstruction);
				buildingInConstruction = null;
			}
		}
	}

	/**
	 * Define a move for a troop
	 * @param troop The troop to move
	 * @param target The targeted territory
	 */
	public void addTroopInMoving(int troop, Territory target) {
		troopsInMoving.put(troop, target);
	}

	/**
	 * Move all troops which have a defined move
	 */
	public void moveTroops() {
		if (!troopsInMoving.isEmpty()) {
			ArrayList<Troop> troopsToRemove = new ArrayList<Troop>();
			for (Integer troop : troopsInMoving.keySet()) {
				troopsInMoving.get(troop).addTroop(troops.get(troop));
				troopsToRemove.add(troops.get(troop));
			}
			for (Troop troop : troopsToRemove) {
				troops.remove(troop);
			}
			troopsInMoving.clear();
		}
	}
	
	/**
	 * Cancel the building construction
	 */
	public void cancelConstruction(){
		if(buildingInConstruction != null){
			for(Integer resource : owner.getStock().keySet()){
				owner.increaseResource(resource, buildingInConstruction.getRessourceCost(owner.getStock().get(resource)));
			}
			buildingInConstruction = null;
		}
	}

	public HashMap<Integer, Resource> getResources() {
		return resources;
	}

	public void addNeigh(int neighbour, Territory territory) {
		neighbours.put(neighbour, territory);
	}

	public Territory getNeigh(int neighbour) {
		return neighbours.get(neighbour);
	}

	public HashMap<Integer, Territory> getNeighbours() {
		return neighbours;
	}

	public void addTroop(Troop t) {
		troops.add(t);
	}

	public ArrayList<Troop> getTroops() {
		return troops;
	}
	
	public abstract float getCoeffResource();

	public Building getBuilding() {
		return building;
	}
	
	public HashMap<Integer, Territory> getTroopsInMoving(){
		return troopsInMoving;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}
	
	public void setBuildingInConstruction(Building building){
		buildingInConstruction = building;
	}
	
	public void setTroops(ArrayList<Troop> troops){
		this.troops = troops;
	}
	
	public void setTroopsInMoving(HashMap<Integer, Territory> troops){
		troopsInMoving = troops;
	}

	public void setOwner(Player player) {
		this.owner = player;
	}

	public Player getOwner() {
		return owner;
	}
	
	public abstract boolean isCrossable();
	
	public boolean isCapital(){
		return isCapital;
	}

	public Building getBuildingInConstruction() {
		return buildingInConstruction;
	}
	
	public int getId(){
		return id;
	}

	@Override
	public String toString() {
		return Localisation.getInstance().getMessage("Owner") + " : " + getOwner();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((building == null) ? 0 : building.hashCode());
		result = prime * result + ((buildingInConstruction == null) ? 0 : buildingInConstruction.hashCode());
		result = prime * result + id;
		result = prime * result + (isCapital ? 1231 : 1237);
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((resources == null) ? 0 : resources.hashCode());
		result = prime * result + ((troops == null) ? 0 : troops.hashCode());
		result = prime * result + ((troopsInMoving == null) ? 0 : troopsInMoving.hashCode());
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
		Territory other = (Territory) obj;
		if (building == null) {
			if (other.building != null)
				return false;
		} else if (!building.equals(other.building))
			return false;
		if (buildingInConstruction == null) {
			if (other.buildingInConstruction != null)
				return false;
		} else if (!buildingInConstruction.equals(other.buildingInConstruction))
			return false;
		if (id != other.id)
			return false;
		if (isCapital != other.isCapital)
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		if (troops == null) {
			if (other.troops != null)
				return false;
		} else if (!troops.equals(other.troops))
			return false;
		if (troopsInMoving == null) {
			if (other.troopsInMoving != null)
				return false;
		} else if (!troopsInMoving.equals(other.troopsInMoving))
			return false;
		return true;
	}
	
}
