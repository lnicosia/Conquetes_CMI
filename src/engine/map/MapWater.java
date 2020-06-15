package engine.map;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import data.territory.Territory;
import data.territory.types.Water;

/**
 * Map filled with water (for editor)
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class MapWater extends Map {
	
	public MapWater(){
		
	}


	/**
	 * 
	 * @param nbLine The number of line/column
	 */
	public MapWater(int nbLine) {
		super(nbLine);
	}


	/**
	 * 
	 * @param nbLine The number of line
	 * @param nbColumn The number of culumn
	 */
	public MapWater(int nbLine, int nbColumn){
		super(nbLine,nbColumn);
	}
	
	/**
	 * 
	 * @param mw Map to copy
	 */
	public MapWater(MapWater mw){
		super(mw);
	}
	
	@Override
	protected void generateTableMap() {
		
		Class<? extends Territory>[][] tableMap = getTableMap();
		for(int i = 0; i < tableMap.length; i++){
			for(int j = 0; j < tableMap[i].length; j++){
				tableMap[i][j] = Water.class;
			}
		}
	}

	/**
	 * Give a territory with the class given
	 * @param x The position x of the territory
	 * @param y The position y of the territory
	 * @param type The class asked
	 * @return The territory created
	 */
	@SuppressWarnings("unchecked")
	public Territory setType(int x, int y, String type){
		Territory tr = null;
		MapIterator mi = new MapIterator( getBaseTerritory() , getNbLine() , getNbColumn() );
		while(mi.hasNext()){
			Territory t = mi.next();
			if((getPositionX(mi.getPosition())==x)&&(getPositionY(mi.getPosition())==y)){
				try {
					Class<? extends Territory> territoryType;
					if(type.equals("Capital")){
						territoryType = (Class<? extends Territory>) Class.forName("data.territory.types.Plain" );

					}
					else{
						territoryType = (Class<? extends Territory>) Class.forName("data.territory.types." + type );
						
					}
					Constructor<? extends Territory> ctr = territoryType.getConstructor(new Class[] {Territory.class});
					tr = (Territory) ctr.newInstance(new Object[]{t});
					
					break;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return tr;
	}
	
	@Override
	public Map clone(){
		return new MapWater(this);
	}

	@Override
	protected int createProduction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int createStock() {
		// TODO Auto-generated method stub
		return 0;
	}

}
