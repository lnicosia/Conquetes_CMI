package network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import data.player.Player;
import engine.exception.ActionNotPossibleException;
import engine.exception.EndOfTurnException;
import engine.game.GameLoop;
import engine.game.action.Action;
import engine.game.player.EntityEngine;
import engine.game.player.PlayerEngine;

/**
 * Do the liaison beetween Client and Server (Server)
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class ServerExecute extends Thread{
	
	private Socket socket;
	ObjectInputStream in;
	private ObjectOutputStream out;
	private GameLoop game;
	boolean stop = false;
	boolean save = false;
	
	private final String key = new String();
	private final String keySave = new String();
	
	/**
	 * 
	 * @param socket The server socket
	 * @param game The game loop
	 */
	public ServerExecute(Socket socket, GameLoop game, int clientNumber){
		this.socket = socket;
		this.game = game;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeInt(clientNumber);
			out.flush();
			out.reset();
			this.start();
		} catch (IOException eIo) {
			eIo.printStackTrace();
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * The loop for communicate with client
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run(){
		try{
			in = new ObjectInputStream(socket.getInputStream());
			while(true){
				synchronized (key) {
					if(stop){
						break;
					}
				}
				Object o = in.readObject();
				if(o.getClass().equals(String.class)){
					String msg = (String) o;
					if(msg.equals("save")){
						synchronized (keySave) {
							save = true;
						}
					}
				}
				else{
					HashMap<String, Object> list = (HashMap<String, Object>) o;
					Integer currentPlayer = (Integer) list.get("player");
					Action action = (Action) list.get("action");
					PlayerEngine player = (PlayerEngine) game.getPlayers().get(currentPlayer);
					ArrayList<Player> players = new ArrayList<Player>();
					for(EntityEngine ee : game.getPlayers()){
						players.add(ee.getDataEntity());
					}
					String msg = "";
					try {
						msg = player.makeAction(action, game.getGameInitialisator().getMap(), players);
					} catch(EndOfTurnException e){
						player.endOfGestionTurn();
					} catch (ActionNotPossibleException e) {
						msg = e.getMessage();
					}
					synchronized (key) {
						out.writeUTF("msg");
						out.writeObject(msg);
						out.flush();
						out.reset();
					}
				}
			}
		} catch (EOFException e){
			
		} catch (SocketException e){
			stop = true;
		} catch (Exception e) {
			try {
				e.printStackTrace();
				socket.close();
				stop = true;
			} catch (IOException eClose) {
				eClose.printStackTrace();
				stop = true;
			}
		}
	}
	
	/**
	 * 
	 * @return If the save has asked
	 */
	public boolean askSave(){
		synchronized (keySave) {
			if(save){
				save = false;
				return true;
			}
			else{
				return false;
			}
		}
	}
	
	/**
	 * Stop the server
	 */
	public void stopServer(){
		synchronized (key) {
			stop = true;
		}
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Send new informations to the client
	 * @param list The informations
	 * @throws IOException Thrown if failed
	 */
	public void updateInformations(HashMap<String, Object> list) throws IOException{
		synchronized (key) {
			sendInfo("map", list.get("map"));
			sendInfo("players", list.get("players"));
			sendInfo("turn", list.get("turn"));
			sendInfo("endofgame", list.get("endofgame"));
			sendInfo("endofturn", list.get("endofturn"));
			sendInfo("nbia", list.get("nbia"));
			out.flush();
			out.reset();
		}
	}
	
	private void sendInfo(String name, Object info) throws IOException{
		out.writeUTF(name);
		out.writeObject(info);
	}
}
