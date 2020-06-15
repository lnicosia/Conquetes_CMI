package engine.exception;

/**
 * Exception thrown when a alliance is not possible
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class AllianceNotPossibleException extends ActionNotPossibleException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param s The message of error
	 */
	public AllianceNotPossibleException(String s) {
		super(s);
	}

}
