package engine.map;

import java.util.ArrayList;

import org.reflections.Reflections;

import data.territory.Territory;

/**
 * Representation of a random map
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class MapRandom extends Map {

	private static final long serialVersionUID = 1L;
	
	public MapRandom(){
		
	}

	/**
	 * 
	 * @param nbLine The number of line
	 * @param nbColumn The number of culumn
	 */
	public MapRandom(int nbLine, int nbColumn) {
		super(nbLine, nbColumn);
	}
	

	/**
	 * 
	 * @param nbLine The number of line/column
	 */
	public MapRandom(int nbLine) {
		super(nbLine);
	}
	
	/**
	 * 
	 * @param map Map to copy
	 */
	public MapRandom(MapRandom map) {
		super(map);
	}

	@Override
	protected void generateTableMap() {
		Reflections territoryPackage = new Reflections("data.territory.types");
		ArrayList<Class<? extends Territory>> territoryList = new ArrayList<Class<? extends Territory>>(territoryPackage.getSubTypesOf(Territory.class));
		
		Class<? extends Territory>[][] tableMap = getTableMap();
		for(int i = 0; i < tableMap.length; i++){
			for(int j = 0; j < tableMap[i].length; j++){
				int territoryType = (int) (Math.random() * territoryList.size());
				tableMap[i][j] = territoryList.get(territoryType);
			}
		}
		
	}	
	
	@Override
	public Map clone(){
		return new MapRandom(this);
	}
	
	@Override
	protected int createProduction(){
		return (int) (Math.random() * 16 + 5);
	}
	
	@Override
	protected int createStock(){
		return (int) (Math.random() * 21 + 80);
	}

	@Override
	public String toString() {
		return "Random" + super.toString();
	}
	
	

}
