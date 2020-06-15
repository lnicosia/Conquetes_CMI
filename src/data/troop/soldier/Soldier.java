package data.troop.soldier;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The super class of soldier Data
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public abstract class Soldier implements Serializable{

	private static final long serialVersionUID = 1L;
	private int attack, defense;
	private int productTime;
	private int speedPoint;
	private HashMap<Integer, Integer> cost;

	/**
	 * 
	 */
	public Soldier() {
		cost = new HashMap<Integer, Integer>();
	}
	
	/**
	 * 
	 * @param s The soldier to copy
	 */
	public Soldier(Soldier s){
		attack = s.getAttack();
		defense = s.getDefense();
		productTime = s.getProductTime();
		speedPoint = s.getSpeedPoint();
	}

	/**
	 * Try to buy a soldier with these resources
	 * @param resources Resources The available resources
	 * @return If the soldier had bought with successfully
	 */
	public boolean buy(HashMap<Integer, Integer> resources) {
		for (Integer i : cost.keySet()) {
			if (!(cost.get(i) <= resources.get(i))) {
				return false;
			}
		}
		for (Integer i : cost.keySet()) {
			resources.put(i, resources.get(i) - cost.get(i));
		}
		return true;
	}
	
	/**
	 * Clone the soldier to a new instance
	 * @return The new instance
	 */
	public abstract Soldier clone();
	
	public int getAttack(){
		return attack;
	}
	
	public int getDefense(){
		return defense;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}
	
	public int getSpeedPoint(){
		return speedPoint;
	}

	public int getProductTime() {
		return productTime;
	}

	public int decreaseProductTime() {
		if (productTime > 0) {
			productTime--;
		}
		return productTime;
	}

	public void setProductTime(int productTime) {
		this.productTime = productTime;
	}

	public void addRessourceCost(Integer ressource, int cost) {
		this.cost.put(ressource, cost);
	}

	@Override
	public String toString(){
		return getClass().getSimpleName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attack;
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + defense;
		result = prime * result + productTime;
		result = prime * result + speedPoint;
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
		Soldier other = (Soldier) obj;
		if (attack != other.attack)
			return false;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (defense != other.defense)
			return false;
		if (productTime != other.productTime)
			return false;
		if (speedPoint != other.speedPoint)
			return false;
		return true;
	}
	
	

}
