package graphic.map;

import network.Client;

import com.badlogic.gdx.Gdx;

/**
 * Thread which actualise the map after receiving new informations
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class ActualiseMap extends Thread{
	
	private GenerateGraphicMap ggm;
	private Client client;
	
	/**
	 * 
	 * @param ggm The graphic representation of the map
	 * @param client The client which receive informations
	 */
	public ActualiseMap(GenerateGraphicMap ggm, Client client){
		this.ggm = ggm;
		this.client = client;
	}
	
	@Override
	public void run(){
		while(!client.getEndOfTurn()){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				
			}
		}
		ggm.setNewMap(client.getMap());
		Gdx.graphics.requestRendering();
	}

}
