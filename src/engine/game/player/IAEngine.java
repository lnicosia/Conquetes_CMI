package engine.game.player;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import data.player.Player;
import engine.game.player.ia.IAScript;
import engine.map.Map;

/**
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class IAEngine extends EntityEngine implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private final IAScript script;
	
	/**
	 * 
	 * @param dataEntity The data of this entity
	 */
	@SuppressWarnings("unchecked")
	public IAEngine(Player dataEntity, String difficulty, Map map, ArrayList<EntityEngine> players) throws Exception{
		super(dataEntity);
		Class<? extends IAScript> scriptClass = (Class<? extends IAScript>) Class.forName("engine.game.player.ia.IA" + difficulty);
		Constructor<? extends IAScript> scriptConstructor = scriptClass.getConstructor(new Class[]{EntityEngine.class, Map.class, ArrayList.class});
		script = (IAScript) scriptConstructor.newInstance(new Object[]{this, map, players});
	}

	/**
	 * Launch the AI algorithm
	 */
	public void gestion(){
		script.setGestionPhase(getGestionPhase());
		Thread scriptThread = new Thread(script);
		scriptThread.start();
	}
	
	
}
