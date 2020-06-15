package engine.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.localisation.Localisation;
import engine.exception.GameNotLoadException;
import engine.exception.GameNotSaveException;
import engine.game.player.EntityEngine;
import engine.map.Map;

/**
 * Save/Load a game/map (Singleton)
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GameStockage {
	
	private static GameStockage instance = new GameStockage();
	
	private String path;
	private String mapPath;
	
	/**
	 * 
	 */
	private GameStockage(){
		path = "save" + File.separator;
		mapPath = "maps" + File.separator;
	}
	
	public static GameStockage getInstance(){
		return instance;
	}
	
	/**
	 * Save the game
	 * @param game The loop of the game to save
	 * @throws GameNotSaveException Thrown if the file already exist
	 * @throws IOException Thrown if there are other problem with the file
	 */
	public void save(GameLoop game) throws GameNotSaveException, IOException{
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM.dd.HH.mm");
		String texte_date = sdf.format(new Date());
		File checkFile = new File(path + texte_date + ".save");
		if(checkFile.isDirectory()){
			throw new IOException(Localisation.getInstance().getMessage("Is Directory"));
		}
		if(checkFile.exists()){
			throw new GameNotSaveException(Localisation.getInstance().getMessage("File Already Exist"));
		}
		System.out.println( path + texte_date + ".save");
		FileOutputStream fos= new FileOutputStream(path + texte_date +".save");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(game);
		oos.close();
		fos.close();
		
	}
	
	/**
	 * Save a map
	 * @param map The map to save
	 * @throws GameNotSaveException Thrown if the file already exist
	 * @throws IOException Thrown if there are other problem with the file
	 */
	public void save(Map map, ArrayList<EntityEngine> players) throws GameNotSaveException, IOException{
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM.dd.HH.mm");
		String texte_date = sdf.format(new Date());
		File checkFile = new File(mapPath + texte_date + ".map");
		if(checkFile.isDirectory()){
			throw new IOException(Localisation.getInstance().getMessage("Is Directory"));
		}
		if(checkFile.exists()){
			throw new GameNotSaveException(Localisation.getInstance().getMessage("File Already Exist"));
		}
		System.out.println( mapPath + texte_date + ".map");
		FileOutputStream fos= new FileOutputStream(mapPath + texte_date +".map");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(map);
		oos.writeObject(players);
		oos.close();
		fos.close();
		
	}
	
	/**
	 * Load a game
	 * @param fileName The name of file to load
	 * @return The game loaded
	 * @throws GameNotLoadException Thrown if the file don't exist
	 * @throws IOException Thrown if there are other problem with the file
	 */
	public GameLoop load(String fileName)throws GameNotLoadException, IOException{
		File checkFile = new File(path + fileName);
		GameLoop game;
		if(checkFile.isDirectory()){
			throw new IOException(Localisation.getInstance().getMessage("Is Directory"));
		}
		if(!checkFile.exists()){
			throw new GameNotLoadException(Localisation.getInstance().getMessage("File Dont Exist"));
		}
		FileInputStream fis = new FileInputStream(path + fileName);
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		try {
			game = (GameLoop) ois.readObject();
		} catch (ClassNotFoundException e) {
			ois.close();
			fis.close();
			throw new GameNotLoadException(Localisation.getInstance().getMessage("Bad Loading"));
		}
		
		ois.close();
		fis.close();
		return game;
	}
	
	public GameInitializator loadMap(String fileName)throws GameNotLoadException, IOException{
		File checkFile = new File(mapPath + fileName);
		Map map;
		ArrayList<EntityEngine> players;
		System.err.println(checkFile);
		if(checkFile.isDirectory()){
			throw new IOException(Localisation.getInstance().getMessage("Is Directory"));
		}
		if(!checkFile.exists()){
			throw new GameNotLoadException(Localisation.getInstance().getMessage("File Dont Exist"));
		}
		FileInputStream fis = new FileInputStream(mapPath + fileName);
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		try {
			map = (Map) ois.readObject();
			players = (ArrayList<EntityEngine>) ois.readObject();
		} catch (ClassNotFoundException e) {
			ois.close();
			fis.close();
			throw new GameNotLoadException(Localisation.getInstance().getMessage("Bad Loading"));
		}
		
		ois.close();
		fis.close();
		GameInitializator gi = new GameInitializator(map, players);
		return gi;
	}
	
	/**
	 * Set the path to folder of stockage
	 * @param path The new path
	 * @throws FileNotFoundException Thrown if the file don't exist
	 */
	public void setPath(String path) throws FileNotFoundException{
		File checkDirectory = new File(path);
		if(!checkDirectory.exists()){
			checkDirectory.mkdir();
		}
		else if(checkDirectory.isDirectory()){
			this.path = path;
		}
		else{
			throw new FileNotFoundException(path + " " + Localisation.getInstance().getMessage("Not Directory"));
		}
	}

}
