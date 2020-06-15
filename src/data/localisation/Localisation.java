package data.localisation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Singleton class which manage the differents languages.<br />
 * Its use the localisation files located in resources/localisation.<br />
 * The file have to be writed specifically : Each line represent one message in
 * this format : "key:message" <br />
 * Change Language with the method {@link Localisation#setLocalisation(String)}.
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Localisation {

	private static Localisation instance = new Localisation();

	private HashMap<String, String> messages;
	private String language;

	/**
	 * 
	 */
	private Localisation() {
		messages = new HashMap<String, String>();
		language = "en";
		setLocalisation("en");
	}
	
	public static Localisation getInstance(){
		return instance;
	}

	/**
	 * Set the language of the program
	 * 
	 * @param localisation Language use in the program. The String have to correspond to a file in location folder (ex : "location_en.txt" for String "en")
	 */
	public void setLocalisation(String localisation) {
		BufferedReader locationFile;

		try {
			String line;
			String[] informationLine;
			locationFile = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + "resources" + "/" + "localisation" + "/" + "localisation_" + localisation + ".txt"), "UTF-8"));
			while ((line = locationFile.readLine()) != null) {
				if (line.contains(":")) {
					informationLine = line.split(":");
					messages.put(informationLine[0], informationLine[1]);
				}
			}
			locationFile.close();
			language = localisation;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to return the message saved in file which correspond to a given
	 * key
	 * 
	 * @param key
	 *            The key which correspond to message asked
	 * @return the message which correspond to the key
	 */
	public String getMessage(String key) {
		String message = messages.get(key);
		if (message != null) {
			return message;
		} else {
			return "Message key " + key + " not found";
		}
	}
	
	public String getLanguage(){
		return language;
	}

	public Localisation getLocation() {
		return instance;
	}
}
