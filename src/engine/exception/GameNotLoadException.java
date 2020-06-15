package engine.exception;

/**
 * Exception thrown when the map is not load
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GameNotLoadException extends ActionNotPossibleException{

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param s The message of error
	 */
	public GameNotLoadException(String s) {
		super(s);
	}
}
