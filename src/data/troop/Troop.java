package data.troop;

import java.io.Serializable;
import java.util.ArrayList;

import data.player.Player;
import data.troop.soldier.Soldier;

/**
 * The data of a troop
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Troop implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Soldier> soldiers;
	private Player owner;

	/**
	 * 
	 * @param owner The owner of the troop
	 */
	public Troop(Player owner) {
		soldiers = new ArrayList<Soldier>();
		this.owner = owner;
	}

	/**
	 * 
	 * @param owner The owner of the troop
	 * @param soldier The first soldier of the troop
	 */
	public Troop(Player owner, Soldier soldier) {
		this(owner);
		soldiers.add(soldier);
	}
	
	/**
	 * Clone the troop in a new instance
	 */
	public Troop clone(){
		Troop newTroop = new Troop(owner);
		for(int i = 0; i < soldiers.size(); i++){
			newTroop.addSoldier(soldiers.get(0).clone());
		}
		return newTroop;
	}

	public ArrayList<Soldier> getSoldiers(){
		return soldiers;
	}
	public void addSoldier(Soldier soldier) {
		soldiers.add(soldier);
	}

	public Player getOwner() {
		return owner;
	}
	
	public int getAttack(){
		int attack = 0;
		for(Soldier s : soldiers){
			attack += s.getAttack();
		}
		return attack;
	}
	
	public int getDefense(){
		int defense = 0;
		for(Soldier s : soldiers){
			defense += s.getDefense();
		}
		return defense;
	}

	public String toString() {
		return soldiers.toString();
	}

	/**
	 * Decrease the remaining time of production
	 * @return The remaining time of production
	 */
	public int checkCurrentTimeProduct() {
		int productTimeMax = 0;
		for (Soldier s : soldiers) {
			int currentProductTime = s.decreaseProductTime();
			if (currentProductTime > productTimeMax) {
				productTimeMax = currentProductTime;
			}
		}
		return productTimeMax;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((soldiers == null) ? 0 : soldiers.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
	
	

}
