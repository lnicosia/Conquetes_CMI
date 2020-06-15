package engine.exception;

/**
 * Exception thrown when a alliance is not accept
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class AllianceNotAcceptException extends ActionNotPossibleException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param s The message of error
	 */
	public AllianceNotAcceptException(String s) {
		super(s);
	}

}
