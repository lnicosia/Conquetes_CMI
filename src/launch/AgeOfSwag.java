package launch;

import graphic.screen.GuiScreen;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Main class of the project
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class AgeOfSwag {
	public static void main(String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// config.width=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		// Apporte le plein  écran en fonction de la résolution de celui-ci
		// config.height=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		config.width = (int) GuiScreen.standartWidthDefiniton;
		config.height = (int) GuiScreen.standartHeightDefiniton;
		config.addIcon("resources/images/icons/icon16.png", Files.FileType.Internal);
		config.title = "Ages of Swag : Conquest of CMI";
		new LwjglApplication(new GuiScreen(), config);

	}
}
