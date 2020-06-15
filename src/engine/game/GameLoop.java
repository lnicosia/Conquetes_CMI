package engine.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import data.player.Player;
import data.territory.Territory;
import data.territory.building.DefenseIncrease;
import data.territory.building.ResourceIncrease;
import data.territory.building.TroopIncrease;
import data.troop.Troop;
import engine.game.phase.FightPhase;
import engine.game.phase.GestionPhase;
import engine.game.player.EntityEngine;
import engine.game.player.IAEngine;
import engine.map.Map;

/**
 * The main loop of the game
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GameLoop implements Runnable, Serializable {

	private static final long serialVersionUID = 1L;
	private GameInitializator gi;
	private ArrayList<Integer> turnOrder;
	private int turn;
	private int indexPlayer;
	private boolean endOfGame;
	private boolean loop;
	private boolean isTurnPassed;
	private boolean suspendedLoop;

	private final String keySuspendedLoop = new String();
	private final String keySuspendAll = new String();
	private final String key = new String();
	private final String loopKey = new String();
	private final String turnKey = new String();

	/**
	 * 
	 * @param gi
	 *            The corresponding GameInitializator
	 */
	public GameLoop(GameInitializator gi) {
		this.gi = gi;
		this.turnOrder = gi.getTurnOrder();
		this.turn = 0;
		endOfGame = false;
		loop = true;
		isTurnPassed = false;
		indexPlayer = 0;
		suspendedLoop = false;
	}

	/**
	 * Action on each begin loop
	 * 
	 * @param map
	 *            The map of the game
	 */
	public void makeBeginingAction(Map map) {
		ArrayList<Player> lostPlayers = new ArrayList<Player>();
		for (EntityEngine entity : getPlayers()) {
			lostPlayers.add(entity.getDataEntity());
		}
		for (Territory t : map) {
			if (t.getBuilding() != null) {
				if (t.getBuilding() instanceof TroopIncrease) {
					TroopIncrease currentBuilding = (TroopIncrease) t.getBuilding();
					Troop newTroop = currentBuilding.checkTroop();
					if (newTroop != null) {
						t.addTroop(newTroop);
					}
				}
				if (t.getBuilding() instanceof ResourceIncrease) {
					ResourceIncrease currentBuilding = (ResourceIncrease) t.getBuilding();
					for (Integer i : t.getResources().keySet()) {
						if (t.getOwner() != null) {
							t.getOwner().increaseResource(i, t.getResources().get(i).product(currentBuilding.getResourceIncrease(i)));
						}
					}
				}
				if (t.isCapital()) {
					lostPlayers.remove(t.getOwner());
				}
			} else {
				t.checkBuildingInConstruction();
				for (Integer i : t.getResources().keySet()) {
					if (t.getOwner() != null) {
						t.getOwner().increaseResource(i, t.getResources().get(i).product(0));
					}
				}
			}
			if (!t.getTroops().isEmpty()) {
				if (t.getOwner() == null) {
					t.setOwner(chooseOwner(t.getTroops()));
				} else {
					if ((!t.getOwner().equals(t.getTroops().get(0).getOwner())) || (t.getOwner().getAllies().contains(t.getTroops().get(0).getOwner()))) {
						t.setOwner(chooseOwner(t.getTroops()));
					}
				}
			}
		}

		if (!lostPlayers.isEmpty()) {
			for (Territory t : map) {
				if (lostPlayers.contains(t.getOwner())) {
					t.cancelConstruction();
					if (!(t.getBuilding() instanceof DefenseIncrease)) {
						t.setBuilding(null);
					}
					t.setOwner(null);
				}
			}
			ArrayList<EntityEngine> lostEntity = new ArrayList<EntityEngine>();
			for (EntityEngine entity : getPlayers()) {
				if (lostPlayers.contains(entity.getDataEntity())) {
					lostEntity.add(entity);
				}
			}
			for (EntityEngine entity : lostEntity) {
				getPlayers().remove(entity);
			}
			if (getPlayers().size() == 1) {
				endOfGame(getPlayers().get(0));
			}
		}
	}

	/**
	 * The loop of the game
	 */
	@Override
	public void run() {
		while (true) {
			makeBeginingAction(gi.getMap());
			synchronized (turnKey) {
				isTurnPassed = true;
			}
			if (isEndOfLoop()) {
				break;
			}
			for (EntityEngine entity : getPlayers()) {
				GestionPhase gp = new GestionPhase(gi.getMap(), keySuspendAll);
				entity.setGestionPhase(gp);
				if (entity.getClass().equals(IAEngine.class)) {
					((IAEngine) (entity)).gestion();
				}
			}
			for (EntityEngine entity : getPlayers()) {
				while (!entity.getGestionPhase().getEndOfTurn()) {
					synchronized (keySuspendAll) {
						while (true) {
							synchronized (keySuspendedLoop) {
								if (!suspendedLoop) {
									break;
								}
								try {
									Thread.sleep(20);
								} catch (InterruptedException e) {

								}
							}
						}
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {

					}
				}
			}
			new FightPhase(gi.getMap());
			turn++;
		}

	}

	/**
	 * Stop the loop
	 */
	public void suspendLoop() {
		synchronized (keySuspendedLoop) {
			suspendedLoop = true;
		}
	}

	/**
	 * Resume the loop
	 */
	public void resumeLoop() {
		synchronized (keySuspendedLoop) {
			suspendedLoop = false;
		}
	}

	/**
	 * Choose the owner if two allies is on territory
	 * 
	 * @param troops
	 *            List of troops in the territory
	 * @return The owner
	 */
	private Player chooseOwner(ArrayList<Troop> troops) {
		HashMap<Player, Integer> powerOfOwners = new HashMap<Player, Integer>();
		for (Troop troop : troops) {
			if (powerOfOwners.containsKey(troop.getOwner())) {
				powerOfOwners.put(troop.getOwner(), powerOfOwners.get(troop.getOwner()) + 1);
			} else {
				powerOfOwners.put(troop.getOwner(), 1);
			}
		}
		Player realOwner = null;
		int valueOfRealOwner = 0;
		for (Player p : powerOfOwners.keySet()) {
			if (powerOfOwners.get(p) > valueOfRealOwner) {
				realOwner = p;
				valueOfRealOwner = powerOfOwners.get(p);
			}
		}
		return realOwner;
	}

	public boolean getEndOfGame() {
		synchronized (key) {
			return endOfGame;
		}
	}

	/**
	 * Set the endOfGame
	 * 
	 * @param entityEngine
	 *            The winner
	 */
	public synchronized void endOfGame(EntityEngine entityEngine) {
		endOfGame = true;
		if (entityEngine != null) {
			System.err.println(entityEngine.getDataEntity().toString() + " gagne la partie !");
		} else {
			System.err.println("personne ne gagne la partie");
		}
		endOfLoop();
	}

	/**
	 * 
	 * @return If the turn has passed
	 */
	public boolean isTurnPassed() {
		synchronized (turnKey) {
			if (isTurnPassed) {
				isTurnPassed = false;
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 
	 * @return If this is the end of the loop
	 */
	public boolean isEndOfLoop() {
		synchronized (loopKey) {
			return !loop;
		}
	}

	/**
	 * Check the end of the loop
	 */
	public void endOfLoop() {
		synchronized (loopKey) {
			loop = false;
		}
	}

	public int getNbIa() {
		int nbIa = 0;
		for (EntityEngine ee : gi.getPlayers()) {
			if (ee.getClass().equals(IAEngine.class)) {
				nbIa++;
			}
		}
		return nbIa;
	}

	public int getTurn() {
		return turn;
	}

	public ArrayList<EntityEngine> getPlayers() {
		return gi.getPlayers();
	}

	public GameInitializator getGameInitialisator() {
		return gi;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (endOfGame ? 1231 : 1237);
		result = prime * result + ((gi == null) ? 0 : gi.hashCode());
		result = prime * result + indexPlayer;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + (loop ? 1231 : 1237);
		result = prime * result + turn;
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
		GameLoop other = (GameLoop) obj;
		if (endOfGame != other.endOfGame)
			return false;
		if (gi == null) {
			if (other.gi != null)
				return false;
		} else if (!gi.equals(other.gi))
			return false;
		if (indexPlayer != other.indexPlayer)
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (loop != other.loop)
			return false;
		if (turn != other.turn)
			return false;
		if (turnOrder == null) {
			if (other.turnOrder != null)
				return false;
		} else if (!turnOrder.equals(other.turnOrder))
			return false;
		return true;
	}

}
