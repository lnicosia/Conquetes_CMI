package engine.exception;

/**
 * Exception thrown when any path has found
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class NoPathFindException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param s The message of error
	 */
	public NoPathFindException(String s) {
		super(s);
	}

}
