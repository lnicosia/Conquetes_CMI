package engine.exception;

/**
 * Exception thrown when the map is not save
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GameNotSaveException extends ActionNotPossibleException{

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param s The message of error
	 */
	public GameNotSaveException(String s) {
		super(s);
	}
}
