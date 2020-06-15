package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import engine.exception.GameNotSaveException;
import engine.game.GameLoop;
import engine.game.GameStockage;

/**
 * Send informations of the game to all connected clients
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Server extends Thread{
	
	private ServerSocket ear;
	private boolean stopServer;
	private GameLoop game;
	private ArrayList<ServerExecute> clients;
	
	private String stopKey = new String();
	
	private int nbPlayer;
	
	/**
	 * 
	 * @param port The port of the server
	 * @param game The game loop
	 * @param nbPlayer The number of player
	 * @throws IOException Thrown if the server creation failed
	 */
	public Server(int port, GameLoop game, int nbPlayer) throws IOException{
		this.stopServer = false;
		this.ear = new ServerSocket(port);
		this.game = game;
		this.clients = new ArrayList<ServerExecute>();
		this.nbPlayer = nbPlayer;
	}
	
	/**
	 * Check if new informations must be send to clients
	 */
	@Override
	public void run(){
		Thread gameThread = new Thread(game);
		boolean begin = true;
		gameThread.start();
		try {
			int nbClient = 0;
			while(nbClient < nbPlayer){
				Socket currentClient = ear.accept();
				clients.add(new ServerExecute(currentClient, game, nbClient));
				nbClient++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			stopServer();
		}
		while(!serverStopped()){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				
			}
			if(game.getEndOfGame()){
				updateInformations(!begin);
				stopServer();
			}
			else if(game.isTurnPassed()){
				updateInformations(!begin);
			}
			
			boolean save = false;
			for(ServerExecute se : clients){
				if(se.askSave()){
					save = true;
				}
			}
			if(save){
				save();
			}
			begin = false;
		}
		for(ServerExecute se : clients){
			se.stopServer();
			try {
				ear.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Save the game loop
	 */
	public void save(){
		try {
			game.suspendLoop();
			GameStockage.getInstance().save(game);
			game.resumeLoop();
		} catch (GameNotSaveException e) {
			System.err.println(e.getMessage());
			game.resumeLoop();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			game.resumeLoop();
		}
	}
	
	/**
	 * Send new informations to clients
	 * @param endOfTurn If it's the end of turn
	 */
	private void updateInformations(boolean endOfTurn){
		HashMap<String, Object> list = new HashMap<String, Object>();
		list.put("map", game.getGameInitialisator().getMap());
		list.put("players", game.getPlayers());
		list.put("turn", game.getTurn());
		list.put("endofgame", game.getEndOfGame());
		list.put("endofturn", endOfTurn);
		list.put("nbia", game.getNbIa());
		for(ServerExecute currentClient : clients){
			try {
				currentClient.updateInformations(list);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Impossible d'envoyer les informations");
			}
		}
	}
	
	/**
	 * 
	 * @return If the server is stopped
	 */
	public boolean serverStopped(){
		synchronized(stopKey){
			return stopServer;
		}
	}
	
	/**
	 * Stop the server
	 */
	public void stopServer(){
		for(ServerExecute se : clients){
			se.stopServer();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized(stopKey){
			stopServer = true;
		}
	}
	
	/**
	 * Remove a client
	 * @param client The client to remove
	 */
	public void removeClient(int i){
		ServerExecute client = clients.get(i);
		client.stopServer();
		clients.remove(client);
	}
	
}
