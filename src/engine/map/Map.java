package engine.map;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import data.player.Player;
import data.territory.Territory;
import data.territory.building.Building;
import data.territory.resource.Resource;
import data.troop.Troop;

/**
 * Super class of representation of a map
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public abstract class Map implements Iterable<Territory>, Externalizable {

	private static final long serialVersionUID = 1L;
	private Territory baseTerritory;
	private Class<? extends Territory>[][] tableMap;
	private int nbLine;
	private int nbColumn;
	
	/**
	 * 
	 */
	protected Map(){
		
	}
	
	/**
	 * 
	 * @param map Map to copy
	 */
	public Map(Map map){
		this.nbLine = map.getNbLine();
		this.nbColumn = map.getNbColumn();
		tableMap = map.getTableMap();
		baseTerritory = createTerritory(tableMap[0][0], 0);
		generateMap();
		for(Territory t : this){
			t.cloneData(map.getTerritory(t.getId()));
		}
	}

	/**
	 * 
	 * @param nbLine The number of line/column
	 */
	public Map(int nbLine){
		this(nbLine, nbLine);
	}
	
	/**
	 * 
	 * @param nbLine The number of line
	 * @param nbColumn The number of culumn
	 */
	@SuppressWarnings("unchecked")
	public Map(int nbLine, int nbColumn) {
		this.nbLine = nbLine;
		this.nbColumn = nbColumn;
		tableMap = new Class[nbLine][nbColumn];
		generateTableMap();
		baseTerritory = createTerritory(tableMap[0][0], 0);
		generateMap();
	}
	
	/**
	 * Clone the map in a new instance
	 */
	public abstract Map clone();

	/**
	 * Generate the table of territory class
	 */
	protected abstract void generateTableMap();

	/**
	 * Generate the map from the table of territory class
	 */
	protected void generateMap() {
		MapIterator it = (MapIterator) iterator();
		Territory currentTerritory = it.next();
		// Territory currentTerritory = super.getBaseTerritory();
		while (it.hasNext()) {
			Territory newTerritory;
			// If on a paire line
			if (getPositionY(it.getPosition()) % 2 == 0) {
				// If in the end of the line
				if (getPositionX(it.getPosition()) == (getNbColumn() - 1)) {
					newTerritory = createTerritory(tableMap[getPositionX(it.getPosition())][getPositionY(it.getPosition()) + 1], it.getPosition()+1);
					currentTerritory.addNeigh(4, newTerritory);
					newTerritory.addNeigh(1, currentTerritory);
				}
				// If not in the end of the line
				else {
					newTerritory = createTerritory(tableMap[getPositionX(it.getPosition()) + 1][getPositionY(it.getPosition())], it.getPosition()+1);
					currentTerritory.addNeigh(3, newTerritory);
					newTerritory.addNeigh(6, currentTerritory);
					// If it is not the first Line
					if (getPositionY(it.getPosition()) != 0) {
						newTerritory.addNeigh(1, currentTerritory.getNeigh(2));
						currentTerritory.getNeigh(2).addNeigh(4, newTerritory);
						newTerritory.addNeigh(2, currentTerritory.getNeigh(2).getNeigh(3));
						currentTerritory.getNeigh(2).getNeigh(3).addNeigh(5, newTerritory);
					}
				}
			}
			// If not on a paire line
			else {
				// If in the end of the line
				if (getPositionX(it.getPosition()) == 0) {
					newTerritory = createTerritory(tableMap[getPositionX(it.getPosition())][getPositionY(it.getPosition()) + 1], it.getPosition()+1);
					currentTerritory.addNeigh(5, newTerritory);
					newTerritory.addNeigh(2, currentTerritory);
				}
				// If not in the end of the line
				else {
					newTerritory = createTerritory(tableMap[getPositionX(it.getPosition()) - 1][getPositionY(it.getPosition())], it.getPosition()+1);
					currentTerritory.addNeigh(6, newTerritory);
					newTerritory.addNeigh(3, currentTerritory);
					newTerritory.addNeigh(2, currentTerritory.getNeigh(1));
					currentTerritory.getNeigh(1).addNeigh(5, newTerritory);
					newTerritory.addNeigh(1, currentTerritory.getNeigh(1).getNeigh(6));
					currentTerritory.getNeigh(1).getNeigh(6).addNeigh(4, newTerritory);
				}
			}
			currentTerritory = it.next();
		}

	}

	/**
	 * Cover the map
	 */
	@Override
	public Iterator<Territory> iterator() {
		Iterator<Territory> it = new MapIterator(baseTerritory, nbLine, nbColumn);
		return it;
	}

	public Territory getBaseTerritory() {
		return baseTerritory;
	}
	
	public void setBaseTerritory(Territory t){
		baseTerritory = t;
	}
	
	/**
	 * Create the production of one territory
	 * @return the value of production created
	 */
	protected abstract int createProduction();
	
	
	/**
	 * Create the stock of one territory
	 * @return the value of stock created
	 */
	protected abstract int createStock();

	/**
	 * Create a territory from a class
	 * @param type The class of territory
	 * @param id The id of territory
	 * @return The territory
	 */
	protected Territory createTerritory(Class<? extends Territory> type, Integer id) {
		Integer production = createProduction();
		Integer stock = createStock();
		//Permet d'instancier une classe du type "type" avec les paramètre production et stock. Ne pas modifier
		try {
			Constructor<? extends Territory> territoryConstructor = type.getConstructor(new Class[]{Integer.class, Integer.class, Integer.class});
			Territory territory = (Territory) territoryConstructor.newInstance(new Object[]{production, stock, id});
			return territory;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int getPositionY(int currentPosition){
		return (currentPosition) / nbColumn;
	}
	
	public int getPositionX(int currentPosition) {
		// If on a paire line
		if (getPositionY(currentPosition) % 2 == 0) {
			return (currentPosition % nbColumn);
		}
		else{
			return (nbColumn - 1 - (currentPosition % nbColumn));
		}
	}
	
	public Territory getTerritory(int id){
		for(Territory t : this){
			if(t.getId() == id){
				return t;
			}
		}
		return null;
	}
	
	public Class<? extends Territory>[][] getTableMap(){
		return tableMap;
	}
	
	public void setNbLine(int nbLine){
		this.nbLine = nbLine;
	}

	public int getNbLine() {
		return nbLine;
	}
	
	public void setNbColumn(int nbColumn){
		this.nbColumn = nbColumn;
	}
	
	public int getNbColumn() {
		return nbColumn;
	}

	public int getTerritoryNb() {
		return (nbLine * nbColumn);
	}
	
	public void setTableMap(Class<? extends Territory>[][] tableMap){
		this.tableMap = tableMap;
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException{
		out.writeInt(nbColumn);
		out.writeInt(nbLine);
		out.writeObject(tableMap);
		for(Territory t : this){
			out.writeObject(t.getClass());
			out.writeInt(t.getResources().get(Resource.WOOD).getProduction().getValue());
			out.writeInt(t.getResources().get(Resource.WOOD).getStock().getValue());
			out.writeInt(t.getId());
			out.writeBoolean(t.isCapital());
			out.writeObject(t.getOwner());
			out.writeObject(t.getBuilding());
			out.writeObject(t.getBuildingInConstruction());
			out.writeObject(t.getTroops());
			out.writeInt(t.getTroopsInMoving().size());
			for(Integer troop : t.getTroopsInMoving().keySet()){
				out.writeInt(troop);
				out.writeInt(t.getTroopsInMoving().get(troop).getId());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private Territory readTerritory(ObjectInput in, HashMap<Integer,Integer> troopsInMovingInfo) throws ClassNotFoundException, IOException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class<? extends Territory> terrClass = (Class<? extends Territory>) in.readObject();
		Constructor<? extends Territory> terrConstruct = terrClass.getConstructor(new Class[]{Integer.class, Integer.class, Integer.class});
		Territory terr = (Territory) terrConstruct.newInstance(new Object[]{in.readInt(), in.readInt(), in.readInt()});
		if(in.readBoolean()){
			terr.defineCapital();
		}
		terr.setOwner((Player) in.readObject());
		terr.setBuilding((Building) in.readObject());
		terr.setBuildingInConstruction((Building) in.readObject());
		terr.setTroops((ArrayList<Troop>) in.readObject());
		int nbTroopInMoving = in.readInt();
		for(int i = 0; i < nbTroopInMoving; i++){
			Integer troop = in.readInt();
			Integer territoryId = in.readInt();
			troopsInMovingInfo.put(troop, territoryId);
		}
		return terr;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.nbColumn = in.readInt();
		this.nbLine = in.readInt();
		this.tableMap = (Class<? extends Territory>[][]) in.readObject();
		try {
			HashMap<Integer, HashMap<Integer, Integer>> troopsInMovingInfo = new HashMap<Integer, HashMap<Integer,Integer>>();
			troopsInMovingInfo.put(0, new HashMap<Integer,Integer>());
			this.baseTerritory = readTerritory(in, troopsInMovingInfo.get(0));
			MapIterator it = (MapIterator) iterator();
			Territory currentTerritory = it.next();
			while (it.hasNext()) {
				Territory newTerritory;
				// If on a paire line
				if (getPositionY(it.getPosition()) % 2 == 0) {
					// If in the end of the line
					if (getPositionX(it.getPosition()) == (getNbColumn() - 1)) {
						newTerritory = readTerritory(in, troopsInMovingInfo.get(it.getPosition()));
						currentTerritory.addNeigh(4, newTerritory);
						newTerritory.addNeigh(1, currentTerritory);
					}
					// If not in the end of the line
					else {
						newTerritory = readTerritory(in, troopsInMovingInfo.get(it.getPosition()));
						currentTerritory.addNeigh(3, newTerritory);
						newTerritory.addNeigh(6, currentTerritory);
						// If it is not the first Line
						if (getPositionY(it.getPosition()) != 0) {
							newTerritory.addNeigh(1, currentTerritory.getNeigh(2));
							currentTerritory.getNeigh(2).addNeigh(4, newTerritory);
							newTerritory.addNeigh(2, currentTerritory.getNeigh(2).getNeigh(3));
							currentTerritory.getNeigh(2).getNeigh(3).addNeigh(5, newTerritory);
						}
					}
				}
				// If not on a paire line
				else {
					// If in the end of the line
					if (getPositionX(it.getPosition()) == 0) {
						newTerritory = readTerritory(in, troopsInMovingInfo.get(it.getPosition()));
						currentTerritory.addNeigh(5, newTerritory);
						newTerritory.addNeigh(2, currentTerritory);
					}
					// If not in the end of the line
					else {
						newTerritory = readTerritory(in, troopsInMovingInfo.get(it.getPosition()));
						currentTerritory.addNeigh(6, newTerritory);
						newTerritory.addNeigh(3, currentTerritory);
						newTerritory.addNeigh(2, currentTerritory.getNeigh(1));
						currentTerritory.getNeigh(1).addNeigh(5, newTerritory);
						newTerritory.addNeigh(1, currentTerritory.getNeigh(1).getNeigh(6));
						currentTerritory.getNeigh(1).getNeigh(6).addNeigh(4, newTerritory);
					}
				}
				currentTerritory = it.next();
				troopsInMovingInfo.put(it.getPosition(), new HashMap<Integer,Integer>());
			}
			
			for(Territory t : this){
				for(Integer troop : troopsInMovingInfo.get(t.getId()).keySet()){
					//t.addTroopInMoving(troop, getTerritory(troopsInMovingInfo.get(t.getId()).get(troop)));
				}
			}
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseTerritory == null) ? 0 : baseTerritory.hashCode());
		result = prime * result + nbColumn;
		result = prime * result + nbLine;
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
		Map other = (Map) obj;
		if (baseTerritory == null) {
			if (other.baseTerritory != null)
				return false;
		} else if (!baseTerritory.equals(other.baseTerritory))
			return false;
		if (nbColumn != other.nbColumn)
			return false;
		if (nbLine != other.nbLine)
			return false;
		return true;
	}

	public String toString(){
		return "Map, Column = " + Integer.toString(getNbColumn()) + ", Line = " + Integer.toString(getNbLine());
	}
	

}
