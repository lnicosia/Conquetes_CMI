package engine.map;

/**
 * Gestion of positions in a map
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Position {
	
	private int x;
	private int y;
	
	/**
	 * 
	 * @param x The position x
	 * @param y The position y
	 */
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * @param pos The position to copy
	 */
	public Position(Position pos){
		x = pos.getX();
		y = pos.getY();
	}
	
	/**
	 * Give the given neighbour position of this position (by number)
	 * @param neighbour The neighbour (1 to 6)
	 * @param nbLine Number of line of map
	 * @param nbColumn Number of column of map
	 * @return The given neighbour position if exist, null else
	 */
	public Position getNeighbour(int neighbour, int nbLine, int nbColumn){
		Position newPos;
		switch(neighbour){
			case 1:
				if(y%2 == 0){
					if((x-1 >= 0) && (y-1 >= 0)){
						newPos = new Position(x - 1, y - 1);
					}
					else{
						newPos = null;
					}
				}
				else{
					if(y-1 >= 0){
						newPos = new Position(x, y - 1);
					}
					else{
						newPos = null;
					}
				}
				break;
			case 2:
				if(y%2 == 0){
					if(y-1 >= 0){
						newPos = new Position(x, y - 1);
					}
					else{
						newPos = null;
					}
				}
				else{
					if((x+1 < nbColumn) && (y-1 >= 0)){
						newPos = new Position(x + 1, y - 1);
					}
					else{
						newPos = null;
					}
				}
				break;
				
			case 3:
				if(x+1 < nbColumn){
					newPos = new Position(x + 1, y);
				}
				else{
					newPos = null;
				}
				break;
				
			case 4:
				if(y%2 == 0){
					if(y+1 < nbLine){
						newPos = new Position(x, y + 1);
					}
					else{
						newPos = null;
					}
				}
				else{
					if((x+1 < nbColumn) && (y+1 < nbLine)){
						newPos = new Position(x + 1, y + 1);
					}
					else{
						newPos = null;
					}
				}
				break;
				
			case 5:
				if(y%2 == 0){
					if((x-1 >= 0) && (y+1 < nbLine)){
						newPos = new Position(x - 1, y + 1);
					}
					else{
						newPos = null;
					}
				}
				else{
					if(y+1 < nbLine){
						newPos = new Position(x, y + 1);
					}
					else{
						newPos = null;
					}
				}
				break;
				
			case 6:
				if(x-1 >= 0){
					newPos = new Position(x - 1, y);
				}
				else{
					newPos = null;
				}
				break;
				
			default:
				newPos = null;
				break;
		}
		return newPos;
	}
	
	/**
	 * Give the given neighbour position of this position (by position)
	 * @param pos The position of the neighbour
	 * @return The given neighbour position if exist, null else
	 */
	public int getNeighbour(Position pos){
		
		int neighbour;
		
		if((pos.getX() > x+1) || (pos.getX() < x-1) || (pos.getY() > y+1) || (pos.getY() < y-1)){
			neighbour = 0;
		}
		else if((pos.getX() == x) && (pos.getY() == y)){
			neighbour = 0;
		}
		
		else if(pos.getY() == y){
			if(pos.getX() < x){
				neighbour = 6;
			}
			else{
				neighbour = 3;
			}
		}
		
		else if(pos.getX() == (x - (y+1) % 2)){
			if(pos.getY() > y){
				neighbour = 5;
			}
			else{
				neighbour = 1;
			}
		}
		else{
			if(pos.getY() > y){
				neighbour = 4;
			}
			else{
				neighbour = 2;
			}
		}
		
		return neighbour;
	}
	
	/**
	 * Get the range with this position and another
	 * @param other The other position
	 * @return The range
	 */
	public int getRange(Position other){
		Position currentPosition = new Position(x, y);
		int range = 0;
		while (!((currentPosition.getX() == other.getX()) && (currentPosition.getY() == other.getY()))){
			range++;
			if(other.getY() > currentPosition.getY()){
				currentPosition.setY(currentPosition.getY() + 1);
			}
			else if(other.getY() < currentPosition.getY()){
				currentPosition.setY(currentPosition.getY() - 1);
			}
			
			if(other.getX() > currentPosition.getX()){
				currentPosition.setX(currentPosition.getX() + 1);
			}
			else if(other.getX() < currentPosition.getX()){
				currentPosition.setX(currentPosition.getX() - 1);
			}
		}
		return range;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
	
	

}
