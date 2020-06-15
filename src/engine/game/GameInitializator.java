package engine.game;

import java.io.Serializable;
import java.util.ArrayList;

import data.player.Player;
import data.territory.Territory;
import engine.game.player.EntityEngine;
import engine.game.player.IAEngine;
import engine.game.player.PlayerEngine;
import engine.map.Map;
import engine.map.MapIterator;
import engine.map.MapProcedural;
import engine.map.MapRandom;
import engine.map.Position;

/**
 * Initialize the data of the game
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GameInitializator implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<EntityEngine> players;
	private int nbIa;
	private Map map;
	private ArrayList<Integer> turnOrder;
	
	/**
	 * Without AI
	 * @param mode The mode asked for the game ("Normal", "Random")
	 * @param production The production 
	 * @param playersNb The number of player
	 */
	public GameInitializator(String mode, int production, int playersNb) {
		this(mode, production, playersNb, new ArrayList<String>());
	}

	/**
	 * With AI
	 * @param mode The mode asked for the game ("Normal", "Random")
	 * @param production The production 
	 * @param playersNb The number of player
	 * @param iaNames The list of type of AI
	 */
	public GameInitializator(String mode, int production, int playersNb, ArrayList<String> iaNames) {
		
		nbIa = iaNames.size();

		if (mode.equals("random")) {

			// Map generation
			int territoryNb = (int) (Math.random() * (playersNb * 3) + (playersNb * 3));
			System.err.println("Nombre de territoires de la map : " + territoryNb * territoryNb);
			map = new MapRandom(territoryNb);
			System.err.println("Carte aléatoire initialisée !");
			int turn = 0;
			// Player and capitals generation
			players = new ArrayList<EntityEngine>();
			turnOrder = new ArrayList<Integer>();
			
			for (int i = 0; i < playersNb; i++) {

				do {
					turn = (int) (Math.random() * playersNb);
				} while ((turnOrder.contains(turn)));
				turnOrder.add(turn);
				
				EntityEngine player;
				if(i < (playersNb - nbIa)){
					player = new PlayerEngine(new Player("Joueur " + (i + 1), i));
				}
				else{
					try {
						player = new IAEngine(new Player("IA " + ((i - playersNb + nbIa ) + 1 ), i), iaNames.get(i - playersNb + nbIa ), map, players);
					} catch (Exception e) {
						System.err.println("IA not created");
						player = null;
					}
				}
				players.add(player);
				
			}
			
			placeCapital(territoryNb / players.size());
			
			/*ArrayList<EntityEngine> ancientOrder = (ArrayList<EntityEngine>) players.clone();
			players.clear();
			for(Integer i : turnOrder){
				players.add(ancientOrder.get(i));
			}*/

			// Information printing
			System.err.println("Mode : " + mode + ". Production : " + production + ". Nombre de joueurs : " + playersNb + ".");
			System.err.println(turnOrder);

		}

		else if (mode.equals("normal")) {
			
			// Map generation (Map Random for the moment)
			int territoryNb = (int) (Math.random() * (playersNb * 3) + (playersNb * 3));
			System.err.println("Nombre de territoires de la map : " + territoryNb * territoryNb);
			map = new MapProcedural(territoryNb, territoryNb, 6, 10);
			System.err.println("Carte aléatoire initialisée !");
			int turn = 0;
			// Player and capitals generation
			players = new ArrayList<EntityEngine>();
			turnOrder = new ArrayList<Integer>();
			for (int i = 0; i < playersNb; i++) {
				
				do {
					turn = (int) (Math.random() * playersNb);
				} while ((turnOrder.contains(turn)));
				turnOrder.add(turn);
				
				EntityEngine player;
				if(i < (playersNb - nbIa)){
					player = new PlayerEngine(new Player("Joueur " + (i + 1), i));
				}
				else{
					try {
						player = new IAEngine(new Player("IA " + ((i - playersNb + nbIa ) + 1 ), i), iaNames.get(i - playersNb + nbIa ), map, players);
					} catch (Exception e) {
						System.err.println("IA not created");
						player = null;
					}
				}
				players.add(player);
				
			}
			
			placeCapital(territoryNb / players.size());

			// Information printing
			System.err.println("Mode : " + mode + ". Production : " + production + ". Nombre de joueurs : " + playersNb + ".");
			System.err.println(turnOrder);
		}

	}
	
public GameInitializator(Map map, ArrayList<EntityEngine> players) {
		
		this.map = map;
		this.players = players;
		nbIa = 0;
		turnOrder = new ArrayList<Integer>();
		int i = 0;
		for(EntityEngine ee : players){
			turnOrder.add(i);
			if(ee.getClass().equals(IAEngine.class)){
				nbIa++;
			}
			i++;
		}

	}
	
	/**
	 * Place the capitals on the map
	 * @param min The range minimum between capitals
	 */
	private void placeCapital(int min){
		ArrayList<Position> randomNumbers = new ArrayList<Position>();
		Integer random;
		Position pos = null;
		for(EntityEngine player : players){
			boolean placed = false;
			while(!placed){
				// Random number used for placing capital on a random territory
				boolean goodNumber;
				do {
					goodNumber = true;
					random = (int) (Math.random() * (map.getTerritoryNb()));
					pos = new Position(map.getPositionX(random), map.getPositionY(random));
					for(Position otherPos : randomNumbers){
						if(pos.getRange(otherPos) < min){
							goodNumber = false;
						}
					}
				} while (!goodNumber);
				MapIterator it = (MapIterator) map.iterator();
	
				// Parcour des territoires afin d'y placer la capitale créée
				while (it.hasNext()) {
					Territory t = it.next();
					if (it.getPosition() == random) {
						if(!t.isCrossable()){
							break;
						}
						else{
							t.defineCapital();
							t.setOwner(player.getDataEntity());
							//Si le territoire est le premier on le prend en tant que base
							if(it.getPosition() == 0){
								map.setBaseTerritory(t);
							}
							placed = true;
							break;
						}
					}
				}
			}
			randomNumbers.add(pos);
		}
	}
	
	public int getNbIa(){
		return nbIa;
	}

	public Map getMap() {
		return map;
	}

	public ArrayList<EntityEngine> getPlayers() {
		return players;
	}

	public ArrayList<Integer> getTurnOrder() {
		return turnOrder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + ((players == null) ? 0 : players.hashCode());
		result = prime * result + ((turnOrder == null) ? 0 : turnOrder.hashCode());
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
		GameInitializator other = (GameInitializator) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		if (players == null) {
			if (other.players != null)
				return false;
		} else if (!players.equals(other.players))
			return false;
		if (turnOrder == null) {
			if (other.turnOrder != null)
				return false;
		} else if (!turnOrder.equals(other.turnOrder))
			return false;
		return true;
	}
	
	
}
