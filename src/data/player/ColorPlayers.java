package data.player;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

/**
 * Singleton which define color of players
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class ColorPlayers {
	
	private static ColorPlayers instance = new ColorPlayers();
	private final HashMap<Integer, Color> playersColor;
	
	/**
	 * 
	 */
	private ColorPlayers(){
		playersColor = new HashMap<Integer, Color>();
		playersColor.put(0, Color.RED);
		playersColor.put(1, Color.BLUE);
		playersColor.put(2, Color.GREEN);
		playersColor.put(3, Color.GRAY);
		playersColor.put(4, Color.YELLOW);
		playersColor.put(5, Color.CYAN);
		playersColor.put(6, Color.PURPLE);
		playersColor.put(7, Color.BLACK);
	}
	
	public static ColorPlayers getInstance(){
		return instance;
	}
	
	/**
	 * @param i The number of player asked
	 * @return the color of player asked
	 */
	public Color getColor(Integer i){
		return playersColor.get(i);
	}

}
