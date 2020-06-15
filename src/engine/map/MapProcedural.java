package engine.map;

import java.util.ArrayList;

import org.reflections.Reflections;

import data.territory.Territory;
import data.territory.types.Mountain;
import data.territory.types.Water;
import engine.exception.NoPathFindException;
import engine.path.PathFindingPosition;

/**
 * Create a procedural map
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class MapProcedural extends Map{

	private static final long serialVersionUID = 1L;
	
	private int min;
	private int max;
	
	public MapProcedural(){
		
	}

	/**
	 * 
	 * @param nbLine The number of line/column
	 */
	public MapProcedural(int nbLine) {
		this(nbLine, nbLine);
	}
	
	/**
	 * 
	 * @param nbLine The number of line
	 * @param nbColumn The number of culumn
	 */
	public MapProcedural(int nbLine, int nbColumn) {
		this(nbLine, nbColumn, 3, (nbLine * nbColumn)/2);
	}
	
	/**
	 * 
	 * @param map Map to copy
	 */
	public MapProcedural(MapProcedural map) {
		super(map);
	}
	
	/**
	 * 
	 * @param nbLine The number of line
	 * @param nbColumn The number of culumn
	 * @param min Minimum of biome size
	 * @param max Maximum of biome size
	 */
	@SuppressWarnings("unchecked")
	public MapProcedural(int nbLine, int nbColumn, int min, int max) {
		if(min > ((nbLine * nbColumn)/2 - 1)){
			min = (nbLine * nbColumn)/2 - 1;
		}
		if(min < 3){
			min = 3;
		}
		this.min = min;
		
		if(max > (nbLine * nbColumn)/2){
			max = (nbLine * nbColumn)/2;
		}
		if(max < min + 1){
			max = min + 1;
		}
		
		this.max = max;
		setNbLine(nbLine);
		setNbColumn(nbColumn);
		setTableMap(new Class[nbLine][nbColumn]);
		generateTableMap();
		setBaseTerritory(createTerritory(getTableMap()[0][0], 0));
		generateMap();
	}

	@Override 
	public Map clone(){
		return new MapProcedural(this);
	}

	@Override
	protected void generateTableMap() {
		Class<? extends Territory>[][] tableMap = getTableMap();
		
		Reflections territoryPackage = new Reflections("data.territory.types");
		ArrayList<Class<? extends Territory>> territoryList = new ArrayList<Class<? extends Territory>>(territoryPackage.getSubTypesOf(Territory.class));
		
		int territoryRemaining = getNbLine() * getNbColumn();
		
		for(int i = 0; i < getNbLine(); i++){
			for(int j = 0; j < getNbColumn(); j++){
				tableMap[i][j] = null;
			}
		}
		
		while(territoryRemaining > 0){
			int currentX = (int) (Math.random() * getNbColumn());
			int currentY = (int) (Math.random() * getNbLine());
			if(tableMap[currentX][currentY] == null){
				int currentNbTerritory = (int) ((Math.random() * (max - min + 1)) + min);
				Class<? extends Territory> currentType;
				do{
					currentType = territoryList.get((int) (Math.random() * territoryList.size()));
				}while(!tryPlace(tableMap, currentType, new Position(currentX, currentY)));
				territoryRemaining--;
				currentNbTerritory--;
				Position currentPosition = new Position(currentX, currentY);
				int currentNeighbour = 0;
				boolean firstGeneration = true;
				ArrayList<Position> generatedTerritory = new ArrayList<Position>();
				generatedTerritory.add(currentPosition);
				while(currentNbTerritory > 0){
					
					currentNeighbour++;
					if(currentNeighbour <= 6){
						Position neighbourPosition = currentPosition.getNeighbour(currentNeighbour, getNbLine(), getNbColumn());
						if(neighbourPosition != null){
							if((tableMap[neighbourPosition.getX()][neighbourPosition.getY()] == null) && tryPlace(tableMap, currentType, neighbourPosition)){
								generatedTerritory.add(new Position(neighbourPosition));
								territoryRemaining--;
								if(territoryRemaining != 0){
									currentNbTerritory--;
								}
								else{
									break;
								}
							}
						}
					}
					else{
						currentNeighbour = 0;
						generatedTerritory.remove(currentPosition);
						if(!generatedTerritory.isEmpty()){
							currentPosition = generatedTerritory.get(0);
							firstGeneration = false;
						}
						else{
							if(firstGeneration){
								int neighbourType;
								Position newType;
								do{
									neighbourType = (int) (Math.random()*6 + 1);
									newType = currentPosition.getNeighbour(neighbourType, getNbLine(), getNbColumn());
								}while((newType == null) || (tableMap[newType.getX()][newType.getY()] == null) || !tryPlace(tableMap, tableMap[newType.getX()][newType.getY()], currentPosition));
							}
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Try to place a territory to a position
	 * @param map The table of territory class
	 * @param type The class of this territory
	 * @param pos The position asked
	 * @return If the territory has placed
	 */
	private boolean tryPlace(Class<? extends Territory>[][] map, Class<? extends Territory> type, Position pos){
		ArrayList<Class<? extends Territory>> conflictedType = new ArrayList<Class<? extends Territory>>();
		conflictedType.add(Mountain.class);
		conflictedType.add(Water.class);
		map[pos.getX()][pos.getY()] = type;
		if(!conflictedType.contains(type)){
			return true;
		}
		else{
			ArrayList<Position> goodNeigh = new ArrayList<Position>();
			for(int i=1; i <= 6; i++){
				Position neigh = pos.getNeighbour(i, getNbLine(), getNbColumn());
				if(!((neigh == null) || (conflictedType.contains(map[neigh.getX()][neigh.getY()])))){
					goodNeigh.add(neigh);
				}
			}
			for(int i=1; i < goodNeigh.size(); i++){
				try {
					new PathFindingPosition(map, goodNeigh.get(0), goodNeigh.get(i), conflictedType);
				} catch (NoPathFindException e) {
					map[pos.getX()][pos.getY()] = null;
					return false;
				}
			}
			return true;
		}
	}

	@Override
	protected int createProduction() {
		//return (int) (Math.random() * 16 + 5);
		return (int) (Math.random() * 35 + 25);
	}

	@Override
	protected int createStock() {
		return (int) (Math.random() * 21 + 80);
	}

	@Override
	public String toString() {
		return "Normal" + super.toString();
	}

}
