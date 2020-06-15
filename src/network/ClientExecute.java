package network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Do the liaison beetween Client and Server (Client)
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class ClientExecute extends Thread{
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private HashMap<String, Object> informations;
	
	private boolean stop;
	
	private final Object key;
	private final Object stopKey = new Object();
	private Integer clientPlayer;
	
	/**
	 * 
	 * @param socket Socket of the server
	 * @param informations Mapping if informations
	 * @param key The key to be synchronized with the client
	 */
	public ClientExecute(Socket socket, HashMap<String, Object> informations, Object key){
		this.socket = socket;
		this.informations = informations;
		this.key = key;
		stop = false;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			clientPlayer = receiveClientPlayer();
			this.start();
		} catch (IOException eIo) {
			clientPlayer = null;
			eIo.printStackTrace();
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			clientPlayer = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * The loop for communicate with the server
	 */
	@Override
	public void run(){
		try {
			while(true){
				synchronized (stopKey) {
					if(stop){
						break;
					}
				}
				while(true){
					try{
						String name = in.readUTF();
						Object o = in.readObject();
						if(name.equals("msg")){
							System.out.println((String) o);
						}
						else{
							synchronized (stopKey) {
								if((name.equals("endofgame")) && ((boolean) o)){
									stop = true;
								}
								synchronized(key){
									informations.put(name, o);
								}
							}
						}
					}
					catch(EOFException e){
						break;
					}
					catch (StackOverflowError e){
						System.err.println("overflow");
					}
				}
			}
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			synchronized (stopKey) {
				stop = true;
			}
		}
	}
	
	public Integer receiveClientPlayer() throws IOException, ClassNotFoundException{
		Integer result = -1;
		result = in.readInt();
		return result;
	}
	
	/**
	 * Send an action to the server
	 * @param list The action 
	 * @throws IOException Thrown if failed
	 */
	public void makeAction(HashMap<String, Object> list) throws IOException{
		out.writeObject(list);
		out.flush();
		out.reset();
	}
	
	/**
	 * Ask the game save to the server
	 * @throws IOException Thrown if failed
	 */
	public void save() throws IOException{
		out.writeObject("save");
	}
	
	public boolean isStopped(){
		synchronized (stopKey) {
			return stop;
		}
	}
	
	public Integer getClientPlayer(){
		return clientPlayer;
	}
}
