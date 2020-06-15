package engine.exception;

/**
 * Exception thrown when the troop is not build
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class TroopNotBuildException extends ActionNotPossibleException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param s The message of error
	 */
	public TroopNotBuildException(String s) {
		super(s);
	}

}
