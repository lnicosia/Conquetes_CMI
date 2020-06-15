package data.path;

/**
 * Data of states used for PathFinding algorithms
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 * @param <T> Type of predecessor
 */
public class PathFindingRepresentation<T> {
	
	private int finalRange;
	private int weight;
	private boolean covered;
	private T predecessor;
	private int direction;
	
	/**
	 * 
	 * @param weight The initial weight of this state
	 */
	public PathFindingRepresentation(int weight){
		this(weight, -1);
	}
	
	/**
	 * 
	 * @param weight The initial weight of this state
	 * @param finalRange The range with the final state
	 */
	public PathFindingRepresentation(int weight, int finalRange){
		this.weight = weight;
		this.finalRange = finalRange;
		covered = false;
		predecessor = null;
		direction = -1;
	}
	
	public int getFinalRange(){
		return finalRange;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public boolean hasCovered(){
		return covered;
	}
	
	public T getPredecessor(){
		return predecessor;
	}
	
	public int getDirection(){
		return direction;
	}
	
	public void setFinalRange(int finalRange){
		this.finalRange = finalRange;
	}
	
	public void setWeight(int weight){
		this.weight = weight;
	}
	
	public void setCovered(){
		covered = true;
	}
	
	public void setPredecessor(T predecessor){
		this.predecessor = predecessor;
	}
	
	public void setPredecessor(T predecessor, int direction){
		setPredecessor(predecessor);
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "PathFindingRepresentation [finalRange=" + finalRange + ", weight=" + weight + ", covered=" + covered + ", predecessor=" + predecessor + ", direction=" + direction + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (covered ? 1231 : 1237);
		result = prime * result + direction;
		result = prime * result + finalRange;
		result = prime * result + ((predecessor == null) ? 0 : predecessor.hashCode());
		result = prime * result + weight;
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathFindingRepresentation other = (PathFindingRepresentation) obj;
		if (covered != other.covered)
			return false;
		if (direction != other.direction)
			return false;
		if (finalRange != other.finalRange)
			return false;
		if (predecessor == null) {
			if (other.predecessor != null)
				return false;
		} else if (!predecessor.equals(other.predecessor))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}
	
	

}
