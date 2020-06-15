package engine.exception;

/**
 * Exception thrown when a alliance is not destroy
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class AllianceNotDestroyException extends ActionNotPossibleException{

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param s The message of error
	 */
	public AllianceNotDestroyException(String s) {
		super(s);
	}

}
