package data.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import data.territory.resource.Resource;

/**
 * Represent the informations about data of players
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	private HashMap<Integer, Integer> stock;
	private ArrayList<Player> allies;
	private ArrayList<Player> askAlliance;
	private String name;
	private int number;

	/**
	 * 
	 * @param name The name of the player
	 * @param number The number of the player
	 */
	public Player(String name, int number) {
		stock = new HashMap<Integer, Integer>();
		stock.put(Resource.WOOD, 0);
		allies = new ArrayList<Player>();
		askAlliance = new ArrayList<Player>();
		this.name = name;
		this.number=number;

	}

	/**
	 * Increase resources of the player
	 * @param resource The type of Resource (data.territory.Resource)
	 * @param value The mount of increases resources
	 */
	public void increaseResource(Integer resource, int value) {
		stock.put(resource, stock.get(resource) + value);
	}

	public HashMap<Integer, Integer> getStock() {
		return stock;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Player> getAllies() {
		return allies;
	}
	
	public ArrayList<Player> getAskAlliance(){
		return askAlliance;
	}
	
	public int getNumber(){
		return number;
	}

	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((askAlliance == null) ? 0 : askAlliance.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + number;
		result = prime * result + ((stock == null) ? 0 : stock.hashCode());
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
		Player other = (Player) obj;
		if (askAlliance == null) {
			if (other.askAlliance != null)
				return false;
		} else if (!askAlliance.equals(other.askAlliance))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number != other.number)
			return false;
		if (stock == null) {
			if (other.stock != null)
				return false;
		} else if (!stock.equals(other.stock))
			return false;
		return true;
	}

	
	
	

}
