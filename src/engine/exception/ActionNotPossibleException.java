package engine.exception;

/**
 * Exception thrown when a action is not possible
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class ActionNotPossibleException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param s The message of error
	 */
	public ActionNotPossibleException(String s) {
		super(s);
	}

}
