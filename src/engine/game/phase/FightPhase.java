package engine.game.phase;

import java.util.ArrayList;
import java.util.HashMap;

import data.player.Player;
import data.territory.Territory;
import data.troop.Troop;
import engine.map.Map;

/**
 * The game phase on fight
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class FightPhase {

	/**
	 * Fight on complete map
	 * @param map The map
	 */
	public FightPhase(Map map) {
		fight(map);
	}
	
	/**
	 * Fight on only one territory
	 * @param t The territory
	 */
	public FightPhase(Territory t){
		fight(t);
	}

	/**
	 * make the fight on the map
	 * @param map The map
	 */
	private void fight(Map map) {
		for (Territory t : map) {
			t.moveTroops();
		}
		
		for (Territory t : map) {
			fight(t);
		}
	}
	
	/**
	 * Make the fight on the territory
	 * @param t The territory
	 */
	private void fight(Territory t){
		if (!t.getTroops().isEmpty()){
			//TODO : Changer pour que ça gère les alliés
			ArrayList<Troop> tempTroops = new ArrayList<Troop>();
			HashMap<Player, Integer> attackOfPlayers = new HashMap<Player, Integer>();
			//HashMap<Player, Integer> defenseOfPlayers = new HashMap<Player, Integer>();
			for(Troop troop : t.getTroops()){
				if(attackOfPlayers.containsKey(troop.getOwner())){
					tempTroops.add(troop);
					attackOfPlayers.put(troop.getOwner(), attackOfPlayers.get(troop.getOwner()) + troop.getAttack());
					//defenseOfPlayers.put(troop.getOwner(), attackOfPlayers.get(troop.getOwner()) + troop.getDefense());
				}
				else{
					tempTroops.add(troop);
					attackOfPlayers.put(troop.getOwner(), troop.getAttack());
					//defenseOfPlayers.put(troop.getOwner(), troop.getDefense());
				}
			}
			int bestAttack = 0;
			Player bestPlayer = null;
			for(Player p : attackOfPlayers.keySet()){
				if(attackOfPlayers.get(p) > bestAttack){
					bestPlayer = p;
					bestAttack = attackOfPlayers.get(p);
				}
				else if(attackOfPlayers.get(p) == bestAttack){
					if(((int) (Math.random() * 2)) == 1){
						bestPlayer = p;
						bestAttack = attackOfPlayers.get(p);
					}
				}
			}
			
			for(Troop troop : tempTroops){
				if(!troop.getOwner().equals(bestPlayer)){
					t.getTroops().remove(troop);
				}
			}
			
			t.setOwner(bestPlayer);
		}
	}

}
