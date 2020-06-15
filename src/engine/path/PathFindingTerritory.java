package engine.path;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Stack;

import data.path.PathFindingRepresentation;
import data.territory.Territory;
import engine.map.Map;
import engine.map.Position;

/**
 * Pathfinding algorithm by territories
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class PathFindingTerritory implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Stack<Integer> path;
	
	/**
	 * 
	 * @param map The current map
	 * @param begin The begin territory
	 * @param ending The target territory
	 */
	public PathFindingTerritory(Map map, Territory begin, Territory ending){
		path = new Stack<Integer>();
		makePath(map, begin, ending);
	}
	
	/**
	 * Make the path
	 * @param map The current map
	 * @param begin The begin territory
	 * @param ending The target territory
	 */
	public void makePath(Map map, Territory begin, Territory ending){
		HashMap<Territory, PathFindingRepresentation<Territory>> mapRepresentation = new HashMap<Territory, PathFindingRepresentation<Territory>>();
		Position endPosition = new Position(map.getPositionX(ending.getId()), map.getPositionY(ending.getId()));
		for(Territory t : map){
			if(t.isCrossable()){
				Position tPosition = new Position(map.getPositionX(t.getId()), map.getPositionY(t.getId()));
				if(t.equals(begin)){
					mapRepresentation.put(t, new PathFindingRepresentation<Territory>(0, tPosition.getRange(endPosition)));
				}
				else{
					mapRepresentation.put(t, new PathFindingRepresentation<Territory>(-1, tPosition.getRange(endPosition)));
				}
			}
		}
		
		boolean findEnding = false;
		
		//Algorithme A*
		while(!findEnding){
			Territory currentTerritory = null;
			Integer currentWeight = -1;
			Integer currentFinalRange = -1;
			for(Territory t : mapRepresentation.keySet()){
				if(!mapRepresentation.get(t).hasCovered()){
					if(mapRepresentation.get(t).getWeight() != -1){
						if(currentWeight == -1){
							currentWeight = mapRepresentation.get(t).getWeight();
							currentFinalRange = mapRepresentation.get(t).getFinalRange();
							currentTerritory = t;
						}
						else if((mapRepresentation.get(t).getWeight() + mapRepresentation.get(t).getFinalRange()) < (currentWeight + currentFinalRange)){
							currentWeight = mapRepresentation.get(t).getWeight();
							currentFinalRange = mapRepresentation.get(t).getFinalRange();
							currentTerritory = t;
						}
						else if(((mapRepresentation.get(t).getWeight() + mapRepresentation.get(t).getFinalRange()) == (currentWeight + currentFinalRange)) && (mapRepresentation.get(t).getFinalRange() < currentFinalRange)){
							currentWeight = mapRepresentation.get(t).getWeight();
							currentFinalRange = mapRepresentation.get(t).getFinalRange();
							currentTerritory = t;
						}
					}
				}
			}
			mapRepresentation.get(currentTerritory).setCovered();
			for(Integer neigh : currentTerritory.getNeighbours().keySet()){
				if((currentTerritory.getNeigh(neigh) != null) && (currentTerritory.getNeigh(neigh).isCrossable())){
					Territory son = currentTerritory.getNeigh(neigh);
					if(!mapRepresentation.get(son).hasCovered()){
						if((mapRepresentation.get(son).getWeight() > mapRepresentation.get(currentTerritory).getWeight() + 1) || (mapRepresentation.get(son).getWeight() == -1)){
							mapRepresentation.get(son).setWeight(mapRepresentation.get(currentTerritory).getWeight() + 1);
							mapRepresentation.get(son).setPredecessor(currentTerritory);
							if(son.equals(ending)){
								findEnding = true;
							}
						}
					}
				}
			}
		}
		
		Territory currentTerritory = ending;
		while (!currentTerritory.equals(begin)){
			path.push(currentTerritory.getId());
			currentTerritory = mapRepresentation.get(currentTerritory).getPredecessor();
		}
	}
	
	public Integer next(){
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
		PathFindingTerritory other = (PathFindingTerritory) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
	
	

}
