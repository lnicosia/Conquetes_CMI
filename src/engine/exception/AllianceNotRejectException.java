package engine.exception;

/**
 * Exception thrown when a alliance is not reject
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class AllianceNotRejectException extends ActionNotPossibleException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param s The message of error
	 */
	public AllianceNotRejectException(String s) {
		super(s);
	}

}