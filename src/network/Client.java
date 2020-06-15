package network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import data.localisation.Localisation;
import data.player.Player;
import engine.game.action.Action;
import engine.game.action.EndOfTurnAction;
import engine.game.player.EntityEngine;
import engine.game.player.IAEngine;
import engine.map.Map;

/**
 * Give game informations received from the server to the GUI
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Client{
	
	private Socket socket;
	private ClientExecute server;
	private boolean separated;
	private int currentPlayer;
	
	private boolean stopClient;
	
	private Object stopKey = new Object();
	private final Object key = new Object();
	private HashMap<String, Object> informations;
	
	/**
	 * 
	 * @param serverName IP of server
	 * @param port Port of server
	 * @param clientPlayer Which player is this client
	 * @throws IOException If the connexion failed
	 */
	public Client(String serverName, int port, boolean separated) throws IOException{
		this.stopClient = false;
		this.socket = new Socket(serverName, port);
		this.informations = new HashMap<String, Object>();
		this.separated = separated;
		server = new ClientExecute(socket, this.informations, key);
		currentPlayer = 0;
	}
	
	/**
	 * Send an action to the server
	 * @param action The action send
	 */
	public void makeAction(Action action){
		HashMap<String, Object> list = new HashMap<String, Object>();
		list.put("player", getPlayers().indexOf(getCurrentPlayer()));
		list.put("action", action);
		try {
			server.makeAction(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(action.getClass().equals(EndOfTurnAction.class) && !separated){
			do{
				if(currentPlayer >= (getPlayers().size() - 1)){
					currentPlayer = 0;
					
				}
				else{
					currentPlayer++;
				}
			}while(getPlayers().get(currentPlayer).getClass().equals(IAEngine.class));
		}
	}
	
	/**
	 * Save the game
	 * @throws IOException If the save failed
	 */
	public void save() throws IOException{
		server.save();
		System.out.println(Localisation.getInstance().getMessage("Save Ok"));
	}
	
	/**
	 * 
	 * @return If the client is stopped
	 */
	public boolean clientStopped(){
		synchronized(stopKey){
			return stopClient;
		}
	}
	
	/**
	 * Stop the client
	 */
	public void stopClient(){
		synchronized(stopKey){
			stopClient = true;
		}
	}
	
	public Map getMap(){
		synchronized(key){
			return (Map) informations.get("map");
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<EntityEngine> getPlayers(){
		synchronized(key){
			return (ArrayList<EntityEngine>) informations.get("players");
		}
	}
	
	public ArrayList<Player> getPlayersData(){
		ArrayList<Player> playersData = new ArrayList<Player>();
		for(EntityEngine ee : getPlayers()){
			playersData.add(ee.getDataEntity());
		}
		return playersData;
	}
	
	public EntityEngine getCurrentPlayer(){
		if(separated){
			return getPlayers().get(server.getClientPlayer());
		}
		else{
			return getPlayers().get(currentPlayer);
		}
	}
	
	public boolean isLastPlayerOnClient(){
		if(separated){
			return true;
		}
		else{
			if(getPlayers().get(currentPlayer).equals(getPlayers().get(getPlayers().size() - 1 - getNbIa()))){
				return true;
			}
			else{
				return false;
			}
		}
	}
	
	public Integer getNbIa(){
		synchronized(key){
			return (Integer) informations.get("nbia");
		}
	}
	
	public Integer getTurn(){
		synchronized(key){
			return (Integer) informations.get("turn");
		}
	}
	
	public boolean getEndOfTurn(){
		synchronized(key){
			if((boolean) informations.get("endofturn")){
				informations.put("endofturn", false);
				return true;
			}
			else{
				return false;
			}
		}
	}
	
	/**
	 * 
	 * @return If online (one screen per player)
	 */
	public boolean isSeparated(){
		return separated;
	}
	
	public boolean getEndOfGame(){
		synchronized(key){
			return (boolean) informations.get("endofgame");
		}
	}

}
