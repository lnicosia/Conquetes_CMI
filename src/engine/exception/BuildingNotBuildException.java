package engine.exception;

/**
 * Exception thrown when a building is not build
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class BuildingNotBuildException extends ActionNotPossibleException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param s The message of error
	 */
	public BuildingNotBuildException(String s) {
		super(s);
	}

}
