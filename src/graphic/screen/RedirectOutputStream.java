package graphic.screen;

import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Class that redirect error messages flux to window
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class RedirectOutputStream extends PrintStream {

	private GuiStream outComponent;
	
	public RedirectOutputStream(GuiStream screen) throws Exception {
		super(new FileOutputStream("./.flux"), true, "ISO-8859-1");
		outComponent = screen;
	}
	
	/**
	 * Print a String in this screen
	 * @param message The message that wiil be send
	 */
	@Override
	public void println(String message) {
		outComponent.setErrorMessage(message);
	}

	@Override
	public String toString() {
		return "RedirectErrStream [outComponent=" + outComponent + "]";
	}
	
	

}