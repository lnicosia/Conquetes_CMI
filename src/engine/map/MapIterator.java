package engine.map;

import java.util.Iterator;

import data.territory.Territory;

/**
 * The iterator which cover a map
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class MapIterator implements Iterator<Territory> {

	private Territory currentTerritory;
	private int nbLine;
	private int nbColumn;
	private int currentPosition;

	/**
	 * 
	 * @param baseTerritory The first territory
	 * @param nbLine Number of line
	 * @param nbColumn Number of column
	 */
	public MapIterator(Territory baseTerritory, int nbLine, int nbColumn) {
		currentTerritory = baseTerritory;
		this.nbColumn = nbColumn;
		this.nbLine = nbLine;
		currentPosition = -1;
	}

	@Override
	public boolean hasNext() {
		return currentPosition < (nbLine * nbColumn) - 1;
	}

	@Override
	public Territory next() {
		if (hasNext()) {
			if (currentPosition == -1) {
				currentPosition++;
				return currentTerritory;
			}
			// If on a paire line
			if ((currentPosition / nbColumn) % 2 == 0) {
				// If in the end of the line
				if ((currentPosition + 1) % nbColumn == 0) {
					currentTerritory = currentTerritory.getNeigh(4);
				}
				// If not in the end of the line
				else {
					currentTerritory = currentTerritory.getNeigh(3);
				}
			}
			// If not on a paire line
			else {
				// If in the end of the line
				if ((currentPosition + 1) % nbColumn == 0) {
					currentTerritory = currentTerritory.getNeigh(5);
				}
				// If not in the end of the line
				else {
					currentTerritory = currentTerritory.getNeigh(6);
				}
			}
			currentPosition++;
			return currentTerritory;
		} else {
			return null;
		}
	}

	public int getPosition() {
		return currentPosition;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
