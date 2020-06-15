package engine.exception;

/**
 * Exception thrown when it is the end of turn
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class EndOfTurnException extends ActionNotPossibleException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param s The message of error
	 */
	public EndOfTurnException(String s) {
		super(s);
	}

}
