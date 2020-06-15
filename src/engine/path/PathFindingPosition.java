package engine.path;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import data.path.PathFindingRepresentation;
import data.territory.Territory;
import engine.exception.NoPathFindException;
import engine.map.Position;

/**
 * Pathfinding algorithm by Positions
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class PathFindingPosition implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Stack<Position> path;
	
	/**
	 * 
	 * @param map The current map
	 * @param begin The begin position
	 * @param ending The target position
	 * @param conflictedType The class considered like obstacle
	 * @throws NoPathFindException
	 */
	public PathFindingPosition(Class<? extends Territory>[][] map, Position begin, Position ending, ArrayList<Class<? extends Territory>> conflictedType) throws NoPathFindException{
		path = new Stack<Position>();
		makePath(map, begin, ending, conflictedType);
	}
	
	/**
	 * Make the path
	 * @param map The current map
	 * @param begin The begin position
	 * @param ending The target position
	 * @param conflictedType The class considered like obstacle
	 * @throws NoPathFindException
	 */
	public void makePath(Class<? extends Territory>[][] map, Position begin, Position ending, ArrayList<Class<? extends Territory>> conflictedType) throws NoPathFindException{
		HashMap<Position, PathFindingRepresentation<Position>> mapRepresentation = new HashMap<Position, PathFindingRepresentation<Position>>();
		int nbLine = map.length;
		int nbColumn = map[0].length;
		for(int i=0; i < nbLine; i++){
			for(int j=0; j < nbColumn; j++){
				if(!conflictedType.contains(map[i][j])){
					Position newPos = new Position(i, j);
					if(begin.equals(newPos)){
						mapRepresentation.put(newPos, new PathFindingRepresentation<Position>(0));
					}
					else{
						mapRepresentation.put(newPos, new PathFindingRepresentation<Position>(-1));
					}
				}
			}
		}
		
		boolean findEnding = false;
		
		//Voir algo du pathfinding de dijkstra
		while(!findEnding){
			Position currentPosition = null;
			Integer currentWeight = -1;
			for(int i=0; i < nbLine; i++){
				for(int j=0; j < nbColumn; j++){
					Position pos = new Position(i, j);
					if(!(mapRepresentation.get(pos) == null) && !mapRepresentation.get(pos).hasCovered()){
						if(((mapRepresentation.get(pos).getWeight() < currentWeight) || (currentWeight == -1)) && (mapRepresentation.get(pos).getWeight() != -1)){
							currentWeight = mapRepresentation.get(pos).getWeight();
							currentPosition = pos;
						}
					}
				}
			}
			if(currentPosition == null){
				throw new NoPathFindException("No path find");
			}
			mapRepresentation.get(currentPosition).setCovered();
			for(int neigh = 1; neigh <= 6; neigh++){
				Position son = currentPosition.getNeighbour(neigh, nbLine, nbColumn);
				if(!(mapRepresentation.get(son) == null) && (!conflictedType.contains(son))){
					if(!mapRepresentation.get(son).hasCovered()){
						if((mapRepresentation.get(son).getWeight() > mapRepresentation.get(currentPosition).getWeight() + 1) || (mapRepresentation.get(son).getWeight() == -1)){
							mapRepresentation.get(son).setWeight(mapRepresentation.get(currentPosition).getWeight() + 1);
							mapRepresentation.get(son).setPredecessor(currentPosition);
							if(son.equals(ending)){
								findEnding = true;
							}
						}
					}
				}
			}
		}
		
		Position currentPosition = ending;
		while (!currentPosition.equals(begin)){
			path.push(currentPosition);
			currentPosition = mapRepresentation.get(currentPosition).getPredecessor();
		}
	}
	
	public Position next(){
		if(!path.isEmpty()){
			return path.pop();
		}
		else{
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		PathFindingPosition other = (PathFindingPosition) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
	
	

}
